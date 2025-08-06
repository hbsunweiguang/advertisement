import http from '@/utils/http.js'

//小程序登录用户
export function login(data) {
  return http.post('/api/login',data)
}