import http from '@/utils/http.js'

// 执法者查询广告列表
export function getEnforcementList(params) {
  return http.get('/advertisement/enforcement/selectEnforcementAll', {
    params: { ...params }
  })
}

// 统计查询处理者数据
export function gethandleCount() {
  return http.get('/api/advertisement/getHandler')
}