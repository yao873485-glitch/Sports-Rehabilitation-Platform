<template>
  <div class="video-add-container">
    <el-card>
      <div slot="header" class="clearfix">
        <el-button icon="el-icon-arrow-left" type="text" @click="goBack">返回</el-button>
        <span style="margin-left: 10px; font-weight: bold;">{{ pageTitle }}</span>
      </div>

      <el-form
        :model="videoForm"
        :rules="formRules"
        ref="videoForm"
        label-width="120px"
        size="small"
        class="video-form"
      >
        <!-- 内容类型 -->
        <el-form-item label="内容类型" prop="contentType" required>
          <el-select v-model="videoForm.contentType" placeholder="请选择内容类型" style="width: 200px;" disabled>
            <el-option label="视频" value="视频" />
          </el-select>
        </el-form-item>

        <!-- 视频标题 -->
        <el-form-item label="视频标题" prop="videoTitle" required>
          <el-input
            v-model="videoForm.videoTitle"
            placeholder="请输入标题，限50个字符"
            maxlength="50"
            show-word-limit
            style="width: 100%;"
            :disabled="isReadOnly"
          />
          <div class="char-count" v-if="!isReadOnly">{{ videoForm.videoTitle.length }}/50</div>
        </el-form-item>

        <!-- 作者 -->
        <el-form-item label="作者" prop="author">
          <el-input
            v-model="videoForm.author"
            placeholder="请输入作者"
            style="width: 200px;"
            :disabled="isReadOnly"
          />
          <el-select
            v-model="videoForm.authorType"
            placeholder="请选择"
            style="width: 150px; margin-left: 10px;"
            :disabled="isReadOnly"
          >
            <el-option label="平台运营" value="平台运营" />
          </el-select>
          <el-button type="text" style="margin-left: 10px;" v-if="!isReadOnly">选择</el-button>
        </el-form-item>

        <!-- 所属板块 -->
        <el-form-item label="所属板块" prop="categorySection" required>
          <el-select
            v-model="videoForm.categorySection"
            placeholder="请选择所属板块"
            style="width: 200px;"
            :disabled="isReadOnly"
          >
            <el-option label="医教" value="医教" />
            <el-option label="康复" value="康复" />
            <el-option label="术后护理" value="术后护理" />
            <el-option label="营养指导" value="营养指导" />
          </el-select>
        </el-form-item>

        <!-- 发布时间 -->
        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker
            v-model="videoForm.publishTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 300px;"
            :disabled="isReadOnly"
          />
        </el-form-item>

        <!-- 是否支持游戏模式 -->
        <el-form-item label="是否支持游戏模式" prop="supportGameMode">
          <el-radio-group v-model="videoForm.supportGameMode" :disabled="isReadOnly">
            <el-radio :label="1">支持</el-radio>
            <el-radio :label="0">不支持</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 是否原创 -->
        <el-form-item label="是否原创" prop="isOriginal">
          <el-radio-group v-model="videoForm.isOriginal" :disabled="isReadOnly">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 视频描述 -->
        <el-form-item label="视频描述" prop="videoDescription">
          <el-input
            v-model="videoForm.videoDescription"
            type="textarea"
            placeholder="请输入描述，用于视频摘要展示"
            :rows="4"
            maxlength="300"
            show-word-limit
            style="width: 100%;"
            :disabled="isReadOnly"
          />
          <div class="char-count" v-if="!isReadOnly">{{ videoForm.videoDescription.length }}/300</div>
        </el-form-item>

        <!-- 封面图 -->
        <el-form-item label="封面图" prop="coverImage">
          <div class="upload-container">
            <el-upload
              v-if="!isReadOnly"
              class="cover-uploader"
              action="/api/upload"
              :show-file-list="false"
              :on-success="handleCoverUploadSuccess"
              :before-upload="beforeCoverUpload"
              accept="image/*"
            >
              <div class="upload-box" v-if="!videoForm.coverImage">
                <i class="el-icon-plus"></i>
              </div>
              <img v-else :src="videoForm.coverImage" class="cover-image" />
            </el-upload>
            <img v-if="isReadOnly && videoForm.coverImage" :src="videoForm.coverImage" class="cover-image" />
            <div class="upload-tips" v-if="!isReadOnly">
              当前图片仅允许上传文件类型，最大3张图片，图片不超过370*225
            </div>
          </div>
        </el-form-item>

        <!-- 视频来源 -->
        <el-form-item label="视频来源" prop="videoSource">
          <el-radio-group v-model="videoForm.videoSource" :disabled="isReadOnly">
            <el-radio label="本地上传">本地上传</el-radio>
            <el-radio label="在线文件地址">在线文件地址</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 文件地址（在线文件地址） -->
        <el-form-item label="文件地址" prop="videoUrl" v-if="videoForm.videoSource === '在线文件地址'">
          <el-input
            v-model="videoForm.videoUrl"
            placeholder="请输入视频URL地址"
            style="width: 100%;"
            :disabled="isReadOnly"
          />
          <el-button v-if="!isReadOnly" type="primary" plain size="small" style="margin-top: 10px;">上传</el-button>
          <div class="upload-tips" v-if="!isReadOnly">支持 wav avi mp4, 建议小于200M</div>
        </el-form-item>

        <!-- 文件上传（本地上传） -->
        <el-form-item label="文件地址" prop="videoUrl" v-if="videoForm.videoSource === '本地上传'">
          <el-upload
            v-if="!isReadOnly"
            class="video-uploader"
            action="/api/upload/video"
            :show-file-list="true"
            :on-success="handleVideoUploadSuccess"
            :before-upload="beforeVideoUpload"
            accept="video/*"
            :file-list="videoFileList"
          >
            <el-button type="primary" plain size="small">上传</el-button>
          </el-upload>
          <div v-if="isReadOnly && videoForm.videoUrl" class="video-url-display">{{ videoForm.videoUrl }}</div>
          <div class="upload-tips" v-if="!isReadOnly">支持 wav avi mp4, 建议小于200M</div>
        </el-form-item>

        <!-- 所属模块 -->
        <el-form-item label="所属模块" prop="moduleCategory">
          <el-radio-group v-model="videoForm.moduleCategory" :disabled="isReadOnly">
            <el-radio label="医教">医教</el-radio>
            <el-radio label="直数">直数</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 点赞基数 -->
        <el-form-item label="点赞基数">
          <el-input
            v-model.number="videoForm.likeCountBase"
            placeholder="0"
            type="number"
            style="width: 150px;"
            :disabled="isReadOnly"
          >
            <template slot="append">人</template>
          </el-input>
        </el-form-item>

        <!-- 阅读基数 -->
        <el-form-item label="阅读基数">
          <el-input
            v-model.number="videoForm.viewCountBase"
            placeholder="0"
            type="number"
            style="width: 150px;"
            :disabled="isReadOnly"
          >
            <template slot="append">人</template>
          </el-input>
        </el-form-item>
      </el-form>

      <div class="form-footer">
        <el-button @click="goBack">返回</el-button>
        <el-button v-if="!isReadOnly" type="primary" @click="handleSubmit">返回并保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { addVideoAsset, updateVideoAsset, getVideoAssetDetail } from '@/api/education'

export default {
  name: 'VideoAdd',
  data() {
    return {
      mode: 'add', // add, edit, copy, preview, detail
      videoId: null,
      videoForm: {
        contentType: '视频',
        videoTitle: '',
        author: '系统管理员',
        authorType: '平台运营',
        categorySection: '医教',
        publishTime: null,
        supportGameMode: 1,
        isOriginal: 1,
        videoDescription: '',
        coverImage: '',
        videoSource: '本地上传',
        videoUrl: '',
        moduleCategory: '医教',
        likeCountBase: 0,
        viewCountBase: 0
      },
      formRules: {
        contentType: [
          { required: true, message: '请选择内容类型', trigger: 'change' }
        ],
        videoTitle: [
          { required: true, message: '请输入视频标题', trigger: 'blur' },
          { max: 50, message: '标题长度不能超过50个字符', trigger: 'blur' }
        ],
        categorySection: [
          { required: true, message: '请选择所属板块', trigger: 'change' }
        ],
        author: [
          { required: true, message: '请输入作者', trigger: 'blur' }
        ],
        videoDescription: [
          { max: 300, message: '描述长度不能超过300个字符', trigger: 'blur' }
        ]
      },
      videoFileList: []
    }
  },
  computed: {
    pageTitle() {
      const titles = {
        add: '新增内容',
        edit: '编辑视频',
        copy: '复制视频',
        preview: '预览视频',
        detail: '视频详情'
      }
      return titles[this.mode] || '新增内容'
    },
    isReadOnly() {
      return this.mode === 'preview' || this.mode === 'detail'
    }
  },
  created() {
    // 获取路由参数
    this.mode = this.$route.query.mode || 'add'
    this.videoId = this.$route.query.id

    // 如果是编辑、复制、预览或详情模式，加载数据
    if (this.videoId && this.mode !== 'add') {
      this.loadVideoData()
    }
  },
  methods: {
    // 加载视频数据
    async loadVideoData() {
      try {
        const response = await getVideoAssetDetail(this.videoId)
        if (response.code === 200) {
          const data = response.data

          // 如果是复制模式，不要复制ID
          if (this.mode === 'copy') {
            delete data.id
            delete data.videoId
          } else {
            this.videoForm.id = data.id
          }

          // 填充表单数据
          Object.keys(this.videoForm).forEach(key => {
            if (data[key] !== undefined && data[key] !== null) {
              this.videoForm[key] = data[key]
            }
          })

          // 如果有视频URL且是本地上传，设置文件列表
          if (data.videoUrl && data.videoSource === '本地上传') {
            this.videoFileList = [{ name: '视频文件', url: data.videoUrl }]
          }
        } else {
          this.$message.error('加载视频数据失败')
          this.$router.go(-1)
        }
      } catch (error) {
        this.$message.error('加载视频数据失败')
        console.error('加载失败:', error)
        this.$router.go(-1)
      }
    },
    // 返回列表页
    goBack() {
      this.$router.go(-1)
    },
    // 封面图上传成功
    handleCoverUploadSuccess(response) {
      if (response.code === 200) {
        this.videoForm.coverImage = response.data.url
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    // 封面图上传前验证
    beforeCoverUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }
      return true
    },
    // 视频上传成功
    handleVideoUploadSuccess(response, file) {
      if (response.code === 200) {
        this.videoForm.videoUrl = response.data.url
        this.videoFileList = [{ name: file.name, url: response.data.url }]
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    // 视频上传前验证
    beforeVideoUpload(file) {
      const isVideo = file.type.startsWith('video/')
      const isLt200M = file.size / 1024 / 1024 < 200

      if (!isVideo) {
        this.$message.error('只能上传视频文件!')
        return false
      }
      if (!isLt200M) {
        this.$message.error('视频大小不能超过 200MB!')
        return false
      }
      return true
    },
    // 提交表单
    handleSubmit() {
      this.$refs.videoForm.validate(async (valid) => {
        if (valid) {
          try {
            if (this.mode === 'edit') {
              // 编辑模式
              await updateVideoAsset(this.videoForm)
              this.$message.success('更新成功')
            } else {
              // 新增或复制模式
              await addVideoAsset(this.videoForm)
              this.$message.success(this.mode === 'copy' ? '复制成功' : '添加成功')
            }
            this.$router.go(-1)
          } catch (error) {
            this.$message.error(this.mode === 'edit' ? '更新失败' : '添加失败')
            console.error('提交失败:', error)
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
.video-add-container {
  padding: 20px;

  .video-form {
    max-width: 800px;
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

  .video-uploader {
    .upload-tips {
      color: #909399;
      font-size: 12px;
      margin-top: 8px;
      line-height: 1.5;
    }
  }

  .form-footer {
    text-align: center;
    margin-top: 30px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;

    .el-button {
      min-width: 120px;
    }
  }
}
</style>
