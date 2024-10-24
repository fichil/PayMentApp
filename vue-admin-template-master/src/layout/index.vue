<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar />
        <tags-view />
      </div>
      <app-main />
    </div>
    <div class="footer">
      <a href="https://beian.miit.gov.cn/" target="_blank">闽ICP备2024072712号-1</a>
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain, TagsView } from './components'
import ResizeMixin from './mixin/ResizeHandler'

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView
  },
  mixins: [ResizeMixin],
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    fixedHeader() {
      return this.$store.state.settings.fixedHeader
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/mixin.scss";
@import "~@/styles/variables.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$sideBarWidth});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px)
}

.mobile .fixed-header {
  width: 100%;
}

.main-container{
  display: flex;
  flex-direction: column;
  flex: 1;
}

.footer {
  position: fixed;
  /* 固定定位 */
  bottom: 0;
  /* 距离底部0 */
  left: 0;
  /* 距离左边0 */
  right: 0;
  /* 距离右边0 */
  background-color: #f1f1f1;
  /* 背景色 */
  text-align: center;
  /* 内容居中 */
  padding: 10px;
  /* 内边距 */
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  /* 上边阴影 */
}

.footer a {
  color: #007BFF;
  /* 链接颜色 */
  text-decoration: none;
  /* 去掉下划线 */
  margin: 0 10px;
  /* 左右间距 */
}

.footer a:hover {
  text-decoration: underline;
  /* 鼠标悬停时加下划线 */
}
</style>
