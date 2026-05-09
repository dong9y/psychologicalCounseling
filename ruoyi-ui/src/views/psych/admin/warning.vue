<template>
  <div class="app-container">
    <el-form :inline="true" size="small" style="margin-bottom: 12px;">
      <el-form-item>
        <el-input v-model="queryParams.studentName" placeholder="学生姓名" clearable />
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.riskLevel" placeholder="风险等级" clearable>
          <el-option label="高风险" value="高风险" />
          <el-option label="中风险" value="中风险" />
          <el-option label="关注" value="关注" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.status" placeholder="处理状态" clearable>
          <el-option label="待处理" value="待处理" />
          <el-option label="处理中" value="处理中" />
          <el-option label="已处理" value="已处理" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getList">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="warningList" v-loading="loading">
      <el-table-column label="预警编号" prop="warningNo" width="150" />
      <el-table-column label="学生" prop="studentName" width="120" />
      <el-table-column label="来源" prop="warningSource" width="120" />
      <el-table-column label="风险等级" width="100">
        <template slot-scope="scope">
          <el-tag :type="riskTagType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分配咨询师" prop="counselorName" width="120" />
      <el-table-column label="触发摘要" prop="triggerSummary" show-overflow-tooltip />
      <el-table-column label="处理意见" prop="processNote" show-overflow-tooltip />
    </el-table>
  </div>
</template>

<script>
import { listAdminWarnings } from "@/api/psych/center"

export default {
  name: "PsychAdminWarning",
  data() {
    return {
      loading: false,
      warningList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        studentName: undefined,
        riskLevel: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listAdminWarnings(this.queryParams).then(response => {
        this.warningList = response.rows || []
        this.loading = false
      })
    },
    riskTagType(level) {
      const map = { "正常": "success", "关注": "warning", "中风险": "warning", "高风险": "danger" }
      return map[level] || "info"
    },
    statusTagType(status) {
      const map = { "待处理": "danger", "处理中": "warning", "已处理": "success" }
      return map[status] || "info"
    }
  }
}
</script>
