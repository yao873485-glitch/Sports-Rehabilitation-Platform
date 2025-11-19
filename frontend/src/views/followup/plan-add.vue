<template>
  <div class="plan-add-container">
    <div class="header">
      <el-button icon="el-icon-arrow-left" @click="handleBack">返回</el-button>
      <span class="title">新增</span>
      <div class="header-actions">
        <el-button @click="handleSaveDraft">保存至草稿</el-button>
        <el-button type="primary" @click="handlePublish">直接发布</el-button>
      </div>
    </div>

    <!-- 步骤条 -->
    <div class="steps-container">
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step title="随访计划基本信息"></el-step>
        <el-step title="随访计划任务配置"></el-step>
        <el-step title="随访计划提醒设置"></el-step>
      </el-steps>
    </div>

    <!-- 步骤内容 -->
    <div class="step-content">
      <!-- 步骤1: 基本信息 -->
      <div v-show="currentStep === 0" class="step-panel">
        <el-form :model="basicForm" :rules="basicRules" ref="basicForm" label-width="120px">
          <el-form-item label="计划名称" prop="planName" required>
            <el-input
              v-model="basicForm.planName"
              placeholder="请输入计划名称"
              maxlength="30"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="随访项目" prop="linkedProjectId" required>
            <el-select
              v-model="basicForm.linkedProjectId"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option
                v-for="project in projectList"
                :key="project.id"
                :label="project.projectName"
                :value="project.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="可用团队" prop="availableTeam" required>
            <el-select
              v-model="basicForm.availableTeam"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option label="团队A" value="团队A" />
              <el-option label="团队B" value="团队B" />
              <el-option label="团队C" value="团队C" />
            </el-select>
          </el-form-item>

          <el-form-item prop="ownerInstitution" required>
            <span slot="label">所属机构</span>
            <el-select
              v-model="basicForm.ownerInstitution"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option label="机构A" value="机构A" />
              <el-option label="机构B" value="机构B" />
              <el-option label="机构C" value="机构C" />
            </el-select>
          </el-form-item>

          <el-form-item label="首次随访基线" prop="firstFollowupBaseline" required>
            <el-select
              v-model="basicForm.firstFollowupBaseline"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option label="基线1" value="基线1" />
              <el-option label="基线2" value="基线2" />
              <el-option label="基线3" value="基线3" />
            </el-select>
          </el-form-item>

          <el-form-item label="计划描述" prop="planDescription" required>
            <el-input
              v-model="basicForm.planDescription"
              type="textarea"
              :rows="4"
              placeholder="请输入"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤2: 任务配置 -->
      <div v-show="currentStep === 1" class="step-panel">
        <div class="task-config-header">
          <p class="hint-text">
            无治疗者或结束治疗服药3周后每次问诊间隔，在开始随访时支付至10周，参照一年内进行咨询访视
          </p>
          <el-button type="primary" plain icon="el-icon-plus" @click="handleAddTask">
            新增随访任务
          </el-button>
        </div>

        <div class="task-list">
          <div v-for="(task, index) in taskList" :key="index" class="task-item">
            <div class="task-header">
              <el-checkbox v-model="task.enabled">{{ task.name }}</el-checkbox>
              <el-button type="text" icon="el-icon-delete" @click="handleDeleteTask(index)">
                删除
              </el-button>
            </div>
            <div class="task-content" v-if="task.enabled">
              <el-form :model="task" label-width="120px" size="small">
                <el-form-item label="任务名称">
                  <el-input v-model="task.name" placeholder="请输入任务名称" />
                </el-form-item>
                <el-form-item label="执行时间">
                  <el-input-number v-model="task.executeDay" :min="1" /> 天后执行
                </el-form-item>
                <el-form-item label="任务内容">
                  <el-input
                    v-model="task.content"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入任务内容"
                  />
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>

      <!-- 步骤3: 提醒设置 -->
      <div v-show="currentStep === 2" class="step-panel">
        <div class="reminder-section">
          <el-checkbox v-model="reminderForm.earlyReminderEnabled">
            <span class="reminder-title">提前提醒</span>
          </el-checkbox>
          <p class="reminder-desc" v-if="reminderForm.earlyReminderEnabled">
            未开始访视的，提前通过手机或短信提醒患者，用于完成访视必须在该上次访视上支付。参照进行进行咨询访视
          </p>
          <el-form v-if="reminderForm.earlyReminderEnabled" :model="reminderForm" label-width="120px" size="small">
            <el-form-item label="提前时间">
              <el-input-number v-model="reminderForm.earlyDays" :min="1" :max="30" /> 天
            </el-form-item>
            <el-form-item label="提醒方式">
              <el-checkbox-group v-model="reminderForm.earlyMethods">
                <el-checkbox label="短信">短信</el-checkbox>
                <el-checkbox label="APP推送">APP推送</el-checkbox>
                <el-checkbox label="微信">微信</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </div>

        <div class="reminder-section">
          <el-checkbox v-model="reminderForm.overdueReminderEnabled">
            <span class="reminder-title">逾期提醒</span>
          </el-checkbox>
          <p class="reminder-desc" v-if="reminderForm.overdueReminderEnabled">
            已过期访视时间的，用户完成访视必须在该上次访视上支付，观察可完成访视后的处在，将不完成访视者可处在随访
          </p>
          <el-form v-if="reminderForm.overdueReminderEnabled" :model="reminderForm" label-width="120px" size="small">
            <el-form-item label="逾期天数">
              <el-input-number v-model="reminderForm.overdueDays" :min="1" :max="30" /> 天
            </el-form-item>
            <el-form-item label="提醒方式">
              <el-checkbox-group v-model="reminderForm.overdueMethods">
                <el-checkbox label="短信">短信</el-checkbox>
                <el-checkbox label="APP推送">APP推送</el-checkbox>
                <el-checkbox label="微信">微信</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 底部按钮 -->
    <div class="footer-buttons">
      <el-button v-if="currentStep > 0" @click="handlePrevStep">上一步</el-button>
      <el-button v-if="currentStep < 2" type="primary" @click="handleNextStep">下一步</el-button>
    </div>
  </div>
</template>

<script>
import { getFollowupProjectConfigList, saveFollowupPlan } from '@/api/followup'

export default {
  name: 'FollowupPlanAdd',
  mounted() {
    this.loadProjectList()
  },
  data() {
    return {
      currentStep: 0,
      // 基本信息表单
      basicForm: {
        planName: '',
        planDescription: '',
        linkedProjectId: '',
        availableTeam: '',
        ownerInstitution: '',
        firstFollowupBaseline: ''
      },
      basicRules: {
        planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
        linkedProjectId: [{ required: true, message: '请选择随访项目', trigger: 'change' }],
        availableTeam: [{ required: true, message: '请选择可用团队', trigger: 'change' }],
        ownerInstitution: [{ required: true, message: '请选择所属机构', trigger: 'change' }],
        firstFollowupBaseline: [{ required: true, message: '请选择首次随访基线', trigger: 'change' }],
        planDescription: [{ required: true, message: '请输入计划描述', trigger: 'blur' }]
      },
      // 随访项目列表
      projectList: [],
      // 任务列表
      taskList: [],
      // 提醒设置表单
      reminderForm: {
        earlyReminderEnabled: false,
        earlyDays: 3,
        earlyMethods: [],
        overdueReminderEnabled: false,
        overdueDays: 7,
        overdueMethods: []
      }
    }
  },
  methods: {
    handleBack() {
      this.$router.back()
    },
    handleSaveDraft() {
      // 保存为草稿
      this.$refs.basicForm.validate(valid => {
        if (valid) {
          this.savePlan(true)
        } else {
          this.$message.warning('请完善基本信息')
        }
      })
    },
    handlePublish() {
      // 直接发布
      this.$refs.basicForm.validate(valid => {
        if (valid) {
          this.savePlan(false)
        } else {
          this.$message.warning('请完善基本信息')
        }
      })
    },
    // 保存计划
    async savePlan(isDraft) {
      try {
        const planData = {
          ...this.basicForm,
          isDraft: isDraft,
          operator: '系统'
        }

        const response = await saveFollowupPlan(planData)

        if (isDraft) {
          this.$message.success('保存草稿成功')
        } else {
          this.$message.success('发布成功')
        }

        // 返回列表页
        this.$router.push('/followup/plan')
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error('保存失败：' + (error.message || '未知错误'))
      }
    },
    handlePrevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    handleNextStep() {
      if (this.currentStep === 0) {
        // 验证基本信息
        this.$refs.basicForm.validate(valid => {
          if (valid) {
            this.currentStep++
          }
        })
      } else if (this.currentStep < 2) {
        this.currentStep++
      }
    },
    handleAddTask() {
      this.taskList.push({
        name: `任务${this.taskList.length + 1}`,
        enabled: true,
        executeDay: 1,
        content: ''
      })
    },
    handleDeleteTask(index) {
      this.taskList.splice(index, 1)
    },
    // 加载随访项目列表
    async loadProjectList() {
      try {
        const response = await getFollowupProjectConfigList({
          pageNum: 1,
          pageSize: 100,
          isPublished: 1 // 只获取已发布的项目
        })
        const pageData = response.data || { records: [], total: 0 }
        this.projectList = (pageData.records || []).map(item => ({
          id: item.id,
          projectName: item.projectName || item.project_name
        }))
      } catch (error) {
        console.error('加载随访项目列表失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.plan-add-container {
  min-height: 100vh;
  background: #f5f5f5;

  .header {
    background: #fff;
    padding: 16px 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);

    .title {
      font-size: 18px;
      font-weight: 500;
      margin-left: 16px;
    }

    .header-actions {
      margin-left: auto;
      display: flex;
      gap: 12px;
    }
  }

  .steps-container {
    background: #fff;
    padding: 40px 24px;
    margin: 20px auto;
    max-width: 1200px;
  }

  .step-content {
    background: #fff;
    margin: 0 auto 20px;
    max-width: 1200px;
    min-height: 400px;
    padding: 30px 40px;

    .step-panel {
      max-width: 800px;
      margin: 0 auto;
    }

    .task-config-header {
      margin-bottom: 20px;

      .hint-text {
        color: #909399;
        font-size: 14px;
        line-height: 1.6;
        margin-bottom: 16px;
      }
    }

    .task-list {
      .task-item {
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        margin-bottom: 16px;
        padding: 16px;

        .task-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12px;
        }

        .task-content {
          padding-top: 12px;
          border-top: 1px solid #f0f0f0;
        }
      }
    }

    .reminder-section {
      margin-bottom: 30px;
      padding: 20px;
      background: #f9f9f9;
      border-radius: 4px;

      .reminder-title {
        font-size: 16px;
        font-weight: 500;
      }

      .reminder-desc {
        color: #909399;
        font-size: 14px;
        line-height: 1.6;
        margin: 12px 0 16px;
        padding-left: 24px;
      }
    }
  }

  .footer-buttons {
    background: #fff;
    padding: 16px 24px;
    text-align: center;
    box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.08);
    position: sticky;
    bottom: 0;

    .el-button {
      min-width: 100px;
      margin: 0 8px;
    }
  }
}
</style>
