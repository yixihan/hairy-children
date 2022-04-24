<template>
  <n-card>
    <!-- 面包屑导航 -->
    <n-breadcrumb>
      <n-breadcrumb-item href=".."> 个人中心 </n-breadcrumb-item>
      <n-breadcrumb-item> 我的收藏 </n-breadcrumb-item>
      <!-- <n-breadcrumb-item> {{ collectionName }} </n-breadcrumb-item> -->
      <n-breadcrumb-item> 收藏夹 </n-breadcrumb-item>
    </n-breadcrumb>
    <history-article-item v-for="item in list" :key="item.id" :item="item"></history-article-item>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NBreadcrumb, NBreadcrumbItem } from 'naive-ui'
import { getUserInfo, getFavoriteArticle, getArticle } from '../../api'
import historyArticleItem from '../../components/history-article-item.vue'

export default defineComponent({
  name: 'HistoryArticle',
  components: {
    NCard,
    NBreadcrumb,
    NBreadcrumbItem,
    historyArticleItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
      getFavoriteArticle({ userCollectionId: to.query.id }).then((res) => {
        res.data.data.page.list.forEach((item) => {
          getArticle({ titleId: item.titleId }).then((ress) => {
            vm.list.push(ress.data.data.title)
          })
        })
      })
      // getUserArticle({ userId: getData('userInfo').userId }).then((res) => {
      //   vm.setState('list', res.data.data.page.list)
      // })
    })
  },
  setup() {
    const state = reactive({
      identifyInfo: {
        realName: '',
        identityCard: ''
      },
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
