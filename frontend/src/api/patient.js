import request from '@/utils/request'

// 获取患者列表
export function getPatientList(query) {
  return request({
    url: '/api/patient/list',
    method: 'get',
    params: query
  })
}

// 获取患者详细信息
export function getPatientDetail(id) {
  return request({
    url: `/api/patient/detail/${id}`,
    method: 'get'
  })
}

// 新增患者信息
export function addPatient(data) {
  return request({
    url: '/api/patient/add',
    method: 'post',
    data: data
  })
}

// 更新患者基本信息
export function updatePatient(data) {
  return request({
    url: '/api/patient/update',
    method: 'put',
    data: data
  })
}

// 保存或更新患者详细信息
export function savePatientDetail(data) {
  return request({
    url: '/api/patient/save-detail',
    method: 'post',
    data: data
  })
}

// 删除患者信息
export function deletePatient(id) {
  return request({
    url: `/api/patient/delete/${id}`,
    method: 'delete'
  })
}