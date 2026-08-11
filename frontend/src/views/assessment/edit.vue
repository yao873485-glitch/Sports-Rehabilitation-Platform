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

    <!-- 评定计划编辑表单 -->
    <el-card style="margin-top: 20px;">
      <!-- 操作按钮区域（右上角） -->
      <div class="table-header">
        <div class="header-actions">
          <el-button class="custom-action-btn" icon="el-icon-plus" size="small" @click="handleAddRow">
            新增一行
          </el-button>
          <el-button
            class="custom-action-btn save-btn"
            :loading="saving"
            size="small"
            @click="handleSave"
          >
            保存
          </el-button>
          <el-button class="custom-action-btn" size="small" @click="handleSubmit">
            提交
          </el-button>
        </div>
      </div>

      <!-- 评定项目表格 -->
      <div style="overflow-x: auto;">
        <el-table
          ref="assessmentTable"
          :data="assessmentList"
          border
          style="width: 100%; min-width: 1400px;"
          :header-cell-style="{
            backgroundColor: 'rgb(248, 248, 249)',
            color: 'rgb(81, 90, 110)',
            textAlign: 'center'
          }"
        >
          <!-- 复选框列 -->
          <el-table-column type="selection" width="55" align="center" />

          <!-- 序号列 -->
          <el-table-column label="序号" type="index" width="80" align="center" :index="index => index + 1" />

          <!-- 执行场景 -->
          <el-table-column label="执行场景" align="center" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.executionType" placeholder="请选择" size="small">
                <el-option label="机构" value="机构" />
                <el-option label="居家" value="居家" />
              </el-select>
            </template>
          </el-table-column>

          <!-- 量表名称 -->
          <el-table-column label="量表名称" align="center" width="300">
            <template slot-scope="scope">
              <el-input v-model="scope.row.scaleName" placeholder="请选择" size="small" />
            </template>
          </el-table-column>

          <!-- 执行频率 -->
          <el-table-column label="执行频率" align="center" width="150">
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

          <!-- 周期（周） -->
          <el-table-column label="周期（周）" align="center" width="150">
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

          <!-- 总次数（次） -->
          <el-table-column label="总次数（次）" align="center" width="150">
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

          <!-- 备注 -->
          <el-table-column label="备注" align="center" min-width="200">
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
                @click="handleDeleteRow(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

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
        <el-button type="primary" @click="confirmSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAssessmentPlanDetail } from '@/api/assessment'
import request from '@/utils/request'

export default {
  name: 'AssessmentEdit',
  data() {
    return {
      loading: false,
      saving: false,
      assessmentPlanId: null,
      patientId: null,
      schemeId: null,
      // 患者信息
      patientInfo: {
        patientName: '',
        idCard: '',
        medicalRecordNo: '',
        birthDate: '',
        diagnosis: '',
        diseaseType: ''
      },
      // 评定项目列表
      assessmentList: [],
      // 执行机构对话框
      executionInstitutionDialogVisible: false,
      selectedExecutionInstitution: ''
    }
  },
  created() {
    const id = this.$route.query.id
    if (id) {
      this.assessmentPlanId = id
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
        console.log('API返回的数据:', data)

        // 保存患者ID和方案ID
        this.patientId = data.patientId
        this.schemeId = data.schemeId

        // 填充患者信息
        this.patientInfo = {
          patientName: data.patientName,
          idCard: data.idCard,
          medicalRecordNo: data.medicalRecordNo,
          birthDate: data.birthDate,
          diagnosis: data.diagnosis,
          diseaseType: data.diseaseType
        }

        // 填充评定项目列表（如果有的话）
        if (data.assessmentList && data.assessmentList.length > 0) {
          this.assessmentList = data.assessmentList
        } else {
          // 如果没有评定项目，创建一个默认项
          this.assessmentList = [{
            id: data.id,
            executionType: data.executionType || '',
            scaleName: data.scaleName || '',
            executionFrequency: data.executionFrequency || '1次/周',
            cycleDuration: data.cycleDuration || 0,
            totalTimes: data.totalTimes || 1,
            remarks: data.remarks || ''
          }]
        }

        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message.error('获取详情失败')
        this.$router.back()
      })
    },

    /** 新增一行 */
    handleAddRow() {
      this.assessmentList.push({
        executionType: '',
        scaleName: '',
        executionFrequency: '1次/周',
        cycleDuration: 0,
        totalTimes: 1,
        remarks: ''
      })
    },

    /** 删除行 */
    handleDeleteRow(index) {
      this.$confirm('确认删除该评定项目吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.assessmentList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /** 保存 */
    handleSave() {
      // 获取选中的行
      const selectedRows = this.$refs.assessmentTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的评定项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.executionType || !item.scaleName || !item.executionFrequency
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中评定项目的必填信息')
        return
      }

      // 只能选中一条记录进行保存
      if (selectedRows.length > 1) {
        this.$message.warning('保存时只能选中一条记录')
        return
      }

      this.saving = true

      const item = selectedRows[0]

      // 构建更新数据
      const updateData = {
        id: this.assessmentPlanId,
        patientId: this.patientId,
        medicalSchemeId: this.schemeId,
        executionType: item.executionType,
        scaleName: item.scaleName,
        executionFrequency: item.executionFrequency,
        cycleDuration: item.cycleDuration || 0,
        totalTimes: item.totalTimes || 1,
        remarks: item.remarks || '',
        executionInstitution: null // 保存时执行机构为空
      }

      console.log('保存数据:', updateData)

      // 调用更新API
      request({
        url: `/patient-assessment-plan/${this.assessmentPlanId}`,
        method: 'put',
        data: updateData
      }).then(() => {
        this.$message.success('保存成功')
        this.saving = false
        // 跳转到评定列表页面
        this.$router.push('/assessment/list')
      }).catch(error => {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
        this.saving = false
      })
    },

    /** 提交 */
    handleSubmit() {
      // 获取选中的行
      const selectedRows = this.$refs.assessmentTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的评定项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.executionType || !item.scaleName || !item.executionFrequency
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中评定项目的必填信息')
        return
      }

      // 只能选中一条记录进行提交
      if (selectedRows.length > 1) {
        this.$message.warning('提交时只能选中一条记录')
        return
      }

      // 显示执行机构选择对话框
      this.executionInstitutionDialogVisible = true
    },

    /** 确认提交 */
    confirmSubmit() {
      if (!this.selectedExecutionInstitution) {
        this.$message.warning('请选择执行机构')
        return
      }

      const selectedRows = this.$refs.assessmentTable.selection
      const item = selectedRows[0]

      this.saving = true

      // 构建提交数据
      const submitData = {
        id: this.assessmentPlanId,
        patientId: this.patientId,
        medicalSchemeId: this.schemeId,
        executionType: item.executionType,
        scaleName: item.scaleName,
        executionFrequency: item.executionFrequency,
        cycleDuration: item.cycleDuration || 0,
        totalTimes: item.totalTimes || 1,
        remarks: item.remarks || '',
        executionInstitution: this.selectedExecutionInstitution,
        status: 2 // 提交后状态变为执行中
      }

      console.log('提交数据:', submitData)

      // 调用更新API
      request({
        url: `/patient-assessment-plan/${this.assessmentPlanId}`,
        method: 'put',
        data: submitData
      }).then(() => {
        this.$message.success('提交成功')
        this.saving = false
        this.executionInstitutionDialogVisible = false
        // 跳转到评定列表页面
        this.$router.push('/assessment/list')
      }).catch(error => {
        console.error('提交失败:', error)
        this.$message.error('提交失败')
        this.saving = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

// 患者信息卡片样式
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

// 表格头部操作区域
.table-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;

  .header-actions {
    display: flex;
    gap: 10px;
  }

  .custom-action-btn {
    background-color: rgb(106, 91, 140);
    color: white;
    border: none;
    padding: 8px 16px;
    font-size: 14px;

    &:hover {
      background-color: rgb(96, 81, 130);
    }

    &.save-btn {
      background-color: rgb(106, 91, 140);
    }
  }
}

// 表格样式
.el-table {
  ::v-deep .el-table__header th {
    background-color: rgb(248, 248, 249);
    color: rgb(81, 90, 110);
    font-weight: 500;
    padding: 12px 0;
  }

  ::v-deep .el-table__body td {
    padding: 8px 0;
  }

  ::v-deep .el-table__header th,
  ::v-deep .el-table__body td {
    text-align: center;
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

::v-deep .el-table__fixed-right .el-table__header th {
  background-color: rgb(248, 248, 249);
}
</style>
