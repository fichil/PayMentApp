import request from '@/utils/request'

// 获取商户列表
export function GetAdmin(params) {
  return request({
    url: 'PayMentApp-platform/admin/selectAllAdmin',
    method: 'post',
    data: params
    
  })
}
//获取商铺信息
export function  GetShop(params) {
  return request({
    url: 'PayMentApp-platform/merchantAdmin/getMerchantAdmin',
    method: 'post',
    data: params
  })
}