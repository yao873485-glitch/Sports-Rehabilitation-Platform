import request from '@/utils/request'

// 上传文件
export function uploadFile(formData) {
  return request({
    url: '/file-share/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取文件列表
export function getFileList(params) {
  return request({
    url: '/file-share/list',
    method: 'get',
    params
  })
}

// 下载文件
export function downloadFile(id) {
  return request({
    url: `/file-share/download/${id}`,
    method: 'get'
  })
}

// 删除文件
export function deleteFile(id) {
  return request({
    url: `/file-share/${id}`,
    method: 'delete'
  })
}
