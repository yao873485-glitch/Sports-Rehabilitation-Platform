<template>
  <div v-if="!item.hidden" class="sidebar-item-container">
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item
          :index="resolvePath(onlyOneChild.path)"
          :class="{'submenu-title-noDropdown':!isNest}"
          @mouseenter.native="showTooltip(onlyOneChild.meta.title)"
          @mouseleave.native="hideTooltip"
        >
          <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>

    <el-submenu
      v-else
      ref="subMenu"
      :index="resolvePath(item.path)"
      popper-append-to-body
      @mouseenter.native="showTooltip(item.meta.title)"
      @mouseleave.native="hideTooltip"
    >
      <template slot="title">
        <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" />
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>

    <!-- 悬停提示 -->
    <div v-if="tooltipVisible" class="hover-tooltip">{{ tooltipText }}</div>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'

export default {
  name: 'SidebarItem',
  components: { Item, AppLink },
  props: {
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    this.onlyOneChild = null
    return {
      tooltipVisible: false,
      tooltipText: ''
    }
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          this.onlyOneChild = item
          return true
        }
      })

      if (showingChildren.length === 1) {
        return true
      }

      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    },
    showTooltip(title) {
      this.tooltipText = title
      this.tooltipVisible = true
    },
    hideTooltip() {
      this.tooltipVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-item-container {
  position: relative;
}

// 悬停提示样式
.hover-tooltip {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background: rgba(0, 21, 41, 0.95);
  color: #fff;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 12px;
  font-family: 'SimHei', '黑体', 'Microsoft YaHei', sans-serif;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  z-index: 9999;
  pointer-events: none;
  white-space: nowrap;
}
</style>
