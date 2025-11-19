<template>
  <div class="pagination-container">
    <el-pagination
      :background="background"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      :layout="layout"
      :page-sizes="pageSizes"
      :total="total"
      v-bind="$attrs"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    limit: {
      type: Number,
      default: 20
    },
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 30, 50]
      }
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return this.limit
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.$emit('pagination', { page: this.currentPage, limit: val })
    },
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
    }
  }
}
</script>

<style lang="scss" scoped>
.pagination-container {
  background: #fff;
  padding: 32px 16px;
}

.pagination-container.hidden {
  display: none;
}

// 修改分页组件的颜色主题
::v-deep .el-pagination {
  // 当前页码背景色
  .el-pager li.active {
    background-color: rgb(106, 91, 140) !important;
    color: #fff !important;
  }

  // 鼠标悬停时的颜色
  .el-pager li:hover {
    color: rgb(106, 91, 140) !important;
  }

  // 当前页码悬停时保持白色文字
  .el-pager li.active:hover {
    background-color: rgb(106, 91, 140) !important;
    color: #fff !important;
  }

  // 上一页/下一页按钮悬停
  .btn-prev:hover,
  .btn-next:hover {
    color: rgb(106, 91, 140) !important;
  }

  // 跳转按钮样式
  .el-pagination__jump {
    .el-input__inner:focus {
      border-color: rgb(106, 91, 140) !important;
    }
  }
}
</style>