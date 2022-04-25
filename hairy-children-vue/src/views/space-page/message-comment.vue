<template>
  <n-card>
    <!-- 面包屑导航 -->
    <n-breadcrumb>
      <n-breadcrumb-item href=".."> 个人中心 </n-breadcrumb-item>
      <n-breadcrumb-item> 过往评论 </n-breadcrumb-item>
    </n-breadcrumb>
    <message-comment-item v-for="item in list" :key="item.id" :item="item"></message-comment-item>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NBreadcrumb, NBreadcrumbItem } from 'naive-ui'
import { getUserInfo, getAllCommentNotices } from '../../api'
import { getData } from '../../utils/tools'
import messageCommentItem from '../../components/message-comment-item.vue'

export default defineComponent({
  name: 'HistoryArticle',
  components: {
    NCard,
    NBreadcrumb,
    NBreadcrumbItem,
    messageCommentItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
      getAllCommentNotices({ userId: getData('userInfo').userId }).then((res) => {
        vm.setState('list', res.data.data.page.list)
      })
    })
  },
  setup() {
    const state = reactive({
      userInfo: {},
      list: []
    })
    const setState = (name, data) => {
      state[name] = data
    }
    return { ...toRefs(state), setState }
  }
})
</script>
<style lang="scss" scoped></style>
