import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'

Vue.config.productionTip = false

let userVersion = localStorage.getItem("version") || '';
let releaseVersion = require('@/assets/system.json').version;
if (releaseVersion !== userVersion) {
    localStorage.clear();
    localStorage.setItem("version", releaseVersion);
}
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
