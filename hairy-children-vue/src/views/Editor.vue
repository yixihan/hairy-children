<template>
  <div>
    <v-md-editor v-model="postInfo.titleContent" :disabled-menus="[]" height="400px" @upload-image="handleUploadImage"></v-md-editor>
    <n-space justify="center" style="margin: 20px 0">
      <n-button type="primary" @click="createHandle">发布</n-button>
    </n-space>
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import { NSpace, NButton, useMessage } from 'naive-ui'
import { updateArticle, getArticle, uploadImg } from '../api/index'
import { getData } from '../utils/tools'

export default defineComponent({
  name: 'EditorPage',
  components: {
    NSpace,
    NButton
  },
  beforeRouteEnter(to, from, next) {
    const { id } = to.params
    if (id) {
      getArticle({ titleId: id }).then((res) => {
        const { title } = res.data.data
        next((vm) => {
          vm.setState(
            'postInfo',
            reactive({
              titleId: `${title.titleId}`,
              titleContent: title.titleContent ? title.titleContent : '',
              isFinish: title.isFinish <= '0' ? '0' : '1'
            })
          )
          vm.setState('titleId', id)
        })
      })
    }
  },
  setup() {
    const router = useRouter()
    const message = useMessage()
    const state = reactive({
      text: '',
      postInfo: {
        userId: `${getData('userInfo').userId}`,
        titleContent: 'Test Text',
        isFinish: '0'
      }
    })
    const setState = (key, data) => {
      state[key] = data
    }
    const handleUploadImage = async (event, insertImage, files) => {
      const formData = new FormData()
      formData.append('img', files[0])
      const { data: res } = await uploadImg(state.titleId, formData)
      if (res.code === 200) {
        insertImage({ url: `http://175.24.229.41:9421/${res.data.imgDir}`, desc: '图片描述' })
      }
    }
    const createHandle = async () => {
      const { data: res } = await updateArticle(state.postInfo)
      if (res.code === 200) {
        message.success('发布成功')
        router.push({
          path: `/article/${state.postInfo.titleId}`
        })
      } else {
        message.error(res.msg)
      }
    }
    return { ...toRefs(state), createHandle, setState, handleUploadImage }
  }
})
</script>
<style lang=""></style>
