<template>
  <div class="content-config-container">
    <el-card>
      <div slot="header" class="clearfix">
        <el-button icon="el-icon-arrow-left" type="text" @click="goBack">返回</el-button>
        <span style="margin-left: 10px; font-weight: bold;">内容标题：{{ contentInfo.contentTitle }}</span>
      </div>

      <el-form
        :model="configForm"
        :rules="formRules"
        ref="configForm"
        label-width="120px"
        size="small"
        class="config-form"
      >
        <!-- 应用项目 -->
        <el-form-item label="应用项目" prop="applicationProject" required>
          <el-select
            v-model="configForm.applicationProject"
            placeholder="请选择"
            style="width: 200px;"
          >
            <el-option label="去关联" value="去关联" />
          </el-select>
        </el-form-item>

        <!-- 应用模块 -->
        <el-form-item label="应用模块">
          <el-select
            v-model="configForm.applicationModule"
            placeholder="请选择"
            style="width: 200px;"
          >
            <el-option label="医教" value="医教" />
            <el-option label="直数" value="直数" />
          </el-select>
        </el-form-item>

        <!-- 所属频道 -->
        <el-form-item label="所属频道" prop="channel">
          <el-select
            v-model="configForm.channel"
            placeholder="请选择"
            style="width: 200px;"
          >
            <el-option label="儿童生长发育" value="儿童生长发育" />
            <el-option label="育儿健康" value="育儿健康" />
            <el-option label="随访" value="随访" />
            <el-option label="健康小知识" value="健康小知识" />
            <el-option label="男性不孕症" value="男性不孕症" />
            <el-option label="孕产健康" value="孕产健康" />
            <el-option label="护理知识" value="护理知识" />
            <el-option label="性功能障碍" value="性功能障碍" />
            <el-option label="精子冷冻" value="精子冷冻" />
          </el-select>
        </el-form-item>

        <!-- 置顶 -->
        <el-form-item label="置顶">
          <el-switch v-model="configForm.pinStatus" />
        </el-form-item>

        <!-- 关联配置标题 -->
        <div class="section-title">关联配置</div>

        <!-- 关联科室 -->
        <el-form-item label="关联科室">
          <el-select
            v-model="configForm.relatedDepartment"
            placeholder="请选择"
            style="width: 200px;"
          >
            <el-option label="去关联" value="去关联" />
          </el-select>
        </el-form-item>

        <!-- 关联医生 -->
        <el-form-item label="关联医生">
          <el-select
            v-model="configForm.relatedDoctor"
            placeholder="去关联"
            style="width: 200px;"
          >
            <el-option label="去关联" value="去关联" />
          </el-select>
        </el-form-item>

        <!-- 关联疾病 -->
        <el-form-item label="关联疾病">
          <el-select
            v-model="configForm.relatedDisease"
            multiple
            collapse-tags
            placeholder="请选择"
            style="width: 400px;"
          >
            <el-option label="高血压" value="高血压" />
            <el-option label="糖尿病" value="糖尿病" />
            <el-option label="冠心病" value="冠心病" />
            <el-option label="慢性阻塞性肺疾病" value="慢性阻塞性肺疾病" />
          </el-select>
        </el-form-item>

        <!-- 关联症状 -->
        <el-form-item label="关联症状">
          <el-select
            v-model="configForm.relatedSymptom"
            multiple
            collapse-tags
            placeholder="请选择"
            style="width: 400px;"
          >
            <el-option label="发热" value="发热" />
            <el-option label="咳嗽" value="咳嗽" />
            <el-option label="头痛" value="头痛" />
            <el-option label="胸痛" value="胸痛" />
            <el-option label="腹痛" value="腹痛" />
            <el-option label="乏力" value="乏力" />
          </el-select>
        </el-form-item>

        <!-- 关联症状 -->
        <el-form-item label="关联症状">
          <el-select
            v-model="configForm.relatedSymptoms"
            placeholder="请选择"
            style="width: 200px;"
          >
            <el-option label="去关联" value="去关联" />
          </el-select>
        </el-form-item>

        <!-- 关联商品 -->
        <el-form-item label="关联商品">
          <el-select
            v-model="configForm.relatedProduct"
            multiple
            collapse-tags
            placeholder="请选择"
            style="width: 400px;"
          >
            <el-option label="商品A" value="商品A" />
            <el-option label="商品B" value="商品B" />
            <el-option label="商品C" value="商品C" />
          </el-select>
        </el-form-item>
      </el-form>

      <div class="form-footer">
        <el-button @click="goBack">返回</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getContentApplicationById, updateContentApplication } from '@/api/education'

export default {
  name: 'ContentConfig',
  data() {
    return {
      contentId: null,
      contentInfo: {
        contentTitle: ''
      },
      configForm: {
        applicationProject: '',
        applicationModule: '',
        channel: '',
        pinStatus: false,
        relatedDepartment: '',
        relatedDoctor: '',
        relatedDisease: [],
        relatedSymptom: [],
        relatedSymptoms: '',
        relatedProduct: []
      },
      formRules: {
        applicationProject: [
          { required: true, message: '请选择应用项目', trigger: 'change' }
        ],
        channel: [
          { required: true, message: '请选择所属频道', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.contentId = this.$route.query.id
    if (this.contentId) {
      this.loadContentConfig()
    }
  },
  methods: {
    // 加载内容配置数据
    async loadContentConfig() {
      try {
        const response = await getContentApplicationById(this.contentId)
        if (response.code === 200) {
          const data = response.data
          this.contentInfo.contentTitle = data.contentTitle

          // 填充表单数据
          this.configForm.applicationProject = data.applicationProject || ''
          this.configForm.applicationModule = data.applicationModule || ''
          this.configForm.channel = data.channel || ''
          this.configForm.pinStatus = data.pinStatus === 1

          // 处理关联疾病（逗号分隔的字符串转数组）
          if (data.relatedDisease) {
            this.configForm.relatedDisease = data.relatedDisease.split(',')
          }

          // 处理关联症状（逗号分隔的字符串转数组）
          if (data.relatedSymptom) {
            this.configForm.relatedSymptom = data.relatedSymptom.split(',')
          }

          // 处理关联商品（逗号分隔的字符串转数组）
          if (data.relatedProduct) {
            this.configForm.relatedProduct = data.relatedProduct.split(',')
          }
        } else {
          this.$message.error('加载配置数据失败')
          this.$router.go(-1)
        }
      } catch (error) {
        this.$message.error('加载配置数据失败')
        console.error('加载失败:', error)
        this.$router.go(-1)
      }
    },
    // 返回
    goBack() {
      this.$router.go(-1)
    },
    // 保存配置
    handleSave() {
      this.$refs.configForm.validate(async (valid) => {
        if (valid) {
          try {
            const params = {
              id: this.contentId,
              applicationProject: this.configForm.applicationProject,
              applicationModule: this.configForm.applicationModule,
              channel: this.configForm.channel,
              pinStatus: this.configForm.pinStatus ? 1 : 0,
              relatedDisease: this.configForm.relatedDisease.join(','),
              relatedSymptom: this.configForm.relatedSymptom.join(','),
              relatedProduct: this.configForm.relatedProduct.join(',')
            }

            await updateContentApplication(params)
            this.$message.success('保存成功')
            this.$router.go(-1)
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
.content-config-container {
  padding: 20px;

  .config-form {
    max-width: 800px;
  }

  .section-title {
    font-weight: bold;
    font-size: 16px;
    color: #303133;
    margin: 20px 0 15px 0;
    padding-left: 10px;
    border-left: 4px solid #409eff;
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
