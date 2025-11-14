import request from '@/utils/request'

// 获取课程预约记录列表
export function getReservationList(params) {
  return request({
    url: '/reservation/list',
    method: 'get',
    params
  })
}

// 获取预约详情
export function getReservationDetail(id) {
  return request({
    url: `/reservation/${id}`,
    method: 'get'
  })
}

// 核销预约
export function verifyReservation(id) {
  return request({
    url: `/reservation/${id}/verify`,
    method: 'put'
  })
}
