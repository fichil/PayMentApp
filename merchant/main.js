
// #ifndef VUE3
import Vue from 'vue'
import App from './App'
import http from './utils/axios.js'
import store from './store'

Vue.config.productionTip = false

App.mpType = 'app'
Vue.prototype.$axios = http;
Vue.prototype.$store = store;
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
import App from './App.vue'
import http from './utils/axios.js'
import store from './store'

export function createApp() {
  const app = createSSRApp(App)
  app.config.globalProperties.$axios = http;
  app.config.globalProperties.$store = store;
  return {
    app
  }
}
// #endif