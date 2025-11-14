<template>
  <div class="prescription-list">
    <!-- 患者基本信息 -->
    <el-card class="patient-info-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="header-title">患者基本信息</span>
        </div>
      </template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="患者姓名">{{ schemeInfo.patientName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ schemeInfo.genderDesc }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ schemeInfo.age }}岁</el-descriptions-item>
        <el-descriptions-item label="档案号">{{ schemeInfo.medicalRecordNo }}</el-descriptions-item>
        <el-descriptions-item label="方案编号">{{ schemeInfo.schemeId }}</el-descriptions-item>
        <el-descriptions-item label="病种">{{ schemeInfo.diseaseType }}</el-descriptions-item>
        <el-descriptions-item label="入组机构" :span="2">{{ schemeInfo.orgName }}</el-descriptions-item>
        <el-descriptions-item label="方案状态">
          <el-tag :type="schemeInfo.status === 1 ? 'success' : 'info'">
            {{ schemeInfo.statusDesc }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 处方清单表格 -->
    <el-card class="prescription-table-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="header-title">运动处方清单</span>
          <div class="header-actions">
            <el-button type="primary" size="small" icon="el-icon-download" @click="handleExport">
              导出清单
            </el-button>
            <el-button type="success" size="small" icon="el-icon-printer" @click="handlePrint">
              打印清单
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="prescriptionList"
        border
        style="width: 100%"
        empty-text="暂无处方数据"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="exerciseName" label="运动项目" min-width="120" show-overflow-tooltip />
        <el-table-column prop="exerciseType" label="运动类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getExerciseTypeColor(scope.row.exerciseType)">
              {{ scope.row.exerciseType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="frequency" label="频次" width="100" align="center" />
        <el-table-column prop="duration" label="时长(分钟)" width="100" align="center" />
        <el-table-column prop="intensity" label="强度" width="100" align="center" />
        <el-table-column prop="sets" label="组数" width="80" align="center" />
        <el-table-column prop="reps" label="次数" width="80" align="center" />
        <el-table-column prop="restTime" label="休息时间(秒)" width="120" align="center" />
        <el-table-column prop="targetArea" label="目标部位" width="120" show-overflow-tooltip />
        <el-table-column prop="instructions" label="动作要领" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="执行状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.status === 'completed' ? 'success' :
                     scope.row.status === 'in_progress' ? 'warning' : 'info'"
              size="mini"
            >
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </div>
</template>

<script>
import { getPatientPrescriptionList } from '@/api/patient-scheme-info'

export default {
  name: 'PrescriptionList',
  props: {
    schemeInfo: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      prescriptionList: []
    }
  },
  watch: {
    schemeInfo: {
      immediate: true,
      handler(newVal) {
        if (newVal && newVal.schemeId) {
          this.loadPrescriptionList()
        }
      }
    }
  },
  methods: {
    async loadPrescriptionList() {
      this.loading = true
      try {
        const response = await getPatientPrescriptionList(this.schemeInfo.schemeId)
        this.prescriptionList = response.data || []
      } catch (error) {
        console.error('获取处方清单失败:', error)
        this.$message.error('获取处方清单失败')
      } finally {
        this.loading = false
      }
    },

    getExerciseTypeColor(type) {
      const colorMap = {
        '有氧运动': 'success',
        '力量训练': 'warning',
        '柔韧性训练': 'info',
        '平衡训练': 'primary'
      }
      return colorMap[type] || 'default'
    },

    getStatusText(status) {
      const statusMap = {
        'completed': '已完成',
        'in_progress': '进行中',
        'pending': '待执行'
      }
      return statusMap[status] || status
    },

    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    handlePrint() {
      this.$message.info('打印功能开发中...')
    },

    handleClose() {
      this.$emit('close')
    }
  }
}
</script>

<style lang="scss" scoped>
.prescription-list {
  .patient-info-card {
    margin-bottom: 20px;
  }

  .prescription-table-card {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-title {
      font-weight: 600;
      font-size: 16px;
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .action-buttons {
    text-align: center;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;

    .el-button {
      margin: 0 10px;
    }
  }
}

::v-deep .el-descriptions {
  .el-descriptions__header {
    margin-bottom: 10px;
  }
}

::v-deep .el-table {
  .el-table__header {
    background-color: #f5f7fa;
  }
}
</style>