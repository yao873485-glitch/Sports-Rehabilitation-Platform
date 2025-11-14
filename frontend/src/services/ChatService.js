import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import { getToken } from '@/utils/auth'

/**
 * WebSocket 聊天服务类
 */
class ChatService {
  constructor() {
    this.stompClient = null
    this.connected = false
    this.subscriptions = {}
    this.messageHandlers = []
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 5
    this.reconnectDelay = 3000
  }

  /**
   * 连接到 WebSocket 服务器
   * @param {Number} userId - 当前用户ID
   * @param {Function} onConnected - 连接成功回调
   * @param {Function} onError - 连接失败回调
   */
  connect(userId, onConnected, onError) {
    if (this.connected) {
      console.log('WebSocket already connected')
      return
    }

    const token = getToken()
    if (!token) {
      console.error('No authentication token found')
      if (onError) onError(new Error('No authentication token'))
      return
    }

    // 创建 SockJS 连接
    const socket = new SockJS(`${process.env.VUE_APP_BASE_API}/api/ws?token=${token}`)
    this.stompClient = Stomp.over(socket)

    // 禁用调试日志（可选）
    this.stompClient.debug = null

    // 连接到 STOMP 服务器
    this.stompClient.connect(
      {},
      (frame) => {
        console.log('WebSocket connected:', frame)
        this.connected = true
        this.reconnectAttempts = 0
        this.userId = userId

        // 订阅用户专属消息队列
        this.subscribeToUserMessages(userId)

        if (onConnected) {
          onConnected(frame)
        }
      },
      (error) => {
        console.error('WebSocket connection error:', error)
        this.connected = false

        // 尝试重新连接
        this.handleReconnect(userId, onConnected, onError)

        if (onError) {
          onError(error)
        }
      }
    )
  }

  /**
   * 订阅用户消息队列
   * @param {Number} userId - 用户ID
   */
  subscribeToUserMessages(userId) {
    const destination = `/queue/messages-${userId}`

    if (this.subscriptions[destination]) {
      console.log('Already subscribed to:', destination)
      return
    }

    const subscription = this.stompClient.subscribe(destination, (message) => {
      const messageData = JSON.parse(message.body)
      console.log('Received message:', messageData)

      // 调用所有注册的消息处理器
      this.messageHandlers.forEach(handler => {
        handler(messageData)
      })
    })

    this.subscriptions[destination] = subscription
    console.log('Subscribed to:', destination)
  }

  /**
   * 发送消息
   * @param {Object} message - 消息对象
   */
  sendMessage(message) {
    if (!this.connected || !this.stompClient) {
      console.error('WebSocket not connected')
      return false
    }

    try {
      this.stompClient.send('/app/chat', {}, JSON.stringify(message))
      console.log('Message sent:', message)
      return true
    } catch (error) {
      console.error('Error sending message:', error)
      return false
    }
  }

  /**
   * 注册消息处理器
   * @param {Function} handler - 消息处理函数
   */
  onMessage(handler) {
    if (typeof handler === 'function') {
      this.messageHandlers.push(handler)
    }
  }

  /**
   * 移除消息处理器
   * @param {Function} handler - 要移除的处理函数
   */
  offMessage(handler) {
    const index = this.messageHandlers.indexOf(handler)
    if (index > -1) {
      this.messageHandlers.splice(index, 1)
    }
  }

  /**
   * 断开连接
   */
  disconnect() {
    if (this.stompClient && this.connected) {
      // 取消所有订阅
      Object.values(this.subscriptions).forEach(subscription => {
        subscription.unsubscribe()
      })
      this.subscriptions = {}

      // 断开 STOMP 连接
      this.stompClient.disconnect(() => {
        console.log('WebSocket disconnected')
        this.connected = false
        this.stompClient = null
        this.messageHandlers = []
      })
    }
  }

  /**
   * 处理重新连接
   */
  handleReconnect(userId, onConnected, onError) {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++
      console.log(`Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)

      setTimeout(() => {
        this.connect(userId, onConnected, onError)
      }, this.reconnectDelay)
    } else {
      console.error('Max reconnection attempts reached')
    }
  }

  /**
   * 检查连接状态
   */
  isConnected() {
    return this.connected
  }
}

// 导出单例
export default new ChatService()
