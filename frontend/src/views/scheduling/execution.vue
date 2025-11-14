<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <div class="filter-item">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户姓名"
          style="width: 200px;"
          clearable
        />
      </div>

      <div class="filter-item">
        <el-select v-model="queryParams.classroomName" placeholder="教室名称" clearable style="width: 150px;">
          <el-option label="全部" value="" />
          <el-option label="游泳教室01" value="游泳教室01" />
          <el-option label="普拉提教室01" value="普拉提教室01" />
          <el-option label="康复训练室" value="康复训练室" />
          <el-option label="瑜伽教室" value="瑜伽教室" />
        </el-select>
      </div>

      <div class="filter-item">
        <el-select v-model="queryParams.reservationStatus" placeholder="状态" clearable style="width: 120px;">
          <el-option label="全部" value="" />
          <el-option label="已取消" value="已取消" />
          <el-option label="待核销" value="待核销" />
          <el-option label="已核销" value="已核销" />
          <el-option label="已逾期" value="已逾期" />
        </el-select>
      </div>

      <div class="filter-item">
        <el-date-picker
          v-model="queryParams.dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          :default-time="['00:00:00', '23:59:59']"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 340px;"
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
      :data="reservationList"
      border
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column prop="reservationDate" label="预约日期" width="160" />
      
      <el-table-column prop="reservedBy" label="预约人员" width="120" />
      
      <el-table-column prop="classroomName" label="教室名称" width="150" />
      
      <el-table-column prop="exerciseType" label="运动方式" width="120" />
      
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
            {{ scope.row.age }}岁
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="medicalRecordNumber" label="档案号" width="150" />
      
      <el-table-column prop="createdTime" label="创建时间" width="160" />
      
      <el-table-column prop="reservationStatus" label="预约状态" width="120">
        <template slot-scope="scope">
          <el-tag
            :type="getStatusType(scope.row.reservationStatus)"
            size="mini"
          >
            {{ scope.row.reservationStatus }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column
        label="操作"
        width="80"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            :disabled="scope.row.reservationStatus !== '待核销'"
            @click="handleVerify(scope.row)"
          >
            核销
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
  </div>
</template>

<script>
import { getReservationList, verifyReservation } from '@/api/reservation'

export default {
  name: 'ReservationExecution',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 预约表格数据
      reservationList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerName: '',
        classroomName: '',
        reservationStatus: '',
        dateRange: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询预约列表 */
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      
      // 处理日期范围
      if (params.dateRange && params.dateRange.length === 2) {
        params.startDate = params.dateRange[0]
        params.endDate = params.dateRange[1]
      }
      delete params.dateRange

      getReservationList(params).then(response => {
        this.reservationList = response.data.records
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
        customerName: '',
        classroomName: '',
        reservationStatus: '',
        dateRange: null
      }
      this.handleQuery()
    },
    
    /** 核销预约 */
    handleVerify(row) {
      this.$confirm('确认核销该预约记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        verifyReservation(row.id).then(() => {
          this.$message.success('预约已核销')
          this.getList()
        })
      })
    },
    
    /** 获取状态类型 */
    getStatusType(status) {
      const statusMap = {
        '待核销': 'warning',
        '已核销': 'success',
        '已逾期': 'danger',
        '已取消': 'info'
      }
      return statusMap[status] || 'info'
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
