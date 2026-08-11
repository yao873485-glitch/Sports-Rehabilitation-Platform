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

    <!-- 运动处方表单 -->
    <div style="padding: 20px 0;">
      <!-- 操作按钮区域（右上角） -->
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

    <!-- 执行机构选择对话框 -->
    <el-dialog
      title="选择执行机构"
      :visible.sync="executionInstitutionDialogVisible"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form label-width="100px">
        <el-form-item label="执行机构:">
          <el-select v-model="selectedExecutionInstitution" placeholder="请选择机构" style="width: 100%;">
            <el-option label="西南医科大学附属医院" value="西南医科大学附属医院" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="executionInstitutionDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmSubmitPrescription">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getPrescriptionDetailWithExercises, savePrescription, submitPrescription } from '@/api/prescription'

export default {
  name: 'PrescriptionEdit',
  data() {
    return {
      loading: false,
      prescriptionId: null,
      schemeId: null,
      patientId: null,
      patientInfo: {
        patientName: '',
        idCard: '',
        medicalRecordNo: '',
        birthDate: '',
        diagnosis: '',
        diseaseType: ''
      },
      exerciseList: [],
      prescriptionSaving: false,
      executionInstitutionDialogVisible: false,
      selectedExecutionInstitution: '',
      institutions: ['机构A', '机构B', '机构C'] // 这里应该从后端获取
    }
  },
  created() {
    // 从路由参数获取处方ID
    this.prescriptionId = this.$route.query.id
    this.schemeId = this.$route.query.schemeId
    this.patientId = this.$route.query.patientId

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
        console.log('API返回的数据:', data)
        console.log('API返回的patientId:', data.patientId)
        console.log('API返回的schemeId:', data.schemeId)

        // 设置患者ID（从API响应中获取）
        if (data.patientId) {
          this.patientId = data.patientId
          console.log('设置patientId为:', this.patientId)
        } else {
          console.warn('API响应中没有patientId!')
        }

        // 设置方案ID（从API响应中获取）
        if (data.schemeId) {
          this.schemeId = data.schemeId
          console.log('设置schemeId为:', this.schemeId)
        } else {
          console.warn('API响应中没有schemeId!')
        }

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

    /** 新增一行运动项目 */
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

    /** 删除单个运动项目 */
    handleDeleteSingleExercise(index) {
      this.$confirm('确认删除该运动项目吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.exerciseList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /** 保存运动处方 */
    handleSavePrescription() {
      // 获取选中的行
      const selectedRows = this.$refs.prescriptionTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的运动项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.exerciseScene || !item.exerciseType || !item.exerciseMethod ||
        !item.durationMinutes || !item.exerciseFrequency || !item.exerciseIntensity ||
        !item.cycleWeeks || !item.totalSessions
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中运动项目的必填信息')
        return
      }

      this.prescriptionSaving = true

      // 调试日志
      console.log('保存处方 - patientId:', this.patientId)
      console.log('保存处方 - prescriptionId:', this.prescriptionId)
      console.log('保存处方 - schemeId:', this.schemeId)

      const prescriptionData = {
        prescriptionId: this.prescriptionId,
        schemeId: this.schemeId,
        patientId: this.patientId,
        diseaseType: this.patientInfo.diseaseType,
        executionInstitution: null, // 保存时执行机构为空
        exerciseList: selectedRows.map(item => ({
          id: item.id || null, // 如果有ID说明是已存在的记录
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
          exerciseImage: item.exerciseImage || ''
        }))
      }

      console.log('发送的数据:', JSON.stringify(prescriptionData))

      savePrescription(prescriptionData).then(response => {
        this.$message.success('保存成功')
        this.prescriptionSaving = false
        // 跳转到运动处方列表页面
        this.$router.push('/prescription/list')
      }).catch(error => {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
        this.prescriptionSaving = false
      })
    },

    /** 提交运动处方 */
    handleSubmitPrescription() {
      // 获取选中的行
      const selectedRows = this.$refs.prescriptionTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的运动项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.exerciseScene || !item.exerciseType || !item.exerciseMethod ||
        !item.durationMinutes || !item.exerciseFrequency || !item.exerciseIntensity ||
        !item.cycleWeeks || !item.totalSessions
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中运动项目的必填信息')
        return
      }

      // 显示执行机构选择对话框
      this.executionInstitutionDialogVisible = true
    },

    /** 确认提交运动处方（选择执行机构后） */
    confirmSubmitPrescription() {
      if (!this.selectedExecutionInstitution) {
        this.$message.warning('请选择执行机构')
        return
      }

      const selectedRows = this.$refs.prescriptionTable.selection

      this.prescriptionSaving = true

      const prescriptionData = {
        prescriptionId: this.prescriptionId,
        schemeId: this.schemeId,
        patientId: this.patientId,
        diseaseType: this.patientInfo.diseaseType,
        executionInstitution: this.selectedExecutionInstitution,
        exerciseList: selectedRows.map(item => ({
          id: item.id || null,
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
          exerciseImage: item.exerciseImage || ''
        }))
      }

      submitPrescription(prescriptionData).then(response => {
        this.$message.success('提交成功')
        this.prescriptionSaving = false
        this.executionInstitutionDialogVisible = false
        // 返回列表页
        this.$router.push('/prescription/list')
      }).catch(error => {
        console.error('提交失败:', error)
        this.$message.error('提交失败')
        this.prescriptionSaving = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

// 患者信息容器样式
.patient-info-container {
  display: flex;
  justify-content: space-between;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;

  .info-item {
    flex: 1;
    text-align: center;

    .info-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }

    .info-value {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }
  }
}

// 处方表格头部操作区域
.prescription-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;

  .header-actions {
    display: flex;
    gap: 10px;
  }

  .prescription-action-btn {
    background-color: rgb(106, 91, 140);
    color: white;
    border: none;
    padding: 8px 16px;
    font-size: 14px;

    &:hover {
      background-color: rgb(96, 81, 130);
    }

    &.save-action-btn {
      background-color: rgb(106, 91, 140);
    }
  }
}

// 处方表格样式
.prescription-table {
  ::v-deep .el-table__header {
    th {
      background-color: rgb(250, 250, 250);
      color: rgb(38, 38, 38);
      font-weight: 500;
    }
  }

  ::v-deep .el-table__body {
    td {
      padding: 8px 0;
    }
  }

  // 删除按钮样式
  .delete-btn {
    color: rgb(106, 91, 140);
    padding: 0;

    &:hover {
      color: rgb(96, 81, 130);
    }
  }
}

// 固定操作列样式
::v-deep .el-table__fixed-right {
  box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

// 固定列的表头也需要背景色
::v-deep .el-table__fixed-right .el-table__header th {
  background-color: rgb(250, 250, 250);
}
</style>
