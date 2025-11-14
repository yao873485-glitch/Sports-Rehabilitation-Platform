<template>
  <div class="app-container">
    <!-- 新增按钮 -->
    <div class="button-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-container">
      <div class="filter-item">
        <el-select v-model="queryParams.diseaseType" placeholder="病种类型" clearable style="width: 150px;">
          <el-option label="全部" value="" />
          <el-option
            v-for="type in diseaseTypes"
            :key="type"
            :label="type"
            :value="type"
          />
        </el-select>
      </div>

      <div class="filter-item">
        <el-select v-model="queryParams.status" placeholder="评定状态" clearable style="width: 120px;">
          <el-option label="全部" value="" />
          <el-option label="已结束" value="已结束" />
          <el-option label="创建中" value="创建中" />
          <el-option label="执行中" value="执行中" />
          <el-option label="已完成" value="已完成" />
          <el-option label="未开始" value="未开始" />
        </el-select>
      </div>

      <div class="filter-item">
        <el-select v-model="queryParams.enrollmentInstitution" placeholder="入组机构" clearable style="width: 200px;">
          <el-option label="全部" value="" />
          <el-option
            v-for="institution in institutions"
            :key="institution"
            :label="institution"
            :value="institution"
          />
        </el-select>
      </div>

      <div class="filter-item">
        <el-input
          v-model="queryParams.prescribingDoctor"
          placeholder="开方医生"
          style="width: 150px;"
          clearable
        />
      </div>

      <div class="filter-item">
        <el-input
          v-model="queryParams.searchKeyword"
          placeholder="患者姓名、电话或档案号"
          style="width: 200px;"
          clearable
        />
      </div>

      <div class="filter-item">
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="assessmentList"
      border
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column prop="assessmentName" label="评定方案名称" width="200" show-overflow-tooltip />

      <el-table-column
        prop="customerInfo"
        label="客户信息"
        width="180"
        show-overflow-tooltip
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

      <el-table-column prop="medicalRecordNo" label="档案号" width="150" />

      <el-table-column prop="diseaseType" label="病种" width="150" />

      <el-table-column prop="enrollmentInstitution" label="入组机构" width="180" />

      <el-table-column prop="executionInstitution" label="执行机构" width="180" />

      <el-table-column prop="status" label="状态" width="120">
        <template slot-scope="scope">
          <el-tag
            :type="getStatusType(scope.row.status)"
            size="mini"
          >
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="prescribingDoctor" label="开方医生" width="120" />

      <el-table-column prop="createdTime" label="创建时间" width="160" />

      <el-table-column
        label="操作"
        width="150"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleView(scope.row)"
          >
            查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            :disabled="scope.row.status === '已结束'"
            @click="handleEnd(scope.row)"
          >
            结束
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看详情对话框 -->
    <el-dialog
      title="评定方案详情"
      :visible.sync="detailVisible"
      width="800px"
      append-to-body
    >
      <div v-if="currentAssessment" class="assessment-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="评定方案名称">{{ currentAssessment.assessmentName }}</el-descriptions-item>
          <el-descriptions-item label="客户姓名">{{ currentAssessment.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ currentAssessment.gender }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ currentAssessment.age }}岁</el-descriptions-item>
          <el-descriptions-item label="档案号">{{ currentAssessment.medicalRecordNo }}</el-descriptions-item>
          <el-descriptions-item label="病种">{{ currentAssessment.diseaseType }}</el-descriptions-item>
          <el-descriptions-item label="入组机构">{{ currentAssessment.enrollmentInstitution }}</el-descriptions-item>
          <el-descriptions-item label="执行机构">{{ currentAssessment.executionInstitution }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentAssessment.status)">
              {{ getStatusText(currentAssessment.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="开方医生">{{ currentAssessment.prescribingDoctor }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ currentAssessment.createdTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 新增评定方案对话框 -->
    <el-dialog
      title="新增评定方案"
      :visible.sync="addVisible"
      width="600px"
      append-to-body
    >
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="120px">
        <el-form-item label="评定方案名称" prop="assessmentName">
          <el-input v-model="addForm.assessmentName" placeholder="请输入评定方案名称" />
        </el-form-item>
        <el-form-item label="客户姓名" prop="patientName">
          <el-input v-model="addForm.patientName" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="addForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="addForm.age" :min="1" :max="120" />
        </el-form-item>
        <el-form-item label="档案号" prop="medicalRecordNumber">
          <el-input v-model="addForm.medicalRecordNumber" placeholder="请输入档案号" />
        </el-form-item>
        <el-form-item label="病种" prop="diseaseType">
          <el-select v-model="addForm.diseaseType" placeholder="请选择病种" style="width: 100%">
            <el-option
              v-for="type in diseaseTypes"
              :key="type"
              :label="type"
              :value="type"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="入组机构" prop="enrollmentInstitution">
          <el-select v-model="addForm.enrollmentInstitution" placeholder="请选择入组机构" style="width: 100%">
            <el-option
              v-for="institution in institutions"
              :key="institution"
              :label="institution"
              :value="institution"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="执行机构" prop="executionInstitution">
          <el-select v-model="addForm.executionInstitution" placeholder="请选择执行机构" style="width: 100%">
            <el-option
              v-for="institution in institutions"
              :key="institution"
              :label="institution"
              :value="institution"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开方医生" prop="prescribingDoctor">
          <el-input v-model="addForm.prescribingDoctor" placeholder="请输入开方医生姓名" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAdd">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPatientAssessmentPlanList,
  getPatientAssessmentPlanDetail,
  addPatientAssessmentPlan,
  endPatientAssessmentPlan,
  getDiseaseTypes,
  getInstitutions
} from '@/api/patient-assessment-plan'

export default {
  name: 'AssessmentList',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 评定方案表格数据
      assessmentList: [],
      // 病种类型列表
      diseaseTypes: [],
      // 机构列表
      institutions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        diseaseType: '',
        status: '',
        enrollmentInstitution: '',
        prescribingDoctor: '',
        searchKeyword: ''
      },
      // 详情弹窗
      detailVisible: false,
      currentAssessment: null,
      // 新增弹窗
      addVisible: false,
      // 新增表单
      addForm: {
        assessmentName: '',
        patientName: '',
        gender: '男',
        age: 30,
        medicalRecordNumber: '',
        diseaseType: '',
        enrollmentInstitution: '',
        executionInstitution: '',
        prescribingDoctor: ''
      },
      // 新增表单验证规则
      addRules: {
        assessmentName: [
          { required: true, message: '请输入评定方案名称', trigger: 'blur' }
        ],
        patientName: [
          { required: true, message: '请输入客户姓名', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        age: [
          { required: true, message: '请输入年龄', trigger: 'blur' }
        ],
        medicalRecordNumber: [
          { required: true, message: '请输入档案号', trigger: 'blur' }
        ],
        diseaseType: [
          { required: true, message: '请选择病种', trigger: 'change' }
        ],
        enrollmentInstitution: [
          { required: true, message: '请选择入组机构', trigger: 'change' }
        ],
        executionInstitution: [
          { required: true, message: '请选择执行机构', trigger: 'change' }
        ],
        prescribingDoctor: [
          { required: true, message: '请输入开方医生姓名', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDiseaseTypes()
    this.getInstitutions()
  },
  methods: {
    /** 查询评定方案列表 */
    getList() {
      this.loading = true
      getPatientAssessmentPlanList(this.queryParams).then(response => {
        this.assessmentList = response.data.records
        this.total = response.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /** 获取病种类型 */
    getDiseaseTypes() {
      getDiseaseTypes().then(response => {
        this.diseaseTypes = response.data
      })
    },

    /** 获取机构列表 */
    getInstitutions() {
      getInstitutions().then(response => {
        this.institutions = response.data
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        diseaseType: '',
        status: '',
        enrollmentInstitution: '',
        prescribingDoctor: '',
        searchKeyword: ''
      }
      this.handleQuery()
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.addVisible = true
      this.$nextTick(() => {
        this.$refs.addForm && this.$refs.addForm.resetFields()
      })
    },

    /** 提交新增 */
    submitAdd() {
      this.$refs.addForm.validate(valid => {
        if (valid) {
          addPatientAssessmentPlan(this.addForm).then(() => {
            this.$message.success('新增成功')
            this.addVisible = false
            this.getList()
          })
        }
      })
    },

    /** 查看详情 */
    handleView(row) {
      getPatientAssessmentPlanDetail(row.id).then(response => {
        this.currentAssessment = response.data
        this.detailVisible = true
      })
    },

    /** 结束评定方案 */
    handleEnd(row) {
      this.$confirm('确认结束该评定方案吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        endPatientAssessmentPlan(row.id).then(() => {
          this.$message.success('评定方案已结束')
          this.getList()
        })
      })
    },

    /** 计算年龄 */
    calculateAge(birthDate) {
      if (!birthDate) return ''
      const birth = new Date(birthDate)
      const today = new Date()
      let age = today.getFullYear() - birth.getFullYear()
      const monthDiff = today.getMonth() - birth.getMonth()
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--
      }
      return age
    },

    /** 获取状态文本 */
    getStatusText(status) {
      const statusMap = {
        1: '未开始',
        2: '执行中',
        3: '已完成',
        4: '已结束'
      }
      return statusMap[status] || '未知'
    },

    /** 获取状态类型 */
    getStatusType(status) {
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
.button-container {
  margin-bottom: 20px;
}

.male-icon {
  color: #409eff;
  font-weight: bold;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
}

.assessment-detail {
  padding: 20px;
}

.filter-container {
  .filter-item {
    margin-right: 10px;
    margin-bottom: 10px;
  }
}

.el-table {
  ::v-deep .fixed-width {
    .el-button--mini {
      padding: 2px 4px;
      margin-left: 2px;
    }
  }

  // 固定操作列样式
  ::v-deep .el-table__fixed-right {
    .el-table__fixed-right-patch {
      background-color: #f5f7fa;
    }
  }
}
</style>