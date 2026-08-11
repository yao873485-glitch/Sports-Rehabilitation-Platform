<template>
  <div class="app-container">
    <el-card class="patient-info-card">
      <el-table
        :data="[patientInfo]"
        :show-header="true"
        style="width: 100%"
      >
        <el-table-column prop="name" label="姓名" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }} · {{ scope.row.gender }} · {{ calculateAge(scope.row.birthDate) }}岁
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证" align="center" />
        <el-table-column prop="medicalRecordNo" label="档案号" align="center" />
        <el-table-column prop="birthDate" label="出生日期" align="center" />
        <el-table-column prop="diagnosis" label="诊断" align="center">
          <template slot-scope="scope">
            {{ scope.row.diagnosis || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="diseaseType" label="病种" align="center" />
      </el-table>
    </el-card>

    <el-card style="margin-top: 20px;">
      <div style="padding: 20px;">
        <div class="assessment-header">
          <div class="header-actions">
            <el-button class="assessment-action-btn" icon="el-icon-plus" size="small" @click="handleAddAssessment">
              新增一行
            </el-button>
            <el-button
              class="assessment-action-btn"
              :loading="assessmentSaving"
              size="small"
              @click="handleSaveAssessment"
            >
              保存
            </el-button>
            <el-button
              class="assessment-action-btn"
              size="small"
              @click="handleSubmitAssessment"
            >
              提交
            </el-button>
          </div>
        </div>

        <el-table
          ref="assessmentTable"
          :data="assessmentList"
          border
          class="assessment-table"
          :header-cell-style="{
            backgroundColor: 'rgb(250, 250, 250)',
            color: 'rgb(38, 38, 38)',
            textAlign: 'center'
          }"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="序号" type="index" width="80" align="center" :index="index => index + 1" />

          <el-table-column label="执行场景" align="center" min-width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.executionType" placeholder="请选择" size="small">
                <el-option label="机构" value="机构" />
                <el-option label="居家" value="居家" />
              </el-select>
            </template>
          </el-table-column>

          <el-table-column label="量表名称" align="center" min-width="280">
            <template slot-scope="scope">
              <el-select v-model="scope.row.scaleName" placeholder="请选择" size="small">
                <el-option label="6分钟步行试验" value="6分钟步行试验" />
                <el-option label="疼痛评估（VAS）" value="疼痛评估（VAS）" />
                <el-option label="运动风险孕妇风险评估表" value="运动风险孕妇风险评估表" />
                <el-option label="Framingham心血管风险评估（女性版）" value="Framingham心血管风险评估（女性版）" />
                <el-option label="呼吸量表-BODE指数" value="呼吸量表-BODE指数" />
                <el-option label="圣胡安呼吸问卷（SGRQ）" value="圣胡安呼吸问卷（SGRQ）" />
                <el-option label="宗氏抑郁量表" value="宗氏抑郁量表" />
                <el-option label="抑郁自评量表（SDS）" value="抑郁自评量表（SDS）" />
                <el-option label="多哌啶关综合征运动干预前专病评估" value="多哌啶关综合征运动干预前专病评估" />
                <el-option label="骨质疏松症患者评估问卷-身体功能量表" value="骨质疏松症患者评估问卷-身体功能量表" />
                <el-option label="PHQ-9量表" value="PHQ-9量表" />
                <el-option label="食物频率问卷调查（FFQ）" value="食物频率问卷调查（FFQ）" />
                <el-option label="RPE量表-改良博格量表（Borg CR10 Scale）" value="RPE量表-改良博格量表（Borg CR10 Scale）" />
                <el-option label="孕晚期糖尿病风险评估表" value="孕晚期糖尿病风险评估表" />
                <el-option label="肌少症改良版SARC-F量表（SARC-CalF）" value="肌少症改良版SARC-F量表（SARC-CalF）" />
                <el-option label="自主感知疲劳分级表" value="自主感知疲劳分级表" />
                <el-option label="血糖监测记录表" value="血糖监测记录表" />
                <el-option label="坐立测试评估量表" value="坐立测试评估量表" />
                <el-option label="6分钟步行试验登记表" value="6分钟步行试验登记表" />
                <el-option label="呼吸量表-改良英国医学研究会呼吸困难量表" value="呼吸量表-改良英国医学研究会呼吸困难量表" />
                <el-option label="爱丁堡产后抑郁量表（EPDS）" value="爱丁堡产后抑郁量表（EPDS）" />
                <el-option label="功能性动作筛查诊断记录表" value="功能性动作筛查诊断记录表" />
                <el-option label="孕晚期高血压风险评估量表" value="孕晚期高血压风险评估量表" />
                <el-option label="EuroQol健康指数量表EQ-5D" value="EuroQol健康指数量表EQ-5D" />
                <el-option label="PCOS相关生活质量问卷（PCOSQ-5）" value="PCOS相关生活质量问卷（PCOSQ-5）" />
              </el-select>
            </template>
          </el-table-column>

          <el-table-column label="执行频率" align="center" min-width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.executionFrequency" placeholder="请选择" size="small">
                <el-option label="1次/周" value="1次/周" />
                <el-option label="2次/周" value="2次/周" />
                <el-option label="3次/周" value="3次/周" />
                <el-option label="4次/周" value="4次/周" />
                <el-option label="5次/周" value="5次/周" />
                <el-option label="6次/周" value="6次/周" />
                <el-option label="7次/周" value="7次/周" />
              </el-select>
            </template>
          </el-table-column>

          <el-table-column label="周期（周）" align="center" min-width="180">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.cycleDuration"
                :min="0"
                :max="52"
                size="small"
                controls-position="right"
              />
            </template>
          </el-table-column>

          <el-table-column label="总次数（次）" align="center" min-width="150">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.totalTimes"
                :min="1"
                :max="1000"
                size="small"
                controls-position="right"
              />
            </template>
          </el-table-column>

          <el-table-column label="备注" align="center" min-width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remarks" placeholder="请输入" size="small" />
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" width="120" fixed="right">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                class="delete-btn"
                @click="handleDeleteSingleAssessment(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <el-dialog
      title="选择执行机构"
      :visible.sync="assessmentInstitutionDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form label-width="100px">
        <el-form-item label="执行机构:">
          <el-select
            v-model="selectedAssessmentInstitution"
            placeholder="请选择机构"
            style="width: 100%;"
          >
            <el-option label="西南医科大学附属医院" value="西南医科大学附属医院" />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="assessmentInstitutionDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          :loading="assessmentSaving"
          @click="confirmSubmitAssessment"
        >
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { saveAssessment } from '@/api/medical-scheme'

export default {
  name: 'AssessmentCreate',
  data() {
    return {
      assessmentSaving: false,
      patientInfo: {
        id: null,
        name: '',
        nickname: '',
        gender: '',
        height: null,
        birthDate: '',
        ethnicity: '',
        occupation: '',
        idCardType: '',
        idCard: '',
        phone: '',
        contactAddress: '',
        detailAddress: '',
        medicalRecordNo: '',
        enrollmentInstitution: '',
        diagnosis: '',
        diseaseType: '',
        remarks: ''
      },
      assessmentList: [
        {
          executionType: '机构',
          scaleName: '',
          executionFrequency: '1次/周',
          cycleDuration: 0,
          totalTimes: 1,
          remarks: ''
        }
      ],
      assessmentInstitutionDialogVisible: false,
      selectedAssessmentInstitution: '西南医科大学附属医院'
    }
  },
  mounted() {
    const patientData = this.$route.query.patientData
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
    calculateAge(birthDate) {
      if (!birthDate) return 0
      const birth = new Date(birthDate)
      const today = new Date()
      let age = today.getFullYear() - birth.getFullYear()
      const monthDiff = today.getMonth() - birth.getMonth()
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--
      }
      return age
    },

    handleAddAssessment() {
      this.assessmentList.push({
        executionType: '机构',
        scaleName: '',
        executionFrequency: '1次/周',
        cycleDuration: 0,
        totalTimes: 1,
        remarks: ''
      })
    },

    handleDeleteSingleAssessment(index) {
      this.$confirm('确认删除该评定项吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.assessmentList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    handleSaveAssessment() {
      const selectedRows = this.$refs.assessmentTable.selection
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选择一条记录')
        return
      }

      const incompleteItems = selectedRows.filter(item =>
        !item.executionType || !item.scaleName || !item.executionFrequency
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所选评定项目的必填信息')
        return
      }

      this.assessmentSaving = true

      const assessmentData = {
        schemeId: null,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        executionInstitution: null,
        assessmentList: selectedRows.map(item => ({
          executionType: item.executionType,
          scaleName: item.scaleName,
          executionFrequency: item.executionFrequency,
          cycleDuration: item.cycleDuration,
          totalTimes: item.totalTimes,
          remarks: item.remarks
        }))
      }

      saveAssessment(assessmentData)
        .then(() => {
          this.$message.success('评定计划保存成功')
          this.assessmentSaving = false
        })
        .catch(error => {
          console.error('保存评定计划失败:', error)
          this.$message.error('保存失败，请稍后重试')
          this.assessmentSaving = false
        })
    },

    handleSubmitAssessment() {
      const selectedRows = this.$refs.assessmentTable.selection
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选择一条记录')
        return
      }

      const incompleteItems = selectedRows.filter(item =>
        !item.executionType || !item.scaleName || !item.executionFrequency
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所选评定项目的必填信息')
        return
      }

      this.assessmentInstitutionDialogVisible = true
    },

    confirmSubmitAssessment() {
      const selectedRows = this.$refs.assessmentTable.selection
      this.assessmentSaving = true

      const assessmentData = {
        schemeId: null,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        executionInstitution: this.selectedAssessmentInstitution,
        assessmentList: selectedRows.map(item => ({
          executionType: item.executionType,
          scaleName: item.scaleName,
          executionFrequency: item.executionFrequency,
          cycleDuration: item.cycleDuration,
          totalTimes: item.totalTimes,
          remarks: item.remarks
        }))
      }

      saveAssessment(assessmentData)
        .then(() => {
          this.$message.success('评定计划提交成功')
          this.assessmentSaving = false
          this.assessmentInstitutionDialogVisible = false
        })
        .catch(error => {
          console.error('提交评定计划失败:', error)
          this.$message.error('提交失败，请稍后重试')
          this.assessmentSaving = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;

  .patient-info-card {
    border: 1px solid #ebeef5;
    box-shadow: none;

    ::v-deep .el-card__body {
      padding: 0;
    }

    ::v-deep .el-table {
      border: none;

      &::before {
        display: none;
      }

      th, td {
        border: none;
      }

      th {
        background-color: transparent;
        color: rgb(144, 126, 179);
        font-weight: 500;
        padding: 16px 20px;
        white-space: nowrap;
        text-align: center !important;
      }

      .cell {
        padding: 0;
        text-align: center;
      }

      td {
        padding: 16px 20px;
        color: #303133;
        white-space: nowrap;
        text-align: center !important;

        .cell {
          padding: 0;
          text-align: center;
        }
      }

      table-layout: fixed;
    }
  }

  .assessment-header {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .assessment-action-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);
    color: #fff;

    &:hover,
    &:focus {
      background-color: rgb(134, 116, 169);
      border-color: rgb(134, 116, 169);
      color: #fff;
    }

    &:active {
      background-color: rgb(124, 106, 159);
      border-color: rgb(124, 106, 159);
    }
  }

  .assessment-table {
    ::v-deep .el-table__header {
      th {
        background-color: rgb(250, 250, 250);
        color: rgb(38, 38, 38);
        font-weight: 500;
        padding: 12px 0;
      }
    }

    ::v-deep .el-table__body {
      td {
        padding: 8px 0;
      }

      .cell {
        padding: 0 8px;
      }
    }

    ::v-deep .el-input__inner,
    ::v-deep .el-input-number__input {
      text-align: center;
    }

    ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
      height: 12px;
    }

    ::v-deep .el-table__body-wrapper::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 6px;
    }

    ::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 6px;

      &:hover {
        background: #a8a8a8;
      }
    }

    ::v-deep .el-table__fixed-right {
      box-shadow: -3px 0 10px rgba(0, 0, 0, 0.1);
    }
  }

  .delete-btn {
    color: rgb(144, 126, 179);

    &:hover {
      color: rgb(134, 116, 169);
    }
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
