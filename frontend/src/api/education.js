import request from '@/utils/request'

// ==================== 图文素材库相关接口 ====================

// 分页获取图文素材列表
export function getContentList(params) {
  return request({
    url: '/education/image-content/page',
    method: 'get',
    params
  })
}

// 根据ID获取图文素材详情
export function getContentDetail(id) {
  return request({
    url: `/education/image-content/${id}`,
    method: 'get'
  })
}

// 保存图文素材
export function saveImageContent(data) {
  return request({
    url: '/education/image-content',
    method: 'post',
    data
  })
}

// 添加图文素材（旧接口，保留兼容性）
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
    url: `/education/image-content/${id}`,
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
    url: `/video-asset/${id}`,
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

// ==================== 内容应用配置相关接口 ====================

// 获取内容应用配置
export function getContentConfig(contentId, contentType) {
  return request({
    url: '/api/content-config/get',
    method: 'get',
    params: { contentId, contentType }
  })
}

// 保存内容应用配置
export function saveContentConfig(data) {
  return request({
    url: '/api/content-config/save',
    method: 'post',
    data
  })
}

// 删除内容应用配置
export function deleteContentConfig(contentId, contentType) {
  return request({
    url: '/api/content-config/delete',
    method: 'delete',
    params: { contentId, contentType }
  })
}

// 获取配置操作日志
export function getConfigLogs(contentId, contentType) {
  return request({
    url: '/api/content-config/logs',
    method: 'get',
    params: { contentId, contentType }
  })
}