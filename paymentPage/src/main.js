// 从vue包里引入vue组件
import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
 //import xx from './test.js'
import router from "@/router/index"
import http from "./utils/request"
import store from "./store/index"
Vue.use(ElementUI);
Vue.prototype.$axios = http;
//引入vant(移动端组件)
import Vant from 'vant';
import 'vant/lib/index.css';
Vue.use(Vant);

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')
