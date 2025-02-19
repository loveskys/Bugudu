import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as localstore from './utils/localstore.js'
import * as validata from '@/utils/validata'
import axios from 'axios';
import apis from './api/index.js'
import './assets/icon/iconfont.css'


//定义全局属性和函数
window.router = router;
const app = createApp(App)

//挂载自定义js
app.config.globalProperties.$apis = apis;
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$localstore = localstore;
app.config.globalProperties.$validata = validata;


app.use(ElementPlus)
app.use(createPinia())
app.use(router)
app.mount('#app')
