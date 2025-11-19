<template>
  <div class="content-config-container">
    <el-card>
      <div slot="header" class="header">
        <el-button icon="el-icon-back" size="small" @click="goBack">返回</el-button>
        <span class="title">{{ contentTitle }}</span>
        <div class="actions">
          <el-button type="primary" size="small" @click="handleSave">编辑</el-button>
        </div>
      </div>

      <div v-loading="loading" class="config-content">
        <el-form
          ref="configForm"
          :model="configForm"
          label-width="140px"
          class="config-form"
        >
          <el-form-item label="应用项目">
            <el-input v-model="configForm.applicationProject" placeholder="请输入" style="width: 300px;" />
          </el-form-item>

          <el-form-item label="应用模块">
            <el-select v-model="configForm.applicationModule" placeholder="请选择" style="width: 300px;">
              <el-option label="医教" value="医教" />
              <el-option label="患教" value="患教" />
              <el-option label="健康管理" value="健康管理" />
            </el-select>
          </el-form-item>

          <el-form-item label="所属频道">
            <el-select v-model="configForm.channel" placeholder="请选择" style="width: 300px;">
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

          <el-form-item label="关联疾病">
            <el-input
              v-model="configForm.relatedDiseases"
              type="textarea"
              :rows="3"
              placeholder="多个疾病用逗号分隔"
              style="width: 500px;"
            />
          </el-form-item>

          <el-form-item label="关联症状">
            <el-input
              v-model="configForm.relatedSymptoms"
              type="textarea"
              :rows="3"
              placeholder="多个症状用逗号分隔"
              style="width: 500px;"
            />
          </el-form-item>

          <el-form-item label="关联商品">
            <el-input
              v-model="configForm.relatedProducts"
              type="textarea"
              :rows="3"
              placeholder="多个商品用逗号分隔"
              style="width: 500px;"
            />
          </el-form-item>

          <el-form-item label="作者">
            <el-input v-model="configForm.author" placeholder="请输入作者" style="width: 300px;" />
          </el-form-item>

          <el-form-item label="排序">
            <el-input-number v-model="configForm.sortOrder" :min="0" :max="9999" />
          </el-form-item>

          <el-form-item label="是否置顶">
            <el-switch v-model="configForm.isPinned" :active-value="1" :inactive-value="0" />
          </el-form-item>

          <el-form-item label="备注">
            <el-input
              v-model="configForm.remark"
              type="textarea"
              :rows="4"
              placeholder="请输入备注"
              style="width: 500px;"
            />
          </el-form-item>

          <el-form-item>
            <el-button @click="goBack">返回</el-button>
            <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getContentConfig, saveContentConfig } from '@/api/education'

export default {
  name: 'ContentConfig',
  data() {
    return {
      loading: false,
      saving: false,
      contentId: '',
      contentType: '',
      contentTitle: '加载中...',
      configForm: {
        applicationProject: '',
        applicationModule: '',
        channel: '',
        relatedDiseases: '',
        relatedSymptoms: '',
        relatedProducts: '',
        author: '',
        sortOrder: 0,
        isPinned: 0,
        remark: ''
      }
    }
  },
  created() {
    const query = this.$route.query
    this.contentId = query.contentId
    this.contentType = query.contentType
    this.contentTitle = query.title || '内容配置'

    if (this.contentId && this.contentType) {
      this.fetchConfig()
    } else {
      this.$message.error('缺少必要参数')
      this.goBack()
    }
  },
  methods: {
    // 获取配置数据
    async fetchConfig() {
      this.loading = true
      try {
        const response = await getContentConfig(this.contentId, this.contentType)
        if (response && response.data) {
          const config = response.data
          this.configForm = {
            applicationProject: config.applicationProject || '',
            applicationModule: config.applicationModule || '',
            channel: config.channel || '',
            relatedDiseases: config.relatedDiseases || '',
            relatedSymptoms: config.relatedSymptoms || '',
            relatedProducts: config.relatedProducts || '',
            author: config.operator || '',
            sortOrder: config.sortOrder || 0,
            isPinned: config.isPinned || 0,
            remark: config.remark || ''
          }
        }
      } catch (error) {
        console.error('获取配置失败:', error)
        // 如果没有配置，保持空表单即可
      } finally {
        this.loading = false
      }
    },
    // 保存配置
    async handleSave() {
      this.saving = true
      try {
        const data = {
          contentId: this.contentId,
          contentType: this.contentType,
          applicationProject: this.configForm.applicationProject,
          applicationModule: this.configForm.applicationModule,
          channel: this.configForm.channel,
          relatedDiseases: this.configForm.relatedDiseases,
          relatedSymptoms: this.configForm.relatedSymptoms,
          relatedProducts: this.configForm.relatedProducts,
          sortOrder: this.configForm.sortOrder,
          isPinned: this.configForm.isPinned,
          remark: this.configForm.remark,
          operator: this.configForm.author || '系统管理员'
        }

        await saveContentConfig(data)
        this.$message.success('保存成功')
        setTimeout(() => {
          this.goBack()
        }, 1000)
      } catch (error) {
        this.$message.error('保存失败')
        console.error('保存失败:', error)
      } finally {
        this.saving = false
      }
    },
    // 返回
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.content-config-container {
  padding: 20px;
  background-color: #f5f5f5;

  .el-card {
    background-color: #ffffff;
  }

  .header {
    display: flex;
    align-items: center;
    gap: 16px;

    .title {
      flex: 1;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }

    .actions {
      display: flex;
      gap: 8px;
    }
  }

  .config-content {
    padding: 20px 0;
  }

  .config-form {
    max-width: 800px;

    ::v-deep .el-form-item__label {
      color: #606266;
      font-weight: normal;
      text-align: left;
    }

    ::v-deep .el-form-item {
      margin-bottom: 22px;
    }
  }
}

::v-deep .el-card__header {
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
}

::v-deep .el-card__body {
  padding: 0 20px 20px 20px;
}
</style>
