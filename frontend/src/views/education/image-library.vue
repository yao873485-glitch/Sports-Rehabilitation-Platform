<template>
  <div class="image-library-container" :class="{ 'fullscreen-mode': isFullscreen }">
    <el-card>
      <div slot="header" class="card-header">
        <span>图文素材库</span>
      </div>

      <!-- 搜索筛选区域 -->
      <div v-show="!isFullscreen" class="search-form-wrapper">
      <div class="search-form">
        <el-form :inline="true" :model="searchForm" ref="searchForm" size="small">
          <el-form-item label="发布日期" prop="publishDateRange">
            <el-date-picker
              v-model="searchForm.publishDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item label="内容标题" prop="contentTitle">
            <el-input
              v-model="searchForm.contentTitle"
              placeholder="请输入内容标题"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          <el-form-item label="作者" prop="author">
            <el-input
              v-model="searchForm.author"
              placeholder="请输入作者"
              clearable
              style="width: 150px;"
            />
          </el-form-item>
          <el-form-item label="所属板块" prop="categorySection">
            <el-select
              v-model="searchForm.categorySection"
              placeholder="请选择"
              clearable
              style="width: 150px;"
            >
              <el-option label="医教" value="医教" />
              <el-option label="科普" value="科普" />
              <el-option label="康复指导" value="康复指导" />
              <el-option label="健康资讯" value="健康资讯" />
              <el-option label="疾病预防" value="疾病预防" />
            </el-select>
          </el-form-item>
          <el-form-item label="内容状态" prop="contentStatus">
            <el-select
              v-model="searchForm.contentStatus"
              placeholder="请选择"
              clearable
              style="width: 120px;"
            >
              <el-option label="草稿" value="草稿" />
              <el-option label="待审核" value="待审核" />
              <el-option label="已上架" value="已上架" />
              <el-option label="未上架" value="未上架" />
              <el-option label="已下架" value="已下架" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" icon="el-icon-search">
              查询
            </el-button>
            <el-button @click="handleReset" icon="el-icon-refresh">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      </div>

      <!-- 工具栏 -->
      <div class="toolbar-section">
        <div class="toolbar-right">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">
            添加内容
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

      <!-- 图文素材列表 -->
      <el-table
        ref="tableRef"
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
        stripe
        border
        :size="tableDensity"
      >
        <el-table-column v-if="getColumnVisible('contentTitle')" prop="contentTitle" label="内容标题" min-width="200" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('contentId')" prop="contentId" label="内容ID" width="150" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('articleDescription')" prop="articleDescription" label="文章描述" min-width="250" show-overflow-tooltip />
        <el-table-column v-if="getColumnVisible('categorySection')" prop="categorySection" label="所属板块" width="120" align="center" />
        <el-table-column v-if="getColumnVisible('viewCount')" prop="viewCount" label="阅读量" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ formatNumber(scope.row.viewCount) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisible('likeCount')" prop="likeCount" label="点赞量" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ formatNumber(scope.row.likeCount) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisible('author')" prop="author" label="作者" width="100" align="center" />
        <el-table-column v-if="getColumnVisible('publishTime')" prop="publishTime" label="发布时间" width="160" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.publishTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisible('contentStatus')" prop="contentStatus" label="内容状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.contentStatus)"
              size="small"
            >
              {{ scope.row.contentStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisible('articleLink')" prop="articleLink" label="文章链接" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
              icon="el-icon-edit"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleCopy(scope.row)"
              icon="el-icon-copy-document"
            >
              复制
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handlePreview(scope.row)"
              icon="el-icon-view"
            >
              预览
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDetail(scope.row)"
              icon="el-icon-document"
            >
              详情
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

    <!-- 预览对话框 -->
    <el-dialog title="内容预览" :visible.sync="previewDialogVisible" width="80%">
      <div v-if="currentContent" class="content-preview">
        <div class="preview-header">
          <h2>{{ currentContent.contentTitle }}</h2>
          <div class="preview-meta">
            <span class="meta-item">作者：{{ currentContent.author }}</span>
            <span class="meta-item">所属板块：{{ currentContent.categorySection }}</span>
            <span class="meta-item">发布时间：{{ formatDateTime(currentContent.publishTime) }}</span>
          </div>
        </div>
        <div class="preview-content">
          <p>{{ currentContent.articleDescription || '暂无内容描述' }}</p>
        </div>
        <div class="preview-stats">
          <span class="stat-item">阅读量：{{ formatNumber(currentContent.viewCount) }}</span>
          <span class="stat-item">点赞量：{{ formatNumber(currentContent.likeCount) }}</span>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getContentList } from '@/api/education'
import TableToolbar from '@/components/TableToolbar'

export default {
  name: 'ImageLibrary',
  components: {
    TableToolbar
  },
  data() {
    return {
      // 搜索表单
      searchForm: {
        publishDateRange: null,
        contentTitle: '',
        author: '',
        categorySection: '',
        contentStatus: ''
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
      previewDialogVisible: false,
      // 当前操作的内容
      currentContent: null,
      // 表格工具栏
      tableDensity: 'default',
      isFullscreen: false,
      tableColumns: [
        { prop: 'contentTitle', label: '内容标题', visible: true },
        { prop: 'contentId', label: '内容ID', visible: true },
        { prop: 'articleDescription', label: '文章描述', visible: true },
        { prop: 'categorySection', label: '所属板块', visible: true },
        { prop: 'viewCount', label: '阅读量', visible: true },
        { prop: 'likeCount', label: '点赞量', visible: true },
        { prop: 'author', label: '作者', visible: true },
        { prop: 'publishTime', label: '发布时间', visible: true },
        { prop: 'contentStatus', label: '内容状态', visible: true },
        { prop: 'articleLink', label: '文章链接', visible: true }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取列的可见性
    getColumnVisible(prop) {
      const column = this.tableColumns.find(col => col.prop === prop)
      return column ? column.visible : true
    },
    // 刷新
    handleRefresh() {
      this.fetchData()
      this.$message.success('刷新成功')
    },
    // 密度变化
    handleDensityChange(density) {
      this.tableDensity = density
    },
    // 列显示变化
    handleColumnChange() {
      // columns 已经被修改了，不需要额外处理
    },
    // 全屏切换
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
          contentTitle: this.searchForm.contentTitle,
          author: this.searchForm.author,
          categorySection: this.searchForm.categorySection,
          contentStatus: this.searchForm.contentStatus,
          publishStartTime: this.searchForm.publishDateRange ? this.searchForm.publishDateRange[0] : null,
          publishEndTime: this.searchForm.publishDateRange ? this.searchForm.publishDateRange[1] : null
        }
        const response = await getContentList(params)
        this.tableData = response.data.records
        this.pagination.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败')
        console.error('获取数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 查询
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
    // 添加内容
    handleAdd() {
      this.$router.push('/education/image-add')
    },
    // 编辑
    handleEdit(row) {
      this.$router.push({
        path: '/education/image-add',
        query: {
          id: row.id,
          mode: 'edit'
        }
      })
    },
    // 复制
    handleCopy(row) {
      this.$router.push({
        path: '/education/image-add',
        query: {
          id: row.id,
          mode: 'copy'
        }
      })
    },
    // 预览
    handlePreview(row) {
      this.currentContent = { ...row }
      this.previewDialogVisible = true
    },
    // 详情
    handleDetail(row) {
      this.$router.push({
        path: '/education/image-detail',
        query: {
          id: row.id
        }
      })
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
        '草稿': 'info',
        '待审核': 'warning',
        '已上架': 'success',
        '未上架': 'danger',
        '已下架': 'info'
      }
      return statusMap[status] || 'info'
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    },
    // 格式化数字
    formatNumber(num) {
      if (!num) return '0'
      return num.toString()
    }
  }
}
</script>

<style lang="scss" scoped>
.image-library-container {
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

      .toolbar-section {
        flex-shrink: 0;
        border-bottom: 1px solid #ebeef5;
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

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    ::v-deep .table-toolbar {
      display: flex;
      gap: 8px;
    }
  }

  .search-form-wrapper {
    margin-bottom: 0;
  }

  .search-form {
    padding: 20px;
    background-color: #f5f5f5;
    border-radius: 4px;
  }

  .toolbar-section {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 16px 20px;
    background-color: #fff;
    border-bottom: 1px solid #ebeef5;

    .toolbar-right {
      display: flex;
      align-items: center;
      gap: 12px;
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

  .content-preview {
    .preview-header {
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 20px;
      margin-bottom: 20px;

      h2 {
        margin: 0 0 15px 0;
        color: #303133;
      }

      .preview-meta {
        .meta-item {
          margin-right: 20px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .preview-content {
      margin-bottom: 20px;
      line-height: 1.8;
      color: #606266;
    }

    .preview-stats {
      border-top: 1px solid #ebeef5;
      padding-top: 15px;

      .stat-item {
        margin-right: 20px;
        color: #909399;
        font-size: 14px;
      }
    }
  }

  // 添加内容对话框样式
  .char-count {
    text-align: right;
    color: #909399;
    font-size: 12px;
    margin-top: 5px;
  }

  .upload-container {
    .cover-uploader {
      .upload-box {
        width: 178px;
        height: 178px;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          border-color: #409eff;
        }

        i {
          font-size: 28px;
          color: #8c939d;
        }
      }

      .cover-image {
        width: 178px;
        height: 178px;
        display: block;
        object-fit: cover;
      }
    }

    .upload-tips {
      color: #909399;
      font-size: 12px;
      margin-top: 8px;
      line-height: 1.5;
    }
  }

  .editor-container {
    width: 100%;

    .editor-toolbar {
      border: 1px solid #dcdfe6;
      border-bottom: none;
      padding: 10px;
      background-color: #f5f7fa;
      display: flex;
      align-items: center;
      gap: 10px;

      .paragraph-select {
        padding: 5px 10px;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        background-color: white;
        cursor: pointer;
        font-size: 14px;

        &:focus {
          outline: none;
          border-color: #409eff;
        }
      }
    }

    ::v-deep .el-textarea {
      .el-textarea__inner {
        border-top-left-radius: 0;
        border-top-right-radius: 0;
      }
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

// 对话框样式优化
::v-deep .el-dialog__body {
  max-height: 600px;
  overflow-y: auto;
}
</style>