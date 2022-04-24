<template>
  <n-layout>
    <n-layout-header>
      <n-breadcrumb>
        <n-breadcrumb-item href="/home"> 首页 </n-breadcrumb-item>
        <n-breadcrumb-item href="#"> 个人中心 </n-breadcrumb-item>
      </n-breadcrumb>
    </n-layout-header>
    <n-layout has-sider>
      <n-layout-sider content-style="padding: 24px;">
        <n-menu mode="vertical" accordion :options="menuOptions"></n-menu>
      </n-layout-sider>
      <n-layout-content content-style="padding: 24px;">
        <router-view></router-view>
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>
<script>
import { defineComponent, reactive, toRefs, h } from 'vue'
import { NLayout, NLayoutHeader, NLayoutSider, NLayoutContent, NBreadcrumb, NBreadcrumbItem, NMenu } from 'naive-ui'
import { RouterView, RouterLink } from 'vue-router'
import { getUserInfo } from '../api'
import { getData } from '../utils/tools'

export default defineComponent({
  name: 'SpacePage',
  components: {
    NLayout,
    NLayoutHeader,
    NLayoutSider,
    NLayoutContent,
    RouterView,
    NBreadcrumb,
    NBreadcrumbItem,
    NMenu
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
    })
  },
  setup() {
    const state = reactive({
      menuOptions: [
        {
          label: () =>
            h(
              RouterLink,
              {
                to: {
                  name: 'space-userinfo',
                  query: {
                    userId: getData('token').userId
                  }
                }
              },
              { default: () => '账号信息' }
            ),
          key: 'userinfo'
        },
        {
          label: () =>
            h(
              RouterLink,
              {
                to: {
                  name: 'space-identify',
                  query: {
                    userId: getData('token').userId
                  }
                }
              },
              { default: () => '实名认证' }
            ),
          key: 'identify'
        },
        {
          label: '历史信息',
          key: 'history',
          children: [
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      name: 'space-history-article',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '过往发布' }
                ),
              key: 'history-article'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/history/comment',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '过往评论' }
                ),
              key: 'history-comment'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/history/clue',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '提交的线索' }
                ),
              key: 'history-clue'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/history/adopt',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '申请的领养' }
                ),
              key: 'history-adopt'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: 'space/history/favorite',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '我的收藏' }
                ),
              key: 'history-favorite'
            }
          ]
        },
        {
          label: '个人信箱',
          key: 'message',
          children: [
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/message/adopt',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '收到的领养申请' }
                ),
              key: 'message-adopt'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/message/comment',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '收到的评论' }
                ),
              key: 'message-comment'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/message/reply',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '收到的回复' }
                ),
              key: 'message-reply'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/message/clue',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '收到的线索' }
                ),
              key: 'message-clue'
            },
            {
              label: () =>
                h(
                  RouterLink,
                  {
                    to: {
                      path: '/space/message/like',
                      query: {
                        userId: getData('token').userId
                      }
                    }
                  },
                  { default: () => '收到的赞' }
                ),
              key: 'message-like'
            }
          ]
        }
      ],
      userInfo: {}
    })
    const setState = (name, data) => {
      state[name] = data
    }

    return { ...toRefs(state), setState }
  }
})
</script>
<style lang="scss" scoped></style>
