<template>
  <div class="video-library-container">
    <el-card>
      <div slot="header">
        <span>视频素材库</span>
      </div>

      <!-- 搜索筛选区域 -->
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
          <el-form-item label="视频标题" prop="videoTitle">
            <el-input
              v-model="searchForm.videoTitle"
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
              <el-option label="康复" value="康复" />
              <el-option label="术后护理" value="术后护理" />
              <el-option label="营养指导" value="营养指导" />
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

      <!-- 操作按钮区域 -->
      <div class="toolbar">
        <el-button
          type="primary"
          @click="handleAdd"
          icon="el-icon-plus"
          size="small"
        >
          添加内容
        </el-button>
      </div>

      <!-- 视频素材列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="videoTitle" label="视频标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="videoId" label="视频ID" width="150" show-overflow-tooltip />
        <el-table-column prop="videoDescription" label="视频描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="categorySection" label="所属板块" width="120" align="center" />
        <el-table-column prop="viewCount" label="阅读量" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ formatNumber(scope.row.viewCount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞量" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ formatNumber(scope.row.likeCount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="100" align="center" />
        <el-table-column prop="publishTime" label="发布时间" width="160" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.publishTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="contentStatus" label="内容状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.contentStatus)"
              size="small"
            >
              {{ scope.row.contentStatus }}
            </el-tag>
          </template>
        </el-table-column>
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

    <!-- 详情对话框 -->
    <el-dialog title="视频素材详情" :visible.sync="detailDialogVisible" width="70%">
      <div v-if="currentVideo" class="video-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="视频标题" :span="2">{{ currentVideo.videoTitle }}</el-descriptions-item>
          <el-descriptions-item label="视频ID">{{ currentVideo.videoId }}</el-descriptions-item>
          <el-descriptions-item label="所属板块">{{ currentVideo.categorySection }}</el-descriptions-item>
          <el-descriptions-item label="作者">{{ currentVideo.author }}</el-descriptions-item>
          <el-descriptions-item label="内容状态">
            <el-tag :type="getStatusType(currentVideo.contentStatus)">
              {{ currentVideo.contentStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="阅读量">{{ formatNumber(currentVideo.viewCount) }}</el-descriptions-item>
          <el-descriptions-item label="点赞量">{{ formatNumber(currentVideo.likeCount) }}</el-descriptions-item>
          <el-descriptions-item label="发布时间">{{ formatDateTime(currentVideo.publishTime) }}</el-descriptions-item>
          <el-descriptions-item label="视频描述" :span="2">
            <div class="description-content">{{ currentVideo.videoDescription || '暂无描述' }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog title="视频预览" :visible.sync="previewDialogVisible" width="80%">
      <div v-if="currentVideo" class="video-preview">
        <div class="preview-header">
          <h2>{{ currentVideo.videoTitle }}</h2>
          <div class="preview-meta">
            <span class="meta-item">作者：{{ currentVideo.author }}</span>
            <span class="meta-item">所属板块：{{ currentVideo.categorySection }}</span>
            <span class="meta-item">发布时间：{{ formatDateTime(currentVideo.publishTime) }}</span>
          </div>
        </div>
        <div class="preview-content">
          <div class="video-player">
            <i class="el-icon-video-camera" style="font-size: 64px; color: #909399;"></i>
            <p>视频播放区域</p>
          </div>
          <div class="video-description">
            <p>{{ currentVideo.videoDescription || '暂无视频描述' }}</p>
          </div>
        </div>
        <div class="preview-stats">
          <span class="stat-item">阅读量：{{ formatNumber(currentVideo.viewCount) }}</span>
          <span class="stat-item">点赞量：{{ formatNumber(currentVideo.likeCount) }}</span>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getVideoAssetList } from '@/api/education'

export default {
  name: 'VideoLibrary',
  data() {
    return {
      // 搜索表单
      searchForm: {
        publishDateRange: null,
        videoTitle: '',
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
      detailDialogVisible: false,
      previewDialogVisible: false,
      // 当前操作的视频
      currentVideo: null
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
          videoTitle: this.searchForm.videoTitle,
          author: this.searchForm.author,
          categorySection: this.searchForm.categorySection,
          contentStatus: this.searchForm.contentStatus,
          publishStartTime: this.searchForm.publishDateRange ? this.searchForm.publishDateRange[0] : null,
          publishEndTime: this.searchForm.publishDateRange ? this.searchForm.publishDateRange[1] : null
        }
        const response = await getVideoAssetList(params)
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
      this.$router.push('/education/video-add')
    },
    // 编辑
    handleEdit(row) {
      this.$router.push({
        path: '/education/video-add',
        query: { mode: 'edit', id: row.id }
      })
    },
    // 复制
    handleCopy(row) {
      this.$router.push({
        path: '/education/video-add',
        query: { mode: 'copy', id: row.id }
      })
    },
    // 预览
    handlePreview(row) {
      this.$router.push({
        path: '/education/video-add',
        query: { mode: 'preview', id: row.id }
      })
    },
    // 详情
    handleDetail(row) {
      this.$router.push({
        path: '/education/video-add',
        query: { mode: 'detail', id: row.id }
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
        '未上架': 'info',
        '已下架': 'danger'
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
.video-library-container {
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

  .video-detail {
    padding: 20px 0;

    .description-content {
      line-height: 1.6;
      white-space: pre-wrap;
      word-break: break-word;
    }
  }

  .video-preview {
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

      .video-player {
        margin-bottom: 20px;
        padding: 60px;
        border: 2px dashed #e0e0e0;
        border-radius: 8px;
        background-color: #fafafa;
        text-align: center;

        i {
          display: block;
          margin-bottom: 10px;
        }

        p {
          margin: 0;
          color: #909399;
          font-size: 16px;
        }
      }

      .video-description {
        line-height: 1.8;
        color: #606266;
      }
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
