<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :lg="11">
        <el-card shadow="never">
          <div slot="header">心理倾诉</div>
          <el-form label-width="90px">
            <el-form-item label="匿名模式">
              <el-switch v-model="form.anonymousFlag" :active-value="1" :inactive-value="0" />
              <span class="form-tip">开启后咨询师默认看不到您的身份信息</span>
            </el-form-item>
            <el-form-item label="倾诉内容">
              <el-input
                v-model="form.text"
                type="textarea"
                :rows="10"
                maxlength="500"
                show-word-limit
                placeholder="请描述最近让你困扰的情绪、事件或压力来源..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSubmit">提交分析</el-button>
              <el-button @click="resetForm">清空</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="13">
        <el-card shadow="never">
          <div slot="header">分析结果</div>
          <div v-if="result">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="主情绪">{{ result.primaryEmotion }}</el-descriptions-item>
              <el-descriptions-item label="风险等级">
                <el-tag :type="riskTagType(result.riskLevel)">{{ result.riskLevel }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="情绪得分">{{ result.emotionScore }}</el-descriptions-item>
              <el-descriptions-item label="置信度">{{ result.confidence }}</el-descriptions-item>
              <el-descriptions-item label="情绪标签" :span="2">{{ result.emotionTags }}</el-descriptions-item>
              <el-descriptions-item label="分析摘要" :span="2">{{ result.analysisSummary }}</el-descriptions-item>
              <el-descriptions-item label="疏导建议" :span="2">{{ result.guidanceAdvice }}</el-descriptions-item>
            </el-descriptions>
          </div>
          <el-empty v-else description="提交后显示情感分析结果" />
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top: 16px;">
      <div slot="header">历史倾诉记录</div>
      <el-table v-loading="loading" :data="recordList">
        <el-table-column label="时间" prop="createTime" width="180">
          <template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="主情绪" prop="primaryEmotion" width="120" />
        <el-table-column label="风险等级" width="120">
          <template slot-scope="scope">
            <el-tag :type="riskTagType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="匿名" width="80">
          <template slot-scope="scope">{{ scope.row.anonymousFlag === 1 ? "是" : "否" }}</template>
        </el-table-column>
        <el-table-column label="分析摘要" prop="analysisSummary" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { submitEmotion, listEmotionRecords } from "@/api/psych/center"

export default {
  name: "PsychStudentConfession",
  data() {
    return {
      loading: false,
      result: null,
      recordList: [],
      form: {
        text: "",
        anonymousFlag: 0
      }
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
      submitEmotion(this.form).then(response => {
        this.result = response.data
        this.$modal.msgSuccess("分析完成")
        this.getList()
      })
    },
    resetForm() {
      this.form = { text: "", anonymousFlag: 0 }
      this.result = null
    },
    riskTagType(level) {
      const map = { "正常": "success", "关注": "warning", "中风险": "warning", "高风险": "danger" }
      return map[level] || "info"
    }
  }
}
</script>

<style scoped>
.form-tip { margin-left: 12px; color: #909399; font-size: 12px; }
</style>
