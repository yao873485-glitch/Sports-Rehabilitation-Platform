<template>
  <div class="admin-app-page">
    <div class="page-header">
      <div>
        <h2>待审核用户申请</h2>
        <p>共有 {{ applications.length }} 条记录等待处理</p>
      </div>
      <div class="header-actions">
        <el-button
          icon="el-icon-refresh"
          :loading="loading"
          @click="fetchApplications"
        >
          刷新
        </el-button>
        <el-button type="primary" @click="handleLogout">退出登录</el-button>
      </div>
    </div>

    <el-card shadow="never">
      <el-table
        :data="applications"
        v-loading="loading"
        border
        empty-text="暂无待审核申请"
      >
        <el-table-column prop="username" label="账户用户名" min-width="140" />
        <el-table-column prop="phone" label="联系电话" min-width="130" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="applyRole" label="申请角色" min-width="120" />
        <el-table-column prop="organization" label="所在机构" min-width="180" />
        <el-table-column label="证件预览" width="150">
          <template slot-scope="{ row }">
            <el-image
              v-if="row.certificateUrl"
              :src="row.certificateUrl"
              fit="cover"
              class="certificate-thumb"
              :preview-src-list="[row.certificateUrl]"
            >
              <div slot="error" class="image-slot">无法预览</div>
            </el-image>
            <span v-else class="text-muted">未上传</span>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" min-width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="{ row }">
            <el-button
              type="success"
              size="mini"
              :loading="processingId === row.id && actionType === 'approve'"
              @click="handleApprove(row)"
            >
              通过
            </el-button>
            <el-button
              type="danger"
              size="mini"
              :loading="processingId === row.id && actionType === 'reject'"
              @click="handleReject(row)"
            >
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { fetchPendingApplications, approveApplication, rejectApplication } from '@/api/admin'
import { removeToken } from '@/utils/auth'

export default {
  name: 'AdminApplications',
  data() {
    return {
      applications: [],
      loading: false,
      processingId: null,
      actionType: null
    }
  },
  created() {
    this.fetchApplications()
  },
  methods: {
    async fetchApplications() {
      this.loading = true
      try {
        const res = await fetchPendingApplications()
        this.applications = res.data || []
      } catch (error) {
        this.$message.error(error.response?.data?.message || '获取列表失败')
      } finally {
        this.loading = false
      }
    },
    async handleApprove(row) {
      try {
        await this.$confirm(`确认通过【${row.username}】的申请吗？`, '提示', {
          type: 'warning'
        })
      } catch {
        return
      }

      this.processingId = row.id
      this.actionType = 'approve'
      try {
        await approveApplication(row.id)
        this.$message.success('已通过申请')
        this.fetchApplications()
      } catch (error) {
        this.$message.error(error.response?.data?.message || '操作失败')
      } finally {
        this.processingId = null
        this.actionType = null
      }
    },
    async handleReject(row) {
      try {
        await this.$confirm(`确认拒绝【${row.username}】的申请吗？`, '提示', {
          type: 'warning'
        })
      } catch {
        return
      }

      this.processingId = row.id
      this.actionType = 'reject'
      try {
        await rejectApplication(row.id)
        this.$message.success('已拒绝该申请')
        this.fetchApplications()
      } catch (error) {
        this.$message.error(error.response?.data?.message || '操作失败')
      } finally {
        this.processingId = null
        this.actionType = null
      }
    },
    handleLogout() {
      removeToken()
      localStorage.removeItem('adminInfo')
      this.$router.replace('/admin/login')
    }
  }
}
</script>

<style scoped lang="scss">
.admin-app-page {
  min-height: 100vh;
  background: #f5f7fb;
  padding: 32px 40px 60px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h2 {
    margin: 0;
    font-size: 24px;
    color: #1f2d3d;
  }

  p {
    margin: 6px 0 0;
    color: #909399;
    font-size: 14px;
  }
}

.header-actions {
  display: flex;
  gap: 12px;
}

.certificate-thumb {
  width: 120px;
  height: 80px;
  border-radius: 6px;
  border: 1px solid #ebeef5;
  object-fit: cover;
}

.text-muted {
  color: #909399;
  font-size: 13px;
}

.image-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: #c0c4cc;
  font-size: 12px;
}
</style>
