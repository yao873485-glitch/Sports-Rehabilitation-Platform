import request from '@/utils/request'

// 获取评定执行列表
export function getAssessmentExecutionList(params) {
  return request({
    url: '/api/assessment-execution/list',
    method: 'get',
    params
  })
}

// 获取评定执行详情
export function getAssessmentExecutionDetail(id) {
  return request({
    url: `/api/assessment-execution/${id}`,
    method: 'get'
  })
}

// 结束评定执行
export function endAssessmentExecution(id) {
  return request({
    url: `/api/assessment-execution/${id}/end`,
    method: 'put'
  })
}

// 执行评定
export function executeAssessment(id) {
  return request({
    url: `/api/assessment-execution/${id}/execute`,
    method: 'put'
  })
}

// 获取所有病种类型
export function getDiseaseTypes() {
  return request({
    url: '/api/assessment-execution/disease-types',
    method: 'get'
  })
}

// 获取所有机构列表
export function getInstitutions() {
  return request({
    url: '/api/assessment-execution/institutions',
    method: 'get'
  })
}

// 获取评估执行信息（用于执行弹窗）
export function getAssessmentExecutionInfo(assessmentPlanId) {
  return request({
    url: `/api/assessment-execution/execution-info/${assessmentPlanId}`,
    method: 'get'
  })
}

// 提交评估执行记录
export function submitAssessmentExecution(data) {
  return request({
    url: '/api/assessment-execution/submit',
    method: 'post',
    data
  })
}