import request from '@/utils/request'

export function upload(formData) {
  return request({
    url: '/picture/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
