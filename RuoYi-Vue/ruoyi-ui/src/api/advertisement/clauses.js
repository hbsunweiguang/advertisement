import request from '@/utils/request'

// 查询法律法规管理列表
export function listClauses(query) {
  return request({
    url: '/advertisement/clauses/list',
    method: 'get',
    params: query
  })
}

export function listAll(query) {
  return request({
    url: '/advertisement/clauses/listAll',
    method: 'get',
    params: query
  })
}

// 查询法律法规管理详细
export function getClauses(id) {
  return request({
    url: '/advertisement/clauses/' + id,
    method: 'get'
  })
}

// 新增法律法规管理
export function addClauses(data) {
  return request({
    url: '/advertisement/clauses',
    method: 'post',
    data: data
  })
}

// 修改法律法规管理
export function updateClauses(data) {
  return request({
    url: '/advertisement/clauses',
    method: 'put',
    data: data
  })
}

// 删除法律法规管理
export function delClauses(id) {
  return request({
    url: '/advertisement/clauses/' + id,
    method: 'delete'
  })
}
