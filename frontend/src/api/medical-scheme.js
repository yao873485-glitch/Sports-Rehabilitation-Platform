import request from '@/utils/request'

// 获取医疗方案列表
export function getMedicalSchemeList(params) {
  return request({
    url: '/api/medical-scheme/list',
    method: 'get',
    params
  })
}

// 获取医疗方案详情
export function getMedicalSchemeDetail(id) {
  return request({
    url: `/api/medical-scheme/${id}`,
    method: 'get'
  })
}

// 结束医疗方案
export function endMedicalScheme(id) {
  return request({
    url: `/api/medical-scheme/${id}/end`,
    method: 'put'
  })
}

// 检查患者是否已有执行中的方案（通过档案号）
export function checkPatientHasActiveScheme(medicalRecordNo) {
  return request({
    url: '/api/medical-scheme/check-patient',
    method: 'get',
    params: { medicalRecordNo }
  })
}

// 创建方案
export function createScheme(data) {
  return request({
    url: '/api/medical-scheme/create',
    method: 'post',
    data
  })
}

// 保存运动处方
export function savePrescription(data) {
  return request({
    url: '/api/patient-exercise-prescription/save-prescription',
    method: 'post',
    data
  })
}

// 保存评定计划
export function saveAssessment(data) {
  return request({
    url: '/api/medical-scheme/save-assessment',
    method: 'post',
    data
  })
}

// 更新患者病种
export function updatePatientDiseaseType(data) {
  return request({
    url: '/api/patient/update-disease-type',
    method: 'put',
    data
  })
}