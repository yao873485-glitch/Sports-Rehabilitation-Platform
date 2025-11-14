<template>
  <div class="classroom-container">
    <el-card>
      <div slot="header">
        <span>教室配置</span>
      </div>

      <!-- 筛选和查询区域 -->
      <div class="filter-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
          <el-form-item label="教室名称">
            <el-input
              v-model="queryParams.searchKeyword"
              placeholder="请输入教室名称"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="教室状态">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
              <el-option label="请选择状态" value="" />
              <el-option label="已上架" value="已上架" />
              <el-option label="已下架" value="已下架" />
              <el-option label="维护中" value="维护中" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 教室列表 -->
      <div class="table-container">
        <el-table
          v-loading="loading"
          :data="classroomList"
          border
          :stripe="true"
          style="width: 100%"
          :header-cell-style="{ background: '#f5f7fa' }"
        >
          <el-table-column label="运动方式" prop="exerciseType" align="center" width="120" />
          <el-table-column label="教室名称" prop="classroomName" align="center" min-width="150" />
          <el-table-column label="课程时长" prop="classDuration" align="center" width="100">
            <template slot-scope="scope">
              {{ scope.row.classDuration }} 分钟
            </template>
          </el-table-column>
          <el-table-column label="预约上限" prop="reservationLimit" align="center" width="100" />
          <el-table-column label="状态" prop="status" align="center" width="100">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status === '已上架' ? 'success' : scope.row.status === '已下架' ? 'danger' : 'warning'"
              >
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="上次操作时间" prop="lastOperationTime" align="center" width="180">
            <template slot-scope="scope">
              {{ parseTime(scope.row.lastOperationTime) }}
            </template>
          </el-table-column>
          <el-table-column label="创建时间" prop="createdTime" align="center" width="180">
            <template slot-scope="scope">
              {{ parseTime(scope.row.createdTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" fixed="right">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleEdit(scope.row)"
              >
                编辑
              </el-button>
              <el-button
                size="mini"
                type="text"
                :icon="scope.row.status === '已上架' ? 'el-icon-bottom' : 'el-icon-top'"
                :class="scope.row.status === '已上架' ? 'text-danger' : 'text-success'"
                @click="handleStatusChange(scope.row)"
              >
                {{ scope.row.status === '已上架' ? '下架' : '上架' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <div class="pagination-info">
            当前显示第 {{ (queryParams.pageNum - 1) * queryParams.pageSize + 1 }} 到
            {{ Math.min(queryParams.pageNum * queryParams.pageSize, total) }} 条，
            总共 {{ total }} 条数据
          </div>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryParams.pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="queryParams.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          />
        </div>
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialog.title"
      :visible.sync="dialog.visible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="运动方式" prop="exerciseType">
          <el-select v-model="form.exerciseType" placeholder="请选择运动方式" style="width: 100%">
            <el-option label="游泳" value="游泳" />
            <el-option label="瑜伽" value="瑜伽" />
            <el-option label="普拉提" value="普拉提" />
            <el-option label="功率自行车" value="功率自行车" />
            <el-option label="跑步机" value="跑步机" />
            <el-option label="力量训练" value="力量训练" />
            <el-option label="有氧操" value="有氧操" />
          </el-select>
        </el-form-item>
        <el-form-item label="教室名称" prop="classroomName">
          <el-input v-model="form.classroomName" placeholder="请输入教室名称" />
        </el-form-item>
        <el-form-item label="课程时长" prop="classDuration">
          <el-input-number
            v-model="form.classDuration"
            :min="15"
            :max="180"
            :step="15"
            placeholder="请输入课程时长"
            style="width: 100%"
          />
          <div class="form-tip">单位：分钟</div>
        </el-form-item>
        <el-form-item label="预约上限" prop="reservationLimit">
          <el-input-number
            v-model="form.reservationLimit"
            :min="1"
            :max="100"
            placeholder="请输入预约上限"
            style="width: 100%"
          />
          <div class="form-tip">单位：人</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listClassroom, getClassroom, addClassroom, updateClassroom, deleteClassroom, onlineClassroom, offlineClassroom } from '@/api/classroom'

export default {
  name: 'ClassroomConfig',
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        searchKeyword: '',
        status: ''
      },
      // 表格数据
      classroomList: [],
      total: 0,
      loading: false,
      // 对话框
      dialog: {
        visible: false,
        title: ''
      },
      // 表单数据
      form: {
        id: undefined,
        exerciseType: '',
        classroomName: '',
        classDuration: 60,
        reservationLimit: 20
      },
      // 表单验证规则
      rules: {
        exerciseType: [
          { required: true, message: '请选择运动方式', trigger: 'change' }
        ],
        classroomName: [
          { required: true, message: '请输入教室名称', trigger: 'blur' }
        ],
        classDuration: [
          { required: true, message: '请输入课程时长', trigger: 'blur' }
        ],
        reservationLimit: [
          { required: true, message: '请输入预约上限', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取教室列表
    async getList() {
      this.loading = true
      try {
        const response = await listClassroom(this.queryParams)
        this.classroomList = response.data.records
        this.total = response.data.total
      } catch (error) {
        console.error('获取教室列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    // 重置查询
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        searchKeyword: '',
        status: ''
      }
      this.getList()
    },
    // 新增
    handleAdd() {
      this.dialog = {
        visible: true,
        title: '新增教室'
      }
      this.form = {
        id: undefined,
        exerciseType: '',
        classroomName: '',
        classDuration: 60,
        reservationLimit: 20
      }
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    // 编辑
    async handleEdit(row) {
      try {
        const response = await getClassroom(row.id)
        this.form = { ...response.data }
        this.dialog = {
          visible: true,
          title: '编辑教室'
        }
        this.$nextTick(() => {
          this.$refs.form.clearValidate()
        })
      } catch (error) {
        console.error('获取教室详情失败:', error)
      }
    },
    // 状态切换
    async handleStatusChange(row) {
      const action = row.status === '已上架' ? '下架' : '上架'
      const confirmMessage = `确定要${action}该教室吗？`

      try {
        await this.$confirm(confirmMessage, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        if (row.status === '已上架') {
          await offlineClassroom(row.id)
        } else {
          await onlineClassroom(row.id)
        }

        this.$message.success(`${action}成功`)
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error(`${action}教室失败:`, error)
        }
      }
    },
    // 提交表单
    async submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            if (this.form.id) {
              await updateClassroom(this.form)
              this.$message.success('修改成功')
            } else {
              await addClassroom(this.form)
              this.$message.success('新增成功')
            }
            this.dialog.visible = false
            this.getList()
          } catch (error) {
            console.error('提交表单失败:', error)
          }
        }
      })
    },
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.queryParams.pageNum = 1
      this.getList()
    },
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    // 时间格式化
    parseTime(time) {
      if (!time) return ''
      return this.$moment(time).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<style lang="scss" scoped>
.classroom-container {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.table-container {
  margin-top: 20px;
}

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.pagination-info {
  color: #606266;
  font-size: 14px;
}

.form-tip {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
}

.text-danger {
  color: #f56c6c;
}

.text-success {
  color: #67c23a;
}
</style>