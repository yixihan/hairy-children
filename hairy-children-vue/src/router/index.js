import Vue from 'vue'
import VueRouter from 'vue-router'

import Main from '../views/main.vue'
import Register from '../views/v/register.vue'
import Login from '../views/v/login.vue'
import Test from '../views/v/test.vue'
import ResetPassword from '../views/v/resetPassword.vue'

Vue.use(VueRouter);

// 新增修改方法：获取原型对象上的push函数
const originalPush = VueRouter.prototype.push
// 新增修改方法：修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'main',
    redirect: '/main',
    meta: {
      requireAuth: false
    }
  },
  {
    path: '/main',
    name: 'main',
    component: Main,
    meta: {
      requireAuth: false
    },
    children: [
      {
        path: '/login',
        name: 'login',
        component: Login,
        meta: {
          requireAuth: false
        }
      },
      {
        path: '/register',
        name: 'register',
        component: Register,
        meta: {
          requireAuth: false
        }
      },
      {
        path: '/test',
        name: 'test',
        component: Test,
        meta: {
          requireAuth: true
        }
      },
      {
        path: '/resetPassword',
        name: 'resetPassword',
        component: ResetPassword,
        meta: {
          requireAuth: false
        }
      }
    ]

  },
  
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router