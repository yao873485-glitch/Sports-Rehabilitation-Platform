<template>
  <div class="content-add-container">
    <el-card>
      <div slot="header" class="clearfix">
        <el-button icon="el-icon-arrow-left" type="text" @click="goBack">返回</el-button>
        <span style="margin-left: 10px; font-weight: bold;">{{ pageTitle }}</span>
      </div>

      <el-form
        :model="contentForm"
        :rules="formRules"
        ref="contentForm"
        label-width="120px"
        size="small"
        class="content-form"
      >
        <!-- 内容类型 -->
        <el-form-item label="内容类型" prop="contentType" required>
          <el-select v-model="contentForm.contentType" placeholder="请选择内容类型" style="width: 200px;" disabled>
            <el-option label="文章" value="文章" />
          </el-select>
        </el-form-item>

        <!-- 内容标题 -->
        <el-form-item label="内容标题" prop="contentTitle" required>
          <el-input
            v-model="contentForm.contentTitle"
            placeholder="请输入标题，限50个字符"
            maxlength="50"
            show-word-limit
            style="width: 100%;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 作者 -->
        <el-form-item label="作者" prop="author">
          <el-input
            v-model="contentForm.author"
            placeholder="请输入作者"
            style="width: 200px;"
            :disabled="isReadOnly"
          />
          <el-select
            v-model="contentForm.authorType"
            placeholder="请选择"
            style="width: 150px; margin-left: 10px;"
            :disabled="isReadOnly"
          >
            <el-option label="平台运营" value="平台运营" />
          </el-select>
        </el-form-item>

        <!-- 所属板块 -->
        <el-form-item label="所属板块" prop="categorySection" required>
          <el-select
            v-model="contentForm.categorySection"
            placeholder="请选择所属板块"
            style="width: 200px;"
            :disabled="isReadOnly"
          >
            <el-option label="医教" value="医教" />
            <el-option label="科普" value="科普" />
            <el-option label="康复指导" value="康复指导" />
            <el-option label="健康资讯" value="健康资讯" />
          </el-select>
        </el-form-item>

        <!-- 发布时间 -->
        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker
            v-model="contentForm.publishTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 300px;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 是否支持游戏模式 -->
        <el-form-item label="是否支持游戏模式" prop="supportGameMode">
          <el-radio-group v-model="contentForm.supportGameMode" :disabled="isReadOnly">
            <el-radio :label="1">支持</el-radio>
            <el-radio :label="0">不支持</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 是否原创 -->
        <el-form-item label="是否原创" prop="isOriginal">
          <el-radio-group v-model="contentForm.isOriginal" :disabled="isReadOnly">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 文章描述 -->
        <el-form-item label="文章描述" prop="articleDescription">
          <el-input
            v-model="contentForm.articleDescription"
            type="textarea"
            :rows="4"
            placeholder="请输入文章描述"
            maxlength="500"
            show-word-limit
            style="width: 100%;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 封面图 -->
        <el-form-item label="封面图" prop="coverImage">
          <el-upload
            class="cover-uploader"
            action="#"
            :show-file-list="false"
            :on-change="handleCoverChange"
            :auto-upload="false"
            :disabled="isReadOnly"
          >
            <img v-if="contentForm.coverImage" :src="contentForm.coverImage" class="cover-image">
            <i v-else class="el-icon-plus cover-uploader-icon"></i>
          </el-upload>
          <div class="upload-tip">建议尺寸：750x500像素</div>
        </el-form-item>

        <!-- 文章来源 -->
        <el-form-item label="文章来源" prop="contentSource">
          <el-input
            v-model="contentForm.contentSource"
            placeholder="请输入文章来源"
            style="width: 300px;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 文章内容 -->
        <el-form-item label="文章内容" prop="articleContent">
          <el-input
            v-model="contentForm.articleContent"
            type="textarea"
            :rows="10"
            placeholder="请输入文章内容"
            style="width: 100%;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 外链地址 -->
        <el-form-item label="外链地址" prop="externalLink">
          <el-input
            v-model="contentForm.externalLink"
            placeholder="请输入外链地址"
            style="width: 100%;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 所属模块 -->
        <el-form-item label="所属模块" prop="moduleCategory">
          <el-select
            v-model="contentForm.moduleCategory"
            placeholder="请选择所属模块"
            style="width: 200px;"
            :disabled="isReadOnly"
          >
            <el-option label="慢病管理" value="慢病管理" />
            <el-option label="康复训练" value="康复训练" />
            <el-option label="健康科普" value="健康科普" />
          </el-select>
        </el-form-item>

        <!-- 阅读基数 -->
        <el-form-item label="阅读基数" prop="viewCountBase">
          <el-input-number
            v-model="contentForm.viewCountBase"
            :min="0"
            :step="1"
            style="width: 200px;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 点赞基数 -->
        <el-form-item label="点赞基数" prop="likeCountBase">
          <el-input-number
            v-model="contentForm.likeCountBase"
            :min="0"
            :step="1"
            style="width: 200px;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 操作按钮 -->
        <el-form-item v-if="!isReadOnly">
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEdit ? '保存' : '提交' }}
          </el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getContentDetail, addContent, updateContent } from '@/api/education'

export default {
  name: 'ContentAdd',
  data() {
    return {
      contentForm: {
        contentType: '文章',
        contentTitle: '',
        author: '',
        authorType: '平台运营',
        categorySection: '',
        publishTime: '',
        supportGameMode: 0,
        isOriginal: 1,
        articleDescription: '',
        coverImage: '',
        contentSource: '',
        articleContent: '',
        externalLink: '',
        moduleCategory: '',
        viewCountBase: 0,
        likeCountBase: 0
      },
      formRules: {
        contentTitle: [
          { required: true, message: '请输入内容标题', trigger: 'blur' }
        ],
        categorySection: [
          { required: true, message: '请选择所属板块', trigger: 'change' }
        ]
      },
      submitting: false,
      isEdit: false,
      isReadOnly: false
    }
  },
  computed: {
    pageTitle() {
      if (this.isReadOnly) return '查看图文内容'
      return this.isEdit ? '编辑图文内容' : '新增图文内容'
    }
  },
  created() {
    const { id, mode } = this.$route.query
    if (id) {
      this.isEdit = true
      this.isReadOnly = mode === 'view'
      this.loadContentData(id)
    }
  },
  methods: {
    // 加载内容数据
    async loadContentData(id) {
      try {
        const response = await getContentDetail(id)
        if (response.code === 200) {
          this.contentForm = { ...this.contentForm, ...response.data }
        }
      } catch (error) {
        this.$message.error('加载数据失败')
        console.error('加载数据失败:', error)
      }
    },
    // 处理封面上传
    handleCoverChange(file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        this.contentForm.coverImage = e.target.result
      }
      reader.readAsDataURL(file.raw)
    },
    // 提交表单
    async handleSubmit() {
      this.$refs.contentForm.validate(async (valid) => {
        if (!valid) {
          return false
        }

        this.submitting = true
        try {
          if (this.isEdit) {
            await updateContent(this.contentForm)
            this.$message.success('保存成功')
          } else {
            await addContent(this.contentForm)
            this.$message.success('添加成功')
          }
          this.goBack()
        } catch (error) {
          this.$message.error(this.isEdit ? '保存失败' : '添加失败')
          console.error('提交失败:', error)
        } finally {
          this.submitting = false
        }
      })
    },
    // 返回
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.content-add-container {
  padding: 20px;

  .content-form {
    max-width: 800px;
  }

  .cover-uploader {
    ::v-deep .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;

      &:hover {
        border-color: #409EFF;
      }
    }

    .cover-image {
      width: 200px;
      height: 133px;
      display: block;
      object-fit: cover;
    }

    .cover-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 200px;
      height: 133px;
      line-height: 133px;
      text-align: center;
      display: block;
    }
  }

  .upload-tip {
    color: #999;
    font-size: 12px;
    margin-top: 5px;
  }
}
</style>
