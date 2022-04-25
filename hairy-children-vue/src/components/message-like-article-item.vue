<template>
  <n-card style="margin: 20px 0">
    <!-- <n-h2>{{ articleInfo.titleName }}</n-h2> -->
    <n-space style="font-size: 18px">
      <n-text>{{ item.sendUserName + ' 点赞了你的帖子: ' + articleInfo.titleName }}</n-text>
    </n-space>
    <n-space justify="space-between" align="center">
      <n-space>
        <!-- <n-text>{{ item.titleType === 1 ? '收养贴' : '寻宠贴' }}</n-text> -->
        <n-time :time="item.gmtCreate" type="relative" />
      </n-space>
      <n-button @click="goToArticle">查看详情</n-button>
    </n-space>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { NCard, NSpace, NText, NTime, NButton } from 'naive-ui'
import { getArticle } from '../api/index'
import { getData, abstractFn } from '../utils/tools'

export default defineComponent({
  components: {
    NCard,
    NSpace,
    NText,
    NTime,
    NButton
  },
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    // const message = useMessage()
    const router = useRouter()
    const state = reactive({
      userInfo: {},
      comment: '',
      userName: '',
      showDeleteModal: false,
      articleInfo: {}
    })

    onMounted(async () => {
      const { data: res } = await getArticle({ titleId: props.item.titleId })
      state.articleInfo = res.data.title
    })
    const goToArticle = () => {
      router.push({
        path: `/article/${props.item.titleId}`
      })
    }
    return { ...toRefs(state), getData, abstractFn, goToArticle }
  }
})
</script>
<style lang="scss">
p {
  margin: 0;
}
</style>
