<template>
  <div class="schedule-container">
    <el-card>
      <div slot="header">
        <span>排课</span>
      </div>

      <!-- 筛选和查询区域 -->
      <div class="filter-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
          <el-form-item label="教室名称">
            <el-select v-model="queryParams.classroomName" placeholder="请选择教室" clearable style="width: 200px">
              <el-option label="游泳教室01" value="游泳教室01" />
              <el-option label="游泳教室012" value="游泳教室012" />
            </el-select>
          </el-form-item>
          <el-form-item label="课时时长">
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
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">立即查询</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 时间范围选择 -->
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

      <!-- 排课表格 -->
      <div class="schedule-table-container">
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
              :key="`${day.date}-${timeSlot}`"
              class="schedule-cell"
              :class="{
                'active': getCellStatus(day.date, timeSlot).isActive,
                'disabled': !getCellStatus(day.date, timeSlot).isActive
              }"
              @click="handleCellClick(day.date, timeSlot, $event)"
            >
              <div v-if="getCellStatus(day.date, timeSlot).personName" class="person-name">
                {{ getCellStatus(day.date, timeSlot).personName }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右键菜单 -->
      <div
        v-if="contextMenu.visible"
        class="context-menu"
        :style="{ left: contextMenu.x + 'px', top: contextMenu.y + 'px' }"
        @click.stop
      >
        <div
          v-for="item in contextMenu.items"
          :key="item.key"
          class="menu-item"
          @click="item.action"
        >
          {{ item.label }}
        </div>
      </div>

      <!-- 发布排班按钮 -->
      <div class="action-container">
        <el-button type="primary" icon="el-icon-edit" @click="handlePublish">发布排班</el-button>
      </div>
    </el-card>

    <!-- 关闭课时弹窗 -->
    <el-dialog
      title="关闭课时"
      :visible.sync="closeClassDialog.visible"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form :model="closeClassDialog.form" label-width="80px">
        <el-form-item label="适用次数" required>
          <el-select v-model="closeClassDialog.form.scope" placeholder="请选择适用次数" style="width: 100%">
            <el-option label="本次" value="this" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
            <el-option label="长期" value="long" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeClassDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="confirmCloseClass">确定</el-button>
      </div>
    </el-dialog>

    <!-- 配置人员弹窗 -->
    <el-dialog
      title="配置人员"
      :visible.sync="personConfigDialog.visible"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form :model="personConfigDialog.form" label-width="80px">
        <el-form-item label="适用次数" required>
          <el-select v-model="personConfigDialog.form.scope" placeholder="请选择适用次数" style="width: 100%">
            <el-option label="本次" value="this" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
            <el-option label="长期" value="long" />
          </el-select>
        </el-form-item>
        <el-form-item label="人员名称" required>
          <el-select v-model="personConfigDialog.form.personId" placeholder="请选择人员" style="width: 100%">
            <el-option
              v-for="person in personList"
              :key="person.id"
              :label="person.name"
              :value="person.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="personConfigDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="confirmPersonConfig">确定</el-button>
      </div>
    </el-dialog>

    <!-- 开启课时弹窗 -->
    <el-dialog
      title="开启课时"
      :visible.sync="openClassDialog.visible"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form :model="openClassDialog.form" label-width="80px">
        <el-form-item label="适用次数" required>
          <el-select v-model="openClassDialog.form.scope" placeholder="请选择适用次数" style="width: 100%">
            <el-option label="本次" value="this" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
            <el-option label="长期" value="long" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openClassDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="confirmOpenClass">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Schedule',
  data() {
    return {
      // 查询参数
      queryParams: {
        classroomName: '',
        classDuration: '',
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
      timeSlots: ['09:00', '11:00', '13:00', '15:00', '17:00'],
      // 课程状态数据
      scheduleData: {},
      // 右键菜单
      contextMenu: {
        visible: false,
        x: 0,
        y: 0,
        items: [],
        currentCell: null
      },
      // 关闭课时弹窗
      closeClassDialog: {
        visible: false,
        form: {
          scope: 'this'
        }
      },
      // 配置人员弹窗
      personConfigDialog: {
        visible: false,
        form: {
          scope: 'this',
          personId: ''
        }
      },
      // 开启课时弹窗
      openClassDialog: {
        visible: false,
        form: {
          scope: 'this'
        }
      },
      // 人员列表
      personList: [
        { id: 1, name: '张教练' },
        { id: 2, name: '李教练' },
        { id: 3, name: '王教练' },
        { id: 4, name: '赵教练' }
      ]
    }
  },
  created() {
    this.initWeekDays()
    this.initScheduleData()
  },
  mounted() {
    // 点击其他地方关闭右键菜单
    document.addEventListener('click', () => {
      this.contextMenu.visible = false
    })
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

    // 初始化课程数据
    initScheduleData() {
      // 初始化一些示例数据，部分时段被占用
      const today = new Date()
      const weekdays = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']

      for (let i = 0; i < 7; i++) {
        const date = new Date(today)
        date.setDate(today.getDate() - (today.getDay() === 0 ? 6 : today.getDay() - 1) + i)
        const dateStr = this.formatDate(date)

        this.timeSlots.forEach(timeSlot => {
          const key = `${dateStr}-${timeSlot}`
          // 随机设置一些时段为被占用状态
          if (Math.random() > 0.3) {
            this.scheduleData[key] = {
              isActive: true,
              personId: null,
              personName: null
            }
          } else {
            this.scheduleData[key] = {
              isActive: false,
              personId: null,
              personName: null
            }
          }
        })
      }
    },

    // 获取单元格状态
    getCellStatus(date, timeSlot) {
      const key = `${date}-${timeSlot}`
      return this.scheduleData[key] || { isActive: false, personId: null, personName: null }
    },

    // 处理单元格点击
    handleCellClick(date, timeSlot, event) {
      event.stopPropagation()
      const key = `${date}-${timeSlot}`
      const status = this.getCellStatus(date, timeSlot)

      this.contextMenu.currentCell = { date, timeSlot, key, status }
      this.contextMenu.x = event.clientX
      this.contextMenu.y = event.clientY

      if (status.isActive) {
        // 白色背景（被占用）的选项
        const items = [
          { key: 'close', label: '关闭课时', action: this.showCloseClassDialog },
          { key: 'config', label: '配置人员', action: this.showPersonConfigDialog }
        ]

        // 如果已配置人员，添加取消人员选项
        if (status.personId) {
          items.push({ key: 'cancelPerson', label: '取消人员', action: this.cancelPerson })
        }

        this.contextMenu.items = items
      } else {
        // 浅灰色背景（未占用）的选项
        this.contextMenu.items = [
          { key: 'open', label: '开启课时', action: this.showOpenClassDialog }
        ]
      }

      this.contextMenu.visible = true
    },

    // 显示关闭课时弹窗
    showCloseClassDialog() {
      this.closeClassDialog.form.scope = 'this'
      this.closeClassDialog.visible = true
      this.contextMenu.visible = false
    },

    // 确认关闭课时
    confirmCloseClass() {
      const { currentCell } = this.contextMenu
      if (!currentCell) return

      const { key } = currentCell
      const scope = this.closeClassDialog.form.scope

      // 根据适用范围更新状态
      this.updateClassStatus(key, false, scope)

      this.closeClassDialog.visible = false
      this.$message.success('课时已关闭')
    },

    // 显示配置人员弹窗
    showPersonConfigDialog() {
      this.personConfigDialog.form.scope = 'this'
      this.personConfigDialog.form.personId = ''
      this.personConfigDialog.visible = true
      this.contextMenu.visible = false
    },

    // 确认配置人员
    confirmPersonConfig() {
      const { currentCell } = this.contextMenu
      if (!currentCell) return

      if (!this.personConfigDialog.form.personId) {
        this.$message.warning('请选择人员')
        return
      }

      const { key } = currentCell
      const personId = this.personConfigDialog.form.personId
      const person = this.personList.find(p => p.id === personId)

      // 更新人员配置
      this.scheduleData[key].personId = personId
      this.scheduleData[key].personName = person.name

      this.personConfigDialog.visible = false
      this.$message.success('人员配置成功')
    },

    // 取消人员配置
    cancelPerson() {
      const { currentCell } = this.contextMenu
      if (!currentCell) return

      const { key } = currentCell
      this.scheduleData[key].personId = null
      this.scheduleData[key].personName = null

      this.contextMenu.visible = false
      this.$message.success('人员配置已取消')
    },

    // 显示开启课时弹窗
    showOpenClassDialog() {
      this.openClassDialog.form.scope = 'this'
      this.openClassDialog.visible = true
      this.contextMenu.visible = false
    },

    // 确认开启课时
    confirmOpenClass() {
      const { currentCell } = this.contextMenu
      if (!currentCell) return

      const { key } = currentCell
      const scope = this.openClassDialog.form.scope

      // 根据适用范围更新状态
      this.updateClassStatus(key, true, scope)

      this.openClassDialog.visible = false
      this.$message.success('课时已开启')
    },

    // 更新课程状态
    updateClassStatus(key, isActive, scope) {
      const [date, timeSlot] = key.split('-')

      if (scope === 'this') {
        // 仅本次
        this.scheduleData[key].isActive = isActive
      } else if (scope === 'week') {
        // 本周所有相同时段
        this.weekDays.forEach(day => {
          const weekKey = `${day.date}-${timeSlot}`
          if (this.scheduleData[weekKey]) {
            this.scheduleData[weekKey].isActive = isActive
          }
        })
      } else if (scope === 'month') {
        // 本月所有相同时段
        const [year, month] = date.split('-')
        for (let i = 1; i <= 31; i++) {
          const dateStr = `${year}-${month.padStart(2, '0')}-${i.toString().padStart(2, '0')}`
          const monthKey = `${dateStr}-${timeSlot}`
          if (this.scheduleData[monthKey]) {
            this.scheduleData[monthKey].isActive = isActive
          }
        }
      } else if (scope === 'long') {
        // 长期，所有相同时段
        Object.keys(this.scheduleData).forEach(k => {
          if (k.endsWith(`-${timeSlot}`)) {
            this.scheduleData[k].isActive = isActive
          }
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

      // 更新时间范围
      this.timeRange.startDate = this.formatDate(startOfWeek)
      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6)
      this.timeRange.endDate = this.formatDate(endOfWeek)
    },

    // 发布排班
    handlePublish() {
      this.$message.success('排班已发布')
      // 这里可以调用后端API进行发布
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
.schedule-container {
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

.schedule-table-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 20px;
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

  .schedule-cell {
    flex: 1;
    padding: 12px;
    border-right: 1px solid #ebeef5;
    min-height: 60px;
    position: relative;
    cursor: pointer;

    &:last-child {
      border-right: none;
    }

    // 白色背景 - 表示该时间段正在被使用
    &.active {
      background: #ffffff;
      border: 1px solid #dcdfe6;
    }

    // 浅灰色背景 - 表示该时间段未被使用
    &.disabled {
      background: #f5f7fa;
      border: 1px solid #e4e7ed;
    }

    &:hover {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      transform: translateY(-1px);
      transition: all 0.2s ease;
    }

    .person-name {
      font-size: 12px;
      color: #409eff;
      font-weight: 500;
      text-align: center;
      background: rgba(64, 158, 255, 0.1);
      border-radius: 3px;
      padding: 2px 4px;
      margin-top: 2px;
    }
  }
}

.action-container {
  text-align: right;
}

// 右键菜单样式
.context-menu {
  position: fixed;
  background: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 2000;
  min-width: 120px;
  padding: 4px 0;

  .menu-item {
    padding: 10px 16px;
    font-size: 14px;
    color: #606266;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background: #f5f7fa;
      color: #409eff;
    }

    &:first-child {
      border-radius: 4px 4px 0 0;
    }

    &:last-child {
      border-radius: 0 0 4px 4px;
    }
  }
}

// 弹窗样式
.el-dialog {
  .el-form-item {
    margin-bottom: 20px;
  }

  .dialog-footer {
    text-align: right;
    padding-top: 20px;
  }
}
</style>