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
      <n-gi :span="18" style="padding: 0 32px">
        <n-space vertical>
          <!-- 防止内容为空时控制台报错 -->
          <n-form label-align="left" label-placement="left" label-width="auto">
            <n-form-item label="理由">{{ item.adoptReason }}</n-form-item>
            <n-form-item label="城市">{{ item.adoptUserAddress }}</n-form-item>
            <n-form-item label="年龄">{{ item.adoptUserAge }}</n-form-item>
            <n-form-item label="联系方式">{{ item.adoptUserPhone }}</n-form-item>
            <n-form-item label="养宠理念">{{ item.adoptConcept }}</n-form-item>
            <n-form-item label="接动物方式">{{ item.adoptWay }}</n-form-item>
            <n-form-item label="是否接受定期回访">{{ item.isReturnVisit === 1 ? '是' : '否' }}</n-form-item>
            <n-form-item label="是否能够定期反馈领养情况">{{ item.isFeedback === 1 ? '是' : '否' }}</n-form-item>
          </n-form>
          <n-image-group>
            <n-space>
              <!-- imgs url最后一张末尾有':'，所以分隔字符串后选取第一个 -->
              <n-image v-for="img in item.imgs" :key="img" :src="'http://175.24.229.41:9421/' + img.split(':')[0]" width="200"></n-image>
            </n-space>
          </n-image-group>
          <n-space justify="flex-end">
            <n-time :time="item.gmtCreate" type="relative" />
            <n-button v-if="getData('userInfo').userId === item.userId" text @click="showDeleteModal = true">删除</n-button>
          </n-space>
        </n-space>
      </n-gi>
    </n-grid>
    <n-modal
      v-model:show="showDeleteModal"
      :mask-closable="false"
      preset="dialog"
      title="警告"
      content="确认删除该领养信息吗？"
      positive-text="确认"
      negative-text="算了"
      @positive-click="onDeleteClick"
    />
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import {
  NCard,
  NGrid,
  NGi,
  NSpace,
  NAvatar,
  NText,
  NTime,
  NButton,
  NForm,
  NFormItem,
  NModal,
  useMessage,
  NImage,
  NImageGroup
} from 'naive-ui'
// import { Favorite, Chat } from '@vicons/carbon'
import { addSonComment, deleteAdoption } from '../api/index'
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
    // NIcon,
    // NInput,
    NButton,
    // Favorite,
    // Chat,
    NForm,
    NFormItem,
    NModal,
    NImage,
    NImageGroup
  },
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const message = useMessage()
    const state = reactive({
      userInfo: {},
      isReply: false,
      replyInfo: {},
      comment: '',
      userName: '',
      showDeleteModal: false
    })

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
    // 删除领养信息
    const onDeleteClick = async () => {
      const { data: res } = await deleteAdoption({ adoptId: props.item.adoptId })
      if (res.code === 200) {
        // 删除成功后关闭弹窗
        state.showDeleteModal = false
        message.success('删除成功')
        // 刷新页面
        window.location.reload()
      } else {
        message.error('删除失败')
      }
    }
    return { ...toRefs(state), replyDisplay, AddSonComment, onDeleteClick, getData }
  }
})
</script>
<style lang="scss">
p {
  margin: 0;
}
</style>
