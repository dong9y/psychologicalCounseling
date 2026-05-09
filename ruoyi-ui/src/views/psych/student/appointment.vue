<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header">咨询师列表</div>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :lg="6" v-for="item in counselorList" :key="item.counselorId">
          <div class="counselor-card">
            <div class="counselor-name">{{ item.realName }}</div>
            <div class="counselor-title">{{ item.title }}</div>
            <div class="counselor-desc">{{ item.specialty }}</div>
            <div class="counselor-score">服务评分：{{ item.serviceScore || 4.8 }}</div>
            <el-button type="primary" size="mini" style="width: 100%;" @click="openDialog(item)">预约咨询</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never" style="margin-top: 16px;">
      <div slot="header">我的预约记录</div>
      <el-table :data="appointmentList" v-loading="loading">
        <el-table-column label="咨询师" prop="counselorName" width="140" />
        <el-table-column label="职称" prop="counselorTitle" width="180" />
        <el-table-column label="预约时间" width="180">
          <template slot-scope="scope">{{ parseTime(scope.row.appointmentTime) }}</template>
        </el-table-column>
        <el-table-column label="主题" prop="topic" width="160" />
        <el-table-column label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="咨询师反馈" prop="reviewNote" show-overflow-tooltip />
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status === '已完成' && !scope.row.evaluationContent"
              type="text"
              @click="openEvaluate(scope.row)"
            >评价咨询</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="'预约 ' + (form.counselorName || '')" :visible.sync="open" width="560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker v-model="form.appointmentTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择预约时间" />
        </el-form-item>
        <el-form-item label="咨询主题" prop="topic">
          <el-select v-model="form.topic" placeholder="请选择主题">
            <el-option label="学业压力" value="学业压力" />
            <el-option label="情感困扰" value="情感困扰" />
            <el-option label="人际关系" value="人际关系" />
            <el-option label="情绪管理" value="情绪管理" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题描述" prop="problemDescription">
          <el-input v-model="form.problemDescription" type="textarea" :rows="4" maxlength="200" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAppointment">提交预约</el-button>
        <el-button @click="open = false">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="评价咨询" :visible.sync="evaluateOpen" width="520px">
      <el-form label-width="90px">
        <el-form-item label="评分">
          <el-rate v-model="evaluateForm.evaluationScore" :max="5" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="evaluateForm.evaluationContent" type="textarea" :rows="4" maxlength="200" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEvaluate">提交评价</el-button>
        <el-button @click="evaluateOpen = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCounselors, createAppointment, listStudentAppointments, evaluateAppointment } from "@/api/psych/center"

export default {
  name: "PsychStudentAppointment",
  data() {
    return {
      loading: false,
      open: false,
      evaluateOpen: false,
      counselorList: [],
      appointmentList: [],
      currentAppointmentId: null,
      form: {},
      evaluateForm: {
        evaluationScore: 5,
        evaluationContent: ""
      },
      rules: {
        appointmentTime: [{ required: true, message: "请选择预约时间", trigger: "change" }],
        topic: [{ required: true, message: "请选择咨询主题", trigger: "change" }],
        problemDescription: [{ required: true, message: "请填写问题描述", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getCounselorList()
    this.getAppointmentList()
  },
  methods: {
    getCounselorList() {
      listCounselors({ pageNum: 1, pageSize: 20 }).then(response => {
        this.counselorList = response.rows || []
      })
    },
    getAppointmentList() {
      this.loading = true
      listStudentAppointments({ pageNum: 1, pageSize: 20 }).then(response => {
        this.appointmentList = response.rows || []
        this.loading = false
      })
    },
    openDialog(item) {
      this.form = {
        counselorUserId: item.userId,
        counselorName: item.realName,
        appointmentTime: "",
        topic: "",
        problemDescription: ""
      }
      this.open = true
    },
    submitAppointment() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        createAppointment(this.form).then(() => {
          this.$modal.msgSuccess("预约提交成功")
          this.open = false
          this.getAppointmentList()
        })
      })
    },
    openEvaluate(row) {
      this.currentAppointmentId = row.appointmentId
      this.evaluateForm = { evaluationScore: 5, evaluationContent: "" }
      this.evaluateOpen = true
    },
    submitEvaluate() {
      evaluateAppointment(this.currentAppointmentId, this.evaluateForm).then(() => {
        this.$modal.msgSuccess("评价成功")
        this.evaluateOpen = false
        this.getAppointmentList()
      })
    },
    statusTagType(status) {
      const map = { "待确认": "info", "已确认": "primary", "已拒绝": "danger", "已完成": "success", "已取消": "warning" }
      return map[status] || "info"
    }
  }
}
</script>

<style scoped>
.counselor-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  min-height: 182px;
}
.counselor-name { font-size: 16px; font-weight: 600; color: #0f172a; }
.counselor-title { color: #475569; margin: 8px 0 6px; }
.counselor-desc { color: #64748b; min-height: 42px; line-height: 1.7; font-size: 13px; }
.counselor-score { color: #409EFF; margin: 12px 0; font-size: 13px; }
</style>
