import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './index.css'

import 'flowbite';

const app = createApp(App)

app.use(router)

app.mount('#app')