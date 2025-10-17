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

// 分页获取图片列表
export function getList(data) {
  return request({
    url: '/picture/list/page',   // 替换成你后端的接口地址
    method: 'post',
    data: data
  })
}

// 获取类别列表
export function getCategoryList() {
  return request({
    url: '/picture/list/category',
    method: 'get'
  })
}

// 获取标签列表
export function getTagList() {
  return request({
    url: '/picture/list/tag',
    method: 'get'
  })
}

// 根据 id 获取图片详细信息
export function getPictureDetail(id) {
  return request({
    url: '/picture/get?id=' + id,
    method: 'get',
  })
}
