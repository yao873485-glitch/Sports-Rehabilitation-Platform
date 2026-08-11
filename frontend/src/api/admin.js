import request from '@/utils/request'

export function adminLogin(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function fetchPendingApplications() {
  return request({
    url: '/admin/applications',
    method: 'get'
  })
}

export function approveApplication(userId) {
  return request({
    url: `/admin/applications/${userId}/approve`,
    method: 'post'
  })
}

export function rejectApplication(userId) {
  return request({
    url: `/admin/applications/${userId}/reject`,
    method: 'post'
  })
}
