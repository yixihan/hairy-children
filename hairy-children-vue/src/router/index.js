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
import UserFavorites from '@/views/user/collection/UserFavorites'
import FavoriteList from '@/views/user/collection/FavoriteList'
import UserAdopt from '@/views/user/history/UserAdopt'
import UserClue from '@/views/user/history/UserClue'
import UserComment from '@/views/user/history/UserComment'
import UserArticle from '@/views/user/history/UserArticle'
import UserAdoptMsg from '@/views/user/msg/UserAdoptMsg'
import UserClueMsg from '@/views/user/msg/UserClueMsg'
import UserCommentMsg from '@/views/user/msg/UserCommentMsg'
import UserCommentReplyMsg from '@/views/user/msg/UserCommentReplyMsg'
import UserLikeMsg from '@/views/user/msg/UserLikeMsg'
import AdoptSearch from '@/views/search/AdoptSearch'
import FindPetSearch from '@/views/search/FindPetSearch'
import WriteArticle from '@/views/article/WriteArticle'
import WriteAdopt from '@/views/article/WriteAdopt'
import WriteClue from '@/views/article/WriteClue'
import ShowArticle from '@/views/article/ShowArticle'
import ShowAdopt from '@/views/article/ShowAdopt'
import ShowClue from '@/views/article/ShowClue'
import EditArticle from '@/views/article/EditArticle'

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
      // 主页
      {
        path: '/',
        name: 'index',
        component: Index,
        meta: {
          requireAuth: false
        }
      },
      // 测试页面
      {
        path: '/test',
        name: 'test',
        component: Test,
        meta: {
          requireAuth: true
        }
      },
      // 登录
      {
        path: '/login',
        name: 'login',
        component: Login,
        meta: {
          requireAuth: false
        }
      },
      // 注册
      {
        path: '/register',
        name: 'register',
        component: Register,
        meta: {
          requireAuth: false
        }
      },
      // 重置密码
      {
        path: '/resetPassword',
        name: 'resetPassword',
        component: ResetPassword,
        meta: {
          requireAuth: false
        }
      },
      // 个人中心
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
            path: '/center/:userId/favorite',
            name: '收藏夹',
            component: UserFavorites,
            meta: {
              requireAuth: true
            },
          },
          {
            path: '/center/:userId/favorite/:collectionId',
            name: '收藏夹列表',
            component: FavoriteList,
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
      },
      // 搜索相关
      {
        path: '/search/adopt',
        name: 'searchAdopt',
        component: AdoptSearch,
        meta: {
          requireAuth: true
        }
      },
      {
        path: '/search/findPet',
        name: 'searchFindPet',
        component: FindPetSearch,
        meta: {
          requireAuth: true
        }
      },
      // 贴子相关
      {
        path: '/article/create',
        name: 'creatArticle',
        component: WriteArticle,
        meta: {
          requireAuth: true
        },
      },
      {
        path: '/article/:titleId',
        name: 'showArticle',
        component: ShowArticle,
        meta: {
          requireAuth: true
        },
      },
      {
        path: '/article/:titleId/edit',
        name: 'editArticle',
        component: EditArticle,
        meta: {
          requireAuth: true
        },
      },
      // 领养申请
      {
        path: '/adopt/:adoptId/edit',
        name: 'editAdopt',
        component: WriteAdopt,
        meta: {
          requireAuth: true
        },
      },
      {
        path: '/adopt/:adoptId',
        name: 'showAdopt',
        component: ShowAdopt,
        meta: {
          requireAuth: true
        },
      },
      // 线索
      {
        path: '/clue/:clueId/edit',
        name: 'editClue',
        component: WriteClue,
        meta: {
          requireAuth: true
        },
      },
      {
        path: '/clue/:clueId',
        name: 'showClue',
        component: ShowClue,
        meta: {
          requireAuth: true
        },
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