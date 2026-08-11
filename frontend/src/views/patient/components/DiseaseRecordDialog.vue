<template>
  <el-dialog
    :visible.sync="dialogVisible"
    title="专病档案"
    width="900px"
    :close-on-click-modal="false"
    class="disease-record-dialog"
  >
    <el-form
      ref="diseaseForm"
      :model="form"
      :disabled="readonly"
      label-width="100%"
      label-position="top"
      class="disease-form"
    >
      <!-- 运动前筛查结果 -->
      <el-divider content-position="left"><h3>运动前筛查结果</h3></el-divider>

      <el-form-item label="LPA（min）">
        <el-input v-model.number="form.lpaMin" type="number" placeholder="请输入LPA" />
      </el-form-item>

      <el-form-item label="MPA（min）">
        <el-input v-model.number="form.mpaMin" type="number" placeholder="请输入MPA" />
      </el-form-item>

      <el-form-item label="VPA（min）">
        <el-input v-model.number="form.vpaMin" type="number" placeholder="请输入VPA" />
      </el-form-item>

      <!-- 体力活动水平 -->
      <el-divider content-position="left"><h3>体力活动水平</h3></el-divider>

      <el-form-item label="身高（cm）">
        <el-input-number
          v-model="form.heightCm"
          :min="0"
          :precision="2"
          controls-position="right"
          style="width: 100%;"
        />
      </el-form-item>

      <el-form-item label="体重（kg）">
        <el-input-number
          v-model="form.weightKg"
          :min="0"
          :precision="2"
          controls-position="right"
          style="width: 100%;"
        />
      </el-form-item>

      <el-form-item label="BMI（kg/m2）">
        <el-input-number
          v-model="form.bmi"
          :min="0"
          :precision="2"
          controls-position="right"
          style="width: 100%;"
          disabled
        />
      </el-form-item>

      <el-form-item label="腰围（cm）">
        <el-input-number
          v-model="form.waistCm"
          :min="0"
          :precision="2"
          controls-position="right"
          style="width: 100%;"
        />
      </el-form-item>

      <el-form-item label="臀围（cm）">
        <el-input-number
          v-model="form.hipCm"
          :min="0"
          :precision="2"
          controls-position="right"
          style="width: 100%;"
        />
      </el-form-item>

      <el-form-item label="腰臀比">
        <el-input-number
          v-model="form.waistHipRatio"
          :min="0"
          :precision="2"
          controls-position="right"
          style="width: 100%;"
          disabled
        />
      </el-form-item>

      <!-- 疾病史 -->
      <el-divider content-position="left"><h3>疾病史</h3></el-divider>

      <el-form-item label="疾病史">
        <el-checkbox-group v-model="diseaseHistoryList">
          <div style="display: flex; flex-direction: column; gap: 10px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="高血压">高血压</el-checkbox>
            <el-checkbox label="糖尿病">糖尿病</el-checkbox>
            <el-checkbox label="心脏病">心脏病</el-checkbox>
            <el-checkbox label="肺部疾病">肺部疾病</el-checkbox>
            <el-checkbox label="骨折史">骨折史</el-checkbox>
            <el-checkbox label="其他">其他</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item v-if="diseaseHistoryList.includes('其他')" label="如有其他请说明">
        <el-input
          v-model="form.diseaseHistoryDetail"
          type="textarea"
          :rows="3"
          placeholder="请详细说明其他疾病史"
        />
      </el-form-item>

      <!-- 家族史 -->
      <el-divider content-position="left"><h3>家族史</h3></el-divider>

      <el-form-item label="家族史">
        <el-checkbox-group v-model="familyHistoryList">
          <div style="display: flex; flex-direction: column; gap: 10px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="高血压">高血压</el-checkbox>
            <el-checkbox label="糖尿病">糖尿病</el-checkbox>
            <el-checkbox label="心脏病">心脏病</el-checkbox>
            <el-checkbox label="肺部疾病">肺部疾病</el-checkbox>
            <el-checkbox label="骨折史">骨折史</el-checkbox>
            <el-checkbox label="其他">其他</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item v-if="familyHistoryList.includes('其他')" label="如有其他请说明">
        <el-input
          v-model="form.familyHistoryDetail"
          type="textarea"
          :rows="3"
          placeholder="请详细说明其他家族史"
        />
      </el-form-item>

      <!-- 血液指标 -->
      <el-divider content-position="left"><h3>血液指标</h3></el-divider>

      <el-form-item label="空腹血糖（mmol/L）">
        <el-input v-model.number="form.fastingGlucose" type="number" placeholder="请输入空腹血糖" />
      </el-form-item>

      <el-form-item label="总胆固醇（mmol/L）">
        <el-input v-model.number="form.totalCholesterol" type="number" placeholder="请输入总胆固醇" />
      </el-form-item>

      <el-form-item label="高密度脂蛋白（mmol/L）">
        <el-input v-model.number="form.hdlCholesterol" type="number" placeholder="请输入高密度脂蛋白" />
      </el-form-item>

      <el-form-item label="低密度脂蛋白（mmol/L）">
        <el-input v-model.number="form.ldlCholesterol" type="number" placeholder="请输入低密度脂蛋白" />
      </el-form-item>

      <el-form-item label="血压（mmHg）">
        <el-input v-model="form.bloodPressure" placeholder="例如：120/80" />
      </el-form-item>

      <el-form-item label="心率（次/分）">
        <el-input-number
          v-model="form.heartRate"
          :min="0"
          controls-position="right"
          style="width: 100%;"
        />
      </el-form-item>

      <!-- 健康筛查 -->
      <el-divider content-position="left"><h3>健康筛查</h3></el-divider>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">生活方式</h4>

      <el-form-item label="静坐少动">
        <el-radio-group v-model="form.sedentary">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="吸烟">
        <el-radio-group v-model="form.smoking">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="饮酒">
        <el-radio-group v-model="form.drinking">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="阳光照射不足">
        <el-radio-group v-model="form.insufficientSunlight">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="过量含咖啡因的饮料">
        <el-radio-group v-model="form.excessiveCaffeine">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">饮食情况</h4>

      <el-form-item label="蛋白质摄入量">
        <el-radio-group v-model="form.proteinIntake">
          <el-radio label="过多">过多</el-radio>
          <el-radio label="正常">正常</el-radio>
          <el-radio label="不足">不足</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="高钙饮食">
        <el-radio-group v-model="form.highCalciumDiet">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="钙和/或维生素D缺乏">
        <el-radio-group v-model="form.calciumVdDeficiency">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">药物使用情况</h4>

      <el-form-item label="药物使用情况">
        <el-checkbox-group v-model="medicationList">
          <div style="display: flex; flex-direction: column; gap: 10px;">
            <el-checkbox label="糖皮质激素">糖皮质激素</el-checkbox>
            <el-checkbox label="质子泵抑制剂">质子泵抑制剂</el-checkbox>
            <el-checkbox label="抗癫痫药物">抗癫痫药物</el-checkbox>
            <el-checkbox label="抗病毒药物">抗病毒药物</el-checkbox>
            <el-checkbox label="噻吩烷二酮类药物">噻吩烷二酮类药物</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>

      <!-- 运动风险分级 -->
      <el-divider content-position="left"><h3>运动风险分级</h3></el-divider>

      <el-form-item label="运动风险分级">
        <el-radio-group v-model="form.exerciseRiskLevel">
          <el-radio label="低">低</el-radio>
          <el-radio label="中">中</el-radio>
          <el-radio label="高">高</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 医学检查 -->
      <el-divider content-position="left"><h3>医学检查</h3></el-divider>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">骨折</h4>
      <el-form-item label="骨折">
        <el-radio-group v-model="form.fractureHistory">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.fractureHistory === '有'" label="如有请说明">
        <el-input
          v-model="form.fractureDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明骨折情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">血常规</h4>
      <el-form-item label="血常规">
        <el-radio-group v-model="form.bloodRoutineStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.bloodRoutineStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.bloodRoutineDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">尿常规</h4>
      <el-form-item label="尿常规">
        <el-radio-group v-model="form.urineRoutineStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.urineRoutineStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.urineRoutineDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">肝功能</h4>
      <el-form-item label="肝功能">
        <el-radio-group v-model="form.liverFunctionStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.liverFunctionStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.liverFunctionDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">肾功能</h4>
      <el-form-item label="肾功能">
        <el-radio-group v-model="form.kidneyFunctionStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.kidneyFunctionStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.kidneyFunctionDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">血脂</h4>
      <el-form-item label="血脂">
        <el-radio-group v-model="form.bloodLipidStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.bloodLipidStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.bloodLipidDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">糖化血红蛋白</h4>
      <el-form-item label="糖化血红蛋白">
        <el-radio-group v-model="form.hba1cStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.hba1cStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.hba1cDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">空腹血糖</h4>
      <el-form-item label="空腹血糖">
        <el-radio-group v-model="form.fastingGlucoseStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.fastingGlucoseStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.fastingGlucoseDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">胰岛功能</h4>
      <el-form-item label="胰岛功能">
        <el-radio-group v-model="form.pancreaticFunctionStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.pancreaticFunctionStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.pancreaticFunctionDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">胰岛功能/C肽</h4>
      <el-form-item label="胰岛功能/C肽">
        <el-radio-group v-model="form.cPeptideStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.cPeptideStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.cPeptideDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">维生素D</h4>
      <el-form-item label="维生素D">
        <el-radio-group v-model="form.vitaminDStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.vitaminDStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.vitaminDDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">D二聚体</h4>
      <el-form-item label="D二聚体">
        <el-radio-group v-model="form.dDimerStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.dDimerStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.dDimerDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <!-- 影像学检查 -->
      <el-divider content-position="left"><h3>影像学检查</h3></el-divider>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">彩超</h4>

      <el-form-item label="子宫附件">
        <el-radio-group v-model="form.ultrasoundUterusStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.ultrasoundUterusStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.ultrasoundUterusDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <el-form-item label="乳腺">
        <el-radio-group v-model="form.ultrasoundBreastStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.ultrasoundBreastStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.ultrasoundBreastDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <el-form-item label="肝胆胰脾">
        <el-radio-group v-model="form.ultrasoundLiverStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.ultrasoundLiverStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.ultrasoundLiverDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">骨密度</h4>
      <el-form-item label="骨密度">
        <el-radio-group v-model="form.boneDensityStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.boneDensityStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.boneDensityDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">CT腹部脂肪容积</h4>
      <el-form-item label="CT腹部脂肪容积">
        <el-radio-group v-model="form.ctAbdominalFatStatus">
          <el-radio label="正常">正常</el-radio>
          <el-radio label="异常">异常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.ctAbdominalFatStatus === '异常'" label="异常有请说明">
        <el-input
          v-model="form.ctAbdominalFatDetail"
          type="textarea"
          :rows="2"
          placeholder="请说明异常情况"
        />
      </el-form-item>

      <!-- 运动与体能评估 -->
      <el-divider content-position="left"><h3>运动与体能评估</h3></el-divider>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">心肺功能</h4>

      <el-form-item label="最大心率">
        <el-input v-model.number="form.maxHeartRate" type="number" placeholder="请输入最大心率" />
      </el-form-item>

      <el-form-item label="6MWT">
        <el-input v-model.number="form.sixMwt" type="number" placeholder="请输入6MWT" />
      </el-form-item>

      <el-form-item label="VO₂max">
        <el-input v-model.number="form.vo2Max" type="number" placeholder="请输入VO₂max" />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">肌肉力量</h4>

      <el-form-item label="上肢1-RM">
        <el-input v-model.number="form.upperLimb1rm" type="number" placeholder="请输入上肢1-RM" />
      </el-form-item>

      <el-form-item label="下肢1-RM">
        <el-input v-model.number="form.lowerLimb1rm" type="number" placeholder="请输入下肢1-RM" />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">肌肉耐力</h4>

      <el-form-item label="上肢（次）">
        <el-input-number
          v-model="form.upperLimbEndurance"
          :min="0"
          controls-position="right"
          style="width: 100%;"
        />
      </el-form-item>

      <el-form-item label="下肢（次）">
        <el-input-number
          v-model="form.lowerLimbEndurance"
          :min="0"
          controls-position="right"
          style="width: 100%;"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">柔韧性</h4>

      <el-form-item label="坐位体前屈（cm）">
        <el-input v-model.number="form.sitReachCm" type="number" placeholder="请输入坐位体前屈" />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">平衡测试</h4>

      <el-form-item label="单推闭眼站立(左)(s)">
        <el-input v-model.number="form.balanceLeftEyeClosed" type="number" placeholder="请输入时间" />
      </el-form-item>

      <el-form-item label="单推闭眼站立(右)(s)">
        <el-input v-model.number="form.balanceRightEyeClosed" type="number" placeholder="请输入时间" />
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item style="margin-top: 40px; text-align: center;">
        <el-button @click="handleClose">取消</el-button>
        <el-button
          v-if="!readonly"
          type="primary"
          :loading="submitLoading"
          style="background-color: rgb(106, 91, 140); border-color: rgb(106, 91, 140);"
          @click="handleSubmit"
        >
          提交
        </el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { saveDiseaseRecord, getDiseaseRecordByPatientId } from '@/api/disease-record'

export default {
  name: 'DiseaseRecordDialog',
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
      type: [Number, String],
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      // 复选框列表
      diseaseHistoryList: [],
      familyHistoryList: [],
      medicationList: [],
      form: {
        // 运动前筛查结果
        lpaMin: null,
        mpaMin: null,
        vpaMin: null,
        // 体力活动水平
        heightCm: null,
        weightKg: null,
        bmi: null,
        waistCm: null,
        hipCm: null,
        waistHipRatio: null,
        // 疾病史
        diseaseHistoryDetail: '',
        // 家族史
        familyHistoryDetail: '',
        // 血液指标
        fastingGlucose: null,
        totalCholesterol: null,
        hdlCholesterol: null,
        ldlCholesterol: null,
        bloodPressure: '',
        heartRate: null,
        // 健康筛查 - 生活方式
        sedentary: '',
        smoking: '',
        drinking: '',
        insufficientSunlight: '',
        excessiveCaffeine: '',
        // 健康筛查 - 饮食情况
        proteinIntake: '',
        highCalciumDiet: '',
        calciumVdDeficiency: '',
        // 运动风险分级
        exerciseRiskLevel: '',
        // 医学检查
        fractureHistory: '',
        fractureDetail: '',
        bloodRoutineStatus: '',
        bloodRoutineDetail: '',
        urineRoutineStatus: '',
        urineRoutineDetail: '',
        liverFunctionStatus: '',
        liverFunctionDetail: '',
        kidneyFunctionStatus: '',
        kidneyFunctionDetail: '',
        bloodLipidStatus: '',
        bloodLipidDetail: '',
        hba1cStatus: '',
        hba1cDetail: '',
        fastingGlucoseStatus: '',
        fastingGlucoseDetail: '',
        pancreaticFunctionStatus: '',
        pancreaticFunctionDetail: '',
        cPeptideStatus: '',
        cPeptideDetail: '',
        vitaminDStatus: '',
        vitaminDDetail: '',
        dDimerStatus: '',
        dDimerDetail: '',
        // 影像学检查
        ultrasoundUterusStatus: '',
        ultrasoundUterusDetail: '',
        ultrasoundBreastStatus: '',
        ultrasoundBreastDetail: '',
        ultrasoundLiverStatus: '',
        ultrasoundLiverDetail: '',
        boneDensityStatus: '',
        boneDensityDetail: '',
        ctAbdominalFatStatus: '',
        ctAbdominalFatDetail: '',
        // 运动与体能评估
        maxHeartRate: null,
        sixMwt: null,
        vo2Max: null,
        upperLimb1rm: null,
        lowerLimb1rm: null,
        upperLimbEndurance: null,
        lowerLimbEndurance: null,
        sitReachCm: null,
        balanceLeftEyeClosed: null,
        balanceRightEyeClosed: null
      }
    }
  },
  computed: {
    // 自动计算BMI
    calculatedBmi() {
      if (this.form.heightCm && this.form.weightKg && this.form.heightCm > 0) {
        const heightM = this.form.heightCm / 100
        return (this.form.weightKg / (heightM * heightM)).toFixed(2)
      }
      return null
    },
    // 自动计算腰臀比
    calculatedWaistHipRatio() {
      if (this.form.waistCm && this.form.hipCm && this.form.hipCm > 0) {
        return (this.form.waistCm / this.form.hipCm).toFixed(2)
      }
      return null
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.patientId) {
        this.loadDiseaseRecord()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    },
    patientId(val) {
      console.log('DiseaseRecordDialog - patientId 变化:', val)
    },
    // 自动计算BMI
    'form.heightCm': function() {
      if (this.calculatedBmi) {
        this.form.bmi = parseFloat(this.calculatedBmi)
      }
    },
    'form.weightKg': function() {
      if (this.calculatedBmi) {
        this.form.bmi = parseFloat(this.calculatedBmi)
      }
    },
    // 自动计算腰臀比
    'form.waistCm': function() {
      if (this.calculatedWaistHipRatio) {
        this.form.waistHipRatio = parseFloat(this.calculatedWaistHipRatio)
      }
    },
    'form.hipCm': function() {
      if (this.calculatedWaistHipRatio) {
        this.form.waistHipRatio = parseFloat(this.calculatedWaistHipRatio)
      }
    }
  },
  mounted() {
    console.log('DiseaseRecordDialog mounted - patientId:', this.patientId)
  },
  methods: {
    /** 加载专病档案数据 */
    async loadDiseaseRecord() {
      try {
        const response = await getDiseaseRecordByPatientId(this.patientId)
        if (response && response.data) {
          const data = response.data
          // 填充表单数据
          Object.keys(this.form).forEach(key => {
            if (data[key] !== undefined && data[key] !== null) {
              this.form[key] = data[key]
            }
          })
          // 填充复选框列表
          this.diseaseHistoryList = data.diseaseHistoryList || []
          this.familyHistoryList = data.familyHistoryList || []
          this.medicationList = data.medicationList || []
        }
      } catch (error) {
        console.error('加载专病档案数据失败:', error)
        // 如果查询失败，不显示错误，允许用户新建
      }
    },

    /** 提交表单 */
    handleSubmit() {
      if (this.readonly) return
      this.submitLoading = true

      // 确保 patientId 是数字类型
      const patientId = typeof this.patientId === 'string'
        ? parseInt(this.patientId)
        : this.patientId

      console.log('原始 patientId:', this.patientId, '类型:', typeof this.patientId)
      console.log('转换后 patientId:', patientId, '类型:', typeof patientId)

      if (!patientId || isNaN(patientId)) {
        this.$message.error('患者ID无效，请重新打开页面')
        this.submitLoading = false
        return
      }

      // 准备提交数据
      const submitData = {
        patientId: patientId,
        ...this.form,
        // 将复选框列表添加到提交数据
        diseaseHistoryList: this.diseaseHistoryList,
        familyHistoryList: this.familyHistoryList,
        medicationList: this.medicationList
      }

      console.log('提交专病档案数据:', submitData)
      console.log('患者ID:', patientId)

      // 调用保存API
      saveDiseaseRecord(submitData)
        .then(response => {
          this.$message.success('专病档案保存成功')
          this.$emit('saved', response)
          this.dialogVisible = false
        })
        .catch(error => {
          this.$message.error('保存失败: ' + (error.message || '未知错误'))
        })
        .finally(() => {
          this.submitLoading = false
        })
    },

    /** 关闭对话框 */
    handleClose() {
      this.$confirm('确定关闭吗？未保存的数据将丢失', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.dialogVisible = false
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.disease-record-dialog {
  ::v-deep .el-dialog__body {
    max-height: 70vh;
    overflow-y: auto;
  }
}

.disease-form {
  h3 {
    margin: 0;
    font-size: 16px;
    color: #303133;
  }

  h4 {
    margin: 0;
    font-size: 14px;
    color: #606266;
  }

  ::v-deep .el-divider__text {
    background-color: #fff;
  }

  // 标签在上方，左对齐
  ::v-deep .el-form-item__label {
    text-align: left !important;
    padding-bottom: 8px;
    padding-right: 0;
    font-weight: 500;
    color: #606266;
  }

  // 输入框和选项左对齐（但按钮区域除外）
  ::v-deep .el-form-item__content {
    text-align: left;

    // 按钮区域保持居中
    .el-button {
      &:first-child {
        margin-right: 10px;
      }
    }
  }

  // 专门设置按钮区域居中
  ::v-deep .el-form-item[style*="text-align: center"] .el-form-item__content {
    text-align: center !important;
  }

  // 单选框和复选框左对齐，垂直排列
  ::v-deep .el-radio-group,
  ::v-deep .el-checkbox-group {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  // 单选/复选项之间的间距
  ::v-deep .el-radio,
  ::v-deep .el-checkbox {
    margin-right: 30px;
    margin-bottom: 10px;
  }
}
</style>
