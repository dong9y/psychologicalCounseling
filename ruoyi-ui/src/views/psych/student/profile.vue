<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never">
          <div slot="header">学生档案</div>
          <el-descriptions :column="1" border v-if="profile.profile">
            <el-descriptions-item label="姓名">{{ profile.profile.realName }}</el-descriptions-item>
            <el-descriptions-item label="学号">{{ profile.profile.studentNo }}</el-descriptions-item>
            <el-descriptions-item label="学院">{{ profile.profile.college }}</el-descriptions-item>
            <el-descriptions-item label="专业">{{ profile.profile.major }}</el-descriptions-item>
            <el-descriptions-item label="班级">{{ profile.profile.className }}</el-descriptions-item>
            <el-descriptions-item label="联系方式">{{ profile.profile.contactPhone }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="16">
        <el-card shadow="never">
          <div slot="header">通知消息</div>
          <div v-if="messages.length">
            <div class="message-item" v-for="item in messages" :key="item.messageId">
              <div class="message-title">
                <span>{{ item.title }}</span>
                <el-tag size="mini" :type="item.readStatus === '0' ? 'danger' : 'info'">{{ item.readStatus === '0' ? '未读' : '已读' }}</el-tag>
              </div>
              <div class="message-content">{{ item.content }}</div>
              <div class="message-time">{{ parseTime(item.createTime) }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无通知消息" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never">
          <div slot="header">最近倾诉记录</div>
          <el-table :data="profile.latestEmotionRecords || []">
            <el-table-column label="时间" width="170">
              <template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template>
            </el-table-column>
            <el-table-column label="主情绪" prop="primaryEmotion" width="100" />
            <el-table-column label="风险等级" prop="riskLevel" width="100" />
            <el-table-column label="摘要" prop="analysisSummary" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never">
          <div slot="header">最近测评记录</div>
          <el-table :data="profile.latestAssessmentRecords || []">
            <el-table-column label="时间" width="170">
              <template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template>
            </el-table-column>
            <el-table-column label="量表" prop="scaleName" />
            <el-table-column label="等级" prop="resultLevel" width="100" />
            <el-table-column label="摘要" prop="resultSummary" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStudentProfile, listStudentMessages } from "@/api/psych/center"

export default {
  name: "PsychStudentProfile",
  data() {
    return {
      profile: {},
      messages: []
    }
  },
  created() {
    this.getProfile()
    this.getMessages()
  },
  methods: {
    getProfile() {
      getStudentProfile().then(response => {
        this.profile = response.data || {}
      })
    },
    getMessages() {
      listStudentMessages(20).then(response => {
        this.messages = response.data || []
      })
    }
  }
}
</script>

<style scoped>
.message-item { padding: 10px 0; border-bottom: 1px solid #eef2f7; }
.message-item:last-child { border-bottom: none; }
.message-title { display: flex; justify-content: space-between; font-weight: 600; color: #0f172a; }
.message-content { color: #475569; line-height: 1.7; margin: 8px 0; }
.message-time { color: #94a3b8; font-size: 12px; }
</style>
