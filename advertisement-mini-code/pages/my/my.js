// pages/mobx/mobx.js
import {ComponentWithStore} from "mobx-miniprogram-bindings"
import {numStore} from "@/store/numstore.js"

ComponentWithStore({
  storeBindings:{
    store: numStore,
    fields:["num1","num2","sum"],
    actions:["update"] 
  },
  data: {
  },
  methods:{
    logout(){
      // 退出登录
      wx.removeStorageSync("username")
      // 跳转到登录页面
      wx.redirectTo({
        url: '/pages/login/login',
      })
    },
  },
  onLoad(options) {
  }
})