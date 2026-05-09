<template>
  <div class="app-container">
    <el-form :inline="true" size="small" style="margin-bottom: 12px;">
      <el-form-item>
        <el-input v-model="queryParams.scaleName" placeholder="量表名称" clearable @keyup.enter.native="getList" />
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getList">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增量表</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="scaleList" v-loading="loading">
      <el-table-column label="编码" prop="scaleCode" width="120" />
      <el-table-column label="名称" prop="scaleName" />
      <el-table-column label="类型" prop="scaleType" width="120" />
      <el-table-column label="题目数" prop="questionCount" width="100" />
      <el-table-column label="预警阈值" prop="warningThreshold" width="100" />
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">{{ scope.row.status === '0' ? '正常' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="说明" prop="description" show-overflow-tooltip />
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" :visible.sync="open" width="620px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="量表编码" prop="scaleCode">
          <el-input v-model="form.scaleCode" />
        </el-form-item>
        <el-form-item label="量表名称" prop="scaleName">
          <el-input v-model="form.scaleName" />
        </el-form-item>
        <el-form-item label="量表类型" prop="scaleType">
          <el-input v-model="form.scaleType" />
        </el-form-item>
        <el-form-item label="题目数量" prop="questionCount">
          <el-input-number v-model="form.questionCount" :min="1" />
        </el-form-item>
        <el-form-item label="最大分值" prop="maxScore">
          <el-input-number v-model="form.maxScore" :min="1" />
        </el-form-item>
        <el-form-item label="预警阈值" prop="warningThreshold">
          <el-input-number v-model="form.warningThreshold" :min="1" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="量表说明">
          <el-input v-model="form.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="结果模板">
          <el-input v-model="form.resultTemplate" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="open = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAdminScales, addScale, updateScale } from "@/api/psych/center"

export default {
  name: "PsychAdminScale",
  data() {
    return {
      loading: false,
      open: false,
      title: "",
      scaleList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        scaleName: undefined,
        status: undefined
      },
      form: {},
      rules: {
        scaleCode: [{ required: true, message: "请输入量表编码", trigger: "blur" }],
        scaleName: [{ required: true, message: "请输入量表名称", trigger: "blur" }],
        scaleType: [{ required: true, message: "请输入量表类型", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listAdminScales(this.queryParams).then(response => {
        this.scaleList = response.rows || []
        this.loading = false
      })
    },
    handleAdd() {
      this.title = "新增量表"
      this.form = {
        scaleCode: "",
        scaleName: "",
        scaleType: "",
        questionCount: 20,
        maxScore: 80,
        warningThreshold: 45,
        status: "0",
        description: "",
        resultTemplate: ""
      }
      this.open = true
    },
    handleEdit(row) {
      this.title = "编辑量表"
      this.form = Object.assign({}, row)
      this.open = true
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const request = this.form.scaleId ? updateScale(this.form) : addScale(this.form)
        request.then(() => {
          this.$modal.msgSuccess("保存成功")
          this.open = false
          this.getList()
        })
      })
    }
  }
}
</script>
