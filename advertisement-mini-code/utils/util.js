import {baseURL} from "./apiUrl"

const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return `${[year, month, day].map(formatNumber).join('/')} ${[hour, minute, second].map(formatNumber).join(':')}`
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : `0${n}`
}

// 处理图片URL
export const handleImageUrl = (url) => {
  // 如果url为空或无效，返回默认图片
  if (!url) {
    return '/static/images/approval.png';
  }
  
  // 分割多张图片URL，取第一张
  const urls = url.split(',');
  const firstUrl = urls[0].trim();
  
  // 如果是完整HTTP URL直接返回
  if (firstUrl.startsWith('http')) {
    return firstUrl;
  }
  
  // 如果是相对路径，拼接基础URL
  if (firstUrl.startsWith('/profile')) {
    // return `${baseURL}${firstUrl}`;
    return '/static/images/approval.png';

  }
  
  // 其他情况返回默认图片
  return '/static/images/approval.png';
}

export default {
  formatTime,
  handleImageUrl
}
