import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'
import permission from './modules/permission'
import tagsView from './modules/tagsView'

Vue.use(Vuex) // 注册 Vuex 插件，使其可以在 Vue 中使用

// 创建一个 Vuex.Store 实例
const store = new Vuex.Store({
  modules: {
    app, // 将 app 模块注册到 store 中
    settings, // 将 settings 模块注册到 store 中
    user, // 将 user 模块注册到 store 中
    permission,
    tagsView
  },
  getters // 将 getters 注册到 store 中，用于访问状态
})

export default store
