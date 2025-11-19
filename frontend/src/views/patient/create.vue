<template>
  <div class="app-container">
    <el-card>
      <!-- 标签页 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 基本信息标签页 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicForm"
            :model="basicForm"
            :rules="basicRules"
            label-width="140px"
            style="max-width: 700px; padding: 20px 0;"
          >
            <el-form-item label="客户姓名" prop="name">
              <el-input v-model="basicForm.name" placeholder="请输入客户姓名" :disabled="isReadonly" clearable />
            </el-form-item>

            <el-form-item label="客户昵称">
              <el-input v-model="basicForm.nickname" placeholder="请输入客户昵称" :disabled="isReadonly" clearable />
            </el-form-item>

            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="basicForm.phone"
                placeholder="请输入联系电话"
                maxlength="11"
                :disabled="isReadonly"
                clearable
              />
            </el-form-item>

            <el-form-item label="证件类型" prop="idCardType">
              <el-select v-model="basicForm.idCardType" placeholder="居民身份证" :disabled="isReadonly" style="width: 100%;">
                <el-option label="居民身份证" value="居民身份证" />
                <el-option label="居民户口簿" value="居民户口簿" />
                <el-option label="护照" value="护照" />
                <el-option label="出生证" value="出生证" />
                <el-option label="新生儿" value="新生儿" />
                <el-option label="军官证" value="军官证" />
                <el-option label="驾驶证" value="驾驶证" />
                <el-option label="港澳居民来往内地通行证" value="港澳居民来往内地通行证" />
                <el-option label="台湾居民来往内地通行证" value="台湾居民来往内地通行证" />
              </el-select>
            </el-form-item>

            <el-form-item label="证件号" prop="idCard">
              <el-input v-model="basicForm.idCard" placeholder="请输入证件号" :disabled="isReadonly" clearable />
            </el-form-item>

            <el-form-item label="性别" prop="gender">
              <el-select v-model="basicForm.gender" placeholder="请选择性别" :disabled="isReadonly" style="width: 100%;">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>

            <el-form-item label="身高（cm）" prop="height">
              <el-input
                v-model.number="basicForm.height"
                placeholder="请输入身高"
                type="number"
                :disabled="isReadonly"
                clearable
              />
            </el-form-item>

            <el-form-item label="出生年月" prop="birthDate">
              <el-date-picker
                v-model="basicForm.birthDate"
                type="date"
                placeholder="请选择出生年月"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                prefix-icon="el-icon-date"
                :disabled="isReadonly"
                style="width: 100%;"
              />
            </el-form-item>

            <el-form-item label="民族" prop="ethnicity">
              <el-select v-model="basicForm.ethnicity" placeholder="请选择民族" :disabled="isReadonly" filterable style="width: 100%;">
                <el-option label="汉族" value="汉族" />
                <el-option label="蒙古族" value="蒙古族" />
                <el-option label="回族" value="回族" />
                <el-option label="藏族" value="藏族" />
                <el-option label="维吾尔族" value="维吾尔族" />
                <el-option label="苗族" value="苗族" />
                <el-option label="彝族" value="彝族" />
                <el-option label="壮族" value="壮族" />
                <el-option label="布依族" value="布依族" />
                <el-option label="朝鲜族" value="朝鲜族" />
                <el-option label="满族" value="满族" />
                <el-option label="侗族" value="侗族" />
                <el-option label="瑶族" value="瑶族" />
                <el-option label="白族" value="白族" />
                <el-option label="土家族" value="土家族" />
                <el-option label="哈尼族" value="哈尼族" />
                <el-option label="哈萨克族" value="哈萨克族" />
                <el-option label="傣族" value="傣族" />
                <el-option label="黎族" value="黎族" />
                <el-option label="傈僳族" value="傈僳族" />
                <el-option label="佤族" value="佤族" />
                <el-option label="畲族" value="畲族" />
                <el-option label="高山族" value="高山族" />
                <el-option label="拉祜族" value="拉祜族" />
                <el-option label="水族" value="水族" />
                <el-option label="东乡族" value="东乡族" />
                <el-option label="纳西族" value="纳西族" />
                <el-option label="景颇族" value="景颇族" />
                <el-option label="柯尔克孜族" value="柯尔克孜族" />
                <el-option label="土族" value="土族" />
                <el-option label="达斡尔族" value="达斡尔族" />
                <el-option label="仫佬族" value="仫佬族" />
                <el-option label="羌族" value="羌族" />
                <el-option label="布朗族" value="布朗族" />
                <el-option label="撒拉族" value="撒拉族" />
                <el-option label="毛南族" value="毛南族" />
                <el-option label="仡佬族" value="仡佬族" />
                <el-option label="锡伯族" value="锡伯族" />
                <el-option label="阿昌族" value="阿昌族" />
                <el-option label="普米族" value="普米族" />
                <el-option label="塔吉克族" value="塔吉克族" />
                <el-option label="怒族" value="怒族" />
                <el-option label="乌孜别克族" value="乌孜别克族" />
                <el-option label="俄罗斯族" value="俄罗斯族" />
                <el-option label="鄂温克族" value="鄂温克族" />
                <el-option label="德昂族" value="德昂族" />
                <el-option label="保安族" value="保安族" />
                <el-option label="裕固族" value="裕固族" />
                <el-option label="京族" value="京族" />
                <el-option label="塔塔尔族" value="塔塔尔族" />
                <el-option label="独龙族" value="独龙族" />
                <el-option label="鄂伦春族" value="鄂伦春族" />
                <el-option label="赫哲族" value="赫哲族" />
                <el-option label="门巴族" value="门巴族" />
                <el-option label="珞巴族" value="珞巴族" />
                <el-option label="基诺族" value="基诺族" />
              </el-select>
            </el-form-item>

            <el-form-item label="职业" prop="occupation">
              <el-select v-model="basicForm.occupation" placeholder="请选择职业" :disabled="isReadonly" filterable style="width: 100%;">
                <el-option label="国家机关、党群组织、企业、事业单位负责人" value="国家机关、党群组织、企业、事业单位负责人" />
                <el-option label="专业技术人员" value="专业技术人员" />
                <el-option label="办事人员和有关人员" value="办事人员和有关人员" />
                <el-option label="商业、服务业人员" value="商业、服务业人员" />
                <el-option label="农、林、牧、渔、水利业生产人员" value="农、林、牧、渔、水利业生产人员" />
                <el-option label="生产、运输设备操作人员及有关人员" value="生产、运输设备操作人员及有关人员" />
                <el-option label="军人" value="军人" />
                <el-option label="不便分类的其他从业人员" value="不便分类的其他从业人员" />
                <el-option label="学生" value="学生" />
                <el-option label="退休人员" value="退休人员" />
                <el-option label="无业人员" value="无业人员" />
              </el-select>
            </el-form-item>

            <el-form-item label="联系地址" prop="contactAddress">
              <el-cascader
                v-model="basicForm.contactAddress"
                :options="regionOptions"
                placeholder="请选择省市区"
                :disabled="isReadonly"
                style="width: 100%;"
                filterable
              />
            </el-form-item>

            <el-form-item label="详细地址" prop="detailAddress">
              <el-input
                v-model="basicForm.detailAddress"
                placeholder="请输入详细地址（门牌号、小区名称等）"
                :disabled="isReadonly"
                clearable
              />
            </el-form-item>

            <el-form-item label="备注说明">
              <el-input
                v-model="basicForm.remarks"
                type="textarea"
                :rows="4"
                placeholder="请输入客户备注信息"
                :disabled="isReadonly"
              />
            </el-form-item>

            <!-- 操作按钮 -->
            <el-form-item>
              <div style="text-align: center; margin-top: 30px;">
                <el-button class="custom-primary-btn" @click="handleBackToList">返回列表</el-button>
                <el-button v-if="!isReadonly" type="primary" class="custom-primary-btn" @click="handleNextStep">下一步</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 入组信息标签页 -->
        <el-tab-pane label="入组信息" name="enrollment" :disabled="!basicFormCompleted">
          <el-form
            ref="enrollmentForm"
            :model="enrollmentForm"
            :rules="enrollmentRules"
            label-width="140px"
            style="max-width: 700px; padding: 20px 0;"
          >
            <el-form-item label="入组编号（档案号）">
              <el-input
                v-model="displayMedicalRecordNo"
                placeholder="档案号"
                disabled
              />
            </el-form-item>

            <el-form-item label="病种类型" prop="diseaseType">
              <el-select
                v-model="enrollmentForm.diseaseType"
                filterable
                placeholder="请选择病种类型"
                :disabled="isReadonly"
                style="width: 100%;"
              >
                <el-option label="骨质疏松症" value="骨质疏松症" />
                <el-option label="妊娠合并高血压" value="妊娠合并高血压" />
                <el-option label="妊娠合并糖尿病" value="妊娠合并糖尿病" />
                <el-option label="肥胖症" value="肥胖症" />
                <el-option label="冠心病" value="冠心病" />
                <el-option label="高血压" value="高血压" />
                <el-option label="多囊卵巢综合征（PCOS）女性生育力保护" value="多囊卵巢综合征（PCOS）女性生育力保护" />
                <el-option label="血脂异常" value="血脂异常" />
                <el-option label="骨关节炎" value="骨关节炎" />
                <el-option label="肌少症" value="肌少症" />
                <el-option label="慢阻肺" value="慢阻肺" />
                <el-option label="青少年特发性脊柱侧凸" value="青少年特发性脊柱侧凸" />
                <el-option label="糖尿病" value="糖尿病" />
              </el-select>
            </el-form-item>

            <!-- 新建模式下显示的额外字段 -->
            <template v-if="!isEditMode">
              <el-form-item label="诊断" prop="diagnosis">
                <el-select
                  v-model="enrollmentForm.diagnosis"
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入关键字搜索"
                  :remote-method="searchDiagnosis"
                  :loading="diagnosisLoading"
                  style="width: 100%;"
                >
                  <el-option
                    v-for="item in diagnosisList"
                    :key="item"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="入组机构" prop="enrollmentInstitution">
                <el-input
                  v-model="enrollmentForm.enrollmentInstitution"
                  placeholder="请输入入组机构"
                  clearable
                />
              </el-form-item>
            </template>

            <!-- 档案填写入口 -->
            <el-divider content-position="left">档案信息</el-divider>
            <div class="file-entry-container">
              <div class="file-entry-item">
                <span class="file-entry-label">健康档案</span>
                <el-button
                  type="primary"
                  size="small"
                  class="custom-primary-btn"
                  @click="handleFillHealthRecord"
                >
                  {{ isReadonly ? '查看' : '填写' }}
                </el-button>
              </div>
              <div class="file-entry-item">
                <span class="file-entry-label">专病档案</span>
                <el-button
                  type="primary"
                  size="small"
                  style="background-color: #9b59d6; border-color: #9b59d6;"
                  @click="handleFillDiseaseRecord"
                >
                  {{ isReadonly ? '查看' : '填写' }}
                </el-button>
              </div>
              <div class="file-entry-item">
                <span class="file-entry-label">入组评估</span>
                <el-button
                  type="primary"
                  size="small"
                  style="background-color: #9b59d6; border-color: #9b59d6;"
                  @click="handleFillEnrollmentAssessment"
                >
                  {{ isReadonly ? '查看' : '填写' }}
                </el-button>
              </div>
            </div>

            <!-- 操作按钮 -->
            <el-form-item>
              <div style="text-align: center; margin-top: 30px;">
                <el-button v-if="!isReadonly" @click="handlePrevStep">上一步</el-button>
                <el-button v-if="!isReadonly" type="primary" class="custom-primary-btn" :loading="submitLoading" @click="handleSubmit">保存</el-button>
                <el-button class="custom-primary-btn" @click="handleBackToList">返回列表</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { addPatient, getPatientDetail, savePatientDetail, updatePatient } from '@/api/patient'
import { chinaRegionData } from '@/utils/china-region-data'

export default {
  name: 'PatientCreate',
  data() {
    // 手机号验证规则
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入联系电话'))
      } else if (!/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }

    return {
      activeTab: 'basic',
      basicFormCompleted: false,
      submitLoading: false,
      diagnosisLoading: false,
      regionOptions: chinaRegionData, // 省市区数据
      patientId: null, // 患者ID，用于编辑模式
      isEditMode: false, // 是否为编辑模式
      isReadonly: false, // 是否为只读模式
      displayMedicalRecordNo: '', // 显示的档案号

      // 基本信息表单
      basicForm: {
        name: '',
        nickname: '',
        phone: '',
        idCardType: '',
        idCard: '',
        gender: '',
        height: null,
        birthDate: '',
        ethnicity: '',
        occupation: '',
        contactAddress: [],
        detailAddress: '',
        remarks: ''
      },

      // 基本信息验证规则
      basicRules: {
        name: [
          { required: true, message: '请输入客户姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, validator: validatePhone, trigger: 'blur' }
        ],
        idCardType: [
          { required: true, message: '请选择证件类型', trigger: 'change' }
        ],
        idCard: [
          { required: true, message: '请输入证件号', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        height: [
          { required: true, message: '请输入身高', trigger: 'blur' },
          { type: 'number', message: '身高必须为数字', trigger: 'blur' }
        ],
        birthDate: [
          { required: true, message: '请选择出生年月', trigger: 'change' }
        ],
        ethnicity: [
          { required: true, message: '请选择民族', trigger: 'change' }
        ],
        occupation: [
          { required: true, message: '请选择职业', trigger: 'change' }
        ],
        contactAddress: [
          { required: true, message: '请选择联系地址', trigger: 'change' }
        ],
        detailAddress: [
          { required: true, message: '请输入详细地址', trigger: 'blur' }
        ]
      },

      // 入组信息表单
      enrollmentForm: {
        diseaseType: '',
        diagnosis: '',
        enrollmentInstitution: ''
      },

      // 入组信息验证规则
      enrollmentRules: {
        diseaseType: [
          { required: true, message: '请选择病种类型', trigger: 'change' }
        ],
        diagnosis: [
          { required: true, message: '请输入诊断', trigger: 'change' }
        ],
        enrollmentInstitution: [
          { required: true, message: '请输入入组机构', trigger: 'blur' }
        ]
      },

      // 诊断列表（模拟数据，实际应该从后端获取）
      diagnosisList: [
        '高血压病',
        '糖尿病',
        '冠心病',
        '骨质疏松症',
        '肥胖症',
        '慢性阻塞性肺疾病',
        '骨关节炎',
        '肌少症',
        '妊娠合并高血压',
        '妊娠合并糖尿病',
        '多囊卵巢综合征',
        '血脂异常',
        '青少年特发性脊柱侧凸'
      ],
      filteredDiagnosisList: []
    }
  },
  mounted() {
    // 检查是否有患者ID参数（编辑模式或查看模式）
    const patientId = this.$route.query.patientId
    const mode = this.$route.query.mode

    if (patientId) {
      this.patientId = parseInt(patientId)

      // 判断是否为只读模式
      if (mode === 'view') {
        this.isReadonly = true
        this.isEditMode = false
        this.basicFormCompleted = true // 允许切换到入组信息标签页
      } else {
        this.isEditMode = true
      }

      this.loadPatientData()
    }
  },
  methods: {
    /**
     * 解析省市区字符串为数组
     * 例如："河北省秦皇岛市北戴河区" -> ["河北省", "秦皇岛市", "北戴河区"]
     */
    parseRegionString(regionStr) {
      if (!regionStr) return []

      const result = []
      let remaining = regionStr.trim()

      // 匹配省级单位（省/自治区/直辖市）
      // 处理直辖市特殊情况：北京市、上海市、天津市、重庆市
      const directMunicipalities = ['北京市', '上海市', '天津市', '重庆市']
      const isDirectMunicipality = directMunicipalities.some(city => remaining.startsWith(city))

      if (isDirectMunicipality) {
        // 直辖市：第一级和第二级都是市
        const cityName = remaining.substring(0, 3) // "北京市"
        result.push(cityName)
        result.push(cityName) // 直辖市的省级和市级都用同一个名称
        remaining = remaining.substring(3)
      } else {
        // 普通省份
        const provinceMatch = remaining.match(/^(.+?(?:省|自治区))/)
        if (provinceMatch) {
          result.push(provinceMatch[1])
          remaining = remaining.substring(provinceMatch[1].length)

          // 匹配市级单位
          const cityMatch = remaining.match(/^(.+?(?:市|地区|州|盟))/)
          if (cityMatch) {
            result.push(cityMatch[1])
            remaining = remaining.substring(cityMatch[1].length)
          }
        }
      }

      // 剩余部分作为区/县级单位
      if (remaining && remaining.trim()) {
        result.push(remaining.trim())
      }

      return result.length >= 2 ? result : []
    },

    /** 加载患者数据 */
    async loadPatientData() {
      try {
        const response = await getPatientDetail(this.patientId)
        const basicInfo = response.data.basicInfo
        const detailInfo = response.data.detailInfo

        // 填充基本信息
        if (basicInfo) {
          this.basicForm.name = basicInfo.name || ''
          this.basicForm.phone = basicInfo.phone || ''
          this.basicForm.idCard = basicInfo.idCard || ''
          this.basicForm.gender = basicInfo.gender || ''
          this.basicForm.birthDate = basicInfo.birthDate || ''

          // 填充入组信息（从patient表）
          this.enrollmentForm.diseaseType = basicInfo.diseaseType || ''
          this.enrollmentForm.enrollmentInstitution = basicInfo.enrollmentInstitution || ''
          this.displayMedicalRecordNo = basicInfo.medicalRecordNo || ''
        }

        // 填充详细信息
        if (detailInfo) {
          this.basicForm.nickname = detailInfo.nickname || ''
          this.basicForm.idCardType = detailInfo.idCardType || '居民身份证'
          this.basicForm.height = detailInfo.heightCm || null
          this.basicForm.ethnicity = detailInfo.ethnicity || ''
          this.basicForm.occupation = detailInfo.occupation || ''

          // 处理联系地址：将字符串解析为数组格式以适配级联选择器
          if (detailInfo.contactProvinceCityDistrict) {
            const parsedRegion = this.parseRegionString(detailInfo.contactProvinceCityDistrict)
            if (parsedRegion.length > 0) {
              this.basicForm.contactAddress = parsedRegion
            } else {
              // 解析失败时，保留原字符串供用户重新选择
              this.basicForm.contactAddress = []
              console.warn('无法解析联系地址:', detailInfo.contactProvinceCityDistrict)
            }
          }

          // 填充详细地址
          this.basicForm.detailAddress = detailInfo.detailAddress || ''

          // 填充备注说明
          this.basicForm.remarks = detailInfo.remark || ''
        }

        // 编辑模式下，允许切换到入组信息页
        if (this.isEditMode) {
          this.basicFormCompleted = true
        }

        this.$message.success('患者信息加载成功')
      } catch (error) {
        console.error('加载患者信息失败:', error)
        this.$message.error('加载患者信息失败')
      }
    },

    /** 标签页切换 */
    handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)
    },

    /** 返回列表 */
    handleBackToList() {
      this.$confirm('确定要返回列表吗？未保存的数据将丢失', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 返回到患者选择弹窗的来源页面
        this.$router.go(-1)
      }).catch(() => {
        // 取消操作
      })
    },

    /** 下一步 */
    handleNextStep() {
      this.$refs.basicForm.validate((valid) => {
        if (valid) {
          this.basicFormCompleted = true
          this.activeTab = 'enrollment'
          this.$message.success('基本信息验证通过，请继续填写入组信息')
        } else {
          this.$message.error('请完善必填项')
          return false
        }
      })
    },

    /** 上一步 */
    handlePrevStep() {
      this.activeTab = 'basic'
    },

    /** 搜索诊断 */
    searchDiagnosis(query) {
      if (query !== '') {
        this.diagnosisLoading = true
        setTimeout(() => {
          this.diagnosisList = this.diagnosisList.filter(item => {
            return item.toLowerCase().includes(query.toLowerCase())
          })
          this.diagnosisLoading = false
        }, 200)
      } else {
        // 恢复完整列表
        this.diagnosisList = [
          '高血压病',
          '糖尿病',
          '冠心病',
          '骨质疏松症',
          '肥胖症',
          '慢性阻塞性肺疾病',
          '骨关节炎',
          '肌少症',
          '妊娠合并高血压',
          '妊娠合并糖尿病',
          '多囊卵巢综合征',
          '血脂异常',
          '青少年特发性脊柱侧凸'
        ]
      }
    },

    /** 提交表单 */
    handleSubmit() {
      // 编辑模式：只验证病种类型
      if (this.isEditMode) {
        if (!this.enrollmentForm.diseaseType) {
          this.$message.error('请选择病种类型')
          return
        }
        this.submitLoading = true
        this.updateExistingPatient()
      } else {
        // 新建模式：验证所有字段
        this.$refs.enrollmentForm.validate((valid) => {
          if (valid) {
            this.submitLoading = true
            this.createNewPatient()
          } else {
            this.$message.error('请完善必填项')
            return false
          }
        })
      }
    },

    /** 创建新患者 */
    createNewPatient() {
      // 合并表单数据
      const patientData = {
        ...this.basicForm,
        ...this.enrollmentForm
      }

      // 处理联系地址：将数组转换为字符串
      if (Array.isArray(patientData.contactAddress)) {
        patientData.contactAddress = patientData.contactAddress.join('')
      }

      // 确保detailAddress字段被包含
      patientData.detailAddress = this.basicForm.detailAddress || ''

      // 确保remarks字段被包含
      patientData.remarks = this.basicForm.remarks || ''

      console.log('提交的患者数据:', patientData)

      // 调用后端API保存患者信息
      addPatient(patientData)
        .then(response => {
          this.$message.success('患者信息保存成功')
          this.submitLoading = false

          // 保存成功后，获取返回的患者信息（包含自动生成的ID和档案号）
          const savedPatientInfo = response.data || {}

          // 合并所有患者数据
          const fullPatientData = {
            ...patientData,
            id: savedPatientInfo.id,
            medicalRecordNo: savedPatientInfo.medicalRecordNo || patientData.medicalRecordNo
          }

          // 判断来源，决定跳转目标
          const from = this.$route.query.from
          if (from === 'scheme') {
            // 从方案管理来的，跳转到方案配置页面
            this.$router.push({
              path: '/scheme/config-patient',
              query: {
                patientData: JSON.stringify(fullPatientData)
              }
            })
          } else {
            // 默认情况（快速建档），返回档案列表
            this.$router.push({ path: '/archive/list' })
          }
        })
        .catch(error => {
          console.error('保存患者信息失败:', error)
          this.$message.error('保存失败，请稍后重试')
          this.submitLoading = false
        })
    },

    /** 更新已有患者 */
    async updateExistingPatient() {
      try {
        // 1. 更新patient_detail表的数据
        const detailData = {
          patientId: this.patientId,
          nickname: this.basicForm.nickname,
          idCardType: this.basicForm.idCardType,
          heightCm: this.basicForm.height,
          ethnicity: this.basicForm.ethnicity,
          occupation: this.basicForm.occupation,
          contactProvinceCityDistrict: Array.isArray(this.basicForm.contactAddress)
            ? this.basicForm.contactAddress.join('')
            : this.basicForm.contactAddress,
          detailAddress: this.basicForm.detailAddress || '',
          remark: this.basicForm.remarks || ''
        }

        await savePatientDetail(detailData)

        // 2. 更新patient表的入组信息
        const basicData = {
          id: this.patientId,
          diseaseType: this.enrollmentForm.diseaseType,
          enrollmentInstitution: this.enrollmentForm.enrollmentInstitution
        }

        await updatePatient(basicData)

        this.$message.success('患者信息更新成功')
        this.submitLoading = false

        // 3. 准备跳转数据（需要获取完整的患者信息）
        const response = await getPatientDetail(this.patientId)
        const patientInfo = response.data.basicInfo

        const fullPatientData = {
          id: this.patientId,
          name: patientInfo.name,
          gender: patientInfo.gender,
          birthDate: patientInfo.birthDate,
          phone: patientInfo.phone,
          idCard: patientInfo.idCard,
          medicalRecordNo: patientInfo.medicalRecordNo,
          diseaseType: this.enrollmentForm.diseaseType,
          enrollmentInstitution: this.enrollmentForm.enrollmentInstitution,
          ...this.basicForm,
          ...this.enrollmentForm
        }

        // 4. 根据来源决定跳转目标
        const from = this.$route.query.from
        if (from === 'archive') {
          // 从档案列表来的，返回档案列表
          this.$router.push({ path: '/archive/list' })
        } else if (from === 'scheme') {
          // 从方案管理来的，跳转到方案配置页面
          this.$router.push({
            path: '/scheme/config-patient',
            query: {
              patientData: JSON.stringify(fullPatientData)
            }
          })
        } else {
          // 默认返回档案列表
          this.$router.push({ path: '/archive/list' })
        }
      } catch (error) {
        console.error('更新患者信息失败:', error)
        this.$message.error('更新失败，请稍后重试')
        this.submitLoading = false
      }
    },

    /** 填写健康档案 */
    handleFillHealthRecord() {
      // 合并基本信息和入组信息
      const patientData = {
        ...this.basicForm,
        ...this.enrollmentForm
      }
      // 跳转到健康档案填写页面
      const query = {
        from: 'create',
        patientData: JSON.stringify(patientData)
      }
      // 如果是只读模式，添加 mode 参数
      if (this.isReadonly) {
        query.mode = 'view'
        query.patientId = this.patientId
      }
      this.$router.push({
        path: '/patient/health-record',
        query
      })
    },

    /** 填写专病档案 */
    handleFillDiseaseRecord() {
      // 合并基本信息和入组信息
      const patientData = {
        ...this.basicForm,
        ...this.enrollmentForm
      }
      // 跳转到专病档案填写页面
      const query = {
        from: 'create',
        patientData: JSON.stringify(patientData)
      }
      // 如果是只读模式，添加 mode 参数
      if (this.isReadonly) {
        query.mode = 'view'
        query.patientId = this.patientId
      }
      this.$router.push({
        path: '/patient/disease-record',
        query
      })
    },

    /** 填写入组评估 */
    handleFillEnrollmentAssessment() {
      // 合并基本信息和入组信息
      const patientData = {
        ...this.basicForm,
        ...this.enrollmentForm
      }
      // 跳转到入组评估填写页面
      const query = {
        from: 'create',
        patientData: JSON.stringify(patientData)
      }
      // 如果是只读模式，添加 mode 参数
      if (this.isReadonly) {
        query.mode = 'view'
        query.patientId = this.patientId
      }
      this.$router.push({
        path: '/patient/enrollment-assessment',
        query
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;

  ::v-deep .el-tabs__item {
    font-size: 16px;
    font-weight: 500;
  }

  ::v-deep .el-tabs__item.is-active {
    color: #409eff;
  }

  ::v-deep .el-tabs__item.is-disabled {
    color: #c0c4cc;
    cursor: not-allowed;
  }

  ::v-deep .el-form-item__label {
    font-weight: 500;
  }

  ::v-deep .el-card {
    min-height: 600px;
  }

  // 自定义主要按钮颜色
  .custom-primary-btn {
    background-color: rgb(106, 91, 140);
    border-color: rgb(106, 91, 140);
    color: #fff;

    &:hover,
    &:focus {
      background-color: rgb(96, 81, 130);
      border-color: rgb(96, 81, 130);
      color: #fff;
    }

    &:active {
      background-color: rgb(86, 71, 120);
      border-color: rgb(86, 71, 120);
    }
  }

  .el-button--primary {
    background-color: rgb(106, 91, 140) !important;
    border-color: rgb(106, 91, 140) !important;
    color: #fff !important;

    &:hover,
    &:focus {
      background-color: rgb(96, 81, 130) !important;
      border-color: rgb(96, 81, 130) !important;
      color: #fff !important;
    }

    &:active {
      background-color: rgb(86, 71, 120) !important;
      border-color: rgb(86, 71, 120) !important;
      color: #fff !important;
    }
  }

  ::v-deep .el-button--primary {
    background-color: rgb(106, 91, 140) !important;
    border-color: rgb(106, 91, 140) !important;
    color: #fff !important;

    &:hover,
    &:focus {
      background-color: rgb(96, 81, 130) !important;
      border-color: rgb(96, 81, 130) !important;
      color: #fff !important;
    }

    &:active {
      background-color: rgb(86, 71, 120) !important;
      border-color: rgb(86, 71, 120) !important;
      color: #fff !important;
    }
  }
}

.file-entry-container {
  margin: 20px 0;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;

  .file-entry-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #e4e7ed;

    &:last-child {
      border-bottom: none;
    }

    .file-entry-label {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }
  }
}
</style>
