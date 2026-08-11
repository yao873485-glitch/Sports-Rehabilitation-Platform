import request from '@/utils/request'

/**
 * 保存或更新专病档案
 * @param {Object} data 专病档案数据
 */
export function saveDiseaseRecord(data) {
  return request({
    url: '/api/disease-record/save',
    method: 'post',
    data
  })
}

/**
 * 根据患者ID查询专病档案
 * @param {Number} patientId 患者ID
 */
export function getDiseaseRecordByPatientId(patientId) {
  return request({
    url: `/api/disease-record/${patientId}`,
    method: 'get'
  })
}

/**
 * 根据患者ID删除专病档案
 * @param {Number} patientId 患者ID
 */
export function deleteDiseaseRecord(patientId) {
  return request({
    url: `/api/disease-record/${patientId}`,
    method: 'delete'
  })
}
