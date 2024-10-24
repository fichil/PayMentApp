import request from '@/utils/request'

export function login(data) {
  return request({
    url: 'PayMentApp-platform/admin/login',
    // url: '/api/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: 'PayMentApp-platform/admin/getAdminInfo',
    method: 'get',
    headers: { admin_token: token }
  })
}
export function refreshCaptcha(){
  return request({
    url: "api/PayMentApp-platform/admin/code" + "?" + new Date().getTime(),
    method: 'get'
  })
}
export function getMenu(roleId){
  return request({
    url: 'PayMentApp-platform/menu/getMenu',
    params: {roleId},
    method: 'get'
  })
}

//修改密码
export function updatePassword(data) {
  return request({
    url: 'PayMentApp-platform/admin/changePassword',
    method: 'post',
    data:data
  })
}
//修改姓名
export function updateName(data) {
  return request({
    url: 'PayMentApp-platform/admin/updateAdmin',
    method: 'post',
    data:data
  })
}


// export function logout() {
//   return request({
//     url: '/vue-admin-template/user/logout',
//     method: 'post'
//   })

// }
