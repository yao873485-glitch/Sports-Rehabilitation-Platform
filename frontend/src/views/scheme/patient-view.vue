<template>
  <div class="app-container">
    <!-- 患者信息卡片 - 固定顶部 -->
    <el-card class="patient-info-card">
      <el-table
        :data="[patientInfo]"
        :show-header="true"
        style="width: 100%"
      >
        <el-table-column prop="name" label="姓名" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }} · {{ scope.row.gender }} · {{ calculateAge(scope.row.birthDate) }}岁
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证" align="center" />
        <el-table-column prop="medicalRecordNo" label="档案号" align="center" />
        <el-table-column prop="birthDate" label="出生日期" align="center" />
        <el-table-column prop="diagnosis" label="诊断" align="center">
          <template slot-scope="scope">
            {{ scope.row.diagnosis || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="diseaseType" label="病种" align="center" />
      </el-table>
    </el-card>

    <!-- 主内容区 -->
    <el-card style="margin-top: 20px;">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 基本信息标签页 -->
        <el-tab-pane label="基本信息" name="basic">
          <div class="basic-info-content">
            <!-- 方案周期 -->
            <div class="info-row">
              <span class="info-label">方案周期:</span>
              <el-select
                v-model="schemeCycle"
                placeholder="请选择方案周期"
                style="width: 200px;"
                disabled
              >
                <el-option
                  v-for="month in 12"
                  :key="month"
                  :label="`${month}`"
                  :value="month"
                />
              </el-select>
              <span style="margin-left: 10px; color: #606266;">月</span>
            </div>

            <!-- 健康档案 -->
            <div class="info-row">
              <span class="info-label">健康档案:</span>
              <el-button
                size="small"
                class="view-btn"
                @click="handleViewHealthRecord"
              >
                查看
              </el-button>
            </div>

            <!-- 专病档案 -->
            <div class="info-row">
              <span class="info-label">专病档案:</span>
              <el-button
                size="small"
                class="view-btn"
                @click="handleViewDiseaseRecord"
              >
                查看
              </el-button>
            </div>

            <!-- 入组评估 -->
            <div class="info-row">
              <span class="info-label">入组评估:</span>
              <el-button
                size="small"
                class="view-btn"
                @click="handleViewEnrollmentAssessment"
              >
                查看
              </el-button>
            </div>
          </div>
        </el-tab-pane>

        <!-- 运动处方标签页 -->
        <el-tab-pane label="运动处方" name="prescription">
          <div style="padding: 20px;">
            <!-- 数据表格 -->
            <el-table
              v-loading="prescriptionLoading"
              :data="prescriptionList"
              border
              highlight-current-row
              style="width: 100%"
              :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
            >
              <el-table-column prop="prescriptionName" label="运动处方名称" width="220" align="center" header-align="center" />

              <el-table-column
                prop="patientInfo"
                label="患者信息"
                width="180"
                align="center"
                header-align="center"
              >
                <template slot-scope="scope">
                  <span>
                    {{ scope.row.patientName }}
                    <span :class="scope.row.gender === '男' ? 'male-icon' : 'female-icon'">
                      {{ scope.row.gender === '男' ? '♂' : '♀' }}
                    </span>
                    {{ calculateAge(scope.row.birthDate) }}岁
                  </span>
                </template>
              </el-table-column>

              <el-table-column prop="diseaseType" label="病种" width="150" align="center" header-align="center" />

              <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" header-align="center" />

              <el-table-column prop="executionInstitution" label="执行机构" width="200" align="center" header-align="center" />

              <el-table-column prop="prescriptionStatus" label="处方状态" width="140" align="center" header-align="center">
                <template slot-scope="scope">
                  <el-tag
                    :type="getStatusType(scope.row.prescriptionStatus)"
                    size="mini"
                  >
                    {{ getStatusText(scope.row.prescriptionStatus) }}
                  </el-tag>
                </template>
              </el-table-column>

              <el-table-column prop="prescribingDoctor" label="开方医生" width="140" align="center" header-align="center" />

              <el-table-column prop="createdTime" label="创建时间" width="180" align="center" header-align="center" />

              <el-table-column
                label="操作"
                width="140"
                fixed="right"
                align="center"
                header-align="center"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleViewPrescription(scope.row)"
                  >
                    查看
                  </el-button>
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleEditPrescription(scope.row)"
                  >
                    编辑
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-wrapper">
              <el-pagination
                @size-change="handlePrescriptionSizeChange"
                @current-change="handlePrescriptionPageChange"
                :current-page="prescriptionQuery.pageNum"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="prescriptionQuery.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="prescriptionTotal"
                small
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 评定计划标签页 -->
        <el-tab-pane label="评定计划" name="assessment">
          <div style="padding: 20px;">
            <!-- 数据表格 -->
            <el-table
              v-loading="assessmentLoading"
              :data="assessmentList"
              border
              highlight-current-row
              style="width: 100%"
              :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
            >
              <el-table-column prop="assessmentName" label="评定方案名称" width="220" align="center" header-align="center" />

              <el-table-column
                prop="customerInfo"
                label="客户信息"
                width="180"
                align="center"
                header-align="center"
              >
                <template slot-scope="scope">
                  <span>
                    {{ scope.row.patientName }}
                    <span :class="scope.row.gender === '男' ? 'male-icon' : 'female-icon'">
                      {{ scope.row.gender === '男' ? '♂' : '♀' }}
                    </span>
                    {{ calculateAge(scope.row.birthDate) }}岁
                  </span>
                </template>
              </el-table-column>

              <el-table-column prop="diseaseType" label="病种" width="150" align="center" header-align="center" />

              <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" header-align="center" />

              <el-table-column prop="executionInstitution" label="执行机构" width="200" align="center" header-align="center" />

              <el-table-column prop="status" label="状态" width="140" align="center" header-align="center">
                <template slot-scope="scope">
                  <el-tag
                    :type="getAssessmentStatusType(scope.row.status)"
                    size="mini"
                  >
                    {{ getAssessmentStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>

              <el-table-column prop="prescribingDoctor" label="开方医生" width="140" align="center" header-align="center" />

              <el-table-column prop="createdTime" label="创建时间" width="180" align="center" header-align="center" />

              <el-table-column
                label="操作"
                width="160"
                fixed="right"
                align="center"
                header-align="center"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleViewAssessment(scope.row)"
                  >
                    查看
                  </el-button>
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleExecuteAssessment(scope.row)"
                  >
                    执行
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-wrapper">
              <el-pagination
                @size-change="handleAssessmentSizeChange"
                @current-change="handleAssessmentPageChange"
                :current-page="assessmentQuery.pageNum"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="assessmentQuery.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="assessmentTotal"
                small
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 随访管理标签页 -->
        <el-tab-pane label="随访管理" name="followup">
          <div style="padding: 20px;">
            <!-- 随访信息展示区 -->
            <div v-if="followupList.length > 0">
              <!-- 顶部区域：计划名称 -->
              <div class="followup-header">
                <div class="plan-name">
                  <span class="label">计划名称：</span>
                  <span class="value">{{ getFollowupDisplayData().planName }}</span>
                </div>
              </div>

              <!-- 患者信息展示区 -->
              <div class="patient-info-section">
                <!-- 第一行 -->
                <div class="info-row">
                  <div class="info-item">
                    <span class="info-label">患者姓名：</span>
                    <span class="info-value">{{ getFollowupDisplayData().patientName }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">患者年龄：</span>
                    <span class="info-value">{{ getFollowupDisplayData().age }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">患者性别：</span>
                    <span class="info-value">{{ getFollowupDisplayData().gender }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">患者身份证号：</span>
                    <span class="info-value">{{ getFollowupDisplayData().idCard }}</span>
                  </div>
                </div>

                <!-- 第二行 -->
                <div class="info-row">
                  <div class="info-item">
                    <span class="info-label">申请时间：</span>
                    <span class="info-value">{{ getFollowupDisplayData().applicationTime }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">加入项目时间：</span>
                    <span class="info-value">{{ getFollowupDisplayData().enrollmentTime }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">来源：</span>
                    <span class="info-value">{{ getFollowupDisplayData().source }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">病种：</span>
                    <span class="info-value">{{ patientInfo.diseaseType || '-' }}</span>
                  </div>
                </div>

                <!-- 第三行 -->
                <div class="info-row">
                  <div class="info-item">
                    <span class="info-label">手机号：</span>
                    <span class="info-value">{{ getFollowupDisplayData().phone }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">主负责医生：</span>
                    <span class="info-value">{{ getFollowupDisplayData().primaryDoctor }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">健管师：</span>
                    <span class="info-value">{{ getFollowupDisplayData().healthManager }}</span>
                  </div>
                </div>
              </div>

              <!-- 分隔线 -->
              <el-divider />
            </div>

            <!-- 随访计划展示区 -->
            <div v-if="followupList.length > 0" class="followup-plan-content">
              <!-- 随访计划进度 -->
              <div class="followup-progress">
                <span class="progress-text">随访计划进度：已完成</span>
                <span class="progress-value">{{ followupPlanProgress }}%</span>
              </div>

              <!-- 时间轴式任务列表 -->
              <div class="followup-timeline">
                <div
                  v-for="(task, index) in followupTasks"
                  :key="index"
                  class="timeline-item"
                >
                  <!-- 左侧时间轴区域 -->
                  <div class="timeline-left">
                    <div class="timeline-date">{{ task.date }}</div>
                    <div class="timeline-status">{{ task.statusText }}</div>
                    <div class="timeline-dot"></div>
                    <div v-if="index < followupTasks.length - 1" class="timeline-line"></div>
                  </div>

                  <!-- 中间分隔线 -->
                  <div class="timeline-divider"></div>

                  <!-- 右侧任务详情卡片 -->
                  <div class="timeline-right">
                    <el-card class="task-card">
                      <div class="task-header">
                        <h4 class="task-title">{{ task.title }}</h4>
                        <div class="task-actions-inline">
                          <a href="javascript:;" class="action-link" @click="handleGoFill(task)">去填写</a>
                          <span class="divider">|</span>
                          <a href="javascript:;" class="action-link" @click="handleRemindPatient(task)">提醒患者</a>
                          <span class="divider">|</span>
                          <a href="javascript:;" class="action-link" @click="handleTemporaryChange(task)">发起临时变更</a>
                        </div>
                      </div>

                      <div class="task-footer">
                        <el-button
                          type="primary"
                          size="small"
                          @click="handleTemporaryEvent(task)"
                        >
                          发起临时事件
                        </el-button>
                      </div>
                    </el-card>
                  </div>
                </div>
              </div>
            </div>

            <!-- 分隔线 -->
            <el-divider v-if="followupList.length > 0" />

            <!-- 操作日志 -->
            <div class="operation-log">
              <h3 style="margin-bottom: 15px; font-size: 16px; color: #303133;">操作日志</h3>
              <el-table
                :data="operationLogs"
                border
                style="width: 100%"
              >
                <el-table-column prop="operator" label="操作人" width="150" />
                <el-table-column prop="operationTime" label="操作时间" width="180" />
                <el-table-column prop="channel" label="渠道" width="120" />
                <el-table-column prop="action" label="动作" />
              </el-table>
              <div v-if="operationLogs.length === 0" style="text-align: center; padding: 40px; color: #909399;">
                暂无数据
              </div>
            </div>

            <el-empty v-if="!followupLoading && followupList.length === 0" description="暂无随访管理信息" style="margin-top: 40px;" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 开始随访对话框 -->
    <el-dialog
      title="开始随访"
      :visible.sync="startFollowupVisible"
      width="600px"
      append-to-body
    >
      <el-form :model="followupForm" label-width="120px">
        <el-form-item label="选择计划模板" required>
          <el-select
            v-model="followupForm.planId"
            placeholder="请选择随访计划"
            style="width: 100%"
            @change="handlePlanChange"
          >
            <el-option
              v-for="plan in followupPlanList"
              :key="plan.id"
              :label="plan.planName"
              :value="plan.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="随访项目">
          <el-input v-model="followupForm.project" placeholder="请输入随访项目名称" />
        </el-form-item>

        <el-form-item label="随访团队" required>
          <el-input v-model="followupForm.team" placeholder="请输入随访团队" />
        </el-form-item>

        <el-form-item label="主负责医生">
          <el-input v-model="followupForm.primaryDoctor" placeholder="请输入主负责医生" />
        </el-form-item>

        <el-form-item label="健管师">
          <el-input v-model="followupForm.healthManager" placeholder="请输入健管师" />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="startFollowupVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="submitStartFollowup"
          style="background-color: rgb(144, 126, 179); border-color: rgb(144, 126, 179);"
        >
          确 定
        </el-button>
      </span>
    </el-dialog>

    <!-- 健康档案弹窗 -->
    <health-record-dialog
      :visible.sync="healthRecordDialogVisible"
      :patient-data="patientInfo"
      :patient-id="patientInfo.id"
      @saved="handleHealthRecordSaved"
    />

    <!-- 专病档案弹窗 -->
    <disease-record-dialog
      :visible.sync="diseaseRecordDialogVisible"
      :patient-id="patientInfo.id"
      @saved="handleDiseaseRecordSaved"
    />

    <!-- 入组评估弹窗 -->
    <enrollment-assessment-dialog
      :visible.sync="enrollmentAssessmentDialogVisible"
      :patient-id="patientInfo.id"
      @saved="handleEnrollmentAssessmentSaved"
    />
  </div>
</template>

<script>
import { getPatientDetail } from '@/api/patient'
import { getPatientExercisePrescriptionList } from '@/api/patient-exercise-prescription'
import { getPatientAssessmentPlanList } from '@/api/patient-assessment-plan'
import { getFollowupList, getFollowupPlanList, enrollPatient } from '@/api/followup'
import { getSchemeCycleByPatientId, getSchemeCycleByPatientIdAndSchemeId, getSchemeCycleBySchemeId } from '@/api/scheme-cycle'
import HealthRecordDialog from '@/views/patient/components/HealthRecordDialog.vue'
import DiseaseRecordDialog from '@/views/patient/components/DiseaseRecordDialog.vue'
import EnrollmentAssessmentDialog from '@/views/patient/components/EnrollmentAssessmentDialog.vue'

export default {
  name: 'PatientView',
  components: {
    HealthRecordDialog,
    DiseaseRecordDialog,
    EnrollmentAssessmentDialog
  },
  data() {
    return {
      activeTab: 'basic',
      healthRecordDialogVisible: false,
      diseaseRecordDialogVisible: false,
      enrollmentAssessmentDialogVisible: false,
      patientInfo: {
        id: '',
        name: '',
        gender: '',
        birthDate: '',
        idCard: '',
        medicalRecordNo: '',
        diagnosis: '',
        diseaseType: ''
      },
      // 基本信息 - 方案周期
      schemeCycle: null,
      schemeId: null,
      // 运动处方相关
      prescriptionLoading: false,
      prescriptionList: [],
      prescriptionTotal: 0,
      prescriptionQuery: {
        pageNum: 1,
        pageSize: 10
      },
      // 评定计划相关
      assessmentLoading: false,
      assessmentList: [],
      assessmentTotal: 0,
      assessmentQuery: {
        pageNum: 1,
        pageSize: 10
      },
      // 随访管理相关
      followupLoading: false,
      followupList: [],
      followupPlanProgress: 0, // 随访计划进度
      followupTasks: [], // 随访任务列表
      operationLogs: [], // 操作日志
      // 开始随访对话框
      startFollowupVisible: false,
      followupPlanList: [], // 随访计划列表
      followupForm: {
        planId: '',
        planName: '',
        project: '',
        team: '',
        primaryDoctor: '',
        healthManager: ''
      }
    }
  },
  mounted() {
    // 从路由参数获取激活的标签页
    const activeTab = this.$route.query.activeTab
    if (activeTab) {
      this.activeTab = activeTab
    }

    // 从路由参数获取患者信息
    const patientData = this.$route.query.patientData
    const patientId = this.$route.query.patientId
    const schemeId = this.$route.query.schemeId
    this.schemeId = schemeId || null

    if (patientData) {
      try {
        this.patientInfo = JSON.parse(patientData)
      } catch (error) {
        console.error('解析患者信息失败:', error)
        this.$message.error('获取患者信息失败')
        this.$router.back()
      }
    } else if (patientId) {
      // 如果只有 patientId，则从后端加载患者信息
      this.loadPatientInfo(patientId)
    } else {
      this.$message.error('缺少患者信息')
      this.$router.back()
    }

    // 如果激活的是随访管理标签页，加载随访数据
    if (this.activeTab === 'followup') {
      this.loadFollowupData()
    }

    // 加载方案周期数据
    this.loadSchemeCycle()
  },
  methods: {
    /** 加载方案周期 */
    async loadSchemeCycle() {
      const patientId = this.patientInfo.id || this.$route.query.patientId
      const schemeId = this.schemeId || this.$route.query.schemeId

      try {
        let response = null
        if (patientId && schemeId) {
          response = await getSchemeCycleByPatientIdAndSchemeId(patientId, schemeId)
        } else if (schemeId) {
          response = await getSchemeCycleBySchemeId(schemeId)
        } else if (patientId) {
          response = await getSchemeCycleByPatientId(patientId)
        }

        if (response && response.data) {
          this.schemeCycle = response.data.cycleMonths
        } else {
          this.schemeCycle = null
        }
      } catch (error) {
        console.error('加载方案周期失败:', error)
        this.schemeCycle = null
      }
    },

    /** 加载患者信息（通过 patientId） */
    async loadPatientInfo(patientId) {
      try {
        const response = await getPatientDetail(patientId)
        if (response.data) {
          const basicInfo = response.data.basicInfo
          const detailInfo = response.data.detailInfo

          this.patientInfo = {
            id: basicInfo.id,
            name: basicInfo.name,
            gender: basicInfo.gender,
            birthDate: basicInfo.birthDate,
            idCard: basicInfo.idCard,
            medicalRecordNo: basicInfo.medicalRecordNo,
            diagnosis: detailInfo ? (detailInfo.diagnosis || '-') : '-',
            diseaseType: basicInfo.diseaseType
          }
        }
      } catch (error) {
        console.error('加载患者信息失败:', error)
        this.$message.error('加载患者信息失败')
        this.$router.back()
      }
    },

    /** 加载随访数据 */
    loadFollowupData() {
      // 加载随访列表
      if (this.patientInfo.id) {
        this.getFollowupList()
      }
    },

    /** 计算年龄 */
    calculateAge(birthDate) {
      if (!birthDate) return 0
      const birth = new Date(birthDate)
      const today = new Date()
      let age = today.getFullYear() - birth.getFullYear()
      const monthDiff = today.getMonth() - birth.getMonth()
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--
      }
      return age
    },

    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'prescription' && this.prescriptionList.length === 0) {
        this.getPrescriptionList()
      } else if (tab.name === 'assessment' && this.assessmentList.length === 0) {
        this.getAssessmentList()
      } else if (tab.name === 'followup' && this.followupList.length === 0) {
        this.getFollowupList()
      }
    },

    /** 获取运动处方列表 */
    getPrescriptionList() {
      this.prescriptionLoading = true
      const params = {
        ...this.prescriptionQuery,
        // 使用档案号作为搜索关键词查询该患者的处方
        searchKeyword: this.patientInfo.medicalRecordNo
      }
      getPatientExercisePrescriptionList(params).then(response => {
        this.prescriptionList = response.data.records || []
        this.prescriptionTotal = response.data.total || 0
        this.prescriptionLoading = false
      }).catch(() => {
        this.prescriptionLoading = false
      })
    },

    /** 获取评定列表 */
    getAssessmentList() {
      this.assessmentLoading = true
      const params = {
        ...this.assessmentQuery,
        // 使用档案号作为搜索关键词查询该患者的评定
        searchKeyword: this.patientInfo.medicalRecordNo
      }
      getPatientAssessmentPlanList(params).then(response => {
        this.assessmentList = response.data.records || []
        this.assessmentTotal = response.data.total || 0
        this.assessmentLoading = false
      }).catch(() => {
        this.assessmentLoading = false
      })
    },

    /** 获取随访列表 */
    getFollowupList() {
      console.log('===== 开始获取随访列表 =====')
      console.log('患者ID:', this.patientInfo.id)
      this.followupLoading = true
      const params = {
        patientId: this.patientInfo.id
      }
      getFollowupList(params).then(response => {
        console.log('随访列表API响应:', response)
        this.followupList = response.data.records || []
        console.log('随访列表数据:', this.followupList)
        this.followupLoading = false
        // 初始化随访任务列表
        if (this.followupList.length > 0) {
          console.log('初始化随访任务列表')
          this.initFollowupTasks()
          this.loadOperationLogs()
        } else {
          console.warn('随访列表为空')
        }
      }).catch(error => {
        console.error('获取随访列表失败:', error)
        this.followupLoading = false
      })
    },

    /** 初始化随访任务列表 */
    initFollowupTasks() {
      // 根据实际的随访计划生成任务列表
      // 这里使用模拟数据，实际应该从后端获取
      const followupInfo = this.followupList[0] || {}

      this.followupTasks = [
        {
          date: this.formatDate(followupInfo.enrollmentTime),
          statusText: '已完成',
          title: `${followupInfo.followupPlanName || '随访计划'} - 第1期`,
          status: 'completed'
        },
        {
          date: this.getNextPeriodDate(followupInfo.enrollmentTime, 1),
          statusText: '进行中',
          title: `${followupInfo.followupPlanName || '随访计划'} - 第2期`,
          status: 'inProgress'
        },
        {
          date: this.getNextPeriodDate(followupInfo.enrollmentTime, 2),
          statusText: '未开始',
          title: `${followupInfo.followupPlanName || '随访计划'} - 第3期`,
          status: 'pending'
        }
      ]

      // 计算进度
      const completedTasks = this.followupTasks.filter(t => t.status === 'completed').length
      this.followupPlanProgress = Math.round((completedTasks / this.followupTasks.length) * 100)
    },

    /** 格式化日期 */
    formatDate(dateStr) {
      if (!dateStr) return new Date().toISOString().split('T')[0]
      return dateStr.split(' ')[0]
    },

    /** 获取下一期日期（每期间隔30天） */
    getNextPeriodDate(startDate, periodIndex) {
      if (!startDate) {
        const date = new Date()
        date.setDate(date.getDate() + periodIndex * 30)
        return date.toISOString().split('T')[0]
      }
      const date = new Date(startDate)
      date.setDate(date.getDate() + periodIndex * 30)
      return date.toISOString().split('T')[0]
    },

    /** 加载操作日志 */
    loadOperationLogs() {
      // 模拟操作日志数据
      // 实际应该从后端API获取
      this.operationLogs = []
    },

    /** 去填写 */
    handleGoFill(task) {
      this.$message.info('去填写: ' + task.title)
    },

    /** 提醒患者 */
    handleRemindPatient(task) {
      this.$message.info('提醒患者: ' + task.title)
    },

    /** 发起临时变更 */
    handleTemporaryChange(task) {
      this.$message.info('发起临时变更: ' + task.title)
    },

    /** 发起临时事件 */
    handleTemporaryEvent(task) {
      this.$message.info('发起临时事件: ' + task.title)
    },

    /** 获取随访展示数据 */
    getFollowupDisplayData() {
      console.log('获取随访展示数据，followupList长度:', this.followupList.length)
      if (this.followupList.length === 0) {
        console.warn('随访列表为空，返回空对象')
        return {}
      }
      const followupInfo = this.followupList[0]
      console.log('随访信息:', followupInfo)
      console.log('患者信息:', this.patientInfo)
      const displayData = {
        planName: followupInfo.followupPlanName || '-',
        patientName: followupInfo.patientName || this.patientInfo.name || '-',
        age: followupInfo.age || this.calculateAge(this.patientInfo.birthDate),
        gender: followupInfo.gender || this.patientInfo.gender || '-',
        idCard: followupInfo.idCard || '-',
        applicationTime: followupInfo.applicationTime || '-',
        enrollmentTime: followupInfo.enrollmentTime || '-',
        source: followupInfo.source || '-',
        phone: followupInfo.phone || this.patientInfo.phone || '-',
        primaryDoctor: followupInfo.primaryDoctor || '-',
        healthManager: followupInfo.healthManager || '-'
      }
      console.log('组合后的显示数据:', displayData)
      return displayData
    },

    /** 获取随访状态类型 */
    getFollowupStatusType(status) {
      const typeMap = {
        '待入组': 'info',
        '随访中': 'warning',
        '已暂停': 'danger',
        '已完成': 'success',
        '已退出': 'info'
      }
      return typeMap[status] || 'info'
    },

    /** 开始随访 */
    handleStartFollowup() {
      // 加载随访计划列表
      this.loadFollowupPlans()
      // 重置表单
      this.followupForm = {
        planId: '',
        planName: '',
        project: '',
        team: '西南医科大学附属医院',
        primaryDoctor: '',
        healthManager: ''
      }
      this.startFollowupVisible = true
    },

    /** 加载随访计划列表 */
    loadFollowupPlans() {
      // 加载所有状态的随访计划（不限制status）
      const params = {
        pageNum: 1,
        pageSize: 100
        // 不传status参数，加载所有状态的计划
      }

      console.log('请求随访计划列表，参数:', params)

      getFollowupPlanList(params).then(response => {
        console.log('===== 随访计划API响应 =====')
        console.log('完整响应:', response)
        console.log('响应数据:', response.data)
        console.log('记录列表:', response.data.records)

        this.followupPlanList = response.data.records || []
        console.log('加载的随访计划数量:', this.followupPlanList.length)
        console.log('计划列表内容:', this.followupPlanList)

        if (this.followupPlanList.length === 0) {
          this.$message.warning('当前没有可用的随访计划，请先创建随访计划')
        } else {
          console.log('成功加载随访计划:', this.followupPlanList.map(p => ({
            id: p.id,
            planName: p.planName,
            status: p.status
          })))
        }
      }).catch(error => {
        console.error('===== 加载随访计划失败 =====')
        console.error('错误对象:', error)
        console.error('错误信息:', error.message)
        console.error('错误响应:', error.response)
        this.$message.error('加载随访计划列表失败: ' + (error.message || '未知错误'))
      })
    },

    /** 随访计划选择变化 */
    handlePlanChange(planId) {
      const selectedPlan = this.followupPlanList.find(p => p.id === planId)
      if (selectedPlan) {
        this.followupForm.planName = selectedPlan.planName
        this.followupForm.project = selectedPlan.linkedProjectName || ''
      }
    },

    /** 提交开始随访 */
    submitStartFollowup() {
      // 验证必填项
      if (!this.followupForm.planId) {
        this.$message.warning('请选择随访计划')
        return
      }
      if (!this.followupForm.team) {
        this.$message.warning('请输入随访团队')
        return
      }

      // 构建请求数据，过滤空值
      const data = {
        patientId: this.patientInfo.id,
        followupPlanId: this.followupForm.planId,
        followupPlanName: this.followupForm.planName || null,
        followupProjectName: this.followupForm.project || null,
        followupTeam: this.followupForm.team,
        primaryDoctor: this.followupForm.primaryDoctor || null,
        healthManager: this.followupForm.healthManager || null,
        source: '医生推荐',
        followupStatus: '待入组',
        createdBy: '系统管理员'
      }

      console.log('提交的随访数据:', data)

      enrollPatient(data).then(() => {
        this.$message.success('开始随访成功')
        this.startFollowupVisible = false
        // 刷新随访列表
        this.getFollowupList()
      }).catch(error => {
        console.error('开始随访失败:', error)
        this.$message.error('开始随访失败')
      })
    },

    /** 处方分页 - 每页条数改变 */
    handlePrescriptionSizeChange(size) {
      this.prescriptionQuery.pageSize = size
      this.prescriptionQuery.pageNum = 1
      this.getPrescriptionList()
    },

    /** 处方分页 - 当前页改变 */
    handlePrescriptionPageChange(page) {
      this.prescriptionQuery.pageNum = page
      this.getPrescriptionList()
    },

    /** 评定分页 - 每页条数改变 */
    handleAssessmentSizeChange(size) {
      this.assessmentQuery.pageSize = size
      this.assessmentQuery.pageNum = 1
      this.getAssessmentList()
    },

    /** 评定分页 - 当前页改变 */
    handleAssessmentPageChange(page) {
      this.assessmentQuery.pageNum = page
      this.getAssessmentList()
    },

    /** 查看处方详情 */
    handleViewPrescription(row) {
      this.$message.info('查看处方详情功能开发中...')
    },

    /** 编辑处方 */
    handleEditPrescription(row) {
      this.$message.info('编辑处方功能开发中...')
    },

    /** 查看评定详情 */
    handleViewAssessment(row) {
      this.$message.info('查看评定详情功能开发中...')
    },

    /** 执行评定 */
    handleExecuteAssessment(row) {
      this.$message.info('执行评定功能开发中...')
    },

    /** 查看健康档案 */
    handleViewHealthRecord() {
      if (!this.patientInfo.id) {
        this.$message.error('患者ID不存在')
        return
      }
      this.healthRecordDialogVisible = true
    },

    /** 查看专病档案 */
    handleViewDiseaseRecord() {
      if (!this.patientInfo.id) {
        this.$message.error('患者ID不存在')
        return
      }
      this.diseaseRecordDialogVisible = true
    },

    /** 查看入组评估 */
    handleViewEnrollmentAssessment() {
      if (!this.patientInfo.id) {
        this.$message.error('患者ID不存在')
        return
      }
      this.enrollmentAssessmentDialogVisible = true
    },

    /** 健康档案保存成功回调 */
    handleHealthRecordSaved() {
      this.$message.success('健康档案保存成功')
      this.healthRecordDialogVisible = false
    },

    /** 专病档案保存成功回调 */
    handleDiseaseRecordSaved() {
      this.$message.success('专病档案保存成功')
      this.diseaseRecordDialogVisible = false
    },

    /** 入组评估保存成功回调 */
    handleEnrollmentAssessmentSaved() {
      this.$message.success('入组评估保存成功')
      this.enrollmentAssessmentDialogVisible = false
    },

    /** 获取处方状态文本 */
    getStatusText(status) {
      const statusMap = {
        1: '已创建',
        2: '执行中',
        3: '已完成',
        4: '已结束'
      }
      return statusMap[status] || '未知'
    },

    /** 获取处方状态类型 */
    getStatusType(status) {
      const statusMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return statusMap[status] || 'info'
    },

    /** 获取评定状态文本 */
    getAssessmentStatusText(status) {
      const statusMap = {
        1: '已创建',
        2: '执行中',
        3: '已完成',
        4: '已结束'
      }
      return statusMap[status] || '未知'
    },

    /** 获取评定状态类型 */
    getAssessmentStatusType(status) {
      const statusMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
// 基本信息标签页内容样式
.basic-info-content {
  padding: 20px 40px;

  .info-row {
    display: flex;
    align-items: center;
    margin-bottom: 24px;

    .info-label {
      width: 80px;
      color: #606266;
      font-size: 14px;
    }

    .view-btn {
      background-color: rgb(144, 126, 179);
      border-color: rgb(144, 126, 179);
      color: #fff;
      padding: 8px 24px;

      &:hover,
      &:focus {
        background-color: rgb(124, 106, 159);
        border-color: rgb(124, 106, 159);
      }
    }
  }
}

.patient-info-card {
  margin-bottom: 0;

  ::v-deep .el-card__body {
    padding: 16px;
  }

  ::v-deep .el-table {
    font-size: 14px;

    // 去掉表格边框
    &::before {
      display: none;
    }

    th,
    th.el-table__cell {
      background-color: #fff !important;
      color: rgb(144, 126, 179);
      font-weight: 500;
      border: none !important;
      padding: 0 !important;
      text-align: center !important;
    }

    td,
    td.el-table__cell {
      border: none !important;
      padding: 0 !important;
      text-align: center !important;
    }

    // 核心：清除所有cell的padding，完全居中
    .el-table__cell {
      border: none !important;
      text-align: center !important;
      padding: 0 !important;

      .cell {
        padding: 12px 10px !important;
        text-align: center !important;
        line-height: 1.5 !important;
        display: flex !important;
        justify-content: center !important;
        align-items: center !important;
      }
    }
  }
}

.male-icon {
  color: #409eff;
  font-weight: bold;
  margin: 0 2px;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
  margin: 0 2px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

::v-deep .el-table {
  font-size: 12px;

  th {
    font-weight: 500;
    padding: 8px 0;
  }

  td {
    padding: 8px 0;
  }

  .el-button--text {
    padding: 0 5px;
    margin: 0 2px;
    font-size: 14px;
    color: rgb(106, 91, 140);

    &:hover {
      color: rgb(96, 81, 130);
    }
  }
}

::v-deep .el-tabs__item {
  color: #303133;
  font-size: 14px;

  &.is-active {
    color: rgb(106, 91, 140);
  }

  &:hover {
    color: rgb(106, 91, 140);
  }
}

::v-deep .el-tabs__active-bar {
  background-color: rgb(106, 91, 140);
}

::v-deep .el-tag--mini {
  padding: 0 8px;
  height: 24px;
  line-height: 22px;
}

::v-deep .el-descriptions {
  .el-descriptions__label {
    font-weight: 500;
    color: #606266;
    background-color: #fafafa;
  }

  .el-descriptions__content {
    color: #303133;
  }
}

// 随访信息展示样式
.followup-info-section {
  margin-bottom: 30px;
}

.followup-info-table {
  ::v-deep .el-table__body tr {
    height: 45px;
  }

  ::v-deep .el-table__cell {
    padding: 8px 12px;
  }

  .info-label {
    font-size: 14px;
    color: #606266;
    font-weight: 500;
  }

  .info-value {
    font-size: 14px;
    color: #303133;
  }
}

// 随访计划展示样式
.followup-plan-content {
  margin-bottom: 30px;
}

.followup-progress {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 20px;

  .progress-text {
    font-size: 14px;
    color: #606266;
  }

  .progress-value {
    font-size: 24px;
    font-weight: bold;
    color: rgb(106, 91, 140);
  }
}

.followup-timeline {
  .timeline-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 30px;
    position: relative;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .timeline-left {
    width: 200px;
    flex-shrink: 0;
    padding-right: 20px;
    position: relative;

    .timeline-date {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
      margin-bottom: 5px;
    }

    .timeline-status {
      font-size: 12px;
      color: #909399;
      margin-bottom: 10px;
    }

    .timeline-dot {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background-color: rgb(106, 91, 140);
      position: absolute;
      right: -6px;
      top: 5px;
    }

    .timeline-line {
      width: 2px;
      background-color: #e4e7ed;
      position: absolute;
      right: -1px;
      top: 20px;
      bottom: -35px;
    }

    .terminate-btn {
      margin-top: 10px;
      width: 80px;
    }
  }

  .timeline-divider {
    width: 2px;
    background-color: #e4e7ed;
    margin: 0 20px;
    flex-shrink: 0;
  }

  .timeline-right {
    flex: 1;
    min-width: 0;

    .task-card {
      .task-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .task-title {
          font-size: 16px;
          color: #303133;
          font-weight: 500;
          margin: 0;
        }

        .task-actions-inline {
          display: flex;
          align-items: center;
          gap: 8px;

          .action-link {
            color: rgb(106, 91, 140);
            font-size: 14px;
            text-decoration: none;
            cursor: pointer;

            &:hover {
              color: rgb(96, 81, 130);
              text-decoration: underline;
            }

            &.danger {
              color: #f56c6c;

              &:hover {
                color: #f56c6c;
              }
            }
          }

          .divider {
            color: #dcdfe6;
            margin: 0 4px;
          }
        }
      }

      .task-footer {
        display: flex;
        justify-content: flex-start;
      }
    }
  }
}

.operation-log {
  margin-top: 30px;
}

// 随访管理样式
.followup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e4e7ed;

  .plan-name {
    .label {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }

    .value {
      font-size: 16px;
      color: #606266;
      margin-left: 10px;
    }
  }
}

.patient-info-section {
  background-color: #f9fafb;
  padding: 20px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;

  .info-row {
    display: flex;
    justify-content: flex-start;
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }

    .info-item {
      flex: 1;
      display: flex;
      align-items: center;
      min-width: 0;

      .info-label {
        font-size: 19px;
        color: rgb(24, 28, 24);
        white-space: nowrap;
        font-weight: 500;
      }

      .info-value {
        font-size: 19px;
        color: rgb(24, 28, 24);
        margin-left: 5px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .edit-link {
        font-size: 19px;
        color: #409eff;
        margin-left: 8px;
        text-decoration: none;
        white-space: nowrap;
        cursor: pointer;

        &:hover {
          color: #66b1ff;
          text-decoration: underline;
        }
      }
    }
  }
}
</style>
