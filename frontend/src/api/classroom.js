import request from '@/utils/request'

// 获取教室列表
// 注意：这里使用listClassroom而不是getClassroomList，因为前端页面中调用的是listClassroom
export function listClassroom(params) {
  return request({
    url: '/api/classroom-facility/list',
    method: 'get',
    params
  })
}

// 获取教室详情
export function getClassroom(id) {
  return request({
    url: `/api/classroom-facility/${id}`,
    method: 'get'
  })
}

// 新增教室
export function addClassroom(data) {
  return request({
    url: '/api/classroom-facility',
    method: 'post',
    data
  })
}

// 更新教室
export function updateClassroom(data) {
  return request({
    url: `/api/classroom-facility/${data.id}`,
    method: 'put',
    data
  })
}

// 删除教室
export function deleteClassroom(id) {
  return request({
    url: `/api/classroom-facility/${id}`,
    method: 'delete'
  })
}

// 上架教室
export function onlineClassroom(id) {
  return request({
    url: `/api/classroom-facility/${id}/online`,
    method: 'put'
  })
}

// 下架教室
export function offlineClassroom(id) {
  return request({
    url: `/api/classroom-facility/${id}/offline`,
    method: 'put'
  })
}

// 获取所有运动类型
export function getExerciseTypes() {
  return request({
    url: '/api/classroom-facility/exercise-types',
    method: 'get'
  })
}