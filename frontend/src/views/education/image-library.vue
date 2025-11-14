<template>
  <div class="image-library-container">
    <el-card>
      <div slot="header">
        <span>图文素材库</span>
      </div>

      <!-- 搜索筛选区域 -->
      <div class="search-form">
        <el-form :inline="true" :model="searchForm" ref="searchForm" size="small">
          <el-form-item label="发布日期" prop="publishDateRange">
            <el-date-picker
              v-model="searchForm.publishDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item label="内容标题" prop="contentTitle">
            <el-input
              v-model="searchForm.contentTitle"
              placeholder="请输入内容标题"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          <el-form-item label="作者" prop="author">
            <el-input
              v-model="searchForm.author"
              placeholder="请输入作者"
              clearable
              style="width: 150px;"
            />
          </el-form-item>
          <el-form-item label="所属板块" prop="categorySection">
            <el-select
              v-model="searchForm.categorySection"
              placeholder="请选择"
              clearable
              style="width: 150px;"
            >
              <el-option label="医教" value="医教" />
              <el-option label="科普" value="科普" />
              <el-option label="康复指导" value="康复指导" />
              <el-option label="健康资讯" value="健康资讯" />
              <el-option label="疾病预防" value="疾病预防" />
            </el-select>
          </el-form-item>
          <el-form-item label="内容状态" prop="contentStatus">
            <el-select
              v-model="searchForm.contentStatus"
              placeholder="请选择"
              clearable
              style="width: 120px;"
            >
              <el-option label="草稿" value="草稿" />
              <el-option label="待审核" value="待审核" />
              <el-option label="已上架" value="已上架" />
              <el-option label="未上架" value="未上架" />
              <el-option label="已下架" value="已下架" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" icon="el-icon-search">
              查询
            </el-button>
            <el-button @click="handleReset" icon="el-icon-refresh">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="toolbar">
        <el-button
          type="primary"
          @click="handleAdd"
          icon="el-icon-plus"
          size="small"
        >
          添加内容
        </el-button>
      </div>

      <!-- 图文素材列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="contentTitle" label="内容标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="contentId" label="内容ID" width="150" show-overflow-tooltip />
        <el-table-column prop="articleDescription" label="文章描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="categorySection" label="所属板块" width="120" align="center" />
        <el-table-column prop="viewCount" label="阅读量" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ formatNumber(scope.row.viewCount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞量" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ formatNumber(scope.row.likeCount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="100" align="center" />
        <el-table-column prop="publishTime" label="发布时间" width="160" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.publishTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="contentStatus" label="内容状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.contentStatus)"
              size="small"
            >
              {{ scope.row.contentStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
              icon="el-icon-edit"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleCopy(scope.row)"
              icon="el-icon-copy-document"
            >
              复制
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handlePreview(scope.row)"
              icon="el-icon-view"
            >
              预览
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDetail(scope.row)"
              icon="el-icon-document"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页导航 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background
        >
          <span class="pagination-info">
            当前第 {{ (pagination.currentPage - 1) * pagination.pageSize + 1 }}-{{
              Math.min(pagination.currentPage * pagination.pageSize, pagination.total)
            }} 条，共 {{ pagination.total }} 条数据
          </span>
        </el-pagination>
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog title="图文素材详情" :visible.sync="detailDialogVisible" width="70%">
      <div v-if="currentContent" class="content-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="内容标题" :span="2">{{ currentContent.contentTitle }}</el-descriptions-item>
          <el-descriptions-item label="内容ID">{{ currentContent.contentId }}</el-descriptions-item>
          <el-descriptions-item label="所属板块">{{ currentContent.categorySection }}</el-descriptions-item>
          <el-descriptions-item label="作者">{{ currentContent.author }}</el-descriptions-item>
          <el-descriptions-item label="内容状态">
            <el-tag :type="getStatusType(currentContent.contentStatus)">
              {{ currentContent.contentStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="阅读量">{{ formatNumber(currentContent.viewCount) }}</el-descriptions-item>
          <el-descriptions-item label="点赞量">{{ formatNumber(currentContent.likeCount) }}</el-descriptions-item>
          <el-descriptions-item label="发布时间">{{ formatDateTime(currentContent.publishTime) }}</el-descriptions-item>
          <el-descriptions-item label="文章描述" :span="2">
            <div class="description-content">{{ currentContent.articleDescription || '暂无描述' }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog title="内容预览" :visible.sync="previewDialogVisible" width="80%">
      <div v-if="currentContent" class="content-preview">
        <div class="preview-header">
          <h2>{{ currentContent.contentTitle }}</h2>
          <div class="preview-meta">
            <span class="meta-item">作者：{{ currentContent.author }}</span>
            <span class="meta-item">所属板块：{{ currentContent.categorySection }}</span>
            <span class="meta-item">发布时间：{{ formatDateTime(currentContent.publishTime) }}</span>
          </div>
        </div>
        <div class="preview-content">
          <p>{{ currentContent.articleDescription || '暂无内容描述' }}</p>
        </div>
        <div class="preview-stats">
          <span class="stat-item">阅读量：{{ formatNumber(currentContent.viewCount) }}</span>
          <span class="stat-item">点赞量：{{ formatNumber(currentContent.likeCount) }}</span>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 添加/编辑内容对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="addDialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        :model="contentForm"
        :rules="formRules"
        ref="contentForm"
        label-width="120px"
        size="small"
      >
        <el-form-item label="内容类型" prop="contentType" required>
          <el-select v-model="contentForm.contentType" placeholder="请选择内容类型" style="width: 200px;">
            <el-option label="文章" value="文章" />
          </el-select>
        </el-form-item>

        <el-form-item label="文章标题" prop="contentTitle" required>
          <el-input
            v-model="contentForm.contentTitle"
            placeholder="请输入标题，限50个字符"
            maxlength="50"
            show-word-limit
            style="width: 100%;"
          />
          <div class="char-count">{{ contentForm.contentTitle.length }}/50</div>
        </el-form-item>

        <el-form-item label="作者" prop="author">
          <el-input
            v-model="contentForm.author"
            placeholder="请输入作者"
            style="width: 200px;"
          />
          <el-select
            v-model="contentForm.authorType"
            placeholder="请选择"
            style="width: 150px; margin-left: 10px;"
          >
            <el-option label="平台运营" value="平台运营" />
          </el-select>
          <el-button type="text" style="margin-left: 10px;">选择</el-button>
        </el-form-item>

        <el-form-item label="文章标签" prop="tags">
          <el-input
            v-model="contentForm.tags"
            placeholder="请输入标签"
            style="width: 300px;"
          />
          <el-button type="text" style="margin-left: 10px;">选择</el-button>
        </el-form-item>

        <el-form-item label="所属板块" prop="categorySection" required>
          <el-select
            v-model="contentForm.categorySection"
            placeholder="请选择所属板块"
            style="width: 200px;"
          >
            <el-option label="医教" value="医教" />
            <el-option label="科普" value="科普" />
            <el-option label="康复指导" value="康复指导" />
            <el-option label="健康资讯" value="健康资讯" />
            <el-option label="疾病预防" value="疾病预防" />
          </el-select>
        </el-form-item>

        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker
            v-model="contentForm.publishTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 300px;"
          />
        </el-form-item>

        <el-form-item label="是否支持游戏模式" prop="supportGameMode">
          <el-radio-group v-model="contentForm.supportGameMode">
            <el-radio :label="1">支持</el-radio>
            <el-radio :label="0">不支持</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="是否原创" prop="isOriginal">
          <el-radio-group v-model="contentForm.isOriginal">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="文章描述" prop="articleDescription">
          <el-input
            v-model="contentForm.articleDescription"
            type="textarea"
            placeholder="请输入描述，用于文章摘要示"
            :rows="4"
            maxlength="300"
            show-word-limit
            style="width: 100%;"
          />
          <div class="char-count">{{ contentForm.articleDescription.length }}/300</div>
        </el-form-item>

        <el-form-item label="封面图" prop="coverImage">
          <div class="upload-container">
            <el-upload
              class="cover-uploader"
              action="/api/upload"
              :show-file-list="false"
              :on-success="handleCoverUploadSuccess"
              :before-upload="beforeCoverUpload"
              accept="image/*"
            >
              <div class="upload-box" v-if="!contentForm.coverImage">
                <i class="el-icon-plus"></i>
              </div>
              <img v-else :src="contentForm.coverImage" class="cover-image" />
            </el-upload>
            <div class="upload-tips">
              当前图片仅允许上传文件类型，最大3张图片，图片不超过370*225
            </div>
          </div>
        </el-form-item>

        <el-form-item label="文章来源" prop="contentSource">
          <el-radio-group v-model="contentForm.contentSource">
            <el-radio label="系统录入">系统录入</el-radio>
            <el-radio label="外链">外链</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="文章内容" prop="articleContent" v-if="contentForm.contentSource === '系统录入'">
          <div class="editor-container">
            <div class="editor-toolbar">
              <el-button-group>
                <el-button size="mini" icon="el-icon-picture">插入图片</el-button>
                <el-button size="mini" icon="el-icon-link">插入链接</el-button>
              </el-button-group>
              <select class="paragraph-select">
                <option value="Paragraph">Paragraph</option>
              </select>
            </div>
            <el-input
              v-model="contentForm.articleContent"
              type="textarea"
              placeholder="请输入文章内容"
              :rows="10"
              style="width: 100%;"
            />
          </div>
        </el-form-item>

        <el-form-item label="外链地址" prop="externalLink" v-if="contentForm.contentSource === '外链'">
          <el-input
            v-model="contentForm.externalLink"
            placeholder="请输入外链地址"
            style="width: 100%;"
          />
        </el-form-item>

        <el-form-item label="所属模块" prop="moduleCategory">
          <el-radio-group v-model="contentForm.moduleCategory">
            <el-radio label="医教">医教</el-radio>
            <el-radio label="直数">直数</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="点赞基数">
          <el-input
            v-model.number="contentForm.likeCountBase"
            placeholder="0"
            type="number"
            style="width: 150px;"
          >
            <template slot="append">人</template>
          </el-input>
        </el-form-item>

        <el-form-item label="阅读基数">
          <el-input
            v-model.number="contentForm.viewCountBase"
            placeholder="0"
            type="number"
            style="width: 150px;"
          >
            <template slot="append">人</template>
          </el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">返回</el-button>
        <el-button type="primary" @click="handleSubmit">返回并保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getContentList, getContentDetail, addContent, updateContent } from '@/api/education'

export default {
  name: 'ImageLibrary',
  data() {
    return {
      // 搜索表单
      searchForm: {
        publishDateRange: null,
        contentTitle: '',
        author: '',
        categorySection: '',
        contentStatus: ''
      },
      // 表格数据
      tableData: [],
      loading: false,
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      // 对话框显示状态
      detailDialogVisible: false,
      previewDialogVisible: false,
      addDialogVisible: false,
      // 当前操作的内容
      currentContent: null,
      // 添加/编辑表单
      contentForm: {
        contentType: '文章',
        contentTitle: '',
        author: '系统管理员',
        authorType: '平台运营',
        tags: '',
        publishTime: null,
        supportGameMode: 1,
        isOriginal: 1,
        articleDescription: '',
        coverImage: '',
        contentSource: '系统录入',
        articleContent: '',
        externalLink: '',
        moduleCategory: '医教',
        likeCountBase: 0,
        viewCountBase: 0,
        categorySection: '医教'
      },
      // 表单验证规则
      formRules: {
        contentType: [
          { required: true, message: '请选择内容类型', trigger: 'change' }
        ],
        contentTitle: [
          { required: true, message: '请输入文章标题', trigger: 'blur' },
          { max: 50, message: '标题长度不能超过50个字符', trigger: 'blur' }
        ],
        categorySection: [
          { required: true, message: '请选择所属板块', trigger: 'change' }
        ],
        author: [
          { required: true, message: '请输入作者', trigger: 'blur' }
        ],
        articleDescription: [
          { max: 300, message: '描述长度不能超过300个字符', trigger: 'blur' }
        ]
      },
      // 对话框标题
      dialogTitle: '添加内容',
      // 是否编辑模式
      isEdit: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          contentTitle: this.searchForm.contentTitle,
          author: this.searchForm.author,
          categorySection: this.searchForm.categorySection,
          contentStatus: this.searchForm.contentStatus,
          publishStartTime: this.searchForm.publishDateRange ? this.searchForm.publishDateRange[0] : null,
          publishEndTime: this.searchForm.publishDateRange ? this.searchForm.publishDateRange[1] : null
        }
        const response = await getContentList(params)
        this.tableData = response.data.records
        this.pagination.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败')
        console.error('获取数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 添加内容
    handleAdd() {
      this.dialogTitle = '新增内容'
      this.isEdit = false
      this.resetForm()
      this.addDialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑内容'
      this.isEdit = true
      this.currentContent = { ...row }
      // 填充表单数据
      this.contentForm = {
        id: row.id,
        contentType: row.contentType || '文章',
        contentTitle: row.contentTitle || '',
        author: row.author || '',
        authorType: row.authorType || '',
        tags: row.tags || '',
        publishTime: row.publishTime || null,
        supportGameMode: row.supportGameMode !== undefined ? row.supportGameMode : 1,
        isOriginal: row.isOriginal !== undefined ? row.isOriginal : 1,
        articleDescription: row.articleDescription || '',
        coverImage: row.coverImage || '',
        contentSource: row.contentSource || '系统录入',
        articleContent: row.articleContent || '',
        externalLink: row.externalLink || '',
        moduleCategory: row.moduleCategory || '',
        likeCountBase: row.likeCountBase || 0,
        viewCountBase: row.viewCountBase || 0,
        categorySection: row.categorySection || ''
      }
      this.addDialogVisible = true
    },
    // 复制
    handleCopy(row) {
      this.$message.info('复制功能开发中')
    },
    // 预览
    handlePreview(row) {
      this.currentContent = { ...row }
      this.previewDialogVisible = true
    },
    // 详情
    async handleDetail(row) {
      try {
        const response = await getContentDetail(row.id)
        this.currentContent = response.data
        this.detailDialogVisible = true
      } catch (error) {
        this.$message.error('获取详情失败')
        console.error('获取详情失败:', error)
      }
    },
    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchData()
    },
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '草稿': 'info',
        '待审核': 'warning',
        '已上架': 'success',
        '未上架': 'danger',
        '已下架': 'info'
      }
      return statusMap[status] || 'info'
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    },
    // 格式化数字
    formatNumber(num) {
      if (!num) return '0'
      return num.toString()
    },
    // 重置表单
    resetForm() {
      this.contentForm = {
        contentType: '文章',
        contentTitle: '',
        author: '系统管理员',
        authorType: '平台运营',
        tags: '',
        publishTime: null,
        supportGameMode: 1,
        isOriginal: 1,
        articleDescription: '',
        coverImage: '',
        contentSource: '系统录入',
        articleContent: '',
        externalLink: '',
        moduleCategory: '医教',
        likeCountBase: 0,
        viewCountBase: 0,
        categorySection: '医教'
      }
      if (this.$refs.contentForm) {
        this.$refs.contentForm.resetFields()
      }
    },
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    // 封面图上传成功
    handleCoverUploadSuccess(response) {
      if (response.code === 200) {
        this.contentForm.coverImage = response.data.url
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    // 封面图上传前验证
    beforeCoverUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }
      return true
    },
    // 提交表单
    handleSubmit() {
      this.$refs.contentForm.validate(async (valid) => {
        if (valid) {
          try {
            if (this.isEdit) {
              // 编辑模式
              await updateContent(this.contentForm)
              this.$message.success('修改成功')
            } else {
              // 新增模式
              await addContent(this.contentForm)
              this.$message.success('添加成功')
            }
            this.addDialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error(this.isEdit ? '修改失败' : '添加失败')
            console.error('提交失败:', error)
          }
        } else {
          this.$message.warning('请填写必填项')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.image-library-container {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #f5f5f5;
    border-radius: 4px;
  }

  .toolbar {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;

    .pagination-info {
      margin-right: 20px;
      color: #606266;
      font-size: 14px;
    }
  }

  .content-detail {
    padding: 20px 0;

    .description-content {
      line-height: 1.6;
      white-space: pre-wrap;
      word-break: break-word;
    }
  }

  .content-preview {
    .preview-header {
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 20px;
      margin-bottom: 20px;

      h2 {
        margin: 0 0 15px 0;
        color: #303133;
      }

      .preview-meta {
        .meta-item {
          margin-right: 20px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .preview-content {
      margin-bottom: 20px;
      line-height: 1.8;
      color: #606266;
    }

    .preview-stats {
      border-top: 1px solid #ebeef5;
      padding-top: 15px;

      .stat-item {
        margin-right: 20px;
        color: #909399;
        font-size: 14px;
      }
    }
  }

  // 添加内容对话框样式
  .char-count {
    text-align: right;
    color: #909399;
    font-size: 12px;
    margin-top: 5px;
  }

  .upload-container {
    .cover-uploader {
      .upload-box {
        width: 178px;
        height: 178px;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          border-color: #409eff;
        }

        i {
          font-size: 28px;
          color: #8c939d;
        }
      }

      .cover-image {
        width: 178px;
        height: 178px;
        display: block;
        object-fit: cover;
      }
    }

    .upload-tips {
      color: #909399;
      font-size: 12px;
      margin-top: 8px;
      line-height: 1.5;
    }
  }

  .editor-container {
    width: 100%;

    .editor-toolbar {
      border: 1px solid #dcdfe6;
      border-bottom: none;
      padding: 10px;
      background-color: #f5f7fa;
      display: flex;
      align-items: center;
      gap: 10px;

      .paragraph-select {
        padding: 5px 10px;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        background-color: white;
        cursor: pointer;
        font-size: 14px;

        &:focus {
          outline: none;
          border-color: #409eff;
        }
      }
    }

    ::v-deep .el-textarea {
      .el-textarea__inner {
        border-top-left-radius: 0;
        border-top-right-radius: 0;
      }
    }
  }
}

::v-deep .el-table {
  .el-table__header-wrapper {
    th {
      background-color: #f8f9fa;
      color: #333;
      font-weight: 600;
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

// 对话框样式优化
::v-deep .el-dialog__body {
  max-height: 600px;
  overflow-y: auto;
}
</style>