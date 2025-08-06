import http from '@/utils/http.js'

//小程序采集用户查询广告列表
export function getHistoryList(params) {
  return http.get('/api/advertisement/list',{
    params:{...params}
  })
}

//统计查询采集者数据
export function getCollectCount() {
  return http.get('/api/advertisement/getCollector')
}