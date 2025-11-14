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
