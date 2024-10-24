import request from '@/utils/request'

// 获取7日数据
export function GetSevenInfo(params) {
  return request({
    url: 'PayMentApp-order/grid/getInfo',
    method: 'post',
  })
}

//获取两种付费方式
export function GetTwiceInfo(params) {
  return request({
    url: 'PayMentApp-order/grid/getTwiceInfo',
    method: 'post',
  })
}