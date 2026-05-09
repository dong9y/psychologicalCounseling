<template>
  <div class="student-dashboard">
    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-card-icon">
            <i class="el-icon-brain"></i>
          </div>
          <div class="stat-card-content">
            <div class="stat-label">当前心理状态</div>
            <div class="stat-value">{{ dashboard.latestRiskLevel || '正常' }}</div>
            <el-progress
              :percentage="getRiskPercentage(dashboard.latestRiskLevel)"
              :stroke-width="6"
              :show-text="false"
              :color="getRiskColor(dashboard.latestRiskLevel)"
            />
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-success">
          <div class="stat-card-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
          <div class="stat-card-content">
            <div class="stat-label">测评完成率</div>
            <div class="stat-value">{{ getAssessmentPercent() }}%</div>
            <el-progress
              :percentage="getAssessmentPercent()"
              :stroke-width="6"
              :show-text="false"
              color="#67C23A"
            />
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-warning">
          <div class="stat-card-icon">
            <i class="el-icon-calendar"></i>
          </div>
          <div class="stat-card-content">
            <div class="stat-label">待处理预约</div>
            <div class="stat-value">{{ dashboard.pendingAppointmentCount || 0 }}</div>
            <div class="stat-sub">已完成 {{ dashboard.completedAppointmentCount || 0 }} 次咨询</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-info">
          <div class="stat-card-icon">
            <i class="el-icon-bell"></i>
            <span v-if="dashboard.unreadMessageCount > 0" class="msg-badge">{{ dashboard.unreadMessageCount }}</span>
          </div>
          <div class="stat-card-content">
            <div class="stat-label">未读通知</div>
            <div class="stat-value">{{ dashboard.unreadMessageCount || 0 }}</div>
            <div class="stat-sub">{{ dashboard.totalMessageCount || 0 }} 条消息</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="chart-header">
            <span><i class="el-icon trend-icon"></i> 情绪趋势</span>
            <el-radio-group v-model="emotionDays" size="small">
              <el-radio-button label="7">7天</el-radio-button>
              <el-radio-button label="30">30天</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-container">
            <LineChart :chartData="emotionChartData" height="280px" />
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="chart-header">
            <span><i class="el-icon pie-icon"></i> 风险等级分布</span>
          </div>
          <div class="chart-container pie-container">
            <PieChart :chartData="riskChartData" height="280px" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 -->
    <el-card shadow="never" class="entry-card">
      <div slot="header" class="entry-header">
        <span><i class="el-icon-menu"></i> 快捷入口</span>
      </div>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :lg="6">
          <div class="entry-item entry-confession" @click="$router.push('/psych/student/confession')">
            <div class="entry-icon">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="entry-info">
              <h4>心理倾诉</h4>
              <p>提交文本困扰，获取情绪分析</p>
            </div>
            <i class="el-icon-arrow-right entry-arrow"></i>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6">
          <div class="entry-item entry-assessment" @click="$router.push('/psych/student/assessment')">
            <div class="entry-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="entry-info">
              <h4>心理测评</h4>
              <p>完成量表作答，查看评估结果</p>
            </div>
            <i class="el-icon-arrow-right entry-arrow"></i>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6">
          <div class="entry-item entry-appointment" @click="$router.push('/psych/student/appointment')">
            <div class="entry-icon">
              <i class="el-icon-date"></i>
            </div>
            <div class="entry-info">
              <h4>咨询预约</h4>
              <p>选择咨询师，提交预约申请</p>
            </div>
            <i class="el-icon-arrow-right entry-arrow"></i>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6">
          <div class="entry-item entry-profile" @click="$router.push('/psych/student/profile')">
            <div class="entry-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="entry-info">
              <h4>个人中心</h4>
              <p>查看历史记录与通知消息</p>
            </div>
            <i class="el-icon-arrow-right entry-arrow"></i>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 最近动态 -->
    <el-row :gutter="20" class="news-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="news-card">
          <div slot="header" class="news-header">
            <span><i class="el-icon-document"></i> 最近情感分析</span>
            <el-button type="text" @click="$router.push('/psych/student/confession')">去倾诉</el-button>
          </div>
          <div v-if="dashboard.latestEmotionRecord" class="emotion-detail">
            <div class="emotion-tags">
              <el-tag :type="getRiskTagType(dashboard.latestEmotionRecord.riskLevel)">
                {{ dashboard.latestEmotionRecord.riskLevel || '正常' }}
              </el-tag>
              <el-tag type="info" v-for="tag in parseTags(dashboard.latestEmotionRecord.emotionTags)" :key="tag">
                {{ tag }}
              </el-tag>
            </div>
            <div class="emotion-summary">
              <div class="summary-item">
                <span class="label">主情绪：</span>
                <span class="value">{{ dashboard.latestEmotionRecord.primaryEmotion || '-' }}</span>
              </div>
              <div class="summary-item">
                <span class="label">置信度：</span>
                <span class="value">{{ dashboard.latestEmotionRecord.confidence || '-' }}</span>
              </div>
            </div>
            <div class="emotion-advice">
              <i class="el-icon-lightbulb"></i>
              <span>{{ dashboard.latestEmotionRecord.guidanceAdvice || '暂无建议' }}</span>
            </div>
            <div class="emotion-time">
              {{ parseTime(dashboard.latestEmotionRecord.createTime) }}
            </div>
          </div>
          <el-empty v-else description="暂无倾诉记录" :image-size="80"></el-empty>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="news-card">
          <div slot="header" class="news-header">
            <span><i class="el-icon-bell"></i> 最近通知</span>
            <el-button type="text" @click="$router.push('/psych/student/profile')">查看全部</el-button>
          </div>
          <div v-if="dashboard.recentMessages && dashboard.recentMessages.length" class="message-list">
            <div v-for="item in dashboard.recentMessages" :key="item.messageId" class="message-item">
              <div class="message-badge" :class="getMessageBadgeClass(item)"></div>
              <div class="message-content">
                <div class="message-title">{{ item.title }}</div>
                <div class="message-text">{{ item.content }}</div>
                <div class="message-time">{{ parseTime(item.createTime) }}</div>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无通知消息" :image-size="80"></el-empty>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStudentDashboard } from "@/api/psych/center"
import LineChart from "@/views/dashboard/LineChart"
import PieChart from "@/views/dashboard/PieChart"

export default {
  name: "PsychStudentIndex",
  components: { LineChart, PieChart },
  data() {
    return {
      dashboard: {
        latestEmotionRecord: null,
        recentMessages: [],
        latestRiskLevel: '正常',
        recentAssessmentCount: 3,
        totalAssessmentCount: 5,
        pendingAppointmentCount: 1,
        completedAppointmentCount: 2,
        unreadMessageCount: 2,
        totalMessageCount: 8
      },
      emotionDays: '7',
      // 模拟7天数据（实际应从API获取）
      emotionHistory: {
        '7': {
          xData: ['今天', '昨天', '前天', '3天前', '4天前', '5天前', '6天前'],
          expectedData: [78, 72, 85, 68, 75, 82, 70],
          actualData: [75, 70, 80, 72, 78, 75, 68]
        },
        '30': {
          xData: Array.from({ length: 30 }, (_, i) => `${i + 1}天前`).reverse(),
          expectedData: Array.from({ length: 30 }, () => Math.floor(Math.random() * 30) + 60),
          actualData: Array.from({ length: 30 }, () => Math.floor(Math.random() * 30) + 55)
        }
      }
    }
  },
  computed: {
    emotionChartData() {
      const data = this.emotionHistory[this.emotionDays] || this.emotionHistory['7']
      return {
        xData: data.xData,
        expectedData: data.expectedData,
        actualData: data.actualData
      }
    },
    riskChartData() {
      const level = this.dashboard.latestRiskLevel || '正常'
      let normal = 70, attention = 20, mediumRisk = 8, highRisk = 2
      if (level === '关注') {
        normal = 60; attention = 30; mediumRisk = 7; highRisk = 3
      } else if (level === '中风险') {
        normal = 50; attention = 25; mediumRisk = 20; highRisk = 5
      } else if (level === '高风险') {
        normal = 40; attention = 20; mediumRisk = 25; highRisk = 15
      }
      return {
        legendData: ['正常', '关注', '中风险', '高风险'],
        seriesData: [
          { value: normal, name: '正常' },
          { value: attention, name: '关注' },
          { value: mediumRisk, name: '中风险' },
          { value: highRisk, name: '高风险' }
        ]
      }
    }
  },
  created() {
    this.getDashboard()
  },
  methods: {
    getDashboard() {
      getStudentDashboard().then(response => {
        if (response.data) {
          this.dashboard = {
            ...this.dashboard,
            ...response.data
          }
        }
      }).catch(() => {
        // API不可用时使用模拟数据
      })
    },
    getRiskPercentage(level) {
      const map = { '正常': 90, '关注': 70, '中风险': 50, '高风险': 30 }
      return map[level] || 90
    },
    getRiskColor(level) {
      const map = { '正常': '#67C23A', '关注': '#E6A23C', '中风险': '#F56C6C', '高风险': '#F56C6C' }
      return map[level] || '#67C23A'
    },
    getRiskTagType(level) {
      const map = { '正常': 'success', '关注': 'warning', '中风险': 'warning', '高风险': 'danger' }
      return map[level] || 'success'
    },
    getAssessmentPercent() {
      const total = this.dashboard.totalAssessmentCount || 0
      const recent = this.dashboard.recentAssessmentCount || 0
      if (total === 0) return 0
      return Math.round((recent / total) * 100)
    },
    parseTags(tags) {
      if (!tags) return []
      return tags.split(',').filter(t => t.trim())
    },
    getMessageBadgeClass(item) {
      if (item.messageType === 'warning') return 'badge-danger'
      if (item.messageType === 'appointment') return 'badge-success'
      return 'badge-info'
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
.student-dashboard {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: calc(100vh - 84px);
}

/* 统计卡片 */
.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  color: #fff;
  transition: transform 0.3s, box-shadow 0.3s;
  min-height: 130px;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.stat-card-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card-success {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-card-warning {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-card-info {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-card-icon {
  font-size: 48px;
  opacity: 0.9;
  margin-right: 16px;
  position: relative;
}

.msg-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #fff;
  color: #f5576c;
  border-radius: 50%;
  width: 22px;
  height: 22px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.stat-card-content {
  flex: 1;
}

.stat-label {
  font-size: 13px;
  opacity: 0.85;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-sub {
  font-size: 12px;
  opacity: 0.75;
}

.el-progress {
  margin-top: 4px;
}

/* 图表区域 */
.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 16px;
  overflow: hidden;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

.chart-header span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.trend-icon {
  color: #409EFF;
}

.pie-icon {
  color: #67C23A;
}

.chart-container {
  padding-top: 10px;
}

.pie-container {
  display: flex;
  justify-content: center;
}

/* 快捷入口 */
.entry-card {
  border-radius: 16px;
  margin-bottom: 20px;
}

.entry-header {
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.entry-item {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 12px;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.entry-item:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.entry-confession:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: #667eea;
}

.entry-assessment:hover {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: #fff;
  border-color: #f5576c;
}

.entry-appointment:hover {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #fff;
  border-color: #00f2fe;
}

.entry-profile:hover {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: #fff;
  border-color: #43e97b;
}

.entry-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
  background: rgba(255, 255, 255, 0.9);
  color: #409EFF;
}

.entry-confession:hover .entry-icon,
.entry-assessment:hover .entry-icon,
.entry-appointment:hover .entry-icon,
.entry-profile:hover .entry-icon {
  background: rgba(255, 255, 255, 0.3);
  color: #fff;
}

.entry-info {
  flex: 1;
}

.entry-info h4 {
  margin: 0 0 4px;
  font-size: 15px;
  color: #303133;
}

.entry-item:hover .entry-info h4 {
  color: #fff;
}

.entry-info p {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.entry-item:hover .entry-info p {
  color: rgba(255, 255, 255, 0.85);
}

.entry-arrow {
  font-size: 18px;
  color: #c0c4cc;
  transition: transform 0.3s;
}

.entry-item:hover .entry-arrow {
  transform: translateX(4px);
  color: #fff;
}

/* 最近动态 */
.news-row {
  margin-bottom: 20px;
}

.news-card {
  border-radius: 16px;
  height: 100%;
}

.news-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

.news-header span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.emotion-detail {
  padding: 10px 0;
}

.emotion-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.emotion-summary {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 16px;
}

.summary-item {
  display: flex;
  align-items: center;
}

.summary-item .label {
  color: #909399;
  font-size: 13px;
}

.summary-item .value {
  color: #303133;
  font-weight: 600;
  font-size: 14px;
}

.emotion-advice {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  padding: 12px 16px;
  border-radius: 8px;
  display: flex;
  align-items: flex-start;
  gap: 10px;
  color: #0369a1;
  font-size: 13px;
  line-height: 1.6;
  margin-bottom: 12px;
}

.emotion-advice i {
  color: #f59e0b;
  margin-top: 2px;
}

.emotion-time {
  color: #c0c4cc;
  font-size: 12px;
  text-align: right;
}

.message-list {
  max-height: 280px;
  overflow-y: auto;
}

.message-item {
  display: flex;
  align-items: flex-start;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
}

.message-item:last-child {
  border-bottom: none;
}

.message-badge {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 12px;
  margin-top: 6px;
  flex-shrink: 0;
}

.badge-danger {
  background: #F56C6C;
}

.badge-success {
  background: #67C23A;
}

.badge-info {
  background: #409EFF;
}

.message-content {
  flex: 1;
}

.message-title {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
  font-size: 14px;
}

.message-text {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
  margin-bottom: 6px;
}

.message-time {
  color: #c0c4cc;
  font-size: 12px;
}

/* 滚动条样式 */
.message-list::-webkit-scrollbar {
  width: 4px;
}

.message-list::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 2px;
}
</style>
