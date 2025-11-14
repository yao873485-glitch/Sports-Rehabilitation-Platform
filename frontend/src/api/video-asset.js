import request from '@/utils/request'

// 获取视频素材列表
export function getVideoAssetList(params) {
  return request({
    url: '/video-asset/list',
    method: 'get',
    params
  })
}

// 获取视频素材详情
export function getVideoAssetDetail(id) {
  return request({
    url: `/video-asset/${id}`,
    method: 'get'
  })
}

// 复制视频素材
export function copyVideoAsset(id) {
  return request({
    url: `/video-asset/${id}/copy`,
    method: 'post'
  })
}
