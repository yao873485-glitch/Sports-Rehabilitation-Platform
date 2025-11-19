<template>
  <div class="content-management-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>内容应用管理</span>
        <div class="header-actions">
          <el-button
            size="small"
            icon="el-icon-refresh"
            @click="handleRefresh"
            circle
            title="刷新"
          />
          <el-button
            size="small"
            icon="el-icon-download"
            @click="handleExport"
            circle
            title="导出"
          />
          <el-button
            size="small"
            icon="el-icon-setting"
            @click="handleSettings"
            circle
            title="设置"
          />
        </div>
      </div>

      <!-- 搜索筛选区域 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm" ref="searchForm" size="small">
          <el-form-item label="上架时间" prop="publishTimeRange">
            <el-date-picker
              v-model="searchForm.publishTimeRange"
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
          <el-form-item label="资源类型" prop="resourceType">
            <el-select
              v-model="searchForm.resourceType"
              placeholder="请选择"
              clearable
              style="width: 120px;"
            >
              <el-option label="文章" value="文章" />
              <el-option label="视频" value="视频" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属版块" prop="categorySection">
            <el-select
              v-model="searchForm.categorySection"
              placeholder="请选择"
              clearable
              style="width: 120px;"
            >
              <el-option label="医教" value="医教" />
              <el-option label="患教" value="患教" />
            </el-select>
          </el-form-item>
          <el-form-item label="上下架状态" prop="status">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择"
              clearable
              style="width: 120px;"
            >
              <el-option label="已上架" value="已上架" />
              <el-option label="未上架" value="未上架" />
            </el-select>
          </el-form-item>
          <el-form-item label="置顶状态" prop="pinStatus">
            <el-select
              v-model="searchForm.pinStatus"
              placeholder="请选择"
              clearable
              style="width: 120px;"
            >
              <el-option label="已置顶" value="已置顶" />
              <el-option label="未置顶" value="未置顶" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属频道" prop="channel">
            <el-select
              v-model="searchForm.channel"
              placeholder="请选择"
              clearable
              style="width: 150px;"
            >
              <el-option label="儿童生长发育" value="儿童生长发育" />
              <el-option label="育儿健康" value="育儿健康" />
              <el-option label="随访" value="随访" />
              <el-option label="健康小知识" value="健康小知识" />
              <el-option label="男性不孕症" value="男性不孕症" />
              <el-option label="孕产健康" value="孕产健康" />
              <el-option label="护理知识" value="护理知识" />
              <el-option label="性功能障碍" value="性功能障碍" />
              <el-option label="精子冷冻" value="精子冷冻" />
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

      <!-- 内容管理列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
        stripe
        border
      >
        <el-table-column prop="contentTitle" label="内容标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="contentId" label="内容ID" width="150" show-overflow-tooltip />
        <el-table-column prop="contentType" label="资源类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.contentType === '视频' ? 'success' : 'primary'" size="small">
              {{ scope.row.contentType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contentDescription" label="内容描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="categorySection" label="所属版块" width="100" align="center" />
        <el-table-column prop="applicationModule" label="应用模块" width="120" show-overflow-tooltip />
        <el-table-column prop="applicationProject" label="应用项目" width="120" show-overflow-tooltip />
        <el-table-column prop="channel" label="所属频道" width="120" show-overflow-tooltip />
        <el-table-column prop="relatedDisease" label="关联疾病类型" width="120" show-overflow-tooltip />
        <el-table-column prop="relatedSymptom" label="关联症状" width="120" show-overflow-tooltip />
        <el-table-column prop="relatedProduct" label="关联商品" width="120" show-overflow-tooltip />
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
        <el-table-column prop="publishTime" label="上架时间" width="160" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.publishTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="contentStatus" label="上下架状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.contentStatus === '已上架' ? 'success' : 'info'" size="small">
              {{ scope.row.contentStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pinStatus" label="置顶状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.pinStatus === 1 ? 'warning' : ''" size="small">
              {{ scope.row.pinStatus === 1 ? '已置顶' : '未置顶' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="articleUrl" label="文章链接" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link v-if="scope.row.articleUrl" :href="scope.row.articleUrl" target="_blank" type="primary">
              查看链接
            </el-link>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handlePreview(scope.row)"
            >
              预览
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDetail(scope.row)"
            >
              详情
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleConfig(scope.row)"
            >
              配置
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
    <el-dialog :title="previewTitle" :visible.sync="previewDialogVisible" width="80%" top="5vh">
      <div v-if="previewContent" class="preview-content">
        <!-- 视频预览 -->
        <div v-if="previewContent.contentType === '视频'" class="video-preview">
          <div class="preview-header">
            <h2>{{ previewContent.videoTitle }}</h2>
            <div class="preview-meta">
              <span>作者: {{ previewContent.author }}</span>
              <span>发布时间: {{ formatDateTime(previewContent.publishTime) }}</span>
              <span>阅读量: {{ formatNumber(previewContent.viewCount) }}</span>
              <span>点赞量: {{ formatNumber(previewContent.likeCount) }}</span>
            </div>
          </div>
          <div class="preview-video">
            <video v-if="previewContent.videoUrl" :src="previewContent.videoUrl" controls style="width: 100%; max-height: 500px;"></video>
            <div v-else class="no-video">暂无视频</div>
          </div>
          <div class="preview-description">
            <h3>视频描述</h3>
            <p>{{ previewContent.videoDescription || '暂无描述' }}</p>
          </div>
        </div>

        <!-- 图文预览 -->
        <div v-else-if="previewContent.contentType === '文章'" class="article-preview">
          <div class="preview-header">
            <h2>{{ previewContent.contentTitle }}</h2>
            <div class="preview-meta">
              <span>作者: {{ previewContent.author }}</span>
              <span>发布时间: {{ formatDateTime(previewContent.publishTime) }}</span>
              <span>阅读量: {{ formatNumber(previewContent.viewCount) }}</span>
              <span>点赞量: {{ formatNumber(previewContent.likeCount) }}</span>
            </div>
          </div>
          <div class="preview-cover" v-if="previewContent.coverImage">
            <img :src="previewContent.coverImage" alt="封面图">
          </div>
          <div class="preview-description">
            <p>{{ previewContent.articleDescription || '暂无描述' }}</p>
          </div>
          <div class="preview-article-content">
            <div v-html="previewContent.articleContent || '暂无内容'"></div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getContentManagementList, getVideoAssetDetail, getContentDetail } from '@/api/education'

export default {
  name: 'ContentManagement',
  data() {
    return {
      // 搜索表单
      searchForm: {
        publishTimeRange: null,
        contentTitle: '',
        author: '',
        resourceType: '',
        categorySection: '',
        status: '',
        pinStatus: '',
        channel: ''
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
      // 预览对话框
      previewDialogVisible: false,
      previewContent: null
    }
  },
  computed: {
    previewTitle() {
      if (!this.previewContent) return '内容预览'
      return this.previewContent.contentType === '视频' ? '视频预览' : '图文预览'
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
          contentTitle: this.searchForm.contentTitle,
          author: this.searchForm.author,
          resourceType: this.searchForm.resourceType,
          categorySection: this.searchForm.categorySection,
          status: this.searchForm.status,
          pinStatus: this.searchForm.pinStatus,
          channel: this.searchForm.channel,
          publishStartTime: this.searchForm.publishTimeRange ? this.searchForm.publishTimeRange[0] : null,
          publishEndTime: this.searchForm.publishTimeRange ? this.searchForm.publishTimeRange[1] : null
        }
        const response = await getContentManagementList(params)
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
    // 刷新
    handleRefresh() {
      this.fetchData()
    },
    // 导出
    handleExport() {
      this.$message.info('导出功能开发中')
    },
    // 设置
    handleSettings() {
      this.$message.info('设置功能开发中')
    },
    // 编辑 - 根据内容类型跳转到对应的编辑页面
    handleEdit(row) {
      if (row.contentType === '视频') {
        // 跳转到视频编辑页面
        this.$router.push({
          path: '/education/video-add',
          query: { id: row.contentId, mode: 'edit' }
        })
      } else if (row.contentType === '文章') {
        // 跳转到图文编辑页面
        this.$router.push({
          path: '/education/content-add',
          query: { id: row.contentId, mode: 'edit' }
        })
      }
    },
    // 预览
    async handlePreview(row) {
      try {
        let response
        if (row.contentType === '视频') {
          response = await getVideoAssetDetail(row.contentId)
        } else if (row.contentType === '文章') {
          response = await getContentDetail(row.contentId)
        }

        if (response && response.code === 200) {
          this.previewContent = response.data
          this.previewDialogVisible = true
        }
      } catch (error) {
        this.$message.error('加载预览内容失败')
        console.error('加载预览内容失败:', error)
      }
    },
    // 详情 - 根据内容类型跳转到对应的详情页面
    handleDetail(row) {
      if (row.contentType === '视频') {
        // 跳转到视频详情页面
        this.$router.push({
          path: '/education/video-add',
          query: { id: row.contentId, mode: 'view' }
        })
      } else if (row.contentType === '文章') {
        // 跳转到图文详情页面
        this.$router.push({
          path: '/education/content-add',
          query: { id: row.contentId, mode: 'view' }
        })
      }
    },
    // 配置
    handleConfig(row) {
      this.$router.push({
        path: '/education/content-config',
        query: {
          contentId: row.contentId,
          contentType: row.contentType,
          title: row.contentTitle
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
.content-management-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-actions {
      display: flex;
      gap: 8px;
    }
  }

  .search-form {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #f5f5f5;
    border-radius: 4px;
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

  .preview-content {
    .video-preview, .article-preview {
      .preview-header {
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 1px solid #eee;

        h2 {
          margin: 0 0 10px 0;
          font-size: 24px;
          font-weight: 600;
          color: #333;
        }

        .preview-meta {
          display: flex;
          gap: 20px;
          font-size: 14px;
          color: #666;

          span {
            display: flex;
            align-items: center;
          }
        }
      }

      .preview-video {
        margin-bottom: 20px;

        .no-video {
          text-align: center;
          padding: 60px 0;
          color: #999;
          background-color: #f5f5f5;
          border-radius: 4px;
        }
      }

      .preview-cover {
        margin-bottom: 20px;

        img {
          width: 100%;
          max-height: 400px;
          object-fit: cover;
          border-radius: 4px;
        }
      }

      .preview-description {
        margin-bottom: 20px;

        h3 {
          font-size: 16px;
          margin-bottom: 10px;
          color: #333;
        }

        p {
          line-height: 1.8;
          color: #666;
          white-space: pre-wrap;
          word-break: break-word;
        }
      }

      .preview-article-content {
        padding: 20px;
        background-color: #f9f9f9;
        border-radius: 4px;
        line-height: 1.8;
        color: #333;

        ::v-deep img {
          max-width: 100%;
          height: auto;
        }
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

::v-deep .el-form--inline .el-form-item {
  margin-bottom: 10px;
  margin-right: 15px;
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