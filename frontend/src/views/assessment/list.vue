<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <!-- 第一排 -->
      <div class="filter-row">
        <div class="filter-item">
          <label>病种类型：</label>
          <el-select v-model="queryParams.diseaseType" placeholder="全部" clearable style="width: 150px;">
            <el-option label="全部" value="" />
            <el-option
              v-for="type in diseaseTypes"
              :key="type"
              :label="type"
              :value="type"
            />
          </el-select>
        </div>

        <div class="filter-item">
          <label>评定状态：</label>
          <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 150px;">
            <el-option label="全部" value="" />
            <el-option label="已创建" :value="1" />
            <el-option label="执行中" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已结束" :value="4" />
          </el-select>
        </div>

        <div class="filter-item">
          <label>搜索：</label>
          <el-input
            v-model="queryParams.searchKeyword"
            placeholder="请输入患者姓名/电话/档案号"
            style="width: 240px;"
            clearable
          />
        </div>

        <div class="filter-item">
          <label>入组机构：</label>
          <el-select v-model="queryParams.enrollmentInstitution" placeholder="全部" clearable style="width: 200px;">
            <el-option label="全部" value="" />
            <el-option
              v-for="institution in institutions"
              :key="institution"
              :label="institution"
              :value="institution"
            />
          </el-select>
        </div>
      </div>

      <!-- 第二排 -->
      <div class="filter-row">
        <div class="filter-item">
          <label>开方医生：</label>
          <el-input
            v-model="queryParams.prescribingDoctor"
            placeholder="请输入"
            style="width: 150px;"
            clearable
          />
        </div>

        <div class="filter-item">
          <el-button class="custom-primary-btn" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </div>
      </div>
    </div>

    <!-- 表格操作栏 -->
    <div class="table-header">
      <div class="table-title">评定方案列表</div>
      <el-button class="custom-primary-btn" icon="el-icon-plus" @click="handleAdd">新增</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="assessmentList"
      border
      highlight-current-row
      style="width: 100%"
      :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
    >
      <el-table-column prop="assessmentName" label="评定方案名称" width="220" align="center" header-align="center" />

      <el-table-column
        prop="customerInfo"
        label="客户信息"
        width="180"
        align="center"
        header-align="center"
      >
        <template slot-scope="scope">
          <span>
            {{ scope.row.patientName }}
            <span :class="scope.row.gender === '男' ? 'male-icon' : 'female-icon'">
              {{ scope.row.gender === '男' ? '♂' : '♀' }}
            </span>
            {{ calculateAge(scope.row.birthDate) }}岁
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="medicalRecordNumber" label="档案号" width="180" align="center" header-align="center" />

      <el-table-column prop="diseaseType" label="病种" width="150" align="center" header-align="center" />

      <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" header-align="center" />

      <el-table-column prop="executionInstitution" label="执行机构" width="200" align="center" header-align="center" />

      <el-table-column prop="status" label="状态" width="140" align="center" header-align="center">
        <template slot-scope="scope">
          <el-tag
            :type="getStatusType(scope.row.status)"
            size="mini"
          >
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="prescribingDoctor" label="开方医生" width="140" align="center" header-align="center" />

      <el-table-column prop="createdTime" label="创建时间" width="180" align="center" header-align="center" />

      <el-table-column
        label="操作"
        width="240"
        fixed="right"
        align="center"
        header-align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!-- 查看按钮 - 所有状态都显示 -->
          <el-button
            size="mini"
            type="text"
            @click="handleView(scope.row)"
          >
            查看
          </el-button>

          <!-- 已创建状态(1)：显示编辑、删除 -->
          <el-button
            v-if="scope.row.status == 1"
            size="mini"
            type="text"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            v-if="scope.row.status == 1"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>

          <!-- 执行中状态(2)：显示结束 -->
          <el-button
            v-if="scope.row.status == 2"
            size="mini"
            type="text"
            @click="handleEnd(scope.row)"
          >
            结束
          </el-button>

          <!-- 已结束状态(4)：只显示查看按钮 -->
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增评定方案对话框 -->

    <patient-select-dialog
      :visible.sync="patientSelectVisible"
      :show-quick-create="false"
      @confirm="handlePatientConfirm"
    />
  </div>
</template>

<script>
import {
  getPatientAssessmentPlanList,
  endPatientAssessmentPlan,
  deletePatientAssessmentPlan,
  getDiseaseTypes,
  getInstitutions
} from '@/api/patient-assessment-plan'
import PatientSelectDialog from '@/views/scheme/components/PatientSelectDialog.vue'

export default {
  name: 'AssessmentList',
  components: {
    PatientSelectDialog
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 评定方案表格数据
      assessmentList: [],
      // 病种类型列表
      diseaseTypes: [],
      // 机构列表
      institutions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        diseaseType: '',
        status: '',
        enrollmentInstitution: '',
        prescribingDoctor: '',
        searchKeyword: ''
      },
      // ??????????
      patientSelectVisible: false
    }
  },
  created() {
    this.getList()
    this.getDiseaseTypes()
    this.getInstitutions()
  },
  methods: {
    /** 查询评定方案列表 */
    getList() {
      this.loading = true
      getPatientAssessmentPlanList(this.queryParams).then(response => {
        this.assessmentList = response.data.records
        this.total = response.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /** 获取病种类型 */
    getDiseaseTypes() {
      getDiseaseTypes().then(response => {
        this.diseaseTypes = response.data
      })
    },

    /** 获取机构列表 */
    getInstitutions() {
      getInstitutions().then(response => {
        this.institutions = response.data
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        diseaseType: '',
        status: '',
        enrollmentInstitution: '',
        prescribingDoctor: '',
        searchKeyword: ''
      }
      this.handleQuery()
    },

    /** ????????? */
    handleAdd() {
      this.patientSelectVisible = true
    },

    /** ??????????*/
    handlePatientConfirm(patient) {
      this.patientSelectVisible = false
      this.$router.push({
        path: '/assessment/create',
        query: {
          patientData: JSON.stringify(patient)
        }
      })
    },

    /** 查看详情 */
    handleView(row) {
      this.$router.push({
        path: '/assessment/view',
        query: { id: row.id }
      })
    },

    /** 结束评定方案 */
    handleEnd(row) {
      this.$confirm('确认结束该评定方案吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        endPatientAssessmentPlan(row.id).then(() => {
          this.$message.success('评定方案已结束')
          this.getList()
        })
      }).catch(() => {
        // 用户点击取消，不做任何操作
      })
    },

    /** 编辑评定方案 */
    handleEdit(row) {
      this.$router.push({
        path: '/assessment/edit',
        query: { id: row.id }
      })
    },

    /** 删除评定方案 */
    handleDelete(row) {
      this.$confirm('确认删除该评定方案吗？删除后将无法恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePatientAssessmentPlan(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {
        // 用户点击取消，不做任何操作
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

    /** 获取状态文本 */
    getStatusText(status) {
      const statusMap = {
        1: '已创建',
        2: '执行中',
        3: '已完成',
        4: '已结束'
      }
      return statusMap[status] || '未知'
    },

    /** 获取状态类型 */
    getStatusType(status) {
      const statusMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  margin-bottom: 20px;

  .filter-row {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    margin-bottom: 10px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .filter-item {
    display: flex;
    align-items: center;
    margin-right: 15px;
    margin-bottom: 10px;

    label {
      margin-right: 8px;
      font-weight: 500;
      white-space: nowrap;
      color: #606266;
    }
  }
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

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;

  .table-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.male-icon {
  color: #409eff;
  font-weight: bold;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
}

.el-table {
  // 表头样式
  ::v-deep .el-table__header th {
    color: rgb(37, 37, 37);
    font-weight: 500;
    background-color: rgb(250, 250, 250);
    padding: 16px 0;
  }

  // 表格单元格样式 - 增加内边距
  ::v-deep .el-table__body td {
    padding: 16px 0;
  }

  // 表头和单元格居中对齐
  ::v-deep .el-table__header th,
  ::v-deep .el-table__body td {
    text-align: center;
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

    i {
      color: rgb(106, 91, 140);
    }

    &:hover i {
      color: rgb(96, 81, 130);
    }
  }

  ::v-deep .fixed-width {
    .el-button--mini {
      padding: 2px 4px;
      margin-left: 2px;
    }
  }

  // 固定操作列样式
  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
    background-color: #fff;

    .el-table__header th {
      background-color: rgb(250, 250, 250);
    }

    .el-table__fixed-right-patch {
      background-color: rgb(250, 250, 250);
    }
  }
}
</style>
