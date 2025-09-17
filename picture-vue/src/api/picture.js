import request from '@/utils/request'

export function upload(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}
