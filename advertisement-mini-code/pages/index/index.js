// index.js


Page({
  data: {
    // 页面数据
  },

  // 生命周期函数--监听页面加载
  onLoad: function() {
    // 页面加载时执行的逻辑
  },

  // 举报信息查询点击事件
  onReportQuery: function() {
    wx.navigateTo({
      url: '/pages/historyList/historyList'
    });
  }
})
