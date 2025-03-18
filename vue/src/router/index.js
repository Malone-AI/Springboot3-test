import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/manager/home'},
    { path: '/manager',
      component: () => import('../views/Manager.vue'),
      children: [
        {path: 'home', component: () => import('../views/Home.vue')},
        {path: 'admin', component: () => import('../views/Admin.vue')},
        {path: 'user', component: () => import('../views/User.vue')}
      ]
    },
    { path: '/login', component: import('../views/login.vue'),},
    { path: '/register', component: import('../views/register.vue'),},
    { path: '/notFound', component: import('../views/404.vue'),},
    { path: '/:pathMatch(.*)', redirect: '/notFound'}
  ],
})

export default router
