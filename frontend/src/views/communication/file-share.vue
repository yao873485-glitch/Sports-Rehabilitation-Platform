<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <div class="filter-item">
        <el-input
          v-model="queryParams.patientName"
          placeholder="患者姓名"
          style="width: 150px;"
          clearable
        />
      </div>

      <div class="filter-item">
        <el-input
          v-model="queryParams.fileName"
          placeholder="文件名"
          style="width: 200px;"
          clearable
        />
      </div>

      <div class="filter-item">
        <el-select v-model="queryParams.fileType" placeholder="文件类型" clearable style="width: 120px;">
          <el-option label="图片" value="image" />
          <el-option label="PDF" value="pdf" />
          <el-option label="文档" value="doc" />
          <el-option label="表格" value="excel" />
          <el-option label="其他" value="other" />
        </el-select>
      </div>

      <div class="filter-item">
        <el-date-picker
          v-model="queryParams.uploadDateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          :default-time="['00:00:00', '23:59:59']"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 340px;"
        />
      </div>

      <div class="filter-item">
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="operation-container">
      <el-upload
        :action="uploadUrl"
        :data="uploadData"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :show-file-list="false"
        style="display: inline-block;"
      >
        <el-button type="primary" icon="el-icon-upload">上传文件</el-button>
      </el-upload>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="fileList"
      border
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column prop="fileName" label="文件名" width="250" show-overflow-tooltip />
      
      <el-table-column prop="patientName" label="患者姓名" width="120" />
      
      <el-table-column prop="fileType" label="文件类型" width="100">
        <template slot-scope="scope">
          <el-tag :type="getFileTypeColor(scope.row.fileType)" size="mini">
            {{ getFileTypeText(scope.row.fileType) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="uploadBy" label="上传人" width="120" />
      
      <el-table-column prop="uploadTime" label="上传时间" width="160" />
      
      <el-table-column prop="downloadCount" label="下载次数" width="100" />
      
      <el-table-column
        label="操作"
        width="150"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleDownload(scope.row)"
          >
            下载
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handlePreview(scope.row)"
          >
            预览
          </el-button>
          <el-button
            size="mini"
            type="text"
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
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 上传对话框 -->
    <el-dialog
      title="上传文件"
      :visible.sync="uploadVisible"
      width="500px"
      append-to-body
    >
      <el-form :model="uploadForm" :rules="uploadRules" ref="uploadForm" label-width="80px">
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="uploadForm.patientId" placeholder="请选择患者" style="width: 100%;">
            <el-option 
              v-for="patient in patientOptions" 
              :key="patient.id" 
              :label="patient.name" 
              :value="patient.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文件" prop="file">
          <el-upload
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="uploadFileList"
            :limit="1"
            action=""
          >
            <el-button size="small" type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="uploadVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleConfirmUpload">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getFileList, uploadFile, downloadFile, deleteFile } from '@/api/file-share'

export default {
  name: 'FileShare',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 文件表格数据
      fileList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientName: '',
        fileName: '',
        fileType: '',
        uploadDateRange: null
      },
      // 上传
      uploadUrl: '/api/file-share/upload',
      uploadData: {
        patientId: 1,
        patientName: '张三'
      },
      uploadVisible: false,
      uploadForm: {
        patientId: null,
        file: null
      },
      uploadRules: {
        patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
        file: [{ required: true, message: '请选择文件', trigger: 'change' }]
      },
      uploadFileList: [],
      // 患者选项
      patientOptions: [
        { id: 1, name: '张三' },
        { id: 2, name: '李四' },
        { id: 3, name: '王五' }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询文件列表 */
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      
      // 处理上传时间范围
      if (params.uploadDateRange && params.uploadDateRange.length === 2) {
        params.uploadStartTime = params.uploadDateRange[0]
        params.uploadEndTime = params.uploadDateRange[1]
      }
      delete params.uploadDateRange

      getFileList(params).then(response => {
        this.fileList = response.data.records
        this.total = response.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
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
        patientName: '',
        fileName: '',
        fileType: '',
        uploadDateRange: null
      }
      this.handleQuery()
    },

    /** 上传前检查 */
    beforeUpload(file) {
      const isLt50M = file.size / 1024 / 1024 < 50
      if (!isLt50M) {
        this.$message.error('上传文件大小不能超过 50MB!')
        return false
      }
      return true
    },

    /** 上传成功 */
    handleUploadSuccess(response) {
      this.$message.success('文件上传成功')
      this.getList()
    },

    /** 上传失败 */
    handleUploadError() {
      this.$message.error('文件上传失败')
    },

    /** 文件改变 */
    handleFileChange(file) {
      this.uploadForm.file = file.raw
    },

    /** 确认上传 */
    handleConfirmUpload() {
      this.$refs.uploadForm.validate((valid) => {
        if (valid) {
          const formData = new FormData()
          formData.append('file', this.uploadForm.file)
          formData.append('patientId', this.uploadForm.patientId)
          
          const patient = this.patientOptions.find(p => p.id === this.uploadForm.patientId)
          formData.append('patientName', patient ? patient.name : '')
          
          uploadFile(formData).then(() => {
            this.$message.success('文件上传成功')
            this.uploadVisible = false
            this.uploadForm = { patientId: null, file: null }
            this.uploadFileList = []
            this.getList()
          })
        }
      })
    },
    
    /** 下载文件 */
    handleDownload(row) {
      downloadFile(row.id).then(response => {
        // 创建下载链接
        const url = response.data
        const link = document.createElement('a')
        link.href = url
        link.download = row.fileName
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        
        this.$message.success('文件下载成功')
        this.getList() // 刷新下载次数
      })
    },
    
    /** 预览文件 */
    handlePreview(row) {
      if (row.fileType === 'image') {
        // 图片预览 - 使用正确的URL前缀
        const imageUrl = row.fileUrl.startsWith('/uploads/') ? row.fileUrl : `/uploads/${row.fileUrl}`
        this.$alert(`<img src="${imageUrl}" style="max-width: 100%; max-height: 400px;">`, '图片预览', {
          dangerouslyUseHTMLString: true
        })
      } else {
        this.$message.info('该文件类型不支持预览')
      }
    },
    
    /** 删除文件 */
    handleDelete(row) {
      this.$confirm('确认删除该文件吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFile(row.id).then(() => {
          this.$message.success('文件删除成功')
          this.getList()
        })
      })
    },

    /** 获取文件类型颜色 */
    getFileTypeColor(type) {
      const colorMap = {
        'image': 'success',
        'pdf': 'danger',
        'doc': 'primary',
        'excel': 'warning',
        'other': 'info'
      }
      return colorMap[type] || 'info'
    },

    /** 获取文件类型文本 */
    getFileTypeText(type) {
      const textMap = {
        'image': '图片',
        'pdf': 'PDF',
        'doc': '文档',
        'excel': '表格',
        'other': '其他'
      }
      return textMap[type] || '其他'
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  .filter-item {
    margin-right: 10px;
    margin-bottom: 10px;
  }
}

.operation-container {
  margin-bottom: 20px;
}

.el-table {
  ::v-deep .fixed-width {
    .el-button--mini {
      padding: 2px 4px;
      margin-left: 2px;
    }
  }
}
</style>
