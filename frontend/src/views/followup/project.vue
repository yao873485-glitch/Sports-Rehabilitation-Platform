<template>
  <div class="followup-project-container" :class="{ 'fullscreen-mode': isFullscreen }">
    <el-card>
      <div slot="header">
        <span>随访项目管理</span>
      </div>

      <!-- 搜索筛选区域 -->
      <div v-show="!isFullscreen" class="search-form">
        <el-form :inline="true" :model="searchForm" ref="searchForm" size="small">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="searchForm.projectName"
              placeholder="请输入项目名称"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          <el-form-item label="状态" prop="isPublished">
            <el-select
              v-model="searchForm.isPublished"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="全部" value="" />
              <el-option label="已发布" :value="1" />
              <el-option label="未发布" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间" prop="dateRange">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" icon="el-icon-search">
              搜索
            </el-button>
            <el-button @click="handleReset" icon="el-icon-refresh">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="toolbar">
        <div class="toolbar-right">
          <el-button
            type="primary"
            @click="handleAdd"
            icon="el-icon-circle-plus-outline"
          >
            新增
          </el-button>
          <table-toolbar
            :columns="tableColumns"
            :density="tableDensity"
            :fullscreen="isFullscreen"
            @refresh="handleRefresh"
            @density-change="handleDensityChange"
            @column-change="handleColumnChange"
            @fullscreen-change="handleFullscreenChange"
          />
        </div>
      </div>

      <!-- 项目列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
        :size="tableDensity"
      >
        <el-table-column v-if="getColumnVisible('projectCode')" prop="projectCode" label="项目编号" width="180" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('projectName')" prop="projectName" label="项目名称" min-width="150" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('projectDescription')" prop="projectDescription" label="项目介绍" min-width="200" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('linkedFollowupPlan')" prop="linkedFollowupPlan" label="绑定随访计划" width="150" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('lastModifiedTime')" prop="lastModifiedTime" label="最后修改时间" width="160" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.lastModifiedTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisible('createdTime')" prop="createdTime" label="创建时间" width="160" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisible('operator')" prop="operator" label="操作人" width="100" align="center" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('isPublished')" prop="isPublished" label="是否发布" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.isPublished ? 'success' : 'info'"
              size="small"
            >
              {{ scope.row.isPublished ? '已发布' : '未发布' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisible('actions')" label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleViewDetail(scope.row)"
              icon="el-icon-view"
            >
              查看详情
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
              icon="el-icon-edit"
            >
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页导航 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background
        >
          <span class="pagination-info">
            当前第 {{ (pagination.currentPage - 1) * pagination.pageSize + 1 }}-{{
              Math.min(pagination.currentPage * pagination.pageSize, pagination.total)
            }} 条，共 {{ pagination.total }} 条数据
          </span>
        </el-pagination>
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog title="项目详情" :visible.sync="detailDialogVisible" width="70%">
      <div v-if="currentProject" class="project-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="项目编号">{{ currentProject.projectCode }}</el-descriptions-item>
          <el-descriptions-item label="项目名称">{{ currentProject.projectName }}</el-descriptions-item>
          <el-descriptions-item label="绑定随访计划">{{ currentProject.linkedFollowupPlan }}</el-descriptions-item>
          <el-descriptions-item label="操作人">{{ currentProject.operator }}</el-descriptions-item>
          <el-descriptions-item label="是否发布">
            <el-tag :type="currentProject.isPublished ? 'success' : 'info'">
              {{ currentProject.isPublished ? '已发布' : '未发布' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(currentProject.createdTime) }}</el-descriptions-item>
          <el-descriptions-item label="最后修改时间">{{ formatDateTime(currentProject.lastModifiedTime) }}</el-descriptions-item>
          <el-descriptions-item label="项目介绍" :span="2">
            <div class="description-content">{{ currentProject.projectDescription || '暂无介绍' }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFollowupProjectConfigList } from '@/api/followup'
import TableToolbar from '@/components/TableToolbar'

export default {
  name: 'FollowupProject',
  components: {
    TableToolbar
  },
  data() {
    return {
      // 搜索表单
      searchForm: {
        projectName: '',
        isPublished: '',
        dateRange: []
      },
      // 表格数据
      tableData: [],
      loading: false,
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      // 对话框显示状态
      detailDialogVisible: false,
      // 当前操作的项目
      currentProject: null,
      tableDensity: 'default',
      isFullscreen: false,
      tableColumns: [
        { prop: 'projectCode', label: '项目编号', visible: true },
        { prop: 'projectName', label: '项目名称', visible: true },
        { prop: 'projectDescription', label: '项目介绍', visible: true },
        { prop: 'linkedFollowupPlan', label: '绑定随访计划', visible: true },
        { prop: 'lastModifiedTime', label: '最后修改时间', visible: true },
        { prop: 'createdTime', label: '创建时间', visible: true },
        { prop: 'operator', label: '操作人', visible: true },
        { prop: 'isPublished', label: '是否发布', visible: true },
        { prop: 'actions', label: '操作', visible: true }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    getColumnVisible(prop) {
      const column = this.tableColumns.find(col => col.prop === prop)
      return column ? column.visible : true
    },
    handleRefresh() {
      this.fetchData()
    },
    handleDensityChange(density) {
      this.tableDensity = density
    },
    handleColumnChange() {
      // columns 已在组件内更新
    },
    handleFullscreenChange(fullscreen) {
      this.isFullscreen = fullscreen
    },
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          projectName: this.searchForm.projectName,
          isPublished: this.searchForm.isPublished,
          startDate: this.searchForm.dateRange ? this.searchForm.dateRange[0] : null,
          endDate: this.searchForm.dateRange ? this.searchForm.dateRange[1] : null
        }

        // 使用新的项目配置API
        const response = await getFollowupProjectConfigList(params)

        // 后端返回 Result.success(page)
        // 拦截器返回整个 res 对象: { code: 200, message: "...", data: page }
        // page 对象包含 records 和 total 字段
        const pageData = response.data || { records: [], total: 0 }

        // 转换数据格式以匹配表格显示
        this.tableData = (pageData.records || []).map(item => ({
          id: item.id,
          projectCode: item.projectCode || item.project_code,
          projectName: item.projectName || item.project_name,
          projectDescription: item.projectDescription || item.project_description,
          linkedFollowupPlan: item.bindPlan || item.bind_plan || '-',
          lastModifiedTime: item.updatedTime || item.updated_time,
          createdTime: item.createdTime || item.created_time,
          operator: item.operator,
          isPublished: item.isPublished !== undefined ? item.isPublished : item.is_published
        }))
        this.pagination.total = pageData.total || 0
      } catch (error) {
        this.$message.error('获取数据失败')
        console.error('获取数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 刷新列表
    getList() {
      this.fetchData()
    },
    // 新增项目
    handleAdd() {
      this.$router.push('/followup/project/add')
    },
    // 查看详情
    handleViewDetail(row) {
      this.currentProject = { ...row }
      this.detailDialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.$router.push(`/followup/project/edit/${row.id}`)
    },
    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchData()
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.followup-project-container {
  padding: 20px;

  &.fullscreen-mode {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 9999;
    background-color: #fff;
    padding: 0;
    margin: 0;

    ::v-deep .el-card {
      height: 100%;
      display: flex;
      flex-direction: column;
      border: none;

      .el-card__header {
        flex-shrink: 0;
        border-bottom: none;
      }

      .el-card__body {
        flex: 1;
        overflow: auto;
        display: flex;
        flex-direction: column;
        padding: 0;
      }

      .toolbar {
        flex-shrink: 0;
        border-bottom: 1px solid #ebeef5;
        padding: 12px 20px;
      }

      .el-table {
        flex: 1;
      }

      .pagination-container {
        flex-shrink: 0;
        padding: 10px 20px;
        border-top: 1px solid #ebeef5;
      }
    }
  }

  .search-form {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #f5f5f5;
    border-radius: 4px;
  }

  .toolbar {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
    align-items: center;

    .toolbar-right {
      display: flex;
      align-items: center;
      gap: 8px;

      .el-button.el-button--primary {
        color: #fff;
        border-color: #409eff;
        background-color: #409eff;
      }

      .el-button.is-circle {
        color: #606266;
        border-color: #dcdfe6;

        &:hover {
          color: #409eff;
          border-color: #409eff;
        }
      }
    }
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;

    .pagination-info {
      margin-right: 20px;
      color: #606266;
      font-size: 14px;
    }
  }

  .project-detail {
    padding: 20px 0;

    .description-content {
      line-height: 1.6;
      white-space: pre-wrap;
      word-break: break-word;
    }
  }
}

::v-deep .el-table {
  .el-table__header-wrapper {
    th {
      background-color: #f8f9fa;
      color: #333;
      font-weight: 600;
    }
  }
}

// 日期选择器分隔符样式
::v-deep .el-date-editor {
  .el-range-separator {
    padding: 0 8px;
    width: auto;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
  }

  .el-range-input {
    width: 42%;
  }
}
</style>
