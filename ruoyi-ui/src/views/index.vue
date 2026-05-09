<template>
  <div class="app-container role-home">
    <el-card shadow="never">
      <div class="role-home-title">校园心理疏导情感分析系统</div>
      <div class="role-home-desc">系统正在根据当前账号角色为您跳转到对应工作台。</div>
      <div class="role-home-action">
        <el-button type="primary" @click="redirectByRole">立即进入</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Index",
  mounted() {
    this.redirectByRole()
  },
  methods: {
    redirectByRole() {
      const roles = this.$store.getters.roles || []
      if (roles.includes("student")) {
        this.$router.replace("/psych/student/index")
        return
      }
      if (roles.includes("counselor")) {
        this.$router.replace("/psych/counselor/index")
        return
      }
      this.$router.replace("/psych/admin/index")
    }
  }
}
</script>

<style scoped>
.role-home {
  min-height: calc(100vh - 120px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.role-home .el-card {
  width: 100%;
  max-width: 560px;
  text-align: center;
  border-radius: 8px;
}

.role-home-title {
  font-size: 28px;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 16px;
}

.role-home-desc {
  color: #64748b;
  line-height: 1.8;
}

.role-home-action {
  margin-top: 24px;
}
</style>
