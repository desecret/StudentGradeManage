import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from "element-plus/es/locale/lang/zh-cn";
import './assets/css/global.css'
import Echarts from "vue-echarts"
import * as echarts from "echarts"

const app = createApp(App);

app.use(ElementPlus, { locale: zhCn })
    .use(router).mount('#app')

app.component("v-chart", Echarts)

app.config.globalProperties.$echarts = echarts

//全局注册图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}