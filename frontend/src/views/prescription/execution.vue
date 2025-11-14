<template>
  <div class="app-container">
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
        <el-select v-model="queryParams.prescriptionStatus" placeholder="处方状态" clearable style="width: 120px;">
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
        <el-select v-model="queryParams.executionInstitution" placeholder="执行机构" clearable style="width: 200px;">
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
      :data="prescriptionList"
      border
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column prop="prescriptionName" label="运动处方名称" width="200" show-overflow-tooltip />

      <el-table-column
        prop="patientInfo"
        label="患者信息"
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

      <el-table-column prop="prescriptionStatus" label="处方状态" width="120">
        <template slot-scope="scope">
          <el-tag
            :type="getStatusType(scope.row.prescriptionStatus)"
            size="mini"
          >
            {{ getStatusText(scope.row.prescriptionStatus) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="prescribingDoctor" label="开方医生" width="120" />

      <el-table-column prop="createdTime" label="创建时间" width="160" />

      <el-table-column
        label="操作"
        width="250"
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
            :disabled="scope.row.prescriptionStatus === '已结束'"
            @click="handleEnd(scope.row)"
          >
            结束
          </el-button>
          <el-button
            size="mini"
            type="text"
            :disabled="scope.row.prescriptionStatus === '已完成' || scope.row.prescriptionStatus === '已结束'"
            @click="handleExecute(scope.row)"
          >
            执行
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleExecution(scope.row)"
          >
            执行情况
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleList(scope.row)"
          >
            清单
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
      title="处方执行详情"
      :visible.sync="detailVisible"
      width="800px"
      append-to-body
    >
      <div v-if="currentPrescription" class="prescription-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="处方名称">{{ currentPrescription.prescriptionName }}</el-descriptions-item>
          <el-descriptions-item label="患者姓名">{{ currentPrescription.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ currentPrescription.gender }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ currentPrescription.age }}岁</el-descriptions-item>
          <el-descriptions-item label="档案号">{{ currentPrescription.medicalRecordNumber }}</el-descriptions-item>
          <el-descriptions-item label="病种">{{ currentPrescription.diseaseType }}</el-descriptions-item>
          <el-descriptions-item label="入组机构">{{ currentPrescription.enrollmentInstitution }}</el-descriptions-item>
          <el-descriptions-item label="执行机构">{{ currentPrescription.executionInstitution }}</el-descriptions-item>
          <el-descriptions-item label="处方状态">
            <el-tag :type="getStatusType(currentPrescription.prescriptionStatus)">
              {{ currentPrescription.prescriptionStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="开方医生">{{ currentPrescription.prescribingDoctor }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ currentPrescription.createdTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 执行情况对话框 -->
    <el-dialog
      title="处方执行情况"
      :visible.sync="executionVisible"
      width="800px"
      append-to-body
    >
      <div v-if="currentPrescription" class="execution-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="处方名称">{{ currentPrescription.prescriptionName }}</el-descriptions-item>
          <el-descriptions-item label="患者姓名">{{ currentPrescription.patientName }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(currentPrescription.prescriptionStatus)">
              {{ currentPrescription.prescriptionStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="执行机构">{{ currentPrescription.executionInstitution }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentPrescription.createdTime }}</el-descriptions-item>
          <el-descriptions-item label="执行进度">
            <el-progress :percentage="getExecutionProgress(currentPrescription.prescriptionStatus)" />
          </el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>执行记录</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in getExecutionRecords(currentPrescription)"
              :key="index"
              :timestamp="activity.timestamp"
              :color="activity.color"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="executionVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPrescriptionExecutionList,
  getPrescriptionExecutionDetail,
  endPrescriptionExecution,
  executePrescription,
  getDiseaseTypes,
  getInstitutions
} from '@/api/prescription-execution'

export default {
  name: 'PrescriptionExecution',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 处方表格数据
      prescriptionList: [],
      // 病种类型列表
      diseaseTypes: [],
      // 机构列表
      institutions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        diseaseType: '',
        prescriptionStatus: '',
        enrollmentInstitution: '',
        executionInstitution: '',
        searchKeyword: ''
      },
      // 详情弹窗
      detailVisible: false,
      // 执行情况弹窗
      executionVisible: false,
      currentPrescription: null
    }
  },
  created() {
    this.getList()
    this.getDiseaseTypes()
    this.getInstitutions()
  },
  methods: {
    /** 查询处方列表 */
    getList() {
      this.loading = true
      getPrescriptionExecutionList(this.queryParams).then(response => {
        this.prescriptionList = response.data.records
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
        prescriptionStatus: '',
        enrollmentInstitution: '',
        executionInstitution: '',
        searchKeyword: ''
      }
      this.handleQuery()
    },

    /** 查看详情 */
    handleView(row) {
      getPrescriptionExecutionDetail(row.id).then(response => {
        this.currentPrescription = response.data
        this.detailVisible = true
      })
    },

    /** 结束处方 */
    handleEnd(row) {
      this.$confirm('确认结束该处方吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        endPrescriptionExecution(row.id).then(() => {
          this.$message.success('处方已结束')
          this.getList()
        })
      })
    },

    /** 执行处方 */
    handleExecute(row) {
      this.$confirm('确认开始执行该处方吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        executePrescription(row.id).then(() => {
          this.$message.success('处方开始执行')
          this.getList()
        })
      })
    },

    /** 清单操作 */
    handleList(row) {
      this.$message.info('清单功能正在开发中...')
    },

    /** 查看执行情况 */
    handleExecution(row) {
      getPrescriptionExecutionDetail(row.id).then(response => {
        this.currentPrescription = response.data
        this.executionVisible = true
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
    },

    /** 获取执行进度 */
    getExecutionProgress(status) {
      const progressMap = {
        '未开始': 0,
        '创建中': 25,
        '执行中': 75,
        '已完成': 100,
        '已结束': 100
      }
      return progressMap[status] || 0
    },

    /** 获取执行记录 */
    getExecutionRecords(prescription) {
      const records = []

      if (prescription.createdTime) {
        records.push({
          content: '处方创建',
          timestamp: prescription.createdTime,
          color: '#67C23A'
        })
      }

      if (prescription.prescriptionStatus === '执行中' || prescription.prescriptionStatus === '已完成' || prescription.prescriptionStatus === '已结束') {
        records.push({
          content: '开始执行',
          timestamp: prescription.updatedTime,
          color: '#E6A23C'
        })
      }

      if (prescription.prescriptionStatus === '已完成') {
        records.push({
          content: '执行完成',
          timestamp: prescription.updatedTime,
          color: '#409EFF'
        })
      }

      if (prescription.prescriptionStatus === '已结束') {
        records.push({
          content: '处方已结束',
          timestamp: prescription.updatedTime,
          color: '#F56C6C'
        })
      }

      return records
    }
  }
}
</script>

<style lang="scss" scoped>
.male-icon {
  color: #409eff;
  font-weight: bold;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
}

.prescription-detail, .execution-detail {
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