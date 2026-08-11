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
          :rules="formRules"
          label-width="140px"
          class="config-form"
        >
          <el-form-item label="应用项目" prop="applicationProject">
            <el-input v-model="configForm.applicationProject" placeholder="请输入应用项目" style="width: 300px;" />
          </el-form-item>

          <el-form-item label="应用模块" prop="applicationModule">
            <el-select v-model="configForm.applicationModule" placeholder="请选择应用模块" style="width: 300px;">
              <el-option label="医教" value="医教" />
              <el-option label="患教" value="患教" />
              <el-option label="健康管理" value="健康管理" />
            </el-select>
          </el-form-item>

          <el-form-item label="所属频道" prop="channel">
            <el-select v-model="configForm.channel" placeholder="请选择所属频道" style="width: 300px;">
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

          <el-form-item label="关联配置">
            <div class="relation-config">
              <div class="relation-item">
                <span class="relation-label">关联科室：</span>
                <span class="relation-value">{{ configForm.relatedDepartments || '未关联' }}</span>
                <el-link type="primary" @click="showDepartmentDialog">去关联</el-link>
              </div>
              <div class="relation-item">
                <span class="relation-label">关联医生：</span>
                <span class="relation-value">{{ configForm.relatedDoctors || '未关联' }}</span>
                <el-link type="primary" @click="showDoctorDialog">去关联</el-link>
              </div>
            </div>
          </el-form-item>

          <el-form-item label="关联疾病">
            <el-select
              v-model="configForm.relatedDiseases"
              multiple
              filterable
              allow-create
              placeholder="请选择或输入关联疾病"
              style="width: 500px;"
            >
              <el-option v-for="item in diseaseOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>

          <el-form-item label="关联症状">
            <el-select
              v-model="configForm.relatedSymptoms"
              multiple
              filterable
              allow-create
              placeholder="请选择或输入关联症状"
              style="width: 500px;"
            >
              <el-option v-for="item in symptomOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>

          <el-form-item label="关联商品">
            <el-select
              v-model="configForm.relatedProducts"
              multiple
              filterable
              allow-create
              placeholder="请选择或输入关联商品"
              style="width: 500px;"
            >
              <el-option v-for="item in productOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>

          <el-form-item label="排序">
            <el-input-number v-model="configForm.sortOrder" :min="0" :max="9999" />
          </el-form-item>

          <el-form-item label="是否置顶">
            <el-switch v-model="configForm.isPinned" :active-value="1" :inactive-value="0" />
          </el-form-item>

          <el-form-item>
            <el-button @click="goBack">返回</el-button>
            <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
          </el-form-item>
        </el-form>

        <!-- 日志记录 -->
        <div v-if="operationLogs.length > 0" class="operation-logs">
          <h3 class="logs-title">日志记录</h3>
          <el-table :data="operationLogs" border>
            <el-table-column prop="operationType" label="操作类型" width="120" align="center" />
            <el-table-column prop="operationContent" label="操作内容" show-overflow-tooltip />
            <el-table-column prop="operator" label="操作人" width="120" align="center" />
            <el-table-column prop="operationTime" label="操作时间" width="180" align="center" />
          </el-table>
        </div>
      </div>

    <!-- 关联科室对话框 -->
    <el-dialog title="关联科室" :visible.sync="departmentDialogVisible" width="600px">
      <el-select
        v-model="selectedDepartments"
        multiple
        filterable
        placeholder="请选择科室"
        style="width: 100%;"
      >
        <el-option v-for="dept in departmentList" :key="dept" :label="dept" :value="dept" />
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="departmentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDepartments">确定</el-button>
      </div>
    </el-dialog>

    <!-- 关联医生对话框 -->
    <el-dialog title="关联医生" :visible.sync="doctorDialogVisible" width="600px">
      <el-select
        v-model="selectedDoctors"
        multiple
        filterable
        placeholder="请选择医生"
        style="width: 100%;"
      >
        <el-option v-for="doctor in doctorList" :key="doctor" :label="doctor" :value="doctor" />
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="doctorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDoctors">确定</el-button>
      </div>
    </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { getContentConfig, saveContentConfig, getConfigLogs } from '@/api/education'

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
        relatedDiseases: [],
        relatedSymptoms: [],
        relatedProducts: [],
        relatedDepartments: '',
        relatedDoctors: '',
        sortOrder: 0,
        isPinned: 0
      },
      formRules: {
        applicationProject: [
          { required: true, message: '请输入应用项目', trigger: 'blur' }
        ],
        applicationModule: [
          { required: true, message: '请选择应用模块', trigger: 'change' }
        ],
        channel: [
          { required: true, message: '请选择所属频道', trigger: 'change' }
        ]
      },
      // 下拉选项数据
      diseaseOptions: ['糖尿病', '高血压', '冠心病', '脑卒中', '慢性阻塞性肺疾病', '哮喘', '类风湿关节炎', '骨质疏松', '抑郁症', '焦虑症'],
      symptomOptions: ['头痛', '发热', '咳嗽', '胸闷', '气短', '乏力', '食欲不振', '恶心', '呕吐', '腹痛', '腹泻', '便秘', '失眠', '心悸'],
      productOptions: ['血糖仪', '血压计', '雾化器', '制氧机', '呼吸机', '康复器械', '理疗仪', '按摩器', '护理床', '轮椅'],
      // 关联科室和医生
      departmentDialogVisible: false,
      selectedDepartments: [],
      departmentList: ['内科', '外科', '儿科', '妇产科', '骨科', '神经内科', '心血管内科', '呼吸内科', '消化内科', '内分泌科', '肾内科', '血液科', '肿瘤科', '康复医学科', '中医科'],
      doctorDialogVisible: false,
      selectedDoctors: [],
      doctorList: ['张医生', '李医生', '王医生', '刘医生', '陈医生', '赵医生', '孙医生', '周医生', '吴医生', '郑医生'],
      // 日志记录
      operationLogs: []
    }
  },
  created() {
    const query = this.$route.query
    this.contentId = query.contentId
    this.contentType = query.contentType
    this.contentTitle = query.title || '内容配置'

    if (this.contentId && this.contentType) {
      this.fetchConfig()
      this.fetchLogs()
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
            relatedDiseases: config.relatedDiseases ? config.relatedDiseases.split(',').filter(item => item.trim()) : [],
            relatedSymptoms: config.relatedSymptoms ? config.relatedSymptoms.split(',').filter(item => item.trim()) : [],
            relatedProducts: config.relatedProducts ? config.relatedProducts.split(',').filter(item => item.trim()) : [],
            relatedDepartments: config.relatedDepartments || '',
            relatedDoctors: config.relatedDoctors || '',
            sortOrder: config.sortOrder || 0,
            isPinned: config.isPinned || 0
          }
          // 初始化已选择的科室和医生
          this.selectedDepartments = config.relatedDepartments ? config.relatedDepartments.split(',').filter(item => item.trim()) : []
          this.selectedDoctors = config.relatedDoctors ? config.relatedDoctors.split(',').filter(item => item.trim()) : []
        }
      } catch (error) {
        console.error('获取配置失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 获取日志记录
    async fetchLogs() {
      try {
        const response = await getConfigLogs(this.contentId, this.contentType)
        if (response && response.data) {
          this.operationLogs = response.data
        }
      } catch (error) {
        console.error('获取日志失败:', error)
      }
    },
    // 显示科室选择对话框
    showDepartmentDialog() {
      this.departmentDialogVisible = true
    },
    // 确认科室选择
    confirmDepartments() {
      this.configForm.relatedDepartments = this.selectedDepartments.join(',')
      this.departmentDialogVisible = false
    },
    // 显示医生选择对话框
    showDoctorDialog() {
      this.doctorDialogVisible = true
    },
    // 确认医生选择
    confirmDoctors() {
      this.configForm.relatedDoctors = this.selectedDoctors.join(',')
      this.doctorDialogVisible = false
    },
    // 保存配置
    async handleSave() {
      // 表单验证
      this.$refs.configForm.validate(async (valid) => {
        if (!valid) {
          return false
        }

        this.saving = true
        try {
          const data = {
            contentId: this.contentId,
            contentType: this.contentType,
            applicationProject: this.configForm.applicationProject,
            applicationModule: this.configForm.applicationModule,
            channel: this.configForm.channel,
            relatedDiseases: this.configForm.relatedDiseases.join(','),
            relatedSymptoms: this.configForm.relatedSymptoms.join(','),
            relatedProducts: this.configForm.relatedProducts.join(','),
            relatedDepartments: this.configForm.relatedDepartments,
            relatedDoctors: this.configForm.relatedDoctors,
            sortOrder: this.configForm.sortOrder,
            isPinned: this.configForm.isPinned,
            operator: '系统管理员'
          }

          await saveContentConfig(data)
          this.$message.success('保存成功')
          // 重新加载日志
          this.fetchLogs()
        } catch (error) {
          this.$message.error('保存失败')
          console.error('保存失败:', error)
        } finally {
          this.saving = false
        }
      })
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

    .relation-config {
      .relation-item {
        display: flex;
        align-items: center;
        margin-bottom: 12px;

        .relation-label {
          font-weight: 500;
          color: #606266;
          margin-right: 8px;
        }

        .relation-value {
          flex: 1;
          color: #303133;
          margin-right: 12px;
        }

        .el-link {
          font-size: 14px;
        }
      }
    }
  }

  .operation-logs {
    margin-top: 40px;
    padding-top: 30px;
    border-top: 2px solid #ebeef5;

    .logs-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 16px;
    }

    ::v-deep .el-table {
      th {
        background-color: rgb(248, 248, 249);
        color: rgb(81, 90, 110);
        font-weight: 500;
      }
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
