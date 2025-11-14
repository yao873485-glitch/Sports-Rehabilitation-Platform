<template>
  <div class="followup-plan-container">
    <el-card>
      <div slot="header">
        <span>随访计划管理</span>
      </div>

      <!-- 搜索筛选区域 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm" ref="searchForm" size="small">
          <el-form-item label="计划名称" prop="planName">
            <el-input
              v-model="searchForm.planName"
              placeholder="请输入计划名称"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="全部" value="" />
              <el-option label="未发布" value="未发布" />
              <el-option label="已发布" value="已发布" />
              <el-option label="已停用" value="已停用" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" icon="el-icon-search">
              搜索
            </el-button>
            <el-button @click="handleReset" icon="el-icon-refresh">
              清空
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="toolbar">
        <el-button
          type="primary"
          @click="handleAdd"
          icon="el-icon-plus"
          size="small"
        >
          新增计划
        </el-button>
      </div>

      <!-- 计划列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="planName" label="计划名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="linkedProject" label="随访项目" width="150" show-overflow-tooltip />
        <el-table-column prop="versionNumber" label="当前版本号" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.status)"
              size="small"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastModifiedTime" label="最近一次修改时间" width="180" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.lastModifiedTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="180" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="versionRemark" label="版本备注" min-width="120" show-overflow-tooltip />
        <el-table-column prop="invitationQrCodeUrl" label="随访邀请二维码" width="140" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.invitationQrCodeUrl" class="qr-code-container">
              <el-image
                :src="scope.row.invitationQrCodeUrl"
                :preview-src-list="[scope.row.invitationQrCodeUrl]"
                fit="cover"
                style="width: 40px; height: 40px; cursor: pointer;"
                :z-index="9999"
              >
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <el-button
                type="text"
                size="mini"
                @click="handleDownloadQrCode(scope.row)"
                style="display: block; margin-top: 4px;"
              >
                下载
              </el-button>
            </div>
            <span v-else class="no-qr-code">暂无二维码</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
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
              @click="handleCopyTemplate(scope.row)"
              icon="el-icon-copy-document"
            >
              复制模板
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDelete(scope.row)"
              icon="el-icon-delete"
              style="color: #f56c6c;"
            >
              删除
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
    <el-dialog title="计划详情" :visible.sync="detailDialogVisible" width="70%">
      <div v-if="currentPlan" class="plan-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="计划名称">{{ currentPlan.planName }}</el-descriptions-item>
          <el-descriptions-item label="随访项目">{{ currentPlan.linkedProject }}</el-descriptions-item>
          <el-descriptions-item label="当前版本号">{{ currentPlan.versionNumber }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentPlan.status)">
              {{ currentPlan.status }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(currentPlan.createdTime) }}</el-descriptions-item>
          <el-descriptions-item label="最近修改时间">{{ formatDateTime(currentPlan.lastModifiedTime) }}</el-descriptions-item>
          <el-descriptions-item label="版本备注" :span="2">
            <div class="remark-content">{{ currentPlan.versionRemark || '暂无备注' }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="随访邀请二维码" :span="2">
            <div v-if="currentPlan.invitationQrCodeUrl" class="qr-code-detail">
              <el-image
                :src="currentPlan.invitationQrCodeUrl"
                :preview-src-list="[currentPlan.invitationQrCodeUrl]"
                fit="cover"
                style="width: 100px; height: 100px;"
              >
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <el-button
                type="primary"
                size="small"
                @click="handleDownloadQrCode(currentPlan)"
                style="margin-left: 20px;"
              >
                下载二维码
              </el-button>
            </div>
            <span v-else>暂无二维码</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      title="删除确认"
      :visible.sync="deleteDialogVisible"
      width="30%"
      center
    >
      <div style="text-align: center; padding: 20px 0;">
        <i class="el-icon-warning" style="font-size: 48px; color: #e6a23c; margin-bottom: 20px;"></i>
        <p style="font-size: 16px; margin-bottom: 10px;">确定要删除该随访计划吗？</p>
        <p style="color: #909399; font-size: 14px;">删除后无法恢复，请谨慎操作</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定删除</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFollowupPlanList, deleteFollowupPlan } from '@/api/followup'

export default {
  name: 'FollowupPlan',
  data() {
    return {
      // 搜索表单
      searchForm: {
        planName: '',
        status: ''
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
      deleteDialogVisible: false,
      // 当前操作的计划
      currentPlan: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          planName: this.searchForm.planName,
          status: this.searchForm.status
        }
        const response = await getFollowupPlanList(params)
        this.tableData = response.data.records
        this.pagination.total = response.data.total
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
    // 清空
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 新增计划
    handleAdd() {
      this.$message.info('新增计划功能开发中')
    },
    // 查看详情
    handleViewDetail(row) {
      this.currentPlan = { ...row }
      this.detailDialogVisible = true
    },
    // 复制模板
    handleCopyTemplate(row) {
      this.$message.info('复制模板功能开发中')
    },
    // 删除
    handleDelete(row) {
      this.currentPlan = { ...row }
      this.deleteDialogVisible = true
    },
    // 确认删除
    async confirmDelete() {
      try {
        await deleteFollowupPlan(this.currentPlan.id)
        this.$message.success('删除成功')
        this.deleteDialogVisible = false
        this.fetchData()
      } catch (error) {
        this.$message.error('删除失败')
        console.error('删除失败:', error)
      }
    },
    // 下载二维码
    handleDownloadQrCode(row) {
      if (row.invitationQrCodeUrl) {
        const link = document.createElement('a')
        link.href = row.invitationQrCodeUrl
        link.download = `随访计划二维码-${row.planName}.png`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      }
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
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '未发布': 'info',
        '已发布': 'success',
        '已停用': 'warning'
      }
      return statusMap[status] || 'info'
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
.followup-plan-container {
  padding: 20px;

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

  .qr-code-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .no-qr-code {
    color: #909399;
    font-size: 12px;
  }

  .plan-detail {
    padding: 20px 0;

    .remark-content {
      line-height: 1.6;
      white-space: pre-wrap;
      word-break: break-word;
    }

    .qr-code-detail {
      display: flex;
      align-items: center;
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

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 20px;
}
</style>