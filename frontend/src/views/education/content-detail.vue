<template>
  <div class="content-detail-container">
    <el-card>
      <div slot="header" class="header">
        <el-button icon="el-icon-back" size="small" @click="goBack">返回</el-button>
        <span class="title">{{ detailData.contentTitle || detailData.videoTitle || '详情' }}</span>
        <div class="header-actions">
          <el-button type="primary" size="small" @click="handleEdit">编辑</el-button>
        </div>
      </div>

      <div v-loading="loading" class="detail-content">
        <div class="detail-form">
          <div class="form-row">
            <div class="form-label">内容ID</div>
            <div class="form-value">{{ detailData.contentId || detailData.videoId || '-' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">内容标题</div>
            <div class="form-value">{{ detailData.contentTitle || detailData.videoTitle || '-' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">内容描述</div>
            <div class="form-value">{{ detailData.articleDescription || detailData.videoDescription || '-' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">封面图</div>
            <div class="form-value image-preview">
              <img v-if="detailData.coverImage" :src="detailData.coverImage" alt="封面图" />
              <span v-else>-</span>
            </div>
          </div>

          <div class="form-row">
            <div class="form-label">作者</div>
            <div class="form-value">{{ detailData.author || '-' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">作者类型</div>
            <div class="form-value">{{ detailData.authorType || '-' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">是否支持游客模式</div>
            <div class="form-value">{{ detailData.supportGameMode === 1 ? '是' : '否' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">是否原创</div>
            <div class="form-value">{{ detailData.isOriginal === 1 ? '是' : '否' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">所属模块</div>
            <div class="form-value">{{ detailData.moduleCategory || '-' }}</div>
          </div>

          <div class="form-row" v-if="contentType === '视频'">
            <div class="form-label">视频来源</div>
            <div class="form-value">{{ detailData.videoSource || '-' }}</div>
          </div>

          <div class="form-row" v-if="contentType === '视频'">
            <div class="form-label">视频资源地址</div>
            <div class="form-value">{{ detailData.videoUrl || '-' }}</div>
          </div>

          <div class="form-row" v-if="contentType === '文章'">
            <div class="form-label">内容来源</div>
            <div class="form-value">{{ detailData.contentSource || '-' }}</div>
          </div>

          <div class="form-row" v-if="contentType === '文章'">
            <div class="form-label">外链地址</div>
            <div class="form-value">{{ detailData.externalLink || '-' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">点赞基数</div>
            <div class="form-value">{{ detailData.likeCountBase || 0 }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">阅读基数</div>
            <div class="form-value">{{ detailData.viewCountBase || 0 }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">实际点赞量</div>
            <div class="form-value">{{ detailData.likeCount || 0 }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">实际阅读量</div>
            <div class="form-value">{{ detailData.viewCount || 0 }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">发布时间</div>
            <div class="form-value">{{ formatDateTime(detailData.publishTime) }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">内容状态</div>
            <div class="form-value">
              <el-tag v-if="detailData.contentStatus" :type="getStatusType(detailData.contentStatus)" size="small">
                {{ detailData.contentStatus }}
              </el-tag>
              <span v-else>-</span>
            </div>
          </div>

          <div class="form-row">
            <div class="form-label">操作人</div>
            <div class="form-value">{{ detailData.operator || '-' }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">创建时间</div>
            <div class="form-value">{{ formatDateTime(detailData.createdTime || detailData.createdAt) }}</div>
          </div>

          <div class="form-row">
            <div class="form-label">更新时间</div>
            <div class="form-value">{{ formatDateTime(detailData.updatedTime || detailData.updatedAt) }}</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getContentDetail, getVideoAssetDetail } from '@/api/education'

export default {
  name: 'ContentDetail',
  data() {
    return {
      loading: false,
      contentId: '',
      contentType: '',
      detailData: {}
    }
  },
  created() {
    const query = this.$route.query
    this.contentId = query.contentId
    this.contentType = query.contentType

    if (this.contentId && this.contentType) {
      this.fetchDetail()
    } else {
      this.$message.error('缺少必要参数')
      this.goBack()
    }
  },
  methods: {
    // 获取详情数据
    async fetchDetail() {
      this.loading = true
      try {
        let response
        if (this.contentType === '视频') {
          response = await getVideoAssetDetail(this.contentId)
        } else if (this.contentType === '文章') {
          response = await getContentDetail(this.contentId)
        }

        if (response && response.data) {
          this.detailData = response.data
        }
      } catch (error) {
        this.$message.error('获取详情失败')
        console.error('获取详情失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      const date = new Date(dateTime)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '已发布': 'success',
        '草稿': 'info',
        '已下架': 'danger',
        '未上架': 'warning'
      }
      return statusMap[status] || 'info'
    },
    // 编辑
    handleEdit() {
      if (this.contentType === '视频') {
        // 跳转到视频编辑页面
        this.$router.push({
          path: '/education/video-add',
          query: { id: this.contentId, mode: 'edit' }
        })
      } else if (this.contentType === '文章') {
        // 跳转到图文编辑页面
        this.$router.push({
          path: '/education/content-add',
          query: { id: this.contentId, mode: 'edit' }
        })
      }
    },
    // 返回
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.content-detail-container {
  padding: 20px;
  background-color: #f5f5f5;

  .el-card {
    background-color: #ffffff;
    border: 1px solid #e4e7ed;
  }

  .header {
    display: flex;
    align-items: center;
    gap: 16px;

    .title {
      flex: 1;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .detail-content {
    padding: 20px 0;
  }

  .detail-form {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    background-color: #ffffff;
    overflow: hidden;

    .form-row {
      display: flex;
      border-bottom: 1px solid #dcdfe6;
      min-height: 50px;

      &:last-child {
        border-bottom: none;
      }

      .form-label {
        width: 200px;
        min-width: 200px;
        padding: 12px 20px;
        background-color: #ffffff;
        color: #606266;
        font-weight: 500;
        display: flex;
        align-items: center;
        border-right: 1px solid #dcdfe6;
      }

      .form-value {
        flex: 1;
        padding: 12px 20px;
        background-color: rgb(245, 247, 250);
        color: #303133;
        display: flex;
        align-items: center;
        word-break: break-all;

        &.image-preview {
          img {
            max-width: 300px;
            max-height: 200px;
            border-radius: 4px;
            margin: 8px 0;
          }
        }
      }
    }
  }
}

::v-deep .el-card__header {
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #fafafa;
}

::v-deep .el-card__body {
  padding: 0 20px 20px 20px;
}
</style>
