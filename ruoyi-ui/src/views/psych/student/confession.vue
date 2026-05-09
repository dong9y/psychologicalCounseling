<template>
  <div class="confession-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-icon">
        <i class="el-icon-edit-outline"></i>
      </div>
      <div class="header-content">
        <h2>心理倾诉</h2>
        <p>倾诉是释放情绪、获得专业指导的重要渠道。您的隐私将得到保护。</p>
      </div>
      <el-button type="text" class="header-tip">
        了解更多 <i class="el-icon-arrow-right"></i>
      </el-button>
    </div>

    <!-- 主要内容区 -->
    <el-row :gutter="24">
      <!-- 倾诉表单 -->
      <el-col :xs="24" :lg="11">
        <el-card shadow="never" class="form-card">
          <div slot="header" class="card-header">
            <span class="header-title"><i class="el-icon-edit"></i> 倾诉表单</span>
          </div>

          <el-form label-position="top">
            <!-- 匿名模式 -->
            <el-form-item>
              <div class="anonymous-toggle">
                <div class="toggle-info">
                  <span class="toggle-label">匿名模式</span>
                  <span class="toggle-desc">开启后咨询师默认看不到您的身份信息</span>
                </div>
                <el-switch
                  v-model="form.anonymousFlag"
                  :active-value="1"
                  :inactive-value="0"
                  active-color="#667eea"
                />
              </div>
            </el-form-item>

            <!-- 心情标签 -->
            <el-form-item label="选择您当前的心情标签（可选）">
              <div class="mood-tags">
                <el-tag
                  v-for="tag in moodTags"
                  :key="tag.name"
                  :type="tag.type"
                  :class="{ 'tag-selected': selectedTags.includes(tag.name) }"
                  @click="toggleTag(tag.name)"
                >
                  <i :class="tag.icon"></i> {{ tag.name }}
                </el-tag>
              </div>
            </el-form-item>

            <!-- 倾诉内容 -->
            <el-form-item label="倾诉内容">
              <div class="textarea-wrapper" :class="{ 'is-focus': textareaFocused }">
                <el-input
                  v-model="form.text"
                  type="textarea"
                  :rows="8"
                  maxlength="500"
                  show-word-limit
                  placeholder="请描述最近让你困扰的情绪、事件或压力来源..."
                  @focus="textareaFocused = true"
                  @blur="textareaFocused = false"
                />
              </div>
            </el-form-item>

            <!-- 提交按钮 -->
            <div class="form-actions">
              <el-button type="primary" class="submit-btn" @click="handleSubmit" :loading="submitting">
                <i class="el-icon-s-promotion"></i>
                {{ submitting ? '分析中...' : '提交分析' }}
              </el-button>
              <el-button class="reset-btn" @click="resetForm">
                <i class="el-icon-refresh"></i> 重置
              </el-button>
            </div>
          </el-form>
        </el-card>
      </el-col>

      <!-- 分析结果 -->
      <el-col :xs="24" :lg="13">
        <el-card shadow="never" class="result-card">
          <div slot="header" class="card-header">
            <span class="header-title"><i class="el-icon-data-analysis"></i> 分析结果</span>
            <el-tag v-if="result" :type="riskTagType(result.riskLevel)" size="small">
              {{ result.riskLevel || '正常' }}
            </el-tag>
          </div>

          <div v-if="result" class="result-content">
            <!-- 风险仪表盘 -->
            <div class="risk-gauge">
              <div class="gauge-chart" ref="gaugeChart"></div>
              <div class="gauge-center">
                <div class="gauge-score">{{ result.emotionScore || 75 }}</div>
                <div class="gauge-label">情绪指数</div>
              </div>
            </div>

            <!-- 基本指标 -->
            <el-row :gutter="16" class="metric-row">
              <el-col :span="12">
                <div class="metric-item">
                  <span class="metric-label">主情绪</span>
                  <span class="metric-value primary">{{ result.primaryEmotion || '未知' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="metric-item">
                  <span class="metric-label">置信度</span>
                  <span class="metric-value">{{ result.confidence || '0%' }}</span>
                </div>
              </el-col>
            </el-row>

            <!-- 情绪标签 -->
            <div class="result-tags">
              <el-tag
                v-for="tag in parseTags(result.emotionTags)"
                :key="tag"
                type="info"
                effect="plain"
              >
                {{ tag }}
              </el-tag>
            </div>

            <!-- 分析摘要 -->
            <div class="result-section">
              <div class="section-title"><i class="el-icon-document"></i> 分析摘要</div>
              <div class="section-content">{{ result.analysisSummary || '暂无' }}</div>
            </div>

            <!-- 疏导建议 -->
            <div class="advice-card">
              <div class="advice-icon"><i class="el-icon-lightbulb"></i></div>
              <div class="advice-content">
                <div class="advice-title">疏导建议</div>
                <div class="advice-text">{{ result.guidanceAdvice || '暂无建议' }}</div>
              </div>
            </div>
          </div>

          <div v-else class="result-empty">
            <div class="empty-illustration">
              <i class="el-icon-chat-dot-round"></i>
            </div>
            <p>提交倾诉内容后，这里将展示情感分析结果</p>
            <div class="empty-hint">
              <span><i class="el-icon-time"></i> 平均分析时间：10-30秒</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 历史记录 -->
    <el-card shadow="never" class="history-card">
      <div slot="header" class="card-header">
        <span class="header-title"><i class="el-icon-time"></i> 历史倾诉记录</span>
        <span class="record-count">共 {{ recordList.length }} 条</span>
      </div>

      <div v-if="recordList.length" class="history-grid">
        <div
          v-for="record in recordList"
          :key="record.id"
          class="history-item"
          :class="getRiskClass(record.riskLevel)"
          @click="showRecordDetail(record)"
        >
          <div class="history-date">
            <i class="el-icon-calendar"></i>
            {{ parseTime(record.createTime) }}
          </div>
          <div class="history-emotion">
            <i :class="getEmotionIcon(record.primaryEmotion)"></i>
            {{ record.primaryEmotion || '未知' }}
          </div>
          <div class="history-summary">{{ record.analysisSummary || '暂无摘要' }}</div>
          <div class="history-footer">
            <el-tag :type="riskTagType(record.riskLevel)" size="mini">
              {{ record.riskLevel || '正常' }}
            </el-tag>
            <span v-if="record.anonymousFlag === 1" class="anonymous-badge">
              <i class="el-icon-user-solid"></i> 匿名
            </span>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无倾诉记录" :image-size="100"></el-empty>
    </el-card>

    <!-- 记录详情弹窗 -->
    <el-dialog :visible.sync="dialogVisible" title="倾诉详情" width="600px">
      <div v-if="selectedRecord" class="record-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="倾诉时间">{{ parseTime(selectedRecord.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="riskTagType(selectedRecord.riskLevel)">{{ selectedRecord.riskLevel }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="主情绪">{{ selectedRecord.primaryEmotion }}</el-descriptions-item>
          <el-descriptions-item label="情绪得分">{{ selectedRecord.emotionScore }}</el-descriptions-item>
          <el-descriptions-item label="倾诉内容" :span="2">{{ selectedRecord.text }}</el-descriptions-item>
          <el-descriptions-item label="分析摘要" :span="2">{{ selectedRecord.analysisSummary }}</el-descriptions-item>
          <el-descriptions-item label="疏导建议" :span="2">{{ selectedRecord.guidanceAdvice }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { submitEmotion, listEmotionRecords } from "@/api/psych/center"
import * as echarts from 'echarts'

export default {
  name: "PsychStudentConfession",
  data() {
    return {
      loading: false,
      submitting: false,
      result: null,
      recordList: [],
      textareaFocused: false,
      dialogVisible: false,
      selectedRecord: null,
      form: {
        text: "",
        anonymousFlag: 0
      },
      moodTags: [
        { name: '焦虑', type: 'warning', icon: 'el-icon-warning' },
        { name: '压力', type: 'danger', icon: 'el-icon-minus' },
        { name: '迷茫', type: 'info', icon: 'el-icon-question' },
        { name: '低落', type: 'warning', icon: 'el-icon-bottom' },
        { name: '烦躁', type: 'danger', icon: 'el-icon-close' },
        { name: '失眠', type: 'danger', icon: 'el-icon-moon' },
        { name: '孤独', type: 'info', icon: 'el-icon-user' },
        { name: '恐惧', type: 'danger', icon: 'el-icon-warning-outline' }
      ],
      selectedTags: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listEmotionRecords({ pageNum: 1, pageSize: 20 }).then(response => {
        this.recordList = response.rows || []
        this.loading = false
      })
    },
    handleSubmit() {
      if (!this.form.text || !this.form.text.trim()) {
        this.$modal.msgWarning("请输入倾诉内容")
        return
      }
      this.submitting = true
      const submitData = {
        ...this.form,
        emotionTags: this.selectedTags.join(',')
      }
      submitEmotion(submitData).then(response => {
        this.result = response.data
        this.$modal.msgSuccess("分析完成")
        this.submitting = false
        this.getList()
        this.initGaugeChart(response.data.emotionScore || 75)
      }).catch(() => {
        this.submitting = false
      })
    },
    resetForm() {
      this.form = { text: "", anonymousFlag: 0 }
      this.result = null
      this.selectedTags = []
    },
    toggleTag(tag) {
      const index = this.selectedTags.indexOf(tag)
      if (index > -1) {
        this.selectedTags.splice(index, 1)
      } else {
        this.selectedTags.push(tag)
      }
    },
    parseTags(tags) {
      if (!tags) return []
      return tags.split(',').filter(t => t.trim())
    },
    riskTagType(level) {
      const map = { "正常": "success", "关注": "warning", "中风险": "warning", "高风险": "danger" }
      return map[level] || "info"
    },
    getRiskClass(level) {
      const map = { "正常": "risk-normal", "关注": "risk-attention", "中风险": "risk-medium", "高风险": "risk-high" }
      return map[level] || "risk-normal"
    },
    getEmotionIcon(emotion) {
      const map = {
        '焦虑': 'el-icon-warning',
        '压力': 'el-icon-minus',
        '迷茫': 'el-icon-question',
        '低落': 'el-icon-bottom',
        '正常': 'el-icon-check',
        '平静': 'el-icon-check'
      }
      return map[emotion] || 'el-icon-info'
    },
    parseTime(time) {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })
    },
    showRecordDetail(record) {
      this.selectedRecord = record
      this.dialogVisible = true
    },
    initGaugeChart(score) {
      this.$nextTick(() => {
        if (!this.$refs.gaugeChart) return
        const chart = echarts.init(this.$refs.gaugeChart)
        const option = {
          series: [{
            type: 'gauge',
            startAngle: 200,
            endAngle: -20,
            min: 0,
            max: 100,
            splitNumber: 5,
            radius: '90%',
            center: ['50%', '60%'],
            axisLine: {
              lineStyle: {
                width: 15,
                color: [
                  [0.3, '#67C23A'],
                  [0.7, '#E6A23C'],
                  [1, '#F56C6C']
                ]
              }
            },
            pointer: {
              itemStyle: {
                color: '#303133'
              },
              width: 4,
              length: '60%'
            },
            axisTick: {
              distance: -20,
              length: 8,
              lineStyle: {
                color: '#fff',
                width: 2
              }
            },
            splitLine: {
              distance: -20,
              length: 15,
              lineStyle: {
                color: '#fff',
                width: 3
              }
            },
            axisLabel: {
              color: '#909399',
              distance: 25,
              fontSize: 12
            },
            detail: {
              show: false
            },
            data: [{ value: score }]
          }]
        }
        chart.setOption(option)
      })
    }
  }
}
</script>

<style scoped>
.confession-page {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: calc(100vh - 84px);
}

/* 页面标题 */
.page-header {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 24px 30px;
  margin-bottom: 24px;
  color: #fff;
}

.header-icon {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  margin-right: 20px;
}

.header-content {
  flex: 1;
}

.header-content h2 {
  margin: 0 0 8px;
  font-size: 22px;
  font-weight: 600;
}

.header-content p {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

.header-tip {
  color: #fff;
  opacity: 0.9;
}

/* 卡片通用样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #303133;
}

.header-title i {
  color: #667eea;
}

/* 表单卡片 */
.form-card {
  border-radius: 16px;
  margin-bottom: 24px;
}

.anonymous-toggle {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f8f9fa;
  border-radius: 10px;
}

.toggle-info {
  display: flex;
  flex-direction: column;
}

.toggle-label {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.toggle-desc {
  font-size: 12px;
  color: #909399;
}

/* 心情标签 */
.mood-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.mood-tags .el-tag {
  cursor: pointer;
  transition: all 0.3s;
  padding: 8px 14px;
  border-radius: 20px;
}

.mood-tags .el-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.mood-tags .tag-selected {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: transparent;
}

/* 输入框 */
.textarea-wrapper {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
}

.textarea-wrapper.is-focus {
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}

/* 提交按钮 */
.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

.submit-btn {
  flex: 1;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 24px;
  font-size: 16px;
  transition: all 0.3s;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.reset-btn {
  border-radius: 24px;
  width: 100px;
}

/* 结果卡片 */
.result-card {
  border-radius: 16px;
  margin-bottom: 24px;
}

.result-content {
  padding: 10px 0;
}

/* 风险仪表盘 */
.risk-gauge {
  position: relative;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.gauge-chart {
  width: 100%;
  height: 100%;
}

.gauge-center {
  position: absolute;
  text-align: center;
}

.gauge-score {
  font-size: 42px;
  font-weight: 700;
  color: #303133;
  line-height: 1;
}

.gauge-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

/* 指标行 */
.metric-row {
  margin: 20px 0;
}

.metric-item {
  background: #f8f9fa;
  padding: 14px 16px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
}

.metric-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 6px;
}

.metric-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.metric-value.primary {
  color: #667eea;
}

/* 结果标签 */
.result-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

/* 分析摘要 */
.result-section {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 10px;
  margin-bottom: 16px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #606266;
  margin-bottom: 8px;
}

.section-content {
  font-size: 14px;
  color: #303133;
  line-height: 1.7;
}

/* 疏导建议 */
.advice-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 16px;
  display: flex;
  gap: 14px;
  color: #fff;
}

.advice-icon {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.advice-title {
  font-weight: 600;
  margin-bottom: 6px;
  font-size: 14px;
}

.advice-text {
  font-size: 13px;
  opacity: 0.95;
  line-height: 1.6;
}

/* 空状态 */
.result-empty {
  text-align: center;
  padding: 40px 20px;
}

.empty-illustration {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 36px;
  color: #fff;
  opacity: 0.8;
}

.result-empty p {
  color: #909399;
  margin: 0 0 16px;
}

.empty-hint {
  display: flex;
  justify-content: center;
  gap: 20px;
  color: #c0c4cc;
  font-size: 12px;
}

.empty-hint span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 历史记录 */
.history-card {
  border-radius: 16px;
}

.record-count {
  font-size: 12px;
  color: #909399;
}

.history-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.history-item {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s;
  border-left: 4px solid #67C23A;
}

.history-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.history-item.risk-attention {
  border-left-color: #E6A23C;
}

.history-item.risk-medium {
  border-left-color: #F56C6C;
}

.history-item.risk-high {
  border-left-color: #F56C6C;
  background: #fff5f5;
}

.history-date {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.history-emotion {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.history-summary {
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.history-footer {
  display: flex;
  align-items: center;
  gap: 8px;
}

.anonymous-badge {
  font-size: 11px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 2px;
}

/* 详情弹窗 */
.record-detail {
  padding: 10px 0;
}
</style>
