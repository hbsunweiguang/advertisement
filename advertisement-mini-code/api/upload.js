import http from '@/utils/http.js'

//从redis中获取行业类型列表
export function getIndustryList() {
  return http.get('/api/advertisement/industry/list')
}

//检测行业列表中是否存在该行业
export function checkIndustry(industry) {
  return http.get('/api/advertisement/industry/check',{params:{industry}})
}

//新增行业类型到redis中

export function addIndustry(industry) {
  return http.post(`/api/advertisement/industry?industry=${industry}`)
}

//媒体类型-从redis中获取媒体类型列表
export function getMediaTypeList() {
  return http.get('/api/advertisement/medium/list')
}

//媒体类型-检测媒体列表中是否存在该媒体类型
export function checkMediaType(medium) {
  return http.get('/api/advertisement/medium/check',{params:{medium}})
}


//媒体类型-新增媒体类型到redis中
export function addMediaType(medium) {
  return http.post(`/api/advertisement/medium?medium=${medium}`)
}

//采集上传广告
export function uploadAdvertisement(data) {
  return http.post('/api/advertisement/advertisement',data)
}
