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
        },
        {
          path: '/list',
          name: 'list',
          component: () => import('../views/List.vue')
        },
        {
          path: '/write',
          name: 'write',
          component: () => import('../views/Editor.vue')
        },
        {
          path: '/article/:id',
          name: 'article',
          component: () => import('../views/Article.vue')
        },
        {
          path: '/:userId/space',
          name: 'space',
          component: () => import('../views/Space.vue'),
          children: [
            {
              path: 'userinfo',
              name: 'space-userinfo',
              component: () => import('../views/space-page/user-info.vue')
            },
            // 实名认证
            {
              path: 'identify',
              name: 'space-identify',
              component: () => import('../views/space-page/Identify.vue')
            }
          ]
        }
      ]
    }
  ]
})

export default router
