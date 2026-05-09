<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never">
          <div slot="header">量表列表</div>
          <div v-for="item in scaleList" :key="item.scaleId" class="scale-card" :class="{ active: activeScale && item.scaleId === activeScale.scaleId }" @click="loadScale(item.scaleId)">
            <div class="scale-name">{{ item.scaleName }}</div>
            <div class="scale-meta">{{ item.scaleType }} · {{ item.questionCount }} 题</div>
            <div class="scale-desc">{{ item.description }}</div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="16">
        <el-card shadow="never">
          <div slot="header">量表作答</div>
          <div v-if="activeScale">
            <div class="assessment-head">
              <h3>{{ activeScale.scaleName }}</h3>
              <p>{{ activeScale.description }}</p>
            </div>
            <el-form label-position="top">
              <div v-for="question in activeScale.questions" :key="question.questionId" class="question-block">
                <div class="question-title">{{ question.questionSort }}. {{ question.questionTitle }}</div>
                <el-radio-group v-model="answers[question.questionId]">
                  <el-radio v-for="option in question.options" :key="option.optionId" :label="option.optionId">
                    {{ option.optionLabel }}
                  </el-radio>
                </el-radio-group>
              </div>
              <el-form-item>
                <el-button type="primary" @click="submitCurrentScale">提交测评</el-button>
              </el-form-item>
            </el-form>
          </div>
          <el-empty v-else description="请选择左侧量表" />
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top: 16px;">
      <div slot="header">历史测评记录</div>
      <el-table :data="recordList" v-loading="recordLoading">
        <el-table-column label="时间" width="180">
          <template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="量表名称" prop="scaleName" />
        <el-table-column label="总分" prop="totalScore" width="100" />
        <el-table-column label="评估等级" width="120">
          <template slot-scope="scope">
            <el-tag :type="riskTagType(scope.row.resultLevel)">{{ scope.row.resultLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="结果摘要" prop="resultSummary" show-overflow-tooltip />
      </el-table>
    </el-card>
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
      recordLoading: false,
      recordList: []
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
      })
    },
    getRecordList() {
      this.recordLoading = true
      listAssessmentRecords({ pageNum: 1, pageSize: 20 }).then(response => {
        this.recordList = response.rows || []
        this.recordLoading = false
      })
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
        this.$alert(`${data.resultSummary}\n\n${data.guidanceAdvice}`, "测评完成", { confirmButtonText: "知道了" })
        this.getRecordList()
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
.scale-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 14px;
  margin-bottom: 12px;
  cursor: pointer;
}
.scale-card.active { border-color: #409EFF; background: #f0f7ff; }
.scale-name { font-weight: 600; color: #0f172a; }
.scale-meta { margin: 6px 0; color: #64748b; font-size: 13px; }
.scale-desc { color: #475569; line-height: 1.7; font-size: 13px; }
.assessment-head h3 { margin: 0 0 8px; }
.assessment-head p { margin: 0 0 18px; color: #64748b; }
.question-block { padding: 14px 0; border-bottom: 1px solid #edf2f7; }
.question-title { margin-bottom: 10px; font-weight: 600; }
</style>
