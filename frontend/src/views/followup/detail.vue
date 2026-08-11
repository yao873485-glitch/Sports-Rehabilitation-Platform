<template>
  <div class="followup-detail-container">
    <el-card>
      <!-- 返回按钮 -->
      <div style="margin-bottom: 20px;">
        <el-button icon="el-icon-back" @click="$router.back()">返回</el-button>
      </div>

      <!-- 随访信息展示区 -->
      <div v-if="followupInfo">
        <!-- 顶部区域：计划名称 -->
        <div class="followup-header">
          <div class="plan-name">
            <span class="label">计划名称：</span>
            <span class="value">{{ followupInfo.followupPlanName || '-' }}</span>
          </div>
        </div>

        <!-- 患者信息展示区 -->
        <div class="patient-info-section">
          <!-- 第一行 -->
          <div class="info-row">
            <div class="info-item">
              <span class="info-label">患者姓名：</span>
              <span class="info-value">{{ followupInfo.patientName }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">患者年龄：</span>
              <span class="info-value">{{ followupInfo.age }}岁</span>
            </div>
            <div class="info-item">
              <span class="info-label">患者性别：</span>
              <span class="info-value">{{ followupInfo.gender }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">患者身份证号：</span>
              <span class="info-value">{{ followupInfo.idCard || '-' }}</span>
            </div>
          </div>

          <!-- 第二行 -->
          <div class="info-row">
            <div class="info-item">
              <span class="info-label">申请时间：</span>
              <span class="info-value">{{ formatDateTime(followupInfo.applicationTime) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">加入项目时间：</span>
              <span class="info-value">{{ formatDateTime(followupInfo.enrollmentTime) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">来源：</span>
              <span class="info-value">{{ followupInfo.source || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">病种：</span>
              <span class="info-value">{{ patientInfo.diseaseType || '-' }}</span>
            </div>
          </div>

          <!-- 第三行 -->
          <div class="info-row">
            <div class="info-item">
              <span class="info-label">手机号：</span>
              <span class="info-value">{{ followupInfo.phone || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">主负责医生：</span>
              <span class="info-value">{{ followupInfo.primaryDoctor || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">健管师：</span>
              <span class="info-value">{{ followupInfo.healthManager || '-' }}</span>
            </div>
          </div>
        </div>

        <!-- 分隔线 -->
        <el-divider />

        <!-- 随访计划展示区 -->
        <div class="followup-plan-content">
          <!-- 随访计划进度 -->
          <div class="followup-progress">
            <span class="progress-text">随访计划进度：已完成</span>
            <span class="progress-value">{{ followupPlanProgress }}%</span>
          </div>

          <!-- 时间轴式任务列表 -->
          <div class="followup-timeline">
            <div
              v-for="(task, index) in followupTasks"
              :key="index"
              class="timeline-item"
            >
              <!-- 左侧时间轴区域 -->
              <div class="timeline-left">
                <div class="timeline-date">{{ task.date }}</div>
                <div class="timeline-status">{{ task.statusText }}</div>
                <div class="timeline-dot"></div>
                <div v-if="index < followupTasks.length - 1" class="timeline-line"></div>
              </div>

              <!-- 中间分隔线 -->
              <div class="timeline-divider"></div>

              <!-- 右侧任务详情卡片 -->
              <div class="timeline-right">
                <el-card class="task-card">
                  <div class="task-header">
                    <h4 class="task-title">{{ task.title }}</h4>
                    <div class="task-actions-inline">
                      <a href="javascript:;" class="action-link" @click="handleGoFill(task)">去填写</a>
                      <span class="divider">|</span>
                      <a href="javascript:;" class="action-link" @click="handleRemindPatient(task)">提醒患者</a>
                      <span class="divider">|</span>
                      <a href="javascript:;" class="action-link" @click="handleTemporaryChange(task)">发起临时变更</a>
                    </div>
                  </div>

                  <div class="task-footer">
                    <el-button
                      type="primary"
                      size="small"
                      @click="handleTemporaryEvent(task)"
                    >
                      发起临时事件
                    </el-button>
                  </div>
                </el-card>
              </div>
            </div>
          </div>
        </div>

        <!-- 分隔线 -->
        <el-divider />

        <!-- 操作日志 -->
        <div class="operation-log">
          <h3 style="margin-bottom: 15px; font-size: 16px; color: #303133;">操作日志</h3>
          <el-table
            :data="operationLogs"
            border
            style="width: 100%"
          >
            <el-table-column prop="operator" label="操作人" width="150" />
            <el-table-column prop="operationTime" label="操作时间" width="180" />
            <el-table-column prop="channel" label="渠道" width="120" />
            <el-table-column prop="action" label="动作" />
          </el-table>
          <div v-if="operationLogs.length === 0" style="text-align: center; padding: 40px; color: #909399;">
            暂无数据
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-else-if="loading" style="text-align: center; padding: 100px;">
        <i class="el-icon-loading" style="font-size: 48px; color: #409eff;"></i>
        <p style="margin-top: 20px; color: #909399;">加载中...</p>
      </div>

      <!-- 暂无数据 -->
      <div v-else style="text-align: center; padding: 100px;">
        <el-empty description="暂无随访管理信息" />
      </div>
    </el-card>
  </div>
</template>

<script>
import { getFollowupDetail } from '@/api/followup'
import { getPatientDetail } from '@/api/patient'

export default {
  name: 'FollowupDetail',
  data() {
    return {
      loading: false,
      followupInfo: null,
      patientInfo: {},
      followupPlanProgress: 0,
      followupTasks: [],
      operationLogs: []
    }
  },
  mounted() {
    this.loadFollowupDetail()
  },
  methods: {
    /** 加载随访详情 */
    async loadFollowupDetail() {
      const followupId = this.$route.query.id
      if (!followupId) {
        this.$message.error('缺少随访记录ID')
        this.$router.back()
        return
      }

      this.loading = true
      try {
        // 加载随访详情
        const response = await getFollowupDetail(followupId)
        this.followupInfo = response.data

        // 加载患者详情
        if (this.followupInfo.patientId) {
          const patientRes = await getPatientDetail(this.followupInfo.patientId)
          this.patientInfo = patientRes.data || {}
        }

        // 初始化随访任务
        this.initFollowupTasks()

        // 加载操作日志（暂无）
        this.operationLogs = []
      } catch (error) {
        console.error('加载随访详情失败:', error)
        this.$message.error('加载随访详情失败')
      } finally {
        this.loading = false
      }
    },

    /** 初始化随访任务 */
    initFollowupTasks() {
      if (!this.followupInfo) {
        this.followupTasks = []
        this.followupPlanProgress = 0
        return
      }

      this.followupTasks = [
        {
          date: this.formatDate(this.followupInfo.enrollmentTime),
          statusText: '已完成',
          title: `${this.followupInfo.followupPlanName || '随访计划'} - 第1期`,
          status: 'completed'
        },
        {
          date: this.getNextPeriodDate(this.followupInfo.enrollmentTime, 1),
          statusText: '进行中',
          title: `${this.followupInfo.followupPlanName || '随访计划'} - 第2期`,
          status: 'inProgress'
        },
        {
          date: this.getNextPeriodDate(this.followupInfo.enrollmentTime, 2),
          statusText: '未开始',
          title: `${this.followupInfo.followupPlanName || '随访计划'} - 第3期`,
          status: 'pending'
        }
      ]

      const completedTasks = this.followupTasks.filter(t => t.status === 'completed').length
      this.followupPlanProgress = Math.round((completedTasks / this.followupTasks.length) * 100)
    },

    /** 格式化日期时间 */
    formatDateTime(datetime) {
      if (!datetime) return '-'
      const date = new Date(datetime)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      }).replace(/\//g, '-')
    },

    /** 格式化日期 */
    formatDate(dateStr) {
      if (!dateStr) return new Date().toISOString().split('T')[0]
      if (typeof dateStr === 'string' && dateStr.includes(' ')) {
        return dateStr.split(' ')[0]
      }
      return new Date(dateStr).toISOString().split('T')[0]
    },

    /** 获取下一期日期（每期间隔30天） */
    getNextPeriodDate(startDate, periodIndex) {
      if (!startDate) {
        const date = new Date()
        date.setDate(date.getDate() + periodIndex * 30)
        return date.toISOString().split('T')[0]
      }
      const date = new Date(startDate)
      date.setDate(date.getDate() + periodIndex * 30)
      return date.toISOString().split('T')[0]
    },

    /** 去填写 */
    handleGoFill(task) {
      this.$message.info('去填写: ' + task.title)
    },

    /** 提醒患者 */
    handleRemindPatient(task) {
      this.$message.info('提醒患者: ' + task.title)
    },

    /** 发起临时变更 */
    handleTemporaryChange(task) {
      this.$message.info('发起临时变更: ' + task.title)
    },

    /** 发起临时事件 */
    handleTemporaryEvent(task) {
      this.$message.info('发起临时事件: ' + task.title)
    }
  }
}
</script>

<style lang="scss" scoped>
.followup-detail-container {
  padding: 20px;
}

.followup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e4e7ed;

  .plan-name {
    font-size: 18px;
    font-weight: 600;
    color: #303133;

    .label {
      color: #606266;
      margin-right: 8px;
    }

    .value {
      color: #303133;
    }
  }
}

.patient-info-section {
  background-color: #f9fafb;
  padding: 20px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  margin-bottom: 20px;

  .info-row {
    display: flex;
    margin-bottom: 15px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .info-item {
    flex: 1;
    display: flex;
    align-items: center;
    font-size: 14px;
  }

  .info-label {
    color: #606266;
    margin-right: 8px;
    white-space: nowrap;
  }

  .info-value {
    color: #303133;
    font-weight: 500;
  }
}

.followup-plan-content {
  margin-top: 20px;
}

.followup-progress {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #f0f9ff;
  border-radius: 4px;
  margin-bottom: 25px;

  .progress-text {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
  }

  .progress-value {
    font-size: 24px;
    font-weight: 600;
    color: #409eff;
  }
}

.followup-timeline {
  .timeline-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 30px;
    position: relative;

    &:last-child {
      margin-bottom: 0;

      .timeline-line {
        display: none;
      }
    }
  }

  .timeline-left {
    width: 180px;
    flex-shrink: 0;
    text-align: right;
    padding-right: 25px;
    position: relative;
  }

  .timeline-date {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
    margin-bottom: 8px;
  }

  .timeline-status {
    font-size: 14px;
    color: #909399;
  }

  .timeline-dot {
    position: absolute;
    right: -8px;
    top: 5px;
    width: 16px;
    height: 16px;
    background-color: #409eff;
    border: 3px solid #fff;
    border-radius: 50%;
    box-shadow: 0 0 0 2px #409eff;
  }

  .timeline-line {
    position: absolute;
    right: 0;
    top: 21px;
    width: 2px;
    height: calc(100% + 30px);
    background-color: #e4e7ed;
  }

  .timeline-divider {
    width: 2px;
    background-color: #e4e7ed;
    margin: 0 20px;
  }

  .timeline-right {
    flex: 1;
  }

  .task-card {
    border: 1px solid #e4e7ed;
    border-radius: 8px;

    ::v-deep .el-card__body {
      padding: 20px;
    }
  }

  .task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .task-title {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
    margin: 0;
  }

  .task-actions-inline {
    display: flex;
    align-items: center;
    gap: 8px;

    .action-link {
      color: rgb(106, 91, 140);
      font-size: 14px;
      text-decoration: none;

      &:hover {
        color: rgb(96, 81, 130);
        text-decoration: underline;
      }
    }

    .divider {
      color: #dcdfe6;
    }
  }

  .task-footer {
    display: flex;
    justify-content: flex-end;
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid #f0f0f0;
  }
}

.operation-log {
  margin-top: 30px;
}
</style>
