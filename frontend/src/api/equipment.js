import request from '@/utils/request'

// 获取设备列表
export function getEquipmentList(params) {
  return request({
    url: '/api/equipment/list',
    method: 'get',
    params
  })
}

// 获取设备详情
export function getEquipmentDetail(id) {
  return request({
    url: `/api/equipment/${id}`,
    method: 'get'
  })
}

// 新增设备
export function addEquipment(data) {
  return request({
    url: '/api/equipment',
    method: 'post',
    data
  })
}

// 更新设备
export function updateEquipment(id, data) {
  return request({
    url: `/api/equipment/${id}`,
    method: 'put',
    data
  })
}

// 删除设备
export function deleteEquipment(id) {
  return request({
    url: `/api/equipment/${id}`,
    method: 'delete'
  })
}

// 获取所有设备类型
export function getEquipmentTypes() {
  return request({
    url: '/api/equipment/types',
    method: 'get'
  })
}

// 批量操作设备
export function batchOperation(data) {
  return request({
    url: '/api/equipment/batch-operation',
    method: 'post',
    data
  })
}

// 导入设备数据
export function importEquipment(data) {
  return request({
    url: '/api/equipment/import',
    method: 'post',
    data
  })
}