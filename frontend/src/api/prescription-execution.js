import request from '@/utils/request'

// 获取处方执行列表
export function getPrescriptionExecutionList(params) {
  return request({
    url: '/api/prescription-execution/list',
    method: 'get',
    params
  })
}

// 获取处方执行详情
export function getPrescriptionExecutionDetail(id) {
  return request({
    url: `/api/prescription-execution/${id}`,
    method: 'get'
  })
}

// 结束处方执行
export function endPrescriptionExecution(id) {
  return request({
    url: `/api/prescription-execution/${id}/end`,
    method: 'put'
  })
}

// 执行处方
export function executePrescription(id) {
  return request({
    url: `/api/prescription-execution/${id}/execute`,
    method: 'put'
  })
}

// 获取所有病种类型
export function getDiseaseTypes() {
  return request({
    url: '/api/prescription-execution/disease-types',
    method: 'get'
  })
}

// 获取所有机构列表
export function getInstitutions() {
  return request({
    url: '/api/prescription-execution/institutions',
    method: 'get'
  })
}