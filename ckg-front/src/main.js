import Vue from "vue"
import App from "./App.vue"
import router from "./router"
import store from "./store"
import ElementUI from "element-ui"
import "element-ui/lib/theme-chalk/index.css"
import axios from "axios"

import {postRequest, putRequest, getRequest, deleteRequest} from './utils/api'

Vue.config.productionTip = false

Vue.use(ElementUI)
//Vue.use(axios);
Vue.prototype.$axios = axios;
Vue.prototype.postRequest = postRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.deleteRequest = deleteRequest;
new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app")
