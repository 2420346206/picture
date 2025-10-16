import request from '@/utils/request'

export function addSpace(data) {
  return request({
    url: '/space/add',
    method: 'post',
    data
  })
}

export function getSpaceList(data) {
  return request({
    url: '/space/list/page',
    method: 'post',
    data
  })
}
