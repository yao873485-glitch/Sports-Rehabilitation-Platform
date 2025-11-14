<template>
  <div class="chat-container">
    <div class="chat-header">
      <h3>医患沟通平台 - 消息聊天</h3>
      <div class="header-status">
        <el-tag :type="connected ? 'success' : 'danger'" size="small">
          {{ connected ? '已连接' : '未连接' }}
        </el-tag>
      </div>
    </div>

    <div class="chat-main">
      <!-- 患者列表 -->
      <div class="patient-list">
        <div class="list-header">
          <span>患者列表</span>
          <el-badge :value="totalUnreadCount" :hidden="totalUnreadCount === 0" class="unread-badge">
            <i class="el-icon-bell"></i>
          </el-badge>
        </div>
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索患者..."
            prefix-icon="el-icon-search"
            clearable
            size="small"
          />
        </div>
        <div class="list-content">
          <div
            v-for="patient in filteredPatientList"
            :key="patient.id"
            :class="['patient-item', { active: currentPatient && currentPatient.id === patient.id }]"
            @click="selectPatient(patient)"
          >
            <div class="patient-avatar">
              <el-avatar :size="40" :src="patient.avatar">
                {{ patient.name.charAt(0) }}
              </el-avatar>
            </div>
            <div class="patient-info">
              <div class="patient-name">
                {{ patient.name }}
                <el-tag v-if="patient.isOnline" type="success" size="mini">在线</el-tag>
              </div>
              <div class="patient-last-msg">{{ patient.lastMessage || '暂无消息' }}</div>
            </div>
            <div class="patient-meta">
              <div class="last-time">{{ formatLastTime(patient.lastTime) }}</div>
              <div v-if="patient.unreadCount > 0" class="unread-count">
                {{ patient.unreadCount > 99 ? '99+' : patient.unreadCount }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 聊天区域 -->
      <div class="chat-area">
        <div v-if="!currentPatient" class="no-chat">
          <i class="el-icon-chat-line-square"></i>
          <p>请选择一个患者开始聊天</p>
        </div>

        <div v-else class="chat-content">
          <!-- 聊天头部 -->
          <div class="chat-content-header">
            <div class="patient-avatar">
              <el-avatar :size="40" :src="currentPatient.avatar">
                {{ currentPatient.name.charAt(0) }}
              </el-avatar>
            </div>
            <div class="patient-detail">
              <div class="name">{{ currentPatient.name }}</div>
              <div class="status">
                <span v-if="currentPatient.isOnline" class="online-status">
                  <i class="el-icon-circle-check"></i> 在线
                </span>
                <span v-else-if="typingStatus" class="typing-status">
                  <i class="el-icon-edit-outline"></i> 对方正在输入...
                </span>
                <span v-else class="offline-status">离线</span>
              </div>
            </div>
            <div class="chat-actions">
              <el-button size="mini" icon="el-icon-refresh" @click="refreshChatHistory">刷新</el-button>
              <el-button size="mini" icon="el-icon-more" @click="showMoreActions">更多</el-button>
            </div>
          </div>

          <!-- 消息列表 -->
          <div class="message-list" ref="messageList" @scroll="handleScroll">
            <!-- 加载更多 -->
            <div v-if="hasMoreHistory" class="load-more">
              <el-button
                v-if="!loadingHistory"
                size="mini"
                @click="loadMoreHistory"
                :loading="loadingHistory"
              >
                加载更多历史消息
              </el-button>
              <div v-else class="loading-text">加载中...</div>
            </div>

            <!-- 日期分隔 -->
            <div v-for="(group, date) in groupedMessages" :key="date" class="message-group">
              <div class="date-divider">{{ date }}</div>
              <div
                v-for="message in group"
                :key="message.id"
                :class="['message-item', message.senderType === 'doctor' ? 'sent' : 'received']"
              >
                <div v-if="message.senderType === 'patient'" class="message-avatar">
                  <el-avatar :size="32" :src="currentPatient.avatar">
                    {{ currentPatient.name.charAt(0) }}
                  </el-avatar>
                </div>
                <div class="message-content">
                  <div class="message-bubble">
                    <!-- 文字消息 -->
                    <div v-if="message.messageType === 'text'" class="message-text">
                      {{ message.content }}
                    </div>
                    <!-- 图片消息 -->
                    <div v-else-if="message.messageType === 'image'" class="message-image">
                      <el-image
                        :src="message.content"
                        :preview-src-list="[message.content]"
                        fit="cover"
                        style="max-width: 200px; max-height: 200px;"
                      />
                    </div>
                    <!-- 文件消息 -->
                    <div v-else-if="message.messageType === 'file'" class="message-file">
                      <i class="el-icon-document"></i>
                      <span>{{ getFileName(message.content) }}</span>
                    </div>
                    <!-- 语音消息 -->
                    <div v-else-if="message.messageType === 'voice'" class="message-voice">
                      <i class="el-icon-microphone"></i>
                      <span>{{ message.duration }}s</span>
                    </div>
                  </div>
                  <div class="message-meta">
                    <span class="message-time">{{ formatTime(message.sendTime) }}</span>
                    <span v-if="message.senderType === 'doctor'" class="read-status">
                      <i v-if="message.isRead" class="el-icon-circle-check read-icon"></i>
                      <i v-else class="el-icon-time unread-icon"></i>
                    </span>
                  </div>
                </div>
                <div v-if="message.senderType === 'doctor'" class="message-avatar">
                  <el-avatar :size="32">医</el-avatar>
                </div>
              </div>
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="input-area">
            <div class="input-toolbar">
              <el-upload
                :action="uploadUrl"
                :show-file-list="false"
                :before-upload="beforeUpload"
                :on-success="handleUploadSuccess"
                accept="image/*"
                class="upload-btn"
              >
                <el-button size="mini" icon="el-icon-picture" title="发送图片">图片</el-button>
              </el-upload>
              <el-upload
                :action="uploadUrl"
                :show-file-list="false"
                :before-upload="beforeUpload"
                :on-success="handleUploadSuccess"
                class="upload-btn"
              >
                <el-button size="mini" icon="el-icon-folder" title="发送文件">文件</el-button>
              </el-upload>
              <el-button size="mini" icon="el-icon-microphone" @click="startRecording" title="语音消息">
                语音
              </el-button>
              <el-button size="mini" icon="el-icon-star-off" title="收藏消息">收藏</el-button>
            </div>
            <div class="input-box">
              <el-input
                v-model="inputMessage"
                type="textarea"
                :rows="3"
                placeholder="请输入消息内容... Ctrl+Enter 发送"
                @keydown.ctrl.enter="handleSendMessage"
                @input="handleInput"
                :disabled="!connected"
              />
              <div class="input-actions">
                <span class="input-tip">{{ inputMessage.length }}/500</span>
                <el-button
                  type="primary"
                  @click="handleSendMessage"
                  :disabled="!inputMessage.trim() || !connected"
                  :loading="sending"
                >
                  发送
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getChatHistory, sendMessage, markAsRead, uploadFile } from '@/api/chat'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'

export default {
  name: 'Chat',
  data() {
    return {
      // 当前选中的患者
      currentPatient: null,
      // 患者列表
      patientList: [
        {
          id: 1,
          name: '张三',
          lastMessage: '谢谢医生，我感觉好多了',
          lastTime: new Date(Date.now() - 1000 * 60 * 5),
          unreadCount: 2,
          isOnline: true,
          avatar: ''
        },
        {
          id: 2,
          name: '李四',
          lastMessage: '明天几点来复查？',
          lastTime: new Date(Date.now() - 1000 * 60 * 60),
          unreadCount: 0,
          isOnline: false,
          avatar: ''
        },
        {
          id: 3,
          name: '王五',
          lastMessage: '医生，药吃完了怎么办？',
          lastTime: new Date(Date.now() - 1000 * 60 * 60 * 24),
          unreadCount: 1,
          isOnline: false,
          avatar: ''
        }
      ],
      // 搜索关键词
      searchKeyword: '',
      // 消息列表
      messageList: [],
      // 输入的消息
      inputMessage: '',
      // WebSocket连接
      stompClient: null,
      connected: false,
      // 发送中状态
      sending: false,
      // 正在输入状态
      typingStatus: false,
      typingTimer: null,
      // 历史消息分页
      currentPage: 1,
      pageSize: 20,
      hasMoreHistory: true,
      loadingHistory: false,
      // 上传地址
      uploadUrl: '/api/chat/upload',
      // 当前用户信息（假设是医生）
      currentUser: {
        id: 1,
        type: 'doctor',
        name: '医生'
      }
    }
  },
  computed: {
    // 过滤后的患者列表
    filteredPatientList() {
      if (!this.searchKeyword) return this.patientList
      return this.patientList.filter(patient =>
        patient.name.includes(this.searchKeyword)
      )
    },
    // 总未读数
    totalUnreadCount() {
      return this.patientList.reduce((total, patient) => total + patient.unreadCount, 0)
    },
    // 按日期分组的消息
    groupedMessages() {
      const groups = {}
      this.messageList.forEach(message => {
        const date = this.formatDate(message.sendTime)
        if (!groups[date]) {
          groups[date] = []
        }
        groups[date].push(message)
      })
      return groups
    }
  },
  mounted() {
    this.initWebSocket()
  },
  beforeDestroy() {
    this.disconnect()
    if (this.typingTimer) {
      clearTimeout(this.typingTimer)
    }
  },
  methods: {
    /** 初始化WebSocket */
    initWebSocket() {
      this.stompClient = new Client({
        webSocketFactory: () => new SockJS('/api/ws'),
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        onConnect: () => {
          this.connected = true
          console.log('WebSocket连接成功')

          // 订阅私人消息频道
          this.stompClient.subscribe(`/user/${this.currentUser.id}/queue/messages`, (message) => {
            const msg = JSON.parse(message.body)
            this.handleReceiveMessage(msg)
          })

          // 订阅输入状态
          this.stompClient.subscribe(`/user/${this.currentUser.id}/queue/typing`, (message) => {
            const data = JSON.parse(message.body)
            this.handleTypingStatus(data)
          })
        },
        onStompError: (error) => {
          this.connected = false
          console.error('WebSocket连接失败:', error)
        }
      })

      this.stompClient.activate()
    },

    /** 断开WebSocket连接 */
    disconnect() {
      if (this.stompClient && this.connected) {
        this.stompClient.deactivate()
        this.connected = false
      }
    },

    /** 选择患者 */
    selectPatient(patient) {
      this.currentPatient = patient
      this.messageList = []
      this.currentPage = 1
      this.hasMoreHistory = true
      this.loadChatHistory()
      this.markMessagesAsRead()
    },

    /** 加载聊天历史 */
    async loadChatHistory() {
      if (!this.currentPatient || this.loadingHistory) return

      this.loadingHistory = true
      try {
        const response = await getChatHistory({
          patientId: this.currentPatient.id,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        })

        const newMessages = response.data.records
        if (newMessages.length < this.pageSize) {
          this.hasMoreHistory = false
        }

        if (this.currentPage === 1) {
          this.messageList = newMessages.reverse()
        } else {
          // 加载更多时，将新消息插入到前面
          this.messageList = [...newMessages.reverse(), ...this.messageList]
        }

        this.$nextTick(() => {
          if (this.currentPage === 1) {
            this.scrollToBottom()
          }
        })
      } catch (error) {
        console.error('加载聊天历史失败:', error)
        this.$message.error('加载聊天历史失败')
      } finally {
        this.loadingHistory = false
      }
    },

    /** 加载更多历史消息 */
    loadMoreHistory() {
      this.currentPage++
      this.loadChatHistory()
    },

    /** 处理接收到的消息 */
    handleReceiveMessage(message) {
      if (this.currentPatient && message.senderId === this.currentPatient.id) {
        this.messageList.push(message)
        this.scrollToBottom()
        // 自动标记为已读
        this.markSingleMessageAsRead(message.id)
      } else {
        // 更新患者列表的最后消息和未读数
        this.updatePatientLastMessage(message)
      }
    },

    /** 更新患者列表的最后消息 */
    updatePatientLastMessage(message) {
      const patient = this.patientList.find(p => p.id === message.senderId)
      if (patient) {
        patient.lastMessage = message.content
        patient.lastTime = message.sendTime
        if (this.currentPatient?.id !== message.senderId) {
          patient.unreadCount++
        }
      }
    },

    /** 发送消息 */
    async handleSendMessage() {
      if (!this.inputMessage.trim() || !this.currentPatient || !this.connected) return

      this.sending = true
      try {
        const message = {
          receiverId: this.currentPatient.id,
          content: this.inputMessage.trim(),
          messageType: 'text'
        }

        await sendMessage(message)

        // 添加到消息列表
        const newMessage = {
          id: Date.now(),
          senderType: 'doctor',
          senderId: this.currentUser.id,
          senderName: this.currentUser.name,
          content: this.inputMessage.trim(),
          messageType: 'text',
          sendTime: new Date(),
          isRead: false
        }
        this.messageList.push(newMessage)
        this.inputMessage = ''

        this.scrollToBottom()

        // 更新患者列表
        this.currentPatient.lastMessage = newMessage.content
        this.currentPatient.lastTime = newMessage.sendTime

      } catch (error) {
        console.error('发送消息失败:', error)
        this.$message.error('发送消息失败')
      } finally {
        this.sending = false
      }
    },

    /** 处理输入事件 */
    handleInput() {
      if (this.inputMessage.length > 500) {
        this.inputMessage = this.inputMessage.substring(0, 500)
        this.$message.warning('消息内容不能超过500字')
      }

      // 发送输入状态
      if (this.currentPatient && this.connected) {
        this.sendTypingStatus(true)

        // 清除之前的定时器
        if (this.typingTimer) {
          clearTimeout(this.typingTimer)
        }

        // 3秒后发送停止输入状态
        this.typingTimer = setTimeout(() => {
          this.sendTypingStatus(false)
        }, 3000)
      }
    },

    /** 发送输入状态 */
    sendTypingStatus(isTyping) {
      if (this.stompClient && this.connected && this.currentPatient) {
        this.stompClient.publish({
          destination: '/app/typing',
          body: JSON.stringify({
            senderId: this.currentUser.id,
            receiverId: this.currentPatient.id,
            isTyping: isTyping
          })
        })
      }
    },

    /** 处理输入状态 */
    handleTypingStatus(data) {
      if (this.currentPatient && data.senderId === this.currentPatient.id) {
        this.typingStatus = data.isTyping

        if (this.typingStatus) {
          // 3秒后自动隐藏输入状态
          setTimeout(() => {
            this.typingStatus = false
          }, 3000)
        }
      }
    },

    /** 上传前处理 */
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isImage && !file.type.includes('file')) {
        this.$message.error('只能上传图片或文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    /** 上传成功处理 */
    handleUploadSuccess(response, file) {
      const messageType = file.raw.type.startsWith('image/') ? 'image' : 'file'
      this.sendMediaMessage(response.url, messageType)
    },

    /** 发送媒体消息 */
    async sendMediaMessage(content, messageType) {
      if (!this.currentPatient || !this.connected) return

      try {
        await sendMessage({
          receiverId: this.currentPatient.id,
          content: content,
          messageType: messageType
        })

        const newMessage = {
          id: Date.now(),
          senderType: 'doctor',
          senderId: this.currentUser.id,
          senderName: this.currentUser.name,
          content: content,
          messageType: messageType,
          sendTime: new Date(),
          isRead: false
        }
        this.messageList.push(newMessage)
        this.scrollToBottom()

      } catch (error) {
        console.error('发送媒体消息失败:', error)
        this.$message.error('发送失败')
      }
    },

    /** 开始录音 */
    startRecording() {
      this.$message.info('语音录制功能开发中...')
    },

    /** 标记消息为已读 */
    async markMessagesAsRead() {
      if (!this.currentPatient) return

      try {
        const conversationId = `doctor_${this.currentUser.id}_patient_${this.currentPatient.id}`
        await markAsRead({
          conversationId: conversationId,
          receiverId: this.currentUser.id
        })

        // 更新未读数
        this.currentPatient.unreadCount = 0
      } catch (error) {
        console.error('标记已读失败:', error)
      }
    },

    /** 标记单条消息为已读 */
    async markSingleMessageAsRead(messageId) {
      // 这里可以实现单条消息的已读标记
    },

    /** 刷新聊天历史 */
    refreshChatHistory() {
      if (this.currentPatient) {
        this.messageList = []
        this.currentPage = 1
        this.hasMoreHistory = true
        this.loadChatHistory()
      }
    },

    /** 显示更多操作 */
    showMoreActions() {
      this.$message.info('更多功能开发中...')
    },

    /** 处理滚动事件 */
    handleScroll(event) {
      const { scrollTop } = event.target
      if (scrollTop === 0 && this.hasMoreHistory && !this.loadingHistory) {
        this.loadMoreHistory()
      }
    },

    /** 滚动到底部 */
    scrollToBottom() {
      this.$nextTick(() => {
        const messageList = this.$refs.messageList
        if (messageList) {
          messageList.scrollTop = messageList.scrollHeight
        }
      })
    },

    /** 获取文件名 */
    getFileName(url) {
      return url.split('/').pop() || '未知文件'
    },

    /** 格式化时间 */
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
      const messageDate = new Date(date.getFullYear(), date.getMonth(), date.getDate())

      if (messageDate.getTime() === today.getTime()) {
        return date.toLocaleTimeString('zh-CN', { hour12: false }).substring(0, 5)
      } else {
        return date.toLocaleDateString('zh-CN', {
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit'
        })
      }
    },

    /** 格式化最后消息时间 */
    formatLastTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diffMs = now - date
      const diffMins = Math.floor(diffMs / 60000)
      const diffHours = Math.floor(diffMs / 3600000)
      const diffDays = Math.floor(diffMs / 86400000)

      if (diffMins < 1) return '刚刚'
      if (diffMins < 60) return `${diffMins}分钟前`
      if (diffHours < 24) return `${diffHours}小时前`
      if (diffDays < 7) return `${diffDays}天前`

      return date.toLocaleDateString('zh-CN', {
        month: '2-digit',
        day: '2-digit'
      })
    },

    /** 格式化日期 */
    formatDate(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
      const yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000)
      const messageDate = new Date(date.getFullYear(), date.getMonth(), date.getDate())

      if (messageDate.getTime() === today.getTime()) {
        return '今天'
      } else if (messageDate.getTime() === yesterday.getTime()) {
        return '昨天'
      } else {
        return date.toLocaleDateString('zh-CN', {
          month: '2-digit',
          day: '2-digit'
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.chat-container {
  height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;

  .chat-header {
    padding: 16px 20px;
    border-bottom: 1px solid #e4e7ed;
    background: #f5f7fa;
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
    }
  }

  .chat-main {
    flex: 1;
    display: flex;
    overflow: hidden;

    .patient-list {
      width: 320px;
      border-right: 1px solid #e4e7ed;
      display: flex;
      flex-direction: column;
      background: #fafafa;

      .list-header {
        padding: 16px;
        border-bottom: 1px solid #e4e7ed;
        font-weight: 600;
        color: #303133;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .unread-badge {
          cursor: pointer;
        }
      }

      .search-box {
        padding: 12px;
        border-bottom: 1px solid #e4e7ed;
      }

      .list-content {
        flex: 1;
        overflow-y: auto;

        .patient-item {
          display: flex;
          padding: 12px 16px;
          cursor: pointer;
          border-bottom: 1px solid #f0f0f0;
          transition: all 0.3s;
          background: #fff;

          &:hover {
            background-color: #f5f7fa;
          }

          &.active {
            background-color: #ecf5ff;
            border-left: 3px solid #409eff;
          }

          .patient-avatar {
            margin-right: 12px;
          }

          .patient-info {
            flex: 1;
            min-width: 0;

            .patient-name {
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
              display: flex;
              align-items: center;
              gap: 8px;
            }

            .patient-last-msg {
              font-size: 12px;
              color: #909399;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }

          .patient-meta {
            text-align: right;
            display: flex;
            flex-direction: column;
            justify-content: space-between;

            .last-time {
              font-size: 11px;
              color: #909399;
              margin-bottom: 4px;
            }

            .unread-count {
              background: #f56c6c;
              color: white;
              border-radius: 10px;
              padding: 2px 6px;
              font-size: 10px;
              min-width: 18px;
              text-align: center;
              line-height: 1.2;
            }
          }
        }
      }
    }

    .chat-area {
      flex: 1;
      display: flex;
      flex-direction: column;

      .no-chat {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #909399;

        i {
          font-size: 64px;
          margin-bottom: 16px;
          color: #c0c4cc;
        }
      }

      .chat-content {
        flex: 1;
        display: flex;
        flex-direction: column;

        .chat-content-header {
          display: flex;
          align-items: center;
          padding: 16px 20px;
          border-bottom: 1px solid #e4e7ed;
          background: #fff;

          .patient-avatar {
            margin-right: 12px;
          }

          .patient-detail {
            flex: 1;

            .name {
              font-weight: 500;
              color: #303133;
              margin-bottom: 2px;
            }

            .status {
              font-size: 12px;

              .online-status {
                color: #67c23a;
              }

              .typing-status {
                color: #409eff;
                font-style: italic;
              }

              .offline-status {
                color: #909399;
              }
            }
          }

          .chat-actions {
            display: flex;
            gap: 8px;
          }
        }

        .message-list {
          flex: 1;
          overflow-y: auto;
          padding: 16px;
          background: #f5f7fa;

          .load-more {
            text-align: center;
            margin-bottom: 16px;

            .loading-text {
              color: #909399;
              font-size: 12px;
            }
          }

          .message-group {
            .date-divider {
              text-align: center;
              color: #909399;
              font-size: 12px;
              margin: 16px 0;
              position: relative;

              &::before {
                content: '';
                position: absolute;
                top: 50%;
                left: 0;
                right: 0;
                height: 1px;
                background: #e4e7ed;
                z-index: 1;
              }

              &::after {
                content: attr(data-date);
                background: #f5f7fa;
                padding: 0 12px;
                position: relative;
                z-index: 2;
              }
            }
          }

          .message-item {
            margin-bottom: 16px;
            display: flex;
            align-items: flex-end;

            &.sent {
              justify-content: flex-end;

              .message-content {
                align-items: flex-end;
              }

              .message-bubble {
                background: #409eff;
                color: white;
                border-bottom-right-radius: 4px;
              }
            }

            &.received {
              justify-content: flex-start;

              .message-content {
                align-items: flex-start;
              }

              .message-bubble {
                background: #fff;
                color: #303133;
                border-bottom-left-radius: 4px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
              }
            }

            .message-avatar {
              margin: 0 8px;
            }

            .message-content {
              display: flex;
              flex-direction: column;
              max-width: 60%;

              .message-bubble {
                padding: 10px 14px;
                border-radius: 8px;
                word-wrap: break-word;
                line-height: 1.4;
                position: relative;

                .message-text {
                  white-space: pre-wrap;
                }

                .message-image {
                  border-radius: 4px;
                  overflow: hidden;
                }

                .message-file {
                  display: flex;
                  align-items: center;
                  gap: 8px;
                  padding: 8px;
                  background: rgba(0, 0, 0, 0.05);
                  border-radius: 4px;
                  margin: 4px 0;
                }

                .message-voice {
                  display: flex;
                  align-items: center;
                  gap: 8px;
                  padding: 8px 12px;
                  background: rgba(0, 0, 0, 0.05);
                  border-radius: 16px;
                  margin: 4px 0;
                  cursor: pointer;

                  &:hover {
                    background: rgba(0, 0, 0, 0.1);
                  }
                }
              }

              .message-meta {
                display: flex;
                align-items: center;
                gap: 4px;
                margin-top: 4px;
                font-size: 11px;
                color: #909399;

                .read-status {
                  .read-icon {
                    color: #67c23a;
                  }

                  .unread-icon {
                    color: #909399;
                  }
                }
              }
            }
          }
        }

        .input-area {
          border-top: 1px solid #e4e7ed;
          background: #fff;
          padding: 16px;

          .input-toolbar {
            display: flex;
            gap: 8px;
            margin-bottom: 12px;

            .upload-btn {
              display: inline-block;
            }
          }

          .input-box {
            display: flex;
            flex-direction: column;
            gap: 8px;

            .el-textarea {
              flex: 1;
            }

            .input-actions {
              display: flex;
              justify-content: space-between;
              align-items: center;

              .input-tip {
                font-size: 12px;
                color: #909399;
              }
            }
          }
        }
      }
    }
  }
}

// 自定义滚动条
.message-list::-webkit-scrollbar {
  width: 6px;
}

.message-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.message-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.message-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>