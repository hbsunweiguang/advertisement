import {login} from "@/api/login";

// pages/login/login.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    username: "fys",
    password: "123456",
    roleKey: "handle"
  },
  // 角色选择
  onRoleSelect(e) {
    const roleKey = e.currentTarget.dataset.roleKey;
    this.setData({
      roleKey: roleKey
    });
  },

  // 表单提交
  onSubmit() {
    const { username, password, roleKey } = this.data;
    
    // 表单验证
    if (!username || !password) {
      wx.showToast({
        title: '请填写完整信息',
        icon: 'none'
      });
      return;
    }
    
    // 模拟登录请求
    wx.showLoading({
      title: '登录中...'
    });
    login({ username, password, roleKey }).then(res=>{
      console.log(res);
      if(res.code == 500 ) {
        wx.showToast({
          icon: none,
          title: res.msg,
        })
        return
      }
      // 存储用户信息
      wx.setStorageSync('token', res.data.token); 
      wx.setStorageSync('username', res.data.username); 
      wx.setStorageSync('userId', res.data.userId); 
      wx.setStorageSync('nickName', res.data.user.nickName);
      wx.setStorageSync('phonenumber', res.data.user.phonenumber);
      // wx.setStorageSync('role', roleKey); 

      wx.setStorageSync('roleKey', res.data.user.roles[0].roleKey);
      wx.setStorageSync('roleName', res.data.user.roles[0].roleName);

      setTimeout(() => {
        wx.hideLoading();
        wx.showToast({
          title: '登录成功',
          icon: 'success'
        });
        if(res.data.user.roles[0].roleKey == "collect") {
          wx.reLaunch({
            url: '/pages/index/index',
          });
        } else {
          wx.redirectTo({
            url: '/pages/enforcementList/enforcementList',
          });
        }
      }, 1500)
    }).catch(err=>{
      console.log(err);
      wx.hideLoading();
      wx.showToast({
        title: '登录失败',
        icon: 'none'
      });
    })
  }
})