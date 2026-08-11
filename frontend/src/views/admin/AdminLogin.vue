<template>
  <div class="admin-login-page">
    <el-card class="login-card">
      <div class="card-header">
        <h2>管理员登录</h2>
        <p>仅限审核人员使用</p>
      </div>

      <el-form ref="loginForm" :model="form" :rules="rules" label-position="top">
        <el-form-item label="管理员账号" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入管理员账号"
            clearable
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="full-width"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
        <div class="back-link">
          <span @click="goBack">← 返回用户登录</span>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { adminLogin } from '@/api/admin'
import { setToken } from '@/utils/auth'

export default {
  name: 'AdminLogin',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入管理员账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入管理员密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(async(valid) => {
        if (!valid) return
        this.loading = true
        try {
          const res = await adminLogin(this.form)
          setToken(res.data.token)
          localStorage.setItem('adminInfo', JSON.stringify(res.data))
          this.$message.success('登录成功')
          this.$router.replace('/admin/applications')
        } catch (error) {
          this.$message.error(error.response?.data?.message || '登录失败，请稍后再试')
        } finally {
          this.loading = false
        }
      })
    },
    goBack() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped lang="scss">
.admin-login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #eef2ff 0%, #f7f8fc 100%);
  padding: 40px 20px;
}

.login-card {
  width: 420px;
  border-radius: 12px;
  box-shadow: 0 12px 32px rgba(35, 37, 51, 0.12);

  .card-header {
    text-align: center;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 24px;
      color: #1f2d3d;
    }

    p {
      margin: 8px 0 0;
      color: #909399;
      font-size: 14px;
    }
  }

  .full-width {
    width: 100%;
  }

  .back-link {
    text-align: center;
    font-size: 14px;
    color: #606266;
    cursor: pointer;

    span {
      border-bottom: 1px solid transparent;
      transition: border-color 0.2s;
      padding-bottom: 2px;
    }

    span:hover {
      border-bottom-color: #606266;
    }
  }
}
</style>
