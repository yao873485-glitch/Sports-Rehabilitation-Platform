<template>
  <div class="patient-detail-view">
    <!-- 基本信息 -->
    <el-card class="card-section" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="header-title">基本信息</span>
          <span class="required-mark">* 表示必填项</span>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ patientData.basicInfo?.name || '-' }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ patientData.basicInfo?.age || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ patientData.basicInfo?.gender || '-' }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ patientData.basicInfo?.birthDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="证件号码">{{ patientData.basicInfo?.idNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ patientData.basicInfo?.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="档案号">{{ patientData.basicInfo?.recordNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="病种">{{ patientData.basicInfo?.disease || '-' }}</el-descriptions-item>
        <el-descriptions-item label="入组机构">{{ patientData.basicInfo?.institution || '-' }}</el-descriptions-item>
        <el-descriptions-item label="居住地址" :span="2">{{ patientData.basicInfo?.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ patientData.basicInfo?.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="最后修改时间">{{ patientData.basicInfo?.updateTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 详细信息 -->
    <el-card class="card-section" shadow="never" v-if="patientData.detailInfo">
      <template #header>
        <div class="card-header">
          <span class="header-title">详细信息</span>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="证件类型">{{ patientData.detailInfo.idType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="证件号">{{ patientData.detailInfo.idNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身高">{{ patientData.detailInfo.height ? patientData.detailInfo.height + 'cm' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="出生年月">{{ patientData.detailInfo.birthDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="民族">{{ patientData.detailInfo.ethnicity || '-' }}</el-descriptions-item>
        <el-descriptions-item label="职业">{{ patientData.detailInfo.occupation || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系地址">{{ patientData.detailInfo.contactAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细地址" :span="2">{{ patientData.detailInfo.detailedAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注说明" :span="2">{{ patientData.detailInfo.remarks || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 空状态 -->
    <div v-if="!patientData.basicInfo" class="empty-state">
      <el-empty description="暂无患者信息" />
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </div>
</template>

<script>
import { getPatientDetail } from '@/api/patient'

export default {
  name: 'PatientDetailView',
  props: {
    patientId: {
      type: Number,
      required: true
    },
    readonly: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      loading: false,
      patientData: {
        basicInfo: null,
        detailInfo: null
      }
    }
  },
  watch: {
    patientId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadPatientDetail()
        }
      }
    }
  },
  methods: {
    async loadPatientDetail() {
      this.loading = true
      try {
        const response = await getPatientDetail(this.patientId)
        this.patientData = response.data
      } catch (error) {
        console.error('加载患者详情失败:', error)
        this.$message.error('加载患者详情失败')
      } finally {
        this.loading = false
      }
    },

    handleClose() {
      this.$emit('close')
    }
  }
}
</script>

<style lang="scss" scoped>
.patient-detail-view {
  .card-section {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-title {
        font-weight: 600;
        font-size: 16px;
      }

      .required-mark {
        color: #f56c6c;
        font-size: 12px;
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 40px 0;
  }

  .action-buttons {
    text-align: center;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
  }
}
</style>