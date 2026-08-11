import request from '@/utils/request'

/**
 * 保存或更新健康档案
 * @param {Object} data 健康档案数据
 */
export function saveHealthRecord(data) {
  return request({
    url: '/api/health-record/save',
    method: 'post',
    data
  })
}

/**
 * 根据患者ID查询健康档案
 * @param {Number} patientId 患者ID
 */
export function getHealthRecordByPatientId(patientId) {
  return request({
    url: `/api/health-record/${patientId}`,
    method: 'get'
  })
}

/**
 * 根据患者ID删除健康档案
 * @param {Number} patientId 患者ID
 */
export function deleteHealthRecord(patientId) {
  return request({
    url: `/api/health-record/${patientId}`,
    method: 'delete'
  })
}
