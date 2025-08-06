import request from '@/utils/request'

// 查询行政区域管理列表
export function listRegional(query) {
  return request({
    url: '/advertisement/regional/list',
    method: 'get',
    params: query
  })
}

export function listArea(query) {
  return request({
    url: '/advertisement/regional/listAll',
    method: 'get',
    params: query
  })
}

// 查询行政区域管理详细
export function getRegional(code) {
  return request({
    url: '/advertisement/regional/' + code,
    method: 'get'
  })
}

// 新增行政区域管理
export function addRegional(data) {
  return request({
    url: '/advertisement/regional',
    method: 'post',
    data: data
  })
}

// 修改行政区域管理
export function updateRegional(data) {
  return request({
    url: '/advertisement/regional',
    method: 'put',
    data: data
  })
}

// 删除行政区域管理
export function delRegional(code) {
  return request({
    url: '/advertisement/regional/' + code,
    method: 'delete'
  })
}
