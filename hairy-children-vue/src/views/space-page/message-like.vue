<template>
  <n-card>
    <!-- 面包屑导航 -->
    <n-breadcrumb>
      <n-breadcrumb-item href=".."> 个人中心 </n-breadcrumb-item>
      <n-breadcrumb-item> 个人信箱 </n-breadcrumb-item>
      <n-breadcrumb-item> 收到的赞 </n-breadcrumb-item>
    </n-breadcrumb>
    <n-h3>文章</n-h3>
    <message-like-article-item v-for="item in articleList" :key="item.id" :item="item"></message-like-article-item>
    <n-h3>评论</n-h3>
    <message-like-comment-item v-for="item in commentList" :key="item.id" :item="item"></message-like-comment-item>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NBreadcrumb, NBreadcrumbItem, NH3 } from 'naive-ui'
import { getUserInfo, getAllLikeNotices, getAllCommentLikeNotices } from '../../api'
import { getData } from '../../utils/tools'
import messageLikeArticleItem from '../../components/message-like-article-item.vue'
import messageLikeCommentItem from '../../components/message-like-comment-item.vue'

export default defineComponent({
  name: 'HistoryArticle',
  components: {
    NCard,
    NBreadcrumb,
    NBreadcrumbItem,
    messageLikeArticleItem,
    messageLikeCommentItem,
    NH3
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
      getAllLikeNotices({ userId: getData('userInfo').userId }).then((res) => {
        vm.setState('articleList', res.data.data.page.list)
      })
      getAllCommentLikeNotices({ userId: getData('userInfo').userId }).then((res) => {
        vm.setState('commentList', res.data.data.page.list)
      })
    })
  },
  setup() {
    const state = reactive({
      userInfo: {},
      articleList: [],
      commentList: []
    })
    const setState = (name, data) => {
      state[name] = data
    }
    return { ...toRefs(state), setState }
  }
})
</script>
<style lang="scss" scoped></style>
