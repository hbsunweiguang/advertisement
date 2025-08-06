// pages/enforcementList/enforcementList.js
import { getEnforcementList,gethandleCount } from "@/api/enforcementList";
import { handleImageUrl } from '../../utils/util';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    //违法类型
    typeOptions: [
      { value: null, label: '全部' },
      { value: '1', label: '禁用词类' },
      { value: '2', label: '虚假宣传' },
      { value: '3', label: '低俗类' },
      { value: '4', label: '敏感类' },
      { value: '5', label: '其他类' }
    ],
    //违法类型
    violationType: null,
    //处理结果
    handleResult: '0',
    // 添加刷新和加载状态
    refreshing: false,
    loadingMore: false,
    enforcementList: [],
    showConfirm: false,
    pageNum: 1,
    pageSize: 6,
    total: 0,
    createBy:wx.getStorageSync('userId'),
    nickName:wx.getStorageSync('nickName'),
    phonenumber:wx.getStorageSync('phonenumber'),
    count:0,
    handle:0
  },
  getData(){
    this.setData({
      createBy:wx.getStorageSync('userId'),
      nickName:wx.getStorageSync('nickName'),
      phonenumber:wx.getStorageSync('phonenumber'),
      violationType: null,
      pageNum: 1,      //重置页码为第一页
      handleResult: '0' // 修改: 默认展示待处理 1已拆除 2已罚款
    });
    this.getEnforcementListData();
  },
  /*** 生命周期函数--监听页面加载*/
  onLoad(options) {
    this.getData()
  },
  onShow(){
    this.getData()
    this.gethandleCountData()
  },
  // 获取统计数据
  gethandleCountData(){
    gethandleCount().then(res=>{
      console.log(res,11)
      this.setData({
        count:res.data.count,
        handle:res.data.handle
      })
    })
  },
  // 获取执法列表
  getEnforcementListData() {
    let { violationType, handleResult, createBy, pageNum, pageSize } = this.data;
    // 添加return语句，返回Promise
    return getEnforcementList({
      violationType: violationType,
      handleResult: handleResult,
      createBy,
      pageNum,
      pageSize
    }).then(res => {
      // console.log(res);
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
        enforcementList: processedRows,
        total: res.total
      });
    });
  },

  // 点击头像退出登录
  onClickAvatar() {
    this.setData({
      showConfirm: true
    });
  },

  // 取消退出登录
  closeDialog() {
    this.setData({
      showConfirm: false
    });
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

  // 实现下拉刷新功能
  onPullDownRefresh() {
    if (this.data.refreshing) return;

    this.setData({
      refreshing: true,
      pageNum: 1  // 重置页码为第一页
    });
    // 刷新数据
    this.getEnforcementListData()
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


  // 实现上拉加载更多功能
  onReachBottom() {
    if (this.data.loadingMore) return;

    // 检查是否还有更多数据
    if (this.data.pageNum * this.data.pageSize >= this.data.total) return;

    this.setData({
      loadingMore: true
    });

    // 递增页码
    const nextPage = this.data.pageNum + 1;
    let { handleResult, violationType, createBy } = this.data;

    getEnforcementList({
      handleResult: handleResult,
      violationType: violationType,
      createBy,
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
      const updatedList = this.data.enforcementList.concat(processedRows);

      this.setData({
        enforcementList: updatedList,
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
      // 在setData回调中调用getEnforcementListData，确保数据已更新
      this.getEnforcementListData();
    });
  },

  /**
   * 状态选择变化
   */
  onStatusChange(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({
      handleResult: status,
      pageNum: 1  // 重置页码为第一页
    }, () => {
      // 在setData回调中调用getEnforcementListData，确保数据已更新
      this.getEnforcementListData();
    });
  }
})