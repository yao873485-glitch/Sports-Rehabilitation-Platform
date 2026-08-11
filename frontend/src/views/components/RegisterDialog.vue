<template>
  <el-dialog
    title="注册申请"
    :visible.sync="dialogVisible"
    width="550px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="registerForm"
      :model="registerForm"
      :rules="registerRules"
      label-width="120px"
      class="register-form"
    >
      <!-- 账户用户名 -->
      <el-form-item label="账户用户名" prop="username" required>
        <el-input
          v-model="registerForm.username"
          placeholder="请输入"
          clearable
        />
      </el-form-item>

      <!-- 性别 -->
      <el-form-item label="性别" prop="gender" required>
        <el-select v-model="registerForm.gender" placeholder="请选择" style="width: 100%">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>

      <!-- 出生日期 -->
      <el-form-item label="出生日期" prop="birthDate" required>
        <el-date-picker
          v-model="registerForm.birthDate"
          type="date"
          placeholder="请选择出生日期"
          value-format="yyyy-MM-dd"
          style="width: 100%"
          prefix-icon="el-icon-date"
        />
      </el-form-item>

      <!-- 联系电话 -->
      <el-form-item label="联系电话" prop="phone" required>
        <el-input
          v-model="registerForm.phone"
          placeholder="请输入联系电话"
          clearable
        />
      </el-form-item>

      <!-- 密码 -->
      <el-form-item label="密码" prop="password" required>
        <el-input
          v-model="registerForm.password"
          type="password"
          placeholder="请输入密码"
          clearable
          show-password
        />
      </el-form-item>

      <!-- 所在平台 -->
      <el-form-item label="所在平台">
        <el-input
          v-model="registerForm.platform"
          disabled
          placeholder="体医融合平台"
        />
      </el-form-item>

      <!-- 所在机构 -->
      <el-form-item label="所在机构" prop="organization" required>
        <el-select v-model="registerForm.organization" placeholder="请选择所在机构" style="width: 100%">
          <el-option label="北京体育医院" value="北京体育医院" />
          <el-option label="上海康复中心" value="上海康复中心" />
          <el-option label="广州运动医学研究所" value="广州运动医学研究所" />
          <el-option label="深圳体医融合中心" value="深圳体医融合中心" />
        </el-select>
      </el-form-item>

      <!-- 申请角色 -->
      <el-form-item label="申请角色" prop="applyRole" required>
        <el-select v-model="registerForm.applyRole" placeholder="请选择申请角色" style="width: 100%">
          <el-option label="医生" value="医生" />
          <el-option label="康复师" value="康复师" />
          <el-option label="教练" value="教练" />
        </el-select>
      </el-form-item>

      <!-- 证件上传 -->
      <el-form-item label="证件上传" prop="certificateUrl" required>
        <div class="upload-section">
          <p class="upload-tip">运动处方培训合格证书</p>
          <el-upload
            class="certificate-uploader"
            action="/api/file/upload/certificate"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :headers="uploadHeaders"
          >
            <div v-if="registerForm.certificateUrl" class="certificate-preview">
              <img :src="registerForm.certificateUrl" alt="证件预览">
              <div class="preview-overlay">
                <i class="el-icon-zoom-in" @click.stop="previewImage" />
                <i class="el-icon-delete" @click.stop="removeImage" />
              </div>
            </div>
            <div v-else class="upload-placeholder">
              <i class="el-icon-upload" />
              <p>上传证件</p>
            </div>
          </el-upload>
          <p class="upload-limit">只能上传jpg/png文件，且不超过5MB</p>
        </div>
      </el-form-item>
    </el-form>

    <!-- 底部按钮 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        确认提交
      </el-button>
    </div>

    <!-- 图片预览对话框 -->
    <el-dialog
      :visible.sync="imagePreviewVisible"
      width="60%"
      append-to-body
    >
      <img :src="registerForm.certificateUrl" alt="证件预览" style="width: 100%">
    </el-dialog>
  </el-dialog>
</template>

<script>
import { register } from '@/api/auth'
import { getToken } from '@/utils/auth'

export default {
  name: 'RegisterDialog',

  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },

  data() {
    return {
      registerForm: {
        username: '',
        gender: '',
        birthDate: '',
        phone: '',
        password: '',
        platform: '体医融合平台',
        organization: '',
        applyRole: '',
        certificateUrl: ''
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '用户名长度在2-20个字符', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        birthDate: [
          { required: true, message: '请选择出生日期', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        organization: [
          { required: true, message: '请选择所在机构', trigger: 'change' }
        ],
        applyRole: [
          { required: true, message: '请选择申请角色', trigger: 'change' }
        ],
        certificateUrl: [
          { required: true, message: '请上传证件', trigger: 'change' }
        ]
      },
      submitting: false,
      imagePreviewVisible: false,
      uploadHeaders: {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },

  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },

  methods: {
    /**
     * 上传前校验
     */
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isJPG) {
        this.$message.error('只能上传jpg或png格式的图片')
        return false
      }
      if (!isLt5M) {
        this.$message.error('图片大小不能超过5MB')
        return false
      }
      return true
    },

    /**
     * 上传成功回调
     */
    handleUploadSuccess(response) {
      if (response.code === 200) {
        this.registerForm.certificateUrl = response.data.url
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },

    /**
     * 上传失败回调
     */
    handleUploadError() {
      this.$message.error('上传失败，请重试')
    },

    /**
     * 预览图片
     */
    previewImage() {
      this.imagePreviewVisible = true
    },

    /**
     * 删除图片
     */
    removeImage() {
      this.$confirm('确定要删除该证件吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.registerForm.certificateUrl = ''
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /**
     * 提交注册申请
     */
    handleSubmit() {
      this.$refs.registerForm.validate(async(valid) => {
        if (!valid) {
          return
        }

        this.submitting = true
        try {
          const response = await register(this.registerForm)

          if (response.code === 200) {
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(response.message || '注册失败')
          }
        } catch (error) {
          console.error('注册失败', error)
          this.$message.error(error.response?.data?.message || '注册失败，请稍后重试')
        } finally {
          this.submitting = false
        }
      })
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.$refs.registerForm.resetFields()
      this.registerForm.certificateUrl = ''
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped lang="scss">
.register-form {
  ::v-deep .el-form-item__label {
    font-weight: 500;
    color: #606266;

    &::before {
      color: #f56c6c;
    }
  }

  ::v-deep .el-input__inner,
  ::v-deep .el-textarea__inner {
    border-radius: 4px;
    padding-left: 15px;
    padding-right: 15px;

    &:focus {
      border-color: #9370db;
    }
  }

  ::v-deep .el-input__prefix,
  ::v-deep .el-input__suffix {
    display: flex;
    align-items: center;
    padding: 0 8px;
  }

  ::v-deep .el-date-editor {
    width: 100%;

    .el-input__inner {
      padding-left: 45px;
    }

    .el-input__prefix {
      left: 8px;
    }
  }

  ::v-deep .el-select {
    width: 100%;
  }

  .upload-section {
    .upload-tip {
      margin: 0 0 10px 0;
      font-size: 14px;
      color: #606266;
    }

    .certificate-uploader {
      ::v-deep .el-upload {
        width: 100%;
        border: 2px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        overflow: hidden;
        transition: all 0.3s;

        &:hover {
          border-color: #9370db;
        }
      }

      .upload-placeholder {
        padding: 40px 0;
        text-align: center;
        color: #909399;

        i {
          font-size: 48px;
          margin-bottom: 10px;
        }

        p {
          margin: 0;
          font-size: 14px;
        }
      }

      .certificate-preview {
        position: relative;
        width: 100%;

        img {
          width: 100%;
          display: block;
        }

        .preview-overlay {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.5);
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 20px;
          opacity: 0;
          transition: opacity 0.3s;

          i {
            font-size: 24px;
            color: white;
            cursor: pointer;

            &:hover {
              color: #9370db;
            }
          }
        }

        &:hover .preview-overlay {
          opacity: 1;
        }
      }
    }

    .upload-limit {
      margin: 8px 0 0 0;
      font-size: 12px;
      color: #909399;
    }
  }
}

.dialog-footer {
  text-align: right;

  ::v-deep .el-button--primary {
    background: linear-gradient(135deg, #9370db 0%, #7b68ee 100%);
    border: none;

    &:hover {
      background: linear-gradient(135deg, #7b68ee 0%, #9370db 100%);
    }
  }
}
</style>
