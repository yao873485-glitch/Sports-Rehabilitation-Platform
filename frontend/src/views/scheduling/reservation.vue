<template>
  <div class="reservation-container">
    <el-card>
      <div slot="header">
        <span>预约</span>
      </div>

      <!-- 筛选和查询区域 -->
      <div class="filter-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
          <el-form-item label="日期选择">
            <el-date-picker
              v-model="queryParams.date"
              type="date"
              placeholder="选择日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="教室名称">
            <el-select v-model="queryParams.classroomName" placeholder="请选择教室" clearable style="width: 200px">
              <el-option label="游泳教室01" value="游泳教室01" />
            </el-select>
          </el-form-item>
          <el-form-item label="课时输入">
            <el-input
              v-model="queryParams.classDuration"
              placeholder="请输入课时时长"
              clearable
              style="width: 200px"
            >
              <template slot="append">分钟</template>
            </el-input>
          </el-form-item>
          <el-form-item label="运动方式">
            <el-input
              v-model="queryParams.exerciseType"
              placeholder="请输入运动方式"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item>
            <el-button class="custom-primary-btn" icon="el-icon-search" @click="handleQuery">立即查询</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 时间范围切换 -->
      <div class="time-range-container">
        <el-radio-group v-model="currentWeek" @change="handleWeekChange">
          <el-radio-button label="lastWeek">上周</el-radio-button>
          <el-radio-button label="currentWeek">本周</el-radio-button>
          <el-radio-button label="nextWeek">下周</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 预约表格 -->
      <div class="reservation-table-container">
        <div class="table-header">
          <div class="header-cell time-header">时间</div>
          <div
            v-for="day in weekDays"
            :key="day.date"
            class="header-cell day-header"
            :class="{ 'today': day.isToday }"
          >
            {{ day.weekday }}<br>{{ day.date }}
          </div>
        </div>

        <div class="table-body">
          <div
            v-for="timeSlot in timeSlots"
            :key="timeSlot"
            class="table-row"
          >
            <div class="time-cell">{{ timeSlot }}</div>
            <div
              v-for="day in weekDays"
              :key="day.date"
              class="reservation-cell"
            >
              <!-- 空白单元格，表示没有预约课程 -->
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Reservation',
  data() {
    return {
      // 查询参数
      queryParams: {
        date: '',
        classroomName: '',
        classDuration: '',
        exerciseType: ''
      },
      // 当前周次
      currentWeek: 'currentWeek',
      // 一周七天
      weekDays: [],
      // 时间槽
      timeSlots: ['09:00', '11:00', '13:00', '15:00', '17:00']
    }
  },
  created() {
    this.initWeekDays()
  },
  methods: {
    // 初始化周日期
    initWeekDays() {
      const today = new Date()
      const currentDay = today.getDay() // 0-6, 0是周日
      const startOfWeek = new Date(today)

      // 计算本周一的日期
      startOfWeek.setDate(today.getDate() - (currentDay === 0 ? 6 : currentDay - 1))

      this.weekDays = []
      const weekdays = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']

      for (let i = 0; i < 7; i++) {
        const date = new Date(startOfWeek)
        date.setDate(startOfWeek.getDate() + i)

        const dateStr = this.formatDate(date)
        const isToday = this.isToday(date)

        this.weekDays.push({
          weekday: weekdays[i],
          date: dateStr,
          isToday: isToday
        })
      }
    },
    // 查询
    handleQuery() {
      console.log('查询参数:', this.queryParams)
      // 这里可以调用后端API进行查询
    },
    // 周次切换
    handleWeekChange(week) {
      const today = new Date()
      const startOfWeek = new Date(today)

      if (week === 'lastWeek') {
        startOfWeek.setDate(today.getDate() - 7)
      } else if (week === 'nextWeek') {
        startOfWeek.setDate(today.getDate() + 7)
      }

      // 重新计算周日期
      const currentDay = startOfWeek.getDay()
      startOfWeek.setDate(startOfWeek.getDate() - (currentDay === 0 ? 6 : currentDay - 1))

      this.weekDays = []
      const weekdays = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']

      for (let i = 0; i < 7; i++) {
        const date = new Date(startOfWeek)
        date.setDate(startOfWeek.getDate() + i)

        const dateStr = this.formatDate(date)
        const isToday = this.isToday(date)

        this.weekDays.push({
          weekday: weekdays[i],
          date: dateStr,
          isToday: isToday
        })
      }
    },
    // 格式化日期
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    // 判断是否为今天
    isToday(date) {
      const today = new Date()
      return date.getDate() === today.getDate() &&
             date.getMonth() === today.getMonth() &&
             date.getFullYear() === today.getFullYear()
    }
  }
}
</script>

<style lang="scss" scoped>
.reservation-container {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.time-range-container {
  margin-bottom: 20px;
}

.reservation-table-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.table-header {
  display: flex;
  background: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.header-cell {
  padding: 12px;
  text-align: center;
  border-right: 1px solid #ebeef5;
  font-weight: 600;
  color: #606266;

  &:last-child {
    border-right: none;
  }
}

.time-header {
  width: 100px;
  flex-shrink: 0;
}

.day-header {
  flex: 1;

  &.today {
    background: #ecf5ff;
    color: #409eff;
  }
}

.table-body {
  .table-row {
    display: flex;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
    }
  }

  .time-cell {
    width: 100px;
    flex-shrink: 0;
    padding: 12px;
    text-align: center;
    border-right: 1px solid #ebeef5;
    background: #fafafa;
    color: #606266;
  }

  .reservation-cell {
    flex: 1;
    padding: 12px;
    border-right: 1px solid #ebeef5;
    min-height: 60px;
    background: #fff;

    &:last-child {
      border-right: none;
    }

    &:hover {
      background: #f5f7fa;
      cursor: pointer;
    }
  }
}

// 自定义按钮样式
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
</style>