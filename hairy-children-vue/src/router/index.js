import { createRouter, createWebHistory } from 'vue-router'
import Index from '../views/Index.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: Index,
      redirect: '/home',
      children: [
        {
          path: '/home',
          name: 'home',
          component: () => import('../views/Home.vue')
        },
        {
          path: '/login',
          name: 'login',
          component: () => import('../views/Login.vue')
        },
        {
          path: '/register',
          name: 'register',
          component: () => import('../views/Register.vue')
        }
      ]
    }
  ]
})

export default router
