// pages/test/test.js

import {baseURL} from '@/utils/apiUrl'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    baseURL,
    pdfUrl: '',
    
    // pdfUrl: 'https://gitee.com/chen-chenghsc/gallery-2/raw/master/advertisement_1754029345937_20250801142226A001.pdf',

    // pdfUrl: 'http://192.168.4.11:8080/profile/upload/2025/08/01/advertisement_1754033942559_20250801153902A003.pdf',

    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

    this.setData({
      pdfUrl:options.report
    })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})