<template>
  <el-dialog
    title="选择患者"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <!-- 搜索区域 -->
    <div class="search-container">
      <div class="search-label">搜索：</div>
      <el-input
        v-model="searchKeyword"
        placeholder="请输入患者姓名/电话/档案号"
        clearable
        style="width: 300px;"
        @keyup.enter="handleSearch"
        @clear="handleReset"
      />
      <el-button class="custom-primary-btn" icon="el-icon-search" @click="handleSearch">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      <el-button
        v-if="showQuickCreate"
        class="custom-primary-btn quick-create-btn"
        @click="handleQuickCreate"
      >
        快速建档
      </el-button>
    </div>

    <!-- 患者表格 -->
    <div class="patient-table-container">
      <el-table
        ref="patientTable"
        :data="patientList"
        highlight-current-row
        style="width: 100%"
        :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
        :row-class-name="getRowClassName"
        @row-click="handleRowClick"
      >
        <el-table-column width="55" align="center" header-align="center">
          <template slot-scope="scope">
            <div class="radio-wrapper" @click.stop="handleRadioClick(scope.row.id)">
              <span
                class="custom-radio"
                :class="{ 'is-checked': selectedPatientId === scope.row.id }"
              >
                <span class="custom-radio-inner"></span>
              </span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="患者信息" width="180" align="center" header-align="center">
          <template slot-scope="scope">
            <span class="patient-name">{{ scope.row.name }}</span>
            <span :class="scope.row.gender === '男' ? 'male-icon' : 'female-icon'">
              {{ scope.row.gender === '男' ? '♂' : '♀' }}
            </span>
            <span class="patient-age">{{ calculateAge(scope.row.birthDate) }}岁</span>
          </template>
        </el-table-column>

        <el-table-column prop="birthDate" label="出生日期" width="150" align="center" header-align="center" />

        <el-table-column prop="medicalRecordNo" label="档案号" width="220" align="center" header-align="center" />

        <el-table-column prop="diseaseType" label="病种" width="180" align="center" header-align="center" />

        <el-table-column prop="enrollmentInstitution" label="入组机构" min-width="200" align="center" header-align="center" />
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :current-page="queryParams.pageNum"
        :page-size="queryParams.pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
      <div class="pagination-info">
        {{ queryParams.pageSize }}条/页
      </div>
    </div>

    <!-- 底部按钮 -->
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button class="custom-primary-btn" @click="handleConfirm">确认</el-button>
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
    },
    showQuickCreate: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      searchKeyword: '',
      selectedPatientId: null,
      patientList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        name: '' // 后端使用name参数
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.resetData()
        this.getPatientList()
      }
    }
  },
  methods: {
    /** 获取表格行样式类名 */
    getRowClassName({ row }) {
      return this.selectedPatientId === row.id ? 'selected-row' : ''
    },

    /** 处理表格行点击 */
    handleRowClick(row) {
      this.handleRadioClick(row.id)
    },

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
        name: this.queryParams.name // 后端使用name参数进行搜索
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
      this.queryParams.name = this.searchKeyword
      this.queryParams.pageNum = 1
      this.selectedPatientId = null // 清除选中状态
      this.getPatientList()
    },

    /** 重置 */
    handleReset() {
      this.searchKeyword = ''
      this.queryParams.name = ''
      this.queryParams.pageNum = 1
      this.selectedPatientId = null // 清除选中状态
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
        name: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.search-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
  gap: 10px;

  .search-label {
    font-weight: 500;
    color: #606266;
    white-space: nowrap;
  }

  .quick-create-btn {
    margin-left: auto;
  }
}

.patient-table-container {
  margin-bottom: 20px;
}

.patient-name {
  font-weight: 600;
  margin-right: 5px;
}

.male-icon {
  color: #409eff;
  font-weight: bold;
  margin-right: 5px;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
  margin-right: 5px;
}

.patient-age {
  color: #909399;
  font-size: 13px;
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

// 自定义按钮样式
.custom-primary-btn {
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

// 表格样式
.el-table {
  ::v-deep .el-table__header th {
    color: rgb(37, 37, 37);
    font-weight: 500;
    background-color: rgb(250, 250, 250);
    padding: 16px 0;
  }

  ::v-deep .el-table__body td {
    padding: 16px 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  ::v-deep .el-table__header th,
  ::v-deep .el-table__body td {
    text-align: center;
  }

  // 确保表头字体颜色
  ::v-deep .el-table__header-wrapper th {
    color: rgb(37, 37, 37) !important;
  }

  // 表格行可点击样式
  ::v-deep .el-table__body tr {
    cursor: pointer;
    transition: background-color 0.2s;

    &:hover {
      background-color: #f5f7fa;
    }
  }

  // 选中行样式
  ::v-deep .el-table__body tr.selected-row {
    background-color: rgb(230, 221, 239) !important;

    td {
      background-color: rgb(230, 221, 239) !important;
    }
  }
}

// 自定义单选框样式
.radio-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 5px;
}

.custom-radio {
  display: inline-block;
  width: 18px;
  height: 18px;
  border: 2px solid #dcdfe6;
  border-radius: 50%;
  position: relative;
  background-color: #fff;
  transition: all 0.2s;
  cursor: pointer;

  &:hover {
    border-color: rgb(144, 126, 179);
  }

  // 选中状态
  &.is-checked {
    border-color: rgb(144, 126, 179);
    background-color: rgb(144, 126, 179);

    .custom-radio-inner {
      display: block;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: #fff;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }

  .custom-radio-inner {
    display: none;
  }
}

// 分页样式
::v-deep .el-pagination {
  .el-pager li {
    &.active {
      color: rgb(144, 126, 179);
      cursor: default;
    }

    &:hover {
      color: rgb(144, 126, 179);
    }
  }

  .btn-prev,
  .btn-next {
    &:hover {
      color: rgb(144, 126, 179);
    }
  }
}
</style>
