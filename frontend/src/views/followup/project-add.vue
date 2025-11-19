<template>
  <div class="app-container">
    <el-form
      ref="projectForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      class="project-form"
    >
      <!-- 项目名称 -->
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="formData.projectName"
          placeholder="请输入项目名称"
          maxlength="15"
          show-word-limit
        />
      </el-form-item>

      <!-- 项目简介 -->
      <el-form-item label="项目简介" prop="projectBrief">
        <el-input
          v-model="formData.projectBrief"
          type="textarea"
          :rows="3"
          placeholder="请输入项目简介"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>

      <!-- 项目介绍 -->
      <el-form-item label="项目介绍">
        <el-input
          v-model="formData.projectDescription"
          type="textarea"
          :rows="5"
          placeholder="请输入项目介绍"
        />
      </el-form-item>

      <!-- 项目病种 -->
      <el-form-item label="项目病种" prop="diseaseType">
        <el-select
          v-model="formData.diseaseType"
          placeholder="请选择项目病种"
          style="width: 100%;"
        >
          <el-option label="高血压" value="高血压" />
          <el-option label="糖尿病" value="糖尿病" />
          <el-option label="骨关节炎" value="骨关节炎" />
          <el-option label="慢性阻塞性肺疾病" value="慢性阻塞性肺疾病" />
          <el-option label="冠心病" value="冠心病" />
        </el-select>
      </el-form-item>

      <!-- 项目类型 -->
      <el-form-item label="项目类型">
        <el-select
          v-model="formData.projectType"
          placeholder="请选择项目类型"
          style="width: 100%;"
        >
          <el-option label="随访类型A" value="随访类型A" />
          <el-option label="随访类型B" value="随访类型B" />
        </el-select>
      </el-form-item>

      <!-- 患者咨询次数 -->
      <el-form-item label="患者咨询次数">
        <div style="display: flex; align-items: center;">
          <el-button icon="el-icon-minus" size="small" @click="decrementConsultation" />
          <el-input-number
            v-model="formData.consultationTimes"
            :min="1"
            :max="100"
            controls-position="right"
            style="width: 120px; margin: 0 10px;"
          />
          <el-button icon="el-icon-plus" size="small" @click="incrementConsultation" />
          <span style="margin-left: 10px;">次</span>
        </div>
      </el-form-item>

      <!-- 单次时长 -->
      <el-form-item label="单次时长">
        <div style="display: flex; align-items: center;">
          <el-button icon="el-icon-minus" size="small" @click="decrementDuration" />
          <el-input-number
            v-model="formData.singleDuration"
            :min="1"
            :max="24"
            controls-position="right"
            style="width: 120px; margin: 0 10px;"
          />
          <el-button icon="el-icon-plus" size="small" @click="incrementDuration" />
          <span style="margin-left: 10px;">时</span>
        </div>
      </el-form-item>

      <!-- 是否发布 -->
      <el-form-item label="是否发布">
        <el-switch
          v-model="formData.isPublished"
          active-color="rgb(106, 91, 140)"
          inactive-color="#dcdfe6"
        />
      </el-form-item>

      <!-- 按钮区 -->
      <el-form-item>
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { saveFollowupProject } from '@/api/followup'

export default {
  name: 'FollowupProjectAdd',
  data() {
    return {
      formData: {
        projectCode: '',
        projectName: '',
        projectBrief: '',
        projectDescription: '',
        diseaseType: '',
        projectType: '',
        consultationTimes: 1,
        singleDuration: 1,
        bindPlan: '',
        operator: '系统',
        isPublished: false
      },
      formRules: {
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' },
          { max: 15, message: '项目名称不能超过15个字符', trigger: 'blur' }
        ],
        projectBrief: [
          { max: 50, message: '项目简介不能超过50个字符', trigger: 'blur' }
        ],
        diseaseType: [
          { required: true, message: '请选择项目病种', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    // 自动生成10位项目标号
    this.formData.projectCode = this.generateProjectCode()
  },
  methods: {
    // 生成10位随机项目标号
    generateProjectCode() {
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
      let code = ''
      for (let i = 0; i < 10; i++) {
        code += chars.charAt(Math.floor(Math.random() * chars.length))
      }
      return code
    },
    incrementConsultation() {
      if (this.formData.consultationTimes < 100) {
        this.formData.consultationTimes++
      }
    },
    decrementConsultation() {
      if (this.formData.consultationTimes > 1) {
        this.formData.consultationTimes--
      }
    },
    incrementDuration() {
      if (this.formData.singleDuration < 24) {
        this.formData.singleDuration++
      }
    },
    decrementDuration() {
      if (this.formData.singleDuration > 1) {
        this.formData.singleDuration--
      }
    },
    handleSave() {
      this.$refs.projectForm.validate(valid => {
        if (valid) {
          // 调用后端API保存数据
          saveFollowupProject(this.formData).then(() => {
            this.$message.success('保存成功')
            this.$router.back()
          }).catch(error => {
            this.$message.error('保存失败：' + (error.message || '未知错误'))
          })
        }
      })
    },
    handleCancel() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  background: #fff;
  min-height: calc(100vh - 84px);
  display: flex;
  justify-content: center;
}

.project-form {
  max-width: 800px;
  width: 100%;
}

::v-deep .el-form-item__label {
  font-weight: 500;
}

::v-deep .el-input-number .el-input__inner {
  text-align: center;
}
</style>
