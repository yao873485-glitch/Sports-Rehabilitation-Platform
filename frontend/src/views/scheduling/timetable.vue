<template>
  <div class="timetable-container">
    <el-card>
      <div slot="header">
        <span>课表</span>
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
              <el-option label="瑜伽教室01" value="瑜伽教室01" />
              <el-option label="普拉提教室01" value="普拉提教室01" />
              <el-option label="功率自行车教室01" value="功率自行车教室01" />
            </el-select>
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
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">立即查询</el-button>
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
        <div class="time-range-info">
          当前时间范围：{{ timeRange.startDate }} 至 {{ timeRange.endDate }}
        </div>
      </div>

      <!-- 查询结果展示区 -->
      <div class="result-container">
        <div class="result-header">
          <h3>课程安排结果</h3>
        </div>

        <!-- 课表表格 -->
        <div class="timetable-table-container">
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
                class="course-cell"
              >
                <!-- 课程信息 -->
                <div v-if="getCourseInfo(day.date, timeSlot)" class="course-info">
                  <div class="course-name">{{ getCourseInfo(day.date, timeSlot).name }}</div>
                  <div class="course-teacher">教师：{{ getCourseInfo(day.date, timeSlot).teacher }}</div>
                  <div class="course-content">{{ getCourseInfo(day.date, timeSlot).content }}</div>
                </div>
                <div v-else class="empty-cell">
                  暂无课程
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Timetable',
  data() {
    return {
      // 查询参数
      queryParams: {
        date: '',
        classroomName: '',
        exerciseType: ''
      },
      // 当前周次
      currentWeek: 'currentWeek',
      // 时间范围
      timeRange: {
        startDate: '',
        endDate: ''
      },
      // 一周七天
      weekDays: [],
      // 时间槽
      timeSlots: ['09:00-10:00', '10:00-11:00', '11:00-12:00', '14:00-15:00', '15:00-16:00', '16:00-17:00'],
      // 模拟课程数据
      courseData: [
        {
          date: this.formatDate(new Date()),
          timeSlot: '09:00-10:00',
          name: '游泳基础课程',
          teacher: '张教练',
          content: '游泳基础动作教学',
          classroom: '游泳教室01',
          exerciseType: '游泳'
        },
        {
          date: this.formatDate(new Date()),
          timeSlot: '14:00-15:00',
          name: '瑜伽放松课程',
          teacher: '李老师',
          content: '瑜伽基础体式练习',
          classroom: '瑜伽教室01',
          exerciseType: '瑜伽'
        },
        {
          date: this.formatDate(new Date(new Date().setDate(new Date().getDate() + 1))),
          timeSlot: '10:00-11:00',
          name: '普拉提核心训练',
          teacher: '王教练',
          content: '核心力量训练',
          classroom: '普拉提教室01',
          exerciseType: '普拉提'
        }
      ]
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

      // 设置时间范围
      this.timeRange.startDate = this.formatDate(startOfWeek)
      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6)
      this.timeRange.endDate = this.formatDate(endOfWeek)
    },
    // 查询
    handleQuery() {
      console.log('查询参数:', this.queryParams)
      // 这里可以调用后端API进行查询
      this.$message.success('查询成功')
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

      // 更新时间范围
      this.timeRange.startDate = this.formatDate(startOfWeek)
      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6)
      this.timeRange.endDate = this.formatDate(endOfWeek)
    },
    // 获取课程信息
    getCourseInfo(date, timeSlot) {
      // 根据查询条件过滤课程
      let filteredCourses = this.courseData.filter(course =>
        course.date === date && course.timeSlot === timeSlot
      )

      // 应用筛选条件
      if (this.queryParams.classroomName) {
        filteredCourses = filteredCourses.filter(course =>
          course.classroom === this.queryParams.classroomName
        )
      }

      if (this.queryParams.exerciseType) {
        filteredCourses = filteredCourses.filter(course =>
          course.exerciseType === this.queryParams.exerciseType
        )
      }

      return filteredCourses.length > 0 ? filteredCourses[0] : null
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
.timetable-container {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.time-range-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px;
}

.time-range-info {
  color: #606266;
  font-size: 14px;
}

.result-container {
  .result-header {
    margin-bottom: 16px;
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
    }
  }
}

.timetable-table-container {
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
  width: 120px;
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
    width: 120px;
    flex-shrink: 0;
    padding: 12px;
    text-align: center;
    border-right: 1px solid #ebeef5;
    background: #fafafa;
    color: #606266;
    font-weight: 500;
  }

  .course-cell {
    flex: 1;
    padding: 8px;
    border-right: 1px solid #ebeef5;
    min-height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;

    &:last-child {
      border-right: none;
    }

    .course-info {
      background: #f0f9ff;
      border: 1px solid #bae7ff;
      border-radius: 4px;
      padding: 8px;
      width: 100%;
      text-align: left;

      .course-name {
        font-weight: 600;
        color: #1890ff;
        margin-bottom: 4px;
        font-size: 14px;
      }

      .course-teacher {
        color: #666;
        font-size: 12px;
        margin-bottom: 2px;
      }

      .course-content {
        color: #999;
        font-size: 12px;
      }
    }

    .empty-cell {
      color: #c0c4cc;
      font-size: 14px;
    }
  }
}
</style>