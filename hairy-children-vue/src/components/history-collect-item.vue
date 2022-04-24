<template>
  <n-card style="margin: 20px 0">
    <n-h2>{{ item.collectionName }}</n-h2>
    <!-- <n-text>{{ abstractFn(90, item.titleContent) + ' ……' }}</n-text> -->
    <n-space justify="space-between" align="center">
      <n-space>
        <!-- <n-text>{{ item.titleType === 1 ? '收养贴' : '寻宠贴' }}</n-text> -->
        <n-text>数量：{{ item.collectionCount }}</n-text>
        <n-text>更新时间： <n-time :time="item.gmtCreate" type="relative" /></n-text>
      </n-space>
      <n-space>
        <n-button @click="deleteCollection">删除</n-button>
        <n-button @click="goToCollectionDetail">查看详情</n-button>
      </n-space>
    </n-space>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import { NCard, NSpace, NText, NTime, NButton, NH2, useMessage, useDialog } from 'naive-ui'
import { deleteFavorite } from '../api/index'
import { getData, abstractFn } from '../utils/tools'

export default defineComponent({
  components: {
    NCard,
    NSpace,
    NText,
    NTime,
    NButton,
    NH2
  },
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const message = useMessage()
    const router = useRouter()
    const dialog = useDialog()
    const state = reactive({
      userInfo: {},
      comment: '',
      userName: '',
      showDeleteModal: false,
      articleInfo: {}
    })
    const goToCollectionDetail = () => {
      router.push({
        name: 'space-history-collect-detail',
        query: {
          id: props.item.collectionId
        }
      })
    }
    const deleteCollection = async () => {
      dialog.warning({
        title: '警告',
        content: '你确定？',
        positiveText: '确定',
        negativeText: '不确定',
        onPositiveClick: async () => {
          const { data: res } = await deleteFavorite({ userCollectionId: props.item.collectionId })
          if (res.code === 200) {
            message.success('删除成功')
            router.push({
              name: 'space-history-collect'
            })
            router.go(0)
          }
        },
        onNegativeClick: () => {
          message.error('取消删除')
        }
      })
    }
    return { ...toRefs(state), getData, abstractFn, goToCollectionDetail, deleteCollection }
  }
})
</script>
<style lang="scss">
p {
  margin: 0;
}
</style>
