// pages/historyList/historyList.js
import {getHistoryList,getCollectCount} from "@/api/historyList";

import {baseURL} from "@/utils/apiUrl"
import { handleImageUrl } from '../../utils/util';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    baseURL:baseURL,
    typeOptions: [
      { value: null, label: '全部' },
      { value: '1', label: '禁用词类' },
      { value: '2', label: '虚假宣传' },
      { value: '3', label: '低俗类' },
      { value: '4', label: '敏感类' },
      { value: '5', label: '其他类' }
    ],
    violationType: null,
    auditStatus: '0', // 新增状态变量，默认为'all'
    // 修改: 添加刷新和加载状态
    refreshing: false,
    loadingMore: false,
    historyList: [
      {
        id: 1,
        title: '虚假宣传',
        date: '2025年5月16日',
        location: '河北省石家庄市桥西区23街道',
        image: '/static/images/approval.png'
      }
    ],
    showConfirm:false,
    pageNum:1,
    pageSize:6,
    total:0,
    userId:wx.getStorageSync('userId'),
    nickName:wx.getStorageSync('nickName'),
    phonenumber:wx.getStorageSync('phonenumber'),
    count:0,
    handle:0
  },

  getCollectData(){
    getCollectCount().then(res=>{
      this.setData({
        count:res.data.count,
        handle:res.data.handle
      })
    })
  },
  getData(){
    // 初始化数据
    this.setData({
      userId:wx.getStorageSync('userId'),
      nickName:wx.getStorageSync('nickName'),
      phonenumber:wx.getStorageSync('phonenumber'),
      violationType: null,
      pageNum: 1,      //重置页码为第一页
      auditStatus: '0' // 修改: 默认展示所有记录
    });
    // 修改: 在中调用getHistoryListData获取数据
    this.getHistoryListData();
  },
  onLoad() {
    this.getData()
  },
  onShow(){
    this.getData()

    this.getCollectData()
  },
  //获取上传历史列表
  getHistoryListData(){
    let { violationType, auditStatus, userId, pageNum, pageSize } = this.data;
    // 添加return语句，返回Promise
    return getHistoryList({
      violationType,
      auditStatus,
      userId,
      pageNum,
      pageSize
    }).then(res=>{
      console.log(res);
      // 处理图片路径
      const processedRows = res.rows.map(item => {
        // 如果有adImages字段，处理图片路径
        if (item.adImages) {
          item.processedAdImage = handleImageUrl(item.adImages);
        } else {
          // 没有图片使用默认图片
          item.processedAdImage = '/static/images/approval.png';
        }
        return item;
      });
      
      this.setData({
        historyList: processedRows,
        total: res.total
      });
    });
  },
  //点击头像退出登录
  onClickAvatar(){
    this.setData({
      showConfirm:true
    })
  },
  // 取消退出登录
  closeDialog(){
    this.setData({
      showConfirm:false
    })
  },
  // 确定退出
  confirmQuit(){
    // 清除缓存
    wx.removeStorageSync('token');
    wx.removeStorageSync('userId');
    wx.removeStorageSync('nickName');
    wx.removeStorageSync('username');
    wx.removeStorageSync('phonenumber');
    wx.removeStorageSync('roleKey');
    wx.removeStorageSync('roleName');


    // 跳转回登录页
    wx.reLaunch({
      url: '/pages/login/login'
    })
  },

  // 修改: 实现下拉刷新功能
  onPullDownRefresh() {
    if (this.data.refreshing) return;
    
    this.setData({
      refreshing: true,
      pageNum: 1  // 重置页码为第一页
    });
    // 刷新数据
    this.getHistoryListData()
      .then(() => {
        // 请求成功，停止下拉刷新
        wx.stopPullDownRefresh();
        this.setData({
          refreshing: false
        });
      })
      .catch((error) => {
        // 请求失败，也停止下拉刷新
        console.error('刷新数据失败', error);
        wx.stopPullDownRefresh();
        this.setData({
          refreshing: false
        });
      });
  },


  // 修改: 实现上拉加载更多功能
  onReachBottom() {
    if (this.data.loadingMore) return;
    
    // 检查是否还有更多数据
    if(this.data.pageNum * this.data.pageSize >= this.data.total) return;
    
    this.setData({
      loadingMore: true
    });
    
    // 递增页码
    const nextPage = this.data.pageNum + 1;
    let { auditStatus, violationType, userId } = this.data;
    
    getHistoryList({
      auditStatus,
      violationType,
      userId,
      pageNum: nextPage,
      pageSize: this.data.pageSize
    }).then(res => {
      // 处理图片路径，与初始加载保持一致
      const processedRows = res.rows.map(item => {
        if (item.adImages) {
          item.processedAdImage = handleImageUrl(item.adImages);
        } else {
          item.processedAdImage = '/static/images/approval.png';
        }
        return item;
      });
      
      // 合并新数据
      const updatedList = this.data.historyList.concat(processedRows);
      
      this.setData({
        historyList: updatedList,
        pageNum: nextPage,
        loadingMore: false
      });
    }).catch(error => {
      console.error('加载更多数据失败', error);
      this.setData({
        loadingMore: false
      });
    });
  },


  /**
   * 类型选择变化
   */
  onTypeChange(e) {
    const value = e.detail.value;
    this.setData({
      violationType: value,
      pageNum: 1  // 重置页码为第一页
    }, () => {
      // 在setData回调中调用getHistoryListData，确保数据已更新
      this.getHistoryListData();
    });
  },

  /**
   * 状态选择变化
   */
  onStatusChange(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({
      auditStatus: status,
      pageNum: 1  // 重置页码为第一页
    }, () => {
      // 在setData回调中调用getHistoryListData，确保数据已更新
      this.getHistoryListData();
    });
  },
  //获取上传历史列表
  getHistoryListData(){
    let { violationType, auditStatus, userId, pageNum, pageSize } = this.data;
    // 添加return语句，返回Promise
    return getHistoryList({
      violationType,
      auditStatus,
      userId,
      pageNum,
      pageSize
    }).then(res=>{
      console.log(res);
      // 处理图片路径
      const processedRows = res.rows.map(item => {
        // 如果有adImages字段，处理图片路径
        if (item.adImages) {
          item.processedAdImage = handleImageUrl(item.adImages);
        } else {
          // 没有图片使用默认图片
          item.processedAdImage = '/static/images/approval.png';
        }
        return item;
      });
      
      this.setData({
        historyList: processedRows,
        total: res.total
      });
    });
  },
  //点击头像退出登录
  onClickAvatar(){
    this.setData({
      showConfirm:true
    })
  },
  // 取消
  closeDialog(){
    this.setData({
      showConfirm:false
    })
  },
  // 确定退出
  confirmQuit(){
    // 清除缓存
    wx.removeStorageSync('token');
    wx.removeStorageSync('userId');
    wx.removeStorageSync('nickName');
    wx.removeStorageSync('username');
    wx.removeStorageSync('phonenumber');
    wx.removeStorageSync('roleKey');
    wx.removeStorageSync('roleName');
    // 跳转回登录页
    wx.reLaunch({
      url: '/pages/login/login'
    })
  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  // 修改: 实现下拉刷新功能
  onPullDownRefresh() {
    if (this.data.refreshing) return;
    
    this.setData({
      refreshing: true,
      pageNum: 1  // 重置页码为第一页
    });
    // 刷新数据
    this.getHistoryListData()
      .then(() => {
        // 请求成功，停止下拉刷新
        wx.stopPullDownRefresh();
        this.setData({
          refreshing: false
        });
      })
      .catch((error) => {
        // 请求失败，也停止下拉刷新
        console.error('刷新数据失败', error);
        wx.stopPullDownRefresh();
        this.setData({
          refreshing: false
        });
      });
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  // 修改: 实现上拉加载更多功能
  onReachBottom() {
    if (this.data.loadingMore) return;
    
    // 检查是否还有更多数据
    if(this.data.pageNum * this.data.pageSize >= this.data.total) return;
    
    this.setData({
      loadingMore: true
    });
    
    // 递增页码
    const nextPage = this.data.pageNum + 1;
    let { auditStatus, violationType, userId } = this.data;
    
    getHistoryList({
      auditStatus,
      violationType,
      userId,
      pageNum: nextPage,
      pageSize: this.data.pageSize
    }).then(res => {
      // 处理图片路径，与初始加载保持一致
      const processedRows = res.rows.map(item => {
        if (item.adImages) {
          item.processedAdImage = handleImageUrl(item.adImages);
        } else {
          item.processedAdImage = '/static/images/approval.png';
        }
        return item;
      });
      
      // 合并新数据
      const updatedList = this.data.historyList.concat(processedRows);
      
      this.setData({
        historyList: updatedList,
        pageNum: nextPage,
        loadingMore: false
      });
    }).catch(error => {
      console.error('加载更多数据失败', error);
      this.setData({
        loadingMore: false
      });
    });
  },

  /**
   * 类型选择变化
   */
  onTypeChange(e) {
    const value = e.detail.value;
    this.setData({
      violationType: value,
      pageNum: 1  // 重置页码为第一页
    }, () => {
      // 在setData回调中调用getHistoryListData，确保数据已更新
      this.getHistoryListData();
    });
  },

  /**
   * 状态选择变化
   */
  onStatusChange(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({
      auditStatus: status,
      pageNum: 1  // 重置页码为第一页
    }, () => {
      // 在setData回调中调用getHistoryListData，确保数据已更新
      this.getHistoryListData();
    });
  }
})