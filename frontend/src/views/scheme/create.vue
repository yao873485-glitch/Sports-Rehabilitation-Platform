<template>
  <div class="app-container">
    <el-card>
      <!-- 患者信息卡片 -->
      <div class="patient-info-card">
        <div class="patient-header">
          <i class="el-icon-user-solid" style="font-size: 24px; color: #409eff; margin-right: 10px;" />
          <span class="patient-name">{{ patientInfo.name }}</span>
          <el-tag :type="patientInfo.gender === '男' ? 'primary' : 'danger'" size="small" style="margin-left: 10px;">
            {{ patientInfo.gender }}
          </el-tag>
        </div>
        <div class="patient-details">
          <span>档案号：{{ patientInfo.medicalRecordNo }}</span>
          <span>病种类型：{{ patientInfo.diseaseType }}</span>
          <span>入组机构：{{ patientInfo.enrollmentInstitution }}</span>
        </div>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 运动处方标签页 -->
        <el-tab-pane label="运动处方" name="prescription">
          <el-form
            ref="prescriptionForm"
            :model="prescriptionForm"
            :rules="prescriptionRules"
            label-width="120px"
            style="max-width: 700px; margin: 20px auto;"
          >
            <!-- 方案周期 -->
            <el-form-item label="方案周期" prop="schemeCycle">
              <el-select
                v-model="prescriptionForm.schemeCycle"
                placeholder="请选择方案周期"
                style="width: 200px;"
                @change="handleCycleChange"
              >
                <el-option
                  v-for="month in 12"
                  :key="month"
                  :label="`${month}月`"
                  :value="month"
                />
              </el-select>
              <span style="margin-left: 10px; color: #909399;">单位：月</span>
            </el-form-item>

            <!-- 档案信息区域 -->
            <el-divider content-position="left">档案信息</el-divider>
            <div class="archive-info-container">
              <div class="archive-info-item">
                <span class="archive-label">健康档案</span>
                <el-button
                  type="primary"
                  size="small"
                  style="background-color: #9b59d6; border-color: #9b59d6;"
                  @click="handleViewHealthRecord"
                >
                  查看
                </el-button>
              </div>
              <div class="archive-info-item">
                <span class="archive-label">专病档案</span>
                <el-button
                  type="primary"
                  size="small"
                  style="background-color: #9b59d6; border-color: #9b59d6;"
                  @click="handleViewDiseaseRecord"
                >
                  查看
                </el-button>
              </div>
              <div class="archive-info-item">
                <span class="archive-label">入组评估</span>
                <el-button
                  type="primary"
                  size="small"
                  style="background-color: #9b59d6; border-color: #9b59d6;"
                  @click="handleViewEnrollmentAssessment"
                >
                  查看
                </el-button>
              </div>
            </div>

            <!-- 保存按钮 -->
            <el-form-item>
              <div style="text-align: right; margin-top: 40px;">
                <el-button
                  type="primary"
                  :disabled="!isCycleModified"
                  :loading="submitLoading"
                  style="background-color: #6b3fa0; border-color: #6b3fa0;"
                  @click="handleSave"
                >
                  保存
                </el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 其他标签页（暂时禁用，等运动处方保存后才能访问） -->
        <el-tab-pane label="营养指导" name="nutrition" :disabled="!prescriptionCompleted">
          <div style="padding: 20px; text-align: center; color: #909399;">
            请先完成运动处方的填写
          </div>
        </el-tab-pane>

        <el-tab-pane label="心理干预" name="psychology" :disabled="!prescriptionCompleted">
          <div style="padding: 20px; text-align: center; color: #909399;">
            请先完成运动处方的填写
          </div>
        </el-tab-pane>

        <el-tab-pane label="健康教育" name="education" :disabled="!prescriptionCompleted">
          <div style="padding: 20px; text-align: center; color: #909399;">
            请先完成运动处方的填写
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { createScheme } from '@/api/medical-scheme'

export default {
  name: 'SchemeCreate',
  data() {
    return {
      activeTab: 'prescription',
      prescriptionCompleted: false,
      submitLoading: false,
      isCycleModified: false,

      // 患者信息（从路由参数获取）
      patientInfo: {
        id: null,
        name: '',
        gender: '',
        medicalRecordNo: '',
        diseaseType: '',
        enrollmentInstitution: ''
      },

      // 运动处方表单
      prescriptionForm: {
        schemeCycle: null
      },

      // 验证规则
      prescriptionRules: {
        schemeCycle: [
          { required: true, message: '请选择方案周期', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    // 从路由参数获取患者信息
    const patientData = this.$route.query.patient
    if (patientData) {
      try {
        this.patientInfo = JSON.parse(patientData)
      } catch (error) {
        console.error('解析患者信息失败:', error)
        this.$message.error('获取患者信息失败')
        this.$router.back()
      }
    } else {
      this.$message.error('缺少患者信息')
      this.$router.back()
    }
  },
  methods: {
    /** 标签页切换 */
    handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)
    },

    /** 方案周期变化 */
    handleCycleChange(value) {
      this.isCycleModified = true
      console.log('选择的方案周期:', value)
    },

    /** 查看健康档案 */
    handleViewHealthRecord() {
      this.$router.push({
        path: '/patient/health-record',
        query: {
          patientId: this.patientInfo.id,
          from: 'scheme-create'
        }
      })
    },

    /** 查看专病档案 */
    handleViewDiseaseRecord() {
      this.$router.push({
        path: '/patient/disease-record',
        query: {
          patientId: this.patientInfo.id,
          from: 'scheme-create'
        }
      })
    },

    /** 查看入组评估 */
    handleViewEnrollmentAssessment() {
      this.$router.push({
        path: '/patient/enrollment-assessment',
        query: {
          patientId: this.patientInfo.id,
          from: 'scheme-create'
        }
      })
    },

    /** 保存方案 */
    handleSave() {
      this.$refs.prescriptionForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true

          const schemeData = {
            patientId: this.patientInfo.id,
            medicalRecordNo: this.patientInfo.medicalRecordNo,
            schemeCycle: this.prescriptionForm.schemeCycle,
            diseaseType: this.patientInfo.diseaseType
          }

          console.log('提交的方案数据:', schemeData)

          // 调用后端API保存方案
          createScheme(schemeData)
            .then(response => {
              this.$message.success('方案创建成功')
              this.submitLoading = false
              this.prescriptionCompleted = true
              this.isCycleModified = false

              // 可以继续填写其他标签页或返回列表
              this.$confirm('方案创建成功，是否返回方案列表？', '提示', {
                confirmButtonText: '返回列表',
                cancelButtonText: '继续编辑',
                type: 'success'
              }).then(() => {
                this.$router.push({ path: '/scheme/list' })
              }).catch(() => {
                // 继续编辑
              })
            })
            .catch(error => {
              console.error('保存方案失败:', error)
              this.$message.error('保存失败，请稍后重试')
              this.submitLoading = false
            })
        } else {
          this.$message.error('请完善必填项')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;

  .patient-info-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    color: white;

    .patient-header {
      display: flex;
      align-items: center;
      margin-bottom: 15px;

      .patient-name {
        font-size: 20px;
        font-weight: bold;
      }
    }

    .patient-details {
      display: flex;
      gap: 30px;
      font-size: 14px;
      opacity: 0.9;
    }
  }

  ::v-deep .el-tabs__item {
    font-size: 16px;
    font-weight: 500;
  }

  ::v-deep .el-tabs__item.is-active {
    color: #409eff;
  }

  ::v-deep .el-tabs__item.is-disabled {
    color: #c0c4cc;
    cursor: not-allowed;
  }

  ::v-deep .el-form-item__label {
    font-weight: 500;
  }
}

.archive-info-container {
  margin: 20px 0;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;

  .archive-info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #e4e7ed;

    &:last-child {
      border-bottom: none;
    }

    .archive-label {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }
  }
}
</style>
