<template>
  <el-dialog
    :visible.sync="visible"
    :title="dialogTitle"
    width="90%"
    :close-on-click-modal="false"
    :before-close="handleClose"
    custom-class="execution-dialog"
  >
    <div v-loading="loading" class="execution-content">
      <!-- 患者基本信息 -->
      <div class="patient-info-section">
        <div class="info-grid">
          <div class="info-item">
            <div class="info-label">姓名</div>
            <div class="info-value">{{ patientInfo.patientName }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">性别</div>
            <div class="info-value">{{ patientInfo.gender }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">年龄</div>
            <div class="info-value">{{ patientInfo.age }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">档案号</div>
            <div class="info-value">{{ patientInfo.medicalRecordNo }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">电话</div>
            <div class="info-value">{{ patientInfo.phone }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">方案编号</div>
            <div class="info-value">{{ patientInfo.medicalRecordNo }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">入组日期</div>
            <div class="info-value">{{ formatDate(patientInfo.enrollmentDate) }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">记录日期</div>
            <div class="info-value">
              <el-date-picker
                v-model="formData.recordDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- IPAQ问卷 -->
      <div class="questionnaire-section">
        <!-- 问题1 -->
        <div class="question-item">
          <div class="question-title">1、最近 7 天内，您有几天做了剧烈的体育活动，像是提重物、挖掘、有氧运动 或是快速骑车？</div>
          <div class="question-subtitle">每周(天)</div>
          <el-input v-model="formData.question1Answer" placeholder="请输入" />
          <el-radio v-model="formData.question1SkipReason" label="无相关体育活动">无相关体育活动 →跳到问题 3</el-radio>
        </div>

        <!-- 问题2 -->
        <div class="question-item">
          <div class="question-title">2、在这其中一天您通常会花多少时间在剧烈的体育活动上？</div>
          <div class="question-subtitle">每天（时/分）</div>
          <el-input v-model="formData.question2Answer" placeholder="请输入" />
          <el-checkbox v-model="formData.question2Unknown" :true-label="1" :false-label="0">不知道或不确定</el-checkbox>
        </div>

        <!-- 问题3 -->
        <div class="question-item">
          <div class="question-title">3、最近 7 天内，您有几天做了适度的体育活动，像是提轻的物品、以平常的速 度骑车或打双人网球？ 请不要包括走路。</div>
          <div class="question-subtitle">每周（天）</div>
          <el-input v-model="formData.question3Answer" placeholder="请输入" />
          <el-radio v-model="formData.question3SkipReason" label="无适度体育活动">无适度体育活动 →跳到问题 5</el-radio>
        </div>

        <!-- 问题4 -->
        <div class="question-item">
          <div class="question-title">4、在这其中一天您通常会花多少时间在适度的体育活动上？</div>
          <div class="question-subtitle">每天（时/分）</div>
          <el-input v-model="formData.question4Answer" placeholder="请输入" />
          <el-checkbox v-model="formData.question4Unknown" :true-label="1" :false-label="0">不知道或不确定</el-checkbox>
        </div>

        <!-- 问题5 -->
        <div class="question-item">
          <div class="question-title">5、最近 7 天内，您有几天是步行，且一次步行至少 10 分钟？</div>
          <div class="question-subtitle">每周（天）</div>
          <el-input v-model="formData.question5Answer" placeholder="请输入" />
          <el-radio v-model="formData.question5SkipReason" label="没有步行">没有步行 →跳到问题 7</el-radio>
        </div>

        <!-- 问题6 -->
        <div class="question-item">
          <div class="question-title">6、在这其中一天您通常会花多少时间在步行上？</div>
          <div class="question-subtitle">每天（时/分）</div>
          <el-input v-model="formData.question6Answer" placeholder="请输入" />
          <el-checkbox v-model="formData.question6Unknown" :true-label="1" :false-label="0">不知道或不确定</el-checkbox>
        </div>

        <!-- 问题7 -->
        <div class="question-item">
          <div class="question-title">7、最近七天内，工作日您有多久时间是坐着的？</div>
          <div class="question-subtitle">每天（时/分）</div>
          <el-input v-model="formData.question7Answer" placeholder="请输入" />
          <el-checkbox v-model="formData.question7Unknown" :true-label="1" :false-label="0">不知道或不确定</el-checkbox>
        </div>

        <!-- 执行信息 -->
        <div class="execution-info">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="执行人员" label-width="120px">
                <el-input v-model="formData.executor" placeholder="请输入执行人员" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="执行时间" label-width="120px">
                <el-input :value="formatDateTime(formData.executionTime)" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 提交按钮 -->
        <div class="submit-section">
          <el-button type="primary" size="large" @click="handleSubmit">提交</el-button>
        </div>
      </div>
    </div>

    <!-- 底部按钮 -->
    <div slot="footer" class="dialog-footer">
      <el-button class="save-btn" @click="handleSave">暂存</el-button>
      <el-button class="confirm-btn" type="primary" @click="handleConfirm">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getAssessmentExecutionInfo, submitAssessmentExecution } from '@/api/assessment-execution'

export default {
  name: 'ExecutionDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assessmentPlanId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      loading: false,
      patientInfo: {},
      formData: {
        assessmentPlanId: null,
        patientId: null,
        recordDate: new Date(),
        question1Answer: '',
        question1SkipReason: '',
        question2Answer: '',
        question2Unknown: 0,
        question3Answer: '',
        question3SkipReason: '',
        question4Answer: '',
        question4Unknown: 0,
        question5Answer: '',
        question5SkipReason: '',
        question6Answer: '',
        question6Unknown: 0,
        question7Answer: '',
        question7Unknown: 0,
        executor: '',
        executionTime: new Date()
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.patientInfo.assessmentName ? `${this.patientInfo.assessmentName}（IPAQ 短卷）` : '国际体力活动问卷（IPAQ 短卷）'
    }
  },
  watch: {
    visible(val) {
      if (val && this.assessmentPlanId) {
        this.loadExecutionInfo()
      }
    }
  },
  methods: {
    loadExecutionInfo() {
      this.loading = true
      getAssessmentExecutionInfo(this.assessmentPlanId).then(response => {
        this.patientInfo = response.data
        this.formData.assessmentPlanId = response.data.assessmentPlanId
        this.formData.patientId = response.data.patientId
        this.formData.executor = this.$store.state.user ? this.$store.state.user.name : '系统'
        this.formData.executionTime = new Date()
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    handleSubmit() {
      this.handleConfirm()
    },

    handleSave() {
      // 暂存
      const submitData = {
        ...this.formData,
        status: 1
      }
      submitAssessmentExecution(submitData).then(() => {
        this.$message.success('暂存成功')
        this.handleClose()
        this.$emit('success')
      })
    },

    handleConfirm() {
      // 确定提交
      const submitData = {
        ...this.formData,
        status: 2
      }
      submitAssessmentExecution(submitData).then(() => {
        this.$message.success('提交成功')
        this.handleClose()
        this.$emit('success')
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.$emit('close')
    },

    formatDate(date) {
      if (!date) return ''
      return date
    },

    formatDateTime(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    }
  }
}
</script>

<style lang="scss" scoped>
.execution-dialog {
  ::v-deep .el-dialog__body {
    padding: 20px 30px;
    max-height: 70vh;
    overflow-y: auto;
  }
}

.execution-content {
  .patient-info-section {
    margin-bottom: 30px;
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .info-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 20px;
    }

    .info-item {
      display: flex;
      flex-direction: column;

      .info-label {
        font-size: 14px;
        color: #606266;
        margin-bottom: 8px;
        font-weight: 500;
      }

      .info-value {
        font-size: 14px;
        color: #303133;
        padding: 8px 12px;
        background-color: #fff;
        border-radius: 4px;
        min-height: 36px;
        display: flex;
        align-items: center;
      }
    }
  }

  .questionnaire-section {
    .question-item {
      margin-bottom: 30px;
      padding: 20px;
      background-color: #fff;
      border: 1px solid #e4e7ed;
      border-radius: 4px;

      .question-title {
        font-size: 15px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 10px;
        line-height: 1.6;
      }

      .question-subtitle {
        font-size: 14px;
        color: #606266;
        margin-bottom: 10px;
      }

      .el-input {
        margin-bottom: 10px;
      }

      .el-radio,
      .el-checkbox {
        display: block;
        margin-top: 10px;
        color: #409eff;
      }
    }

    .execution-info {
      margin-top: 30px;
      padding: 20px;
      background-color: #f5f7fa;
      border-radius: 4px;
    }

    .submit-section {
      text-align: center;
      margin-top: 30px;

      .el-button {
        width: 200px;
        background-color: #409eff;
        border-color: #409eff;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
  padding: 10px 20px;

  .save-btn {
    background-color: rgb(106, 91, 140);
    border-color: rgb(106, 91, 140);
    color: #fff;

    &:hover {
      background-color: rgb(96, 81, 130);
      border-color: rgb(96, 81, 130);
    }
  }

  .confirm-btn {
    background-color: rgb(106, 91, 140);
    border-color: rgb(106, 91, 140);

    &:hover {
      background-color: rgb(96, 81, 130);
      border-color: rgb(96, 81, 130);
    }
  }
}
</style>
