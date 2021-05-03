import Vue from 'vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import App from './App.vue'
import router from './router'

Vue.config.productionTip = false

import * as filters from './filters'
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

new Vue({
  router,
  components: {
    'App': App
  },
  render: h => h(App),
}).$mount('#app')