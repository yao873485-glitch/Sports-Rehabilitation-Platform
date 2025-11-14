<template>
  <div class="chat-room">
    <el-card class="chat-container">
      <!-- 聊天头部 -->
      <div slot="header" class="chat-header">
        <span>{{ chatTitle }}</span>
        <el-tag :type="isConnected ? 'success' : 'danger'" size="small">
          {{ isConnected ? '已连接' : '未连接' }}
        </el-tag>
      </div>

      <!-- 消息列表区域 -->
      <div class="message-list" ref="messageList">
        <div
          v-for="message in messages"
          :key="message.id"
          :class="['message-item', message.senderId === currentUserId ? 'message-sent' : 'message-received']"
        >
          <div class="message-info">
            <span class="sender-name">{{ message.senderName }}</span>
            <span class="send-time">{{ formatTime(message.sendTime) }}</span>
          </div>
          <div class="message-content">
            <div class="message-bubble">
              {{ message.content }}
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <el-empty v-if="messages.length === 0" description="暂无聊天消息"></el-empty>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="输入消息内容..."
          @keyup.enter.native="handleSendMessage"
        />
        <div class="input-actions">
          <el-button type="primary" size="small" @click="handleSendMessage" :disabled="!isConnected || !inputMessage.trim()">
            发送 (Enter)
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import ChatService from '@/services/ChatService'
import { getChatHistory, markAsRead } from '@/api/chat'
import { mapGetters } from 'vuex'

export default {
  name: 'ChatRoom',

  data() {
    return {
      messages: [],
      inputMessage: '',
      isConnected: false,
      receiverId: null,
      receiverName: '',
      conversationId: '',
      loading: false,
      page: 1,
      pageSize: 50
    }
  },

  computed: {
    ...mapGetters(['userId', 'username']),

    currentUserId() {
      return this.userId
    },

    chatTitle() {
      return this.receiverName ? `与 ${this.receiverName} 的对话` : '聊天室'
    }
  },

  mounted() {
    // 从路由参数获取接收者信息
    this.receiverId = this.$route.query.receiverId
    this.receiverName = this.$route.query.receiverName || '对方'

    // 初始化聊天
    this.initChat()
  },

  beforeDestroy() {
    // 组件销毁时断开 WebSocket 连接
    this.disconnectWebSocket()
  },

  methods: {
    /**
     * 初始化聊天
     */
    async initChat() {
      // 加载历史消息
      await this.loadChatHistory()

      // 连接 WebSocket
      this.connectWebSocket()

      // 标记消息为已读
      this.markMessagesAsRead()
    },

    /**
     * 连接 WebSocket
     */
    connectWebSocket() {
      ChatService.connect(
        this.currentUserId,
        () => {
          console.log('WebSocket 连接成功')
          this.isConnected = true

          // 注册消息处理器
          ChatService.onMessage(this.handleNewMessage)
        },
        (error) => {
          console.error('WebSocket 连接失败', error)
          this.isConnected = false
          this.$message.error('聊天连接失败，请刷新页面重试')
        }
      )
    },

    /**
     * 断开 WebSocket
     */
    disconnectWebSocket() {
      ChatService.offMessage(this.handleNewMessage)
      ChatService.disconnect()
      this.isConnected = false
    },

    /**
     * 加载聊天历史
     */
    async loadChatHistory() {
      this.loading = true
      try {
        const params = {
          receiverId: this.receiverId,
          pageNum: this.page,
          pageSize: this.pageSize
        }

        const response = await getChatHistory(params)
        if (response.code === 200) {
          this.messages = response.data.records || []
          this.conversationId = this.messages.length > 0 ? this.messages[0].conversationId : ''

          // 滚动到底部
          this.$nextTick(() => {
            this.scrollToBottom()
          })
        }
      } catch (error) {
        console.error('加载聊天历史失败', error)
        this.$message.error('加载聊天记录失败')
      } finally {
        this.loading = false
      }
    },

    /**
     * 处理新消息
     */
    handleNewMessage(message) {
      // 只处理与当前对话相关的消息
      if (message.senderId === this.receiverId || message.receiverId === this.receiverId) {
        this.messages.push(message)

        // 滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom()
        })

        // 如果是接收到的消息，标记为已读
        if (message.receiverId === this.currentUserId) {
          this.markMessagesAsRead()
        }
      }
    },

    /**
     * 发送消息
     */
    handleSendMessage() {
      const content = this.inputMessage.trim()
      if (!content) {
        return
      }

      if (!this.isConnected) {
        this.$message.warning('聊天未连接，请稍后重试')
        return
      }

      // 构造消息对象
      const message = {
        receiverId: this.receiverId,
        content: content,
        messageType: 'text',
        senderType: 'user'
      }

      // 通过 WebSocket 发送
      const success = ChatService.sendMessage(message)

      if (success) {
        // 清空输入框
        this.inputMessage = ''
      } else {
        this.$message.error('消息发送失败')
      }
    },

    /**
     * 标记消息为已读
     */
    async markMessagesAsRead() {
      if (!this.conversationId) return

      try {
        await markAsRead({
          conversationId: this.conversationId,
          receiverId: this.currentUserId
        })
      } catch (error) {
        console.error('标记已读失败', error)
      }
    },

    /**
     * 滚动到底部
     */
    scrollToBottom() {
      const container = this.$refs.messageList
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    },

    /**
     * 格式化时间
     */
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date

      // 今天
      if (diff < 86400000 && now.getDate() === date.getDate()) {
        return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
      }

      // 昨天
      if (diff < 172800000) {
        return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
      }

      // 其他日期
      return date.toLocaleString('zh-CN', {
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped lang="scss">
.chat-room {
  height: calc(100vh - 120px);
  padding: 20px;

  .chat-container {
    height: 100%;
    display: flex;
    flex-direction: column;

    .chat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 16px;
      font-weight: 500;
    }

    .message-list {
      flex: 1;
      overflow-y: auto;
      padding: 20px;
      background-color: #f5f7fa;
      border-radius: 4px;
      margin-bottom: 20px;

      .message-item {
        margin-bottom: 20px;
        display: flex;
        flex-direction: column;

        &.message-sent {
          align-items: flex-end;

          .message-bubble {
            background-color: #409eff;
            color: white;
          }

          .message-info {
            justify-content: flex-end;
          }
        }

        &.message-received {
          align-items: flex-start;

          .message-bubble {
            background-color: white;
            color: #303133;
          }

          .message-info {
            justify-content: flex-start;
          }
        }

        .message-info {
          display: flex;
          gap: 10px;
          margin-bottom: 5px;
          font-size: 12px;
          color: #909399;

          .sender-name {
            font-weight: 500;
          }
        }

        .message-content {
          max-width: 70%;

          .message-bubble {
            padding: 10px 15px;
            border-radius: 8px;
            word-break: break-word;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
          }
        }
      }
    }

    .input-area {
      .input-actions {
        margin-top: 10px;
        display: flex;
        justify-content: flex-end;
      }
    }
  }
}

/* 滚动条样式 */
.message-list::-webkit-scrollbar {
  width: 6px;
}

.message-list::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

.message-list::-webkit-scrollbar-thumb:hover {
  background-color: #c0c4cc;
}
</style>
