import request from '@/utils/request'

// 查询数据可视化大屏数据
export function listStatistics(query) {
  return request({
    url: '/api/statistics/list',
    method: 'get',
    params: query
  })
}

