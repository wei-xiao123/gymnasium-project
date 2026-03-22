import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router/index'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { createPinia } from 'pinia'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import objCopy from './utils/objCopy'
import myconfirm from './utils/myconfirm'

// 实例化 Pinia
const pinia = createPinia()
const app = createApp(App)

// 挂载全局属性
app.config.globalProperties.$objCopy = objCopy
app.config.globalProperties.$myconfirm = myconfirm

app.use(router)
  .use(ElementPlus, {
    locale: zhCn,
  })
  .use(pinia)

app.mount('#app')

// 全局注册图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}