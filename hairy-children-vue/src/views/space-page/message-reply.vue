<template>
  <n-card>
    <!-- 面包屑导航 -->
    <n-breadcrumb>
      <n-breadcrumb-item href=".."> 个人中心 </n-breadcrumb-item>
      <n-breadcrumb-item> 收到的回复 </n-breadcrumb-item>
    </n-breadcrumb>
    <message-reply-item v-for="item in list" :key="item.id" :item="item"></message-reply-item>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NBreadcrumb, NBreadcrumbItem } from 'naive-ui'
import { getUserInfo, getAllReplyNotices } from '../../api'
import { getData } from '../../utils/tools'
import messageReplyItem from '../../components/message-reply-item.vue'

export default defineComponent({
  name: 'HistoryArticle',
  components: {
    NCard,
    NBreadcrumb,
    NBreadcrumbItem,
    messageReplyItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
      getAllReplyNotices({ userId: getData('userInfo').userId }).then((res) => {
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
