<template>
  <div class="scheme-center-container">
    <!-- 左侧患者列表卡片 -->
    <div class="patient-list-card">
      <!-- 顶部标签页 -->
      <div class="tabs-header">
        <div
          class="tab-item"
          :class="{ active: activeTab === 'ungrouped' }"
          @click="handleTabChange('ungrouped')"
        >
          未入组
        </div>
        <div
          class="tab-item"
          :class="{ active: activeTab === 'grouped' }"
          @click="handleTabChange('grouped')"
        >
          已入组
        </div>
      </div>

      <!-- 筛选区域 -->
      <div class="filter-section">
        <div class="filter-item">
          <label>时间日期：</label>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            size="small"
            class="date-picker"
          />
        </div>

        <div class="filter-item">
          <label>开始搜索：</label>
          <el-input
            v-model="searchKeyword"
            placeholder="请输入患者姓名或手机号码搜索"
            size="small"
            class="search-input"
            clearable
          />
        </div>

        <div class="filter-actions">
          <el-button type="primary" size="small" @click="handleSearch">
            搜索
          </el-button>
          <el-button size="small" @click="handleReset">
            清空
          </el-button>
        </div>
      </div>

      <!-- 数据表格 -->
      <div class="table-section">
        <div class="table-wrapper">
          <el-table
            :data="tableData"
            :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
            border
            style="width: 100%"
            @row-click="handleRowClick"
            highlight-current-row
          >
            <el-table-column
              prop="createTime"
              label="创建日期"
              width="140"
              align="center"
            />
            <el-table-column
              prop="patientInfo"
              label="患者信息"
              width="180"
              align="center"
            >
              <template slot-scope="scope">
                <div class="patient-info">
                  <span>{{ scope.row.patientName }}</span>
                  <span class="gender-icon" :class="scope.row.gender === 1 ? 'male' : 'female'">
                    {{ scope.row.gender === 1 ? '♂' : '♀' }}
                  </span>
                  <span class="age">{{ scope.row.age }}岁</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="archiveNo"
              label="档案号"
              width="150"
              align="center"
            />
          </el-table>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            small
          />
        </div>
      </div>

      <!-- 空状态提示 -->
      <div v-if="tableData.length === 0 && !loading" class="empty-state">
        <p>暂无数据</p>
      </div>
    </div>

    <!-- 右侧详情区域 -->
    <div class="detail-panel">
      <div v-if="!selectedPatient" class="empty-detail">
        <p>请选择患者查看详情</p>
      </div>
      <div v-else class="patient-detail">
        <!-- 这里可以展示患者详情 -->
        <h3>患者详情</h3>
        <p>患者姓名：{{ selectedPatient.patientName }}</p>
        <p>档案号：{{ selectedPatient.archiveNo }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SchemeCenter',
  data() {
    return {
      activeTab: 'ungrouped',
      dateRange: null,
      searchKeyword: '',
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      selectedPatient: null
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    handleTabChange(tab) {
      this.activeTab = tab
      this.currentPage = 1
      this.selectedPatient = null
      this.fetchData()
    },
    handleSearch() {
      this.currentPage = 1
      this.fetchData()
    },
    handleReset() {
      this.dateRange = null
      this.searchKeyword = ''
      this.currentPage = 1
      this.fetchData()
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      this.fetchData()
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchData()
    },
    handleRowClick(row) {
      this.selectedPatient = row
    },
    fetchData() {
      // 模拟数据，实际应该调用后端API
      this.loading = true
      setTimeout(() => {
        this.tableData = []
        this.total = 0
        this.loading = false
      }, 300)
    }
  }
}
</script>

<style lang="scss" scoped>
.scheme-center-container {
  display: flex;
  height: calc(100vh - 84px);
  background: #f0f2f5;
  padding: 20px;
  gap: 20px;
}

// 左侧患者列表卡片 - 占三分之一宽度
.patient-list-card {
  width: 33.33%;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

// 顶部标签页
.tabs-header {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.tab-item {
  flex: 1;
  padding: 14px 0;
  text-align: center;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  background: #fff;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
  position: relative;

  &:hover {
    color: #409eff;
  }

  &.active {
    color: #409eff;
    border-bottom-color: #409eff;
    font-weight: 500;
  }
}

// 筛选区域
.filter-section {
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.filter-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;

  label {
    margin-right: 8px;
    font-size: 13px;
    color: #606266;
    white-space: nowrap;
    min-width: 70px;
  }

  .date-picker {
    flex: 1;
  }

  .search-input {
    flex: 1;
  }
}

.filter-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;

  .el-button {
    flex: 1;
  }
}

// 表格区域
.table-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 16px;
}

.table-wrapper {
  flex: 1;
  overflow-x: auto;
  overflow-y: auto;

  // 自定义滚动条样式
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 3px;

    &:hover {
      background: #b4b7bd;
    }
  }

  &::-webkit-scrollbar-track {
    background: #f5f7fa;
  }
}

.patient-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;

  .gender-icon {
    font-size: 13px;
    font-weight: bold;

    &.male {
      color: #409eff;
    }

    &.female {
      color: #f56c6c;
    }
  }

  .age {
    color: #909399;
    font-size: 12px;
  }
}

// 分页
.pagination-wrapper {
  margin-top: 12px;
  display: flex;
  justify-content: center;
  padding-top: 12px;
  border-top: 1px solid #e4e7ed;
  flex-shrink: 0;
}

// 空状态
.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #909399;
  font-size: 13px;
}

// 右侧详情区域 - 占三分之二宽度
.detail-panel {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-detail {
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.patient-detail {
  width: 100%;

  h3 {
    margin-bottom: 20px;
    color: #303133;
  }

  p {
    margin-bottom: 10px;
    color: #606266;
  }
}

// Element UI 样式覆盖
::v-deep .el-table {
  font-size: 12px;

  th {
    font-weight: 500;
    padding: 8px 0;
  }

  td {
    padding: 8px 0;
  }

  .el-table__header-wrapper {
    th {
      background: #f5f7fa !important;
    }
  }

  .el-table__row {
    cursor: pointer;

    &:hover {
      background-color: #f5f7fa;
    }
  }
}

::v-deep .el-button--small {
  padding: 8px 12px;
  font-size: 12px;
}

::v-deep .el-input--small .el-input__inner {
  height: 32px;
  line-height: 32px;
  font-size: 12px;
}

::v-deep .el-date-editor {
  width: 100% !important;

  .el-range-separator {
    width: 30px;
    padding: 0 5px;
    line-height: 26px;
  }

  .el-range-input {
    font-size: 12px;
  }
}

::v-deep .el-pagination {
  font-size: 12px;

  .el-pagination__total,
  .el-pagination__jump {
    font-size: 12px;
  }
}
</style>
