import request from '@/utils/request'

/**
 * 获取评定计划查看详情（包含患者信息）
 */
export function getAssessmentPlanDetail(id) {
  return request({
    url: `/patient-assessment-plan/${id}/view`,
    method: 'get'
  })
}

/**
 * 执行评定项目
 */
export function executeAssessmentItem(data) {
  return request({
    url: '/assessment-execution/execute',
    method: 'post',
    data
  })
}
