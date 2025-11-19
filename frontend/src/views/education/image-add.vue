<template>
  <div class="image-add-container">
    <div class="page-header">
      <el-button icon="el-icon-back" @click="handleBack" size="small">返回</el-button>
      <span class="page-title">{{ pageTitle }}</span>
    </div>

    <div class="form-container">
      <el-form
        :model="contentForm"
        :rules="formRules"
        ref="contentForm"
        label-width="140px"
        size="small"
      >
        <el-form-item label="内容类型" prop="contentType" required>
          <el-select v-model="contentForm.contentType" placeholder="请选择内容类型" style="width: 300px;">
            <el-option label="文章" value="文章" />
          </el-select>
        </el-form-item>

        <el-form-item label="文章标题" prop="contentTitle" required>
          <el-input
            v-model="contentForm.contentTitle"
            placeholder="请输入标题，限50个字符"
            maxlength="50"
            show-word-limit
            style="width: 600px;"
          />
          <div class="char-count">{{ contentForm.contentTitle.length }}/50</div>
        </el-form-item>

        <el-form-item label="作者" prop="author">
          <el-input
            v-model="contentForm.author"
            placeholder="请输入作者"
            style="width: 200px;"
          />
          <el-select
            v-model="contentForm.authorType"
            placeholder="请选择"
            style="width: 150px; margin-left: 10px;"
          >
            <el-option label="平台运营" value="平台运营" />
          </el-select>
          <el-button type="text" style="margin-left: 10px;">选择</el-button>
        </el-form-item>

        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker
            v-model="contentForm.publishTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 300px;"
          />
        </el-form-item>

        <el-form-item label="是否支持游客模式" prop="supportGameMode" required>
          <el-radio-group v-model="contentForm.supportGameMode">
            <el-radio :label="1">支持</el-radio>
            <el-radio :label="0">不支持</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="是否原创" prop="isOriginal" required>
          <el-radio-group v-model="contentForm.isOriginal">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="文章描述" prop="articleDescription" required>
          <el-input
            v-model="contentForm.articleDescription"
            type="textarea"
            placeholder="请输入描述，用于文章摘要展示"
            :rows="4"
            maxlength="300"
            show-word-limit
            style="width: 600px;"
          />
          <div class="char-count">{{ contentForm.articleDescription.length }}/300</div>
        </el-form-item>

        <el-form-item label="封面图" prop="coverImage" required>
          <div class="upload-container">
            <el-upload
              class="cover-uploader"
              action="/api/file/upload/exercise-image"
              :show-file-list="false"
              :on-success="handleCoverUploadSuccess"
              :on-error="handleCoverUploadError"
              :before-upload="beforeCoverUpload"
              accept="image/*"
            >
              <div class="upload-box" v-if="!contentForm.coverImage">
                <i class="el-icon-plus"></i>
              </div>
              <img v-else :src="contentForm.coverImage" class="cover-image" />
            </el-upload>
            <div class="upload-tips">
              封面应为JPG/PNG格式，最大3张图片，图片不超过370*225，大小不超过10MB
            </div>
            <el-button v-if="contentForm.coverImage" size="small" type="danger" @click="handleRemoveCoverImage" style="margin-top: 10px;">删除图片</el-button>
          </div>
        </el-form-item>

        <el-form-item label="文章来源" prop="contentSource" required>
          <el-radio-group v-model="contentForm.contentSource">
            <el-radio label="系统录入">系统录入</el-radio>
            <el-radio label="外链">外链</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="文章内容" prop="articleContent" required v-if="contentForm.contentSource === '系统录入'">
          <div class="editor-container">
            <div class="editor-toolbar">
              <el-button-group>
                <el-button size="mini" icon="el-icon-picture">插入图片</el-button>
                <el-button size="mini" icon="el-icon-link">插入链接</el-button>
              </el-button-group>
              <select class="paragraph-select">
                <option value="Paragraph">Paragraph</option>
              </select>
            </div>
            <el-input
              v-model="contentForm.articleContent"
              type="textarea"
              placeholder="请输入文章内容"
              :rows="10"
              style="width: 600px;"
            />
          </div>
        </el-form-item>

        <el-form-item label="所属模块" prop="moduleCategory" required>
          <el-radio-group v-model="contentForm.moduleCategory">
            <el-radio label="医教">医教</el-radio>
            <el-radio label="患教">患教</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="点赞基数">
          <el-input
            v-model.number="contentForm.likeCountBase"
            placeholder="0"
            type="number"
            style="width: 150px;"
          >
            <template slot="append">人</template>
          </el-input>
        </el-form-item>

        <el-form-item label="阅读基数">
          <el-input
            v-model.number="contentForm.viewCountBase"
            placeholder="0"
            type="number"
            style="width: 150px;"
          >
            <template slot="append">人</template>
          </el-input>
        </el-form-item>

        <el-form-item class="submit-buttons">
          <el-button @click="handleBack">返回</el-button>
          <el-button type="primary" @click="handleSubmit">返回并保存</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { saveImageContent, getContentDetail } from '@/api/education'

export default {
  name: 'ImageAdd',
  data() {
    return {
      // 页面模式: add-新增, edit-编辑, copy-复制
      mode: 'add',
      // 内容ID（编辑或复制时使用）
      contentId: null,
      // 表单数据
      contentForm: {
        contentType: '文章',
        contentTitle: '',
        author: '系统管理员',
        authorType: '平台运营',
        tags: '',
        publishTime: null,
        supportGameMode: 1,
        isOriginal: 1,
        articleDescription: '',
        coverImage: '',
        contentSource: '系统录入',
        articleContent: '',
        externalLink: '',
        moduleCategory: '医教',
        likeCountBase: 0,
        viewCountBase: 0,
        categorySection: '医教'
      },
      // 表单验证规则
      formRules: {
        contentType: [
          { required: true, message: '请选择内容类型', trigger: 'change' }
        ],
        contentTitle: [
          { required: true, message: '请输入文章标题', trigger: 'blur' },
          { max: 50, message: '标题长度不能超过50个字符', trigger: 'blur' }
        ],
        author: [
          { required: true, message: '请输入作者', trigger: 'blur' }
        ],
        articleDescription: [
          { max: 300, message: '描述长度不能超过300个字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    // 页面标题
    pageTitle() {
      if (this.mode === 'edit') {
        return '编辑内容'
      } else if (this.mode === 'copy') {
        return '复制内容'
      }
      return '新增内容'
    }
  },
  created() {
    // 检查路由参数
    const { id, mode } = this.$route.query
    if (id && mode) {
      this.mode = mode
      this.contentId = id
      this.loadContentData(id)
    }
  },
  methods: {
    // 加载内容数据
    async loadContentData(id) {
      try {
        const response = await getContentDetail(id)
        const data = response.data
        // 填充表单数据
        this.contentForm = {
          id: this.mode === 'edit' ? data.id : undefined, // 复制模式不需要id
          contentType: data.contentType || '文章',
          contentTitle: data.contentTitle || '',
          author: data.author || '系统管理员',
          authorType: data.authorType || '平台运营',
          tags: data.tags || '',
          publishTime: data.publishTime || null,
          supportGameMode: data.supportGameMode !== undefined ? data.supportGameMode : 1,
          isOriginal: data.isOriginal !== undefined ? data.isOriginal : 1,
          articleDescription: data.articleDescription || '',
          coverImage: data.coverImage || '',
          contentSource: data.contentSource || '系统录入',
          articleContent: data.articleContent || '',
          externalLink: data.externalLink || '',
          moduleCategory: data.moduleCategory || '医教',
          likeCountBase: data.likeCountBase || 0,
          viewCountBase: data.viewCountBase || 0,
          categorySection: data.categorySection || '医教'
        }
      } catch (error) {
        this.$message.error('加载内容数据失败')
        console.error('加载内容数据失败:', error)
      }
    },
    // 返回
    handleBack() {
      this.$router.push('/education/image-library')
    },
    // 封面图上传成功
    handleCoverUploadSuccess(response) {
      if (response.code === 200) {
        this.contentForm.coverImage = response.data.url
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    // 封面图上传前验证
    beforeCoverUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('图片大小不能超过 10MB!')
        return false
      }
      return true
    },
    // 封面图上传失败
    handleCoverUploadError(err) {
      console.error('上传失败:', err)
      this.$message.error('图片上传失败,请重试')
    },
    // 删除封面图
    handleRemoveCoverImage() {
      this.$confirm('确认删除该图片?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.contentForm.coverImage = ''
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    // 提交表单
    handleSubmit() {
      this.$refs.contentForm.validate(async (valid) => {
        if (valid) {
          try {
            await saveImageContent(this.contentForm)
            this.$message.success('保存成功')
            this.$router.push('/education/image-library')
          } catch (error) {
            this.$message.error('保存失败')
            console.error('保存失败:', error)
          }
        } else {
          this.$message.warning('请填写必填项')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.image-add-container {
  padding: 20px;
  background-color: #fff;

  .page-header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #e4e7ed;

    .page-title {
      margin-left: 15px;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .form-container {
    max-width: 900px;
    margin-left: 20px;
  }

  .char-count {
    text-align: right;
    color: #909399;
    font-size: 12px;
    margin-top: 5px;
  }

  .upload-container {
    .cover-uploader {
      .upload-box {
        width: 178px;
        height: 178px;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          border-color: #409eff;
        }

        i {
          font-size: 28px;
          color: #8c939d;
        }
      }

      .cover-image {
        width: 178px;
        height: 178px;
        display: block;
        object-fit: cover;
      }
    }

    .upload-tips {
      color: #909399;
      font-size: 12px;
      margin-top: 8px;
      line-height: 1.5;
    }
  }

  .editor-container {
    width: 600px;

    .editor-toolbar {
      border: 1px solid #dcdfe6;
      border-bottom: none;
      padding: 10px;
      background-color: #f5f7fa;
      display: flex;
      align-items: center;
      gap: 10px;

      .paragraph-select {
        padding: 5px 10px;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        background-color: white;
        cursor: pointer;
        font-size: 14px;

        &:focus {
          outline: none;
          border-color: #409eff;
        }
      }
    }

    ::v-deep .el-textarea {
      .el-textarea__inner {
        border-top-left-radius: 0;
        border-top-right-radius: 0;
      }
    }
  }

  .submit-buttons {
    margin-top: 30px;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  }
}
</style>
