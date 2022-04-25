<template>
  <n-card style="margin: 20px 0">
    <!-- <n-h2>{{ articleInfo.titleName }}</n-h2> -->
    <n-space style="font-size: 18px">
      <n-text>{{ item.sendUserName + ' 回复了你的评论: ' + abstractFn(40, commentInfo.commentContent) }}</n-text>
    </n-space>
    <n-text>{{ abstractFn(90, item.replyContent) + ' ……' }}</n-text>
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
import { getCommentContent } from '../api/index'
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
      commentInfo: {}
    })

    onMounted(async () => {
      const { data: res } = await getCommentContent({ id: props.item.rootId })
      state.commentInfo = res.data.message
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
