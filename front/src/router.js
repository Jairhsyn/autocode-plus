import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            component: () => import('./views/Home.vue'),
            children: [
                {
                    path: '',
                    name: 'home',
                    redirect: '/init',
                },
                {
                    path: '/select',
                    name: 'select',
                    component: () => import('./views/Select.vue')
                },
                {
                    path: '/config',
                    name: 'config',
                    component: () => import('./views/Config.vue')
                },
                {
                    path: '/custom',
                    name: 'custom',
                    component: () => import('./views/Custom.vue')
                }
            ]
        },
        {
            path: '/init',
            name: 'init',
            component: () => import('./views/InitDb.vue')
        },
        {
            path: '/about',
            name: 'about',
            // route level code-splitting
            // this generates a separate chunk (about.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
        },

    ]
})
