<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :sm="12" :lg="6" v-for="card in cards" :key="card.title">
        <el-card shadow="hover" class="dashboard-card">
          <div class="dashboard-label">{{ card.title }}</div>
          <div class="dashboard-value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never">
          <div slot="header">预警提醒</div>
          <el-table :data="dashboard.recentWarnings || []">
            <el-table-column label="预警编号" prop="warningNo" width="150" />
            <el-table-column label="学生" prop="studentName" width="120" />
            <el-table-column label="风险等级" width="100">
              <template slot-scope="scope">
                <el-tag :type="riskTagType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" prop="status" width="100" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never">
          <div slot="header">待处理预约</div>
          <el-table :data="appointmentList">
            <el-table-column label="时间" width="170">
              <template slot-scope="scope">{{ parseTime(scope.row.appointmentTime) }}</template>
            </el-table-column>
            <el-table-column label="主题" prop="topic" width="140" />
            <el-table-column label="状态" prop="status" width="100" />
            <el-table-column label="问题描述" prop="problemDescription" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCounselorDashboard, listCounselorAppointments } from "@/api/psych/center"

export default {
  name: "PsychCounselorIndex",
  data() {
    return {
      dashboard: {},
      appointmentList: []
    }
  },
  computed: {
    cards() {
      return [
        { title: "今日预约", value: this.dashboard.todayAppointmentCount || 0 },
        { title: "待处理预约", value: this.dashboard.pendingAppointmentCount || 0 },
        { title: "高风险预警", value: this.dashboard.highRiskWarningCount || 0 },
        { title: "本周咨询量", value: this.dashboard.weekCounselingCount || 0 }
      ]
    }
  },
  created() {
    this.getDashboard()
    this.getAppointments()
  },
  methods: {
    getDashboard() {
      getCounselorDashboard().then(response => {
        this.dashboard = response.data || {}
      })
    },
    getAppointments() {
      listCounselorAppointments({ pageNum: 1, pageSize: 10 }).then(response => {
        this.appointmentList = response.rows || []
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
.dashboard-card { min-height: 120px; border-radius: 8px; }
.dashboard-label { color: #64748b; font-size: 13px; }
.dashboard-value { margin-top: 18px; font-size: 30px; font-weight: 700; color: #0f172a; }
</style>
