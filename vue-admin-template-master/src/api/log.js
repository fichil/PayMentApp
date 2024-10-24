import request from '@/utils/request'

// 获取商户列表
export function GetLogs(params) {
  return request({
    url: 'PayMentApp-platform/log/getLogInfo',
    method: 'post',
    data: params
    
  })
}