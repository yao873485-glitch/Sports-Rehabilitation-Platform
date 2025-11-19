<template>
  <div class="app-container">
    <!-- 筛选和查询功能 -->
    <div class="filter-container">
      <div class="filter-item">
        <label>设备名称：</label>
        <el-input
          v-model="queryParams.equipmentName"
          placeholder="请输入设备名称"
          style="width: 200px;"
          clearable
        />
      </div>

      <div class="filter-item">
        <label>设备类型：</label>
        <el-select
          v-model="queryParams.equipmentType"
          placeholder="请选择设备类型"
          style="width: 200px;"
          clearable
        >
          <el-option label="全部" value="全部" />
          <el-option
            v-for="type in equipmentTypes"
            :key="type"
            :label="type"
            :value="type"
          />
        </el-select>
      </div>

      <div class="filter-item">
        <label>设备状态：</label>
        <el-select
          v-model="queryParams.status"
          placeholder="请选择设备状态"
          style="width: 150px;"
          clearable
        >
          <el-option label="全部" value="全部" />
          <el-option label="上架" value="上架" />
          <el-option label="下架" value="下架" />
        </el-select>
      </div>

      <div class="filter-item">
        <el-button class="custom-primary-btn" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </div>
    </div>

    <!-- 批量操作功能 -->
    <div class="operation-container">
      <el-button
        class="custom-primary-btn"
        icon="el-icon-top"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchOnline"
      >
        批量上架
      </el-button>
      <el-button
        class="custom-primary-btn"
        icon="el-icon-bottom"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchOffline"
      >
        批量下架
      </el-button>
      <el-button
        class="custom-primary-btn"
        icon="el-icon-upload2"
        @click="handleImport"
      >
        批量导入
      </el-button>
      <el-button
        class="custom-primary-btn"
        icon="el-icon-plus"
        @click="handleAdd"
      >
        新增
      </el-button>
    </div>

    <!-- 设备列表展示 -->
    <el-table
      v-loading="loading"
      :data="equipmentList"
      border
      stripe
      style="width: 100%; margin-top: 16px;"
      :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" header-align="center" />
      <el-table-column prop="serialNumber" label="序号" width="100" align="center" header-align="center" />
      <el-table-column prop="region" label="区域" width="140" align="center" header-align="center" />
      <el-table-column prop="equipmentName" label="设备名称" width="200" align="center" header-align="center" />
      <el-table-column prop="equipmentType" label="设备类型" width="220" align="center" header-align="center" />
      <el-table-column prop="equipmentCode" label="设备编号" width="180" align="center" header-align="center" />

      <el-table-column prop="status" label="状态" width="120" align="center" header-align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '上架' ? 'success' : 'danger'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="lastOperationTime" label="上次操作时间" width="180" align="center" header-align="center" />
      <el-table-column prop="createdTime" label="创建时间" width="180" align="center" header-align="center" />

      <!-- 固定在右侧的操作列 -->
      <el-table-column
        label="操作"
        width="200"
        align="center"
        header-align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >
            查看
          </el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-delete"
            style="color: #f56c6c;"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 设备详情对话框 -->
    <el-dialog
      title="设备详情"
      :visible.sync="detailVisible"
      width="600px"
      append-to-body
    >
      <div v-if="currentEquipment" class="equipment-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="序号">{{ currentEquipment.serialNumber }}</el-descriptions-item>
          <el-descriptions-item label="区域">{{ currentEquipment.region }}</el-descriptions-item>
          <el-descriptions-item label="设备名称">{{ currentEquipment.equipmentName }}</el-descriptions-item>
          <el-descriptions-item label="设备类型">{{ currentEquipment.equipmentType }}</el-descriptions-item>
          <el-descriptions-item label="设备编号">{{ currentEquipment.equipmentCode }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentEquipment.status === '上架' ? 'success' : 'danger'">
              {{ currentEquipment.status }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="上次操作时间">{{ currentEquipment.lastOperationTime }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentEquipment.createdTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 新增/编辑设备对话框 -->
    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="500px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="区域" prop="region">
          <el-input v-model="form.region" placeholder="请输入区域" />
        </el-form-item>
        <el-form-item label="设备名称" prop="equipmentName">
          <el-input v-model="form.equipmentName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备类型" prop="equipmentType">
          <el-input v-model="form.equipmentType" placeholder="请输入设备类型" />
        </el-form-item>
        <el-form-item label="设备编号" prop="equipmentCode">
          <el-input v-model="form.equipmentCode" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%;">
            <el-option label="上架" value="上架" />
            <el-option label="下架" value="下架" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog
      title="批量导入设备"
      :visible.sync="importVisible"
      width="500px"
      append-to-body
    >
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleFileChange"
        :limit="1"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传Excel文件，且不超过10MB</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="importVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitImport">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getEquipmentList,
  getEquipmentDetail,
  addEquipment,
  updateEquipment,
  deleteEquipment,
  getEquipmentTypes,
  batchOperation,
  importEquipment
} from '@/api/equipment'

export default {
  name: 'EquipmentList',
  data() {
    return {
      // 加载状态
      loading: true,
      // 总条数
      total: 0,
      // 设备表格数据
      equipmentList: [],
      // 设备类型选项
      equipmentTypes: [],
      // 多选数据
      multipleSelection: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentName: '',
        equipmentType: '全部',
        status: '全部'
      },
      // 详情弹窗
      detailVisible: false,
      currentEquipment: null,
      // 表单弹窗
      formVisible: false,
      formTitle: '',
      form: {},
      // 导入弹窗
      importVisible: false,
      importFile: null,
      // 表单校验
      rules: {
        region: [
          { required: true, message: '区域不能为空', trigger: 'blur' }
        ],
        equipmentName: [
          { required: true, message: '设备名称不能为空', trigger: 'blur' }
        ],
        equipmentType: [
          { required: true, message: '设备类型不能为空', trigger: 'blur' }
        ],
        equipmentCode: [
          { required: true, message: '设备编号不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getEquipmentTypes()
  },
  methods: {
    /** 查询设备列表 */
    getList() {
      this.loading = true
      getEquipmentList(this.queryParams).then(response => {
        console.log('设备列表响应:', response)
        this.equipmentList = response.data.records || []
        this.total = response.data.total || 0
        this.loading = false
      }).catch((error) => {
        console.error('获取设备列表失败:', error)
        this.$message.error('获取设备列表失败，请稍后重试')
        this.equipmentList = []
        this.total = 0
        this.loading = false
      })
    },

    /** 获取设备类型 */
    getEquipmentTypes() {
      getEquipmentTypes().then(response => {
        console.log('设备类型响应:', response)
        this.equipmentTypes = response.data || []
      }).catch((error) => {
        console.error('获取设备类型失败:', error)
        this.equipmentTypes = []
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        equipmentName: '',
        equipmentType: '全部',
        status: '全部'
      }
      this.handleQuery()
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    /** 查看操作 */
    handleView(row) {
      getEquipmentDetail(row.id).then(response => {
        this.currentEquipment = response.data
        this.detailVisible = true
      })
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.formTitle = '新增设备'
      this.form = {
        id: null,
        region: '',
        equipmentName: '',
        equipmentType: '',
        equipmentCode: '',
        status: '上架'
      }
      this.formVisible = true
    },

    /** 修改按钮操作 */
    handleEdit(row) {
      this.formTitle = '编辑设备'
      getEquipmentDetail(row.id).then(response => {
        this.form = response.data
        this.formVisible = true
      })
    },

    /** 提交表单 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEquipment(this.form.id, this.form).then(() => {
              this.$message.success('修改成功')
              this.formVisible = false
              this.getList()
            })
          } else {
            addEquipment(this.form).then(() => {
              this.$message.success('新增成功')
              this.formVisible = false
              this.getList()
            })
          }
        }
      })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除该设备？', '系统提示', {
        type: 'warning'
      }).then(() => {
        return deleteEquipment(row.id)
      }).then(() => {
        this.getList()
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /** 批量上架 */
    handleBatchOnline() {
      const ids = this.multipleSelection.map(item => item.id)
      this.$confirm('是否确认批量上架选中的设备？', '系统提示', {
        type: 'warning'
      }).then(() => {
        return batchOperation({
          ids: ids,
          operationType: '上架'
        })
      }).then(() => {
        this.getList()
        this.$message.success('批量上架成功')
      }).catch(() => {})
    },

    /** 批量下架 */
    handleBatchOffline() {
      const ids = this.multipleSelection.map(item => item.id)
      this.$confirm('是否确认批量下架选中的设备？', '系统提示', {
        type: 'warning'
      }).then(() => {
        return batchOperation({
          ids: ids,
          operationType: '下架'
        })
      }).then(() => {
        this.getList()
        this.$message.success('批量下架成功')
      }).catch(() => {})
    },

    /** 批量导入 */
    handleImport() {
      this.importFile = null
      this.importVisible = true
    },

    /** 文件变化 */
    handleFileChange(file) {
      this.importFile = file.raw
    },

    /** 提交导入 */
    submitImport() {
      if (!this.importFile) {
        this.$message.error('请选择要导入的文件')
        return
      }
      // 这里可以添加文件解析和导入逻辑
      this.$message.success('导入功能开发中...')
      this.importVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 20px;

  .filter-item {
    display: flex;
    align-items: center;
    margin-right: 15px;
    margin-bottom: 10px;

    label {
      margin-right: 8px;
      font-weight: 500;
      white-space: nowrap;
    }
  }
}

// 自定义按钮样式
.custom-primary-btn {
  background-color: rgb(144, 126, 179);
  border-color: rgb(144, 126, 179);
  color: #fff;

  &:hover,
  &:focus {
    background-color: rgb(134, 116, 169);
    border-color: rgb(134, 116, 169);
    color: #fff;
  }

  &:active {
    background-color: rgb(124, 106, 159);
    border-color: rgb(124, 106, 159);
  }

  &:disabled {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);
    opacity: 0.5;
  }
}

.operation-container {
  margin-bottom: 16px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 4px;
}

.equipment-detail {
  padding: 20px;
}

// 表格样式
.el-table {
  // 表头样式
  ::v-deep .el-table__header th {
    color: rgb(37, 37, 37);
    font-weight: 500;
    background-color: rgb(250, 250, 250);
    padding: 16px 0;
  }

  // 表格单元格样式 - 增加内边距
  ::v-deep .el-table__body td {
    padding: 16px 0;
  }

  // 表头和单元格居中对齐
  ::v-deep .el-table__header th,
  ::v-deep .el-table__body td {
    text-align: center;
  }

  // 操作列按钮样式
  ::v-deep .el-button--text {
    padding: 0 5px;
    margin: 0 2px;
    font-size: 14px;
    color: rgb(106, 91, 140);

    &:hover {
      color: rgb(96, 81, 130);
    }

    // 图标颜色
    i {
      color: rgb(106, 91, 140);
    }

    &:hover i {
      color: rgb(96, 81, 130);
    }
  }

  // 固定操作列样式
  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
    background-color: #fff;
  }

  // 固定列的表头也需要背景色
  ::v-deep .el-table__fixed-right .el-table__header th {
    background-color: rgb(250, 250, 250);
  }
}

// 表格滚动条样式
::v-deep .el-table__body-wrapper {
  overflow-x: auto;
}
</style>