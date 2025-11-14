<template>
  <div class="patient-detail-edit">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      :disabled="loading"
    >
      <!-- 基本信息 -->
      <el-card class="card-section" shadow="never">
        <template #header>
          <div class="card-header">
            <span class="header-title">基本信息</span>
            <span class="required-mark">* 表示必填项</span>
          </div>
        </template>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="basicInfo.name">
              <el-input v-model="formData.basicInfo.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="basicInfo.age">
              <el-input-number
                v-model="formData.basicInfo.age"
                :min="0"
                :max="150"
                placeholder="请输入年龄"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="basicInfo.gender">
              <el-select v-model="formData.basicInfo.gender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="basicInfo.birthDate">
              <el-date-picker
                v-model="formData.basicInfo.birthDate"
                type="date"
                placeholder="选择出生日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证件号码" prop="basicInfo.idNumber">
              <el-input v-model="formData.basicInfo.idNumber" placeholder="请输入证件号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="basicInfo.phone">
              <el-input v-model="formData.basicInfo.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="档案号" prop="basicInfo.recordNumber">
              <el-input v-model="formData.basicInfo.recordNumber" placeholder="请输入档案号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="病种" prop="basicInfo.disease">
              <el-input v-model="formData.basicInfo.disease" placeholder="请输入病种" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入组机构" prop="basicInfo.institution">
              <el-input v-model="formData.basicInfo.institution" placeholder="请输入入组机构" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="居住地址" prop="basicInfo.address">
              <el-input v-model="formData.basicInfo.address" placeholder="请输入居住地址" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 详细信息 -->
      <el-card class="card-section" shadow="never">
        <template #header>
          <div class="card-header">
            <span class="header-title">详细信息</span>
          </div>
        </template>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证件类型" prop="detailInfo.idType">
              <el-input v-model="formData.detailInfo.idType" placeholder="请输入证件类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="证件号" prop="detailInfo.idNumber">
              <el-input v-model="formData.detailInfo.idNumber" placeholder="请输入证件号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身高(cm)" prop="detailInfo.height">
              <el-input-number
                v-model="formData.detailInfo.height"
                :min="0"
                :max="250"
                placeholder="请输入身高"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生年月" prop="detailInfo.birthDate">
              <el-date-picker
                v-model="formData.detailInfo.birthDate"
                type="date"
                placeholder="选择出生年月"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="民族" prop="detailInfo.ethnicity">
              <el-input v-model="formData.detailInfo.ethnicity" placeholder="请输入民族" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职业" prop="detailInfo.occupation">
              <el-input v-model="formData.detailInfo.occupation" placeholder="请输入职业" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系地址" prop="detailInfo.contactAddress">
              <el-input v-model="formData.detailInfo.contactAddress" placeholder="请输入联系地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="详细地址" prop="detailInfo.detailedAddress">
              <el-input v-model="formData.detailInfo.detailedAddress" placeholder="请输入详细地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注说明" prop="detailInfo.remarks">
              <el-input
                v-model="formData.detailInfo.remarks"
                type="textarea"
                :rows="3"
                placeholder="请输入备注说明"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSave">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getPatientDetail, updatePatient, savePatientDetail } from '@/api/patient'

export default {
  name: 'PatientDetailEdit',
  props: {
    patientId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      formRef: null,
      formData: {
        basicInfo: {
          name: '',
          age: null,
          gender: '',
          birthDate: '',
          idNumber: '',
          phone: '',
          recordNumber: '',
          disease: '',
          institution: '',
          address: ''
        },
        detailInfo: {
          idType: '',
          idNumber: '',
          height: null,
          birthDate: '',
          ethnicity: '',
          occupation: '',
          contactAddress: '',
          detailedAddress: '',
          remarks: ''
        }
      },
      formRules: {
        'basicInfo.name': [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        'basicInfo.phone': [
          { required: true, message: '请输入联系电话', trigger: 'blur' }
        ],
        'basicInfo.recordNumber': [
          { required: true, message: '请输入档案号', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    patientId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadPatientDetail()
        }
      }
    }
  },
  methods: {
    async loadPatientDetail() {
      this.loading = true
      try {
        const response = await getPatientDetail(this.patientId)
        const data = response.data

        if (data.basicInfo) {
          this.formData.basicInfo = { ...data.basicInfo }
        }

        if (data.detailInfo) {
          this.formData.detailInfo = { ...data.detailInfo }
        }
      } catch (error) {
        console.error('加载患者详情失败:', error)
        this.$message.error('加载患者详情失败')
      } finally {
        this.loading = false
      }
    },

    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.loading = true

        // 确保包含ID字段
        const basicData = {
          ...this.formData.basicInfo,
          id: this.patientId
        }

        // 更新基本信息
        const basicResult = await updatePatient(basicData)

        // 保存详细信息
        const detailData = {
          ...this.formData.detailInfo,
          basicId: this.patientId
        }
        const detailResult = await savePatientDetail(detailData)

        // 检查响应状态码而不是success字段
        if (basicResult.code === 200 && detailResult.code === 200) {
          this.$message.success('保存成功')
          this.$emit('saved')
          this.$emit('close')
        } else {
          this.$message.error(basicResult.message || detailResult.message || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error(error.response?.data?.message || error.message || '保存失败')
      } finally {
        this.loading = false
      }
    },

    handleClose() {
      this.$emit('close')
    }
  }
}
</script>

<style lang="scss" scoped>
.patient-detail-edit {
  .card-section {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-title {
        font-weight: 600;
        font-size: 16px;
      }

      .required-mark {
        color: #f56c6c;
        font-size: 12px;
      }
    }
  }

  .action-buttons {
    text-align: center;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;

    .el-button {
      margin: 0 10px;
    }
  }
}
</style>