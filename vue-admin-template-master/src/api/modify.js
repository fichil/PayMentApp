import request from '@/utils/request'

//商户修改
export function GetMerchantmodify(data) {
  return request({
    url: '/PayMentApp-platform/merchant/updateMerchant',
    method: 'post',
    data:data
  })
}
//修改状态
export function GetMerchantmodifyStatus(data) {
  return request({
    url: '/PayMentApp-platform/merchantAdmin/updateMerchantAdmin',
    method: 'post',
    data:data
  })
}
//添加账户
export function GetMerchantaddAccount(data) {
  return request({
    url: '/PayMentApp-platform/merchant/addMerchant',
    method: 'post',
    data:data
  })
}
//修改平台管理员状态
export function GetAdminmodifyStatus(data) {
  return request({
    url: '/PayMentApp-platform/admin/updateAdmin',
    method: 'post',
    data:data
  })
}
//重置商户管理员密码
export function GetAdminresetPassword(data) {
  return request({
    url: '/PayMentApp-platform/merchantAdmin/updateMerchantAdmin',
    method: 'post',
    data:data
  })
}

//同意审核
export function GetMerchantagree(data) {
  return request({
    url: '/PayMentApp-platform/checkOut/agree',
    method: 'post',
    data:data
  })
}
//拒绝审核
export function GetMerchantrefuse(data) {
  return request({
    url: '/PayMentApp-platform/checkOut/reject',
    method: 'post',
    data:data
  })
}
//冻结商户账号
export function GetMerchantfreeze(data) {
  return request({
    url: '/PayMentApp-platform/merchant/updateMerchant',
    method: 'post',
    data:data
  })
}
//解冻商户账号
export function GetMerchantunfreeze(data) {
  return request({
    url: '/PayMentApp-platform/merchant/updateMerchant',
    method: 'post',
    data:data
  })
}

