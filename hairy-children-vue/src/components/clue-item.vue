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
          <v-md-preview :text="item.clueContent ? item.clueContent : '内容为空'"></v-md-preview>
          <n-image-group>
            <n-space style="padding-left: 32px">
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
  </n-card>
  <n-modal
    v-model:show="showDeleteModal"
    :mask-closable="false"
    preset="dialog"
    title="警告"
    content="确认删除该线索吗？"
    positive-text="确认"
    negative-text="算了"
    @positive-click="onDeleteClick"
  />
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCard, NGrid, NGi, NSpace, NAvatar, NText, NTime, NButton, useMessage, NModal, NImage, NImageGroup } from 'naive-ui'
import { deleteClue } from '../api/index'
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
    NButton,
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
      comment: '',
      userName: '',
      showDeleteModal: false
    })
    // 删除线索
    const onDeleteClick = async () => {
      const { data: res } = await deleteClue({ clueId: props.item.clueId })
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
    return { ...toRefs(state), onDeleteClick, getData }
  }
})
</script>
<style lang="scss">
p {
  margin: 0;
}
</style>
