import request from '@/utils/request'

// 获取随访患者列表
export function getFollowupList(params) {
  return request({
    url: '/followup/list',
    method: 'get',
    params
  })
}

// 获取随访患者详情
export function getFollowupDetail(id) {
  return request({
    url: `/followup/${id}`,
    method: 'get'
  })
}

// 移除项目
export function removeFromProject(id) {
  return request({
    url: `/followup/${id}/remove`,
    method: 'put'
  })
}

// ==================== 随访项目相关接口 ====================

// 分页获取随访项目列表
export function getFollowupProjectList(params) {
  return request({
    url: '/api/followup-program/page',
    method: 'get',
    params
  })
}

// 根据ID获取随访项目详情
export function getFollowupProjectDetail(id) {
  return request({
    url: `/api/followup-program/${id}`,
    method: 'get'
  })
}

// 更新随访项目
export function updateFollowupProject(data) {
  return request({
    url: '/api/followup-program',
    method: 'put',
    data
  })
}

// 根据计划创建随访项目
export function createFollowupProject(data) {
  return request({
    url: '/api/followup-program/create',
    method: 'post',
    data
  })
}

// 获取指定模板的项目数量
export function getProjectCountByTemplate(templateName) {
  return request({
    url: `/api/followup-program/count/${templateName}`,
    method: 'get'
  })
}

// ==================== 随访计划相关接口 ====================

// 分页获取随访计划列表
export function getFollowupPlanList(params) {
  return request({
    url: '/api/followup-plan/page',
    method: 'get',
    params
  })
}

// 根据ID获取随访计划详情
export function getFollowupPlanDetail(id) {
  return request({
    url: `/api/followup-plan/${id}`,
    method: 'get'
  })
}

// 删除随访计划
export function deleteFollowupPlan(id) {
  return request({
    url: `/api/followup-plan/${id}`,
    method: 'delete'
  })
}

// 根据模板创建随访计划
export function createFollowupPlan(data) {
  return request({
    url: '/api/followup-plan/create',
    method: 'post',
    data
  })
}

// ==================== 随访记录相关接口 ====================

// 保存随访记录
export function saveFollowupRecord(data) {
  return request({
    url: '/api/followup-record',
    method: 'post',
    data
  })
}

// 根据入组记录ID获取随访记录列表
export function getFollowupRecordsByEnrollmentId(enrollmentId) {
  return request({
    url: `/api/followup-record/list/${enrollmentId}`,
    method: 'get'
  })
}

// 患者入组（创建随访记录）
export function enrollPatient(data) {
  return request({
    url: '/followup/enroll',
    method: 'post',
    data
  })
}
