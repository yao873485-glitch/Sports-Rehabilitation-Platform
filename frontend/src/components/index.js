import Vue from 'vue'

// 全局注册组件
const components = [
  'Breadcrumb',
  'Hamburger',
  'Pagination'
]

components.forEach(name => {
  Vue.component(name, () => import(`@/components/${name}`))
})
