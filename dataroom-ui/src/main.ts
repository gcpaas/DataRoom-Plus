import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'normalize.css'
import '@/assets/base.css'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import VueGridLayout from 'vue-grid-layout-v3'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.use(VueGridLayout);
app.mount('#app')
