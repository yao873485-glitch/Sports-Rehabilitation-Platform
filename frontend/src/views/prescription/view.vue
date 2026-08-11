<template>
  <div class="app-container">
    <!-- 患者信息区域 - 上下布局 -->
    <div class="patient-info-container">
      <div class="info-item">
        <div class="info-label">姓名</div>
        <div class="info-value">{{ patientInfo.patientName }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">身份证</div>
        <div class="info-value">{{ patientInfo.idCard }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">档客号</div>
        <div class="info-value">{{ patientInfo.medicalRecordNo }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">出生日期</div>
        <div class="info-value">{{ patientInfo.birthDate }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">诊断</div>
        <div class="info-value">{{ patientInfo.diagnosis }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">病种</div>
        <div class="info-value">{{ patientInfo.diseaseType }}</div>
      </div>
    </div>

    <!-- 运动处方表格 -->
    <div style="padding: 20px 0;">
      <el-table
        :data="exerciseList"
        border
        class="prescription-table"
        :header-cell-style="{
          backgroundColor: 'rgb(250, 250, 250)',
          color: 'rgb(38, 38, 38)',
          textAlign: 'center'
        }"
      >
        <!-- 序号列 -->
        <el-table-column label="序号" type="index" width="60" align="center" :index="index => index + 1" />

        <!-- 运动场景 -->
        <el-table-column label="运动场景" prop="exerciseScene" align="center" width="120" />

        <!-- 运动类型 -->
        <el-table-column label="运动类型" prop="exerciseType" align="center" width="140" />

        <!-- 运动方式 -->
        <el-table-column label="运动方式" prop="exerciseMethod" align="center" width="200" />

        <!-- 注意事项 -->
        <el-table-column label="注意事项" prop="precautions" align="center" width="180" />

        <!-- 单次运动时间 -->
        <el-table-column label="单次运动时间（分）" prop="durationMinutes" align="center" width="160" />

        <!-- 运动频率 -->
        <el-table-column label="运动频率" prop="exerciseFrequency" align="center" width="120" />

        <!-- 运动强度 -->
        <el-table-column label="运动强度" prop="exerciseIntensity" align="center" width="120" />

        <!-- 运动周期（周） -->
        <el-table-column label="运动周期（周）" prop="cycleWeeks" align="center" width="130" />

        <!-- 总次数（次） -->
        <el-table-column label="总次数（次）" prop="totalSessions" align="center" width="130" />

        <!-- 周运动总量（分） -->
        <el-table-column label="周运动总量（分）" prop="weeklyTotalMinutes" align="center" width="150" />

        <!-- 运动进阶 -->
        <el-table-column label="运动进阶" prop="progression" align="center" width="180" />

        <!-- 备注 -->
        <el-table-column label="备注" prop="remarks" align="center" width="180" />
      </el-table>
    </div>

    <!-- 返回按钮 -->
    <div style="text-align: center; margin-top: 20px;">
      <el-button @click="handleBack">返回</el-button>
    </div>
  </div>
</template>

<script>
import { getPrescriptionDetailWithExercises } from '@/api/prescription'

export default {
  name: 'PrescriptionView',
  data() {
    return {
      loading: false,
      prescriptionId: null,
      patientInfo: {
        patientName: '',
        idCard: '',
        medicalRecordNo: '',
        birthDate: '',
        diagnosis: '',
        diseaseType: ''
      },
      exerciseList: []
    }
  },
  created() {
    // 从路由参数获取处方ID
    this.prescriptionId = this.$route.query.id
    if (this.prescriptionId) {
      this.loadPrescriptionDetail()
    }
  },
  methods: {
    /** 加载处方详情 */
    loadPrescriptionDetail() {
      this.loading = true
      getPrescriptionDetailWithExercises(this.prescriptionId).then(response => {
        const data = response.data
        // 设置患者信息
        this.patientInfo = {
          patientName: data.patientName || '-',
          idCard: data.idCard || '-',
          medicalRecordNo: data.medicalRecordNo || '-',
          birthDate: data.birthDate || '-',
          diagnosis: data.diagnosis || '-',
          diseaseType: data.diseaseType || '-'
        }
        // 设置运动项目列表
        this.exerciseList = data.exerciseList || []
        this.loading = false
      }).catch(error => {
        console.error('加载处方详情失败:', error)
        this.$message.error('加载处方详情失败')
        this.loading = false
      })
    },

    /** 返回上一页 */
    handleBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
}

.patient-info-container {
  display: flex;
  justify-content: space-between;
  background-color: #f9fafb;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;

  .info-item {
    flex: 1;
    text-align: center;

    .info-label {
      color: rgb(144, 126, 179);
      font-size: 14px;
      margin-bottom: 8px;
    }

    .info-value {
      color: #333;
      font-size: 14px;
    }
  }
}

.prescription-table {
  width: 100%;

  ::v-deep .el-table__header th {
    background-color: rgb(250, 250, 250);
    color: rgb(38, 38, 38);
    text-align: center;
  }

  ::v-deep .el-table__body td {
    text-align: center;
  }
}
</style>
