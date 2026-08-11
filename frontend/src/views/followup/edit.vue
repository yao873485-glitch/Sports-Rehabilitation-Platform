<template>
  <div class="followup-detail-container">
    <el-card>
      <!-- 返回按钮 -->
      <div style="margin-bottom: 20px;">
        <el-button icon="el-icon-back" @click="$router.back()">返回</el-button>
      </div>

      <!-- 随访信息展示区 -->
      <div v-if="followupInfo">
        <!-- 顶部区域：计划名称 + 编辑按钮 -->
        <div class="followup-header">
          <div class="plan-name">
            <span class="label">计划名称：</span>
            <span class="value">{{ followupInfo.followupPlanName || '-' }}</span>
          </div>
          <el-button type="primary" @click="handleEditPlan">
            编辑当前计划
          </el-button>
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

    <!-- 编辑计划对话框 -->
    <el-dialog
      :visible.sync="editPlanDialogVisible"
      width="900px"
      :close-on-click-modal="false"
      :show-close="false"
      fullscreen
    >
      <!-- 顶部导航区 -->
      <div slot="title" class="edit-plan-header">
        <div class="header-left">
          <a href="javascript:;" class="back-link" @click="handleCancelEditPlan">
            <i class="el-icon-arrow-left"></i> 返回
          </a>
          <span class="edit-title">编辑</span>
        </div>
        <el-button class="cancel-btn" @click="handleCancelEditPlan">取消</el-button>
      </div>

      <div class="edit-plan-content">
        <!-- 基本信息展示区 -->
        <div class="basic-info-section">
          <div class="info-row">
            <div class="info-label">计划名称：</div>
            <div class="info-value">{{ planForm.planName }}</div>
          </div>
          <div class="info-row">
            <div class="info-item">
              <span class="info-label">创建时间：</span>
              <span class="info-value">{{ planForm.createTime }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">最后一次修改时间：</span>
              <span class="info-value">{{ planForm.lastModifyTime }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">状态：</span>
              <span class="info-value">{{ planForm.status }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">当前版本号：</span>
              <span class="info-value">{{ planForm.version }}</span>
            </div>
          </div>
        </div>

        <!-- 可见团队区域 -->
        <div class="section">
          <h3>可见团队（{{ planForm.visibleTeam.length }}）</h3>
        </div>

        <!-- 计划描述区 -->
        <div class="section">
          <h3>计划描述</h3>
          <el-input
            v-model="planForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入计划描述"
          />
        </div>

        <!-- 功能操作区 -->
        <div class="section">
          <el-button type="primary" icon="el-icon-plus" plain @click="handleAddFollowupTask">
            新增随访任务
          </el-button>
        </div>

        <!-- 随访任务列表 -->
        <div v-for="(task, index) in planForm.followupTasks" :key="index" class="followup-task-item">
          <div class="task-header">
            <span class="task-title">第{{ task.orderNum }}次随访</span>
            <div class="task-actions">
              <i class="el-icon-edit" @click="handleEditTaskName(index)"></i>
              <i class="el-icon-delete" @click="handleDeleteTask(index)"></i>
            </div>
          </div>
          <div class="task-interval">
            <el-button icon="el-icon-minus" size="mini" @click="task.intervalDays--"></el-button>
            <el-input-number v-model="task.intervalDays" :min="1" size="mini" style="width: 80px; margin: 0 10px;" />
            <el-select v-model="task.intervalUnit" size="mini" style="width: 80px;">
              <el-option label="天" value="天" />
              <el-option label="周" value="周" />
              <el-option label="月" value="月" />
            </el-select>
            <el-button
              icon="el-icon-plus"
              size="mini"
              circle
              type="primary"
              style="margin-left: 20px;"
              @click="handleAddFollowupEvent(index)"
            />
          </div>
        </div>

        <!-- 底部提示信息 -->
        <div class="tip-text">
          无论是否选择提前提醒或超时提醒，在计划随访的当天早上10点，会推送一条对应的随访任务消息给到患者
        </div>

        <!-- 提前提醒 -->
        <div class="remind-section">
          <div class="remind-header">
            <el-checkbox v-model="advanceRemind.enabled">提前提醒</el-checkbox>
            <span class="remind-desc">
              未到随访时间，提前提醒用户将要随访，用户完成随访任务的当天，将立即结束提醒
            </span>
          </div>
          <div v-if="advanceRemind.enabled" class="remind-config">
            <span>在计划随访的</span>
            <el-input-number v-model="advanceRemind.days" :min="1" size="small" style="width: 80px; margin: 0 5px;" />
            <el-select v-model="advanceRemind.unit" size="small" style="width: 80px; margin: 0 5px;">
              <el-option label="天" value="天" />
              <el-option label="周" value="周" />
              <el-option label="小时" value="小时" />
            </el-select>
            <span>前提醒患者</span>
            <el-time-picker
              v-model="advanceRemind.time"
              format="HH:mm"
              value-format="HH:mm"
              size="small"
              style="width: 120px; margin: 0 10px;"
            />
            <el-button type="primary" size="small">发送</el-button>
          </div>
        </div>

        <!-- 超时提醒 -->
        <div class="remind-section">
          <div class="remind-header">
            <el-checkbox v-model="overtimeRemind.enabled">超时提醒</el-checkbox>
            <span class="remind-desc">
              已过随访时间，用户仍然未完成随访任务，提醒用户进行随访任务，用户完成随访任务的当天，将立即结束提醒
            </span>
          </div>
          <div v-if="overtimeRemind.enabled" class="remind-config">
            <span>超过计划随访的</span>
            <el-input-number v-model="overtimeRemind.days" :min="1" size="small" style="width: 80px; margin: 0 5px;" />
            <el-select v-model="overtimeRemind.unit" size="small" style="width: 80px; margin: 0 5px;">
              <el-option label="天" value="天" />
              <el-option label="周" value="周" />
              <el-option label="小时" value="小时" />
            </el-select>
            <span>后提醒患者</span>
            <el-input-number v-model="overtimeRemind.times" :min="1" size="small" style="width: 80px; margin: 0 10px;" />
            <span>次</span>
            <el-time-picker
              v-model="overtimeRemind.time"
              format="HH:mm"
              value-format="HH:mm"
              size="small"
              style="width: 120px; margin: 0 10px;"
            />
            <el-button type="primary" size="small">发送</el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFollowupDetail } from '@/api/followup'
import { getPatientDetail } from '@/api/patient'

export default {
  name: 'FollowupEdit',
  data() {
    return {
      loading: false,
      followupInfo: null,
      patientInfo: {},
      followupPlanProgress: 0,
      followupTasks: [],
      operationLogs: [],
      // 编辑计划对话框
      editPlanDialogVisible: false,
      planForm: {
        planName: '',
        createTime: '',
        lastModifyTime: '',
        status: '启用',
        version: '1.0',
        visibleTeam: [],
        description: '',
        followupTasks: []
      },
      // 提前提醒设置
      advanceRemind: {
        enabled: true,
        days: 1,
        unit: '天',
        time: '09:00'
      },
      // 超时提醒设置
      overtimeRemind: {
        enabled: true,
        days: 1,
        unit: '天',
        times: 1,
        time: '09:00'
      }
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
    },

    /** 编辑当前计划 */
    handleEditPlan() {
      // 判断是否有随访计划
      if (!this.followupInfo.followupPlanName || this.followupInfo.followupPlanName.trim() === '') {
        this.$message.error('当前没有随访计划')
        return
      }

      // 初始化计划表单数据
      this.planForm = {
        planName: this.followupInfo.followupPlanName,
        createTime: this.formatDate(new Date()),
        lastModifyTime: this.formatDate(new Date()),
        status: '启用',
        version: '1.0',
        visibleTeam: [],
        description: '',
        followupTasks: []
      }

      // 打开编辑对话框
      this.editPlanDialogVisible = true
    },

    /** 取消编辑计划 */
    handleCancelEditPlan() {
      this.editPlanDialogVisible = false
    },

    /** 新增随访任务 */
    handleAddFollowupTask() {
      const orderNum = this.planForm.followupTasks.length + 1
      this.planForm.followupTasks.push({
        orderNum: orderNum,
        intervalDays: 10,
        intervalUnit: '天'
      })
    },

    /** 编辑任务名称 */
    handleEditTaskName(index) {
      this.$prompt('请输入新的随访次数', '编辑随访名称', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\d+$/,
        inputErrorMessage: '请输入数字'
      }).then(({ value }) => {
        this.planForm.followupTasks[index].orderNum = parseInt(value)
        this.$message.success('修改成功')
      }).catch(() => {})
    },

    /** 删除任务 */
    handleDeleteTask(index) {
      this.$confirm('确认删除该随访任务吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.planForm.followupTasks.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /** 新增随访事件 */
    handleAddFollowupEvent(index) {
      this.$message.info('新增随访事件功能开发中...')
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

// 编辑计划对话框样式
::v-deep .edit-plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;

  .header-left {
    display: flex;
    align-items: center;
    gap: 20px;

    .back-link {
      font-size: 14px;
      color: #606266;
      text-decoration: none;
      display: flex;
      align-items: center;
      gap: 5px;

      &:hover {
        color: #409eff;
      }
    }

    .edit-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .cancel-btn {
    background-color: #f5f7fa;
    border-color: #dcdfe6;
    color: #606266;
  }
}

.edit-plan-content {
  padding: 30px;
  max-height: calc(100vh - 120px);
  overflow-y: auto;

  .basic-info-section {
    margin-bottom: 30px;
    padding: 20px;
    background-color: #f9fafb;
    border-radius: 4px;

    .info-row {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      margin-bottom: 15px;

      &:last-child {
        margin-bottom: 0;
      }

      .info-label {
        font-size: 14px;
        color: #606266;
        font-weight: 500;
      }

      .info-value {
        font-size: 14px;
        color: #303133;
      }

      .info-item {
        display: flex;
        align-items: center;
        gap: 5px;
      }
    }
  }

  .section {
    margin-bottom: 25px;

    h3 {
      font-size: 15px;
      color: #303133;
      margin-bottom: 15px;
      font-weight: 500;
    }
  }

  .followup-task-item {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;
    border-left: 3px solid #409eff;

    .task-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      .task-title {
        font-size: 14px;
        color: #409eff;
        font-weight: 500;
      }

      .task-actions {
        display: flex;
        gap: 15px;

        i {
          font-size: 16px;
          color: #909399;
          cursor: pointer;

          &:hover {
            color: #409eff;
          }

          &.el-icon-delete:hover {
            color: #f56c6c;
          }
        }
      }
    }

    .task-interval {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }

  .tip-text {
    margin: 30px 0;
    padding: 12px;
    background-color: #fef0f0;
    border-left: 3px solid #f56c6c;
    font-size: 13px;
    color: #909399;
    line-height: 1.6;
  }

  .remind-section {
    margin-bottom: 25px;
    padding: 20px;
    background-color: #f9fafb;
    border-radius: 4px;

    .remind-header {
      margin-bottom: 15px;

      .remind-desc {
        font-size: 13px;
        color: #606266;
        margin-left: 10px;
      }
    }

    .remind-config {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 5px;
      padding-left: 30px;

      span {
        font-size: 14px;
        color: #606266;
      }
    }
  }
}
</style>
