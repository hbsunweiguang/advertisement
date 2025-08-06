import request from '@/utils/request'

// 查询执行结果记录列表
export function listEnforcement(query) {
  return request({
    url: '/advertisement/enforcement/list',
    method: 'get',
    params: query
  })
}

// 查询执行结果记录详细
export function getEnforcement(id) {
  return request({
    url: '/advertisement/enforcement/' + id,
    method: 'get'
  })
}

// 新增执行结果记录
export function addEnforcement(data) {
  return request({
    url: '/advertisement/enforcement',
    method: 'post',
    data: data
  })
}

// 修改执行结果记录
export function updateEnforcement(data) {
  return request({
    url: '/advertisement/enforcement',
    method: 'put',
    data: data
  })
}

// 删除执行结果记录
export function delEnforcement(id) {
  return request({
    url: '/advertisement/enforcement/' + id,
    method: 'delete'
  })
}
export function selectReport(id) {
  return request({
    url: '/advertisement/enforcement/selectReport/' + id,
    method: 'get'
  })
}