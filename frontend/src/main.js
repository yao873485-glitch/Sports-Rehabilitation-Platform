import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// Element UI
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// Moment.js
import moment from 'moment'
import 'moment/locale/zh-cn'

// 样式
import '@/styles/index.scss'

// 工具类
import '@/utils/index.js'

// 全局组件
import '@/components/index.js'

// 将moment挂载到Vue原型上
Vue.prototype.$moment = moment

Vue.use(ElementUI, { size: 'medium' })

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
