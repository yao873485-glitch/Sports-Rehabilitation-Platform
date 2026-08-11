<template>
  <div class="app-container">
    <!-- 患者信息卡片 - 固定顶部 -->
    <el-card class="patient-info-card" shadow="never">
      <el-table
        :data="[patientInfo]"
        :show-header="true"
        style="width: 100%"
        :header-cell-style="{ textAlign: 'center', backgroundColor: '#fff', color: 'rgb(144, 126, 179)', fontWeight: '500', border: 'none' }"
        :cell-style="{ textAlign: 'center', border: 'none' }"
      >
        <el-table-column label="姓名" align="center" header-align="center" min-width="150">
          <template slot-scope="scope">
            <div style="text-align: center;">{{ scope.row.patientName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证" align="center" header-align="center" min-width="180" />
        <el-table-column prop="medicalRecordNo" label="档案号" align="center" header-align="center" min-width="180" />
        <el-table-column label="出生日期" align="center" header-align="center" min-width="150">
          <template slot-scope="scope">
            <div style="text-align: center;">{{ scope.row.birthDate || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column label="诊断" align="center" header-align="center" min-width="150">
          <template slot-scope="scope">
            <div style="text-align: center;">{{ scope.row.diagnosis || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="diseaseType" label="病种" align="center" header-align="center" min-width="150" />
      </el-table>
    </el-card>

    <!-- 评定计划详情表格 -->
    <el-card style="margin-top: 20px;">
      <div style="overflow-x: auto;">
        <el-table
          :data="[assessmentDetail]"
          border
          style="width: 100%; min-width: 1200px;"
          :header-cell-style="{
            backgroundColor: 'rgb(248, 248, 249)',
            color: 'rgb(81, 90, 110)',
            textAlign: 'center'
          }"
        >
          <el-table-column type="index" label="序号" width="150" align="center" :index="1" />
          <el-table-column prop="executionType" label="执行场景" align="center" width="180" />
          <el-table-column prop="scaleName" label="量表名称" align="center" width="250" />
          <el-table-column prop="executionFrequency" label="执行频率" align="center" width="180" />
          <el-table-column label="周期（周）" align="center" width="180">
            <template slot-scope="scope">
              {{ scope.row.cycleDuration || 0 }}
            </template>
          </el-table-column>
          <el-table-column label="总次数（次）" align="center" width="180">
            <template slot-scope="scope">
              {{ scope.row.totalTimes || 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="remarks" label="备注" align="center" min-width="300">
            <template slot-scope="scope">
              {{ scope.row.remarks || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 返回按钮 -->
      <div style="text-align: center; margin-top: 30px;">
        <el-button @click="handleBack">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getAssessmentPlanDetail } from '@/api/assessment'

export default {
  name: 'AssessmentView',
  data() {
    return {
      loading: false,
      // 患者信息
      patientInfo: {
        patientName: '',
        idCard: '',
        medicalRecordNo: '',
        birthDate: '',
        diagnosis: '',
        diseaseType: ''
      },
      // 评定计划详情
      assessmentDetail: {
        executionType: '',
        scaleName: '',
        executionFrequency: '',
        cycleDuration: 0,
        totalTimes: 1,
        remarks: ''
      }
    }
  },
  created() {
    const id = this.$route.query.id
    if (id) {
      this.getDetail(id)
    } else {
      this.$message.error('缺少评定计划ID')
      this.$router.back()
    }
  },
  methods: {
    /** 获取详情 */
    getDetail(id) {
      this.loading = true
      getAssessmentPlanDetail(id).then(response => {
        const data = response.data
        // 填充患者信息
        this.patientInfo = {
          patientName: data.patientName,
          idCard: data.idCard,
          medicalRecordNo: data.medicalRecordNo,
          birthDate: data.birthDate,
          diagnosis: data.diagnosis,
          diseaseType: data.diseaseType
        }
        // 填充评定计划详情
        this.assessmentDetail = {
          executionType: data.executionType,
          scaleName: data.scaleName,
          executionFrequency: data.executionFrequency,
          cycleDuration: data.cycleDuration,
          totalTimes: data.totalTimes,
          remarks: data.remarks
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message.error('获取详情失败')
        this.$router.back()
      })
    },

    /** 返回 */
    handleBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.patient-info-card {
  margin-bottom: 20px;
  background-color: rgb(255, 255, 255) !important;

  ::v-deep .el-card__body {
    padding: 20px;
    background-color: rgb(255, 255, 255) !important;
  }

  ::v-deep .el-table {
    border: none !important;
    background-color: rgb(255, 255, 255) !important;

    &::before {
      display: none;
    }

    th,
    th.el-table__cell {
      background-color: rgb(255, 255, 255) !important;
      color: rgb(144, 126, 179) !important;
      font-weight: 500;
      border: none !important;
      padding: 0 !important;
      text-align: center !important;
    }

    td,
    td.el-table__cell {
      border: none !important;
      padding: 0 !important;
      text-align: center !important;
    }

    tr {
      background-color: rgb(255, 255, 255) !important;
    }

    .el-table__header-wrapper,
    .el-table__body-wrapper {
      border: none !important;
      background-color: rgb(255, 255, 255) !important;
    }

    // 核心：清除所有cell的padding，完全居中
    .el-table__cell {
      border: none !important;
      text-align: center !important;
      padding: 0 !important;

      .cell {
        padding: 12px 10px !important;
        text-align: center !important;
        line-height: 1.5 !important;
        display: flex !important;
        justify-content: center !important;
        align-items: center !important;
      }
    }
  }
}

.el-table {
  // 表头样式
  ::v-deep .el-table__header th {
    background-color: rgb(248, 248, 249);
    color: rgb(81, 90, 110);
    font-weight: 500;
    padding: 12px 0;
  }

  // 表格单元格样式
  ::v-deep .el-table__body td {
    padding: 12px 20px;
  }

  // 表头和单元格居中对齐
  ::v-deep .el-table__header th,
  ::v-deep .el-table__body td {
    text-align: center;
  }
}
</style>
