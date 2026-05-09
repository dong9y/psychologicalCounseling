<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never">
          <div slot="header">预警列表</div>
          <el-form :inline="true" size="small" style="margin-bottom: 12px;">
            <el-form-item>
              <el-select v-model="queryParams.status" placeholder="状态" clearable @change="getList">
                <el-option label="待处理" value="待处理" />
                <el-option label="处理中" value="处理中" />
                <el-option label="已处理" value="已处理" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryParams.riskLevel" placeholder="风险等级" clearable @change="getList">
                <el-option label="高风险" value="高风险" />
                <el-option label="中风险" value="中风险" />
                <el-option label="关注" value="关注" />
              </el-select>
            </el-form-item>
          </el-form>
          <el-table :data="warningList" v-loading="loading" @row-click="showDetail">
            <el-table-column label="预警编号" prop="warningNo" width="150" />
            <el-table-column label="学生" prop="studentName" width="100" />
            <el-table-column label="来源" prop="warningSource" width="110" />
            <el-table-column label="等级" width="100">
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
          <div slot="header">预警详情与处理</div>
          <div v-if="detail">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="学生">{{ detail.studentName }}</el-descriptions-item>
              <el-descriptions-item label="来源">{{ detail.warningSource }}</el-descriptions-item>
              <el-descriptions-item label="风险等级">{{ detail.riskLevel }}</el-descriptions-item>
              <el-descriptions-item label="触发摘要">{{ detail.triggerSummary }}</el-descriptions-item>
            </el-descriptions>

            <el-form label-width="90px" style="margin-top: 16px;">
              <el-form-item label="处理状态">
                <el-select v-model="detail.status">
                  <el-option label="待处理" value="待处理" />
                  <el-option label="处理中" value="处理中" />
                  <el-option label="已处理" value="已处理" />
                </el-select>
              </el-form-item>
              <el-form-item label="转交咨询师">
                <el-select v-model="detail.counselorUserId" clearable placeholder="如需转交请选择">
                  <el-option v-for="item in transferOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
              <el-form-item label="处理意见">
                <el-input v-model="detail.processNote" type="textarea" :rows="4" />
              </el-form-item>
              <el-form-item label="跟进计划">
                <el-input v-model="detail.followPlan" type="textarea" :rows="4" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitHandle">保存处理</el-button>
              </el-form-item>
            </el-form>
          </div>
          <el-empty v-else description="请选择左侧预警记录" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listCounselorWarnings, getWarningDetail, handleWarning, listTransferOptions } from "@/api/psych/center"

export default {
  name: "PsychCounselorWarning",
  data() {
    return {
      loading: false,
      warningList: [],
      detail: null,
      transferOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        status: undefined,
        riskLevel: undefined
      }
    }
  },
  created() {
    this.getList()
    this.getTransferOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listCounselorWarnings(this.queryParams).then(response => {
        this.warningList = response.rows || []
        this.loading = false
        if (!this.detail && this.warningList.length) {
          this.showDetail(this.warningList[0])
        }
      })
    },
    getTransferOptions() {
      listTransferOptions().then(response => {
        this.transferOptions = response.data || []
      })
    },
    showDetail(row) {
      getWarningDetail(row.warningId).then(response => {
        this.detail = response.data
      })
    },
    submitHandle() {
      handleWarning(this.detail).then(() => {
        this.$modal.msgSuccess("处理结果已保存")
        this.getList()
      })
    },
    riskTagType(level) {
      const map = { "正常": "success", "关注": "warning", "中风险": "warning", "高风险": "danger" }
      return map[level] || "info"
    }
  }
}
</script>
