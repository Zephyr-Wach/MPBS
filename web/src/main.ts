import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入 Element Plus 及样式
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)
app.use(router)
app.use(ElementPlus)  // 一定要挂载 Element Plus 插件
app.mount('#app')

