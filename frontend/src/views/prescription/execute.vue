<template>
  <div class="app-container">
    <!-- 患者信息区域 -->
    <div class="patient-info-header">
      <span class="info-item">{{ patientInfo.patientName }}</span>
      <span class="info-item">{{ patientInfo.gender }}</span>
      <span class="info-item">{{ patientInfo.age }}岁</span>
      <span class="info-item">出生日期：{{ patientInfo.birthDate }}</span>
      <span class="info-item">病种：{{ patientInfo.diseaseType }}</span>
      <span class="info-item">档案号：{{ patientInfo.medicalRecordNo }}</span>
    </div>

    <!-- 批量执行按钮 -->
    <div style="text-align: right; margin-bottom: 10px;">
      <el-button
        type="primary"
        class="batch-execute-btn"
        :disabled="selectedExercises.length === 0"
        @click="handleBatchExecute"
      >
        批量执行
      </el-button>
    </div>

    <!-- 运动处方表格 -->
    <el-table
      ref="exerciseTable"
      :data="exerciseList"
      border
      class="prescription-table"
      :header-cell-style="{
        backgroundColor: 'rgb(248, 248, 249)',
        color: 'rgb(81, 90, 110)',
        textAlign: 'center'
      }"
      :row-class-name="getRowClassName"
      @selection-change="handleSelectionChange"
    >
      <!-- 选择列 -->
      <el-table-column type="selection" width="55" align="center" />

      <!-- 序号列 -->
      <el-table-column label="序号" type="index" width="80" align="center" :index="index => index + 1" />

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

      <!-- 单次运动量 -->
      <el-table-column label="单次运动量" prop="singleExerciseAmount" align="center" width="130" />

      <!-- 操作 -->
      <el-table-column label="操作" width="180" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleExecuteSingle(scope.row)"
          >
            执行
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleViewList(scope.row)"
          >
            清单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 返回按钮 -->
    <div style="text-align: center; margin-top: 20px;">
      <el-button @click="handleBack">返回</el-button>
    </div>

    <!-- 执行信息弹窗 -->
    <el-dialog
      title="执行信息"
      :visible.sync="executeDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="executeForm"
        :model="executeForm"
        :rules="executeRules"
        label-width="100px"
      >
        <el-form-item label="执行次数" prop="executionCount">
          <el-input-number
            v-model="executeForm.executionCount"
            :min="1"
            :max="999"
            style="width: 100%;"
          />
        </el-form-item>

        <el-form-item label="执行人员" prop="executor">
          <el-input v-model="executeForm.executor" placeholder="请输入执行人员" />
        </el-form-item>

        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="executeForm.startTime"
            type="datetime"
            placeholder="请选择开始时间"
            style="width: 100%;"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="executeForm.endTime"
            type="datetime"
            placeholder="请选择结束时间"
            style="width: 100%;"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="执行记录">
          <el-input
            v-model="executeForm.executionRecord"
            type="textarea"
            :rows="3"
            placeholder="请输入执行记录"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" style="text-align: center;">
        <el-button
          type="primary"
          class="confirm-execute-btn"
          @click="confirmExecute"
        >
          确认执行
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPrescriptionDetailWithExercises, executePrescriptionBatch } from '@/api/prescription'

export default {
  name: 'PrescriptionExecute',
  data() {
    return {
      loading: false,
      prescriptionId: null,
      patientInfo: {
        patientName: '',
        gender: '',
        age: '',
        birthDate: '',
        diseaseType: '',
        medicalRecordNo: ''
      },
      exerciseList: [],
      selectedExercises: [],
      // 执行弹窗
      executeDialogVisible: false,
      currentExecuteItem: null, // 当前执行的项目
      executeForm: {
        executionCount: 1,
        executor: '系统',
        startTime: '',
        endTime: '',
        executionRecord: '无异常'
      },
      executeRules: {
        executionCount: [
          { required: true, message: '请输入执行次数', trigger: 'blur' }
        ],
        executor: [
          { required: true, message: '请输入执行人员', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ]
      }
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

        // 计算年龄
        const age = this.calculateAge(data.birthDate)

        // 设置患者信息
        this.patientInfo = {
          patientName: data.patientName || '-',
          gender: data.gender || '-',
          age: age,
          birthDate: data.birthDate || '-',
          diseaseType: data.diseaseType || '-',
          medicalRecordNo: data.medicalRecordNo || '-'
        }

        // 设置运动项目列表
        this.exerciseList = data.exerciseList || []

        // 默认全选
        this.$nextTick(() => {
          this.exerciseList.forEach(row => {
            this.$refs.exerciseTable.toggleRowSelection(row, true)
          })
        })

        this.loading = false
      }).catch(error => {
        console.error('加载处方详情失败:', error)
        this.$message.error('加载处方详情失败')
        this.loading = false
      })
    },

    /** 计算年龄 */
    calculateAge(birthDate) {
      if (!birthDate) return ''
      const birth = new Date(birthDate)
      const today = new Date()
      let age = today.getFullYear() - birth.getFullYear()
      const monthDiff = today.getMonth() - birth.getMonth()
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--
      }
      return age
    },

    /** 选择变化 */
    handleSelectionChange(selection) {
      this.selectedExercises = selection
      // 强制更新表格以触发行样式重新计算
      this.$forceUpdate()
    },

    /** 获取行样式类名 */
    getRowClassName({ row }) {
      // 检查当前行是否被选中
      const isSelected = this.selectedExercises.some(item => item.id === row.id)
      return isSelected ? 'selected-row' : ''
    },

    /** 批量执行 */
    handleBatchExecute() {
      if (this.selectedExercises.length === 0) {
        this.$message.warning('请至少选择一条运动项目')
        return
      }

      // 打开执行弹窗
      this.currentExecuteItem = null // 批量执行
      this.openExecuteDialog()
    },

    /** 单个执行 */
    handleExecuteSingle(row) {
      // 打开执行弹窗
      this.currentExecuteItem = row
      this.openExecuteDialog()
    },

    /** 打开执行弹窗 */
    openExecuteDialog() {
      // 重置表单
      this.executeForm = {
        executionCount: 1,
        executor: '系统',
        startTime: this.getCurrentDateTime(),
        endTime: '',
        executionRecord: '无异常'
      }
      this.executeDialogVisible = true
      // 清除表单验证
      this.$nextTick(() => {
        if (this.$refs.executeForm) {
          this.$refs.executeForm.clearValidate()
        }
      })
    },

    /** 获取当前日期时间 */
    getCurrentDateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const seconds = String(now.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },

    /** 确认执行 */
    confirmExecute() {
      this.$refs.executeForm.validate(valid => {
        if (!valid) {
          return false
        }

        // 验证结束时间必须大于开始时间
        if (new Date(this.executeForm.endTime) <= new Date(this.executeForm.startTime)) {
          this.$message.error('结束时间必须大于开始时间')
          return false
        }

        // 构建执行数据
        const executeData = {
          prescriptionId: this.prescriptionId,
          patientId: this.$route.query.patientId,
          executionCount: this.executeForm.executionCount,
          executor: this.executeForm.executor,
          startTime: this.executeForm.startTime,
          endTime: this.executeForm.endTime,
          executionRecord: this.executeForm.executionRecord
        }

        if (this.currentExecuteItem) {
          // 单个执行
          executeData.exerciseId = this.currentExecuteItem.id
        } else {
          // 批量执行
          executeData.exerciseIds = this.selectedExercises.map(item => item.id)
        }

        // 调用后端API执行
        executePrescriptionBatch(executeData).then(() => {
          this.$message.success('执行成功')
          this.executeDialogVisible = false

          // 返回上一页
          setTimeout(() => {
            this.handleBack()
          }, 500)
        }).catch(error => {
          console.error('执行失败:', error)
          this.$message.error('执行失败')
        })
      })
    },

    /** 查看清单 */
    handleViewList(row) {
      // TODO: 跳转到清单页面
      this.$message.info('查看清单功能待实现')
    },

    /** 返回上一页 */
    handleBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.patient-info-header {
  background-color: #f5f7fa;
  padding: 15px 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 30px;

  .info-item {
    font-size: 14px;
    color: #303133;
    white-space: nowrap;
  }
}

.batch-execute-btn {
  background-color: rgb(106, 91, 140);
  border-color: rgb(106, 91, 140);

  &:hover,
  &:focus {
    background-color: rgb(96, 81, 130);
    border-color: rgb(96, 81, 130);
  }
}

.prescription-table {
  // 选中行的背景色
  ::v-deep .selected-row {
    background-color: rgb(235, 228, 242) !important;

    td {
      background-color: rgb(235, 228, 242) !important;
    }
  }

  // 表头样式
  ::v-deep .el-table__header th {
    color: rgb(81, 90, 110);
    font-weight: 500;
    background-color: rgb(248, 248, 249);
    padding: 12px 0;
  }

  // 表格单元格样式
  ::v-deep .el-table__body td {
    padding: 12px 20px;
  }

  // 操作列按钮样式
  ::v-deep .el-button--text {
    padding: 0 5px;
    margin: 0 2px;
    font-size: 14px;
    color: rgb(106, 91, 140);

    &:hover {
      color: rgb(96, 81, 130);
    }
  }

  // 固定操作列样式
  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
    background-color: #fff;
  }

  // 固定列的表头也需要背景色
  ::v-deep .el-table__fixed-right .el-table__header th {
    background-color: rgb(248, 248, 249);
  }
}

// 执行弹窗样式
::v-deep .el-dialog {
  .el-dialog__header {
    padding: 20px 20px 10px;
    border-bottom: 1px solid #e8e8e8;

    .el-dialog__title {
      font-size: 18px;
      font-weight: 500;
      color: #303133;
    }
  }

  .el-dialog__body {
    padding: 30px 20px;
  }

  .el-form {
    .el-form-item__label {
      font-weight: 400;
      color: #606266;

      &::before {
        color: #f56c6c;
        margin-right: 4px;
      }
    }

    .el-input-number {
      width: 100%;

      .el-input__inner {
        text-align: left;
      }
    }
  }
}

.confirm-execute-btn {
  background-color: rgb(106, 91, 140);
  border-color: rgb(106, 91, 140);
  padding: 10px 40px;
  font-size: 14px;

  &:hover,
  &:focus {
    background-color: rgb(96, 81, 130);
    border-color: rgb(96, 81, 130);
  }
}
</style>
