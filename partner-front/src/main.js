import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './assets/css/global.css'
//引入Element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//pinia持久化
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const app = createApp(App)
app.use(ElementPlus)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(router)

app.mount('#app')
