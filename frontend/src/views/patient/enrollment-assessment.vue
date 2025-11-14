<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: 600;">入组评估</span>
        <el-button style="float: right;" type="text" icon="el-icon-back" @click="handleBack">返回</el-button>
      </div>

      <el-alert
        title="功能开发中"
        type="info"
        description="入组评估填写功能正在开发中，敬请期待..."
        :closable="false"
        show-icon
        style="margin-bottom: 20px;"
      />

      <!-- 患者基本信息展示 -->
      <el-descriptions v-if="patientData" title="患者基本信息" :column="2" border>
        <el-descriptions-item label="姓名">{{ patientData.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ patientData.gender }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ patientData.phone }}</el-descriptions-item>
        <el-descriptions-item label="证件类型">{{ patientData.idCardType }}</el-descriptions-item>
        <el-descriptions-item label="证件号">{{ patientData.idCard }}</el-descriptions-item>
        <el-descriptions-item label="身高">{{ patientData.height }}cm</el-descriptions-item>
        <el-descriptions-item label="出生年月">{{ patientData.birthDate }}</el-descriptions-item>
        <el-descriptions-item label="民族">{{ patientData.ethnicity }}</el-descriptions-item>
        <el-descriptions-item label="病种类型">{{ patientData.diseaseType }}</el-descriptions-item>
      </el-descriptions>

      <!-- 操作按钮 -->
      <div style="text-align: center; margin-top: 30px;">
        <el-button @click="handleBack">返回</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'EnrollmentAssessment',
  data() {
    return {
      patientData: null
    }
  },
  created() {
    // 从路由参数中获取患者数据
    if (this.$route.query.patientData) {
      try {
        this.patientData = JSON.parse(this.$route.query.patientData)
      } catch (e) {
        console.error('解析患者数据失败:', e)
      }
    }
  },
  methods: {
    handleBack() {
      this.$router.back()
    },
    handleSave() {
      this.$message.success('保存成功')
      this.handleBack()
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
</style>
