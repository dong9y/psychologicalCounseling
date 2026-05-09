<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :sm="12" :lg="6" v-for="card in cards" :key="card.title">
        <el-card shadow="hover" class="admin-card">
          <div class="admin-title">{{ card.title }}</div>
          <div class="admin-value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top: 16px;">
      <div slot="header">近期预警记录</div>
      <el-table :data="dashboard.recentWarnings || []">
        <el-table-column label="预警编号" prop="warningNo" width="150" />
        <el-table-column label="学生" prop="studentName" width="120" />
        <el-table-column label="来源" prop="warningSource" width="120" />
        <el-table-column label="风险等级" prop="riskLevel" width="100" />
        <el-table-column label="状态" prop="status" width="100" />
        <el-table-column label="触发摘要" prop="triggerSummary" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getAdminDashboard } from "@/api/psych/center"

export default {
  name: "PsychAdminIndex",
  data() {
    return {
      dashboard: {}
    }
  },
  computed: {
    cards() {
      return [
        { title: "学生总数", value: this.dashboard.studentCount || 0 },
        { title: "咨询师数量", value: this.dashboard.counselorCount || 0 },
        { title: "累计测评次数", value: this.dashboard.assessmentCount || 0 },
        { title: "预警数量", value: this.dashboard.warningCount || 0 }
      ]
    }
  },
  created() {
    this.getDashboard()
  },
  methods: {
    getDashboard() {
      getAdminDashboard().then(response => {
        this.dashboard = response.data || {}
      })
    }
  }
}
</script>

<style scoped>
.admin-card { min-height: 120px; border-radius: 8px; }
.admin-title { color: #64748b; font-size: 13px; }
.admin-value { margin-top: 20px; font-size: 30px; font-weight: 700; color: #0f172a; }
</style>
