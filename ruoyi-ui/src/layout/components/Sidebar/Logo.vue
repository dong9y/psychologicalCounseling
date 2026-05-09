<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <div class="sidebar-logo-mark">心</div>
        <h1 class="sidebar-title">{{ shortTitle }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <div class="sidebar-logo-mark">心</div>
        <div class="sidebar-logo-text">
          <h1 class="sidebar-title">{{ title }}</h1>
          <p class="sidebar-subtitle">Campus Counseling</p>
        </div>
      </router-link>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    shortTitle() {
      return '心理中心'
    }
  },
  data() {
    return {
      title: '校园心理疏导情感分析系统'
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  height: 76px;
  padding: 14px 16px 10px;
  background: linear-gradient(180deg, #1B263B 0%, #0D1B2A 100%);
  border-bottom: 1px solid #0D1B2A;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    text-decoration: none;

    & .sidebar-logo-mark {
      width: 42px;
      height: 42px;
      min-width: 42px;
      border-radius: 0;
      background: linear-gradient(135deg, #2563EB 0%, #3B82F6 100%);
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      font-weight: 700;
      margin-right: 12px;
      box-shadow: 0 4px 12px rgba(37, 99, 235, 0.4);
    }

    & .sidebar-logo-text {
      min-width: 0;
    }

    & .sidebar-title {
      display: block;
      margin: 0;
      color: #FFFFFF;
      font-weight: 700;
      line-height: 1.3;
      font-size: 15px;
      font-family: "Microsoft YaHei", "PingFang SC", sans-serif;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    & .sidebar-subtitle {
      margin: 4px 0 0;
      color: #94A3B8;
      font-size: 12px;
      line-height: 1;
    }
  }

  &.collapse {
    height: 64px;
    padding: 10px 0;

    .sidebar-logo-link {
      justify-content: center;
    }

    .sidebar-logo-mark {
      margin-right: 0;
    }
  }
}
</style>
