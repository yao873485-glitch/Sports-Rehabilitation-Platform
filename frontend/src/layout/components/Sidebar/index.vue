<template>
  <div class="sidebar-container">
    <div class="sidebar-logo">
      <div class="sidebar-logo-title">运动康复医疗管理平台</div>
    </div>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters(['sidebar']),
    routes() {
      return this.$router.options.routes
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    isCollapse() {
      return !this.sidebar.opened
    },
    variables() {
      return variables
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-container {
  background-color: rgb(0, 21, 41);
  width: 280px !important;
  height: 100vh;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;

  .sidebar-logo {
    background: rgb(0, 12, 23);
    height: 50px;
    line-height: 50px;
    text-align: center;
    overflow: hidden;

    .sidebar-logo-title {
      color: #fff;
      font-weight: 600;
      font-size: 14px;
      white-space: nowrap;
    }
  }

  .scrollbar-wrapper {
    overflow-x: hidden !important;
  }

  .el-scrollbar__bar.is-vertical {
    right: 0px;
  }

  .el-scrollbar {
    height: calc(100% - 50px);
  }

  .is-horizontal {
    display: none;
  }

  a {
    display: inline-block;
    width: 100%;
    overflow: hidden;
  }

  .el-menu {
    border: none;
    height: 100%;
    width: 100% !important;
    font-family: 'SimHei', '黑体', 'Microsoft YaHei', sans-serif;
    font-size: 21px;
  }

  // 确保所有菜单项都使用黑体和增大的字体
  ::v-deep .el-menu-item,
  ::v-deep .el-submenu__title {
    font-family: 'SimHei', '黑体', 'Microsoft YaHei', sans-serif !important;
    font-size: 21px !important;
    line-height: 56px;
  }

  // 调整图标大小以匹配字体
  ::v-deep .el-menu-item i,
  ::v-deep .el-submenu__title i {
    font-size: 21px;
  }
}
</style>
