<template>
  <n-h1 strong prefix="bar" style="margin: 20px 0">{{ title.titleName }}</n-h1>
  <n-card style="margin: 20px 0">
    <n-grid :cols="24">
      <n-gi :span="6" style="border-right: 1px solid #ddd">
        <n-space align="center" vertical>
          <n-avatar round :src="'http://175.24.229.41:9421/' + userInfo.userAvatar" :size="60" object-fit="cover"></n-avatar>
          <n-text>{{ userInfo.userName }}</n-text>
        </n-space>
      </n-gi>
      <n-gi :span="18">
        <n-space vertical>
          <v-md-preview :text="title.titleContent ? title.titleContent : 'null'"></v-md-preview>
          <n-space justify="flex-end">
            <n-button v-if="getData('userInfo').userId === userInfo.userId" text @click="EditArticle">编辑</n-button>
            <span class="user-address">{{ title.userAddress }}</span>
            <n-time :time="title.gmtCreate" type="relative" />
            <div class="center">
              <n-icon size="16">
                <Favorite />
              </n-icon>
              <span class="like"> {{ title.likeCount }}</span>
            </div>
            <div class="center">
              <n-icon size="16">
                <Chat />
              </n-icon>
              <span class="comment"> {{ title.commentCount }}</span>
            </div>
            <div class="center">
              <n-icon size="16">
                <Star />
              </n-icon>
              <span class="collect"> {{ title.collectionCount }}</span>
            </div>
            <span v-if="title.isFinish === 1" class="status" style="color: green">{{ title.titleType === 1 ? '未领养' : '未寻回' }}</span>
            <span v-else class="status" style="color: red">{{ title.titleType === 1 ? '已领养' : '已寻回' }}</span>
          </n-space>
        </n-space>
      </n-gi>
    </n-grid>
  </n-card>
  <n-space vertical justify="center">
    <n-input
      v-model:value="comment"
      type="textarea"
      placeholder="请输入你的评论"
      :autosize="{
        minRows: 3,
        maxRows: 3
      }"
    />
    <n-button @click="AddRootComment">发布</n-button>
  </n-space>
  <comment-item v-for="item in comments?.list" :key="item.rootId" :item="item"> </comment-item>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import { NCard, NGrid, NGi, NSpace, NAvatar, NText, NTime, NIcon, NH1, NInput, NButton, useMessage } from 'naive-ui'
import { Favorite, Star, Chat } from '@vicons/carbon'
import commentItem from '../components/comment-item.vue'
import { getArticle, getUserInfo, addRootComment, getArticleComments } from '../api/index'
import { getData } from '../utils/tools'

export default defineComponent({
  name: 'ArticlePage',
  components: {
    NCard,
    NGrid,
    NGi,
    NSpace,
    NAvatar,
    NText,
    Favorite,
    Star,
    Chat,
    NTime,
    NIcon,
    NH1,
    NInput,
    NButton,
    commentItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.GetArticle(to.params.id)
    })
  },
  setup() {
    const router = useRouter()
    const message = useMessage()
    const state = reactive({
      title: {},
      userInfo: {},
      comment: '',
      comments: []
    })
    const GetArticle = async (data) => {
      const { data: res } = await getArticle({ titleId: data })
      state.title = res.data.title
      const { data: ress } = await getUserInfo({ userId: state.title.userId })
      state.userInfo = ress.data
      const { data: resss } = await getArticleComments({ titleId: state.title.titleId })
      // ?.可选参数，防止无评论时报错
      state.comments = resss.data?.page
    }
    const GetUserInfo = async () => {
      const { data: res } = await getUserInfo()
      state.userInfo = res.data
    }
    const AddRootComment = async () => {
      const { data: res } = await addRootComment({
        answerId: state.title.titleId,
        content: state.comment
      })
      if (res.code === 200) {
        message.success('评论成功')
        router.go(0)
      } else {
        message.error(res.msg)
      }
    }
    const EditArticle = () => {
      router.push({
        name: 'write',
        params: {
          id: state.title.titleId
        }
      })
    }
    return { ...toRefs(state), GetArticle, GetUserInfo, AddRootComment, getData, EditArticle }
  }
})
</script>
<style lang="scss">
.center {
  display: flex;
  align-items: center;
}
.status {
  font-weight: 700;
}
.list-item {
  padding: 20px 0;
  border-bottom: 1px solid #999;
  background-color: #fff;
  .item-title {
    display: block;
    font-size: 20px;
    font-weight: 700;
  }
  .item-content {
    font-size: 16px;
  }
}
</style>
