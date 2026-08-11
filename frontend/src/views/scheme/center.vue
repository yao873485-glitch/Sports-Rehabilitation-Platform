<template>
  <div class="scheme-center-container">
    <!-- 左侧患者列表卡片 -->
    <div class="patient-list-card">
      <!-- 顶部标签页 -->
      <div class="tabs-header">
        <div
          class="tab-item"
          :class="{ active: activeTab === 'ungrouped' }"
          @click="handleTabChange('ungrouped')"
        >
          未入组
        </div>
        <div
          class="tab-item"
          :class="{ active: activeTab === 'grouped' }"
          @click="handleTabChange('grouped')"
        >
          已入组
        </div>
      </div>

      <!-- 筛选区域 -->
      <div class="filter-section">
        <div class="filter-item">
          <label>时间日期：</label>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            size="small"
            class="date-picker"
          />
        </div>

        <div class="filter-item">
          <label>开始搜索：</label>
          <el-input
            v-model="searchKeyword"
            placeholder="请输入患者姓名或手机号码搜索"
            size="small"
            class="search-input"
            clearable
          />
        </div>

        <div class="filter-actions">
          <el-button type="primary" size="small" class="custom-primary-btn" @click="handleSearch">
            查询
          </el-button>
          <el-button size="small" @click="handleReset">
            重置
          </el-button>
        </div>
      </div>

      <!-- 数据表格 -->
      <div class="table-section">
        <div class="table-wrapper">
          <el-table
            :data="tableData"
            :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
            border
            style="width: 100%"
            @row-click="handleRowClick"
            highlight-current-row
            :row-class-name="getRowClassName"
          >
            <el-table-column
              :prop="activeTab === 'grouped' ? 'enrollmentDate' : 'createTime'"
              :label="activeTab === 'grouped' ? '入组日期' : '创建日期'"
              width="200"
              align="center"
              show-overflow-tooltip
            />
            <el-table-column
              prop="patientInfo"
              label="患者信息"
              width="250"
              align="center"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <div class="patient-info" style="white-space: nowrap;">
                  <span>{{ scope.row.patientName || scope.row.name }}</span>
                  <span class="gender-icon" :class="scope.row.gender === '男' ? 'male' : 'female'">
                    {{ scope.row.gender === '男' ? '♂' : '♀' }}
                  </span>
                  <span class="age">{{ scope.row.age }}岁</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="medicalRecordNo"
              label="档案号"
              width="200"
              align="center"
              show-overflow-tooltip
            />
          </el-table>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            small
          />
        </div>
      </div>

      <!-- 空状态提示 -->
      <div v-if="tableData.length === 0 && !loading" class="empty-state">
        <p>暂无数据</p>
      </div>
    </div>

    <!-- 右侧详情区域 -->
    <div class="detail-panel">
      <div v-if="!selectedPatient" class="empty-detail">
        <p>请选择患者查看详情</p>
      </div>
      <template v-else>
        <div
          v-if="activeTab === 'ungrouped'"
          class="patient-detail-container ungrouped-detail-container"
          v-loading="ungroupedDetailLoading"
        >
          <el-card class="ungrouped-tabs-card" shadow="never">
            <el-tabs v-model="ungroupedDetailTab">
              <el-tab-pane label="基本信息" name="basic">
                <div class="section-card">
                  <div class="section-title">基本信息</div>
                  <div class="section-grid">
                    <div class="section-item">
                      <span class="section-label">客户姓名：</span>
                      <span class="section-value">{{ basicInfoData.name || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">联系电话：</span>
                      <span class="section-value">{{ basicInfoData.phone || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">证件类型：</span>
                      <span class="section-value">{{ detailInfoData.idCardType || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">证件号码：</span>
                      <span class="section-value">{{ basicInfoData.idCard || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">性别：</span>
                      <span class="section-value">{{ formatGenderText(basicInfoData.gender) }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">出生年月：</span>
                      <span class="section-value">{{ formatDateDisplay(basicInfoData.birthDate) }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">民族：</span>
                      <span class="section-value">{{ detailInfoData.ethnicity || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">职业：</span>
                      <span class="section-value">{{ detailInfoData.occupation || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">联系地址：</span>
                      <span class="section-value">{{ detailInfoData.contactProvinceCityDistrict || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">详细地址：</span>
                      <span class="section-value">{{ detailInfoData.detailAddress || '-' }}</span>
                    </div>
                  </div>
                </div>
                <div class="section-card">
                  <div class="section-title">补充信息</div>
                  <div class="section-grid supplementary-grid">
                    <div class="section-item">
                      <span class="section-label">客户昵称：</span>
                      <span class="section-value">{{ detailInfoData.nickname || '-' }}</span>
                    </div>
                    <div class="section-item">
                      <span class="section-label">身高：</span>
                      <span class="section-value">
                        {{ detailInfoData.heightCm ? detailInfoData.heightCm + ' cm' : '-' }}
                      </span>
                    </div>
                  </div>
                  <div class="textarea-row">
                    <span class="section-label">备注说明：</span>
                    <div class="section-value remark-text">{{ detailInfoData.remark || '暂无备注' }}</div>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="入组信息" name="enrollment">
                <div class="section-card">
                  <div class="section-title">入组信息</div>
                  <div class="enrollment-form">
                    <div class="form-row">
                      <label>入组编号（档案号）：</label>
                      <el-input :value="basicInfoData.medicalRecordNo || ''" disabled />
                    </div>
                    <div class="form-row">
                      <label>诊断：</label>
                      <el-input :value="detailInfoData.diagnosis || ''" disabled />
                    </div>
                    <div class="form-row">
                      <label>病种类型：</label>
                      <el-input :value="basicInfoData.diseaseType || ''" disabled />
                    </div>
                    <div class="form-row action-row">
                      <label>健康档案：</label>
                      <div class="action-content">
                        <el-button
                          size="small"
                          class="view-btn"
                          :disabled="!dialogPatientId"
                          @click="handleViewHealthRecord"
                        >
                          查看
                        </el-button>
                        <span class="inline-status" :class="{ filled: !!healthRecordData }">
                          {{ healthRecordData ? '已填写' : '未填写' }}
                        </span>
                      </div>
                    </div>
                    <div class="form-row action-row">
                      <label>专病档案：</label>
                      <div class="action-content">
                        <el-button
                          size="small"
                          class="view-btn"
                          :disabled="!dialogPatientId"
                          @click="handleViewDiseaseRecord"
                        >
                          查看
                        </el-button>
                        <span class="inline-status" :class="{ filled: !!diseaseRecordData }">
                          {{ diseaseRecordData ? '已填写' : '未填写' }}
                        </span>
                      </div>
                    </div>
                    <div class="form-row action-row">
                      <label>入组评估：</label>
                      <div class="action-content">
                        <el-button
                          size="small"
                          class="view-btn"
                          :disabled="!dialogPatientId"
                          @click="handleViewEnrollmentAssessment"
                        >
                          查看
                        </el-button>
                        <span class="inline-status" :class="{ filled: !!enrollmentAssessmentData }">
                          {{ enrollmentAssessmentData ? '已填写' : '未填写' }}
                        </span>
                      </div>
                    </div>
                    <el-divider class="audit-divider" />
                    <div class="form-row radio-row audit-row">
                      <label>审核状态：</label>
                      <el-radio-group v-model="enrollmentAuditForm.status" @change="handleEnrollmentStatusChange">
                        <el-radio label="PASSED">审核通过</el-radio>
                        <el-radio label="REJECTED">审核拒绝</el-radio>
                      </el-radio-group>
                    </div>
                    <div v-if="enrollmentAuditForm.status === 'REJECTED'" class="form-row reject-row">
                      <label>拒绝原因：</label>
                      <div class="reject-input">
                        <el-input
                          v-model="enrollmentAuditForm.rejectReason"
                          type="textarea"
                          :rows="3"
                          maxlength="200"
                          show-word-limit
                          placeholder="请输入拒绝原因"
                        />
                      </div>
                    </div>
                    <div class="form-row audit-actions">
                      <div class="current-status">当前状态：{{ enrollmentStatusText }}</div>
                      <div class="audit-buttons">
                        <el-button
                          type="primary"
                          size="small"
                          :loading="savingEnrollmentAudit"
                          :disabled="!dialogPatientId"
                          @click="handleSaveEnrollmentAudit"
                        >
                          保存
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
        <div v-else class="patient-detail-container">
        <!-- 患者信息卡片 -->
        <el-card class="patient-info-card" shadow="never">
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">姓名</div>
              <div class="info-value">{{ selectedPatient.patientName }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">身份证</div>
              <div class="info-value">{{ selectedPatient.idCard || '-' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">档案号</div>
              <div class="info-value">{{ selectedPatient.medicalRecordNo }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">出生日期</div>
              <div class="info-value">{{ selectedPatient.birthDate || '-' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">诊断</div>
              <div class="info-value">{{ selectedPatient.diagnosis || '-' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">病种</div>
              <div class="info-value">{{ selectedPatient.diseaseType || '-' }}</div>
            </div>
          </div>
        </el-card>

        <!-- 标签页内容 -->
        <el-card style="margin: 20px;">
          <el-tabs v-model="detailActiveTab" @tab-click="handleDetailTabClick">
            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="basic">
              <div class="basic-info-content">
                <!-- 方案周期 -->
                <div class="info-row">
                  <span class="info-label">方案周期:</span>
                  <el-select
                    v-model="schemeCycle"
                    placeholder="请选择方案周期"
                    style="width: 200px;"
                    disabled
                  >
                    <el-option
                      v-for="month in 12"
                      :key="month"
                      :label="`${month}`"
                      :value="month"
                    />
                  </el-select>
                  <span style="margin-left: 10px; color: #606266;">月</span>
                </div>

                <!-- 健康档案 -->
                <div class="info-row">
                  <span class="info-label">健康档案:</span>
                  <el-button
                    size="small"
                    class="view-btn"
                    @click="handleViewHealthRecord"
                  >
                    查看
                  </el-button>
                </div>

                <!-- 入组评估 -->
                <div class="info-row">
                  <span class="info-label">入组评估:</span>
                  <el-button
                    size="small"
                    class="view-btn"
                    @click="handleViewEnrollmentAssessment"
                  >
                    查看
                  </el-button>
                </div>
              </div>
            </el-tab-pane>

            <!-- 运动处方 -->
            <el-tab-pane label="运动处方" name="prescription">
              <div v-if="detailActiveTab === 'prescription'">
                <el-table
                  v-loading="prescriptionLoading"
                  :data="prescriptionList"
                  border
                  highlight-current-row
                  style="width: 100%"
                  :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
                >
                  <el-table-column prop="prescriptionName" label="运动处方名称" width="220" align="center" />
                  <el-table-column prop="patientInfo" label="患者信息" width="180" align="center">
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
                  <el-table-column prop="medicalRecordNumber" label="档案号" width="180" align="center" />
                  <el-table-column prop="diseaseType" label="病种" width="150" align="center" />
                  <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" />
                  <el-table-column prop="executionInstitution" label="执行机构" width="200" align="center" />
                  <el-table-column prop="prescriptionStatus" label="处方状态" width="140" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="getPrescriptionStatusType(scope.row.prescriptionStatus)" size="mini">
                        {{ getPrescriptionStatusText(scope.row.prescriptionStatus) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="prescribingDoctor" label="开方医生" width="140" align="center" />
                  <el-table-column prop="createdTime" label="创建时间" width="180" align="center" />
                  <el-table-column label="操作" width="140" fixed="right" align="center">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" @click="handleViewPrescription(scope.row)">查看</el-button>
                      <el-button size="mini" type="text" :disabled="scope.row.prescriptionStatus === '已结束'" @click="handleEndPrescription(scope.row)">结束</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="pagination-wrapper" style="margin-top: 20px;">
                  <el-pagination
                    @size-change="handlePrescriptionSizeChange"
                    @current-change="handlePrescriptionPageChange"
                    :current-page="prescriptionQuery.pageNum"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="prescriptionQuery.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="prescriptionTotal"
                    small
                  />
                </div>
              </div>
            </el-tab-pane>

            <!-- 处方执行 -->
            <el-tab-pane label="处方执行" name="prescriptionExecution">
              <div v-if="detailActiveTab === 'prescriptionExecution'">
                <el-table
                  v-loading="prescriptionLoading"
                  :data="prescriptionList"
                  border
                  highlight-current-row
                  style="width: 100%"
                  :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
                >
                  <el-table-column prop="prescriptionName" label="运动处方名称" width="220" align="center" />
                  <el-table-column prop="patientInfo" label="患者信息" width="180" align="center">
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
                  <el-table-column prop="diseaseType" label="病种" width="150" align="center" />
                  <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" />
                  <el-table-column prop="executionInstitution" label="执行机构" width="200" align="center" />
                  <el-table-column prop="prescriptionStatus" label="处方状态" width="140" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="getPrescriptionStatusType(scope.row.prescriptionStatus)" size="mini">
                        {{ getPrescriptionStatusText(scope.row.prescriptionStatus) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="prescribingDoctor" label="开方医生" width="140" align="center" />
                  <el-table-column prop="createdTime" label="创建时间" width="180" align="center" />
                  <el-table-column label="操作" width="220" fixed="right" align="center">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" @click="handleViewPrescription(scope.row)">查看</el-button>
                      <el-button size="mini" type="text" :disabled="scope.row.prescriptionStatus === '已结束'" @click="handleEndPrescription(scope.row)">结束</el-button>
                      <el-button size="mini" type="text">执行</el-button>
                      <el-button size="mini" type="text">清单</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="pagination-wrapper" style="margin-top: 20px;">
                  <el-pagination
                    @size-change="handlePrescriptionSizeChange"
                    @current-change="handlePrescriptionPageChange"
                    :current-page="prescriptionQuery.pageNum"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="prescriptionQuery.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="prescriptionTotal"
                    small
                  />
                </div>
              </div>
            </el-tab-pane>

            <!-- 评定列表 -->
            <el-tab-pane label="评定列表" name="assessment">
              <div v-if="detailActiveTab === 'assessment'">
                <el-table
                  v-loading="assessmentLoading"
                  :data="assessmentList"
                  border
                  highlight-current-row
                  style="width: 100%"
                  :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
                >
                  <el-table-column prop="assessmentName" label="评定方案名称" width="220" align="center" />
                  <el-table-column prop="customerInfo" label="客户信息" width="180" align="center">
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
                  <el-table-column prop="medicalRecordNumber" label="档案号" width="180" align="center" />
                  <el-table-column prop="diseaseType" label="病种" width="150" align="center" />
                  <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" />
                  <el-table-column prop="executionInstitution" label="执行机构" width="200" align="center" />
                  <el-table-column prop="status" label="状态" width="140" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="getAssessmentStatusType(scope.row.status)" size="mini">
                        {{ getAssessmentStatusText(scope.row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="prescribingDoctor" label="开方医生" width="140" align="center" />
                  <el-table-column prop="createdTime" label="创建时间" width="180" align="center" />
                  <el-table-column label="操作" width="160" fixed="right" align="center">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" @click="handleViewAssessment(scope.row)">查看</el-button>
                      <el-button size="mini" type="text" :disabled="scope.row.status === '已结束'" @click="handleEndAssessment(scope.row)">结束</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="pagination-wrapper" style="margin-top: 20px;">
                  <el-pagination
                    @size-change="handleAssessmentSizeChange"
                    @current-change="handleAssessmentPageChange"
                    :current-page="assessmentQuery.pageNum"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="assessmentQuery.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="assessmentTotal"
                    small
                  />
                </div>
              </div>
            </el-tab-pane>

            <!-- 评定执行 -->
            <el-tab-pane label="评定执行" name="assessmentExecution">
              <div v-if="detailActiveTab === 'assessmentExecution'">
                <el-table
                  v-loading="assessmentLoading"
                  :data="assessmentList"
                  border
                  highlight-current-row
                  style="width: 100%"
                  :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
                >
                  <el-table-column prop="assessmentName" label="评定方案名称" width="220" align="center" />
                  <el-table-column prop="customerInfo" label="客户信息" width="180" align="center">
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
                  <el-table-column prop="diseaseType" label="病种" width="150" align="center" />
                  <el-table-column prop="enrollmentInstitution" label="入组机构" width="200" align="center" />
                  <el-table-column prop="executionInstitution" label="执行机构" width="200" align="center" />
                  <el-table-column prop="status" label="状态" width="140" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="getAssessmentStatusType(scope.row.status)" size="mini">
                        {{ getAssessmentStatusText(scope.row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="prescribingDoctor" label="开方医生" width="140" align="center" />
                  <el-table-column prop="createdTime" label="创建时间" width="180" align="center" />
                  <el-table-column label="操作" width="220" fixed="right" align="center">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" @click="handleViewAssessment(scope.row)">查看</el-button>
                      <el-button size="mini" type="text" :disabled="scope.row.status === '已结束'" @click="handleEndAssessment(scope.row)">结束</el-button>
                      <el-button size="mini" type="text">执行</el-button>
                      <el-button size="mini" type="text">清单</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="pagination-wrapper" style="margin-top: 20px;">
                  <el-pagination
                    @size-change="handleAssessmentSizeChange"
                    @current-change="handleAssessmentPageChange"
                    :current-page="assessmentQuery.pageNum"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="assessmentQuery.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="assessmentTotal"
                    small
                  />
                </div>
              </div>
            </el-tab-pane>

            <!-- 随访执行 -->
            <el-tab-pane label="随访执行" name="followupExecution">
              <div v-if="detailActiveTab === 'followupExecution'">
                <el-table
                  v-loading="followupLoading"
                  :data="followupList"
                  style="width: 100%"
                  :header-cell-style="{ color: 'rgb(37, 37, 37)', fontWeight: '500', backgroundColor: 'rgb(250, 250, 250)' }"
                >
                  <el-table-column type="index" label="序号" width="60" align="center" />
                  <el-table-column prop="patientName" label="患者姓名" width="100" align="center" />
                  <el-table-column prop="gender" label="性别" width="60" align="center" />
                  <el-table-column prop="age" label="年龄" width="60" align="center">
                    <template slot-scope="scope">
                      {{ scope.row.age }}岁
                    </template>
                  </el-table-column>
                  <el-table-column prop="followupPlanName" label="随访计划名称" width="150" show-overflow-tooltip />
                  <el-table-column prop="followupProject" label="随访项目" width="150" show-overflow-tooltip />
                  <el-table-column prop="followupTeam" label="随访团队" width="180" show-overflow-tooltip />
                  <el-table-column prop="primaryDoctor" label="主负责医生" width="100" align="center" />
                  <el-table-column prop="applicationTime" label="申请时间" width="160" align="center">
                    <template slot-scope="scope">
                      {{ formatDateTime(scope.row.applicationTime) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="enrollmentTime" label="加入项目时间" width="160" align="center">
                    <template slot-scope="scope">
                      {{ scope.row.enrollmentTime ? formatDateTime(scope.row.enrollmentTime) : '-' }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="phone" label="手机号" width="120" align="center" />
                  <el-table-column prop="followupStatus" label="随访状态" width="100" align="center">
                    <template slot-scope="scope">
                      <el-tag :type="getFollowupStatusType(scope.row.followupStatus)" size="small">
                        {{ scope.row.followupStatus || '未开始' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="200" align="center" fixed="right">
                    <template slot-scope="scope">
                      <el-button
                        type="text"
                        size="small"
                        @click="handleViewFollowup(scope.row)"
                        icon="el-icon-view"
                        style="color: rgb(106, 91, 140);"
                      >
                        查看
                      </el-button>
                      <el-button
                        type="text"
                        size="small"
                        @click="handleRemoveFollowup(scope.row)"
                        icon="el-icon-delete"
                        style="color: #F56C6C;"
                        :disabled="scope.row.followupStatus === '已退出' || scope.row.followupStatus === '已完成'"
                      >
                        移除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="pagination-wrapper" style="margin-top: 20px; text-align: right;">
                  <el-pagination
                    @size-change="handleFollowupSizeChange"
                    @current-change="handleFollowupPageChange"
                    :current-page="followupQuery.pageNum"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="followupQuery.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="followupTotal"
                  />
                </div>
              </div>
            </el-tab-pane>

            <!-- 随访管理 -->
            <el-tab-pane label="随访管理" name="followup">
              <div v-if="detailActiveTab === 'followup'" style="padding: 20px;">
                <div v-if="followupList.length === 0 && !followupLoading">
                  <el-empty description="暂无随访管理信息" />
                </div>
                <div v-else>
                  <!-- 顶部区域：计划名称 -->
                  <div class="followup-header">
                    <div class="plan-name">
                      <span class="label">计划名称：</span>
                      <span class="value">{{ getFollowupDisplayData().planName }}</span>
                    </div>
                  </div>

                  <!-- 患者信息展示区 -->
                  <div class="patient-info-section">
                    <!-- 第一行 -->
                    <div class="info-row">
                      <div class="info-item">
                        <span class="info-label">患者姓名：</span>
                        <span class="info-value">{{ getFollowupDisplayData().patientName }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">患者年龄：</span>
                        <span class="info-value">{{ getFollowupDisplayData().age }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">患者性别：</span>
                        <span class="info-value">{{ getFollowupDisplayData().gender }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">患者身份证号：</span>
                        <span class="info-value">{{ getFollowupDisplayData().idCard }}</span>
                      </div>
                    </div>

                    <!-- 第二行 -->
                    <div class="info-row">
                      <div class="info-item">
                        <span class="info-label">申请时间：</span>
                        <span class="info-value">{{ getFollowupDisplayData().applicationTime }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">加入项目时间：</span>
                        <span class="info-value">{{ getFollowupDisplayData().enrollmentTime }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">来源：</span>
                        <span class="info-value">{{ getFollowupDisplayData().source }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">病种：</span>
                        <span class="info-value">{{ selectedPatient.diseaseType || '-' }}</span>
                      </div>
                    </div>

                    <!-- 第三行 -->
                    <div class="info-row">
                      <div class="info-item">
                        <span class="info-label">手机号：</span>
                        <span class="info-value">{{ getFollowupDisplayData().phone }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">主负责医生：</span>
                        <span class="info-value">{{ getFollowupDisplayData().primaryDoctor }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">健管师：</span>
                        <span class="info-value">{{ getFollowupDisplayData().healthManager }}</span>
                      </div>
                    </div>
                  </div>

                  <!-- 分隔线 -->
                  <el-divider />

                  <!-- 随访计划展示区 -->
                  <div class="followup-plan-content">
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
                  <el-divider />

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
                      暂无操作日志
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
      </template>
    </div>

    <!-- 健康档案弹窗 -->
    <health-record-dialog
      :visible.sync="healthRecordDialogVisible"
      :patient-data="dialogPatientData"
      :patient-id="dialogPatientId"
      @saved="handleHealthRecordSaved"
    />

    <!-- 专病档案弹窗 -->
    <disease-record-dialog
      :visible.sync="diseaseRecordDialogVisible"
      :patient-id="dialogPatientId"
      @saved="handleDiseaseRecordSaved"
    />

    <!-- 入组评估弹窗 -->
    <enrollment-assessment-dialog
      :visible.sync="enrollmentAssessmentDialogVisible"
      :patient-id="dialogPatientId"
      @saved="handleEnrollmentAssessmentSaved"
    />
  </div>
</template>

<script>
import { getPatientList, getPatientCenterDetail, updateEnrollmentAudit } from '@/api/patient'
import { getMedicalSchemeList } from '@/api/medical-scheme'
import { getPrescriptionList, endPrescription } from '@/api/prescription'
import { getPatientAssessmentPlanList, endPatientAssessmentPlan } from '@/api/patient-assessment-plan'
import { getFollowupList, removeFromProject } from '@/api/followup'
import { getSchemeCycleByPatientId } from '@/api/scheme-cycle'
import HealthRecordDialog from '@/views/patient/components/HealthRecordDialog.vue'
import DiseaseRecordDialog from '@/views/patient/components/DiseaseRecordDialog.vue'
import EnrollmentAssessmentDialog from '@/views/patient/components/EnrollmentAssessmentDialog.vue'

export default {
  name: 'SchemeCenter',
  components: {
    HealthRecordDialog,
    DiseaseRecordDialog,
    EnrollmentAssessmentDialog
  },
  data() {
    return {
      schemeCycle: null,
      healthRecordDialogVisible: false,
      diseaseRecordDialogVisible: false,
      enrollmentAssessmentDialogVisible: false,
      activeTab: 'ungrouped',
      dateRange: null,
      searchKeyword: '',
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      selectedPatient: null,
      detailActiveTab: 'basic',
      ungroupedDetail: null,
      ungroupedDetailTab: 'basic',
      ungroupedDetailLoading: false,
      // 运动处方相关
      prescriptionLoading: false,
      prescriptionList: [],
      prescriptionTotal: 0,
      prescriptionQuery: {
        pageNum: 1,
        pageSize: 10
      },
      // 评定列表相关
      assessmentLoading: false,
      assessmentList: [],
      assessmentTotal: 0,
      assessmentQuery: {
        pageNum: 1,
        pageSize: 10
      },
      // 随访管理相关
      followupLoading: false,
      followupList: [],
      followupTotal: 0,
      followupQuery: {
        pageNum: 1,
        pageSize: 10
      },
      followupPlanProgress: 0,
      followupTasks: [],
      operationLogs: [],
      enrollmentAuditForm: {
        status: 'PASSED',
        rejectReason: ''
      },
      savingEnrollmentAudit: false
    }
  },
  computed: {
    basicInfoData() {
      return this.ungroupedDetail && this.ungroupedDetail.basicInfo ? this.ungroupedDetail.basicInfo : {}
    },
    detailInfoData() {
      return this.ungroupedDetail && this.ungroupedDetail.detailInfo ? this.ungroupedDetail.detailInfo : {}
    },
    healthRecordData() {
      return this.ungroupedDetail && this.ungroupedDetail.healthRecord ? this.ungroupedDetail.healthRecord : null
    },
    diseaseRecordData() {
      return this.ungroupedDetail && this.ungroupedDetail.diseaseRecord ? this.ungroupedDetail.diseaseRecord : null
    },
    enrollmentAssessmentData() {
      return this.ungroupedDetail && this.ungroupedDetail.enrollmentAssessment
        ? this.ungroupedDetail.enrollmentAssessment
        : null
    },
    enrollmentStatusValue() {
      if (this.ungroupedDetail && this.ungroupedDetail.enrollmentStatus) {
        return this.ungroupedDetail.enrollmentStatus
      }
      return 'PENDING'
    },
    dialogPatientId() {
      if (!this.selectedPatient) return null
      if (this.activeTab === 'ungrouped') {
        return this.selectedPatient.id || this.selectedPatient.patientId || null
      }
      return this.selectedPatient.patientId || this.selectedPatient.id || null
    },
    dialogPatientData() {
      if (this.activeTab === 'ungrouped' && this.ungroupedDetail) {
        return {
          ...this.basicInfoData,
          ...this.detailInfoData
        }
      }
      return this.selectedPatient || {}
    },
    enrollmentStatusText() {
      const map = {
        PASSED: '审核通过',
        REJECTED: '审核拒绝',
        PENDING: '待审核'
      }
      return map[this.enrollmentStatusValue] || '待审核'
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    resetEnrollmentAuditForm(detail = null) {
      if (detail && detail.enrollmentStatus === 'REJECTED') {
        this.enrollmentAuditForm.status = 'REJECTED'
        this.enrollmentAuditForm.rejectReason = detail.enrollmentRejectReason || ''
      } else if (detail && detail.enrollmentStatus === 'PASSED') {
        this.enrollmentAuditForm.status = 'PASSED'
        this.enrollmentAuditForm.rejectReason = ''
      } else {
        this.enrollmentAuditForm.status = 'PASSED'
        this.enrollmentAuditForm.rejectReason = ''
      }
    },
    handleTabChange(tab) {
      this.activeTab = tab
      this.currentPage = 1
      this.selectedPatient = null
      this.detailActiveTab = 'basic'
      this.ungroupedDetail = null
      this.ungroupedDetailTab = 'basic'
      this.resetEnrollmentAuditForm()
      this.fetchData()
    },
    handleSearch() {
      this.currentPage = 1
      this.fetchData()
    },
    handleReset() {
      this.dateRange = null
      this.searchKeyword = ''
      this.currentPage = 1
      this.selectedPatient = null
      this.ungroupedDetail = null
      this.resetEnrollmentAuditForm()
      this.fetchData()
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      this.fetchData()
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchData()
    },
    handleRowClick(row) {
      this.selectedPatient = row
      if (this.activeTab === 'ungrouped') {
        this.ungroupedDetailTab = 'basic'
        this.loadUngroupedDetail(row.id || row.patientId)
      } else {
        this.detailActiveTab = 'basic'
        // 加载方案周期
        this.loadSchemeCycle()
      }
    },

    async loadUngroupedDetail(patientId) {
      if (!patientId) {
        this.ungroupedDetail = null
        this.resetEnrollmentAuditForm()
        return
      }
      this.ungroupedDetailLoading = true
      try {
        const response = await getPatientCenterDetail(patientId)
        this.ungroupedDetail = response.data || null
        this.resetEnrollmentAuditForm(this.ungroupedDetail)
      } catch (error) {
        console.error('获取患者详情失败:', error)
        this.$message.error('获取患者详情失败')
        this.ungroupedDetail = null
        this.resetEnrollmentAuditForm()
      } finally {
        this.ungroupedDetailLoading = false
      }
      return this.ungroupedDetail
    },

    /** 加载方案周期 */
    async loadSchemeCycle() {
      if (this.selectedPatient && this.selectedPatient.patientId) {
        try {
          const response = await getSchemeCycleByPatientId(this.selectedPatient.patientId)
          if (response && response.data) {
            this.schemeCycle = response.data.cycleMonths
          }
        } catch (error) {
          console.error('加载方案周期失败:', error)
          // 如果没有方案周期数据，不显示错误
          this.schemeCycle = null
        }
      }
    },

    /** 查看健康档案 */
    handleViewHealthRecord() {
      if (!this.dialogPatientId) {
        this.$message.error('患者ID不存在')
        return
      }
      this.healthRecordDialogVisible = true
    },

    /** 查看专病档案 */
    handleViewDiseaseRecord() {
      if (!this.dialogPatientId) {
        this.$message.error('患者ID不存在')
        return
      }
      this.diseaseRecordDialogVisible = true
    },

    /** 查看入组评估 */
    handleViewEnrollmentAssessment() {
      if (!this.dialogPatientId) {
        this.$message.error('患者ID不存在')
        return
      }
      this.enrollmentAssessmentDialogVisible = true
    },

    /** 健康档案保存成功回调 */
    handleHealthRecordSaved() {
      this.$message.success('健康档案保存成功')
      this.healthRecordDialogVisible = false
    },

    /** 专病档案保存成功回调 */
    handleDiseaseRecordSaved() {
      this.$message.success('专病档案保存成功')
      this.diseaseRecordDialogVisible = false
    },

    /** 入组评估保存成功回调 */
    handleEnrollmentAssessmentSaved() {
      this.$message.success('入组评估保存成功')
      this.enrollmentAssessmentDialogVisible = false
    },
    handleEnrollmentStatusChange(value) {
      if (value === 'PASSED') {
        this.enrollmentAuditForm.rejectReason = ''
      }
    },
    async handleSaveEnrollmentAudit() {
      if (this.activeTab !== 'ungrouped') return
      const patientId = this.dialogPatientId
      if (!patientId) {
        this.$message.error('请选择患者后再保存审核状态')
        return
      }
      const status = this.enrollmentAuditForm.status
      let reason = (this.enrollmentAuditForm.rejectReason || '').trim()
      if (status === 'REJECTED' && !reason) {
        this.$message.error('请输入拒绝原因')
        return
      }
      if (status === 'REJECTED' && reason.length > 200) {
        this.$message.error('拒绝原因不能超过200字')
        return
      }
      const payload = {
        patientId,
        status
      }
      if (status === 'REJECTED') {
        payload.rejectReason = reason
      }
      this.savingEnrollmentAudit = true
      try {
        await updateEnrollmentAudit(payload)
        this.$message.success('审核状态已保存')
        await this.loadUngroupedDetail(patientId)
      } catch (error) {
        console.error('保存审核状态失败:', error)
        const msg = error?.response?.data?.message || '保存失败，请稍后重试'
        this.$message.error(msg)
      } finally {
        this.savingEnrollmentAudit = false
      }
    },
    handleDetailTabClick(tab) {
      console.log('切换到标签页:', tab.name)
      // 根据标签页加载对应数据
      if (tab.name === 'prescription') {
        this.loadPrescriptionList()
      } else if (tab.name === 'prescriptionExecution') {
        this.loadPrescriptionExecutionList()
      } else if (tab.name === 'assessment') {
        this.loadAssessmentList()
      } else if (tab.name === 'assessmentExecution') {
        this.loadAssessmentExecutionList()
      } else if (tab.name === 'followup') {
        this.loadFollowupList()
      } else if (tab.name === 'followupExecution') {
        this.loadFollowupExecutionList()
      }
    },
    /** 加载运动处方列表 */
    loadPrescriptionList() {
      if (!this.selectedPatient) return
      this.prescriptionLoading = true
      // 注意：selectedPatient.id 是 medical_scheme 的 ID，selectedPatient.patientId 才是真正的患者 ID
      const patientId = this.selectedPatient.patientId || this.selectedPatient.id
      const params = {
        pageNum: this.prescriptionQuery.pageNum,
        pageSize: this.prescriptionQuery.pageSize,
        patientId: patientId
      }
      console.log('加载运动处方列表，参数：', params, '患者信息：', this.selectedPatient)
      getPrescriptionList(params).then(response => {
        console.log('运动处方列表响应：', response)
        this.prescriptionList = response.data.records || []
        this.prescriptionTotal = response.data.total || 0
        this.prescriptionLoading = false
      }).catch(error => {
        console.error('加载运动处方失败：', error)
        this.prescriptionLoading = false
      })
    },
    /** 处方分页 */
    handlePrescriptionSizeChange(size) {
      this.prescriptionQuery.pageSize = size
      this.prescriptionQuery.pageNum = 1
      this.loadPrescriptionList()
    },
    handlePrescriptionPageChange(page) {
      this.prescriptionQuery.pageNum = page
      this.loadPrescriptionList()
    },
    /** 查看处方 */
    handleViewPrescription(row) {
      this.$router.push({
        path: '/prescription/view',
        query: { id: row.id }
      })
    },
    /** 结束处方 */
    handleEndPrescription(row) {
      this.$confirm('确认结束该运动处方吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        endPrescription(row.id).then(() => {
          this.$message.success('运动处方已结束')
          this.loadPrescriptionList()
        })
      }).catch(() => {
        // 用户取消操作
      })
    },
    /** 加载处方执行列表 */
    loadPrescriptionExecutionList() {
      if (!this.selectedPatient) return
      this.prescriptionLoading = true
      const patientId = this.selectedPatient.patientId || this.selectedPatient.id
      const params = {
        pageNum: this.prescriptionQuery.pageNum,
        pageSize: this.prescriptionQuery.pageSize,
        patientId: patientId
      }
      console.log('加载处方执行列表，参数：', params, '患者信息：', this.selectedPatient)
      getPrescriptionList(params).then(response => {
        console.log('处方执行列表响应：', response)
        this.prescriptionList = response.data.records || []
        this.prescriptionTotal = response.data.total || 0
        this.prescriptionLoading = false
      }).catch(error => {
        console.error('加载处方执行失败：', error)
        this.prescriptionLoading = false
      })
    },
    /** 加载评定列表 */
    loadAssessmentList() {
      if (!this.selectedPatient) return
      this.assessmentLoading = true
      const patientId = this.selectedPatient.patientId || this.selectedPatient.id
      const params = {
        pageNum: this.assessmentQuery.pageNum,
        pageSize: this.assessmentQuery.pageSize,
        patientId: patientId
      }
      console.log('加载评定列表，参数：', params, '患者信息：', this.selectedPatient)
      getPatientAssessmentPlanList(params).then(response => {
        console.log('评定列表响应：', response)
        this.assessmentList = response.data.records || []
        this.assessmentTotal = response.data.total || 0
        this.assessmentLoading = false
      }).catch(error => {
        console.error('加载评定列表失败：', error)
        this.assessmentLoading = false
      })
    },
    /** 评定分页 */
    handleAssessmentSizeChange(size) {
      this.assessmentQuery.pageSize = size
      this.assessmentQuery.pageNum = 1
      this.loadAssessmentList()
    },
    handleAssessmentPageChange(page) {
      this.assessmentQuery.pageNum = page
      this.loadAssessmentList()
    },
    /** 查看评定 */
    handleViewAssessment(row) {
      this.$router.push({
        path: '/assessment/view',
        query: { id: row.id }
      })
    },
    /** 结束评定 */
    handleEndAssessment(row) {
      this.$confirm('确认结束该评定方案吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        endPatientAssessmentPlan(row.id).then(() => {
          this.$message.success('评定方案已结束')
          this.loadAssessmentList()
        })
      })
    },
    /** 加载评定执行列表 */
    loadAssessmentExecutionList() {
      if (!this.selectedPatient) return
      this.assessmentLoading = true
      const patientId = this.selectedPatient.patientId || this.selectedPatient.id
      const params = {
        pageNum: this.assessmentQuery.pageNum,
        pageSize: this.assessmentQuery.pageSize,
        patientId: patientId
      }
      console.log('加载评定执行列表，参数：', params, '患者信息：', this.selectedPatient)
      getPatientAssessmentPlanList(params).then(response => {
        console.log('评定执行列表响应：', response)
        this.assessmentList = response.data.records || []
        this.assessmentTotal = response.data.total || 0
        this.assessmentLoading = false
      }).catch(error => {
        console.error('加载评定执行失败：', error)
        this.assessmentLoading = false
      })
    },
    /** 加载随访管理列表 */
    loadFollowupList() {
      if (!this.selectedPatient) return
      this.followupLoading = true
      const patientId = this.selectedPatient.patientId || this.selectedPatient.id
      const params = {
        pageNum: this.followupQuery.pageNum,
        pageSize: this.followupQuery.pageSize,
        patientId: patientId
      }
      console.log('加载随访管理列表，参数：', params, '患者信息：', this.selectedPatient)
      getFollowupList(params).then(response => {
        console.log('随访管理列表响应：', response)
        this.followupList = response.data.records || []
        this.followupTotal = response.data.total || 0
        this.followupLoading = false
        // 初始化随访任务和操作日志
        this.initFollowupTasks()
        this.operationLogs = []
      }).catch(error => {
        console.error('加载随访管理失败：', error)
        this.followupLoading = false
      })
    },
    /** 加载随访执行列表 */
    loadFollowupExecutionList() {
      if (!this.selectedPatient) return
      this.followupLoading = true
      const patientId = this.selectedPatient.patientId || this.selectedPatient.id
      const params = {
        pageNum: this.followupQuery.pageNum,
        pageSize: this.followupQuery.pageSize,
        patientId: patientId
      }
      console.log('加载随访执行列表，参数：', params, '患者信息：', this.selectedPatient)
      getFollowupList(params).then(response => {
        console.log('随访执行列表响应：', response)
        this.followupList = response.data.records || []
        this.followupTotal = response.data.total || 0
        this.followupLoading = false
      }).catch(error => {
        console.error('加载随访执行失败：', error)
        this.followupLoading = false
      })
    },
    /** 随访分页 */
    handleFollowupSizeChange(size) {
      this.followupQuery.pageSize = size
      this.followupQuery.pageNum = 1
      if (this.detailActiveTab === 'followup') {
        this.loadFollowupList()
      } else {
        this.loadFollowupExecutionList()
      }
    },
    handleFollowupPageChange(page) {
      this.followupQuery.pageNum = page
      if (this.detailActiveTab === 'followup') {
        this.loadFollowupList()
      } else {
        this.loadFollowupExecutionList()
      }
    },
    /** 查看随访 */
    handleViewFollowup(row) {
      this.$router.push({
        path: '/scheme/patient-view',
        query: {
          patientId: row.patientId,
          schemeId: this.selectedPatient.id
        }
      })
    },
    /** 移除随访 */
    handleRemoveFollowup(row) {
      this.$confirm('确认移除该患者的随访项目吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeFromProject(row.id).then(() => {
          this.$message.success('移除成功')
          this.loadFollowupExecutionList()
        })
      }).catch(() => {
        // 用户取消操作
      })
    },
    /** 格式化日期时间 */
    formatDateTime(datetime) {
      if (!datetime) return '-'
      const date = new Date(datetime)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      }).replace(/\//g, '-')
    },
    formatDateDisplay(date) {
      if (!date) return '-'
      if (this.formatDateOnly) {
        return this.formatDateOnly(date)
      }
      return date
    },
    formatGenderText(gender) {
      if (!gender) return '-'
      if (gender === '男' || gender === '女') {
        return gender
      }
      if (gender === '1' || gender === 1) {
        return '男'
      }
      if (gender === '2' || gender === 2) {
        return '女'
      }
      return gender
    },
    /** 获取随访状态类型 */
    getFollowupStatusType(status) {
      const statusMap = {
        '进行中': 'warning',
        '已完成': 'success',
        '未开始': 'info',
        '已暂停': 'danger',
        '待入组': 'info',
        '随访中': 'warning',
        '已退出': 'info'
      }
      return statusMap[status] || 'info'
    },
    /** 获取随访展示数据 */
    getFollowupDisplayData() {
      if (this.followupList.length === 0) {
        return {}
      }
      const followupInfo = this.followupList[0]
      const displayData = {
        planName: followupInfo.followupPlanName || '-',
        patientName: followupInfo.patientName || this.selectedPatient.patientName || '-',
        age: followupInfo.age || this.calculateAge(this.selectedPatient.birthDate) || '-',
        gender: followupInfo.gender || this.selectedPatient.gender || '-',
        idCard: followupInfo.idCard || this.selectedPatient.idCard || '-',
        applicationTime: this.formatDateTime(followupInfo.applicationTime) || '-',
        enrollmentTime: this.formatDateTime(followupInfo.enrollmentTime) || '-',
        source: followupInfo.source || '-',
        phone: followupInfo.phone || this.selectedPatient.phone || '-',
        primaryDoctor: followupInfo.primaryDoctor || '-',
        healthManager: followupInfo.healthManager || '-'
      }
      return displayData
    },
    /** 初始化随访任务 */
    initFollowupTasks() {
      if (this.followupList.length === 0) {
        this.followupTasks = []
        this.followupPlanProgress = 0
        return
      }
      const followupInfo = this.followupList[0]
      this.followupTasks = [
        {
          date: this.formatDate(followupInfo.enrollmentTime),
          statusText: '已完成',
          title: `${followupInfo.followupPlanName || '随访计划'} - 第1期`,
          status: 'completed'
        },
        {
          date: this.getNextPeriodDate(followupInfo.enrollmentTime, 1),
          statusText: '进行中',
          title: `${followupInfo.followupPlanName || '随访计划'} - 第2期`,
          status: 'inProgress'
        },
        {
          date: this.getNextPeriodDate(followupInfo.enrollmentTime, 2),
          statusText: '未开始',
          title: `${followupInfo.followupPlanName || '随访计划'} - 第3期`,
          status: 'pending'
        }
      ]
      const completedTasks = this.followupTasks.filter(t => t.status === 'completed').length
      this.followupPlanProgress = Math.round((completedTasks / this.followupTasks.length) * 100)
    },
    /** 格式化日期 */
    formatDate(dateStr) {
      if (!dateStr) return new Date().toISOString().split('T')[0]
      return dateStr.split(' ')[0]
    },
    /** 格式化日期（只显示日期部分，yyyy-MM-dd） */
    formatDateOnly(datetime) {
      if (!datetime) return '-'
      const date = new Date(datetime)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    /** 获取下一期日期（每期间隔30天） */
    getNextPeriodDate(startDate, periodIndex) {
      if (!startDate) {
        const date = new Date()
        date.setDate(date.getDate() + periodIndex * 30)
        return date.toISOString().split('T')[0]
      }
      const date = new Date(startDate)
      date.setDate(date.getDate() + periodIndex * 30)
      return date.toISOString().split('T')[0]
    },
    /** 去填写 */
    handleGoFill(task) {
      this.$message.info('去填写: ' + task.title)
    },
    /** 提醒患者 */
    handleRemindPatient(task) {
      this.$message.info('提醒患者: ' + task.title)
    },
    /** 发起临时变更 */
    handleTemporaryChange(task) {
      this.$message.info('发起临时变更: ' + task.title)
    },
    /** 发起临时事件 */
    handleTemporaryEvent(task) {
      this.$message.info('发起临时事件: ' + task.title)
    },
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
    /** 获取处方状态文本 */
    getPrescriptionStatusText(status) {
      const statusMap = {
        1: '已创建',
        2: '执行中',
        3: '已完成',
        4: '已结束'
      }
      return statusMap[status] || '未知'
    },
    /** 获取处方状态类型 */
    getPrescriptionStatusType(status) {
      const statusMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return statusMap[status] || 'info'
    },
    /** 获取评定状态文本 */
    getAssessmentStatusText(status) {
      const statusMap = {
        1: '已创建',
        2: '执行中',
        3: '已完成',
        4: '已结束'
      }
      return statusMap[status] || '未知'
    },
    /** 获取评定状态类型 */
    getAssessmentStatusType(status) {
      const statusMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
      }
      return statusMap[status] || 'info'
    },
    getRowClassName({ row }) {
      if (this.selectedPatient && row.id === this.selectedPatient.id) {
        return 'selected-row'
      }
      return ''
    },
    async fetchData() {
      this.loading = true
      try {
        if (this.activeTab === 'ungrouped') {
          await this.fetchUngroupedPatients()
        } else {
          await this.fetchGroupedPatients()
        }
      } catch (error) {
        console.error('获取数据失败:', error)
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    /** 获取未入组患者 */
    async fetchUngroupedPatients() {
      // 先获取所有方案，找出已入组的患者档案号
      const schemeRes = await getMedicalSchemeList({
        pageNum: 1,
        pageSize: 1000
      })

      const enrolledRecordNos = new Set()
      if (schemeRes.data && schemeRes.data.records) {
        schemeRes.data.records.forEach(scheme => {
          if (scheme.medicalRecordNo) {
            enrolledRecordNos.add(scheme.medicalRecordNo)
          }
        })
      }

      // 获取所有患者
      const params = {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }

      if (this.searchKeyword) {
        params.keyword = this.searchKeyword
      }

      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.dateRange[0]
        params.endDate = this.dateRange[1]
      }

      const response = await getPatientList(params)

      // 过滤出未入组的患者
      const allPatients = response.data.records || []
      const ungroupedPatients = allPatients.filter(patient =>
        !enrolledRecordNos.has(patient.medicalRecordNo)
      )

      this.tableData = ungroupedPatients.map(patient => ({
        ...patient,
        patientName: patient.name, // 统一字段名
        createTime: patient.createdTime || patient.createTime
      }))
      this.total = ungroupedPatients.length
    },
    /** 获取已入组患者 */
    async fetchGroupedPatients() {
      const params = {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }

      if (this.searchKeyword) {
        params.searchKeyword = this.searchKeyword
      }

      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.dateRange[0]
        params.endDate = this.dateRange[1]
      }

      const response = await getMedicalSchemeList(params)

      // 转换数据格式，添加入组日期
      const schemes = response.data.records || []
      this.tableData = schemes.map(scheme => ({
        id: scheme.id,
        patientId: scheme.patientId,
        patientName: scheme.patientName,
        gender: scheme.genderDesc || (scheme.gender === 1 ? '男' : scheme.gender === 2 ? '女' : '未知'),
        age: scheme.age,
        medicalRecordNo: scheme.archiveNo, // 使用archiveNo映射到medicalRecordNo
        idCard: scheme.idCard, // 身份证
        birthDate: scheme.birthDate, // 出生日期
        diagnosis: scheme.diagnosis, // 诊断
        enrollmentDate: this.formatDateOnly(scheme.createTime), // 入组日期等于创建时间，只显示日期部分
        createTime: scheme.createTime,
        diseaseType: scheme.diseaseType
      }))

      this.total = response.data.total || 0
    }
  }
}
</script>

<style lang="scss" scoped>
.scheme-center-container {
  display: flex;
  height: calc(100vh - 84px);
  background: #f0f2f5;
  padding: 20px;
  gap: 20px;
}

// 左侧患者列表卡片 - 固定占三分之一宽度
.patient-list-card {
  width: 33.33%;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

// 顶部标签页
.tabs-header {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.tab-item {
  flex: 1;
  padding: 14px 0;
  text-align: center;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  background: #fff;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
  position: relative;

  &:hover {
    color: rgb(144, 126, 179);
  }

  &.active {
    color: rgb(144, 126, 179);
    border-bottom-color: rgb(144, 126, 179);
    font-weight: 500;
  }
}

// 筛选区域
.filter-section {
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.filter-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;

  label {
    margin-right: 8px;
    font-size: 13px;
    color: #606266;
    white-space: nowrap;
    min-width: 70px;
  }

  .date-picker {
    flex: 1;
  }

  .search-input {
    flex: 1;
  }
}

.filter-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;

  .el-button {
    flex: 1;
  }

  .custom-primary-btn {
    background-color: rgb(144, 126, 179);
    border-color: rgb(144, 126, 179);

    &:hover,
    &:focus {
      background-color: rgb(124, 106, 159);
      border-color: rgb(124, 106, 159);
    }
  }
}

// 表格区域
.table-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 16px;
}

.table-wrapper {
  flex: 1;
  overflow-x: auto;
  overflow-y: auto;

  // 确保表格内容不换行
  ::v-deep .el-table {
    .el-table__body-wrapper {
      overflow-x: auto;
    }

    .el-table__cell {
      white-space: nowrap;
    }
  }

  // 自定义滚动条样式
  &::-webkit-scrollbar {
    width: 8px;
    height: 8px;
  }

  &::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 4px;

    &:hover {
      background: #b4b7bd;
    }
  }

  &::-webkit-scrollbar-track {
    background: #f5f7fa;
  }
}

.patient-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;

  .gender-icon {
    font-size: 13px;
    font-weight: bold;

    &.male {
      color: #409eff;
    }

    &.female {
      color: #f56c6c;
    }
  }

  .age {
    color: #909399;
    font-size: 12px;
  }
}

// 分页
.pagination-wrapper {
  margin-top: 12px;
  display: flex;
  justify-content: center;
  padding-top: 12px;
  border-top: 1px solid #e4e7ed;
  flex-shrink: 0;

  ::v-deep .el-pagination {
    .el-pager li {
      &.active {
        color: #fff;
        background-color: rgb(144, 126, 179);
        border-color: rgb(144, 126, 179);
      }

      &:hover {
        color: rgb(144, 126, 179);
      }
    }

    .btn-prev:hover,
    .btn-next:hover {
      color: rgb(144, 126, 179);
    }
  }
}

// 空状态
.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #909399;
  font-size: 13px;
}

// 右侧详情区域 - 占三分之二宽度
.detail-panel {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.empty-detail {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 14px;
}

.patient-detail-container {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 3px;
  }
}

.ungrouped-detail-container {
  padding: 20px;
}

.ungrouped-tabs-card {
  ::v-deep .el-tabs__content {
    padding-top: 10px;
  }
}

.section-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  background: #fff;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
}

.section-grid {
  display: flex;
  flex-wrap: wrap;
}

.section-grid .section-item {
  width: 33.33%;
  display: flex;
  font-size: 14px;
  margin-bottom: 12px;
  color: #303133;
}

.supplementary-grid .section-item {
  width: 50%;
}

.section-label {
  color: #606266;
  margin-right: 8px;
  white-space: nowrap;
}

.section-value {
  flex: 1;
  color: #303133;
}

.textarea-row {
  display: flex;
  align-items: flex-start;
  margin-top: 12px;
}

.textarea-row .section-label {
  line-height: 24px;
}

.remark-text {
  min-height: 60px;
  padding: 12px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  background-color: #f9fafb;
}

.enrollment-form .form-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.enrollment-form label {
  width: 150px;
  font-weight: 500;
  color: #606266;
}

.enrollment-form .el-input,
.enrollment-form .el-select {
  flex: 1;
}

.enrollment-form .action-row {
  align-items: flex-start;
}

.action-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.inline-status {
  font-size: 13px;
  color: #c0c4cc;
}

.inline-status.filled {
  color: #67c23a;
}

.audit-divider {
  margin: 8px 0 16px 0;
}

.audit-row {
  flex-wrap: wrap;
}

.reject-row {
  align-items: flex-start;
}

.reject-input {
  flex: 1;
}

.audit-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-top: 4px;
}

.current-status {
  color: #909399;
  font-size: 13px;
}

.audit-buttons {
  display: flex;
  gap: 10px;
}

.radio-row {
  align-items: center;
}

.status-hint {
  margin-left: 16px;
  color: #909399;
  font-size: 13px;
}

// 患者信息卡片
.patient-info-card {
  margin: 20px 20px 0 20px;

  ::v-deep .el-card__body {
    padding: 20px;
  }

  .info-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 20px;

    .info-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;

      .info-label {
        color: rgb(155, 127, 179);
        font-size: 14px;
        margin-bottom: 8px;
        font-weight: 500;
      }

      .info-value {
        color: #303133;
        font-size: 14px;
      }
    }
  }
}

// 基本信息内容
.basic-info-content {
  padding: 20px;

  .info-row {
    margin-bottom: 16px;
    font-size: 14px;

    .info-label {
      color: #606266;
      font-weight: 500;
      margin-right: 8px;
    }

    .info-text {
      color: #303133;
    }
  }
}

.view-btn {
  background-color: rgb(144, 126, 179);
  border-color: rgb(144, 126, 179);
  color: #fff;

  &:hover,
  &:focus {
    background-color: rgb(124, 106, 159);
    border-color: rgb(124, 106, 159);
    color: #fff;
  }
}

// Element UI 样式覆盖
::v-deep .el-table {
  font-size: 12px;

  th {
    font-weight: 500;
    padding: 8px 0;
  }

  td {
    padding: 8px 0;
  }

  .el-table__header-wrapper {
    th {
      background: #f5f7fa !important;
    }
  }

  .el-table__row {
    cursor: pointer;

    &:hover {
      background-color: #f5f7fa;
    }

    &.selected-row {
      background-color: #ecf5ff !important;
    }
  }
}

::v-deep .el-tabs {
  .el-tabs__item {
    color: #303133;
    font-size: 14px;

    &.is-active {
      color: rgb(155, 127, 179);
    }

    &:hover {
      color: rgb(155, 127, 179);
    }
  }

  .el-tabs__active-bar {
    background-color: rgb(155, 127, 179);
  }
}

::v-deep .el-button--small {
  padding: 8px 12px;
  font-size: 12px;
}

::v-deep .el-input--small .el-input__inner {
  height: 32px;
  line-height: 32px;
  font-size: 12px;
}

::v-deep .el-date-editor {
  width: 100% !important;

  .el-range-separator {
    width: 30px;
    padding: 0 5px;
    line-height: 26px;
  }

  .el-range-input {
    font-size: 12px;
  }
}

::v-deep .el-pagination {
  font-size: 12px;

  .el-pagination__total,
  .el-pagination__jump {
    font-size: 12px;
  }
}

// 性别图标样式
.male-icon {
  color: #409eff;
  font-weight: bold;
  margin: 0 2px;
}

.female-icon {
  color: #f56c6c;
  font-weight: bold;
  margin: 0 2px;
}

// 标签页内的表格样式
::v-deep .el-tab-pane {
  .el-table {
    .el-button--text {
      padding: 0 5px;
      margin: 0 2px;
      font-size: 14px;
      color: rgb(106, 91, 140);

      &:hover {
        color: rgb(96, 81, 130);
      }
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
    font-size: 18px;
    font-weight: 600;
    color: #303133;

    .label {
      color: #606266;
      margin-right: 8px;
    }

    .value {
      color: #303133;
    }
  }
}

.patient-info-section {
  background-color: #f9fafb;
  padding: 20px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  margin-bottom: 20px;

  .info-row {
    display: flex;
    margin-bottom: 15px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .info-item {
    flex: 1;
    display: flex;
    align-items: center;
    font-size: 14px;
  }

  .info-label {
    color: #606266;
    margin-right: 8px;
    white-space: nowrap;
  }

  .info-value {
    color: #303133;
    font-weight: 500;
  }
}

.followup-plan-content {
  margin-top: 20px;
}

.followup-progress {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #f0f9ff;
  border-radius: 4px;
  margin-bottom: 25px;

  .progress-text {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
  }

  .progress-value {
    font-size: 24px;
    font-weight: 600;
    color: #409eff;
  }
}

.followup-timeline {
  .timeline-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 30px;
    position: relative;

    &:last-child {
      margin-bottom: 0;

      .timeline-line {
        display: none;
      }
    }
  }

  .timeline-left {
    width: 180px;
    flex-shrink: 0;
    text-align: right;
    padding-right: 25px;
    position: relative;
  }

  .timeline-date {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
    margin-bottom: 8px;
  }

  .timeline-status {
    font-size: 14px;
    color: #909399;
  }

  .timeline-dot {
    position: absolute;
    right: -8px;
    top: 5px;
    width: 16px;
    height: 16px;
    background-color: #409eff;
    border: 3px solid #fff;
    border-radius: 50%;
    box-shadow: 0 0 0 2px #409eff;
  }

  .timeline-line {
    position: absolute;
    right: 0;
    top: 21px;
    width: 2px;
    height: calc(100% + 30px);
    background-color: #e4e7ed;
  }

  .timeline-divider {
    width: 2px;
    background-color: #e4e7ed;
    margin: 0 20px;
  }

  .timeline-right {
    flex: 1;
  }

  .task-card {
    border: 1px solid #e4e7ed;
    border-radius: 8px;

    ::v-deep .el-card__body {
      padding: 20px;
    }
  }

  .task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .task-title {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
    margin: 0;
  }

  .task-actions-inline {
    display: flex;
    align-items: center;
    gap: 8px;

    .action-link {
      color: rgb(106, 91, 140);
      font-size: 14px;
      text-decoration: none;

      &:hover {
        color: rgb(96, 81, 130);
        text-decoration: underline;
      }
    }

    .divider {
      color: #dcdfe6;
    }
  }

  .task-footer {
    display: flex;
    justify-content: flex-end;
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid #f0f0f0;
  }
}

.operation-log {
  margin-top: 30px;
}
</style>
