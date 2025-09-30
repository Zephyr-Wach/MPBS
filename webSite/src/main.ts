import { createApp } from "vue"
import App from "./App.vue"
import router from "./router/router.ts"
import i18n from "./i18n"
import "./styles/index.css"
import { createPinia } from 'pinia'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(i18n)
app.mount("#app")
