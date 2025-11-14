import request from '@/utils/request'

// 获取患者评定方案列表
export function getPatientAssessmentPlanList(params) {
  return request({
    url: '/api/patient-assessment-plan/list',
    method: 'get',
    params
  })
}

// 获取患者评定方案详情
export function getPatientAssessmentPlanDetail(id) {
  return request({
    url: `/api/patient-assessment-plan/${id}`,
    method: 'get'
  })
}

// 新增患者评定方案
export function addPatientAssessmentPlan(data) {
  return request({
    url: '/api/patient-assessment-plan',
    method: 'post',
    data
  })
}

// 更新患者评定方案
export function updatePatientAssessmentPlan(id, data) {
  return request({
    url: `/api/patient-assessment-plan/${id}`,
    method: 'put',
    data
  })
}

// 结束患者评定方案
export function endPatientAssessmentPlan(id) {
  return request({
    url: `/api/patient-assessment-plan/${id}/end`,
    method: 'put'
  })
}

// 获取所有病种类型
export function getDiseaseTypes() {
  return request({
    url: '/api/patient-assessment-plan/disease-types',
    method: 'get'
  })
}

// 获取所有机构列表
export function getInstitutions() {
  return request({
    url: '/api/patient-assessment-plan/institutions',
    method: 'get'
  })
}