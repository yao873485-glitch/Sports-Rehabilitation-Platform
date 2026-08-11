import request from '@/utils/request'

// 获取运动处方列表
export function getPrescriptionList(params) {
  return request({
    url: '/prescription/list',
    method: 'get',
    params
  })
}

// 获取运动处方详情
export function getPrescriptionDetail(id) {
  return request({
    url: `/prescription/${id}`,
    method: 'get'
  })
}

// 结束运动处方
export function endPrescription(id) {
  return request({
    url: `/prescription/${id}/end`,
    method: 'put'
  })
}

// 执行运动处方
export function executePrescription(id) {
  return request({
    url: `/prescription/${id}/execute`,
    method: 'put'
  })
}

// 获取运动处方详情（包含患者信息和运动项目列表）
export function getPrescriptionDetailWithExercises(id) {
  return request({
    url: `/prescription/${id}/detail`,
    method: 'get'
  })
}

// 保存运动处方
export function savePrescription(data) {
  return request({
    url: '/prescription/save',
    method: 'post',
    data
  })
}

// 提交运动处方
export function submitPrescription(data) {
  return request({
    url: '/prescription/submit',
    method: 'post',
    data
  })
}

// 执行运动处方（新功能 - 批量执行）
export function executePrescriptionBatch(data) {
  return request({
    url: '/prescription-execution/execute',
    method: 'post',
    data
  })
}
