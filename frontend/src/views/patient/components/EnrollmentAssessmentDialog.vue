<template>
  <el-dialog
    :visible.sync="dialogVisible"
    title="入组评估（运动前健康评估问卷 PAR-Q）"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="assessmentForm"
      :model="form"
      :rules="rules"
      :disabled="readonly"
      label-width="400px"
      label-position="left"
      style="max-height: 600px; overflow-y: auto; padding: 0 20px;"
    >
      <!-- 一般健康问题 -->
      <div class="section-title">一般健康问题</div>

      <el-form-item label="1. 您是否被医生诊断患有心脏疾病？" prop="generalQ1">
        <el-radio-group v-model="form.generalQ1">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="2. 您在休息或日常活动中是否感到胸部疼痛？" prop="generalQ2">
        <el-radio-group v-model="form.generalQ2">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="3. 在过去的一个月中，您是否在没有进行体力活动时感到胸部疼痛？" prop="generalQ3">
        <el-radio-group v-model="form.generalQ3">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="4. 您是否因为头晕而失去平衡，或者曾经失去知觉？" prop="generalQ4">
        <el-radio-group v-model="form.generalQ4">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="5. 您是否有骨骼或关节问题，可能会因为体力活动的改变而恶化？" prop="generalQ5">
        <el-radio-group v-model="form.generalQ5">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="6. 您目前是否正在服用治疗血压或心脏疾病的药物？" prop="generalQ6">
        <el-radio-group v-model="form.generalQ6">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="7. 您是否知道有任何其他原因不应进行体力活动？" prop="generalQ7">
        <el-radio-group v-model="form.generalQ7">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 医疗相关问题 -->
      <div class="section-title">医疗相关问题</div>

      <!-- 问题1 -->
      <el-form-item label="1. 您现在是否怀孕或在过去3个月内分娩？" prop="medicalQ1">
        <el-radio-group v-model="form.medicalQ1">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ1 === '是'">
        <el-form-item label="1a. 您现在是否处于产后6周以内？" prop="medicalQ1a" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ1a">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="1b. 您是否有妊娠并发症？" prop="medicalQ1b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ1b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="form.medicalQ1b === '是'" label="1c. 如果是，请具体说明" prop="medicalQ1c" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ1c" type="textarea" :rows="2" placeholder="请具体说明" />
        </el-form-item>
      </template>

      <!-- 问题2 -->
      <el-form-item label="2. 您是否患有心脏疾病、心血管疾病或脑血管疾病？" prop="medicalQ2">
        <el-radio-group v-model="form.medicalQ2">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ2 === '是'">
        <el-form-item label="2a. 您是否已完成心脏康复计划？" prop="medicalQ2a" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ2a">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="2b. 您是否已获得运动许可？" prop="medicalQ2b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ2b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 问题3 -->
      <el-form-item label="3. 您是否患有高血压？" prop="medicalQ3">
        <el-radio-group v-model="form.medicalQ3">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ3 === '是'">
        <el-form-item label="3a. 您目前是否正在服用治疗高血压的药物？" prop="medicalQ3a" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ3a">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="3b. 您的静息血压是多少？" prop="medicalQ3b" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ3b" placeholder="例如：120/80 mmHg" style="width: 300px;" />
        </el-form-item>

        <el-form-item label="3c. 您的血压是否超过 160/90 mmHg？" prop="medicalQ3c" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ3c">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="3d. 您是否已获得运动许可？" prop="medicalQ3d" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ3d">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 问题4 -->
      <el-form-item label="4. 您是否有任何代谢疾病？" prop="medicalQ4">
        <el-radio-group v-model="form.medicalQ4">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ4 === '是'">
        <el-form-item label="4a. 请列出您的代谢疾病" prop="medicalQ4a" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ4a" type="textarea" :rows="2" placeholder="请列出您的代谢疾病" />
        </el-form-item>

        <el-form-item label="4b. 您是否已获得运动许可？" prop="medicalQ4b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ4b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 问题5 -->
      <el-form-item label="5. 您是否有任何精神健康问题或学习困难？" prop="medicalQ5">
        <el-radio-group v-model="form.medicalQ5">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ5 === '是'">
        <el-form-item label="5a. 您是否正在接受治疗或正在服用药物？" prop="medicalQ5a" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ5a" type="textarea" :rows="2" placeholder="请说明" />
        </el-form-item>

        <el-form-item label="5b. 您的疾病或用药是否影响您的运动能力？" prop="medicalQ5b" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ5b" type="textarea" :rows="2" placeholder="请说明" />
        </el-form-item>

        <el-form-item label="5c. 您是否已获得运动许可？" prop="medicalQ5c" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ5c" type="textarea" :rows="2" placeholder="请说明" />
        </el-form-item>

        <el-form-item label="5d. 请具体说明" prop="medicalQ5d" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ5d" type="textarea" :rows="2" placeholder="请具体说明" />
        </el-form-item>

        <el-form-item label="5e. 其他相关信息" prop="medicalQ5e" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ5e" type="textarea" :rows="2" placeholder="其他相关信息" />
        </el-form-item>
      </template>

      <!-- 问题6 -->
      <el-form-item label="6. 您是否患有任何呼吸系统疾病？" prop="medicalQ6">
        <el-radio-group v-model="form.medicalQ6">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ6 === '是'">
        <el-form-item label="6a. 请具体说明您的呼吸系统疾病" prop="medicalQ6a" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ6a" type="textarea" :rows="2" placeholder="请具体说明" />
        </el-form-item>

        <el-form-item label="6b. 您是否已获得运动许可？" prop="medicalQ6b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ6b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 问题7 -->
      <el-form-item label="7. 您是否有任何肌肉、骨骼或关节问题？" prop="medicalQ7">
        <el-radio-group v-model="form.medicalQ7">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ7 === '是'">
        <el-form-item label="7a. 请具体说明您的问题" prop="medicalQ7a" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ7a" type="textarea" :rows="2" placeholder="请具体说明" />
        </el-form-item>

        <el-form-item label="7b. 您的问题是否影响您进行体力活动？" prop="medicalQ7b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ7b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="7c. 您是否已完成康复治疗？" prop="medicalQ7c" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ7c">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="7d. 您是否已获得运动许可？" prop="medicalQ7d" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ7d">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 问题8 -->
      <el-form-item label="8. 您是否有任何其他慢性疾病？" prop="medicalQ8">
        <el-radio-group v-model="form.medicalQ8">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ8 === '是'">
        <el-form-item label="8a. 请列出您的慢性疾病" prop="medicalQ8a" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ8a" type="textarea" :rows="2" placeholder="请列出您的慢性疾病" />
        </el-form-item>

        <el-form-item label="8b. 您的疾病是否得到控制？" prop="medicalQ8b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ8b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="8c. 您是否已获得运动许可？" prop="medicalQ8c" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ8c">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 问题9 -->
      <el-form-item label="9. 您目前是否正在服用任何药物？" prop="medicalQ9">
        <el-radio-group v-model="form.medicalQ9">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ9 === '是'">
        <el-form-item label="9a. 请列出所有药物" prop="medicalQ9a" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ9a" type="textarea" :rows="2" placeholder="请列出所有药物" />
        </el-form-item>

        <el-form-item label="9b. 这些药物是否影响您的运动能力？" prop="medicalQ9b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ9b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="form.medicalQ9b === '是'" label="9c. 如果是，请具体说明" prop="medicalQ9c" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ9c" type="textarea" :rows="2" placeholder="请具体说明" />
        </el-form-item>
      </template>

      <!-- 问题10 -->
      <el-form-item label="10. 您是否有任何其他健康问题？" prop="medicalQ10">
        <el-radio-group v-model="form.medicalQ10">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="form.medicalQ10 === '是'">
        <el-form-item label="10a. 请具体说明" prop="medicalQ10a" style="margin-left: 30px;">
          <el-input v-model="form.medicalQ10a" type="textarea" :rows="2" placeholder="请具体说明" />
        </el-form-item>

        <el-form-item label="10b. 这些问题是否影响您的运动能力？" prop="medicalQ10b" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ10b">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="10c. 您是否已获得运动许可？" prop="medicalQ10c" style="margin-left: 30px;">
          <el-radio-group v-model="form.medicalQ10c">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 运动建议 -->
      <div class="section-title">运动建议</div>

      <el-form-item label="推荐的运动类型和强度" prop="recommendation1">
        <el-input
          v-model="form.recommendation1"
          type="textarea"
          :rows="3"
          placeholder="请填写推荐的运动类型和强度"
        />
      </el-form-item>

      <el-form-item label="注意事项和限制" prop="recommendation2">
        <el-input
          v-model="form.recommendation2"
          type="textarea"
          :rows="3"
          placeholder="请填写注意事项和限制"
        />
      </el-form-item>

      <el-form-item label="后续随访计划" prop="recommendation3">
        <el-input
          v-model="form.recommendation3"
          type="textarea"
          :rows="3"
          placeholder="请填写后续随访计划"
        />
      </el-form-item>

      <!-- 评估人员信息 -->
      <div class="section-title">评估人员信息</div>

      <el-form-item label="评估人员姓名" prop="assessorName">
        <el-input v-model="form.assessorName" placeholder="请输入评估人员姓名" style="width: 300px;" />
      </el-form-item>

      <el-form-item label="评估日期" prop="assessmentDate">
        <el-date-picker
          v-model="form.assessmentDate"
          type="datetime"
          placeholder="选择评估日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 300px;"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="!readonly" type="primary" :loading="submitLoading" @click="handleSubmit">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveEnrollmentAssessment, getEnrollmentAssessmentByPatientId } from '@/api/enrollment-assessment'

export default {
  name: 'EnrollmentAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    patientId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      form: {
        patientId: null,
        // 一般健康问题
        generalQ1: '',
        generalQ2: '',
        generalQ3: '',
        generalQ4: '',
        generalQ5: '',
        generalQ6: '',
        generalQ7: '',
        // 医疗相关问题1
        medicalQ1: '',
        medicalQ1a: '',
        medicalQ1b: '',
        medicalQ1c: '',
        // 医疗相关问题2
        medicalQ2: '',
        medicalQ2a: '',
        medicalQ2b: '',
        // 医疗相关问题3
        medicalQ3: '',
        medicalQ3a: '',
        medicalQ3b: '',
        medicalQ3c: '',
        medicalQ3d: '',
        // 医疗相关问题4
        medicalQ4: '',
        medicalQ4a: '',
        medicalQ4b: '',
        // 医疗相关问题5
        medicalQ5: '',
        medicalQ5a: '',
        medicalQ5b: '',
        medicalQ5c: '',
        medicalQ5d: '',
        medicalQ5e: '',
        // 医疗相关问题6
        medicalQ6: '',
        medicalQ6a: '',
        medicalQ6b: '',
        // 医疗相关问题7
        medicalQ7: '',
        medicalQ7a: '',
        medicalQ7b: '',
        medicalQ7c: '',
        medicalQ7d: '',
        // 医疗相关问题8
        medicalQ8: '',
        medicalQ8a: '',
        medicalQ8b: '',
        medicalQ8c: '',
        // 医疗相关问题9
        medicalQ9: '',
        medicalQ9a: '',
        medicalQ9b: '',
        medicalQ9c: '',
        // 医疗相关问题10
        medicalQ10: '',
        medicalQ10a: '',
        medicalQ10b: '',
        medicalQ10c: '',
        // 运动建议
        recommendation1: '',
        recommendation2: '',
        recommendation3: '',
        // 评估人员信息
        assessorName: '',
        assessmentDate: ''
      },
      rules: {
        // 可以根据需要添加验证规则
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.loadData()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  mounted() {
    console.log('EnrollmentAssessmentDialog mounted - patientId:', this.patientId)
  },
  methods: {
    // 加载数据
    async loadData() {
      console.log('加载入组评估数据 - patientId:', this.patientId)

      if (!this.patientId) {
        console.error('患者ID为空')
        this.$message.error('患者ID不能为空')
        return
      }

      try {
        const response = await getEnrollmentAssessmentByPatientId(this.patientId)
        console.log('获取入组评估响应:', response)

        if (response && response.data) {
          // 将数据填充到表单中
          this.form = {
            ...this.form,
            ...response.data
          }
          console.log('入组评估数据加载成功')
        } else {
          // 新建，设置患者ID
          this.form.patientId = this.patientId
          console.log('新建入组评估，患者ID:', this.patientId)
        }
      } catch (error) {
        console.error('加载入组评估失败:', error)
        // 如果是404错误，说明还没有评估记录，这是正常的
        if (error.response && error.response.status === 404) {
          this.form.patientId = this.patientId
          console.log('患者暂无入组评估记录，新建')
        } else {
          this.$message.error('加载入组评估失败: ' + (error.message || '未知错误'))
        }
      }
    },

    // 提交表单
    async handleSubmit() {
      if (this.readonly) return
      console.log('提交入组评估表单')

      this.$refs.assessmentForm.validate(async(valid) => {
        if (!valid) {
          this.$message.error('请完善必填项')
          return
        }

        this.submitLoading = true

        try {
          // 确保 patientId 存在
          if (!this.form.patientId) {
            this.form.patientId = this.patientId
          }

          console.log('提交的入组评估数据:', this.form)

          const response = await saveEnrollmentAssessment(this.form)
          console.log('保存入组评估响应:', response)

          this.$message.success('保存成功')
          this.submitLoading = false
          this.$emit('saved', response.data)
          this.handleClose()
        } catch (error) {
          console.error('保存入组评估失败:', error)
          this.$message.error('保存失败: ' + (error.message || '未知错误'))
          this.submitLoading = false
        }
      })
    },

    // 关闭弹窗
    handleClose() {
      this.dialogVisible = false
      this.$refs.assessmentForm.resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
.section-title {
  font-size: 18px;
  font-weight: bold;
  color: rgb(81, 90, 110);
  margin: 20px 0 15px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid rgb(106, 91, 140);
}

::v-deep .el-form-item {
  margin-bottom: 18px;
}

::v-deep .el-form-item__label {
  color: rgb(81, 90, 110);
  font-size: 14px;
  line-height: 1.5;
}

::v-deep .el-radio {
  margin-right: 20px;
}

.dialog-footer {
  text-align: center;
  padding: 10px 0;
}

::v-deep .el-button--primary {
  background-color: rgb(106, 91, 140);
  border-color: rgb(106, 91, 140);

  &:hover {
    background-color: rgba(106, 91, 140, 0.8);
    border-color: rgba(106, 91, 140, 0.8);
  }
}
</style>
