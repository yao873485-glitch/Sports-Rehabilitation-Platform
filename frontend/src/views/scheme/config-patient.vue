<template>
  <div class="app-container">
    <!-- 患者信息卡片 - 固定顶部 -->
    <el-card class="patient-info-card">
      <el-table
        :data="[patientInfo]"
        :show-header="true"
        style="width: 100%"
      >
        <el-table-column prop="name" label="姓名" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }} · {{ scope.row.gender }} · {{ calculateAge(scope.row.birthDate) }}岁
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证" align="center" />
        <el-table-column prop="medicalRecordNo" label="档案号" align="center" />
        <el-table-column prop="birthDate" label="出生日期" align="center" />
        <el-table-column prop="diagnosis" label="诊断" align="center">
          <template slot-scope="scope">
            {{ scope.row.diagnosis || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="diseaseType" label="病种" align="center" />
      </el-table>
    </el-card>

    <!-- 主内容区 -->
    <el-card style="margin-top: 20px;">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 基本信息标签页 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicForm"
            :model="basicForm"
            :rules="basicRules"
            label-width="100px"
            style="max-width: 500px; padding: 20px 0 20px 40px;"
          >
            <!-- 方案周期 -->
            <el-form-item label="方案周期:" prop="schemeCycle" required>
              <el-select
                v-model="basicForm.schemeCycle"
                placeholder="请选择方案周期"
                style="width: 150px;"
                @change="handleCycleChange"
              >
                <el-option
                  v-for="month in 12"
                  :key="month"
                  :label="`${month}`"
                  :value="month"
                />
              </el-select>
              <span style="margin-left: 10px; color: #606266;">月</span>
            </el-form-item>

            <!-- 健康档案 -->
            <el-form-item label="健康档案:">
              <el-button
                size="small"
                class="archive-btn"
                @click="handleViewHealthRecord"
              >
                查看
              </el-button>
            </el-form-item>

            <!-- 专病档案 -->
            <el-form-item label="专病档案:">
              <el-button
                size="small"
                class="archive-btn"
                @click="handleViewDiseaseRecord"
              >
                查看
              </el-button>
            </el-form-item>

            <!-- 入组评估 -->
            <el-form-item label="入组评估:">
              <el-button
                size="small"
                class="archive-btn"
                @click="handleViewEnrollmentAssessment"
              >
                查看
              </el-button>
            </el-form-item>

            <!-- 保存按钮 -->
            <el-form-item>
              <div style="text-align: right; margin-top: 80px; padding-right: 40px;">
                <el-button
                  type="primary"
                  :disabled="!isCycleModified"
                  :loading="submitLoading"
                  class="save-btn"
                  @click="handleSave"
                >
                  保存
                </el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 运动处方标签页 -->
        <el-tab-pane label="运动处方" name="prescription" :disabled="!basicCompleted">
          <div style="padding: 20px;">
            <!-- 操作按钮区域（右上角） -->
            <div class="prescription-header">
              <div class="header-actions">
                <el-button class="prescription-action-btn" icon="el-icon-plus" size="small" @click="handleAddExercise">
                  新增一行
                </el-button>
                <el-button
                  class="prescription-action-btn save-action-btn"
                  :loading="prescriptionSaving"
                  size="small"
                  @click="handleSavePrescription"
                >
                  保存
                </el-button>
                <el-button class="prescription-action-btn" size="small" @click="handleSubmitPrescription">
                  提交
                </el-button>
              </div>
            </div>

            <!-- 运动项目表格 -->
            <el-table
              ref="prescriptionTable"
              :data="exerciseList"
              border
              class="prescription-table"
              :header-cell-style="{
                backgroundColor: 'rgb(250, 250, 250)',
                color: 'rgb(38, 38, 38)',
                textAlign: 'center'
              }"
            >
              <!-- 复选框列 -->
              <el-table-column type="selection" width="55" align="center" />

                <!-- 序号列 -->
                <el-table-column label="序号" type="index" width="60" align="center" :index="index => index + 1" />

                <!-- 运动场景 -->
                <el-table-column label="运动场景" align="center" width="120">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.exerciseScene" placeholder="请选择" size="small">
                      <el-option label="机构" value="机构" />
                      <el-option label="居家" value="居家" />
                    </el-select>
                  </template>
                </el-table-column>

                <!-- 运动类型 -->
                <el-table-column label="运动类型" align="center" width="140">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.exerciseType" placeholder="请选择" size="small">
                      <el-option label="有氧运动" value="有氧运动" />
                      <el-option label="呼吸训练" value="呼吸训练" />
                      <el-option label="平衡训练" value="平衡训练" />
                      <el-option label="抗阻训练" value="抗阻训练" />
                      <el-option label="柔韧性训练" value="柔韧性训练" />
                    </el-select>
                  </template>
                </el-table-column>

                <!-- 运动方式 -->
                <el-table-column label="运动方式" align="center" width="200">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.exerciseMethod" placeholder="请选择" size="small">
                      <el-option label="单腿站立（扶椅背）" value="单腿站立（扶椅背）" />
                      <el-option label="直线行走" value="直线行走" />
                      <el-option label="脚跟-脚尖行走" value="脚跟-脚尖行走" />
                      <el-option label="弹力带训练（侧平举）" value="弹力带训练（侧平举）" />
                      <el-option label="坐姿划船" value="坐姿划船" />
                      <el-option label="器械训练（腿举机轻量）" value="器械训练（腿举机轻量）" />
                    </el-select>
                  </template>
                </el-table-column>

                <!-- 注意事项 -->
                <el-table-column label="注意事项" align="center" width="180">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.precautions" placeholder="请输入" size="small" />
                  </template>
                </el-table-column>

                <!-- 单次运动时间 -->
                <el-table-column label="单次运动时间（分）" align="center" width="160">
                  <template slot-scope="scope">
                    <el-input-number
                      v-model="scope.row.durationMinutes"
                      :min="1"
                      :max="180"
                      size="small"
                      controls-position="right"
                    />
                  </template>
                </el-table-column>

                <!-- 运动频率 -->
                <el-table-column label="运动频率" align="center" width="120">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.exerciseFrequency" placeholder="请选择" size="small">
                      <el-option label="1次/周" value="1次/周" />
                      <el-option label="2次/周" value="2次/周" />
                      <el-option label="3次/周" value="3次/周" />
                      <el-option label="4次/周" value="4次/周" />
                      <el-option label="5次/周" value="5次/周" />
                      <el-option label="6次/周" value="6次/周" />
                      <el-option label="7次/周" value="7次/周" />
                    </el-select>
                  </template>
                </el-table-column>

                <!-- 运动强度 -->
                <el-table-column label="运动强度" align="center" width="120">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.exerciseIntensity" placeholder="请选择" size="small">
                      <el-option label="低强度" value="低强度" />
                      <el-option label="中等" value="中等" />
                      <el-option label="高强度" value="高强度" />
                    </el-select>
                  </template>
                </el-table-column>

                <!-- 运动周期（周） -->
                <el-table-column label="运动周期（周）" align="center" width="130">
                  <template slot-scope="scope">
                    <el-input-number
                      v-model="scope.row.cycleWeeks"
                      :min="1"
                      :max="52"
                      size="small"
                      controls-position="right"
                    />
                  </template>
                </el-table-column>

                <!-- 总次数（次） -->
                <el-table-column label="总次数（次）" align="center" width="130">
                  <template slot-scope="scope">
                    <el-input-number
                      v-model="scope.row.totalSessions"
                      :min="1"
                      :max="1000"
                      size="small"
                      controls-position="right"
                    />
                  </template>
                </el-table-column>

                <!-- 周运动总量（分） -->
                <el-table-column label="周运动总量（分）" align="center" width="150">
                  <template slot-scope="scope">
                    <el-input-number
                      v-model="scope.row.weeklyTotalMinutes"
                      :min="0"
                      :max="10000"
                      size="small"
                      controls-position="right"
                    />
                  </template>
                </el-table-column>

                <!-- 运动进阶 -->
                <el-table-column label="运动进阶" align="center" width="180">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.progression" placeholder="请输入" size="small" />
                  </template>
                </el-table-column>

                <!-- 备注 -->
                <el-table-column label="备注" align="center" width="180">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.remarks" placeholder="请输入" size="small" />
                  </template>
                </el-table-column>

                <!-- 操作 -->
                <el-table-column label="操作" align="center" width="100" fixed="right">
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      size="small"
                      class="delete-btn"
                      @click="handleDeleteSingleExercise(scope.$index)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 评定计划标签页 -->
        <el-tab-pane label="评定计划" name="assessment" :disabled="!assessmentEnabled">
          <div style="padding: 20px;">
            <!-- 操作按钮区域（右上角） -->
            <div class="assessment-header">
              <div class="header-actions">
                <el-button class="assessment-action-btn" icon="el-icon-plus" size="small" @click="handleAddAssessment">
                  新增一行
                </el-button>
                <el-button
                  class="assessment-action-btn"
                  :loading="assessmentSaving"
                  size="small"
                  @click="handleSaveAssessment"
                >
                  保存
                </el-button>
                <el-button
                  class="assessment-action-btn"
                  size="small"
                  @click="handleSubmitAssessment"
                >
                  提交
                </el-button>
              </div>
            </div>

            <!-- 评定计划表格 -->
            <el-table
              ref="assessmentTable"
              :data="assessmentList"
              border
              class="assessment-table"
              :header-cell-style="{
                backgroundColor: 'rgb(250, 250, 250)',
                color: 'rgb(38, 38, 38)',
                textAlign: 'center'
              }"
            >
              <!-- 复选框列 -->
              <el-table-column type="selection" width="55" align="center" />

              <!-- 序号列 -->
              <el-table-column label="序号" type="index" width="80" align="center" :index="index => index + 1" />

              <!-- 执行场景 -->
              <el-table-column label="执行场景" align="center" min-width="180">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.executionType" placeholder="请选择" size="small">
                    <el-option label="机构" value="机构" />
                    <el-option label="居家" value="居家" />
                  </el-select>
                </template>
              </el-table-column>

              <!-- 量表名称 -->
              <el-table-column label="量表名称" align="center" min-width="280">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.scaleName" placeholder="请选择" size="small">
                    <el-option label="6分钟步行试验" value="6分钟步行试验" />
                    <el-option label="疼痛评估（VAS）" value="疼痛评估（VAS）" />
                    <el-option label="运动风险产妇妊娠风险评估表" value="运动风险产妇妊娠风险评估表" />
                    <el-option label="Framingham心血管风险评分（女性版）" value="Framingham心血管风险评分（女性版）" />
                    <el-option label="呼吸量表-BODE指数" value="呼吸量表-BODE指数" />
                    <el-option label="圣乔治呼吸问卷（SGRQ）" value="圣乔治呼吸问卷（SGRQ）" />
                    <el-option label="宗氏抑郁量表" value="宗氏抑郁量表" />
                    <el-option label="抑郁自评量表（SDS）" value="抑郁自评量表（SDS）" />
                    <el-option label="多囊卵巢综合征运动干预前专病评估" value="多囊卵巢综合征运动干预前专病评估" />
                    <el-option label="骨质疏松症患者评估问卷 - 身体功能量表" value="骨质疏松症患者评估问卷 - 身体功能量表" />
                    <el-option label="PHQ-9量表" value="PHQ-9量表" />
                    <el-option label="食物频率法问卷调查(FFQ)" value="食物频率法问卷调查(FFQ)" />
                    <el-option label="RPE量表-改良博格量表 (Borg CR10 Scale)" value="RPE量表-改良博格量表 (Borg CR10 Scale)" />
                    <el-option label="妊娠期糖尿病风险评估表" value="妊娠期糖尿病风险评估表" />
                    <el-option label="肌少症改良版SARC-F量表 (SARC-CalF)" value="肌少症改良版SARC-F量表 (SARC-CalF)" />
                    <el-option label="自主感觉劳累分级表" value="自主感觉劳累分级表" />
                    <el-option label="血糖监测记录表" value="血糖监测记录表" />
                    <el-option label="坐站试验评估量表" value="坐站试验评估量表" />
                    <el-option label="6分钟步行试验登记表" value="6分钟步行试验登记表" />
                    <el-option label="呼吸量表-改良英国医学研究学会呼吸困难量表" value="呼吸量表-改良英国医学研究学会呼吸困难量表" />
                    <el-option label="爱丁堡产后抑郁量表(EPDS)" value="爱丁堡产后抑郁量表(EPDS)" />
                    <el-option label="功能性动作筛查诊断记录表" value="功能性动作筛查诊断记录表" />
                    <el-option label="妊娠期高血压风险评估量表" value="妊娠期高血压风险评估量表" />
                    <el-option label="EuroQol健康指数量表EQ-5D" value="EuroQol健康指数量表EQ-5D" />
                    <el-option label="PCOS相关生活质量问卷（PCOSQ-5）" value="PCOS相关生活质量问卷（PCOSQ-5）" />
                  </el-select>
                </template>
              </el-table-column>

              <!-- 执行频率 -->
              <el-table-column label="执行频率" align="center" min-width="150">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.executionFrequency" placeholder="请选择" size="small">
                    <el-option label="1次/周" value="1次/周" />
                    <el-option label="2次/周" value="2次/周" />
                    <el-option label="3次/周" value="3次/周" />
                    <el-option label="4次/周" value="4次/周" />
                    <el-option label="5次/周" value="5次/周" />
                    <el-option label="6次/周" value="6次/周" />
                    <el-option label="7次/周" value="7次/周" />
                  </el-select>
                </template>
              </el-table-column>

              <!-- 周期（周） -->
              <el-table-column label="周期（周）" align="center" min-width="180">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.cycleDuration"
                    :min="0"
                    :max="52"
                    size="small"
                    controls-position="right"
                  />
                </template>
              </el-table-column>

              <!-- 总次数（次） -->
              <el-table-column label="总次数（次）" align="center" min-width="150">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.totalTimes"
                    :min="1"
                    :max="1000"
                    size="small"
                    controls-position="right"
                  />
                </template>
              </el-table-column>

              <!-- 备注 -->
              <el-table-column label="备注" align="center" min-width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.remarks" placeholder="请输入" size="small" />
                </template>
              </el-table-column>

              <!-- 操作 -->
              <el-table-column label="操作" align="center" width="120" fixed="right">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="small"
                    class="preview-btn"
                    @click="handlePreviewAssessment(scope.$index)"
                  >
                    预览
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    class="delete-btn"
                    @click="handleDeleteSingleAssessment(scope.$index)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="assessment-pagination">
              <span class="pagination-info">共 {{ assessmentList.length }} 条记录</span>
              <el-pagination
                :current-page="1"
                :page-size="10"
                :total="assessmentList.length"
                layout="prev, pager, next, sizes"
                :page-sizes="[10, 20, 50]"
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 随访管理标签页 -->
        <el-tab-pane label="随访管理" name="followup" :disabled="!followupEnabled">
          <div style="padding: 20px;">
            <!-- 顶部区域：计划名称 + 编辑按钮 -->
            <div class="followup-header">
              <div class="plan-name">
                <span class="label">计划名称：</span>
                <span class="value">{{ followupPlanName }}</span>
              </div>
              <el-button type="primary" @click="handleEditPlan">
                编辑当前计划
              </el-button>
            </div>

            <!-- 患者信息展示区 -->
            <div class="patient-info-section">
              <!-- 第一行 -->
              <div class="info-row">
                <div class="info-item">
                  <span class="info-label">患者姓名：</span>
                  <span class="info-value">{{ patientInfo.name }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">患者年龄：</span>
                  <span class="info-value">{{ calculatePatientAge() }}岁</span>
                </div>
                <div class="info-item">
                  <span class="info-label">患者性别：</span>
                  <span class="info-value">{{ patientInfo.gender }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">患者身份证号：</span>
                  <span class="info-value">{{ patientInfo.idCard }}</span>
                </div>
              </div>

              <!-- 第二行 -->
              <div class="info-row">
                <div class="info-item">
                  <span class="info-label">申请时间：</span>
                  <span class="info-value">{{ followupData.applicationTime || '-' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">加入项目时间：</span>
                  <span class="info-value">{{ followupData.joinProjectTime || '-' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">来源：</span>
                  <span class="info-value">{{ followupData.source || '-' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">病种：</span>
                  <span class="info-value">{{ patientInfo.diseaseType }}</span>
                  <a href="javascript:;" class="edit-link" @click="handleEditDiseaseType">修改</a>
                </div>
              </div>

              <!-- 第三行 -->
              <div class="info-row">
                <div class="info-item">
                  <span class="info-label">手机号：</span>
                  <span class="info-value">{{ patientInfo.phone }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">主负责医生：</span>
                  <span class="info-value">{{ followupData.primaryDoctor || '暂无' }}</span>
                  <a href="javascript:;" class="edit-link" @click="handleEditPrimaryDoctor">修改</a>
                </div>
                <div class="info-item">
                  <span class="info-label">健管师：</span>
                  <span class="info-value">{{ followupData.healthManager || '暂无' }}</span>
                  <a href="javascript:;" class="edit-link" @click="handleEditHealthManager">修改</a>
                </div>
              </div>
            </div>

            <!-- 分隔线 -->
            <el-divider />

            <!-- 操作按钮区（未开始随访时显示） -->
            <div v-if="!showFollowupPlan" class="followup-action">
              <el-button type="primary" icon="el-icon-phone" @click="handleStartFollowup">
                开始随访
              </el-button>
            </div>

            <!-- 随访计划展示区（已开始随访后显示） -->
            <div v-if="showFollowupPlan" class="followup-plan-content">
              <!-- 随访计划进度 -->
              <div class="followup-progress">
                <span class="progress-text">随访计划进度：已完成</span>
                <span class="progress-value">{{ followupPlanProgress }}%</span>
              </div>

              <!-- 时间轴式任务列表 -->
              <div class="followup-timeline">
                <div
                  v-for="(task, index) in followupTasks"
                  :key="index"
                  class="timeline-item"
                >
                  <!-- 左侧时间轴区域 -->
                  <div class="timeline-left">
                    <div class="timeline-date">{{ task.date }}</div>
                    <div class="timeline-status">{{ task.statusText }}</div>
                    <div class="timeline-dot"></div>
                    <div v-if="index < followupTasks.length - 1" class="timeline-line"></div>
                    <el-button
                      type="danger"
                      size="small"
                      class="terminate-btn"
                      @click="handleTerminateTask(index)"
                    >
                      中止
                    </el-button>
                  </div>

                  <!-- 中间分隔线 -->
                  <div class="timeline-divider"></div>

                  <!-- 右侧任务详情卡片 -->
                  <div class="timeline-right">
                    <el-card class="task-card">
                      <div class="task-header">
                        <h4 class="task-title">{{ task.title }}</h4>
                        <div class="task-actions-inline">
                          <a href="javascript:;" class="action-link" @click="handleGoFill(task)">去填写</a>
                          <span class="divider">|</span>
                          <a href="javascript:;" class="action-link" @click="handleRemindPatient(task)">提醒患者</a>
                          <span class="divider">|</span>
                          <a href="javascript:;" class="action-link" @click="handleTemporaryChange(task)">发起临时变更</a>
                          <span class="divider">|</span>
                          <a href="javascript:;" class="action-link danger" @click="handleTerminateTask(index)">中止</a>
                        </div>
                      </div>

                      <div class="task-footer">
                        <el-button
                          type="primary"
                          size="small"
                          @click="handleTemporaryEvent(task)"
                        >
                          发起临时事件
                        </el-button>
                      </div>
                    </el-card>
                  </div>
                </div>
              </div>
            </div>

            <!-- 分隔线 -->
            <el-divider v-if="!showFollowupPlan" />

            <!-- 开始随访对话框 -->
            <el-dialog
              title="开始随访"
              :visible.sync="startFollowupDialogVisible"
              width="600px"
              :close-on-click-modal="false"
              @close="handleCloseFollowupDialog"
            >
              <!-- 步骤条 -->
              <el-steps :active="followupStep" align-center finish-status="success" style="margin-bottom: 30px;">
                <el-step title="选择开始日期" icon="el-icon-date" />
                <el-step title="选择计划模板" icon="el-icon-document" />
              </el-steps>

              <!-- 第一步：选择开始日期 -->
              <div v-if="followupStep === 0" class="step-content">
                <el-form label-width="120px">
                  <el-form-item label="开始日期">
                    <el-date-picker
                      v-model="followupStartDate"
                      type="date"
                      placeholder="请选择开始日期"
                      style="width: 100%;"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      prefix-icon="el-icon-date"
                    />
                  </el-form-item>
                </el-form>
              </div>

              <!-- 第二步：选择计划模板 -->
              <div v-if="followupStep === 1" class="step-content">
                <el-form label-width="120px">
                  <el-form-item label="计划模板">
                    <el-select
                      v-model="followupTemplate"
                      placeholder="请选择计划模板"
                      style="width: 100%;"
                      @change="handleTemplateChange"
                    >
                      <el-option
                        v-for="template in followupTemplateList"
                        :key="template.value"
                        :label="template.label"
                        :value="template.value"
                      />
                    </el-select>
                  </el-form-item>
                </el-form>
              </div>

              <!-- 底部按钮 -->
              <div slot="footer" class="dialog-footer">
                <el-button @click="startFollowupDialogVisible = false">取消</el-button>
                <el-button v-if="followupStep > 0" @click="handlePreviousStep">上一步</el-button>
                <el-button v-if="followupStep === 0" type="primary" @click="handleNextStep">下一步</el-button>
                <el-button v-if="followupStep === 1" type="primary" @click="handleNextStep">确认</el-button>
              </div>
            </el-dialog>

            <!-- 操作日志 -->
            <div class="operation-log">
              <h3 style="margin-bottom: 15px; font-size: 16px; color: #303133;">操作日志</h3>
              <el-table
                :data="operationLogs"
                border
                style="width: 100%"
              >
                <el-table-column prop="operator" label="操作人" width="150" />
                <el-table-column prop="operationTime" label="操作时间" width="180" />
                <el-table-column prop="channel" label="渠道" width="120" />
                <el-table-column prop="action" label="动作" />
              </el-table>
              <div v-if="operationLogs.length === 0" style="text-align: center; padding: 40px; color: #909399;">
                暂无数据
              </div>
            </div>
          </div>

          <!-- 修改病种对话框 -->
          <el-dialog title="修改病种" :visible.sync="diseaseTypeDialogVisible" width="400px">
            <el-form label-width="80px">
              <el-form-item label="病种">
                <el-select v-model="editDiseaseType" placeholder="请选择病种" style="width: 100%;">
                  <el-option label="妊娠合并糖尿病" value="妊娠合并糖尿病" />
                  <el-option label="肥胖症" value="肥胖症" />
                  <el-option label="高血压" value="高血压" />
                  <el-option label="糖尿病" value="糖尿病" />
                  <el-option label="冠心病" value="冠心病" />
                  <el-option label="慢性阻塞性肺疾病" value="慢性阻塞性肺疾病" />
                  <el-option label="骨关节炎" value="骨关节炎" />
                  <el-option label="颈椎病" value="颈椎病" />
                  <el-option label="腰椎间盘突出" value="腰椎间盘突出" />
                  <el-option label="脑卒中后遗症" value="脑卒中后遗症" />
                </el-select>
              </el-form-item>
            </el-form>
            <div slot="footer">
              <el-button @click="diseaseTypeDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSaveDiseaseType">保存</el-button>
            </div>
          </el-dialog>

          <!-- 修改主负责医生对话框 -->
          <el-dialog title="修改主负责医生" :visible.sync="primaryDoctorDialogVisible" width="400px">
            <el-form label-width="100px">
              <el-form-item label="主负责医生">
                <el-input v-model="editPrimaryDoctor" placeholder="请输入医生姓名" />
              </el-form-item>
            </el-form>
            <div slot="footer">
              <el-button @click="primaryDoctorDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSavePrimaryDoctor">保存</el-button>
            </div>
          </el-dialog>

          <!-- 修改健管师对话框 -->
          <el-dialog title="修改健管师" :visible.sync="healthManagerDialogVisible" width="400px">
            <el-form label-width="80px">
              <el-form-item label="健管师">
                <el-input v-model="editHealthManager" placeholder="请输入健管师姓名" />
              </el-form-item>
            </el-form>
            <div slot="footer">
              <el-button @click="healthManagerDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSaveHealthManager">保存</el-button>
            </div>
          </el-dialog>

          <!-- 提醒患者对话框 -->
          <el-dialog
            title="设置提醒时间"
            :visible.sync="remindPatientDialogVisible"
            width="500px"
            :close-on-click-modal="false"
          >
            <el-form label-width="0">
              <el-form-item>
                <el-date-picker
                  v-model="remindTime"
                  type="datetime"
                  placeholder="选择提醒时间"
                  style="width: 100%;"
                  format="yyyy-MM-dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  prefix-icon="el-icon-time"
                />
              </el-form-item>
              <div style="color: #909399; font-size: 14px; margin-top: 10px;">
                点击后，患者将在微信上收到填写的提醒推送，是否继续？
              </div>
            </el-form>
            <div slot="footer">
              <el-button @click="remindPatientDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleConfirmRemind">确定</el-button>
            </div>
          </el-dialog>

          <!-- 中止确认对话框 -->
          <el-dialog
            title="提示"
            :visible.sync="terminateConfirmVisible"
            width="450px"
            :close-on-click-modal="false"
          >
            <div style="display: flex; align-items: center; gap: 15px;">
              <i class="el-icon-warning" style="font-size: 24px; color: #E6A23C;"></i>
              <span style="font-size: 14px; color: #606266;">
                中止后，该事件对该患者将失效，是否确认中止？
              </span>
            </div>
            <div slot="footer">
              <el-button @click="terminateConfirmVisible = false">取消</el-button>
              <el-button type="primary" @click="handleConfirmTerminate">确认</el-button>
            </div>
          </el-dialog>

          <!-- 发起临时事件对话框 -->
          <el-dialog
            title="发起临时事件"
            :visible.sync="temporaryEventDialogVisible"
            width="500px"
            :close-on-click-modal="false"
          >
            <el-form :model="temporaryEventForm" label-width="100px">
              <el-form-item label="通知类型">
                <el-select v-model="temporaryEventForm.notifyType" placeholder="请选择通知类型" style="width: 100%;">
                  <el-option label="不要通知" value="不要通知" />
                </el-select>
              </el-form-item>
              <el-form-item label="事件名称">
                <el-input v-model="temporaryEventForm.eventName" placeholder="请输入事件名称" />
              </el-form-item>
              <el-form-item label="事件类型">
                <el-select v-model="temporaryEventForm.eventType" placeholder="请选择事件类型" style="width: 100%;">
                  <el-option label="文章" value="文章" />
                  <el-option label="表单" value="表单" />
                </el-select>
              </el-form-item>
            </el-form>
            <div slot="footer">
              <el-button @click="temporaryEventDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleConfirmTemporaryEvent">确认</el-button>
            </div>
          </el-dialog>

          <!-- 编辑计划对话框 -->
          <el-dialog
            :visible.sync="editPlanDialogVisible"
            width="900px"
            :close-on-click-modal="false"
            :show-close="false"
            fullscreen
          >
            <!-- 顶部导航区 -->
            <div slot="title" class="edit-plan-header">
              <div class="header-left">
                <a href="javascript:;" class="back-link" @click="handleCancelEditPlan">
                  <i class="el-icon-arrow-left"></i> 返回
                </a>
                <span class="edit-title">编辑</span>
              </div>
              <el-button class="cancel-btn" @click="handleCancelEditPlan">取消</el-button>
            </div>

            <div class="edit-plan-content">
              <!-- 基本信息展示区 -->
              <div class="basic-info-section">
                <div class="info-row">
                  <div class="info-label">计划名称：</div>
                  <div class="info-value">{{ planForm.planName }}</div>
                </div>
                <div class="info-row">
                  <div class="info-item">
                    <span class="info-label">创建时间：</span>
                    <span class="info-value">{{ planForm.createTime }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">最后一次修改时间：</span>
                    <span class="info-value">{{ planForm.lastModifyTime }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">状态：</span>
                    <span class="info-value">{{ planForm.status }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">当前版本号：</span>
                    <span class="info-value">{{ planForm.version }}</span>
                  </div>
                </div>
              </div>

              <!-- 可见团队区域 -->
              <div class="section">
                <h3>可见团队（{{ planForm.visibleTeam.length }}）</h3>
              </div>

              <!-- 计划描述区 -->
              <div class="section">
                <h3>计划描述</h3>
                <el-input
                  v-model="planForm.description"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入计划描述"
                />
              </div>

              <!-- 功能操作区 -->
              <div class="section">
                <el-button type="primary" icon="el-icon-plus" plain @click="handleAddFollowupTask">
                  新增随访任务
                </el-button>
              </div>

              <!-- 随访任务列表 -->
              <div v-for="(task, index) in planForm.followupTasks" :key="index" class="followup-task-item">
                <div class="task-header">
                  <span class="task-title">第{{ task.orderNum }}次随访</span>
                  <div class="task-actions">
                    <i class="el-icon-edit" @click="handleEditTaskName(index)"></i>
                    <i class="el-icon-delete" @click="handleDeleteTask(index)"></i>
                  </div>
                </div>
                <div class="task-interval">
                  <el-button icon="el-icon-minus" size="mini" @click="task.intervalDays--"></el-button>
                  <el-input-number v-model="task.intervalDays" :min="1" size="mini" style="width: 80px; margin: 0 10px;" />
                  <el-select v-model="task.intervalUnit" size="mini" style="width: 80px;">
                    <el-option label="天" value="天" />
                    <el-option label="周" value="周" />
                    <el-option label="月" value="月" />
                  </el-select>
                  <el-button
                    icon="el-icon-plus"
                    size="mini"
                    circle
                    type="primary"
                    style="margin-left: 20px;"
                    @click="handleAddFollowupEvent(index)"
                  />
                </div>
              </div>

              <!-- 底部提示信息 -->
              <div class="tip-text">
                无论是否选择提前提醒或超时提醒，在计划随访的当天早上10点，会推送一条对应的随访任务消息给到患者
              </div>

              <!-- 提前提醒 -->
              <div class="remind-section">
                <div class="remind-header">
                  <el-checkbox v-model="advanceRemind.enabled">提前提醒</el-checkbox>
                  <span class="remind-desc">
                    未到随访时间，提前提醒用户将要随访，用户完成随访任务的当天，将立即结束提醒
                  </span>
                </div>
                <div v-if="advanceRemind.enabled" class="remind-config">
                  <span>在计划随访的</span>
                  <el-input-number v-model="advanceRemind.days" :min="1" size="small" style="width: 80px; margin: 0 5px;" />
                  <el-select v-model="advanceRemind.unit" size="small" style="width: 80px; margin: 0 5px;">
                    <el-option label="天" value="天" />
                    <el-option label="周" value="周" />
                    <el-option label="小时" value="小时" />
                  </el-select>
                  <span>前提醒患者</span>
                  <el-time-picker
                    v-model="advanceRemind.time"
                    format="HH:mm"
                    value-format="HH:mm"
                    size="small"
                    style="width: 120px; margin: 0 10px;"
                  />
                  <el-button type="primary" size="small">发送</el-button>
                </div>
              </div>

              <!-- 超时提醒 -->
              <div class="remind-section">
                <div class="remind-header">
                  <el-checkbox v-model="overtimeRemind.enabled">超时提醒</el-checkbox>
                  <span class="remind-desc">
                    已过随访时间，用户仍然未完成随访任务，提醒用户进行随访任务，用户完成随访任务的当天，将立即结束提醒
                  </span>
                </div>
                <div v-if="overtimeRemind.enabled" class="remind-config">
                  <span>超过计划随访的</span>
                  <el-input-number v-model="overtimeRemind.days" :min="1" size="small" style="width: 80px; margin: 0 5px;" />
                  <el-select v-model="overtimeRemind.unit" size="small" style="width: 80px; margin: 0 5px;">
                    <el-option label="天" value="天" />
                    <el-option label="周" value="周" />
                    <el-option label="小时" value="小时" />
                  </el-select>
                  <span>后提醒患者</span>
                  <el-input-number v-model="overtimeRemind.times" :min="1" size="small" style="width: 80px; margin: 0 10px;" />
                  <span>次</span>
                  <el-time-picker
                    v-model="overtimeRemind.time"
                    format="HH:mm"
                    value-format="HH:mm"
                    size="small"
                    style="width: 120px; margin: 0 10px;"
                  />
                  <el-button type="primary" size="small">发送</el-button>
                </div>
              </div>
            </div>
          </el-dialog>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { createScheme, savePrescription, saveAssessment, updatePatientDiseaseType } from '@/api/medical-scheme'
import { createFollowupPlan, createFollowupProject, getProjectCountByTemplate, enrollPatient } from '@/api/followup'

export default {
  name: 'SchemeConfigPatient',
  data() {
    return {
      activeTab: 'basic',
      basicCompleted: false,
      isCycleModified: false,
      submitLoading: false,
      prescriptionSaving: false,
      schemeId: null, // 保存后返回的方案ID

      // 患者信息
      patientInfo: {
        id: null,
        name: '',
        nickname: '',
        gender: '',
        height: null,
        birthDate: '',
        ethnicity: '',
        occupation: '',
        idCardType: '',
        idCard: '',
        phone: '',
        contactAddress: '',
        detailAddress: '',
        medicalRecordNo: '',
        enrollmentInstitution: '',
        diagnosis: '',
        diseaseType: '',
        remarks: ''
      },

      // 基本信息表单
      basicForm: {
        schemeCycle: null
      },

      // 验证规则
      basicRules: {
        schemeCycle: [
          { required: true, message: '请选择方案周期', trigger: 'change' }
        ]
      },

      // 运动项目列表
      exerciseList: [
        {
          exerciseScene: '',
          exerciseType: '',
          exerciseMethod: '',
          precautions: '',
          durationMinutes: null,
          exerciseFrequency: '1次/周',
          exerciseIntensity: '中等',
          cycleWeeks: 1,
          totalSessions: 1,
          weeklyTotalMinutes: 0,
          progression: '',
          remarks: '',
          exerciseImage: ''
        }
      ],

      // 评定计划相关
      assessmentEnabled: false,
      assessmentSaving: false,
      assessmentList: [
        {
          executionType: '机构',
          scaleName: '',
          executionFrequency: '1次/周',
          cycleDuration: 0,
          totalTimes: 1,
          remarks: ''
        }
      ],

      // 随访管理相关
      followupEnabled: false,
      followupPlanName: '',
      followupData: {
        applicationTime: '',
        joinProjectTime: '',
        source: '',
        primaryDoctor: '',
        healthManager: ''
      },
      operationLogs: [],

      // 修改对话框控制
      diseaseTypeDialogVisible: false,
      editDiseaseType: '',
      primaryDoctorDialogVisible: false,
      editPrimaryDoctor: '',
      healthManagerDialogVisible: false,
      editHealthManager: '',

      // 开始随访对话框
      startFollowupDialogVisible: false,
      followupStep: 0, // 当前步骤：0-选择开始日期，1-选择计划模板
      followupStartDate: '',
      followupTemplate: '',
      followupTemplateList: [],
      createdPlanId: null, // 存储已创建的计划ID

      // 随访计划展示
      showFollowupPlan: false,
      followupPlanProgress: 0, // 随访计划进度
      followupTasks: [], // 随访任务列表

      // 提醒患者对话框
      remindPatientDialogVisible: false,
      remindTime: '',
      currentRemindTask: null,

      // 中止确认对话框
      terminateConfirmVisible: false,
      currentTerminateIndex: null,

      // 发起临时事件对话框
      temporaryEventDialogVisible: false,
      temporaryEventForm: {
        notifyType: '',
        eventName: '',
        eventType: ''
      },

      // 编辑计划对话框
      editPlanDialogVisible: false,
      planForm: {
        planName: '',
        createTime: '',
        lastModifyTime: '',
        status: '启用',
        version: '1.0',
        visibleTeam: [],
        description: '',
        followupTasks: []
      },
      // 提前提醒设置
      advanceRemind: {
        enabled: true,
        days: 1,
        unit: '天',
        time: '09:00'
      },
      // 超时提醒设置
      overtimeRemind: {
        enabled: true,
        days: 1,
        unit: '天',
        times: 1,
        time: '09:00'
      }
    }
  },
  computed: {},
  mounted() {
    // 从路由参数获取患者信息
    const patientData = this.$route.query.patientData
    if (patientData) {
      try {
        this.patientInfo = JSON.parse(patientData)
        // 初始化随访模板列表
        this.initFollowupTemplates()
      } catch (error) {
        console.error('解析患者信息失败:', error)
        this.$message.error('获取患者信息失败')
        this.$router.back()
      }
    } else {
      this.$message.error('缺少患者信息')
      this.$router.back()
    }
  },
  methods: {
    /** 计算年龄 */
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

    /** 计算患者年龄（用于随访管理） */
    calculatePatientAge() {
      return this.calculateAge(this.patientInfo.birthDate)
    },

    /** 格式化日期 */
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hours = String(d.getHours()).padStart(2, '0')
      const minutes = String(d.getMinutes()).padStart(2, '0')
      const seconds = String(d.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },

    /** 标签页切换 */
    async handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)

      // 当切换到随访管理标签时，自动创建入组记录
      if (tab.name === 'followup' && this.patientInfo.id) {
        try {
          const enrollmentData = {
            patientId: this.patientInfo.id,
            followupTeam: '西南医科大学附属医院',
            followupStatus: '待入组'
          }

          console.log('准备调用入组接口，患者ID:', this.patientInfo.id)
          const response = await enrollPatient(enrollmentData)
          console.log('入组接口返回:', response)

          if (response && response.code === 200) {
            console.log('自动入组成功，入组记录ID:', response.data)
            // 提示用户可以在随访列表查看
            this.$message.success('已自动加入随访管理，可在随访列表中查看')
          }
        } catch (error) {
          console.error('自动入组失败:', error)
          console.error('错误详情:', error.response || error.message)
          // 如果是网络错误或500错误，提示用户
          if (error.response && error.response.status >= 500) {
            this.$message.warning('随访管理服务暂时不可用，请稍后重试')
          }
        }
      }
    },

    /** 自动入组患者到随访管理 */
    async autoEnrollPatient() {
      if (!this.patientInfo.id) {
        console.error('患者ID不存在，无法入组')
        return
      }

      try {
        const enrollmentData = {
          patientId: this.patientInfo.id,
          followupTeam: '西南医科大学附属医院',
          followupStatus: '待入组'
        }

        console.log('=== 前端发送数据 ===')
        console.log('patientInfo对象:', this.patientInfo)
        console.log('患者ID:', this.patientInfo.id)
        console.log('患者ID类型:', typeof this.patientInfo.id)
        console.log('入组数据:', JSON.stringify(enrollmentData))

        const response = await enrollPatient(enrollmentData)
        console.log('自动入组：入组接口返回:', response)

        if (response && response.code === 200) {
          console.log('自动入组成功，入组记录ID:', response.data)
          this.$message.success('已自动加入随访管理')
        }
      } catch (error) {
        console.error('自动入组失败:', error)
        console.error('错误详情:', error.response || error.message)
        // 静默处理错误，不影响用户体验
      }
    },

    /** 方案周期变化 */
    handleCycleChange(value) {
      this.isCycleModified = true
      console.log('选择的方案周期:', value)
    },

    /** 查看健康档案 */
    handleViewHealthRecord() {
      this.$router.push({
        path: '/patient/health-record',
        query: {
          patientId: this.patientInfo.id,
          from: 'scheme-config'
        }
      })
    },

    /** 查看专病档案 */
    handleViewDiseaseRecord() {
      this.$router.push({
        path: '/patient/disease-record',
        query: {
          patientId: this.patientInfo.id,
          from: 'scheme-config'
        }
      })
    },

    /** 查看入组评估 */
    handleViewEnrollmentAssessment() {
      this.$router.push({
        path: '/patient/enrollment-assessment',
        query: {
          patientId: this.patientInfo.id,
          from: 'scheme-config'
        }
      })
    },

    /** 保存方案 */
    handleSave() {
      this.$refs.basicForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true

          const schemeData = {
            patientId: this.patientInfo.id,
            medicalRecordNo: this.patientInfo.medicalRecordNo,
            schemeCycle: this.basicForm.schemeCycle,
            diseaseType: this.patientInfo.diseaseType
          }

          console.log('提交的方案数据:', schemeData)

          // 调用后端API保存方案
          createScheme(schemeData)
            .then(response => {
              this.$message.success('方案创建成功')
              this.submitLoading = false
              this.basicCompleted = true
              this.isCycleModified = false

              // 保存方案ID
              this.schemeId = response.data

              // 自动跳转到运动处方标签页
              this.activeTab = 'prescription'
            })
            .catch(error => {
              console.error('保存方案失败:', error)
              this.$message.error('保存失败，请稍后重试')
              this.submitLoading = false
            })
        } else {
          this.$message.error('请完善必填项')
          return false
        }
      })
    },

    /** 添加运动项目 */
    handleAddExercise() {
      this.exerciseList.push({
        exerciseScene: '',
        exerciseType: '',
        exerciseMethod: '',
        precautions: '',
        durationMinutes: null,
        exerciseFrequency: '1次/周',
        exerciseIntensity: '中等',
        cycleWeeks: 1,
        totalSessions: 1,
        weeklyTotalMinutes: 0,
        progression: '',
        remarks: '',
        exerciseImage: ''
      })
    },

    /** 删除单个运动项目 */
    handleDeleteSingleExercise(index) {
      this.$confirm('确认删除该运动项目吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.exerciseList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /** 保存运动处方 */
    handleSavePrescription() {
      // 获取选中的行
      const selectedRows = this.$refs.prescriptionTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的运动项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.exerciseScene || !item.exerciseType || !item.exerciseMethod ||
        !item.durationMinutes || !item.exerciseFrequency || !item.exerciseIntensity ||
        !item.cycleWeeks || !item.totalSessions
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中运动项目的必填信息')
        return
      }

      this.prescriptionSaving = true

      const prescriptionData = {
        schemeId: this.schemeId,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        exerciseList: selectedRows.map(item => ({
          exerciseScene: item.exerciseScene,
          exerciseType: item.exerciseType,
          exerciseMethod: item.exerciseMethod,
          precautions: item.precautions,
          durationMinutes: item.durationMinutes,
          exerciseFrequency: item.exerciseFrequency,
          exerciseIntensity: item.exerciseIntensity,
          cycleWeeks: item.cycleWeeks,
          totalSessions: item.totalSessions,
          weeklyTotalMinutes: item.weeklyTotalMinutes,
          progression: item.progression,
          remarks: item.remarks,
          exerciseImage: item.exerciseImage
        }))
      }

      console.log('提交的运动处方数据:', prescriptionData)

      // 调用后端API保存运动处方
      savePrescription(prescriptionData)
        .then(() => {
          this.$message.success('运动处方保存成功')
          this.prescriptionSaving = false
        })
        .catch(error => {
          console.error('保存运动处方失败:', error)
          this.$message.error('保存失败，请稍后重试')
          this.prescriptionSaving = false
        })
    },

    /** 提交运动处方 */
    handleSubmitPrescription() {
      // 获取选中的行
      const selectedRows = this.$refs.prescriptionTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的运动项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.exerciseScene || !item.exerciseType || !item.exerciseMethod ||
        !item.durationMinutes || !item.exerciseFrequency || !item.exerciseIntensity ||
        !item.cycleWeeks || !item.totalSessions
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中运动项目的必填信息')
        return
      }

      this.prescriptionSaving = true

      const prescriptionData = {
        schemeId: this.schemeId,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        exerciseList: selectedRows.map(item => ({
          exerciseScene: item.exerciseScene,
          exerciseType: item.exerciseType,
          exerciseMethod: item.exerciseMethod,
          precautions: item.precautions,
          durationMinutes: item.durationMinutes,
          exerciseFrequency: item.exerciseFrequency,
          exerciseIntensity: item.exerciseIntensity,
          cycleWeeks: item.cycleWeeks,
          totalSessions: item.totalSessions,
          weeklyTotalMinutes: item.weeklyTotalMinutes,
          progression: item.progression,
          remarks: item.remarks,
          exerciseImage: item.exerciseImage
        }))
      }

      console.log('提交的运动处方数据:', prescriptionData)

      // 调用后端API保存运动处方
      savePrescription(prescriptionData)
        .then(() => {
          this.$message.success('运动处方提交成功')
          this.prescriptionSaving = false
          // 启用评定计划标签页并跳转
          this.assessmentEnabled = true
          this.activeTab = 'assessment'
        })
        .catch(error => {
          console.error('提交运动处方失败:', error)
          this.$message.error('提交失败，请稍后重试')
          this.prescriptionSaving = false
        })
    },

    /** 添加评定项目 */
    handleAddAssessment() {
      this.assessmentList.push({
        executionType: '机构',
        scaleName: '',
        executionFrequency: '1次/周',
        cycleDuration: 0,
        totalTimes: 1,
        remarks: ''
      })
    },

    /** 预览评定量表 */
    handlePreviewAssessment(index) {
      const assessment = this.assessmentList[index]
      if (!assessment.scaleName) {
        this.$message.warning('请先选择量表名称')
        return
      }
      this.$message.info(`预览量表: ${assessment.scaleName}`)
      // TODO: 实现量表预览功能
    },

    /** 删除单个评定项目 */
    handleDeleteSingleAssessment(index) {
      this.$confirm('确认删除该评定项目吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.assessmentList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /** 保存评定计划 */
    handleSaveAssessment() {
      // 获取选中的行
      const selectedRows = this.$refs.assessmentTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的评定项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.executionType || !item.scaleName || !item.executionFrequency
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中评定项目的必填信息')
        return
      }

      this.assessmentSaving = true

      const assessmentData = {
        schemeId: this.schemeId,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        assessmentList: selectedRows.map(item => ({
          executionType: item.executionType,
          scaleName: item.scaleName,
          executionFrequency: item.executionFrequency,
          cycleDuration: item.cycleDuration,
          totalTimes: item.totalTimes,
          remarks: item.remarks
        }))
      }

      console.log('提交的评定计划数据:', assessmentData)

      // 调用后端API保存评定计划
      saveAssessment(assessmentData)
        .then(() => {
          this.$message.success('评定计划保存成功')
          this.assessmentSaving = false
        })
        .catch(error => {
          console.error('保存评定计划失败:', error)
          this.$message.error('保存失败，请稍后重试')
          this.assessmentSaving = false
        })
    },

    /** 提交评定计划 */
    handleSubmitAssessment() {
      // 获取选中的行
      const selectedRows = this.$refs.assessmentTable.selection

      // 验证是否至少选中一条记录
      if (!selectedRows || selectedRows.length === 0) {
        this.$message.warning('请至少选中一条记录')
        return
      }

      // 验证选中的评定项目是否填写完整
      const incompleteItems = selectedRows.filter(item =>
        !item.executionType || !item.scaleName || !item.executionFrequency
      )

      if (incompleteItems.length > 0) {
        this.$message.warning('请完善所有选中评定项目的必填信息')
        return
      }

      this.assessmentSaving = true

      const assessmentData = {
        schemeId: this.schemeId,
        patientId: this.patientInfo.id,
        diseaseType: this.patientInfo.diseaseType,
        assessmentList: selectedRows.map(item => ({
          executionType: item.executionType,
          scaleName: item.scaleName,
          executionFrequency: item.executionFrequency,
          cycleDuration: item.cycleDuration,
          totalTimes: item.totalTimes,
          remarks: item.remarks
        }))
      }

      console.log('提交的评定计划数据:', assessmentData)

      // 调用后端API保存评定计划
      saveAssessment(assessmentData)
        .then(() => {
          this.$message.success('评定计划提交成功')
          this.assessmentSaving = false

          // 初始化随访数据（计划名称为空，等待后续添加）
          this.followupPlanName = ''
          this.followupData.applicationTime = this.formatDate(new Date())
          this.followupData.joinProjectTime = this.formatDate(new Date())
          this.followupData.source = '医生推荐'

          // 启用随访管理标签页并跳转
          this.followupEnabled = true
          this.activeTab = 'followup'

          // 自动创建随访入组记录
          this.autoEnrollPatient()
        })
        .catch(error => {
          console.error('提交评定计划失败:', error)
          this.$message.error('提交失败，请稍后重试')
          this.assessmentSaving = false
        })
    },

    /** 初始化随访模板列表 */
    initFollowupTemplates() {
      // 根据患者的病种和入组机构生成模板列表
      const institution = this.patientInfo.enrollmentInstitution || '默认机构'
      const diseaseType = this.patientInfo.diseaseType || '疾病'

      this.followupTemplateList = [
        {
          label: `(${institution})${diseaseType}患者运动与健康随访计划`,
          value: `${institution}_${diseaseType}_运动与健康随访计划`
        },
        {
          label: `(华阳街道龙腾社区卫生服务中心)骨关节炎患者运动与健康随访计划`,
          value: '华阳街道龙腾社区卫生服务中心_骨关节炎_运动与健康随访计划'
        },
        {
          label: `(江安县中医医院)骨关节炎患者运动与健康随访计划`,
          value: '江安县中医医院_骨关节炎_运动与健康随访计划'
        }
      ]
    },

    /** 开始随访 */
    handleStartFollowup() {
      // 重置对话框状态
      this.followupStep = 0
      this.followupStartDate = ''
      this.followupTemplate = ''
      this.createdPlanId = null
      // 打开对话框
      this.startFollowupDialogVisible = true
    },

    /** 下一步 */
    async handleNextStep() {
      if (this.followupStep === 0) {
        // 验证是否选择了开始日期
        if (!this.followupStartDate) {
          this.$message.warning('请选择开始日期')
          return
        }
        // 进入下一步
        this.followupStep = 1
      } else if (this.followupStep === 1) {
        // 验证是否选择了模板
        if (!this.followupTemplate) {
          this.$message.warning('请选择计划模板')
          return
        }

        // 验证是否已创建计划
        if (!this.createdPlanId) {
          this.$message.warning('请先选择计划模板')
          return
        }

        try {
          // 获取模板信息
          const templateInfo = this.followupTemplateList.find(t => t.value === this.followupTemplate)
          const templateName = templateInfo ? templateInfo.label : this.followupTemplate

          // 1. 获取该模板已有的项目数量，计算递增数字
          const countResult = await getProjectCountByTemplate(templateName)
          const count = countResult.data || 0
          const nextNumber = count + 1

          // 2. 生成随机项目编号
          const projectCode = 'PRJ-' + Date.now() + '-' + Math.random().toString(36).substring(2, 11).toUpperCase()

          // 3. 创建随访项目
          const projectData = {
            projectCode: projectCode,
            projectName: `具体·${templateName}+${nextNumber}`,
            projectDescription: '-',
            linkedFollowupPlan: '-',
            isPublished: 1,
            operator: '系统'
          }

          await createFollowupProject(projectData)

          this.$message.success('随访项目创建成功')

          // 更新计划名称
          this.followupPlanName = projectData.projectName

          // 初始化随访任务列表
          this.initFollowupTasks()

          // 关闭对话框，显示随访计划展示区
          this.startFollowupDialogVisible = false
          this.showFollowupPlan = true
        } catch (error) {
          console.error('创建随访项目失败:', error)
          this.$message.error('创建失败，请重试')
        }
      }
    },

    /** 模板选择改变时，自动创建随访计划 */
    async handleTemplateChange(templateValue) {
      if (!templateValue) {
        return
      }

      try {
        // 获取模板信息
        const templateInfo = this.followupTemplateList.find(t => t.value === templateValue)
        const templateName = templateInfo ? templateInfo.label : templateValue

        // 创建随访计划
        const planData = {
          planName: `具体·${templateName}`,
          templateName: templateName,
          linkedProject: this.patientInfo.name || '-',
          versionNumber: '1.0',
          status: '未开始',
          versionRemark: '-'
        }

        const planResult = await createFollowupPlan(planData)
        this.createdPlanId = planResult.data

        this.$message.success('随访计划创建成功')
      } catch (error) {
        console.error('创建随访计划失败:', error)
        this.$message.error('创建随访计划失败，请重试')
        // 清空选择，允许重新选择
        this.followupTemplate = ''
        this.createdPlanId = null
      }
    },

    /** 上一步 */
    handlePreviousStep() {
      if (this.followupStep > 0) {
        this.followupStep--
      }
    },

    /** 确认开始随访 */
    handleConfirmFollowup() {
      // 验证是否选择了模板
      if (!this.followupTemplate) {
        this.$message.warning('请选择计划模板')
        return
      }

      // 提交随访计划
      const followupData = {
        patientId: this.patientInfo.id,
        startDate: this.followupStartDate,
        template: this.followupTemplate,
        schemeId: this.schemeId
      }

      console.log('提交随访计划数据:', followupData)

      // TODO: 调用后端API保存随访计划
      // 暂时先模拟成功
      this.$message.success('随访计划创建成功')
      this.startFollowupDialogVisible = false

      // 更新计划名称
      const templateInfo = this.followupTemplateList.find(t => t.value === this.followupTemplate)
      if (templateInfo) {
        this.followupPlanName = templateInfo.label
      }

      // 初始化随访任务列表
      this.initFollowupTasks()

      // 显示随访计划展示区
      this.showFollowupPlan = true
    },

    /** 初始化随访任务列表 */
    initFollowupTasks() {
      const templateInfo = this.followupTemplateList.find(t => t.value === this.followupTemplate)
      const templateName = templateInfo ? templateInfo.label : ''

      this.followupTasks = [
        {
          date: this.followupStartDate,
          status: 'pending',
          statusText: '待执行(0/1)',
          title: `第二次随访（不要通知提醒）${templateName}`,
          completed: 0,
          total: 1
        },
        {
          date: this.followupStartDate,
          status: 'pending',
          statusText: '待执行(0/1)',
          title: `入组随访（不要通知提醒）${templateName}`,
          completed: 0,
          total: 1
        }
      ]

      // 初始化进度为0%
      this.followupPlanProgress = 0
    },

    /** 去填写 */
    handleGoFill(task) {
      this.$message.info('跳转到填写页面：' + task.title)
      // TODO: 实现跳转到填写页面
    },

    /** 提醒患者 */
    handleRemindPatient(task) {
      this.currentRemindTask = task
      this.remindTime = ''
      this.remindPatientDialogVisible = true
    },

    /** 确认提醒患者 */
    handleConfirmRemind() {
      if (!this.remindTime) {
        this.$message.warning('请选择提醒时间')
        return
      }

      // TODO: 调用后端API保存提醒时间并发送微信推送
      console.log('提醒时间:', this.remindTime)
      console.log('任务:', this.currentRemindTask)

      this.$message.success('提醒设置成功，将在指定时间向患者发送微信推送')
      this.remindPatientDialogVisible = false
    },

    /** 发起临时变更 */
    handleTemporaryChange(task) {
      this.$message.info('发起临时变更功能开发中...')
      // TODO: 实现临时变更功能
    },

    /** 中止任务 */
    handleTerminateTask(index) {
      this.currentTerminateIndex = index
      this.terminateConfirmVisible = true
    },

    /** 确认中止任务 */
    handleConfirmTerminate() {
      // TODO: 调用后端API中止任务
      console.log('中止任务索引:', this.currentTerminateIndex)

      this.$message.success('任务已中止，该事件对该患者已失效')
      this.terminateConfirmVisible = false

      // 可选：移除已中止的任务
      // this.followupTasks.splice(this.currentTerminateIndex, 1)
    },

    /** 发起临时事件 */
    handleTemporaryEvent(task) {
      // 重置表单
      this.temporaryEventForm = {
        notifyType: '',
        eventName: '',
        eventType: ''
      }
      this.temporaryEventDialogVisible = true
    },

    /** 确认发起临时事件 */
    handleConfirmTemporaryEvent() {
      // 验证表单
      if (!this.temporaryEventForm.notifyType) {
        this.$message.warning('请选择通知类型')
        return
      }
      if (!this.temporaryEventForm.eventName || this.temporaryEventForm.eventName.trim() === '') {
        this.$message.warning('请输入事件名称')
        return
      }
      if (!this.temporaryEventForm.eventType) {
        this.$message.warning('请选择事件类型')
        return
      }

      // TODO: 调用后端API提交临时事件
      console.log('临时事件数据:', this.temporaryEventForm)

      this.$message.success('临时事件创建成功')
      this.temporaryEventDialogVisible = false
    },

    /** 关闭随访对话框 */
    handleCloseFollowupDialog() {
      // 重置状态
      this.followupStep = 0
      this.followupStartDate = ''
      this.followupTemplate = ''
      this.createdPlanId = null
    },

    /** 编辑当前计划 */
    handleEditPlan() {
      // 判断是否有随访计划
      if (!this.followupPlanName || this.followupPlanName.trim() === '') {
        this.$message.error('当前没有随访计划')
        return
      }

      // 初始化计划表单数据
      this.planForm = {
        planName: this.followupPlanName,
        createTime: this.formatDate(new Date()),
        lastModifyTime: this.formatDate(new Date()),
        status: '启用',
        version: '1.0',
        visibleTeam: [],
        description: '',
        followupTasks: []
      }

      // 打开编辑对话框
      this.editPlanDialogVisible = true
    },

    /** 取消编辑计划 */
    handleCancelEditPlan() {
      this.editPlanDialogVisible = false
    },

    /** 新增随访任务 */
    handleAddFollowupTask() {
      const orderNum = this.planForm.followupTasks.length + 1
      this.planForm.followupTasks.push({
        orderNum: orderNum,
        intervalDays: 10,
        intervalUnit: '天'
      })
    },

    /** 编辑任务名称 */
    handleEditTaskName(index) {
      this.$prompt('请输入新的随访次数', '编辑随访名称', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\d+$/,
        inputErrorMessage: '请输入数字'
      }).then(({ value }) => {
        this.planForm.followupTasks[index].orderNum = parseInt(value)
        this.$message.success('修改成功')
      }).catch(() => {})
    },

    /** 删除任务 */
    handleDeleteTask(index) {
      this.$confirm('确认删除该随访任务吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.planForm.followupTasks.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    /** 新增随访事件 */
    handleAddFollowupEvent(index) {
      this.$message.info('新增随访事件功能开发中...')
      // TODO: 实现新增随访事件功能
    },

    /** 修改病种 */
    handleEditDiseaseType() {
      this.editDiseaseType = this.patientInfo.diseaseType
      this.diseaseTypeDialogVisible = true
    },

    /** 保存病种 */
    handleSaveDiseaseType() {
      if (!this.editDiseaseType) {
        this.$message.warning('请选择病种')
        return
      }

      // 调用后端API更新患者病种
      updatePatientDiseaseType({
        patientId: this.patientInfo.id,
        diseaseType: this.editDiseaseType
      })
        .then(() => {
          // 更新前端数据
          this.patientInfo.diseaseType = this.editDiseaseType
          this.diseaseTypeDialogVisible = false
          this.$message.success('病种修改成功')
        })
        .catch(error => {
          console.error('修改病种失败:', error)
          this.$message.error('修改失败，请稍后重试')
        })
    },

    /** 修改主负责医生 */
    handleEditPrimaryDoctor() {
      this.editPrimaryDoctor = this.followupData.primaryDoctor
      this.primaryDoctorDialogVisible = true
    },

    /** 保存主负责医生 */
    handleSavePrimaryDoctor() {
      if (!this.editPrimaryDoctor || this.editPrimaryDoctor.trim() === '') {
        this.$message.warning('请输入医生姓名')
        return
      }

      // TODO: 调用后端API更新主负责医生
      // 更新前端数据
      this.followupData.primaryDoctor = this.editPrimaryDoctor.trim()
      this.primaryDoctorDialogVisible = false
      this.$message.success('主负责医生修改成功')

      // 这里应该调用API更新数据库
      // updatePrimaryDoctor({ patientId: this.patientInfo.id, primaryDoctor: this.editPrimaryDoctor })
    },

    /** 修改健管师 */
    handleEditHealthManager() {
      this.editHealthManager = this.followupData.healthManager
      this.healthManagerDialogVisible = true
    },

    /** 保存健管师 */
    handleSaveHealthManager() {
      if (!this.editHealthManager || this.editHealthManager.trim() === '') {
        this.$message.warning('请输入健管师姓名')
        return
      }

      // TODO: 调用后端API更新健管师
      // 更新前端数据
      this.followupData.healthManager = this.editHealthManager.trim()
      this.healthManagerDialogVisible = false
      this.$message.success('健管师修改成功')

      // 这里应该调用API更新数据库
      // updateHealthManager({ patientId: this.patientInfo.id, healthManager: this.editHealthManager })
    },

    /** 返回列表 */
    handleBackToList() {
      this.$router.push('/scheme/list')
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;

  .patient-info-card {
    border: 1px solid #ebeef5;
    box-shadow: none;

    ::v-deep .el-card__body {
      padding: 0;
    }

    // 表格样式 - 无边框
    ::v-deep .el-table {
      // 去除所有边框
      border: none;

      &::before {
        display: none; // 去除底部边框
      }

      th, td {
        border: none; // 去除单元格边框
      }

      // 表头样式
      th {
        background-color: transparent;
        color: rgb(144, 126, 179);
        font-weight: 500;
        padding: 16px 20px;
        white-space: nowrap;
        text-align: center !important; // 强制居中
      }

      // 表头内容容器
      .cell {
        padding: 0;
        text-align: center;
      }

      // 内容样式
      td {
        padding: 16px 20px;
        color: #303133;
        white-space: nowrap;
        text-align: center !important; // 强制居中

        .cell {
          padding: 0;
          text-align: center;
        }
      }

      // 表格布局自动
      table-layout: fixed;
    }
  }

  ::v-deep .el-tabs__item {
    font-size: 16px;
    font-weight: 500;
    color: rgb(144, 126, 179);
  }

  ::v-deep .el-tabs__item.is-active {
    color: rgb(144, 126, 179);
  }

  ::v-deep .el-tabs__active-bar {
    background-color: rgb(144, 126, 179);
  }

  ::v-deep .el-tabs__item.is-disabled {
    color: #c0c4cc;
    cursor: not-allowed;
  }

  ::v-deep .el-descriptions {
    .el-descriptions__label {
      font-weight: 500;
      color: #606266;
    }
  }

  // 档案按钮样式
  .archive-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);
    color: #fff;
    padding: 7px 20px;

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

  // 保存按钮样式
  .save-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);
    color: #fff;
    padding: 10px 30px;
    font-size: 14px;

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

    &.is-disabled {
      background-color: #d1c4dc;
      border-color: #d1c4dc;
      color: #fff;
      cursor: not-allowed;
    }
  }

  // 运动处方按钮样式
  .prescription-header {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .prescription-action-btn {
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

  .save-action-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);

    &:hover,
    &:focus {
      background-color: rgb(134, 116, 169);
      border-color: rgb(134, 116, 169);
    }
  }

  // 运动处方表格样式
  .prescription-table {
    ::v-deep .el-table__header {
      th {
        background-color: rgb(250, 250, 250);
        color: rgb(38, 38, 38);
        font-weight: 500;
        padding: 12px 0;
      }
    }

    ::v-deep .el-table__body {
      td {
        padding: 8px 0;
      }

      .cell {
        padding: 0 8px;
      }
    }

    ::v-deep .el-input__inner,
    ::v-deep .el-input-number__input {
      text-align: center;
    }

    // 横向滚动条样式
    ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
      height: 12px;
    }

    ::v-deep .el-table__body-wrapper::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 6px;
    }

    ::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 6px;

      &:hover {
        background: #a8a8a8;
      }
    }

    // 固定列阴影
    ::v-deep .el-table__fixed-right {
      box-shadow: -3px 0 10px rgba(0, 0, 0, 0.1);
    }
  }

  .delete-btn {
    color: rgb(144, 126, 179);

    &:hover {
      color: rgb(134, 116, 169);
    }
  }

  // 评定计划样式
  .assessment-header {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .assessment-action-btn {
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

  // 评定计划表格样式
  .assessment-table {
    ::v-deep .el-table__header {
      th {
        background-color: rgb(250, 250, 250);
        color: rgb(38, 38, 38);
        font-weight: 500;
        padding: 12px 0;
      }
    }

    ::v-deep .el-table__body {
      td {
        padding: 8px 0;
      }

      .cell {
        padding: 0 8px;
      }
    }

    ::v-deep .el-input__inner,
    ::v-deep .el-input-number__input {
      text-align: center;
    }

    // 横向滚动条样式
    ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
      height: 12px;
    }

    ::v-deep .el-table__body-wrapper::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 6px;
    }

    ::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 6px;

      &:hover {
        background: #a8a8a8;
      }
    }

    // 固定列阴影
    ::v-deep .el-table__fixed-right {
      box-shadow: -3px 0 10px rgba(0, 0, 0, 0.1);
    }
  }

  .preview-btn {
    color: rgb(144, 126, 179);

    &:hover {
      color: rgb(134, 116, 169);
    }
  }

  .assessment-pagination {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 20px;
    gap: 20px;

    .pagination-info {
      font-size: 14px;
      color: #606266;
    }

    ::v-deep .el-pagination {
      .el-pager li.active {
        color: rgb(144, 126, 179);
      }

      .el-pager li:hover {
        color: rgb(144, 126, 179);
      }
    }
  }

  // 随访管理样式
  .followup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 1px solid #e4e7ed;

    .plan-name {
      .label {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }

      .value {
        font-size: 16px;
        color: #606266;
        margin-left: 10px;
      }
    }
  }

  .patient-info-section {
    background-color: #f9fafb;
    padding: 20px;
    border-radius: 4px;
    border: 1px solid #e4e7ed;

    .info-row {
      display: flex;
      justify-content: flex-start;
      margin-bottom: 20px;

      &:last-child {
        margin-bottom: 0;
      }

      .info-item {
        flex: 1;
        display: flex;
        align-items: center;
        min-width: 0;

        .info-label {
          font-size: 19px;
          color: rgb(24, 28, 24);
          white-space: nowrap;
          font-weight: 500;
        }

        .info-value {
          font-size: 19px;
          color: rgb(24, 28, 24);
          margin-left: 5px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .edit-link {
          font-size: 19px;
          color: #409eff;
          margin-left: 8px;
          text-decoration: none;
          white-space: nowrap;
          cursor: pointer;

          &:hover {
            color: #66b1ff;
            text-decoration: underline;
          }
        }
      }
    }
  }

  .followup-action {
    margin: 20px 0;
  }

  .operation-log {
    margin-top: 20px;
  }

  // 开始随访对话框样式
  .step-content {
    min-height: 150px;
    padding: 20px 0;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }

  // 随访计划展示区样式
  .followup-plan-content {
    margin: 20px 0;
  }

  .followup-progress {
    margin-bottom: 30px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .progress-text {
      font-size: 14px;
      color: #909399;
    }

    .progress-value {
      font-size: 14px;
      font-weight: bold;
      color: #303133;
      margin-left: 5px;
    }
  }

  .followup-timeline {
    margin-top: 20px;

    .timeline-item {
      display: flex;
      position: relative;
      margin-bottom: 30px;

      // 左侧时间轴区域
      .timeline-left {
        width: 200px;
        flex-shrink: 0;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        position: relative;

        .timeline-date {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
          margin-bottom: 5px;
        }

        .timeline-status {
          font-size: 13px;
          color: #909399;
          margin-bottom: 10px;
        }

        .timeline-dot {
          width: 12px;
          height: 12px;
          background-color: #409EFF;
          border-radius: 50%;
          margin-bottom: 10px;
        }

        .timeline-line {
          position: absolute;
          left: 6px;
          top: 80px;
          width: 2px;
          height: calc(100% + 30px);
          background-color: #e4e7ed;
        }

        .terminate-btn {
          background-color: #f56c6c;
          border-color: #f56c6c;
          color: #ffffff;

          &:hover {
            background-color: #f78989;
            border-color: #f78989;
            color: #ffffff;
          }
        }
      }

      // 中间分隔线
      .timeline-divider {
        width: 1px;
        background-color: #e4e7ed;
        margin: 0 20px;
      }

      // 右侧任务详情卡片
      .timeline-right {
        flex: 1;

        .task-card {
          .task-header {
            display: flex;
            flex-direction: column;
            gap: 10px;

            .task-title {
              font-size: 16px;
              font-weight: 500;
              color: #303133;
              margin: 0;
            }

            .task-actions-inline {
              display: flex;
              align-items: center;
              gap: 10px;

              .action-link {
                font-size: 14px;
                color: #409eff;
                text-decoration: none;
                cursor: pointer;

                &:hover {
                  color: #66b1ff;
                  text-decoration: underline;
                }

                &.danger {
                  color: #f56c6c;

                  &:hover {
                    color: #f78989;
                  }
                }
              }

              .divider {
                color: #dcdfe6;
              }
            }
          }

          .task-footer {
            display: flex;
            justify-content: flex-start;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #e4e7ed;

            ::v-deep .el-button--primary {
              background-color: #409eff;
              border-color: #409eff;
              color: #ffffff;

              &:hover {
                background-color: #66b1ff;
                border-color: #66b1ff;
              }
            }
          }
        }
      }
    }
  }

  // 编辑计划对话框样式
  ::v-deep .edit-plan-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #e4e7ed;

    .header-left {
      display: flex;
      align-items: center;
      gap: 20px;

      .back-link {
        font-size: 14px;
        color: #606266;
        text-decoration: none;
        display: flex;
        align-items: center;
        gap: 5px;

        &:hover {
          color: #409eff;
        }
      }

      .edit-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .cancel-btn {
      background-color: #f5f7fa;
      border-color: #dcdfe6;
      color: #606266;
    }
  }

  .edit-plan-content {
    padding: 30px;
    max-height: calc(100vh - 120px);
    overflow-y: auto;

    .basic-info-section {
      margin-bottom: 30px;
      padding: 20px;
      background-color: #f9fafb;
      border-radius: 4px;

      .info-row {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        margin-bottom: 15px;

        &:last-child {
          margin-bottom: 0;
        }

        .info-label {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
        }

        .info-value {
          font-size: 14px;
          color: #303133;
        }

        .info-item {
          display: flex;
          align-items: center;
          gap: 5px;
        }
      }
    }

    .section {
      margin-bottom: 25px;

      h3 {
        font-size: 15px;
        color: #303133;
        margin-bottom: 15px;
        font-weight: 500;
      }
    }

    .followup-task-item {
      margin-bottom: 20px;
      padding: 15px;
      background-color: #f5f7fa;
      border-radius: 4px;
      border-left: 3px solid #409eff;

      .task-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .task-title {
          font-size: 14px;
          color: #409eff;
          font-weight: 500;
        }

        .task-actions {
          display: flex;
          gap: 15px;

          i {
            font-size: 16px;
            color: #909399;
            cursor: pointer;

            &:hover {
              color: #409eff;
            }

            &.el-icon-delete:hover {
              color: #f56c6c;
            }
          }
        }
      }

      .task-interval {
        display: flex;
        align-items: center;
        gap: 5px;
      }
    }

    .tip-text {
      margin: 30px 0;
      padding: 12px;
      background-color: #fef0f0;
      border-left: 3px solid #f56c6c;
      font-size: 13px;
      color: #909399;
      line-height: 1.6;
    }

    .remind-section {
      margin-bottom: 25px;
      padding: 20px;
      background-color: #f9fafb;
      border-radius: 4px;

      .remind-header {
        margin-bottom: 15px;

        .remind-desc {
          font-size: 13px;
          color: #606266;
          margin-left: 10px;
        }
      }

      .remind-config {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        gap: 5px;
        padding-left: 30px;

        span {
          font-size: 14px;
          color: #606266;
        }
      }
    }
  }
}
</style>
