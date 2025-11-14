<template>
  <div class="error-page">
    <div class="error-container">
      <div class="error-code">404</div>
      <div class="error-message">抱歉，您访问的页面不存在</div>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Page404',
  methods: {
    goHome() {
      this.$router.push('/')
    }
  }
}
</script>

<style lang="scss" scoped>
.error-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;

  .error-container {
    text-align: center;

    .error-code {
      font-size: 120px;
      font-weight: bold;
      color: #409eff;
      margin-bottom: 20px;
    }

    .error-message {
      font-size: 20px;
      color: #606266;
      margin-bottom: 30px;
    }
  }
}
</style>