<template>
  <n-card style="margin: 20px 0">
    <n-grid :cols="24" style="margin-bottom: 20px">
      <n-gi :span="6" style="border-right: 1px solid #ddd">
        <n-space align="center" vertical>
          <!-- 拼接服务器地址使 图片 URL 能够访问到 -->
          <n-avatar round :src="'http://175.24.229.41:9421/' + item.userAvatar" :size="60" object-fit="cover"></n-avatar>
          <n-text>{{ item.userName }}</n-text>
        </n-space>
      </n-gi>
      <n-gi :span="18">
        <n-space vertical>
          <!-- 防止内容为空时控制台报错 -->
          <v-md-preview :text="item.content ? item.content : '内容为空'"></v-md-preview>
          <n-space justify="flex-end">
            <n-time :time="item.gmtCreate" type="relative" />
            <div class="center">
              <n-icon size="16">
                <Favorite />
              </n-icon>
              <span class="like"> {{ item.likeCount }}</span>
            </div>
            <div class="center" @click="replyDisplay(item.rootId)">
              <n-icon size="16">
                <Chat />
              </n-icon>
              <span class="comment"> {{ item.replyCount }}</span>
            </div>
          </n-space>
        </n-space>
      </n-gi>
    </n-grid>
    <n-grid v-for="son in item.commentReplyList" :key="son.replyId" :cols="24">
      <n-gi :offset="7" :span="17" style="border-top: 1px solid #ddd">
        <n-space vertical style="margin-top: 10px">
          <n-space align="center">
            <n-avatar round :src="'http://175.24.229.41:9421/' + item.userAvatar" :size="30" object-fit="cover"></n-avatar>
            <n-text strong>
              {{ son.userName }}
            </n-text>
          </n-space>
          <p>{{ son.content ? son.content : 'null' }}</p>
          <n-space justify="flex-start">
            <n-time :time="son.gmtCreate" type="relative" />
          </n-space>
        </n-space>
      </n-gi>
    </n-grid>
  </n-card>
  <n-space v-if="isReply" vertical justify="center">
    <n-input
      v-model:value="comment"
      type="textarea"
      placeholder="请输入你的评论"
      :autosize="{
        minRows: 3,
        maxRows: 3
      }"
    />
    <n-button @click="AddSonComment">发布</n-button>
  </n-space>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NGrid, NGi, NSpace, NAvatar, NText, NTime, NIcon, NInput, NButton } from 'naive-ui'
import { Favorite, Chat } from '@vicons/carbon'
import { addSonComment } from '../api/index'
import { getData } from '../utils/tools'

export default defineComponent({
  components: {
    NCard,
    NGrid,
    NGi,
    NSpace,
    NAvatar,
    NText,
    NTime,
    NIcon,
    NInput,
    NButton,
    Favorite,
    Chat
  },
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const state = reactive({
      userInfo: {},
      isReply: false,
      replyInfo: {},
      comment: '',
      userName: ''
    })
    // onMounted(async () => {
    //   const { data: res } = await getUserInfo({
    //     userId: props.item.userId
    //   })
    //   state.userInfo = res.data
    // })
    // const getUserName = async (userId) => {
    //   // console.log(userId)
    //   const { data: res } = await getUserInfo({
    //     userId
    //   })
    //   // console.log(res.data.userName)
    //   state.userName = res.data.userName
    //   return state.userName
    // }

    // 是否展示回复框
    const replyDisplay = (data) => {
      state.isReply = !state.isReply
      state.replyInfo = {
        userId: getData('userInfo').userId,
        rootId: data
      }
    }
    // 添加评论中的子评论
    const AddSonComment = async () => {
      const { data: res } = await addSonComment(props.item.answerId, {
        ...state.replyInfo,
        content: state.comment
      })
      if (res.code === 200) {
        // 成功后清空输入框
        state.comment = ''
      }
    }
    return { ...toRefs(state), replyDisplay, AddSonComment }
  }
})
</script>
<style lang="scss">
p {
  margin: 0;
}
</style>
