<template>
  <n-card>
    <n-result v-if="userInfo.userIdentityCard" status="success" title="完成" description="已完成实名认证"> </n-result>
    <!-- 认证表单 -->
    <n-space v-else vertical>
      <n-form :model="identifyInfo" :label-width="80">
        <n-form-item label="真实姓名">
          <n-input v-model:value="identifyInfo.realName" style="witdh: 300px"></n-input>
        </n-form-item>
        <n-form-item label="身份证号">
          <n-input v-model:value="identifyInfo.identityCard"></n-input>
        </n-form-item>
      </n-form>
      <n-space justify="center">
        <n-button @click="identifyHandle">认证</n-button>
      </n-space>
    </n-space>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NInput, NSpace, NButton, NForm, NFormItem, NCard, useMessage, NResult } from 'naive-ui'
import { useRouter } from 'vue-router'
import { getUserInfo, authentication } from '../../api'

export default defineComponent({
  name: 'SpacePage',
  components: {
    NInput,
    NSpace,
    NButton,
    NForm,
    NFormItem,
    NCard,
    NResult
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      getUserInfo({ userId: to.params.userId }).then((res) => {
        vm.setState('userInfo', res.data.data)
      })
    })
  },
  setup() {
    const router = useRouter()
    const message = useMessage()
    const state = reactive({
      identifyInfo: {
        realName: '',
        identityCard: ''
      },
      userInfo: {}
    })
    const setState = (name, data) => {
      state[name] = data
    }
    const identifyHandle = () => {
      authentication({
        realName: state.identifyInfo.realName,
        identityCard: state.identifyInfo.identityCard
      })
        .then(() => {
          message.success('认证成功')
          router.push(`/space/${router.params.userId}`)
        })
        .catch(() => {
          message.error('认证失败')
        })
    }
    return { ...toRefs(state), setState, identifyHandle }
  }
})
</script>
<style lang="scss" scoped></style>
