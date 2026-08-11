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
                    <div class="prescription-header">
                      <div class="header-actions">
                        <el-button class="prescription-action-btn" icon="el-icon-plus" size="small" @click="handleAddExercise">
                          新增一行
                        </el-button>
                        <el-button
                          class="prescription-action-btn save-action-btn"
                          :loading="prescriptionSaving"
                          size="small"
                          @click="handleSavePrescription"
                        >
                          保存
                        </el-button>
                        <el-button class="prescription-action-btn" size="small" @click="handleSubmitPrescription">
                          提交
                        </el-button>
                      </div>
                    </div>
        
                    <!-- 运动项目表格 -->
                    <el-table
                      ref="prescriptionTable"
                      :data="exerciseList"
                      border
                      class="prescription-table"
                      :header-cell-style="{
                        backgroundColor: 'rgb(250, 250, 250)',
                        color: 'rgb(38, 38, 38)',
                        textAlign: 'center'
                      }"
                    >
                      <!-- 复选框列 -->
                      <el-table-column type="selection" width="55" align="center" />
        
                        <!-- 序号列 -->
                        <el-table-column label="序号" type="index" width="60" align="center" :index="index => index + 1" />
        
                        <!-- 运动场景 -->
                        <el-table-column label="运动场景" align="center" width="120">
                          <template slot-scope="scope">
                            <el-select v-model="scope.row.exerciseScene" placeholder="请选择" size="small">
                              <el-option label="机构" value="机构" />
                              <el-option label="居家" value="居家" />
                            </el-select>
                          </template>
                        </el-table-column>
        
                        <!-- 运动类型 -->
                        <el-table-column label="运动类型" align="center" width="140">
                          <template slot-scope="scope">
                            <el-select v-model="scope.row.exerciseType" placeholder="请选择" size="small">
                              <el-option label="有氧运动" value="有氧运动" />
                              <el-option label="呼吸训练" value="呼吸训练" />
                              <el-option label="平衡训练" value="平衡训练" />
                              <el-option label="抗阻训练" value="抗阻训练" />
                              <el-option label="柔韧性训练" value="柔韧性训练" />
                            </el-select>
                          </template>
                        </el-table-column>
        
                        <!-- 运动方式 -->
                        <el-table-column label="运动方式" align="center" width="200">
                          <template slot-scope="scope">
                            <el-select v-model="scope.row.exerciseMethod" placeholder="请选择" size="small">
                              <el-option label="单腿站立（扶椅背）" value="单腿站立（扶椅背）" />
                              <el-option label="直线行走" value="直线行走" />
                              <el-option label="脚跟-脚尖行走" value="脚跟-脚尖行走" />
                              <el-option label="弹力带训练（侧平举）" value="弹力带训练（侧平举）" />
                              <el-option label="坐姿划船" value="坐姿划船" />
                              <el-option label="器械训练（腿举机轻量）" value="器械训练（腿举机轻量）" />
                            </el-select>
                          </template>
                        </el-table-column>
        
                        <!-- 注意事项 -->
                        <el-table-column label="注意事项" align="center" width="180">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.precautions" placeholder="请输入" size="small" />
                          </template>
                        </el-table-column>
        
                        <!-- 单次运动时间 -->
                        <el-table-column label="单次运动时间（分）" align="center" width="160">
                          <template slot-scope="scope">
                            <el-input-number
                              v-model="scope.row.durationMinutes"
                              :min="1"
                              :max="180"
                              size="small"
                              controls-position="right"
                            />
                          </template>
                        </el-table-column>
        
                        <!-- 运动频率 -->
                        <el-table-column label="运动频率" align="center" width="120">
                          <template slot-scope="scope">
                            <el-select v-model="scope.row.exerciseFrequency" placeholder="请选择" size="small">
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
        
                        <!-- 运动强度 -->
                        <el-table-column label="运动强度" align="center" width="120">
                          <template slot-scope="scope">
                            <el-select v-model="scope.row.exerciseIntensity" placeholder="请选择" size="small">
                              <el-option label="低强度" value="低强度" />
                              <el-option label="中等" value="中等" />
                              <el-option label="高强度" value="高强度" />
                            </el-select>
                          </template>
                        </el-table-column>
        
                        <!-- 运动周期（周） -->
                        <el-table-column label="运动周期（周）" align="center" width="130">
                          <template slot-scope="scope">
                            <el-input-number
                              v-model="scope.row.cycleWeeks"
                              :min="1"
                              :max="52"
                              size="small"
                              controls-position="right"
                            />
                          </template>
                        </el-table-column>
        
                        <!-- 总次数（次） -->
                        <el-table-column label="总次数（次）" align="center" width="130">
                          <template slot-scope="scope">
                            <el-input-number
                              v-model="scope.row.totalSessions"
                              :min="1"
                              :max="1000"
                              size="small"
                              controls-position="right"
                            />
                          </template>
                        </el-table-column>
        
                        <!-- 周运动总量（分） -->
                        <el-table-column label="周运动总量（分）" align="center" width="150">
                          <template slot-scope="scope">
                            <el-input-number
                              v-model="scope.row.weeklyTotalMinutes"
                              :min="0"
                              :max="10000"
                              size="small"
                              controls-position="right"
                            />
                          </template>
                        </el-table-column>
        
                        <!-- 运动进阶 -->
                        <el-table-column label="运动进阶" align="center" width="180">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.progression" placeholder="请输入" size="small" />
                          </template>
                        </el-table-column>
        
                        <!-- 备注 -->
                        <el-table-column label="备注" align="center" width="180">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.remarks" placeholder="请输入" size="small" />
                          </template>
                        </el-table-column>
        
                        <!-- 操作 -->
                        <el-table-column label="操作" align="center" width="100" fixed="right">
                          <template slot-scope="scope">
                            <el-button
                              type="text"
                              size="small"
                              class="delete-btn"
                              @click="handleDeleteSingleExercise(scope.$index)"
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
          :visible.sync="executionInstitutionDialogVisible"
          width="600px"
          :close-on-click-modal="false"
        >
          <el-form label-width="100px">
            <el-form-item label="执行机构:">
              <el-select
                v-model="selectedInstitution"
                placeholder="请选择机构"
                style="width: 100%;"
              >
                <el-option label="西南医科大学附属医院" value="西南医科大学附属医院" />
              </el-select>
            </el-form-item>
          </el-form>
    
          <div slot="footer" class="dialog-footer">
            <el-button @click="executionInstitutionDialogVisible = false">取消</el-button>
            <el-button
              type="primary"
              :loading="prescriptionSaving"
              @click="confirmSubmitPrescription"
            >
              确认
            </el-button>
          </div>
        </el-dialog>
  </div>
</template>

<script>
import { savePrescription } from '@/api/medical-scheme'

export default {
  name: 'PrescriptionCreate',
  data() {
    return {
      prescriptionSaving: false,
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
      exerciseList: [
        {
          exerciseScene: '',
          exerciseType: '',
          exerciseMethod: '',
          precautions: '',
          durationMinutes: null,
          exerciseFrequency: '1次/周',
          exerciseIntensity: '中等',
          cycleWeeks: 1,
          totalSessions: 1,
          weeklyTotalMinutes: 0,
          progression: '',
          remarks: '',
          exerciseImage: ''
        }
      ],
      executionInstitutionDialogVisible: false,
      selectedInstitution: '西南医科大学附属医院'
    }
  },
  mounted() {
    const patientData = this.$route.query.patientData
    if (patientData) {
      try {
        this.patientInfo = JSON.parse(patientData)
      } catch (error) {
        console.error('Failed to parse patient info:', error)
        this.$message.error('Failed to load patient info')
        this.$router.back()
      }
    } else {
      this.$message.error('Missing patient info')
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

    handleAddExercise() {
      this.exerciseList.push({
        exerciseScene: '',
        exerciseType: '',
        exerciseMethod: '',
        precautions: '',
        durationMinutes: null,
        exerciseFrequency: '1次/周',
        exerciseIntensity: '中等',
        cycleWeeks: 1,
        totalSessions: 1,
        weeklyTotalMinutes: 0,
        progression: '',
        remarks: '',
        exerciseImage: ''
      })
    },

    handleDeleteSingleExercise(index) {
      this.$confirm('Are you sure to delete this item?', '??', {
        type: 'warning'
      }).then(() => {
        this.exerciseList.splice(index, 1)
        this.$message.success('Deleted')
      }).catch(() => {})
    },

    handleSavePrescription() {
      const selectedRows = this.$refs.prescriptionTable.selection

      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('Please select at least one row')
        return
      }

      const incompleteItems = selectedRows.filter(item =>
        !item.exerciseScene || !item.exerciseType || !item.exerciseMethod ||
        !item.durationMinutes || !item.exerciseFrequency || !item.exerciseIntensity ||
        !item.cycleWeeks || !item.totalSessions
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('Please complete required fields')
        return
      }

      this.prescriptionSaving = true

      const prescriptionData = {
        schemeId: null,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        executionInstitution: null,
        exerciseList: selectedRows.map(item => ({
          exerciseScene: item.exerciseScene,
          exerciseType: item.exerciseType,
          exerciseMethod: item.exerciseMethod,
          precautions: item.precautions,
          durationMinutes: item.durationMinutes,
          exerciseFrequency: item.exerciseFrequency,
          exerciseIntensity: item.exerciseIntensity,
          cycleWeeks: item.cycleWeeks,
          totalSessions: item.totalSessions,
          weeklyTotalMinutes: item.weeklyTotalMinutes,
          progression: item.progression,
          remarks: item.remarks,
          exerciseImage: item.exerciseImage
        }))
      }

      savePrescription(prescriptionData)
        .then(() => {
          this.$message.success('Saved')
          this.prescriptionSaving = false
        })
        .catch(error => {
          console.error('Save prescription failed:', error)
          this.$message.error('Save failed, please retry')
          this.prescriptionSaving = false
        })
    },

    handleSubmitPrescription() {
      const selectedRows = this.$refs.prescriptionTable.selection

      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('Please select at least one row')
        return
      }

      const incompleteItems = selectedRows.filter(item =>
        !item.exerciseScene || !item.exerciseType || !item.exerciseMethod ||
        !item.durationMinutes || !item.exerciseFrequency || !item.exerciseIntensity ||
        !item.cycleWeeks || !item.totalSessions
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('Please complete required fields')
        return
      }

      this.executionInstitutionDialogVisible = true
    },

    confirmSubmitPrescription() {
      const selectedRows = this.$refs.prescriptionTable.selection

      this.prescriptionSaving = true

      const prescriptionData = {
        schemeId: null,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        executionInstitution: this.selectedInstitution,
        exerciseList: selectedRows.map(item => ({
          exerciseScene: item.exerciseScene,
          exerciseType: item.exerciseType,
          exerciseMethod: item.exerciseMethod,
          precautions: item.precautions,
          durationMinutes: item.durationMinutes,
          exerciseFrequency: item.exerciseFrequency,
          exerciseIntensity: item.exerciseIntensity,
          cycleWeeks: item.cycleWeeks,
          totalSessions: item.totalSessions,
          weeklyTotalMinutes: item.weeklyTotalMinutes,
          progression: item.progression,
          remarks: item.remarks,
          exerciseImage: item.exerciseImage
        }))
      }

      savePrescription(prescriptionData)
        .then(() => {
          this.$message.success('Submitted')
          this.prescriptionSaving = false
          this.executionInstitutionDialogVisible = false
        })
        .catch(error => {
          console.error('Submit prescription failed:', error)
          this.$message.error('Submit failed, please retry')
          this.prescriptionSaving = false
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

    // 表格样式 - 无边框
    ::v-deep .el-table {
      // 去除所有边框
      border: none;

      &::before {
        display: none; // 去除底部边框
      }

      th, td {
        border: none; // 去除单元格边框
      }

      // 表头样式
      th {
        background-color: transparent;
        color: rgb(144, 126, 179);
        font-weight: 500;
        padding: 16px 20px;
        white-space: nowrap;
        text-align: center !important; // 强制居中
      }

      // 表头内容容器
      .cell {
        padding: 0;
        text-align: center;
      }

      // 内容样式
      td {
        padding: 16px 20px;
        color: #303133;
        white-space: nowrap;
        text-align: center !important; // 强制居中

        .cell {
          padding: 0;
          text-align: center;
        }
      }

      // 表格布局自动
      table-layout: fixed;
    }
  }

  ::v-deep .el-tabs__item {
    font-size: 16px;
    font-weight: 500;
    color: rgb(144, 126, 179);
  }

  ::v-deep .el-tabs__item.is-active {
    color: rgb(144, 126, 179);
  }

  ::v-deep .el-tabs__active-bar {
    background-color: rgb(144, 126, 179);
  }

  ::v-deep .el-tabs__item.is-disabled {
    color: #c0c4cc;
    cursor: not-allowed;
  }

  ::v-deep .el-descriptions {
    .el-descriptions__label {
      font-weight: 500;
      color: #606266;
    }
  }

  // 档案按钮样式
  .archive-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);
    color: #fff;
    padding: 7px 20px;

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

  // 保存按钮样式
  .save-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);
    color: #fff;
    padding: 10px 30px;
    font-size: 14px;

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

    &.is-disabled {
      background-color: #d1c4dc;
      border-color: #d1c4dc;
      color: #fff;
      cursor: not-allowed;
    }
  }

  // 运动处方按钮样式
  .prescription-header {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .prescription-action-btn {
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

  .save-action-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);

    &:hover,
    &:focus {
      background-color: rgb(134, 116, 169);
      border-color: rgb(134, 116, 169);
    }
  }

  // 运动处方表格样式
  .prescription-table {
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

    // 横向滚动条样式
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

    // 固定列阴影
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

  // 评定计划样式
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

  // 评定计划表格样式
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

    // 横向滚动条样式
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

    // 固定列阴影
    ::v-deep .el-table__fixed-right {
      box-shadow: -3px 0 10px rgba(0, 0, 0, 0.1);
    }
  }

  .preview-btn {
    color: rgb(144, 126, 179);

    &:hover {
      color: rgb(134, 116, 169);
    }
  }

  .assessment-pagination {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 20px;
    gap: 20px;

    .pagination-info {
      font-size: 14px;
      color: #606266;
    }

    ::v-deep .el-pagination {
      .el-pager li.active {
        color: rgb(144, 126, 179);
      }

      .el-pager li:hover {
        color: rgb(144, 126, 179);
      }
    }
  }

  // 随访管理样式
  .followup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 1px solid #e4e7ed;

    .plan-name {
      .label {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }

      .value {
        font-size: 16px;
        color: #606266;
        margin-left: 10px;
      }
    }
  }

  .patient-info-section {
    background-color: #f9fafb;
    padding: 20px;
    border-radius: 4px;
    border: 1px solid #e4e7ed;

    .info-row {
      display: flex;
      justify-content: flex-start;
      margin-bottom: 20px;

      &:last-child {
        margin-bottom: 0;
      }

      .info-item {
        flex: 1;
        display: flex;
        align-items: center;
        min-width: 0;

        .info-label {
          font-size: 19px;
          color: rgb(24, 28, 24);
          white-space: nowrap;
          font-weight: 500;
        }

        .info-value {
          font-size: 19px;
          color: rgb(24, 28, 24);
          margin-left: 5px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .edit-link {
          font-size: 19px;
          color: #409eff;
          margin-left: 8px;
          text-decoration: none;
          white-space: nowrap;
          cursor: pointer;

          &:hover {
            color: #66b1ff;
            text-decoration: underline;
          }
        }
      }
    }
  }

  .followup-action {
    margin: 20px 0;
  }

  .operation-log {
    margin-top: 20px;
  }

  // 开始随访对话框样式
  .step-content {
    min-height: 150px;
    padding: 20px 0;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }

  // 随访计划展示区样式
  .followup-plan-content {
    margin: 20px 0;
  }

  .followup-progress {
    margin-bottom: 30px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .progress-text {
      font-size: 14px;
      color: #909399;
    }

    .progress-value {
      font-size: 14px;
      font-weight: bold;
      color: #303133;
      margin-left: 5px;
    }
  }

  .followup-timeline {
    margin-top: 20px;

    .timeline-item {
      display: flex;
      position: relative;
      margin-bottom: 30px;

      // 左侧时间轴区域
      .timeline-left {
        width: 200px;
        flex-shrink: 0;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        position: relative;

        .timeline-date {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
          margin-bottom: 5px;
        }

        .timeline-status {
          font-size: 13px;
          color: #909399;
          margin-bottom: 10px;
        }

        .timeline-dot {
          width: 12px;
          height: 12px;
          background-color: #409EFF;
          border-radius: 50%;
          margin-bottom: 10px;
        }

        .timeline-line {
          position: absolute;
          left: 6px;
          top: 80px;
          width: 2px;
          height: calc(100% + 30px);
          background-color: #e4e7ed;
        }

        .terminate-btn {
          background-color: #f56c6c;
          border-color: #f56c6c;
          color: #ffffff;

          &:hover {
            background-color: #f78989;
            border-color: #f78989;
            color: #ffffff;
          }
        }
      }

      // 中间分隔线
      .timeline-divider {
        width: 1px;
        background-color: #e4e7ed;
        margin: 0 20px;
      }

      // 右侧任务详情卡片
      .timeline-right {
        flex: 1;

        .task-card {
          .task-header {
            display: flex;
            flex-direction: column;
            gap: 10px;

            .task-title {
              font-size: 16px;
              font-weight: 500;
              color: #303133;
              margin: 0;
            }

            .task-actions-inline {
              display: flex;
              align-items: center;
              gap: 10px;

              .action-link {
                font-size: 14px;
                color: #409eff;
                text-decoration: none;
                cursor: pointer;

                &:hover {
                  color: #66b1ff;
                  text-decoration: underline;
                }

                &.danger {
                  color: #f56c6c;

                  &:hover {
                    color: #f78989;
                  }
                }
              }

              .divider {
                color: #dcdfe6;
              }
            }
          }

          .task-footer {
            display: flex;
            justify-content: flex-start;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #e4e7ed;

            ::v-deep .el-button--primary {
              background-color: #409eff;
              border-color: #409eff;
              color: #ffffff;

              &:hover {
                background-color: #66b1ff;
                border-color: #66b1ff;
              }
            }
          }
        }
      }
    }
  }

  // 编辑计划对话框样式
  ::v-deep .edit-plan-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #e4e7ed;

    .header-left {
      display: flex;
      align-items: center;
      gap: 20px;

      .back-link {
        font-size: 14px;
        color: #606266;
        text-decoration: none;
        display: flex;
        align-items: center;
        gap: 5px;

        &:hover {
          color: #409eff;
        }
      }

      .edit-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .cancel-btn {
      background-color: #f5f7fa;
      border-color: #dcdfe6;
      color: #606266;
    }
  }

  .edit-plan-content {
    padding: 30px;
    max-height: calc(100vh - 120px);
    overflow-y: auto;

    .basic-info-section {
      margin-bottom: 30px;
      padding: 20px;
      background-color: #f9fafb;
      border-radius: 4px;

      .info-row {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        margin-bottom: 15px;

        &:last-child {
          margin-bottom: 0;
        }

        .info-label {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
        }

        .info-value {
          font-size: 14px;
          color: #303133;
        }

        .info-item {
          display: flex;
          align-items: center;
          gap: 5px;
        }
      }
    }

    .section {
      margin-bottom: 25px;

      h3 {
        font-size: 15px;
        color: #303133;
        margin-bottom: 15px;
        font-weight: 500;
      }
    }

    .followup-task-item {
      margin-bottom: 20px;
      padding: 15px;
      background-color: #f5f7fa;
      border-radius: 4px;
      border-left: 3px solid #409eff;

      .task-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .task-title {
          font-size: 14px;
          color: #409eff;
          font-weight: 500;
        }

        .task-actions {
          display: flex;
          gap: 15px;

          i {
            font-size: 16px;
            color: #909399;
            cursor: pointer;

            &:hover {
              color: #409eff;
            }

            &.el-icon-delete:hover {
              color: #f56c6c;
            }
          }
        }
      }

      .task-interval {
        display: flex;
        align-items: center;
        gap: 5px;
      }
    }

    .tip-text {
      margin: 30px 0;
      padding: 12px;
      background-color: #fef0f0;
      border-left: 3px solid #f56c6c;
      font-size: 13px;
      color: #909399;
      line-height: 1.6;
    }

    .remind-section {
      margin-bottom: 25px;
      padding: 20px;
      background-color: #f9fafb;
      border-radius: 4px;

      .remind-header {
        margin-bottom: 15px;

        .remind-desc {
          font-size: 13px;
          color: #606266;
          margin-left: 10px;
        }
      }

      .remind-config {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        gap: 5px;
        padding-left: 30px;

        span {
          font-size: 14px;
          color: #606266;
        }
      }
    }
  }
}
</style>
