<template>
  <div class="app-container">
    <!-- 专病档案弹窗 -->
    <disease-record-dialog
      :visible.sync="diseaseRecordDialogVisible"
      :patient-id="patientId"
      @saved="handleDiseaseRecordSaved"
    />

    <!-- 入组评估弹窗 -->
    <enrollment-assessment-dialog
      :visible.sync="enrollmentAssessmentDialogVisible"
      :patient-id="patientId"
      @saved="handleEnrollmentAssessmentSaved"
    />
  </div>
</template>

<script>
import DiseaseRecordDialog from './components/DiseaseRecordDialog.vue'
import EnrollmentAssessmentDialog from './components/EnrollmentAssessmentDialog.vue'

export default {
  name: 'DiseaseRecord',
  components: {
    DiseaseRecordDialog,
    EnrollmentAssessmentDialog
  },
  data() {
    return {
      patientId: null,
      diseaseRecordDialogVisible: false,
      enrollmentAssessmentDialogVisible: false
    }
  },
  created() {
    // 从路由参数中获取患者ID
    // 可能是直接的 patientId，也可能在 patientData 对象中
    if (this.$route.query.patientId) {
      this.patientId = parseInt(this.$route.query.patientId)
    } else if (this.$route.query.patientData) {
      try {
        const patientData = JSON.parse(this.$route.query.patientData)
        this.patientId = patientData.id || patientData.patientId
        console.log('从 patientData 解析患者信息:', patientData)
      } catch (e) {
        console.error('解析 patientData 失败:', e)
      }
    }

    console.log('专病档案页面 - 患者ID:', this.patientId)

    if (!this.patientId || isNaN(this.patientId)) {
      this.$message.error('无法获取患者ID，请重新进入')
      this.$router.back()
      return
    }

    // 自动打开弹窗
    this.$nextTick(() => {
      this.diseaseRecordDialogVisible = true
    })
  },
  watch: {
    // 监听弹窗关闭事件，关闭时返回上一页
    diseaseRecordDialogVisible(val) {
      if (!val) {
        // 如果有来源页面和标签页信息，返回到指定标签页
        if (this.$route.query.from === 'create' && this.$route.query.activeTab) {
          this.$router.push({
            path: '/patient/create',
            query: {
              patientId: this.$route.query.patientId,
              mode: this.$route.query.mode,
              activeTab: this.$route.query.activeTab
            }
          })
        } else {
          this.$router.back()
        }
      }
    }
  },
  methods: {
    handleDiseaseRecordSaved() {
      this.$message.success('专病档案保存成功')
      // 关闭弹窗，触发 watch 中的导航逻辑
      this.diseaseRecordDialogVisible = false
    },
    handleEnrollmentAssessmentSaved() {
      this.$message.success('入组评估保存成功')
      // 关闭弹窗，触发 watch 中的导航逻辑
      this.enrollmentAssessmentDialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}
</style>
