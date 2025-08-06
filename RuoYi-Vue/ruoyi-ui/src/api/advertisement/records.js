import request from '@/utils/request'

// 查询审核记录列表
export function listRecords(query) {
  return request({
    url: '/advertisement/records/list',
    method: 'get',
    params: query
  })
}

// 查询审核记录详细
export function getRecords(id) {
  return request({
    url: '/advertisement/records/' + id,
    method: 'get'
  })
}

// 新增审核记录
export function addRecords(data) {
  return request({
    url: '/advertisement/records',
    method: 'post',
    data: data
  })
}

// 修改审核记录
export function updateRecords(data) {
  return request({
    url: '/advertisement/records',
    method: 'put',
    data: data
  })
}

// 删除审核记录
export function delRecords(id) {
  return request({
    url: '/advertisement/records/' + id,
    method: 'delete'
  })
}
