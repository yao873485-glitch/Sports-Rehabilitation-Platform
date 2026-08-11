<template>
  <div class="table-toolbar">
    <!-- 刷新按钮 -->
    <el-tooltip content="刷新" placement="top">
      <el-button
        size="small"
        icon="el-icon-refresh"
        circle
        @click="handleRefresh"
      />
    </el-tooltip>

    <!-- 密度设置 -->
    <el-popover
      placement="bottom"
      width="120"
      trigger="hover"
    >
      <div class="density-options">
        <div
          v-for="item in densityOptions"
          :key="item.value"
          class="density-item"
          :class="{ active: currentDensity === item.value }"
          @click="handleDensityChange(item.value)"
        >
          <i :class="item.icon" />
          <span>{{ item.label }}</span>
          <i v-if="currentDensity === item.value" class="el-icon-check" />
        </div>
      </div>
      <el-button
        slot="reference"
        size="small"
        icon="el-icon-c-scale-to-original"
        circle
        title="密度"
      />
    </el-popover>

    <!-- 列设置 -->
    <el-popover
      placement="bottom"
      width="200"
      trigger="hover"
    >
      <div class="column-settings">
        <div class="column-header">
          <el-checkbox
            v-model="allChecked"
            :indeterminate="isIndeterminate"
            @change="handleCheckAllChange"
          >
            列展示
          </el-checkbox>
          <el-button type="text" size="small" @click="handleReset">重置</el-button>
        </div>
        <div class="column-list">
          <div
            v-for="column in columns"
            :key="column.prop"
            class="column-item"
          >
            <el-checkbox
              v-model="column.visible"
              @change="handleColumnChange"
            >
              {{ column.label }}
            </el-checkbox>
          </div>
        </div>
      </div>
      <el-button
        slot="reference"
        size="small"
        icon="el-icon-setting"
        circle
        title="列设置"
      />
    </el-popover>

    <!-- 全屏切换 -->
    <el-tooltip :content="tooltipContent" placement="top" :open-delay="300">
      <el-button
        size="small"
        :icon="isFullscreen ? 'el-icon-aim' : 'el-icon-full-screen'"
        circle
        @click="handleFullscreen"
      />
    </el-tooltip>
  </div>
</template>

<script>
export default {
  name: 'TableToolbar',
  props: {
    // 列配置
    columns: {
      type: Array,
      required: true,
      default: () => []
    },
    // 当前密度
    density: {
      type: String,
      default: 'default'
    },
    // 是否全屏
    fullscreen: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentDensity: this.density,
      isFullscreen: this.fullscreen,
      densityOptions: [
        { label: '默认', value: 'default', icon: 'el-icon-minus' },
        { label: '中等', value: 'medium', icon: 'el-icon-c-scale-to-original' },
        { label: '紧凑', value: 'small', icon: 'el-icon-s-operation' }
      ]
    }
  },
  computed: {
    allChecked: {
      get() {
        return this.columns.every(col => col.visible)
      },
      set(val) {
        // setter for v-model
      }
    },
    isIndeterminate() {
      const visibleCount = this.columns.filter(col => col.visible).length
      return visibleCount > 0 && visibleCount < this.columns.length
    },
    tooltipContent() {
      return this.isFullscreen ? '退出全屏' : '全屏'
    }
  },
  watch: {
    density(val) {
      this.currentDensity = val
    },
    fullscreen(val) {
      this.isFullscreen = val
    }
  },
  methods: {
    // 刷新
    handleRefresh() {
      this.$emit('refresh')
    },
    // 密度变化
    handleDensityChange(density) {
      this.currentDensity = density
      this.$emit('density-change', density)
    },
    // 列变化
    handleColumnChange() {
      this.$emit('column-change', this.columns)
    },
    // 全选/取消全选
    handleCheckAllChange(checked) {
      this.columns.forEach(col => {
        col.visible = checked
      })
      this.$emit('column-change', this.columns)
    },
    // 重置列设置
    handleReset() {
      this.columns.forEach(col => {
        col.visible = true
      })
      this.$emit('column-change', this.columns)
    },
    // 全屏切换
    handleFullscreen() {
      this.isFullscreen = !this.isFullscreen
      this.$emit('fullscreen-change', this.isFullscreen)
    }
  }
}
</script>

<style lang="scss" scoped>
.table-toolbar {
  display: flex;
  gap: 8px;
  align-items: center;
}

.density-options {
  .density-item {
    display: flex;
    align-items: center;
    padding: 8px 12px;
    cursor: pointer;
    transition: background-color 0.3s;

    &:hover {
      background-color: #f5f7fa;
    }

    &.active {
      color: rgb(106, 91, 140);
      background-color: #f0edf4;
    }

    i:first-child {
      margin-right: 8px;
    }

    span {
      flex: 1;
    }

    .el-icon-check {
      color: rgb(106, 91, 140);
    }
  }
}

.column-settings {
  .column-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 8px;
    margin-bottom: 8px;
    border-bottom: 1px solid #ebeef5;

    ::v-deep .el-button--text {
      color: rgb(106, 91, 140);
      padding: 0;
    }
  }

  .column-list {
    max-height: 300px;
    overflow-y: auto;

    .column-item {
      padding: 6px 0;

      ::v-deep .el-checkbox {
        width: 100%;
      }
    }
  }
}
</style>
