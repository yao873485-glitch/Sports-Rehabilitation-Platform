<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 平台标题 -->
      <h1 class="platform-title">运动康复平台</h1>

      <!-- 登录表单 -->
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <!-- 手机号输入 -->
        <el-form-item prop="phone">
          <el-input
            v-model="loginForm.phone"
            placeholder="请输入手机号码"
            prefix-icon="el-icon-user"
            size="large"
            clearable
          />
        </el-form-item>

        <!-- 密码输入 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            size="large"
            clearable
          >
            <i
              slot="suffix"
              :class="showPassword ? 'el-icon-view' : 'el-icon-view'"
              class="password-icon"
              @click="showPassword = !showPassword"
            />
          </el-input>
        </el-form-item>

        <!-- 登录按钮 -->
        <el-button
          type="primary"
          size="large"
          class="login-button"
          :loading="loading"
          @click="handleLogin"
        >
          立即登录
        </el-button>

        <!-- 注册申请链接 -->
        <div class="register-link">
          <span @click="showRegisterDialog">注册申请</span>
        </div>
      </el-form>
    </div>

    <!-- 注册弹窗 -->
    <register-dialog
      :visible.sync="registerDialogVisible"
      @success="handleRegisterSuccess"
    />

    <div class="admin-entry">
      <span @click="goToAdmin">管理员</span>
    </div>
  </div>
</template>

<script>
import RegisterDialog from './components/RegisterDialog.vue'
import { login } from '@/api/auth'
import { setToken } from '@/utils/auth'

export default {
  name: 'Login',

  components: {
    RegisterDialog
  },

  data() {
    return {
      loginForm: {
        phone: '',
        password: ''
      },
      loginRules: {
        phone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      },
      showPassword: false,
      loading: false,
      registerDialogVisible: false
    }
  },

  methods: {
    /**
     * 处理登录
     */
    handleLogin() {
      this.$refs.loginForm.validate(async(valid) => {
        if (!valid) {
          return
        }

        this.loading = true
        try {
          const response = await login(this.loginForm)

          if (response.code === 200) {
            // 保存token
            setToken(response.data.token)

            // 保存用户信息到localStorage
            localStorage.setItem('userInfo', JSON.stringify(response.data))

            this.$message.success('登录成功')

            // 跳转到首页（使用replace避免返回到登录页）
            this.$router.replace('/dashboard')
          } else {
            this.$message.error(response.message || '登录失败')
          }
        } catch (error) {
          console.error('登录失败', error)
          this.$message.error(error.response?.data?.message || '登录失败，请稍后重试')
        } finally {
          this.loading = false
        }
      })
    },

    /**
     * 显示注册弹窗
     */
    showRegisterDialog() {
      this.registerDialogVisible = true
    },

    /**
     * 注册成功回调
     */
    handleRegisterSuccess() {
      this.$message.success('注册申请提交成功，请等待管理员审核')
      this.registerDialogVisible = false
    },

    goToAdmin() {
      this.$router.push('/admin/login')
    }
  }
}
</script>

<style scoped lang="scss">
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);

  .login-box {
    width: 420px;
    padding: 50px 40px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    .platform-title {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      text-align: center;
      margin-bottom: 40px;
      letter-spacing: 2px;
    }

    .login-form {
      ::v-deep .el-form-item {
        margin-bottom: 24px;
      }

      ::v-deep .el-input__inner {
        height: 48px;
        line-height: 48px;
        font-size: 15px;
        border-radius: 8px;
        border-color: #dcdfe6;
        padding-left: 50px;
        transition: all 0.3s;

        &:focus {
          border-color: #9370db;
        }
      }

      ::v-deep .el-input__prefix {
        left: 18px;
        font-size: 18px;
        color: #909399;
        display: flex;
        align-items: center;
      }

      ::v-deep .el-input__suffix {
        right: 15px;
        display: flex;
        align-items: center;
      }

      .password-icon {
        cursor: pointer;
        font-size: 18px;
        color: #909399;
        margin-right: 10px;

        &:hover {
          color: #606266;
        }
      }

      .login-button {
        width: 100%;
        height: 48px;
        font-size: 16px;
        font-weight: 500;
        margin-top: 10px;
        background: linear-gradient(135deg, #9370db 0%, #7b68ee 100%);
        border: none;
        border-radius: 8px;
        letter-spacing: 1px;

        &:hover {
          background: linear-gradient(135deg, #7b68ee 0%, #9370db 100%);
        }
      }

      .register-link {
        text-align: center;
        margin-top: 20px;
        font-size: 14px;

        span {
          color: #909399;
          cursor: pointer;
          transition: color 0.3s;

          &:hover {
            color: #9370db;
          }
        }
      }
    }
  }
}

.admin-entry {
  position: fixed;
  right: 32px;
  bottom: 28px;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  user-select: none;

  span {
    border-bottom: 1px solid transparent;
    padding-bottom: 2px;
    transition: border-color 0.2s;
  }

  span:hover {
    border-bottom-color: #606266;
  }
}
</style>
