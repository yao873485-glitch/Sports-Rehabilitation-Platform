import request from '@/utils/request'

// 发送消息
export function sendMessage(data) {
  return request({
    url: '/chat/send',
    method: 'post',
    params: data
  })
}

// 获取聊天历史
export function getChatHistory(params) {
  return request({
    url: '/chat/history',
    method: 'get',
    params
  })
}

// 标记消息为已读
export function markAsRead(data) {
  return request({
    url: '/chat/read',
    method: 'post',
    params: data
  })
}
