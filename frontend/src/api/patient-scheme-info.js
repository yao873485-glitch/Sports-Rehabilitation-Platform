import request from '@/utils/request'

// 获取患者方案信息列表
export function getPatientSchemeInfoList(params) {
  return request({
    url: '/api/patient-scheme-info/list',
    method: 'get',
    params
  })
}

// 获取患者方案信息详情
export function getPatientSchemeInfoDetail(id) {
  return request({
    url: `/api/patient-scheme-info/${id}`,
    method: 'get'
  })
}

// 获取患者处方清单
export function getPatientPrescriptionList(schemeId) {
  return request({
    url: `/api/patient-scheme-info/prescription-list/${schemeId}`,
    method: 'get'
  })
}