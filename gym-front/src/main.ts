import { createApp } from "vue";
import App from "./App.vue";
import router from "@/router/index";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { createPinia } from "pinia";
import piniaPersist from "pinia-plugin-persist";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import objCopy from "./utils/objCopy";
import myconfirm from "./utils/myconfirm";
import { permissionDirective } from "./permission";
import * as echarts from "echarts";

// 实例化 Pinia
const pinia = createPinia();
pinia.use(piniaPersist);
const app = createApp(App);

// 挂载全局属性
app.config.globalProperties.$objCopy = objCopy;
app.config.globalProperties.$myconfirm = myconfirm;
// 挂载echarts
app.config.globalProperties.$echarts = echarts;

// 注册权限指令
app.directive("permission", permissionDirective);

app
  .use(router)
  .use(ElementPlus, {
    locale: zhCn,
  })
  .use(pinia);

app.mount("#app");

// 全局注册图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}