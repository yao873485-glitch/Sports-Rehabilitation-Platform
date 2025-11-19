<template>
  <div class="video-detail-container">
    <el-card>
      <div slot="header" class="detail-header">
        <el-button icon="el-icon-back" size="small" @click="goBack">返回</el-button>
        <span class="detail-title">{{ detailData.videoTitle || '加载中...' }}</span>
        <div class="header-actions">
          <el-button type="primary" size="small" @click="handleEdit">编辑</el-button>
        </div>
      </div>

      <div v-loading="loading" class="detail-content">
        <el-form label-width="120px" class="detail-form">
          <el-form-item label="视频ID">
            <div class="form-value">{{ detailData.videoId || '--' }}</div>
          </el-form-item>

          <el-form-item label="视频标题">
            <div class="form-value">{{ detailData.videoTitle || '--' }}</div>
          </el-form-item>

          <el-form-item label="封面图" class="image-item">
            <div class="form-value">
              <el-image
                v-if="detailData.coverImage"
                :src="detailData.coverImage"
                style="width: 400px; max-width: 100%;"
                fit="contain"
              >
                <div slot="error" class="image-error">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <span v-else>--</span>
            </div>
          </el-form-item>

          <el-form-item label="所属机构">
            <div class="form-value">{{ detailData.organizationName || '--' }}</div>
          </el-form-item>

          <el-form-item label="作者">
            <div class="form-value">{{ detailData.author || '--' }}</div>
          </el-form-item>

          <el-form-item label="是否需要登入">
            <div class="form-value">{{ detailData.requireLogin || '--' }}</div>
          </el-form-item>

          <el-form-item label="关联疾病">
            <div class="form-value">{{ detailData.relatedDiseases || '--' }}</div>
          </el-form-item>

          <el-form-item label="关联症状">
            <div class="form-value">{{ detailData.relatedSymptoms || '--' }}</div>
          </el-form-item>

          <el-form-item label="关联商品">
            <div class="form-value">{{ detailData.relatedEquipment || '--' }}</div>
          </el-form-item>

          <el-form-item label="自定义关联">
            <div class="form-value">{{ detailData.customRelations || '--' }}</div>
          </el-form-item>

          <el-form-item label="视频资源地址">
            <div class="form-value">{{ detailData.videoUrl || '--' }}</div>
          </el-form-item>

          <el-form-item label="文章内容">
            <div class="form-value">{{ detailData.videoDescription || '--' }}</div>
          </el-form-item>

          <el-form-item label="阅读量">
            <div class="form-value">{{ formatNumber(detailData.viewCount) }}</div>
          </el-form-item>

          <el-form-item label="发布时间">
            <div class="form-value">{{ formatDateTime(detailData.publishTime) }}</div>
          </el-form-item>

          <el-form-item label="首页状态">
            <div class="form-value">{{ detailData.homepageStatus || '--' }}</div>
          </el-form-item>

          <el-form-item label="状态">
            <div class="form-value">
              <el-tag
                v-if="detailData.contentStatus"
                :type="getStatusType(detailData.contentStatus)"
                size="small"
              >
                {{ detailData.contentStatus }}
              </el-tag>
              <span v-else>--</span>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getVideoAssetDetail } from '@/api/education'

export default {
  name: 'VideoDetail',
  data() {
    return {
      loading: false,
      detailData: {}
    }
  },
  created() {
    this.fetchDetail()
  },
  methods: {
    // 获取详情数据
    async fetchDetail() {
      const id = this.$route.query.id
      if (!id) {
        this.$message.error('缺少ID参数')
        this.goBack()
        return
      }

      this.loading = true
      try {
        const response = await getVideoAssetDetail(id)
        this.detailData = response.data || {}
      } catch (error) {
        this.$message.error('获取详情失败')
        console.error('获取详情失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 返回列表
    goBack() {
      this.$router.back()
    },
    // 编辑
    handleEdit() {
      this.$router.push({
        path: '/education/video-add',
        query: {
          id: this.$route.query.id,
          mode: 'edit'
        }
      })
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
      if (!dateTime) return '--'
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
.video-detail-container {
  padding: 20px;
  background-color: #f5f5f5;

  .el-card {
    background-color: #ffffff;
  }

  .detail-header {
    display: flex;
    align-items: center;
    gap: 16px;

    .detail-title {
      flex: 1;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }

    .header-actions {
      display: flex;
      gap: 8px;
    }
  }

  .detail-content {
    padding: 20px 0;

    .detail-form {
      max-width: 100%;
      border: 1px solid #ebeef5;

      ::v-deep .el-form-item {
        margin-bottom: 0;
        border-bottom: 1px solid #ebeef5;
        display: table;
        width: 100%;
        table-layout: fixed;

        &:last-child {
          border-bottom: none;
        }

        .el-form-item__label {
          background-color: rgb(245, 247, 250);
          color: #606266;
          font-weight: normal;
          text-align: left;
          padding: 12px 20px;
          border-right: 1px solid #ebeef5;
          width: 120px;
          margin-bottom: 0;
          line-height: 1.5;
          display: table-cell;
          vertical-align: top;
        }

        .el-form-item__content {
          margin-left: 0 !important;
          display: table-cell;
          vertical-align: top;
        }

        .form-value {
          color: #303133;
          line-height: 1.5;
          padding: 12px 20px;
          word-break: break-all;
          white-space: pre-wrap;
          width: 100%;

          .el-image {
            display: block;
            margin: 8px 0;
          }

          .image-error {
            width: 400px;
            height: 200px;
            background-color: #f5f7fa;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #909399;
            font-size: 40px;
            border: 1px solid #ebeef5;
          }
        }
      }
    }
  }
}

::v-deep .el-card__header {
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
}

::v-deep .el-card__body {
  padding: 0 20px 20px 20px;
}
</style>
