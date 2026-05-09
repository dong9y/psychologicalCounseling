<template>
    <div :class="['sidebar-theme-wrapper', {'has-logo':showLogo}, settings.sideTheme]">
        <logo v-if="showLogo" :collapse="isCollapse" />
        <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                background-color="#1B263B"
                text-color="#CBD5E1"
                :unique-opened="true"
                active-text-color="#FFFFFF"
                :collapse-transition="false"
                mode="vertical"
                class="psych-sidebar-menu"
            >
                <sidebar-item
                    v-for="(route, index) in filteredSidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
                />
            </el-menu>
        </el-scrollbar>
    </div>
</template>

<script>
import { mapGetters, mapState } from "vuex"
import Logo from "./Logo"
import SidebarItem from "./SidebarItem"

export default {
    components: { SidebarItem, Logo },
    computed: {
        ...mapState(["settings"]),
        ...mapGetters(["sidebarRouters", "sidebar"]),
        filteredSidebarRouters() {
            const hiddenNames = ["若依官网", "系统工具"]
            return (this.sidebarRouters || []).filter(route => {
                const title = route.meta && route.meta.title
                return !hiddenNames.includes(title)
            })
        },
        activeMenu() {
            const route = this.$route
            const { meta, path } = route
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu
            }
            return path
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo
        },
        isCollapse() {
            return !this.sidebar.opened
        }
    }
}
</script>

<style lang="scss" scoped>
.sidebar-theme-wrapper {
  background: linear-gradient(180deg, #1B263B 0%, #1B263B 100%);
  border-right: 1px solid #0D1B2A;
  box-shadow: inset -1px 0 0 rgba(255, 255, 255, 0.05);
}

.psych-sidebar-menu {
  border-right: none;
  background: transparent !important;
  padding: 12px 10px 18px;
}

.psych-sidebar-menu:not(.el-menu--collapse) ::v-deep .el-menu-item,
.psych-sidebar-menu:not(.el-menu--collapse) ::v-deep .el-submenu__title {
  height: 44px;
  line-height: 44px;
  margin: 4px 0;
  border-radius: 0;
  font-weight: 500;
}

.psych-sidebar-menu:not(.el-menu--collapse) ::v-deep .el-menu-item:hover,
.psych-sidebar-menu:not(.el-menu--collapse) ::v-deep .el-submenu__title:hover {
  background: #3B82F6 !important;
  color: #FFFFFF !important;
}

.psych-sidebar-menu:not(.el-menu--collapse) ::v-deep .el-menu-item.is-active {
  background: #2563EB !important;
  color: #FFFFFF !important;
}

.psych-sidebar-menu ::v-deep .el-submenu .el-menu {
  background: transparent !important;
}

.psych-sidebar-menu ::v-deep .nest-menu .el-menu-item {
  padding-left: 48px !important;
  border-radius: 0;
  min-width: auto;
}

.psych-sidebar-menu.el-menu--collapse {
  padding-left: 6px;
  padding-right: 6px;
}

.psych-sidebar-menu.el-menu--collapse ::v-deep .el-menu-item,
.psych-sidebar-menu.el-menu--collapse ::v-deep .el-submenu__title {
  margin: 6px 0;
  border-radius: 0;
}
</style>
