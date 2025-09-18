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

// 新建图片
export function createPicture(data) {
  return request({
    url: '/picture/create',   // 替换成你后端的接口地址
    method: 'post',
    data: data
  })
}
