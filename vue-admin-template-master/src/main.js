import Vue from 'vue'

// 引入 CSS 重置样式库
import 'normalize.css/normalize.css' // 一种现代的 CSS 重置替代品

// 引入 Element UI 组件库
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css' // 引入 Element UI 的主题样式
import locale from 'element-ui/lib/locale/lang/zh-CN' // 引入 Element UI 的中文语言包

// 引入全局 CSS 样式
import '@/styles/index.scss' // 全局 CSS

// 引入根组件
import App from './App'
// 引入 Vuex 状态管理
import store from './store'
// 引入 Vue Router
import router from './router'

// 引入图标组件
import '@/icons' // icon
// 引入权限控制
import '@/permission' // permission control

/**
 * 如果你不想使用 mock-server
 * 但想使用 MockJs 模拟 API
 * 可以执行: mockXHR()
 *
 * 当前 MockJs 将在生产环境中使用，
 * 请在上线之前将其移除！！！
 */
// if (process.env.NODE_ENV === 'production') {
//   const { mockXHR } = require('../mock') // 引入 mock 数据
//   mockXHR() // 执行 mock 数据
// }

// 设置 Element UI 的语言为中文
Vue.use(ElementUI, { locale })
// 如果想要中文版 Element UI，按如下方式声明
// Vue.use(ElementUI)

Vue.config.productionTip = false // 关闭生产模式下的提示信息

// 创建 Vue 实例
new Vue({
  el: '#app', // 挂载到 id 为 app 的 DOM 元素上
  router, // 配置路由
  store, // 配置 Vuex 状态管理
  render: h => h(App) // 渲染根组件
})