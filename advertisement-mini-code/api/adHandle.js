import http from '@/utils/http.js'

//小程序采集用户查询广告详情
export function getAdDetail(id) {
  return http.get(`/api/advertisement/${id}`)
}


//小程序执法用户处理广告
export function handleAdInfo(data) {
  return http.put(`/advertisement/enforcement`,data)
}