<template>
  <div class="followup-list-container">
    <el-card>
      <div slot="header">
        <span>随访列表管理</span>
      </div>

      <!-- 搜索筛选区域 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm" ref="searchForm" size="small">
          <el-form-item label="患者姓名" prop="patientName">
            <el-input
              v-model="searchForm.patientName"
              placeholder="请输入患者姓名"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          <el-form-item label="申请时间" prop="applicationDateRange">
            <el-date-picker
              v-model="searchForm.applicationDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item label="加入项目时间" prop="enrollmentDateRange">
            <el-date-picker
              v-model="searchForm.enrollmentDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item label="年龄范围" prop="ageRange">
            <el-select
              v-model="searchForm.ageRange"
              placeholder="请选择年龄范围"
              clearable
              style="width: 150px;"
            >
              <el-option label="18岁以下" value="0-17" />
              <el-option label="18-30岁" value="18-30" />
              <el-option label="31-50岁" value="31-50" />
              <el-option label="51-70岁" value="51-70" />
              <el-option label="70岁以上" value="71-999" />
            </el-select>
          </el-form-item>
          <el-form-item label="随访计划" prop="followupPlanName">
            <el-select
              v-model="searchForm.followupPlanName"
              placeholder="请选择随访计划"
              clearable
              style="width: 200px;"
            >
              <el-option label="妊娠健康宣教" value="妊娠健康宣教" />
              <el-option label="糖尿病随访计划" value="糖尿病随访计划" />
              <el-option label="高血压随访计划" value="高血压随访计划" />
              <el-option label="康复训练计划" value="康复训练计划" />
            </el-select>
          </el-form-item>
          <el-form-item label="随访状态" prop="followupStatus">
            <el-select
              v-model="searchForm.followupStatus"
              placeholder="请选择随访状态"
              clearable
              style="width: 150px;"
            >
              <el-option label="待入组" value="待入组" />
              <el-option label="随访中" value="随访中" />
              <el-option label="已暂停" value="已暂停" />
              <el-option label="已完成" value="已完成" />
              <el-option label="已退出" value="已退出" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" icon="el-icon-search">
              搜索
            </el-button>
            <el-button @click="handleReset" icon="el-icon-refresh">
              清空
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 患者列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="patientName" label="患者姓名" width="100" align="center" />
        <el-table-column prop="gender" label="性别" width="60" align="center" />
        <el-table-column prop="age" label="年龄" width="60" align="center">
          <template slot-scope="scope">
            {{ scope.row.age }}岁
          </template>
        </el-table-column>
        <el-table-column prop="followupPlanName" label="随访计划名称" width="150" show-overflow-tooltip />
        <el-table-column prop="followupProject" label="随访项目" width="150" show-overflow-tooltip />
        <el-table-column prop="followupTeam" label="随访团队" width="180" show-overflow-tooltip />
        <el-table-column prop="primaryDoctor" label="主负责医生" width="100" align="center" />
        <el-table-column prop="diseaseType" label="病种" width="120" show-overflow-tooltip />
        <el-table-column prop="applicationTime" label="申请时间" width="160" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.applicationTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="enrollmentTime" label="加入项目时间" width="160" align="center">
          <template slot-scope="scope">
            {{ scope.row.enrollmentTime ? formatDateTime(scope.row.enrollmentTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="phoneNumber" label="手机号" width="120" align="center" />
        <el-table-column prop="followupStatus" label="随访状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.followupStatus)"
              size="small"
            >
              {{ scope.row.followupStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleStartFollowup(scope.row)"
              icon="el-icon-phone-outline"
              style="color: #67C23A;"
              :disabled="scope.row.followupStatus === '已退出' || scope.row.followupStatus === '已完成'"
            >
              开始随访
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleViewDetail(scope.row)"
              icon="el-icon-view"
            >
              查看详情
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
              icon="el-icon-edit"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleRemove(scope.row)"
              icon="el-icon-remove"
              style="color: #f56c6c;"
              :disabled="scope.row.followupStatus === '已退出'"
            >
              移除项目
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页导航 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background
        >
          <span class="pagination-info">
            当前第 {{ (pagination.currentPage - 1) * pagination.pageSize + 1 }}-{{
              Math.min(pagination.currentPage * pagination.pageSize, pagination.total)
            }} 条，共 {{ pagination.total }} 条数据
          </span>
        </el-pagination>
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog title="患者详情" :visible.sync="detailDialogVisible" width="80%">
      <div v-if="currentPatient" class="patient-detail">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="序号">{{ currentPatient.serialNumber }}</el-descriptions-item>
          <el-descriptions-item label="患者姓名">{{ currentPatient.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ currentPatient.gender }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ currentPatient.age }}岁</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ currentPatient.phoneNumber }}</el-descriptions-item>
          <el-descriptions-item label="随访状态">
            <el-tag :type="getStatusType(currentPatient.followupStatus)">
              {{ currentPatient.followupStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="随访计划名称">{{ currentPatient.followupPlanName }}</el-descriptions-item>
          <el-descriptions-item label="随访项目">{{ currentPatient.followupProject }}</el-descriptions-item>
          <el-descriptions-item label="主负责医生">{{ currentPatient.primaryDoctor }}</el-descriptions-item>
          <el-descriptions-item label="随访团队" :span="2">{{ currentPatient.followupTeam }}</el-descriptions-item>
          <el-descriptions-item label="病种">{{ currentPatient.diseaseType }}</el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ formatDateTime(currentPatient.applicationTime) }}</el-descriptions-item>
          <el-descriptions-item label="加入项目时间">
            {{ currentPatient.enrollmentTime ? formatDateTime(currentPatient.enrollmentTime) : '暂未加入' }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- 操作日志（随访记录） -->
        <div class="followup-records-section" style="margin-top: 30px;">
          <h3 style="margin-bottom: 15px; font-size: 16px; font-weight: 600;">操作日志</h3>
          <el-table
            :data="followupRecords"
            v-loading="recordsLoading"
            element-loading-text="加载中..."
            border
            style="width: 100%"
          >
            <el-table-column prop="followupDoctor" label="操作人" width="100" align="center" />
            <el-table-column prop="followupDate" label="操作时间" width="160" align="center">
              <template slot-scope="scope">
                {{ formatDateTime(scope.row.followupDate) }}
              </template>
            </el-table-column>
            <el-table-column prop="followupType" label="类型" width="120" align="center" />
            <el-table-column prop="followupContent" label="动作" show-overflow-tooltip />
          </el-table>
          <div v-if="followupRecords.length === 0 && !recordsLoading" style="text-align: center; padding: 40px; color: #909399;">
            暂无数据
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑患者信息" :visible.sync="editDialogVisible" width="60%">
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="120px">
        <el-form-item label="患者姓名" prop="patientName">
          <el-input v-model="editForm.patientName" disabled />
        </el-form-item>
        <el-form-item label="随访计划名称" prop="followupPlanName">
          <el-select v-model="editForm.followupPlanName" placeholder="请选择随访计划" style="width: 100%;">
            <el-option label="妊娠健康宣教" value="妊娠健康宣教" />
            <el-option label="糖尿病随访计划" value="糖尿病随访计划" />
            <el-option label="高血压随访计划" value="高血压随访计划" />
            <el-option label="康复训练计划" value="康复训练计划" />
          </el-select>
        </el-form-item>
        <el-form-item label="随访团队" prop="followupTeam">
          <el-input v-model="editForm.followupTeam" placeholder="请输入随访团队" />
        </el-form-item>
        <el-form-item label="主负责医生" prop="primaryDoctor">
          <el-input v-model="editForm.primaryDoctor" placeholder="请输入主负责医生" />
        </el-form-item>
        <el-form-item label="病种" prop="diseaseType">
          <el-input v-model="editForm.diseaseType" placeholder="请输入病种" />
        </el-form-item>
        <el-form-item label="随访状态" prop="followupStatus">
          <el-select v-model="editForm.followupStatus" placeholder="请选择随访状态" style="width: 100%;">
            <el-option label="待入组" value="待入组" />
            <el-option label="随访中" value="随访中" />
            <el-option label="已暂停" value="已暂停" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已退出" value="已退出" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEdit">保存</el-button>
      </div>
    </el-dialog>

    <!-- 移除项目确认对话框 -->
    <el-dialog
      title="移除项目确认"
      :visible.sync="removeDialogVisible"
      width="30%"
      center
    >
      <div style="text-align: center; padding: 20px 0;">
        <i class="el-icon-warning" style="font-size: 48px; color: #e6a23c; margin-bottom: 20px;"></i>
        <p style="font-size: 16px; margin-bottom: 10px;">确定要移除该患者与项目的关联关系吗？</p>
        <p style="color: #909399; font-size: 14px;">移除后患者将不再参与该项目随访</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="removeDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmRemove">确定移除</el-button>
      </div>
    </el-dialog>

    <!-- 开始随访对话框 -->
    <el-dialog title="随访记录" :visible.sync="followupDialogVisible" width="70%">
      <el-form :model="followupForm" :rules="followupRules" ref="followupForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名">
              <el-input v-model="followupForm.patientName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="随访日期" prop="followupDate">
              <el-date-picker
                v-model="followupForm.followupDate"
                type="datetime"
                placeholder="选择随访日期时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="随访类型" prop="followupType">
              <el-select v-model="followupForm.followupType" placeholder="请选择随访类型" style="width: 100%;">
                <el-option label="电话随访" value="电话随访" />
                <el-option label="门诊随访" value="门诊随访" />
                <el-option label="家庭随访" value="家庭随访" />
                <el-option label="线上随访" value="线上随访" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="随访医生" prop="followupDoctor">
              <el-input v-model="followupForm.followupDoctor" placeholder="请输入随访医生" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="随访内容" prop="followupContent">
          <el-input
            type="textarea"
            v-model="followupForm.followupContent"
            placeholder="请输入随访内容"
            :rows="4"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="随访结果" prop="followupResult">
              <el-select v-model="followupForm.followupResult" placeholder="请选择随访结果" style="width: 100%;">
                <el-option label="良好" value="良好" />
                <el-option label="一般" value="一般" />
                <el-option label="需要干预" value="需要干预" />
                <el-option label="异常" value="异常" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次随访日期" prop="nextFollowupDate">
              <el-date-picker
                v-model="followupForm.nextFollowupDate"
                type="datetime"
                placeholder="选择下次随访日期"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            v-model="followupForm.remarks"
            placeholder="请输入备注信息"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="followupDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveFollowup">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFollowupList, getFollowupDetail, removeFromProject, saveFollowupRecord, getFollowupRecordsByEnrollmentId } from '@/api/followup'

export default {
  name: 'FollowupList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        patientName: '',
        applicationDateRange: null,
        enrollmentDateRange: null,
        ageRange: '',
        followupPlanName: '',
        followupStatus: ''
      },
      // 表格数据
      tableData: [],
      loading: false,
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      // 随访记录数据
      followupRecords: [],
      recordsLoading: false,
      // 对话框显示状态
      detailDialogVisible: false,
      editDialogVisible: false,
      removeDialogVisible: false,
      followupDialogVisible: false,
      // 当前操作的患者
      currentPatient: null,
      // 编辑表单
      editForm: {
        id: null,
        patientName: '',
        followupPlanName: '',
        followupTeam: '',
        primaryDoctor: '',
        diseaseType: '',
        followupStatus: ''
      },
      editRules: {
        followupPlanName: [
          { required: true, message: '请选择随访计划', trigger: 'change' }
        ],
        primaryDoctor: [
          { required: true, message: '请输入主负责医生', trigger: 'blur' },
          { max: 100, message: '医生姓名不能超过100个字符', trigger: 'blur' }
        ],
        followupTeam: [
          { max: 255, message: '随访团队名称不能超过255个字符', trigger: 'blur' }
        ],
        diseaseType: [
          { max: 100, message: '病种不能超过100个字符', trigger: 'blur' }
        ]
      },
      // 随访表单
      followupForm: {
        enrollmentId: null,
        patientId: null,
        patientName: '',
        followupDate: '',
        followupType: '',
        followupContent: '',
        followupResult: '',
        nextFollowupDate: '',
        followupDoctor: '',
        remarks: ''
      },
      followupRules: {
        followupDate: [
          { required: true, message: '请选择随访日期', trigger: 'change' }
        ],
        followupType: [
          { required: true, message: '请选择随访类型', trigger: 'change' }
        ],
        followupContent: [
          { required: true, message: '请输入随访内容', trigger: 'blur' },
          { max: 1000, message: '随访内容不能超过1000个字符', trigger: 'blur' }
        ],
        followupDoctor: [
          { required: true, message: '请输入随访医生', trigger: 'blur' },
          { max: 100, message: '医生姓名不能超过100个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          patientName: this.searchForm.patientName,
          applicationStartTime: this.searchForm.applicationDateRange ? this.searchForm.applicationDateRange[0] : null,
          applicationEndTime: this.searchForm.applicationDateRange ? this.searchForm.applicationDateRange[1] : null,
          enrollmentStartTime: this.searchForm.enrollmentDateRange ? this.searchForm.enrollmentDateRange[0] : null,
          enrollmentEndTime: this.searchForm.enrollmentDateRange ? this.searchForm.enrollmentDateRange[1] : null,
          ageRange: this.searchForm.ageRange,
          followupPlanName: this.searchForm.followupPlanName,
          followupStatus: this.searchForm.followupStatus
        }
        const response = await getFollowupList(params)
        this.tableData = response.data.records
        this.pagination.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败')
        console.error('获取数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 清空
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 查看详情
    async handleViewDetail(row) {
      try {
        const response = await getFollowupDetail(row.id)
        this.currentPatient = response.data
        this.detailDialogVisible = true
        // 加载随访记录
        this.loadFollowupRecords(row.id)
      } catch (error) {
        this.$message.error('获取详情失败')
        console.error('获取详情失败:', error)
      }
    },
    // 加载随访记录
    async loadFollowupRecords(enrollmentId) {
      this.recordsLoading = true
      try {
        const response = await getFollowupRecordsByEnrollmentId(enrollmentId)
        if (response.data.success) {
          this.followupRecords = response.data.data || []
        } else {
          this.followupRecords = []
        }
      } catch (error) {
        this.$message.error('获取随访记录失败')
        console.error('获取随访记录失败:', error)
        this.followupRecords = []
      } finally {
        this.recordsLoading = false
      }
    },
    // 编辑
    handleEdit(row) {
      this.currentPatient = { ...row }
      this.editForm = {
        id: row.id,
        patientName: row.patientName,
        followupPlanName: row.followupPlanName,
        followupTeam: row.followupTeam,
        primaryDoctor: row.primaryDoctor,
        diseaseType: row.diseaseType,
        followupStatus: row.followupStatus
      }
      this.editDialogVisible = true
    },
    // 保存编辑
    async handleSaveEdit() {
      try {
        await this.$refs.editForm.validate()
        // 这里应该调用更新API，暂时提示
        this.$message.info('保存功能开发中')
        this.editDialogVisible = false
        // this.fetchData()
      } catch (error) {
        if (error !== false) {
          this.$message.error('保存失败')
          console.error('保存失败:', error)
        }
      }
    },
    // 移除项目
    handleRemove(row) {
      this.currentPatient = { ...row }
      this.removeDialogVisible = true
    },
    // 确认移除
    async confirmRemove() {
      try {
        await removeFromProject(this.currentPatient.id)
        this.$message.success('已移除项目')
        this.removeDialogVisible = false
        this.fetchData()
      } catch (error) {
        this.$message.error('移除失败')
        console.error('移除失败:', error)
      }
    },
    // 开始随访
    handleStartFollowup(row) {
      this.currentPatient = { ...row }
      // 重置表单
      this.followupForm = {
        enrollmentId: row.id,
        patientId: row.patientId,
        patientName: row.patientName,
        followupDate: new Date().toISOString().slice(0, 19).replace('T', ' '),
        followupType: '',
        followupContent: '',
        followupResult: '',
        nextFollowupDate: '',
        followupDoctor: row.primaryDoctor || '',
        remarks: ''
      }
      this.followupDialogVisible = true
      // 如果患者状态是待入组，自动更新为随访中
      if (row.followupStatus === '待入组') {
        this.$nextTick(() => {
          this.$message.info('患者状态将自动更新为"随访中"')
        })
      }
    },
    // 保存随访记录
    async handleSaveFollowup() {
      try {
        await this.$refs.followupForm.validate()
        // 调用API保存随访记录
        const response = await saveFollowupRecord(this.followupForm)
        if (response.data.success) {
          this.$message.success('随访记录保存成功')
          this.followupDialogVisible = false
          this.fetchData()
        } else {
          this.$message.error(response.data.message || '保存失败')
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error('保存失败')
          console.error('保存失败:', error)
        }
      }
    },
    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchData()
    },
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '待入组': 'info',
        '随访中': 'warning',
        '已暂停': 'danger',
        '已完成': 'success',
        '已退出': 'info'
      }
      return statusMap[status] || 'info'
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.followup-list-container {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #f5f5f5;
    border-radius: 4px;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;

    .pagination-info {
      margin-right: 20px;
      color: #606266;
      font-size: 14px;
    }
  }

  .patient-detail {
    padding: 20px 0;
  }
}

::v-deep .el-table {
  .el-table__header-wrapper {
    th {
      background-color: #f8f9fa;
      color: #333;
      font-weight: 600;
    }
  }
}

// 日期选择器分隔符样式
::v-deep .el-date-editor {
  .el-range-separator {
    padding: 0 8px;
    width: auto;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
  }

  .el-range-input {
    width: 42%;
  }
}
</style>
