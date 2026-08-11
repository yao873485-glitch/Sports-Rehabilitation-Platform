<template>
  <div class="prescription-list-container">
    <!-- 患者信息卡片 -->
    <el-card class="patient-info-card" shadow="never">
      <el-table
        :data="[patientInfo]"
        :show-header="true"
        style="width: 100%"
      >
        <el-table-column prop="name" label="姓名" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证" align="center" />
        <el-table-column prop="recordNumber" label="档案号" align="center" />
        <el-table-column prop="birthDate" label="出生日期" align="center" />
        <el-table-column prop="diagnosis" label="诊断" align="center" />
        <el-table-column prop="diseaseType" label="病种" align="center" />
      </el-table>
    </el-card>

    <!-- 处方执行清单表格 -->
    <el-card class="list-card" shadow="never">
      <div class="table-container">
        <el-table
          :data="prescriptionList"
          border
          style="width: 100%"
          header-cell-class-name="table-header"
          class="prescription-table"
        >
          <el-table-column prop="序号" label="序号" width="80" align="center" fixed />
          <el-table-column prop="运动方式" label="运动方式" width="150" align="center" />
          <el-table-column prop="运动类型" label="运动类型" width="150" align="center" />
          <el-table-column prop="开始时间" label="开始时间" width="120" align="center" />
          <el-table-column prop="结束时间" label="结束时间" width="120" align="center" />
          <el-table-column prop="执行时长（分）" label="执行时长（分）" width="150" align="center" />
          <el-table-column prop="单次运动时间（分）" label="单次运动时间（分）" width="180" align="center" />
          <el-table-column prop="执行人员" label="执行人员" width="120" align="center" />
          <el-table-column prop="执行次数" label="执行次数" width="120" align="center" />
          <el-table-column prop="执行设备" label="执行设备" width="150" align="center" />
          <el-table-column prop="状态" label="状态" width="100" align="center" />
          <el-table-column prop="执行记录" label="执行记录" width="120" align="center" />
          <el-table-column label="操作" width="120" align="center" fixed="right">
            <template>
              <el-button type="text" size="small">操作</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页信息 -->
        <div class="pagination-info">
          <span>当前显示1-{{ prescriptionList.length }}条，总共 {{ total }} 条数据</span>
          <el-pagination
            :current-page="currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next, sizes"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getPatientDetail } from '@/api/patient'

export default {
  name: 'PrescriptionList',
  data() {
    return {
      patientInfo: {
        name: '',
        idCard: '',
        recordNumber: '',
        birthDate: '',
        diagnosis: '',
        diseaseType: ''
      },
      prescriptionList: [],
      total: 0,
      currentPage: 1,
      pageSize: 10
    }
  },
  created() {
    // 从路由参数获取患者ID和方案ID
    const patientId = this.$route.query.patientId
    const schemeId = this.$route.query.schemeId

    if (patientId) {
      this.loadPatientInfo(patientId)
      this.loadPrescriptionList(patientId, schemeId)
    }
  },
  methods: {
    // 加载患者信息
    async loadPatientInfo(patientId) {
      try {
        const response = await getPatientDetail(patientId)
        if (response.data) {
          const basicInfo = response.data.basicInfo
          const detailInfo = response.data.detailInfo

          // 计算年龄
          const age = this.calculateAge(basicInfo.birthDate)

          this.patientInfo = {
            name: `${basicInfo.name}·${basicInfo.gender}·${age}岁`,
            idCard: basicInfo.idCard || '-',
            recordNumber: basicInfo.medicalRecordNo || '-',
            birthDate: basicInfo.birthDate || '-',
            diagnosis: detailInfo ? (detailInfo.diagnosis || '-') : '-',
            diseaseType: basicInfo.diseaseType || '-'
          }
        }
      } catch (error) {
        console.error('加载患者信息失败:', error)
        this.$message.error('加载患者信息失败')
      }
    },
    // 计算年龄
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
    // 加载处方执行清单
    loadPrescriptionList(patientId, schemeId) {
      // TODO: 调用API获取处方执行清单
      console.log('加载处方执行清单，patientId:', patientId, 'schemeId:', schemeId)
      // 临时空数据
      this.prescriptionList = []
      this.total = 0
    },
    // 分页大小改变
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      const patientId = this.$route.query.patientId
      const schemeId = this.$route.query.schemeId
      this.loadPrescriptionList(patientId, schemeId)
    },
    // 当前页改变
    handleCurrentChange(val) {
      this.currentPage = val
      const patientId = this.$route.query.patientId
      const schemeId = this.$route.query.schemeId
      this.loadPrescriptionList(patientId, schemeId)
    }
  }
}
</script>

<style scoped lang="scss">
.prescription-list-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.patient-info-card {
  margin-bottom: 20px;
  border-radius: 8px;

  ::v-deep .el-card__body {
    padding: 16px;
  }

  ::v-deep .el-table {
    font-size: 14px;

    // 去掉表格边框
    &::before {
      display: none;
    }

    th, td {
      border: none;
    }

    .el-table__header-wrapper {
      th {
        background-color: transparent;
        color: rgb(144, 126, 179);
        font-weight: 400;
        font-size: 13px;
        padding: 8px 0;
      }
    }

    .el-table__body-wrapper {
      td {
        padding: 8px 0;
        color: #303133;
      }
    }
  }
}

.list-card {
  border-radius: 8px;

  ::v-deep .el-card__body {
    padding: 0;
  }
}

.table-container {
  width: 100%;
  overflow-x: auto;

  &::-webkit-scrollbar {
    height: 8px;
  }

  &::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 4px;
  }

  &::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 4px;

    &:hover {
      background: #a8a8a8;
    }
  }
}

.prescription-table {
  ::v-deep .table-header {
    background-color: rgb(248, 248, 249);
    color: rgb(81, 90, 110);
    font-weight: 500;
    font-size: 14px;
    height: 50px;

    .cell {
      padding: 0 10px;
    }
  }

  ::v-deep .el-table__body-wrapper {
    .el-table__row {
      td {
        padding: 12px 0;

        .cell {
          padding: 0 10px;
          font-size: 14px;
          color: #606266;
        }
      }

      &:hover > td {
        background-color: #f5f7fa;
      }
    }
  }

  ::v-deep .el-table__fixed-right {
    box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
  }

  ::v-deep .el-table__fixed {
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  }

  ::v-deep .el-button--text {
    color: rgb(106, 91, 140);

    &:hover {
      color: darken(rgb(106, 91, 140), 10%);
    }
  }
}

// 空数据样式
::v-deep .el-table__empty-block {
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

// 分页信息样式
.pagination-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  background-color: #fff;
  border-top: 1px solid #ebeef5;

  > span {
    font-size: 14px;
    color: #606266;
  }

  ::v-deep .el-pagination {
    .el-pagination__sizes {
      .el-input {
        .el-input__inner {
          border-radius: 4px;
        }
      }
    }

    .btn-prev,
    .btn-next,
    .el-pager li {
      background-color: #fff;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      margin: 0 5px;

      &:hover {
        color: rgb(106, 91, 140);
        border-color: rgb(106, 91, 140);
      }
    }

    .el-pager li.active {
      background-color: rgb(106, 91, 140);
      border-color: rgb(106, 91, 140);
      color: #fff;

      &:hover {
        background-color: rgb(106, 91, 140);
        border-color: rgb(106, 91, 140);
      }
    }
  }
}
</style>
