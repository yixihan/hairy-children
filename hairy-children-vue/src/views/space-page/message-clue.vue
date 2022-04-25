<template>
  <n-card>
    <!-- 面包屑导航 -->
    <n-breadcrumb>
      <n-breadcrumb-item href=".."> 个人中心 </n-breadcrumb-item>
      <n-breadcrumb-item> 提交的线索 </n-breadcrumb-item>
    </n-breadcrumb>
    <message-clue-item v-for="item in list" :key="item.id" :item="item"></message-clue-item>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NBreadcrumb, NBreadcrumbItem } from 'naive-ui'
import { getUserInfo, getAllClueNotices } from '../../api'
import { getData } from '../../utils/tools'
import messageClueItem from '../../components/message-clue-item.vue'

export default defineComponent({
  name: 'HistoryArticle',
  components: {
    NCard,
    NBreadcrumb,
    NBreadcrumbItem,
    messageClueItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
      getAllClueNotices({ userId: getData('userInfo').userId }).then((res) => {
        vm.setState('list', res.data.data.page.list)
      })
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
