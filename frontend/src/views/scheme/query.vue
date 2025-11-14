<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <!-- 时间筛选功能 -->
      <div class="filter-item">
        <label>开方日期：</label>
        <el-date-picker
          v-model="queryParams.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          style="width: 300px;"
        />
      </div>

      <!-- 病种类型筛选 -->
      <div class="filter-item">
        <label>病种类型：</label>
        <el-select v-model="queryParams.diseaseType" placeholder="请选择病种类型" clearable style="width: 150px;">
          <el-option label="全部" value="" />
          <el-option label="妊娠合并糖尿病" value="妊娠合并糖尿病" />
          <el-option label="肥胖症" value="肥胖症" />
          <el-option label="高血压" value="高血压" />
          <el-option label="糖尿病" value="糖尿病" />
        </el-select>
      </div>

      <!-- 搜索功能 -->
      <div class="filter-item">
        <label>搜索：</label>
        <el-input
          v-model="queryParams.searchKeyword"
          placeholder="请输入患者姓名/手机号/档案号"
          style="width: 200px;"
          clearable
        />
      </div>

      <!-- 入组机构筛选 -->
      <div class="filter-item">
        <label>入组机构：</label>
        <el-select v-model="queryParams.orgName" placeholder="请选择入组机构" clearable style="width: 200px;">
          <el-option label="全部" value="" />
          <el-option label="西南医科大学附属医院" value="西南医科大学附属医院" />
          <el-option label="江安县中医院" value="江安县中医院" />
        </el-select>
      </div>

      <!-- 方案状态筛选 -->
      <div class="filter-item">
        <label>方案状态：</label>
        <el-select v-model="queryParams.status" placeholder="请选择方案状态" clearable style="width: 120px;">
          <el-option label="全部" value="" />
          <el-option label="执行中" :value="1" />
          <el-option label="已结束" :value="2" />
        </el-select>
      </div>

      <!-- 操作按钮 -->
      <div class="filter-item">
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="schemeList"
      border
      highlight-current-row
      style="width: 100%"
      empty-text="暂无数据"
    >
      <!-- 患者信息列 -->
      <el-table-column
        label="患者信息"
        width="130"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <div class="patient-info">
            <span class="patient-name link-type" @click="handlePatientDetail(scope.row)">{{ scope.row.patientName }}</span>
            <span :class="scope.row.gender === '男' ? 'male-icon' : 'female-icon'">
              {{ scope.row.gender === '男' ? '♂' : '♀' }}
            </span>
            <span class="patient-age">{{ calculateAge(scope.row.birthDate) }}岁</span>
          </div>
        </template>
      </el-table-column>

      <!-- 其他字段 -->
      <el-table-column prop="medicalRecordNo" label="档案号" width="150" show-overflow-tooltip />
      <el-table-column prop="schemeId" label="方案编号" width="180" show-overflow-tooltip />
      <el-table-column prop="diseaseType" label="病种" width="150" show-overflow-tooltip />
      <el-table-column prop="statusDesc" label="状态" width="120">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.status === 1 ? 'success' : 'info'"
            size="mini"
          >
            {{ scope.row.statusDesc }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orgName" label="入组机构" width="200" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column prop="endTime" label="结束时间" width="160" />

      <!-- 操作列 - 固定右侧 -->
      <el-table-column
        label="操作"
        width="120"
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
            icon="el-icon-document"
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
      title="方案详情"
      :visible.sync="detailVisible"
      width="800px"
      append-to-body
    >
      <div v-if="currentScheme" class="scheme-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="患者姓名">{{ currentScheme.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ currentScheme.gender }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ calculateAge(currentScheme.birthDate) }}岁</el-descriptions-item>
          <el-descriptions-item label="档案号">{{ currentScheme.medicalRecordNo }}</el-descriptions-item>
          <el-descriptions-item label="方案编号">{{ currentScheme.schemeId }}</el-descriptions-item>
          <el-descriptions-item label="病种">{{ currentScheme.diseaseType }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentScheme.status === 1 ? 'success' : 'info'">
              {{ currentScheme.status === 1 ? '执行中' : '已结束' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="入组机构">{{ currentScheme.enrollmentInstitution }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentScheme.createTime }}</el-descriptions-item>
          <el-descriptions-item label="结束时间">{{ currentScheme.endTime || '暂无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 处方清单对话框 -->
    <el-dialog
      title="运动处方清单"
      :visible.sync="listVisible"
      width="1200px"
      append-to-body
      :close-on-click-modal="false"
    >
      <prescription-list
        v-if="listVisible && currentScheme"
        :scheme-info="currentScheme"
        @close="listVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script>
import { getPatientSchemeInfoList, getPatientSchemeInfoDetail } from '@/api/patient-scheme-info'

export default {
  name: 'SchemeQuery',
  components: {
    PrescriptionList: () => import('./components/PrescriptionList')
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 方案表格数据
      schemeList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateRange: null,
        diseaseType: '',
        searchKeyword: '',
        orgName: '',
        status: ''
      },
      // 详情弹窗
      detailVisible: false,
      // 清单弹窗
      listVisible: false,
      currentScheme: null
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

    /** 查询方案列表 */
    getList() {
      this.loading = true
      const params = { ...this.queryParams }

      // 处理日期范围
      if (params.dateRange && params.dateRange.length === 2) {
        params.startTime = params.dateRange[0] + ' 00:00:00'
        params.endTime = params.dateRange[1] + ' 23:59:59'
      }
      delete params.dateRange

      getPatientSchemeInfoList(params).then(response => {
        this.schemeList = response.data.records
        this.total = response.data.total
        this.loading = false
      }).catch(() => {
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
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        dateRange: null,
        diseaseType: '',
        searchKeyword: '',
        orgName: '',
        status: ''
      }
      this.handleQuery()
    },

    /** 查看详情 */
    handleView(row) {
      getPatientSchemeInfoDetail(row.id).then(response => {
        this.currentScheme = response.data
        this.detailVisible = true
      })
    },

    /** 清单操作 */
    handleList(row) {
      getPatientSchemeInfoDetail(row.id).then(response => {
        this.currentScheme = response.data
        this.listVisible = true
      }).catch(error => {
        console.error('获取方案详情失败:', error)
        this.$message.error('获取方案详情失败')
      })
    },

    /** 患者详情 */
    handlePatientDetail(row) {
      this.$message.info('患者详情功能正在开发中...')
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
    display: flex;
    align-items: center;
    margin-right: 15px;
    margin-bottom: 10px;

    label {
      margin-right: 8px;
      font-weight: 500;
      white-space: nowrap;
    }

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
  }

  .patient-age {
    color: #666;
    font-size: 12px;
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

.scheme-detail {
  padding: 20px;
}

.link-type {
  color: #409eff;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
}

// 固定操作列样式
.el-table {
  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
  }
}
</style>