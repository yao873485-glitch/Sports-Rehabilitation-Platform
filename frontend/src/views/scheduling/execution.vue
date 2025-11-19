<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <div class="filter-row">
        <div class="filter-item">
          <label>搜索：</label>
          <el-input
            v-model="queryParams.customerName"
            placeholder="输入客户姓名"
            style="width: 200px;"
            clearable
          />
        </div>

        <div class="filter-item">
          <label>教室名称：</label>
          <el-select v-model="queryParams.classroomName" placeholder="全部" clearable style="width: 150px;">
            <el-option label="全部" value="" />
            <el-option label="游泳教室01" value="游泳教室01" />
            <el-option label="普拉提教室01" value="普拉提教室01" />
            <el-option label="康复训练室" value="康复训练室" />
            <el-option label="瑜伽教室" value="瑜伽教室" />
          </el-select>
        </div>

        <div class="filter-item">
          <label>状态：</label>
          <el-select v-model="queryParams.reservationStatus" placeholder="全部" clearable style="width: 120px;">
            <el-option label="全部" value="" />
            <el-option label="已取消" value="已取消" />
            <el-option label="待核销" value="待核销" />
            <el-option label="已核销" value="已核销" />
            <el-option label="已逾期" value="已逾期" />
          </el-select>
        </div>

        <div class="filter-item">
          <label>预约日期：</label>
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
          <el-button class="custom-primary-btn" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </div>
      </div>
    </div>

    <!-- 批量操作区域 -->
    <div class="batch-operation">
      <el-button class="custom-primary-btn" icon="el-icon-check" @click="handleBatchVerify">批量核销</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="reservationList"
      border
      highlight-current-row
      style="width: 100%"
      :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
    >
      <el-table-column prop="reservationDate" label="预约日期" min-width="160" align="center" header-align="center" />

      <el-table-column prop="reservedBy" label="预约人员" min-width="120" align="center" header-align="center" />

      <el-table-column prop="classroomName" label="教室名称" min-width="150" align="center" header-align="center" />

      <el-table-column prop="exerciseType" label="运动方式" min-width="120" align="center" header-align="center" />

      <el-table-column
        prop="patientInfo"
        label="患者信息"
        min-width="180"
        align="center"
        header-align="center"
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

      <el-table-column prop="medicalRecordNumber" label="档案号" min-width="150" align="center" header-align="center" />

      <el-table-column prop="createdTime" label="创建时间" min-width="160" align="center" header-align="center" />

      <el-table-column prop="reservationStatus" label="预约状态" min-width="120" align="center" header-align="center">
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
        width="100"
        fixed="right"
        align="center"
        header-align="center"
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

    /** 批量核销 */
    handleBatchVerify() {
      this.$message.info('批量核销功能正在开发中...')
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
  margin-bottom: 20px;

  .filter-row {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
  }

  .filter-item {
    display: flex;
    align-items: center;
    margin-right: 15px;
    margin-bottom: 10px;

    label {
      margin-right: 8px;
      font-weight: 500;
      white-space: nowrap;
      color: #606266;
    }
  }
}

// 批量操作区域
.batch-operation {
  text-align: right;
  margin-bottom: 15px;
}

// 自定义按钮样式
.custom-primary-btn {
  background-color: rgb(144, 126, 179);
  border-color: rgb(144, 126, 179);
  color: #fff;

  &:hover,
  &:focus {
    background-color: rgb(134, 116, 169);
    border-color: rgb(134, 116, 169);
    color: #fff;
  }

  &:active {
    background-color: rgb(124, 106, 159);
    border-color: rgb(124, 106, 159);
  }
}

.el-table {
  // 表头样式
  ::v-deep .el-table__header th {
    color: rgb(37, 37, 37);
    font-weight: 500;
    background-color: rgb(250, 250, 250);
    padding: 16px 0;
  }

  // 表格单元格样式 - 增加内边距
  ::v-deep .el-table__body td {
    padding: 16px 0;
  }

  // 表头和单元格居中对齐
  ::v-deep .el-table__header th,
  ::v-deep .el-table__body td {
    text-align: center;
  }

  // 操作列按钮样式
  ::v-deep .el-button--text {
    padding: 0 5px;
    margin: 0 2px;
    font-size: 14px;
    color: rgb(106, 91, 140);

    &:hover {
      color: rgb(96, 81, 130);
    }

    i {
      color: rgb(106, 91, 140);
    }

    &:hover i {
      color: rgb(96, 81, 130);
    }
  }

  ::v-deep .fixed-width {
    .el-button--mini {
      padding: 2px 4px;
      margin-left: 2px;
    }
  }

  // 固定操作列样式
  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
    background-color: #fff;

    .el-table__header th {
      background-color: rgb(250, 250, 250);
    }

    .el-table__fixed-right-patch {
      background-color: rgb(250, 250, 250);
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
