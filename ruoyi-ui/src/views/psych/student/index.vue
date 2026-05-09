<template>
  <div class="app-container psych-page">
    <el-row :gutter="16" class="stat-row">
      <el-col :xs="24" :sm="12" :lg="6" v-for="card in cards" :key="card.title">
        <el-card shadow="hover" class="psych-stat-card">
          <div class="stat-label">{{ card.title }}</div>
          <div class="stat-value">{{ card.value }}</div>
          <div class="stat-tip">{{ card.tip }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="psych-panel">
          <div slot="header" class="panel-header">
            <span>最近一次情感分析</span>
            <el-button type="text" @click="$router.push('/psych/student/confession')">去倾诉</el-button>
          </div>
          <div v-if="dashboard.latestEmotionRecord">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="主情绪">{{ dashboard.latestEmotionRecord.primaryEmotion }}</el-descriptions-item>
              <el-descriptions-item label="风险等级">
                <el-tag :type="riskTagType(dashboard.latestEmotionRecord.riskLevel)">{{ dashboard.latestEmotionRecord.riskLevel }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="情绪标签">{{ dashboard.latestEmotionRecord.emotionTags }}</el-descriptions-item>
              <el-descriptions-item label="分析置信度">{{ dashboard.latestEmotionRecord.confidence }}</el-descriptions-item>
              <el-descriptions-item label="分析摘要" :span="2">{{ dashboard.latestEmotionRecord.analysisSummary }}</el-descriptions-item>
              <el-descriptions-item label="疏导建议" :span="2">{{ dashboard.latestEmotionRecord.guidanceAdvice }}</el-descriptions-item>
            </el-descriptions>
          </div>
          <el-empty v-else description="还没有心理倾诉记录" />
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="psych-panel">
          <div slot="header" class="panel-header">
            <span>最近通知</span>
            <el-button type="text" @click="$router.push('/psych/student/profile')">查看全部</el-button>
          </div>
          <div v-if="dashboard.recentMessages && dashboard.recentMessages.length">
            <div v-for="item in dashboard.recentMessages" :key="item.messageId" class="message-item">
              <div class="message-title">{{ item.title }}</div>
              <div class="message-content">{{ item.content }}</div>
              <div class="message-time">{{ parseTime(item.createTime) }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无通知消息" />
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="psych-panel">
      <div slot="header" class="panel-header">
        <span>快捷入口</span>
      </div>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :lg="6" v-for="entry in entries" :key="entry.title">
          <div class="entry-card" @click="$router.push(entry.path)">
            <i :class="entry.icon"></i>
            <h4>{{ entry.title }}</h4>
            <p>{{ entry.desc }}</p>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getStudentDashboard } from "@/api/psych/center"

export default {
  name: "PsychStudentIndex",
  data() {
    return {
      dashboard: {
        latestEmotionRecord: null,
        recentMessages: []
      }
    }
  },
  computed: {
    cards() {
      return [
        { title: "当前心理状态", value: this.dashboard.latestRiskLevel || "正常", tip: "基于最近一次倾诉/测评结果" },
        { title: "测评次数", value: this.dashboard.recentAssessmentCount || 0, tip: "累计完成的心理测评" },
        { title: "待处理预约", value: this.dashboard.pendingAppointmentCount || 0, tip: "待确认或已确认预约" },
        { title: "未读通知", value: this.dashboard.unreadMessageCount || 0, tip: "系统提醒与预警消息" }
      ]
    },
    entries() {
      return [
        { title: "心理倾诉", desc: "提交文本困扰并获取情绪分析", path: "/psych/student/confession", icon: "el-icon-edit-outline" },
        { title: "心理测评", desc: "完成量表作答并查看评估结果", path: "/psych/student/assessment", icon: "el-icon-data-analysis" },
        { title: "咨询预约", desc: "选择咨询师并提交预约申请", path: "/psych/student/appointment", icon: "el-icon-date" },
        { title: "个人中心", desc: "查看历史记录与通知消息", path: "/psych/student/profile", icon: "el-icon-user" }
      ]
    }
  },
  created() {
    this.getDashboard()
  },
  methods: {
    getDashboard() {
      getStudentDashboard().then(response => {
        this.dashboard = response.data || {}
      })
    },
    riskTagType(level) {
      const map = { "正常": "success", "关注": "warning", "中风险": "warning", "高风险": "danger" }
      return map[level] || "info"
    }
  }
}
</script>

<style scoped>
.psych-page .psych-panel { margin-top: 16px; }
.stat-row { margin-bottom: 8px; }
.psych-stat-card { border-radius: 8px; min-height: 126px; }
.stat-label { color: #64748b; font-size: 13px; }
.stat-value { font-size: 28px; font-weight: 700; color: #0f172a; margin: 14px 0 6px; }
.stat-tip { font-size: 12px; color: #94a3b8; }
.panel-header { display: flex; justify-content: space-between; align-items: center; }
.message-item { padding: 12px 0; border-bottom: 1px solid #eef2f7; }
.message-item:last-child { border-bottom: none; }
.message-title { font-weight: 600; margin-bottom: 6px; color: #1e293b; }
.message-content { color: #475569; line-height: 1.7; }
.message-time { color: #94a3b8; font-size: 12px; margin-top: 6px; }
.entry-card {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 18px;
  cursor: pointer;
  min-height: 148px;
  transition: all .2s ease;
}
.entry-card:hover { border-color: #409EFF; transform: translateY(-2px); box-shadow: 0 8px 18px rgba(64,158,255,.12); }
.entry-card i { font-size: 26px; color: #409EFF; }
.entry-card h4 { margin: 14px 0 8px; color: #0f172a; }
.entry-card p { margin: 0; color: #64748b; line-height: 1.7; }
</style>
