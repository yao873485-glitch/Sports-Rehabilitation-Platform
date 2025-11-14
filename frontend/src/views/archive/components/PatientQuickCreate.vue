<template>
  <div class="patient-quick-create">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      :disabled="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="formData.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄" prop="age">
            <el-input-number
              v-model="formData.age"
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
          <el-form-item label="性别" prop="gender">
            <el-select v-model="formData.gender" placeholder="请选择性别" style="width: 100%">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期" prop="birthDate">
            <el-date-picker
              v-model="formData.birthDate"
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
          <el-form-item label="证件号码" prop="idNumber">
            <el-input v-model="formData.idNumber" placeholder="请输入证件号码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="formData.phone" placeholder="请输入联系电话" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="档案号" prop="recordNumber">
            <el-input v-model="formData.recordNumber" placeholder="留空自动生成档案号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="病种" prop="disease">
            <el-input v-model="formData.disease" placeholder="请输入病种" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="入组机构" prop="institution">
            <el-input v-model="formData.institution" placeholder="请输入入组机构" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="居住地址" prop="address">
            <el-input v-model="formData.address" placeholder="请输入居住地址" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSave">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { addPatient } from '@/api/patient'

export default {
  name: 'PatientQuickCreate',
  data() {
    return {
      loading: false,
      formRef: null,
      formData: {
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
      formRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.loading = true

        const result = await addPatient(this.formData)

        this.$message.success('建档成功')
        this.$emit('saved')
        this.$emit('close')
        this.resetForm()
      } catch (error) {
        console.error('建档失败:', error)
        this.$message.error('建档失败')
      } finally {
        this.loading = false
      }
    },

    handleClose() {
      this.$emit('close')
    },

    resetForm() {
      this.formData = {
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
      }
      if (this.$refs.formRef) {
        this.$refs.formRef.clearValidate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.patient-quick-create {
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