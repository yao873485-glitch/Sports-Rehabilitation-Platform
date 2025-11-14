<template>
  <el-dialog
    title="选择患者"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入患者姓名/电话/档案号"
        clearable
        style="width: 400px; margin-right: 10px;"
      />
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      <el-button
        type="primary"
        style="float: right; background-color: #9b59d6; border-color: #9b59d6;"
        @click="handleQuickCreate"
      >
        快速建档
      </el-button>
    </div>

    <!-- 患者列表 -->
    <div class="patient-list-container">
      <el-radio-group v-model="selectedPatientId" style="width: 100%;">
        <div v-if="patientList.length === 0" class="empty-data">
          <el-empty description="暂无患者数据"></el-empty>
        </div>
        <div
          v-for="patient in patientList"
          :key="patient.id"
          class="patient-item"
        >
          <el-radio :label="patient.id" class="patient-radio" @click.native.prevent="handleRadioClick(patient.id)">
            <div class="patient-card">
              <div class="patient-info-row">
                <!-- 患者姓名 + 性别 + 年龄 -->
                <div class="info-group">
                  <span class="label">患者信息：</span>
                  <span class="patient-name">{{ patient.name }}</span>
                  <span :class="patient.gender === '男' ? 'gender-icon male' : 'gender-icon female'">
                    ●
                  </span>
                  <span class="patient-age">{{ calculateAge(patient.birthDate) }}岁</span>
                </div>

                <!-- 出生日期 -->
                <div class="info-group">
                  <span class="label">出生日期：</span>
                  <span class="value">{{ patient.birthDate }}</span>
                </div>

                <!-- 档案号 -->
                <div class="info-group">
                  <span class="label">档案号：</span>
                  <span class="value">{{ patient.medicalRecordNo }}</span>
                </div>

                <!-- 病种 -->
                <div class="info-group">
                  <span class="label">病种：</span>
                  <span class="value">{{ patient.diseaseType }}</span>
                </div>

                <!-- 入组机构 -->
                <div class="info-group">
                  <span class="label">入组机构：</span>
                  <span class="value">{{ patient.enrollmentInstitution }}</span>
                </div>
              </div>
            </div>
          </el-radio>
        </div>
      </el-radio-group>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <div class="pagination-info">
        当前显示 {{ ((queryParams.pageNum - 1) * queryParams.pageSize + 1) }}-{{ Math.min(queryParams.pageNum * queryParams.pageSize, total) }} 条，总共 {{ total }} 条数据
      </div>
      <el-pagination
        :current-page="queryParams.pageNum"
        :page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[5]"
        layout="prev, pager, next, sizes"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>

    <!-- 底部按钮 -->
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleConfirm">确 认</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getPatientList } from '@/api/patient'

export default {
  name: 'PatientSelectDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      searchKeyword: '',
      selectedPatientId: null,
      patientList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        searchKeyword: ''
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.resetData()
        this.getPatientList()
      }
    }
  },
  methods: {
    /** 处理单选按钮点击 - 支持取消选中 */
    handleRadioClick(patientId) {
      if (this.selectedPatientId === patientId) {
        // 如果点击的是已选中的项，则取消选中
        this.selectedPatientId = null
      } else {
        // 否则选中该项
        this.selectedPatientId = patientId
      }
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

    /** 获取患者列表 */
    getPatientList() {
      const params = {
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize,
        searchKeyword: this.queryParams.searchKeyword
      }

      getPatientList(params).then(response => {
        this.patientList = response.data.records || []
        this.total = response.data.total || 0
      }).catch(error => {
        console.error('获取患者列表失败:', error)
        this.$message.error('获取患者列表失败')
        this.patientList = []
        this.total = 0
      })
    },

    /** 搜索 */
    handleSearch() {
      this.queryParams.searchKeyword = this.searchKeyword
      this.queryParams.pageNum = 1
      this.getPatientList()
    },

    /** 重置 */
    handleReset() {
      this.searchKeyword = ''
      this.queryParams.searchKeyword = ''
      this.queryParams.pageNum = 1
      this.getPatientList()
    },

    /** 分页 - 页码变化 */
    handlePageChange(page) {
      this.queryParams.pageNum = page
      this.getPatientList()
    },

    /** 分页 - 每页数量变化 */
    handleSizeChange(size) {
      this.queryParams.pageSize = size
      this.queryParams.pageNum = 1
      this.getPatientList()
    },

    /** 快速建档 */
    handleQuickCreate() {
      // 关闭弹窗并通知父组件跳转到建档页面
      this.handleClose()
      this.$emit('quick-create')
    },

    /** 确认选择 */
    handleConfirm() {
      if (!this.selectedPatientId) {
        this.$message.warning('请选择一个患者')
        return
      }

      // 找到选中的患者信息
      const selectedPatient = this.patientList.find(p => p.id === this.selectedPatientId)
      if (!selectedPatient) {
        this.$message.error('未找到选中的患者信息')
        return
      }

      this.$emit('confirm', selectedPatient)
    },

    /** 关闭弹窗 */
    handleClose() {
      this.$emit('update:visible', false)
      this.resetData()
    },

    /** 重置数据 */
    resetData() {
      this.searchKeyword = ''
      this.selectedPatientId = null
      this.patientList = []
      this.total = 0
      this.queryParams = {
        pageNum: 1,
        pageSize: 5,
        searchKeyword: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.search-container {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.patient-list-container {
  min-height: 300px;
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: 20px;

  .empty-data {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 300px;
  }

  .patient-item {
    margin-bottom: 10px;
    transition: all 0.3s;

    &:hover {
      .patient-card {
        border-color: #409eff;
        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
      }
    }

    .patient-radio {
      width: 100%;
      margin-right: 0;
      display: flex;
      align-items: center;

      ::v-deep .el-radio__input {
        align-self: center;
      }

      ::v-deep .el-radio__label {
        width: 100%;
        padding-left: 10px;
      }
    }

    .patient-card {
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      padding: 15px;
      background-color: #fff;
      transition: all 0.3s;
      cursor: pointer;

      .patient-info-row {
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex-wrap: nowrap;
        gap: 20px;

        .info-group {
          display: flex;
          align-items: center;
          flex-shrink: 0;

          .label {
            color: #606266;
            font-size: 14px;
            margin-right: 5px;
            white-space: nowrap;
          }

          .value {
            color: #303133;
            font-size: 14px;
            font-weight: 500;
            white-space: nowrap;
          }

          .patient-name {
            color: #303133;
            font-size: 14px;
            font-weight: 600;
            margin-right: 5px;
          }

          .gender-icon {
            font-size: 16px;
            margin-right: 5px;

            &.male {
              color: #409eff;
            }

            &.female {
              color: #f56c6c;
            }
          }

          .patient-age {
            color: #909399;
            font-size: 13px;
          }
        }
      }
    }
  }

  // 选中状态样式
  ::v-deep .el-radio__input.is-checked + .el-radio__label .patient-card {
    border-color: #409eff;
    background-color: #ecf5ff;
  }
}

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;

  .pagination-info {
    font-size: 13px;
    color: #606266;
  }
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 10px;
}

// 滚动条样式
.patient-list-container::-webkit-scrollbar {
  width: 6px;
}

.patient-list-container::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;

  &:hover {
    background-color: #c0c4cc;
  }
}

.patient-list-container::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>
