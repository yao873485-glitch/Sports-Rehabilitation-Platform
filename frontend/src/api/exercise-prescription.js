import request from '@/utils/request'

// 获取运动处方列表
export function getExercisePrescriptionList(params) {
  return request({
    url: '/api/exercise-prescription/list',
    method: 'get',
    params
  })
}

// 获取运动处方详情
export function getExercisePrescriptionDetail(id) {
  return request({
    url: `/api/exercise-prescription/${id}`,
    method: 'get'
  })
}

// 新增运动处方
export function addExercisePrescription(data) {
  return request({
    url: '/api/exercise-prescription',
    method: 'post',
    data
  })
}

// 更新运动处方
export function updateExercisePrescription(id, data) {
  return request({
    url: `/api/exercise-prescription/${id}`,
    method: 'put',
    data
  })
}

// 获取所有病种类型
export function getDiseaseTypes() {
  return request({
    url: '/api/exercise-prescription/disease-types',
    method: 'get'
  })
}

// 批量操作运动处方
export function batchOperation(data) {
  return request({
    url: '/api/exercise-prescription/batch-operation',
    method: 'post',
    data
  })
}

// 导入运动处方数据
export function importExercisePrescription(data) {
  return request({
    url: '/api/exercise-prescription/import',
    method: 'post',
    data
  })
}