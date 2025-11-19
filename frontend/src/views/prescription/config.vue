<template>
  <div class="app-container">
    <!-- 筛选和查询功能 -->
    <div class="filter-container">
      <div class="filter-item">
        <label>病种类型：</label>
        <el-select
          v-model="queryParams.diseaseType"
          placeholder="请选择病种类型"
          style="width: 200px;"
          clearable
        >
          <el-option label="全部" value="全部" />
          <el-option
            v-for="type in diseaseTypes"
            :key="type"
            :label="type"
            :value="type"
          />
        </el-select>
      </div>

      <div class="filter-item">
        <label>状态：</label>
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          style="width: 150px;"
          clearable
        >
          <el-option label="全部" value="全部" />
          <el-option label="上架" value="上架" />
          <el-option label="下架" value="下架" />
        </el-select>
      </div>

      <div class="filter-item">
        <label>运动方式：</label>
        <el-input
          v-model="queryParams.exerciseMethod"
          placeholder="请输入运动方式"
          style="width: 200px;"
          clearable
        />
      </div>

      <div class="filter-item">
        <label>运动类型：</label>
        <el-select
          v-model="queryParams.exerciseType"
          placeholder="请选择运动类型"
          style="width: 150px;"
          clearable
        >
          <el-option label="全部" value="全部" />
          <el-option label="有氧运动" value="有氧运动" />
          <el-option label="抗阻训练" value="抗阻训练" />
          <el-option label="柔韧性训练" value="柔韧性训练" />
          <el-option label="平衡训练" value="平衡训练" />
          <el-option label="其他" value="其他" />
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

    <!-- 运动处方列表展示 -->
    <el-table
      v-loading="loading"
      :data="prescriptionList"
      border
      stripe
      style="width: 100%; margin-top: 16px;"
      :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" header-align="center" />
      <el-table-column prop="diseaseType" label="病种类型" width="180" align="center" header-align="center" />
      <el-table-column prop="exerciseMethod" label="运动方式" width="180" align="center" header-align="center" />
      <el-table-column prop="exerciseType" label="运动类型" width="150" align="center" header-align="center" />

      <el-table-column prop="actionDiagram" label="动作图式" width="180" align="center" header-align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.actionDiagram" class="action-diagram-cell">
            <el-image
              v-if="scope.row.actionDiagram.includes('http')"
              :src="scope.row.actionDiagram"
              :preview-src-list="[scope.row.actionDiagram]"
              class="table-image"
              fit="cover"
            >
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
            <span v-else>{{ scope.row.actionDiagram }}</span>
          </div>
          <span v-else>暂无</span>
        </template>
      </el-table-column>

      <el-table-column prop="precautions" label="注意事项" width="200" align="center" header-align="center" />

      <el-table-column prop="progressionLevel" label="运动进阶" width="150" align="center" header-align="center" />

      <el-table-column prop="exerciseScene" label="运动场景" width="150" align="center" header-align="center" />

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
        width="180"
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

    <!-- 运动处方详情对话框 -->
    <el-dialog
      title="运动处方详情"
      :visible.sync="detailVisible"
      width="800px"
      append-to-body
    >
      <div v-if="currentPrescription" class="prescription-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="病种类型">{{ currentPrescription.diseaseType }}</el-descriptions-item>
          <el-descriptions-item label="运动方式">{{ currentPrescription.exerciseMethod }}</el-descriptions-item>
          <el-descriptions-item label="运动类型">{{ currentPrescription.exerciseType }}</el-descriptions-item>
          <el-descriptions-item label="运动进阶">{{ currentPrescription.progressionLevel || '-' }}</el-descriptions-item>
          <el-descriptions-item label="运动场景">{{ currentPrescription.exerciseScene || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentPrescription.status === '上架' ? 'success' : 'danger'">
              {{ currentPrescription.status }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="动作图式" :span="2">
            <div v-if="currentPrescription.actionDiagram">
              <el-image
                v-if="currentPrescription.actionDiagram.includes('http')"
                :src="currentPrescription.actionDiagram"
                :preview-src-list="[currentPrescription.actionDiagram]"
                class="detail-image"
                fit="contain"
              >
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <span v-else>{{ currentPrescription.actionDiagram }}</span>
            </div>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="注意事项" :span="2">
            <div class="precautions-content">{{ currentPrescription.precautions || '-' }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="上次操作时间">{{ currentPrescription.lastOperationTime }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentPrescription.createdTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 新增/编辑运动处方对话框 -->
    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="600px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="病种类型" prop="diseaseType">
          <el-input v-model="form.diseaseType" placeholder="请输入病种类型" />
        </el-form-item>
        <el-form-item label="运动方式" prop="exerciseMethod">
          <el-input v-model="form.exerciseMethod" placeholder="请输入运动方式" />
        </el-form-item>
        <el-form-item label="运动类型" prop="exerciseType">
          <el-select v-model="form.exerciseType" placeholder="请选择运动类型" style="width: 100%;">
            <el-option label="有氧运动" value="有氧运动" />
            <el-option label="抗阻训练" value="抗阻训练" />
            <el-option label="柔韧性训练" value="柔韧性训练" />
            <el-option label="平衡训练" value="平衡训练" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="动作图式" prop="actionDiagram">
          <el-upload
            class="action-diagram-uploader"
            action="/api/file/upload/exercise-image"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            accept="image/*"
          >
            <img v-if="form.actionDiagram" :src="form.actionDiagram" class="action-diagram-preview">
            <i v-else class="el-icon-plus action-diagram-uploader-icon"></i>
          </el-upload>
          <div class="upload-tips">支持jpg、png格式，大小不超过10MB</div>
          <el-button v-if="form.actionDiagram" size="small" type="danger" @click="handleRemoveImage">删除图片</el-button>
        </el-form-item>
        <el-form-item label="注意事项" prop="precautions">
          <el-input
            v-model="form.precautions"
            type="textarea"
            :rows="3"
            placeholder="请输入注意事项"
          />
        </el-form-item>
        <el-form-item label="运动进阶" prop="progressionLevel">
          <el-input v-model="form.progressionLevel" placeholder="请输入运动进阶（如初级、中级、高级）" />
        </el-form-item>
        <el-form-item label="运动场景" prop="exerciseScene">
          <el-input v-model="form.exerciseScene" placeholder="请输入运动场景（如室内、户外、健身房等）" />
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
      title="批量导入运动处方"
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
  getExercisePrescriptionList,
  getExercisePrescriptionDetail,
  addExercisePrescription,
  updateExercisePrescription,
  getDiseaseTypes,
  batchOperation,
  importExercisePrescription
} from '@/api/exercise-prescription'

export default {
  name: 'PrescriptionConfig',
  data() {
    return {
      // 加载状态
      loading: true,
      // 总条数
      total: 0,
      // 运动处方表格数据
      prescriptionList: [],
      // 病种类型选项
      diseaseTypes: [],
      // 多选数据
      multipleSelection: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        diseaseType: '全部',
        status: '全部',
        exerciseMethod: '',
        exerciseType: '全部'
      },
      // 详情弹窗
      detailVisible: false,
      currentPrescription: null,
      // 表单弹窗
      formVisible: false,
      formTitle: '',
      form: {},
      // 导入弹窗
      importVisible: false,
      importFile: null,
      // 表单校验
      rules: {
        diseaseType: [
          { required: true, message: '病种类型不能为空', trigger: 'blur' }
        ],
        exerciseMethod: [
          { required: true, message: '运动方式不能为空', trigger: 'blur' }
        ],
        exerciseType: [
          { required: true, message: '运动类型不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDiseaseTypes()
  },
  methods: {
    /** 查询运动处方列表 */
    getList() {
      this.loading = true
      getExercisePrescriptionList(this.queryParams).then(response => {
        this.prescriptionList = response.data.records
        this.total = response.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /** 获取病种类型 */
    getDiseaseTypes() {
      getDiseaseTypes().then(response => {
        this.diseaseTypes = response.data
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
        diseaseType: '全部',
        status: '全部',
        exerciseMethod: '',
        exerciseType: '全部'
      }
      this.handleQuery()
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    /** 查看操作 */
    handleView(row) {
      getExercisePrescriptionDetail(row.id).then(response => {
        this.currentPrescription = response.data
        this.detailVisible = true
      })
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.formTitle = '新增运动处方'
      this.form = {
        id: null,
        diseaseType: '',
        exerciseMethod: '',
        exerciseType: '',
        actionDiagram: '',
        precautions: '',
        progressionLevel: '',
        exerciseScene: '',
        status: '下架'
      }
      this.formVisible = true
    },

    /** 修改按钮操作 */
    handleEdit(row) {
      this.formTitle = '编辑运动处方'
      getExercisePrescriptionDetail(row.id).then(response => {
        this.form = response.data
        this.formVisible = true
      })
    },

    /** 提交表单 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateExercisePrescription(this.form.id, this.form).then(() => {
              this.$message.success('修改成功')
              this.formVisible = false
              this.getList()
            })
          } else {
            addExercisePrescription(this.form).then(() => {
              this.$message.success('新增成功')
              this.formVisible = false
              this.getList()
            })
          }
        }
      })
    },

    /** 批量上架 */
    handleBatchOnline() {
      const ids = this.multipleSelection.map(item => item.id)
      this.$confirm('是否确认批量上架选中的运动处方？', '系统提示', {
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
      this.$confirm('是否确认批量下架选中的运动处方？', '系统提示', {
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
    },

    /** 上传前校验 */
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isImage) {
        this.$message.error('只能上传图片文件！')
        return false
      }
      if (!isLt10M) {
        this.$message.error('图片大小不能超过 10MB！')
        return false
      }
      return true
    },

    /** 上传成功回调 */
    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        this.form.actionDiagram = response.data.url
        this.$message.success('图片上传成功')
      } else {
        this.$message.error(response.message || '图片上传失败')
      }
    },

    /** 上传失败回调 */
    handleUploadError(err, file) {
      console.error('上传失败:', err)
      this.$message.error('图片上传失败，请重试')
    },

    /** 删除图片 */
    handleRemoveImage() {
      this.$confirm('确认删除该图片？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 可以调用后端接口删除OSS中的图片
        // 这里暂时只清空表单中的URL
        this.form.actionDiagram = ''
        this.$message.success('删除成功')
      }).catch(() => {})
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

.prescription-detail {
  padding: 20px;
}

.action-diagram {
  max-width: 160px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.precautions-content {
  max-height: 100px;
  overflow-y: auto;
  line-height: 1.5;
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

// 图片上传组件样式
.action-diagram-uploader {
  ::v-deep .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 178px;
    height: 178px;
    display: flex;
    align-items: center;
    justify-content: center;

    &:hover {
      border-color: #409EFF;
    }
  }
}

.action-diagram-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.action-diagram-preview {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.upload-tips {
  font-size: 12px;
  color: #606266;
  margin-top: 5px;
  margin-bottom: 10px;
}

// 表格中的图片样式
.action-diagram-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.table-image {
  width: 60px;
  height: 60px;
  cursor: pointer;
  border-radius: 4px;

  ::v-deep img {
    object-fit: cover;
  }
}

// 详情中的图片样式
.detail-image {
  max-width: 300px;
  max-height: 300px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;

  ::v-deep img {
    object-fit: contain;
  }
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

// 固定操作列样式
::v-deep .el-table__fixed-right {
  background: #fff;
}

// 表格滚动条样式
::v-deep .el-table__body-wrapper {
  overflow-x: auto;
}
</style>