import WxRequest from 'wx-request-plus';
import {baseURL} from "./apiUrl"

// 推荐方式: 使用静态工厂方法创建实例
const http = WxRequest.create({
  baseURL: baseURL,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
http.interceptors.request.use(
  config => {
    // 添加token
    config.headers = { 
      ...config.headers,
      'Authorization': `Bearer ${wx.getStorageSync('token')}` 
    };
    // 对GET请求默认启用缓存
    if (config.method?.toUpperCase() === 'GET' && config.cache === undefined) {
      config.cache = true;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
http.interceptors.response.use(
  response => {
    console.log(response,12345)
    if(response.data?.code==401) {
      wx.redirectTo({
        url: '/pages/login/login',
      })
      return
    }
    return response.data
  },
  error => {
    console.log(error,12345)
    // 根据错误类型处理
    switch(error.type) {
      case ErrorType.TIMEOUT:
        console.error('请求超时', error.config.url);
        break;
      case ErrorType.NETWORK:
        console.error('网络连接错误', error.config.url);
        break;
      case ErrorType.SERVER:
        console.error('服务器错误', error.status, error.config.url);
        break;
      case ErrorType.CLIENT:
        console.error('客户端错误', error.status, error.config.url);
        break;
    }
    
    // 可以选择继续抛出错误或返回特定值
    return Promise.reject(error);
  }
);

// 导出wxRequest实例
export default http;