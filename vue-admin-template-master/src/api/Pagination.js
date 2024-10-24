import request from '@/utils/request'

// 获取商户列表
export function GetMerchantList(params) {
  return request({
    url: 'PayMentApp-platform/merchant/getMerchantsInfo',
    method: 'post',
    data: params
  })
}
//获取二维码
export function GetRefreshQrCode(merchantNumber){
  return request({
    url: 'PayMentApp-platform/merchant/refreshQR',
    method: 'get',
    params: { merchantNumber }
  })
}
//获取商户订单
export function GetOrderList(params) {
  return request({
    url: 'PayMentApp-order/order/getAllOrder',
    method: 'post',
    data: params
  })
}

//获取提款订单
export function GetRefundList(params) {
  return request({
    url: 'PayMentApp-order/withdrawOrder/getWithdrawOrders',
    method: 'post',
    data: params
  })
}
//获取审核列表
export function GetAuditList(params) {
  return request({
    url: 'PayMentApp-platform/checkOut/getCheckOutList',
    method: 'post',
    data: params
  })
}
