import request from '@/utils/request'

// 获取患者运动处方列表
export function getPatientExercisePrescriptionList(params) {
  return request({
    url: '/api/patient-exercise-prescription/list',
    method: 'get',
    params
  })
}

// 获取患者运动处方详情
export function getPatientExercisePrescriptionDetail(id) {
  return request({
    url: `/api/patient-exercise-prescription/${id}`,
    method: 'get'
  })
}

// 新增患者运动处方
export function addPatientExercisePrescription(data) {
  return request({
    url: '/api/patient-exercise-prescription',
    method: 'post',
    data
  })
}

// 更新患者运动处方
export function updatePatientExercisePrescription(id, data) {
  return request({
    url: `/api/patient-exercise-prescription/${id}`,
    method: 'put',
    data
  })
}

// 结束患者运动处方
export function endPatientExercisePrescription(id) {
  return request({
    url: `/api/patient-exercise-prescription/${id}/end`,
    method: 'put'
  })
}

// 执行患者运动处方
export function executePatientExercisePrescription(id) {
  return request({
    url: `/api/patient-exercise-prescription/${id}/execute`,
    method: 'put'
  })
}

// 获取所有病种类型
export function getDiseaseTypes() {
  return request({
    url: '/api/patient-exercise-prescription/disease-types',
    method: 'get'
  })
}

// 获取所有机构列表
export function getInstitutions() {
  return request({
    url: '/api/patient-exercise-prescription/institutions',
    method: 'get'
  })
}