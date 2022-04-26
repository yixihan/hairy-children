import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './axios.js'
import './permission.js' // 路由拦截
import axios from 'axios'
import VueAxios from 'vue-axios'
import ElementUi from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.config.productionTip = false

Vue.use(VueAxios, axios)
Vue.use(ElementUi)
Vue.use(mavonEditor)
Vue.use(router)




Vue.prototype.$axios=axios

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
