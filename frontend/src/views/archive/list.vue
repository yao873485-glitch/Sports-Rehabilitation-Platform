<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <!-- 时间筛选 -->
      <div class="filter-item">
        <label class="filter-label">创建时间：</label>
        <el-date-picker
          v-model="queryParams.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          style="width: 300px;"
        />
      </div>

      <!-- 搜索功能 -->
      <div class="filter-item search-item">
        <label class="filter-label">搜索：</label>
        <el-input
          v-model="queryParams.name"
          placeholder="请输入患者姓名/联系电话"
          style="width: 250px;"
          clearable
        />
      </div>

      <!-- 操作按钮 -->
      <div class="filter-item">
        <el-button class="custom-primary-btn" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button class="custom-primary-btn" icon="el-icon-plus" @click="handleQuickCreate">快速建档</el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="patientList"
      highlight-current-row
      style="width: 100%"
      empty-text="暂无数据"
      :header-cell-style="{ color: 'rgb(81, 90, 110)', fontWeight: '500', backgroundColor: 'rgb(248, 248, 249)' }"
    >
      <!-- 患者信息列 -->
      <el-table-column
        label="患者信息"
        width="200"
      >
        <template slot-scope="scope">
          <div class="patient-info">
            <span class="patient-name">{{ scope.row.name }}</span>
            <span :class="scope.row.gender === '男' ? 'male-icon' : 'female-icon'">
              {{ scope.row.gender === '男' ? '♂' : '♀' }}
            </span>
            <span class="patient-age">{{ calculateAge(scope.row.birthDate) }}岁</span>
          </div>
        </template>
      </el-table-column>

      <!-- 其他字段 -->
      <el-table-column prop="birthDate" label="出生日期" width="200" align="center" header-align="center" />
      <el-table-column label="证件号" width="220" align="center" header-align="center">
        <template slot-scope="scope">
          {{ maskIdCard(scope.row.idCard) }}
        </template>
      </el-table-column>
      <el-table-column label="联系电话" width="200" align="center" header-align="center">
        <template slot-scope="scope">
          {{ maskPhone(scope.row.phone) }}
        </template>
      </el-table-column>
      <el-table-column prop="medicalRecordNo" label="档案号" width="240" align="center" header-align="center" />
      <el-table-column prop="diseaseType" label="病种" width="200" align="center" header-align="center" />
      <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" header-align="center" />
      <el-table-column prop="address" label="详细地址" width="300" align="center" header-align="center" />
      <el-table-column prop="createdTime" label="创建时间" width="220" align="center" header-align="center" />

      <!-- 操作列 - 固定右侧 -->
      <el-table-column
        label="操作"
        width="300"
        fixed="right"
        align="center"
        header-align="center"
        class-name="operation-column"
      >
        <template slot-scope="scope">
          <div class="operation-buttons">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-info"
              @click="handleDetail(scope.row)"
            >
              详情
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
          </div>
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

    <!-- 患者详情对话框 -->
    <el-dialog
      title=""
      :visible.sync="detailVisible"
      width="900px"
      :show-close="false"
      append-to-body
      class="patient-detail-dialog"
    >
      <patient-info-detail
        v-if="detailVisible"
        :patient-data="currentPatientData"
        @back="detailVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script>
import { getPatientList } from '@/api/patient'

export default {
  name: 'ArchiveList',
  components: {
    PatientInfoDetail: () => import('./components/PatientInfoDetail')
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 患者表格数据
      patientList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateRange: null,
        name: ''
      },
      // 对话框控制
      detailVisible: false,
      // 当前患者ID
      currentPatientId: null,
      // 当前患者数据
      currentPatientData: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
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

    /** 脱敏证件号 - 从第4位开始到倒数第5位结束用*隐藏 */
    maskIdCard(idCard) {
      if (!idCard || idCard.length < 8) return idCard || '-'
      const start = idCard.substring(0, 3) // 前3位
      const end = idCard.substring(idCard.length - 4) // 后4位
      const maskLength = idCard.length - 7 // 需要遮蔽的长度
      const mask = '*'.repeat(maskLength)
      return start + mask + end
    },

    /** 脱敏手机号 - 从第4位到倒数第5位结束用*表示 */
    maskPhone(phone) {
      if (!phone || phone.length < 8) return phone || '-'
      const start = phone.substring(0, 3) // 前3位
      const end = phone.substring(phone.length - 4) // 后4位
      const maskLength = phone.length - 7 // 需要遮蔽的长度
      const mask = '*'.repeat(maskLength)
      return start + mask + end
    },

    /** 查询患者列表 */
    getList() {
      this.loading = true
      const params = { ...this.queryParams }

      // 处理日期范围
      if (params.dateRange && params.dateRange.length === 2) {
        params.startTime = params.dateRange[0] + ' 00:00:00'
        params.endTime = params.dateRange[1] + ' 23:59:59'
      }
      delete params.dateRange

      getPatientList(params).then(response => {
        if (response && response.data) {
          this.patientList = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.patientList = []
          this.total = 0
        }
        this.loading = false
      }).catch(error => {
        console.error('获取患者列表失败:', error)
        this.$message.error('获取患者列表失败，请稍后重试')
        this.patientList = []
        this.total = 0
        this.loading = false
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      try {
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          dateRange: null,
          name: ''
        }
        this.handleQuery()
      } catch (error) {
        console.error('重置查询失败:', error)
        this.$message.error('重置失败，请稍后重试')
      }
    },

    /** 查看患者详情 */
    handleView(row) {
      // 跳转到患者只读查看页面
      this.$router.push({
        path: '/patient/create',
        query: {
          patientId: row.id,
          mode: 'view'
        }
      })
    },

    /** 查看患者详情 */
    handleDetail(row) {
      this.currentPatientData = { ...row }
      // 隐藏手机号中间四位
      if (this.currentPatientData.phone) {
        const phone = this.currentPatientData.phone
        this.currentPatientData.hiddenPhone = phone.substring(0, 3) + '****' + phone.substring(7)
      }
      this.detailVisible = true
    },

    /** 编辑患者信息 */
    handleEdit(row) {
      // 跳转到患者编辑页面（复用快速建档页面）
      this.$router.push({
        path: '/patient/create',
        query: {
          patientId: row.id,
          from: 'archive'
        }
      })
    },

    /** 快速建档 */
    handleQuickCreate() {
      // 跳转到患者建档页面（新建模式）
      this.$router.push({
        path: '/patient/create'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 20px;

  .filter-item {
    margin-right: 15px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;

    .filter-label {
      margin-right: 8px;
      font-size: 14px;
      color: #606266;
      white-space: nowrap;
    }

    // 日期选择器特殊样式
    ::v-deep .el-date-editor {
      .el-range-separator {
        padding: 0 5px;
        min-width: 24px;
        text-align: center;
        font-weight: 500;
        color: #606266;
      }

      .el-range-input {
        width: 45%;
      }
    }
  }

  // 搜索项增加左侧间距
  .search-item {
    margin-left: 60px;
  }
}

// 自定义按钮样式
.custom-primary-btn {
  background-color: rgb(106, 91, 140);
  border-color: rgb(106, 91, 140);
  color: #fff;

  &:hover,
  &:focus {
    background-color: rgb(96, 81, 130);
    border-color: rgb(96, 81, 130);
    color: #fff;
  }

  &:active {
    background-color: rgb(86, 71, 120);
    border-color: rgb(86, 71, 120);
  }
}

.patient-info {
  display: flex;
  align-items: center;
  white-space: nowrap;

  .patient-name {
    font-weight: 500;
    color: #303133;
    margin-right: 4px;
  }

  .patient-age {
    color: #666;
    font-size: 13px;
    margin-left: 4px;
  }
}

.male-icon {
  color: #409eff;
  font-weight: bold;
  font-size: 14px;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
  font-size: 14px;
}

// 固定操作列样式
.el-table {
  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
    background-color: #fff;
  }

  // 固定列的表头也需要背景色
  ::v-deep .el-table__fixed-right .el-table__header th {
    background-color: rgb(248, 248, 249);
  }

  // 只显示横线，不显示竖线
  ::v-deep td,
  ::v-deep th {
    border-right: none;
  }

  ::v-deep .el-table__body,
  ::v-deep .el-table__header {
    border-right: none;
  }

  // 保留横线
  ::v-deep td {
    border-bottom: 1px solid #EBEEF5;
  }

  ::v-deep th {
    border-bottom: 1px solid #EBEEF5;
  }

  // 表头字体颜色和背景色
  ::v-deep .el-table__header th {
    color: rgb(81, 90, 110);
    font-weight: 500;
    background-color: rgb(248, 248, 249);
  }

  // 表格单元格内边距调整，使字段间距更均匀
  ::v-deep .el-table__body td,
  ::v-deep .el-table__header th {
    padding: 12px 20px;
    white-space: nowrap;
  }

  // 第一列（患者信息）增加左侧内边距
  ::v-deep .el-table__body td:first-child,
  ::v-deep .el-table__header th:first-child {
    padding-left: 40px;
  }

  // 单元格内容样式
  ::v-deep .cell {
    white-space: nowrap;
  }

  // 启用横向滚动条
  ::v-deep .el-table__body-wrapper {
    overflow-x: auto;
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

    // 图标颜色
    i {
      color: rgb(106, 91, 140);
    }

    &:hover i {
      color: rgb(96, 81, 130);
    }
  }

  // 操作列容器样式
  .operation-buttons {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
    white-space: nowrap;

    .el-button {
      color: rgb(106, 91, 140);

      &:hover {
        color: rgb(96, 81, 130);
      }
    }
  }

  // 确保操作列单元格不会触发省略号
  ::v-deep .operation-column .cell {
    overflow: visible !important;
    text-overflow: clip !important;
  }
}
</style>
