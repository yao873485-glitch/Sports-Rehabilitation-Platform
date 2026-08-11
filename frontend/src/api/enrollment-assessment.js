import request from '@/utils/request'

/**
 * 保存或更新入组评估
 * @param {Object} data 入组评估数据
 */
export function saveEnrollmentAssessment(data) {
  return request({
    url: '/api/enrollment-assessment/save',
    method: 'post',
    data
  })
}

/**
 * 根据患者ID查询入组评估
 * @param {Number} patientId 患者ID
 */
export function getEnrollmentAssessmentByPatientId(patientId) {
  return request({
    url: `/api/enrollment-assessment/${patientId}`,
    method: 'get'
  })
}

/**
 * 根据患者ID删除入组评估
 * @param {Number} patientId 患者ID
 */
export function deleteEnrollmentAssessment(patientId) {
  return request({
    url: `/api/enrollment-assessment/${patientId}`,
    method: 'delete'
  })
}
