<template>
  <div class="login">
    <div class="login-shell">
      <section class="brand-panel">
        <div class="brand-mark">
          <svg-icon icon-class="education" />
        </div>
        <h1>{{ title }}</h1>
        <p class="brand-desc">
          面向校园心理健康服务场景，整合学生心理倾诉、量表测评、咨询预约、预警处置和情感分析能力。
        </p>
        <ul class="feature-list">
          <li v-for="item in features" :key="item.text">
            <svg-icon :icon-class="item.icon" />
            <span>{{ item.text }}</span>
          </li>
        </ul>
        <div class="brand-stats">
          <div>
            <strong>24h</strong>
            <span>情绪预警</span>
          </div>
          <div>
            <strong>3类</strong>
            <span>角色协同</span>
          </div>
          <div>
            <strong>闭环</strong>
            <span>咨询跟进</span>
          </div>
        </div>
      </section>

      <section class="form-panel">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
          <div class="form-heading">
            <h2>用户登录</h2>
            <p>请选择身份或输入账号密码进入系统工作台。</p>
          </div>

          <el-form-item prop="username" label="账号">
            <el-input
              v-model="loginForm.username"
              type="text"
              auto-complete="off"
              placeholder="请输入账号 / 工号"
            >
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>

          <el-form-item prop="password" label="密码">
            <el-input
              v-model="loginForm.password"
              type="password"
              auto-complete="off"
              placeholder="请输入密码"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>

          <el-form-item prop="code" label="验证码" v-if="captchaEnabled">
            <div class="code-row">
              <el-input
                v-model="loginForm.code"
                auto-complete="off"
                placeholder="请输入验证码"
                @keyup.enter.native="handleLogin"
              >
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <img :src="codeUrl" @click="getCode" class="login-code-img"/>
            </div>
          </el-form-item>

          <div class="role-title">登录角色</div>
          <div class="role-grid">
            <button
              v-for="role in loginRoles"
              :key="role.value"
              type="button"
              class="role-card"
              :class="{ active: activeRole === role.value }"
              @click="selectRole(role)"
            >
              <svg-icon :icon-class="role.icon" />
              <span>{{ role.label }}</span>
            </button>
          </div>

          <div class="form-options">
            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
            <router-link v-if="register" class="link-type" :to="'/register'">立即注册</router-link>
          </div>

          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            class="login-button"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>

          <div class="demo-list">
            <button
              v-for="role in loginRoles"
              :key="role.value + '-demo'"
              type="button"
              @click="selectRole(role)"
            >
              <strong>{{ role.label }}演示账号</strong>
              <span>账号：{{ role.username }}　密码：{{ role.password }}</span>
            </button>
          </div>
        </el-form>
      </section>
    </div>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import defaultSettings from '@/settings'

export default {
  name: "Login",
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      activeRole: "admin",
      features: [
        { icon: "message", text: "学生心理倾诉与情绪识别" },
        { icon: "chart", text: "量表测评与风险趋势分析" },
        { icon: "date", text: "咨询预约、跟进与评价闭环" },
        { icon: "bell", text: "预警分派和处置过程留痕" }
      ],
      loginRoles: [
        { value: "student", label: "学生", icon: "education", username: "student01", password: "123456" },
        { value: "counselor", label: "咨询师", icon: "people", username: "counselor01", password: "123456" },
        { value: "admin", label: "管理员", icon: "system", username: "admin", password: "admin123" }
      ],
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        code: this.loginForm.code,
        uuid: this.loginForm.uuid
      }
    },
    selectRole(role) {
      this.activeRole = role.value
      this.loginForm.username = role.username
      this.loginForm.password = role.password
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100%;
  padding: 32px 20px 56px;
  overflow: auto;
  background:
    radial-gradient(circle at 16% 18%, rgba(80, 151, 255, 0.22), transparent 30%),
    linear-gradient(135deg, #eef5ff 0%, #f7f9fc 46%, #edf3ff 100%);
}

.login-shell {
  display: grid;
  grid-template-columns: minmax(360px, 0.95fr) minmax(360px, 1fr);
  width: min(1040px, 100%);
  min-height: 640px;
  overflow: hidden;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 24px 60px rgba(36, 70, 134, 0.16);
}

.brand-panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 54px 50px;
  color: #ffffff;
  background:
    linear-gradient(150deg, rgba(31, 81, 193, 0.96), rgba(55, 131, 238, 0.94)),
    url("../assets/images/login-background.jpg") center/cover;
}

.brand-mark {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 54px;
  height: 54px;
  margin-bottom: 30px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.14);
  border: 1px solid rgba(255, 255, 255, 0.25);

  .svg-icon {
    width: 28px;
    height: 28px;
  }
}

.brand-panel h1 {
  margin: 0;
  font-size: 28px;
  line-height: 1.35;
  font-weight: 700;
  letter-spacing: 0;
}

.brand-desc {
  max-width: 430px;
  margin: 18px 0 28px;
  font-size: 14px;
  line-height: 1.9;
  color: rgba(255, 255, 255, 0.86);
}

.feature-list {
  padding: 0;
  margin: 0;
  list-style: none;

  li {
    display: flex;
    align-items: center;
    min-height: 42px;
    margin-bottom: 12px;
    padding: 0 16px;
    border-radius: 6px;
    background: rgba(255, 255, 255, 0.12);
    border: 1px solid rgba(255, 255, 255, 0.16);
    font-size: 13px;
  }

  .svg-icon {
    flex: 0 0 auto;
    width: 15px;
    height: 15px;
    margin-right: 10px;
  }
}

.brand-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-top: 26px;

  div {
    padding: 16px 12px;
    border-radius: 6px;
    background: rgba(17, 43, 109, 0.16);
  }

  strong {
    display: block;
    margin-bottom: 6px;
    font-size: 22px;
    line-height: 1;
  }

  span {
    font-size: 12px;
    color: rgba(255, 255, 255, 0.78);
  }
}

.form-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 44px 42px;
}

.login-form {
  width: 100%;
  max-width: 430px;
  z-index: 1;

  ::v-deep .el-form-item {
    margin-bottom: 18px;
  }

  ::v-deep .el-form-item__label {
    padding-bottom: 6px;
    line-height: 1.2;
    font-size: 13px;
    font-weight: 600;
    color: #26364d;
  }

  ::v-deep .el-input__inner {
    border-color: #dfe7f2;
    border-radius: 6px;
    color: #1f2f45;
    background: #fbfdff;
  }

  ::v-deep .el-input__inner:focus {
    border-color: #2f79f6;
    background: #ffffff;
    box-shadow: 0 0 0 3px rgba(47, 121, 246, 0.1);
  }

  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 40px;
    width: 14px;
    margin-left: 2px;
    color: #7c8da6;
  }
}

.form-heading {
  margin-bottom: 24px;

  h2 {
    margin: 0 0 8px;
    font-size: 24px;
    line-height: 1.25;
    color: #17233d;
    letter-spacing: 0;
  }

  p {
    margin: 0;
    font-size: 13px;
    line-height: 1.7;
    color: #718096;
  }
}

.code-row {
  display: grid;
  grid-template-columns: 1fr 112px;
  gap: 10px;
  align-items: center;
}

.login-code-img {
  width: 112px;
  height: 40px;
  cursor: pointer;
  border-radius: 6px;
  border: 1px solid #dfe7f2;
  object-fit: cover;
}

.role-title {
  margin: 4px 0 10px;
  font-size: 13px;
  font-weight: 600;
  color: #26364d;
}

.role-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 16px;
}

.role-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 72px;
  padding: 8px;
  border: 1px solid #dfe7f2;
  border-radius: 6px;
  color: #31445f;
  background: #ffffff;
  cursor: pointer;
  transition: border-color 0.2s ease, background-color 0.2s ease, box-shadow 0.2s ease;

  .svg-icon {
    width: 22px;
    height: 22px;
    margin-bottom: 8px;
    color: #2f79f6;
  }

  span {
    font-size: 13px;
    line-height: 1.2;
  }

  &:hover,
  &.active {
    border-color: #2f79f6;
    background: #f4f8ff;
    box-shadow: 0 8px 18px rgba(47, 121, 246, 0.12);
  }
}

.form-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 2px 0 18px;
  font-size: 13px;
}

.login-button {
  width: 100%;
  height: 42px;
  border-radius: 6px;
  font-weight: 600;
  background: #2f79f6;
  border-color: #2f79f6;
}

.demo-list {
  display: grid;
  gap: 10px;
  margin-top: 18px;

  button {
    display: block;
    width: 100%;
    min-height: 54px;
    padding: 10px 14px;
    text-align: left;
    border: 1px solid #e6edf5;
    border-radius: 6px;
    background: #f6f9fc;
    cursor: pointer;
  }

  strong,
  span {
    display: block;
  }

  strong {
    margin-bottom: 5px;
    font-size: 13px;
    color: #26364d;
  }

  span {
    font-size: 12px;
    color: #718096;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #7c8da6;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 0;
}

@media (max-width: 900px) {
  .login {
    align-items: flex-start;
    padding: 18px 14px 48px;
  }

  .login-shell {
    grid-template-columns: 1fr;
    min-height: auto;
  }

  .brand-panel {
    padding: 34px 28px;
  }

  .brand-panel h1 {
    font-size: 24px;
  }

  .brand-stats {
    margin-top: 18px;
  }

  .form-panel {
    padding: 30px 24px;
  }
}

@media (max-width: 520px) {
  .role-grid,
  .brand-stats {
    grid-template-columns: 1fr;
  }

  .role-card {
    flex-direction: row;
    height: 48px;

    .svg-icon {
      margin: 0 8px 0 0;
    }
  }

  .code-row {
    grid-template-columns: 1fr;
  }

  .login-code-img {
    width: 100%;
  }
}
</style>
