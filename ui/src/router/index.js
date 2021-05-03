import Vue from 'vue'
import VueRouter from 'vue-router'

import App from '@/DefaultApp.vue'
import Employee from '@/views/Employee.vue'

Vue.use(VueRouter)

const router = new VueRouter({
    mode: 'history',
    routes: [
        { path: '/', component: App },
        { path: '/employee', component: Employee }
    ]
});
export default router