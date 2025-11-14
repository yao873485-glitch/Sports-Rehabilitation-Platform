<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <!-- 时间筛选 -->
      <div class="filter-item">
        <el-date-picker
          v-model="queryParams.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          style="width: 300px;"
        />
      </div>

      <!-- 搜索功能 -->
      <div class="filter-item">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入患者姓名/联系电话"
          style="width: 250px;"
          clearable
        />
      </div>

      <!-- 操作按钮 -->
      <div class="filter-item">
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleQuickCreate">快速建档</el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="patientList"
      border
      highlight-current-row
      style="width: 100%"
      empty-text="暂无数据"
    >
      <!-- 患者信息列 -->
      <el-table-column
        label="患者信息"
        min-width="180"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <div class="patient-info">
            <span class="patient-name">{{ scope.row.name }}</span>
            <span :class="scope.row.gender === '男' ? 'male-icon' : 'female-icon'">
              {{ scope.row.gender === '男' ? '♂' : '♀' }}
            </span>
            <span class="patient-age">{{ calculateAge(scope.row.birthDate) }}岁</span>
          </div>
        </template>
      </el-table-column>

      <!-- 其他字段 -->
      <el-table-column prop="birthDate" label="出生日期" width="120" />
      <el-table-column prop="idCard" label="证件号" width="180" show-overflow-tooltip />
      <el-table-column prop="phone" label="联系电话" width="130" />
      <el-table-column prop="medicalRecordNo" label="档案号" width="150" />
      <el-table-column prop="diseaseType" label="病种" width="120" show-overflow-tooltip />
      <el-table-column prop="enrollmentInstitution" label="入组机构" width="150" show-overflow-tooltip />
      <el-table-column prop="address" label="详细地址" min-width="200" show-overflow-tooltip />
      <el-table-column prop="createdTime" label="创建时间" width="160" />

      <!-- 操作列 - 固定右侧 -->
      <el-table-column
        label="操作"
        width="200"
        fixed="right"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >
            查看
          </el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-info"
            @click="handleDetail(scope.row)"
          >
            详情
          </el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >
            编辑
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

    <!-- 快速建档对话框 -->
    <el-dialog
      title="快速建档"
      :visible.sync="createVisible"
      width="600px"
      append-to-body
    >
      <patient-quick-create
        v-if="createVisible"
        @close="createVisible = false"
        @saved="handleSaved"
      />
    </el-dialog>

    <!-- 患者详情对话框 -->
    <el-dialog
      title=""
      :visible.sync="detailVisible"
      width="900px"
      :show-close="false"
      append-to-body
      class="patient-detail-dialog"
    >
      <patient-info-detail
        v-if="detailVisible"
        :patient-data="currentPatientData"
        @back="detailVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script>
import { getPatientList } from '@/api/patient'

export default {
  name: 'ArchiveList',
  components: {
    PatientQuickCreate: () => import('./components/PatientQuickCreate'),
    PatientInfoDetail: () => import('./components/PatientInfoDetail')
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 患者表格数据
      patientList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateRange: null,
        name: ''
      },
      // 对话框控制
      createVisible: false,
      detailVisible: false,
      // 当前患者ID
      currentPatientId: null,
      // 当前患者数据
      currentPatientData: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
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

    /** 查询患者列表 */
    getList() {
      this.loading = true
      const params = { ...this.queryParams }

      // 处理日期范围
      if (params.dateRange && params.dateRange.length === 2) {
        params.startTime = params.dateRange[0] + ' 00:00:00'
        params.endTime = params.dateRange[1] + ' 23:59:59'
      }
      delete params.dateRange

      getPatientList(params).then(response => {
        if (response && response.data) {
          this.patientList = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.patientList = []
          this.total = 0
        }
        this.loading = false
      }).catch(error => {
        console.error('获取患者列表失败:', error)
        this.$message.error('获取患者列表失败，请稍后重试')
        this.patientList = []
        this.total = 0
        this.loading = false
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      try {
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          dateRange: null,
          name: ''
        }
        this.handleQuery()
      } catch (error) {
        console.error('重置查询失败:', error)
        this.$message.error('重置失败，请稍后重试')
      }
    },

    /** 查看患者详情 */
    handleView(row) {
      // 跳转到患者只读查看页面
      this.$router.push({
        path: '/patient/create',
        query: {
          patientId: row.id,
          mode: 'view'
        }
      })
    },

    /** 查看患者详情 */
    handleDetail(row) {
      this.currentPatientData = { ...row }
      // 隐藏手机号中间四位
      if (this.currentPatientData.phone) {
        const phone = this.currentPatientData.phone
        this.currentPatientData.hiddenPhone = phone.substring(0, 3) + '****' + phone.substring(7)
      }
      this.detailVisible = true
    },

    /** 编辑患者信息 */
    handleEdit(row) {
      // 跳转到患者编辑页面（复用快速建档页面）
      this.$router.push({
        path: '/patient/create',
        query: {
          patientId: row.id,
          from: 'archive'
        }
      })
    },

    /** 快速建档 */
    handleQuickCreate() {
      this.createVisible = true
    },

    /** 保存成功后的处理 */
    handleSaved() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 20px;

  .filter-item {
    margin-right: 15px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;

    // 日期选择器特殊样式
    ::v-deep .el-date-editor {
      .el-range-separator {
        padding: 0 5px;
        min-width: 24px;
        text-align: center;
        font-weight: 500;
        color: #606266;
      }

      .el-range-input {
        width: 45%;
      }
    }
  }
}

.patient-info {
  display: flex;
  align-items: center;
  gap: 8px;

  .patient-name {
    font-weight: 500;
    color: #303133;
    min-width: 60px;
  }

  .patient-age {
    color: #666;
    font-size: 13px;
  }
}

.male-icon {
  color: #409eff;
  font-weight: bold;
  font-size: 14px;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
  font-size: 14px;
}

// 固定操作列样式
.el-table {
  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
  }
}
</style>
