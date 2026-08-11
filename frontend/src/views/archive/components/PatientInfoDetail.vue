<template>
  <div class="patient-info-detail">
    <!-- 顶部患者信息区域 -->
    <div class="patient-header">
      <div class="patient-basic-info">
        <div class="info-row">
          <span class="label">姓名：</span>
          <span class="value">{{ patientData.name }}</span>
          <span class="label gender-label">性别：</span>
          <span class="value">{{ patientData.gender }}</span>
          <span class="label age-label">年龄：</span>
          <span class="value">{{ displayAge }}岁</span>
        </div>
        <div class="info-row">
          <span class="label">手机号码：</span>
          <span class="value">{{ patientData.hiddenPhone || patientData.phone }}</span>
        </div>
        <div class="info-row">
          <span class="label">联系地址：</span>
          <span class="value">{{ fullAddress }}</span>
        </div>
        <div v-if="patientDetail && patientDetail.remark" class="info-row">
          <span class="label">备注说明：</span>
          <span class="value">{{ patientDetail.remark }}</span>
        </div>
      </div>

      <div class="action-area">
        <div class="enrollment-date">
          <span class="label">入组日期：</span>
          <el-date-picker
            v-model="enrollmentDate"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            size="small"
            :clearable="false"
          />
        </div>
        <el-button class="custom-primary-btn" size="small" @click="handleBack">返回</el-button>
      </div>
    </div>

    <!-- 分隔线 -->
    <el-divider />

    <!-- 标签页区域 -->
    <div class="tabs-container">
      <div class="tabs-header">
        <div
          class="tab-item"
          :class="{ active: activeTab === 'all' }"
          @click="switchTab('all')"
        >
          全部
        </div>
        <div class="tab-divider"></div>
        <div
          class="tab-item"
          :class="{ active: activeTab === 'upload' }"
          @click="switchTab('upload')"
        >
          上传报告
        </div>
        <div v-if="activeTab === 'upload'" class="upload-button">
          <el-button class="custom-primary-btn" size="small" @click="showUploadForm">上传</el-button>
        </div>
      </div>

      <!-- 标签页内容 -->
      <div class="tab-content">
        <!-- 全部标签页内容 -->
        <div v-if="activeTab === 'all'" class="all-content">
          <div class="empty-state">
            <i class="el-icon-document"></i>
            <p>暂无数据</p>
          </div>
        </div>

        <!-- 上传报告标签页内容 -->
        <div v-if="activeTab === 'upload'" class="upload-content">
          <!-- 上传表单 -->
          <div v-if="showForm" class="upload-form">
            <el-form :model="uploadForm" :rules="uploadRules" ref="uploadFormRef" label-width="100px">
              <!-- 资料名称 -->
              <el-form-item label="资料名称" prop="name">
                <el-input
                  v-model="uploadForm.name"
                  placeholder="请输入资料名称"
                  maxlength="15"
                  show-word-limit
                />
                <div class="form-tip">不超过15个字</div>
              </el-form-item>

              <!-- 文件上传 -->
              <el-form-item label="文件" prop="file">
                <el-upload
                  class="upload-area"
                  :action="uploadUrl"
                  :auto-upload="false"
                  :on-change="handleFileChange"
                  :on-remove="handleFileRemove"
                  :file-list="fileList"
                  :show-file-list="true"
                  accept=".jpg,.jpeg,.png"
                  list-type="picture"
                  drag
                  multiple
                >
                  <div class="upload-dragger">
                    <i class="el-icon-upload upload-icon"></i>
                    <div class="upload-text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="upload-hint">只能上传jpg/png文件，且不超过500kb</div>
                  </div>
                </el-upload>

                <!-- 文件预览区域 -->
                <div v-if="previewImage" class="preview-area">
                  <div class="preview-item">
                    <img :src="previewImage" alt="预览图片" class="preview-image" />
                    <div class="preview-info">
                      <p class="file-name">{{ uploadForm.file?.name || '已选择文件' }}</p>
                      <p class="file-size">{{ uploadForm.file ? formatFileSize(uploadForm.file.size) : '' }}</p>
                    </div>
                    <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeFile">
                      删除
                    </el-button>
                  </div>
                </div>
              </el-form-item>

              <!-- 链接地址 -->
              <el-form-item label="链接地址">
                <div class="link-input-group">
                  <el-input
                    v-model="uploadForm.link"
                    placeholder="请输入链接"
                  />
                  <el-button type="primary" circle icon="el-icon-plus" size="small" @click="addLink" />
                </div>
              </el-form-item>

              <!-- 备注 -->
              <el-form-item label="备注">
                <el-input
                  v-model="uploadForm.remark"
                  type="textarea"
                  placeholder="请输入备注"
                  :rows="4"
                />
              </el-form-item>
            </el-form>

            <!-- 操作按钮 -->
            <div class="form-actions">
              <el-button @click="resetForm">重置</el-button>
              <el-button class="custom-primary-btn" @click="saveForm">保存</el-button>
              <el-button class="custom-primary-btn" @click="cancelForm">返回</el-button>
            </div>
          </div>

          <!-- 上传列表 -->
          <div v-else class="upload-list">
            <div class="empty-state">
              <i class="el-icon-upload"></i>
              <p>暂无上传报告</p>
              <el-button class="custom-primary-btn" size="small" @click="showUploadForm">上传报告</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { FileUtils } from '@/utils/fileUtils'
import { getPatientDetail } from '@/api/patient'

export default {
  name: 'PatientInfoDetail',
  props: {
    patientData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      // 入组日期
      enrollmentDate: '',
      // 当前激活的标签页
      activeTab: 'all',
      // 是否显示上传表单
      showForm: false,
      // 患者详细信息
      patientDetail: null,
      // 上传表单数据
      uploadForm: {
        name: '',
        file: null,
        link: '',
        remark: ''
      },
      // 表单验证规则
      uploadRules: {
        name: [
          { required: true, message: '请输入资料名称', trigger: 'blur' },
          { max: 15, message: '资料名称不能超过15个字', trigger: 'blur' }
        ]
      },
      // 上传URL（使用本地处理）
      uploadUrl: '#',
      // 上传的链接列表
      linkList: [],
      // 文件列表
      fileList: [],
      // 预览图片URL
      previewImage: '',
      // 上传的文件数据
      uploadedFile: null
    }
  },
  computed: {
    // 完整地址 = 联系地址（省市区）+ 详细地址
    fullAddress() {
      if (!this.patientDetail) {
        return this.patientData.address || '暂无'
      }
      const parts = []
      if (this.patientDetail.contactProvinceCityDistrict) {
        parts.push(this.patientDetail.contactProvinceCityDistrict)
      }
      if (this.patientDetail.detailAddress) {
        parts.push(this.patientDetail.detailAddress)
      }
      return parts.length > 0 ? parts.join('') : '暂无'
    },
    displayAge() {
      const explicitAge = this.patientData?.age
      if (explicitAge !== null && explicitAge !== undefined && explicitAge !== '') {
        return explicitAge
      }
      return this.calculateAge(this.patientData?.birthDate)
    }
  },
  created() {
    // 设置默认入组日期为今天
    const today = new Date()
    this.enrollmentDate = this.formatDate(today)

    // 加载患者详细信息
    this.loadPatientDetail()
  },
  methods: {
    /** 计算年龄 */
    calculateAge(birthDate) {
      if (!birthDate) return ''
      const birth = new Date(birthDate)
      if (Number.isNaN(birth.getTime())) return ''
      const today = new Date()
      let age = today.getFullYear() - birth.getFullYear()
      const monthDiff = today.getMonth() - birth.getMonth()
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--
      }
      return age
    },
    /** 加载患者详细信息 */
    async loadPatientDetail() {
      try {
        if (!this.patientData.id) {
          console.error('患者ID不存在')
          return
        }
        const response = await getPatientDetail(this.patientData.id)
        if (response && response.data && response.data.detailInfo) {
          this.patientDetail = response.data.detailInfo
        }
      } catch (error) {
        console.error('加载患者详细信息失败:', error)
      }
    },

    // 返回上一页
    handleBack() {
      this.$emit('back')
    },

    // 切换标签页
    switchTab(tab) {
      this.activeTab = tab
      if (tab === 'all') {
        this.showForm = false
      }
    },

    // 显示上传表单
    showUploadForm() {
      this.showForm = true
    },

    // 文件选择变化处理
    handleFileChange(file, fileList) {
      console.log('文件选择:', file, fileList)

      // 使用工具类验证文件
      const validation = FileUtils.validateImageFile(file.raw, 500)
      if (!validation.isValid) {
        this.$message.error(validation.message)
        return false
      }

      // 保存文件到表单数据
      this.uploadForm.file = file.raw
      this.uploadedFile = file.raw
      this.fileList = [file]

      // 生成预览URL
      FileUtils.createPreviewUrl(file.raw)
        .then(url => {
          this.previewImage = url
          this.$message.success('文件选择成功')
        })
        .catch(error => {
          console.error('生成预览失败:', error)
          this.$message.error('生成预览失败')
        })
    },

    // 文件移除处理
    handleFileRemove(file, fileList) {
      console.log('文件移除:', file, fileList)
      this.uploadForm.file = null
      this.uploadedFile = null
      this.fileList = fileList
      this.previewImage = ''
    },

    // 移除文件
    removeFile() {
      this.uploadForm.file = null
      this.uploadedFile = null
      this.fileList = []
      this.previewImage = ''
    },

    // 格式化文件大小
    formatFileSize(bytes) {
      return FileUtils.formatFileSize(bytes)
    },

    // 添加链接
    addLink() {
      if (this.uploadForm.link.trim()) {
        this.linkList.push(this.uploadForm.link.trim())
        this.uploadForm.link = ''
      }
    },

    // 重置表单
    resetForm() {
      this.$refs.uploadFormRef.resetFields()
      this.uploadForm = {
        name: '',
        file: null,
        link: '',
        remark: ''
      }
      this.linkList = []
      this.fileList = []
      this.previewImage = ''
      this.uploadedFile = null
    },

    // 保存表单
    saveForm() {
      this.$refs.uploadFormRef.validate((valid) => {
        if (valid) {
          // 检查是否有文件
          if (!this.uploadForm.file) {
            this.$message.error('请上传文件')
            return
          }

          // 保存文件到本地
          this.saveFileToLocal()

          // 模拟保存其他数据
          this.$message.success('保存成功')
          this.showForm = false
          this.resetForm()
        }
      })
    },

    // 保存文件到本地
    saveFileToLocal() {
      if (!this.uploadedFile) return

      try {
        // 生成唯一文件名
        const patientId = this.patientData.id || 'unknown'
        const fileName = FileUtils.generateUniqueFileName(this.uploadedFile.name, patientId, 'report')

        // 使用工具类下载文件
        const result = FileUtils.downloadFile(this.uploadedFile, fileName)

        if (result.success) {
          console.log('文件已保存:', fileName)
          this.$message.success(`文件已保存为: ${fileName}`)
        } else {
          this.$message.error(result.message)
        }
      } catch (error) {
        console.error('保存文件失败:', error)
        this.$message.error('保存文件失败')
      }
    },

    // 取消表单
    cancelForm() {
      this.showForm = false
      this.resetForm()
    },

    // 格式化日期
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.patient-info-detail {
  .patient-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 20px 0;

    .patient-basic-info {
      flex: 1;

      .info-row {
        display: flex;
        align-items: center;
        margin-bottom: 12px;

        .label {
          color: #606266;
          font-weight: 500;
          margin-right: 8px;
          min-width: 80px;

          &.gender-label {
            margin-left: 20px;
          }

          &.age-label {
            margin-left: 20px;
          }
        }

        .value {
          color: #303133;
          font-weight: 500;
        }
      }
    }

    .action-area {
      display: flex;
      align-items: center;
      gap: 20px;

      .enrollment-date {
        display: flex;
        align-items: center;

        .label {
          color: #606266;
          font-weight: 500;
          margin-right: 8px;
        }
      }
    }
  }

  .tabs-container {
    .tabs-header {
      display: flex;
      align-items: center;
      position: relative;
      margin-bottom: 20px;

      .tab-item {
        padding: 10px 20px;
        cursor: pointer;
        color: #909399;
        font-size: 14px;
        transition: all 0.3s;

        &.active {
          color: #303133;
          font-weight: 500;
        }

        &:hover {
          color: #409eff;
        }
      }

      .tab-divider {
        width: 1px;
        height: 20px;
        background: #dcdfe6;
        margin: 0 10px;
      }

      .upload-button {
        margin-left: auto;
      }
    }

    .tab-content {
      .all-content,
      .upload-content {
        min-height: 400px;
      }

      .empty-state {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 300px;
        color: #909399;

        i {
          font-size: 48px;
          margin-bottom: 16px;
        }

        p {
          margin: 0;
          font-size: 14px;
        }
      }

      .upload-form {
        .form-tip {
          font-size: 12px;
          color: #909399;
          margin-top: 4px;
        }

        .upload-area {
          .upload-dragger {
            border: 2px dashed #dcdfe6;
            border-radius: 6px;
            width: 100%;
            height: 180px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s;

            &:hover {
              border-color: #409eff;
            }

            .upload-icon {
              font-size: 28px;
              color: #c0c4cc;
              margin-bottom: 16px;
            }

            .upload-text {
              color: #606266;
              font-size: 14px;
              margin-bottom: 8px;
            }

            .upload-hint {
              color: #909399;
              font-size: 12px;
              text-align: center;
              padding: 0 20px;
              line-height: 1.4;
            }
          }
        }

        .preview-area {
          margin-top: 15px;
          padding: 15px;
          border: 1px solid #e4e7ed;
          border-radius: 6px;
          background: #fafafa;

          .preview-item {
            display: flex;
            align-items: center;
            gap: 15px;

            .preview-image {
              width: 80px;
              height: 80px;
              object-fit: cover;
              border-radius: 4px;
              border: 1px solid #dcdfe6;
            }

            .preview-info {
              flex: 1;

              .file-name {
                margin: 0 0 5px 0;
                font-size: 14px;
                color: #303133;
                font-weight: 500;
              }

              .file-size {
                margin: 0;
                font-size: 12px;
                color: #909399;
              }
            }
          }
        }

        .link-input-group {
          display: flex;
          gap: 10px;

          .el-input {
            flex: 1;
          }
        }

        .form-actions {
          text-align: right;
          margin-top: 30px;

          .el-button {
            margin-left: 10px;
          }
        }
      }
    }
  }
}

// 自定义按钮样式
.custom-primary-btn {
  background-color: rgb(106, 91, 140);
  border-color: rgb(106, 91, 140);
  color: #fff;

  &:hover,
  &:focus {
    background-color: rgb(96, 81, 130);
    border-color: rgb(96, 81, 130);
    color: #fff;
  }

  &:active {
    background-color: rgb(86, 71, 120);
    border-color: rgb(86, 71, 120);
  }
}

// 对话框样式
::v-deep .patient-detail-dialog {
  .el-dialog__header {
    padding: 0;
  }

  .el-dialog__body {
    padding: 0;
  }
}
</style>
