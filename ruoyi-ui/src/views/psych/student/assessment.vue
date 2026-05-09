<template>
  <div class="assessment-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-icon">
        <i class="el-icon-data-analysis"></i>
      </div>
      <div class="header-content">
        <h2>心理测评</h2>
        <p>完成专业量表测评，了解您的心理状态与风险等级</p>
      </div>
    </div>

    <!-- 主要内容区 -->
    <el-row :gutter="24">
      <!-- 量表列表 -->
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="scale-list-card">
          <div slot="header" class="card-header">
            <span class="header-title"><i class="el-icon-notebook-2"></i> 量表列表</span>
            <span class="scale-count">{{ scaleList.length }} 个量表</span>
          </div>

          <div class="scale-list">
            <div
              v-for="item in scaleList"
              :key="item.scaleId"
              class="scale-item"
              :class="{ active: activeScale && item.scaleId === activeScale.scaleId }"
              @click="loadScale(item.scaleId)"
            >
              <div class="scale-icon" :style="{ background: getScaleIconBg(item.scaleType) }">
                <i :class="getScaleIcon(item.scaleType)"></i>
              </div>
              <div class="scale-info">
                <div class="scale-name">{{ item.scaleName }}</div>
                <div class="scale-meta">
                  <el-tag size="mini" :type="getScaleTagType(item.scaleType)">{{ item.scaleType }}</el-tag>
                  <span class="question-count">{{ item.questionCount }} 题</span>
                </div>
                <div class="scale-desc">{{ item.description }}</div>
              </div>
              <div class="scale-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 量表作答 -->
      <el-col :xs="24" :lg="16">
        <el-card shadow="never" class="assessment-card">
          <div slot="header" class="card-header">
            <span class="header-title"><i class="el-icon-edit-outline"></i> 开始测评</span>
          </div>

          <div v-if="activeScale" class="assessment-content">
            <!-- 进度条 -->
            <div class="progress-section">
              <div class="progress-info">
                <span class="scale-title">{{ activeScale.scaleName }}</span>
                <span class="progress-text">第 {{ currentQuestionIndex + 1 }} / {{ activeScale.questions.length }} 题</span>
              </div>
              <el-progress
                :percentage="getProgressPercent()"
                :stroke-width="8"
                :show-text="false"
                color="linear-gradient(90deg, #f093fb, #f5576c)"
              />
            </div>

            <!-- 题目卡片 -->
            <div class="question-card" v-if="currentQuestion">
              <div class="question-number">第 {{ currentQuestionIndex + 1 }} 题</div>
              <div class="question-title">{{ currentQuestion.questionTitle }}</div>

              <div class="option-list">
                <div
                  v-for="(option, idx) in currentQuestion.options"
                  :key="option.optionId"
                  class="option-item"
                  :class="{ selected: answers[currentQuestion.questionId] === option.optionId }"
                  @click="selectOption(currentQuestion.questionId, option.optionId)"
                >
                  <div class="option-radio">
                    <span class="option-letter">{{ String.fromCharCode(65 + idx) }}</span>
                  </div>
                  <div class="option-text">{{ option.optionLabel }}</div>
                  <div class="option-score">{{ option.optionScore }}分</div>
                </div>
              </div>
            </div>

            <!-- 导航按钮 -->
            <div class="nav-buttons">
              <el-button
                :disabled="currentQuestionIndex === 0"
                @click="prevQuestion"
                class="nav-btn prev-btn"
              >
                <i class="el-icon-arrow-left"></i> 上一步
              </el-button>
              <span class="nav-hint">选择答案后点击下一步</span>
              <el-button
                v-if="currentQuestionIndex < activeScale.questions.length - 1"
                :disabled="!answers[currentQuestion.questionId]"
                @click="nextQuestion"
                class="nav-btn next-btn"
              >
                下一步 <i class="el-icon-arrow-right"></i>
              </el-button>
              <el-button
                v-else
                :disabled="!isAllAnswered"
                type="primary"
                @click="submitCurrentScale"
                class="nav-btn submit-btn"
              >
                <i class="el-icon-check"></i> 提交测评
              </el-button>
            </div>
          </div>

          <div v-else class="empty-state">
            <div class="empty-icon">
              <i class="el-icon-document"></i>
            </div>
            <p>请选择左侧的量表开始测评</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 历史测评记录 -->
    <el-card shadow="never" class="history-card">
      <div slot="header" class="card-header">
        <span class="header-title"><i class="el-icon-data-line"></i> 历史测评记录</span>
        <span class="record-count">{{ recordList.length }} 条记录</span>
      </div>

      <div v-if="recordList.length" class="history-grid">
        <div
          v-for="record in recordList"
          :key="record.recordId"
          class="history-item"
          @click="showRecordDetail(record)"
        >
          <div class="history-header">
            <span class="history-date">
              <i class="el-icon-calendar"></i>
              {{ parseTime(record.createTime) }}
            </span>
            <el-tag size="mini" :type="riskTagType(record.resultLevel)">
              {{ record.resultLevel }}
            </el-tag>
          </div>
          <div class="history-name">{{ record.scaleName }}</div>
          <div class="history-score">
            <el-progress
              :percentage="getScorePercent(record.totalScore, record.maxScore)"
              :stroke-width="6"
              :show-text="false"
              :color="getScoreColor(record.resultLevel)"
            />
            <span class="score-text">{{ record.totalScore }} / {{ record.maxScore || 100 }}</span>
          </div>
          <div class="history-summary">{{ record.resultSummary }}</div>
        </div>
      </div>

      <el-empty v-else description="暂无测评记录" :image-size="100"></el-empty>
    </el-card>

    <!-- 测评结果弹窗 -->
    <el-dialog :visible.sync="resultDialogVisible" title="测评结果" width="500px">
      <div v-if="currentResult" class="result-content">
        <div class="result-score">
          <el-progress
            type="circle"
            :percentage="getScorePercent(currentResult.totalScore, currentResult.maxScore)"
            :color="getScoreColor(currentResult.resultLevel)"
            :width="120"
          />
          <div class="score-info">
            <div class="score-label">您的得分</div>
            <div class="score-value">{{ currentResult.totalScore }} / {{ currentResult.maxScore || 100 }}</div>
          </div>
        </div>

        <el-divider></el-divider>

        <div class="result-level">
          <el-tag size="large" :type="riskTagType(currentResult.resultLevel)">
            {{ currentResult.resultLevel }}
          </el-tag>
        </div>

        <div class="result-summary">
          <div class="summary-title"><i class="el-icon-document"></i> 结果摘要</div>
          <div class="summary-text">{{ currentResult.resultSummary }}</div>
        </div>

        <div class="result-advice">
          <div class="advice-icon"><i class="el-icon-lightbulb"></i></div>
          <div class="advice-text">{{ currentResult.guidanceAdvice }}</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listScales, getScaleDetail, submitAssessment, listAssessmentRecords } from "@/api/psych/center"

export default {
  name: "PsychStudentAssessment",
  data() {
    return {
      scaleList: [],
      activeScale: null,
      answers: {},
      currentQuestionIndex: 0,
      recordLoading: false,
      recordList: [],
      resultDialogVisible: false,
      currentResult: null
    }
  },
  computed: {
    currentQuestion() {
      if (!this.activeScale || !this.activeScale.questions) return null
      return this.activeScale.questions[this.currentQuestionIndex]
    },
    isAllAnswered() {
      if (!this.activeScale || !this.activeScale.questions) return false
      return this.activeScale.questions.every(q => this.answers[q.questionId])
    }
  },
  created() {
    this.getScaleList()
    this.getRecordList()
  },
  methods: {
    getScaleList() {
      listScales({ status: "0", pageNum: 1, pageSize: 20 }).then(response => {
        this.scaleList = response.rows || []
        if (this.scaleList.length) {
          this.loadScale(this.scaleList[0].scaleId)
        }
      })
    },
    loadScale(scaleId) {
      getScaleDetail(scaleId).then(response => {
        this.activeScale = response.data
        this.answers = {}
        this.currentQuestionIndex = 0
      })
    },
    getRecordList() {
      this.recordLoading = true
      listAssessmentRecords({ pageNum: 1, pageSize: 20 }).then(response => {
        this.recordList = response.rows || []
        this.recordLoading = false
      })
    },
    selectOption(questionId, optionId) {
      this.$set(this.answers, questionId, optionId)
    },
    prevQuestion() {
      if (this.currentQuestionIndex > 0) {
        this.currentQuestionIndex--
      }
    },
    nextQuestion() {
      if (this.currentQuestionIndex < this.activeScale.questions.length - 1) {
        this.currentQuestionIndex++
      }
    },
    getProgressPercent() {
      if (!this.activeScale || !this.activeScale.questions) return 0
      const answered = this.activeScale.questions.filter(q => this.answers[q.questionId]).length
      return Math.round((answered / this.activeScale.questions.length) * 100)
    },
    submitCurrentScale() {
      if (!this.activeScale) return
      const payloadAnswers = []
      for (const question of this.activeScale.questions) {
        const optionId = this.answers[question.questionId]
        if (!optionId) {
          this.$modal.msgWarning("请完成全部题目后再提交")
          return
        }
        const option = question.options.find(item => item.optionId === optionId)
        payloadAnswers.push({
          questionId: question.questionId,
          optionId: option.optionId,
          optionScore: option.optionScore
        })
      }
      submitAssessment({ scaleId: this.activeScale.scaleId, answers: payloadAnswers }).then(response => {
        const data = response.data || {}
        this.currentResult = data
        this.resultDialogVisible = true
        this.getRecordList()
      })
    },
    showRecordDetail(record) {
      this.currentResult = record
      this.resultDialogVisible = true
    },
    getScaleIcon(type) {
      const map = {
        '抑郁': 'el-icon-sunny',
        '焦虑': 'el-icon-cloudy',
        '压力': 'el-icon-heavy-rain',
        '心理健康': 'el-icon-circle-check',
        '人格': 'el-icon-user',
        '情绪': 'el-icon-cold-drink'
      }
      return map[type] || 'el-icon-document'
    },
    getScaleIconBg(type) {
      const map = {
        '抑郁': 'linear-gradient(135deg, #667eea, #764ba2)',
        '焦虑': 'linear-gradient(135deg, #f093fb, #f5576c)',
        '压力': 'linear-gradient(135deg, #4facfe, #00f2fe)',
        '心理健康': 'linear-gradient(135deg, #43e97b, #38f9d7)',
        '人格': 'linear-gradient(135deg, #fa709a, #fee140)',
        '情绪': 'linear-gradient(135deg, #a8edea, #fed6e3)'
      }
      return map[type] || 'linear-gradient(135deg, #667eea, #764ba2)'
    },
    getScaleTagType(type) {
      const map = {
        '抑郁': 'purple',
        '焦虑': 'pink',
        '压力': 'cyan',
        '心理健康': 'success',
        '人格': 'warning',
        '情绪': 'info'
      }
      return map[type] || 'info'
    },
    riskTagType(level) {
      const map = { "正常": "success", "轻度": "info", "中度": "warning", "重度": "danger", "关注": "warning", "中风险": "warning", "高风险": "danger" }
      return map[level] || "info"
    },
    getScorePercent(score, maxScore) {
      // 如果没有maxScore，假设满分为100
      const max = maxScore || 100
      return Math.round((score / max) * 100)
    },
    getScoreColor(level) {
      const map = { "正常": "#67C23A", "轻度": "#909399", "中度": "#E6A23C", "重度": "#F56C6C", "关注": "#E6A23C", "中风险": "#F56C6C", "高风险": "#F56C6C" }
      return map[level] || "#409EFF"
    },
    parseTime(time) {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })
    }
  }
}
</script>

<style scoped>
.assessment-page {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: calc(100vh - 84px);
}

/* 页面标题 */
.page-header {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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
  color: #f5576c;
}

/* 量表列表卡片 */
.scale-list-card {
  border-radius: 16px;
  margin-bottom: 24px;
}

.scale-count {
  font-size: 12px;
  color: #909399;
}

.scale-list {
  max-height: 500px;
  overflow-y: auto;
}

.scale-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
  margin-bottom: 12px;
  background: #f8f9fa;
}

.scale-item:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.scale-item.active {
  border-color: #f5576c;
  background: linear-gradient(135deg, rgba(240, 147, 251, 0.1), rgba(245, 87, 108, 0.1));
}

.scale-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #fff;
  margin-right: 14px;
  flex-shrink: 0;
}

.scale-info {
  flex: 1;
  min-width: 0;
}

.scale-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 6px;
  font-size: 15px;
}

.scale-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.question-count {
  font-size: 12px;
  color: #909399;
}

.scale-desc {
  font-size: 12px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.scale-arrow {
  color: #c0c4cc;
  font-size: 16px;
  transition: transform 0.3s;
}

.scale-item:hover .scale-arrow {
  transform: translateX(4px);
}

/* 答题卡片 */
.assessment-card {
  border-radius: 16px;
  margin-bottom: 24px;
}

.progress-section {
  margin-bottom: 24px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.scale-title {
  font-weight: 600;
  color: #303133;
  font-size: 15px;
}

.progress-text {
  font-size: 13px;
  color: #909399;
}

.question-card {
  background: #f8f9fa;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
}

.question-number {
  font-size: 12px;
  color: #f5576c;
  font-weight: 600;
  margin-bottom: 12px;
}

.question-title {
  font-size: 17px;
  color: #303133;
  line-height: 1.7;
  margin-bottom: 20px;
  font-weight: 500;
}

.option-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.option-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
  border: 2px solid #ebeef5;
  cursor: pointer;
  transition: all 0.3s;
}

.option-item:hover {
  border-color: #f5576c;
  transform: translateX(4px);
}

.option-item.selected {
  border-color: #f5576c;
  background: linear-gradient(135deg, rgba(240, 147, 251, 0.1), rgba(245, 87, 108, 0.1));
}

.option-radio {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 14px;
  flex-shrink: 0;
  transition: all 0.3s;
}

.option-item.selected .option-radio {
  background: linear-gradient(135deg, #f093fb, #f5576c);
  color: #fff;
}

.option-letter {
  font-weight: 600;
  font-size: 14px;
}

.option-text {
  flex: 1;
  color: #303133;
  font-size: 14px;
}

.option-score {
  font-size: 12px;
  color: #909399;
}

/* 导航按钮 */
.nav-buttons {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-btn {
  border-radius: 24px;
  padding: 12px 24px;
}

.prev-btn {
  border-radius: 24px;
}

.next-btn {
  background: linear-gradient(135deg, #f093fb, #f5576c);
  border: none;
  color: #fff;
}

.next-btn:hover {
  opacity: 0.9;
}

.submit-btn {
  background: linear-gradient(135deg, #f093fb, #f5576c);
  border: none;
  border-radius: 24px;
}

.nav-hint {
  font-size: 12px;
  color: #c0c4cc;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #f093fb, #f5576c);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 36px;
  color: #fff;
  opacity: 0.8;
}

.empty-state p {
  color: #909399;
  margin: 0;
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
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
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

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.history-date {
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

.history-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  font-size: 14px;
}

.history-score {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.score-text {
  font-size: 12px;
  color: #606266;
  white-space: nowrap;
}

.history-summary {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 结果弹窗 */
.result-content {
  padding: 10px 0;
}

.result-score {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 30px;
  padding: 20px 0;
}

.score-info {
  text-align: left;
}

.score-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}

.score-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.result-level {
  text-align: center;
  margin-bottom: 20px;
}

.result-summary {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
}

.summary-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #606266;
  margin-bottom: 8px;
}

.summary-text {
  font-size: 14px;
  color: #303133;
  line-height: 1.7;
}

.result-advice {
  background: linear-gradient(135deg, #f093fb, #f5576c);
  border-radius: 12px;
  padding: 16px;
  display: flex;
  gap: 12px;
  color: #fff;
}

.advice-icon {
  font-size: 20px;
  flex-shrink: 0;
}

.advice-text {
  font-size: 13px;
  line-height: 1.6;
  opacity: 0.95;
}

/* 滚动条 */
.scale-list::-webkit-scrollbar {
  width: 4px;
}

.scale-list::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 2px;
}
</style>
