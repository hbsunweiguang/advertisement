import request from '@/utils/request'

// 查询广告列表列表
export function listAdvertisement(query) {
  return request({
    url: '/advertisement/advertisement/list',
    method: 'get',
    params: query
  })
}

// 查询广告列表详细
export function getAdvertisement(id) {
  return request({
    url: '/advertisement/advertisement/' + id,
    method: 'get'
  })
}

// 新增广告列表
export function addAdvertisement(data) {
  return request({
    url: '/advertisement/advertisement',
    method: 'post',
    data: data
  })
}

// 修改广告列表
export function updateAdvertisement(data) {
  return request({
    url: '/advertisement/advertisement',
    method: 'put',
    data: data
  })
}

// 删除广告列表
export function delAdvertisement(id) {
  return request({
    url: '/advertisement/advertisement/' + id,
    method: 'delete'
  })
}

// 查询某一级的行政区域管理列表
export function allRegional(query) {
  return request({
    url: '/advertisement/advertisement/all',
    method: 'get',
    params: query
  })
}

// 获取采集用户列表
export function getUserList(query) {
  return request({
    url: '/advertisement/advertisement/userList',
    method: 'get',
    params: query
  })
}

export function count() {
  return request({
    url: '/advertisement/advertisement/count',
    method: 'get'
  })
}

// 新增广告列表
export function approveAdvertisement(data) {
  return request({
    url: '/advertisement/advertisement/approve',
    method: 'post',
    data: data
  })
}

//获取redis中的行业列表
export function getIndustry() {
  return request({
    url: '/advertisement/advertisement/industry/list',
    method: 'get'
  })
}

//获取redis中的媒体列表
export function getMedium() {
  return request({
    url: '/advertisement/advertisement/medium/list',
    method: 'get'
  })
}
