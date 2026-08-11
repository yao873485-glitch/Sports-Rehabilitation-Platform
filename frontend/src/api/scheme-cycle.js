import request from '@/utils/request'

/**
 * 保存或更新方案周期配置
 * @param {Object} data 方案周期配置数据
 */
export function saveSchemeCycle(data) {
  return request({
    url: '/api/scheme-cycle/save',
    method: 'post',
    data
  })
}

/**
 * 根据患者ID查询方案周期配置
 * @param {Number} patientId 患者ID
 */
export function getSchemeCycleByPatientId(patientId) {
  return request({
    url: `/api/scheme-cycle/patient/${patientId}`,
    method: 'get'
  })
}

/**
 * 根据患者ID和方案ID查询方案周期配置
 * @param {Number} patientId 患者ID
 * @param {String} schemeId 方案ID
 */
export function getSchemeCycleByPatientIdAndSchemeId(patientId, schemeId) {
  return request({
    url: `/api/scheme-cycle/patient/${patientId}/scheme/${schemeId}`,
    method: 'get'
  })
}

/**
 * 根据方案ID查询方案周期配置
 * @param {String} schemeId 方案ID
 */
export function getSchemeCycleBySchemeId(schemeId) {
  return request({
    url: `/api/scheme-cycle/scheme/${schemeId}`,
    method: 'get'
  })
}

/**
 * 根据患者ID删除方案周期配置
 * @param {Number} patientId 患者ID
 */
export function deleteSchemeCycle(patientId) {
  return request({
    url: `/api/scheme-cycle/patient/${patientId}`,
    method: 'delete'
  })
}
