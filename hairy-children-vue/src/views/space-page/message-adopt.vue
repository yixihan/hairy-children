<template>
  <n-card>
    <!-- 面包屑导航 -->
    <n-breadcrumb>
      <n-breadcrumb-item href=".."> 个人中心 </n-breadcrumb-item>
      <n-breadcrumb-item> 申请的领养 </n-breadcrumb-item>
    </n-breadcrumb>
    <message-adopt-item v-for="item in list" :key="item.id" :item="item"></message-adopt-item>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NBreadcrumb, NBreadcrumbItem } from 'naive-ui'
import { getUserInfo, getReceiveAdoptionNotices } from '../../api'
import messageAdoptItem from '../../components/message-adopt-item.vue'

export default defineComponent({
  name: 'HistoryArticle',
  components: {
    NCard,
    NBreadcrumb,
    NBreadcrumbItem,
    messageAdoptItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
      getReceiveAdoptionNotices().then((res) => {
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
