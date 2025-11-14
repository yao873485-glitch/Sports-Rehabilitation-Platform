import request from '@/utils/request'

// ==================== 图文素材库相关接口 ====================

// 分页获取图文素材列表
export function getContentList(params) {
  return request({
    url: '/edu-content/page',
    method: 'get',
    params
  })
}

// 根据ID获取图文素材详情
export function getContentDetail(id) {
  return request({
    url: `/edu-content/by-content-id/${id}`,
    method: 'get'
  })
}

// 添加图文素材
export function addContent(data) {
  return request({
    url: '/edu-content',
    method: 'post',
    data
  })
}

// 更新图文素材
export function updateContent(data) {
  return request({
    url: `/edu-content/${data.id}`,
    method: 'put',
    data
  })
}

// 删除图文素材
export function deleteContentItem(id) {
  return request({
    url: `/edu-content/${id}`,
    method: 'delete'
  })
}

// ==================== 视频素材库相关接口 ====================

// 分页获取视频素材列表
export function getVideoAssetList(params) {
  return request({
    url: '/video-asset/list',
    method: 'get',
    params
  })
}

// 根据ID获取视频素材详情
export function getVideoAssetDetail(id) {
  return request({
    url: `/video-asset/by-video-id/${id}`,
    method: 'get'
  })
}

// 添加视频素材
export function addVideoAsset(data) {
  return request({
    url: '/video-asset',
    method: 'post',
    data
  })
}

// 更新视频素材
export function updateVideoAsset(data) {
  return request({
    url: `/video-asset/${data.id}`,
    method: 'put',
    data
  })
}

// 删除视频素材
export function deleteVideoAsset(id) {
  return request({
    url: `/video-asset/${id}`,
    method: 'delete'
  })
}

// ==================== 内容应用管理相关接口 ====================

// 分页获取内容应用管理列表
export function getContentManagementList(params) {
  return request({
    url: '/content-management/list',
    method: 'get',
    params
  })
}

// 根据ID获取内容应用管理详情
export function getContentManagementDetail(id) {
  return request({
    url: `/content-management/${id}`,
    method: 'get'
  })
}

// 更新内容状态
export function updateContentStatus(id, status) {
  return request({
    url: `/content-management/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 删除内容
export function deleteContent(id) {
  return request({
    url: `/content-management/${id}`,
    method: 'delete'
  })
}

// 根据ID获取内容应用配置
export function getContentApplicationById(id) {
  return request({
    url: `/content-application/${id}`,
    method: 'get'
  })
}

// 更新内容应用配置
export function updateContentApplication(data) {
  return request({
    url: `/content-application/${data.id}`,
    method: 'put',
    data
  })
}