import Vue from 'vue'
import VueRouter from 'vue-router'

import Home from '../views/Home.vue'
import Index from '@/views/index.vue'
import Register from '../views/v/register.vue'
import Login from '../views/v/login.vue'
import Test from '../views/v/test.vue'
import ResetPassword from '../views/v/resetPassword.vue'
import Center from '@/views/user/Center'
import UserInfo from '@/views/user/UserInfo'
import Authentication from '@/views/user/setting/Authentication'
import UserPhone from '@/views/user/setting/UserPhone'
import UserEmail from '@/views/user/setting/UserEmail'
import UserPassword from '@/views/user/setting/UserPassword'
import UserAdopt from '@/views/user/history/UserAdopt'
import UserClue from '@/views/user/history/UserClue'
import UserComment from '@/views/user/history/UserComment'
import UserArticle from '@/views/user/history/UserArticle'
import UserAdoptMsg from '@/views/user/msg/UserAdoptMsg'
import UserClueMsg from '@/views/user/msg/UserClueMsg'
import UserCommentMsg from '@/views/user/msg/UserCommentMsg'
import UserCommentReplyMsg from '@/views/user/msg/UserCommentReplyMsg'
import UserLikeMsg from '@/views/user/msg/UserLikeMsg'

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
    name: 'Home',
    component: Home,
    redirect: '/',
    meta: {
      requireAuth: false
    },
    children: [
      {
        path: '/',
        name: 'index',
        component: Index,
        meta: {
          requireAuth: false
        }
      },
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
      },
      {
        path: '/center/:userId',
        name: 'center',
        component: Center,
        redirect: '/center/:userId/userInfo',
        meta: {
          requireAuth: true
        },
        children: [
          {
            path: '/center/:userId/userInfo',
            name: '账户信息',
            component: UserInfo,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/setting/authentication',
            name: '实名认证',
            component: Authentication,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/setting/email',
            name: '邮箱设置',
            component: UserEmail,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/setting/phone',
            name: '电话设置',
            component: UserPhone,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/setting/password',
            name: '更改密码',
            component: UserPassword,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/history/adopt',
            name: '申请的领养',
            component: UserAdopt,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/history/clue',
            name: '提交的线索',
            component: UserClue,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/history/article',
            name: '过往发布',
            component: UserArticle,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/history/comment',
            name: '过往评论',
            component: UserComment,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/message/adopt',
            name: '收到的领养申请',
            component: UserAdoptMsg,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/message/clue',
            name: '收到的线索',
            component: UserClueMsg,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/message/comment',
            name: '收到的评论',
            component: UserCommentMsg,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/message/commentReply',
            name: '收到的回复',
            component: UserCommentReplyMsg,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/message/like',
            name: '收到的赞',
            component: UserLikeMsg,
            meta: {
              requireAuth: true
            },
          },
        ]
      }
    ]
  },
  
  
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  linkExactActiveClass: "active",
  routes
})

export default router