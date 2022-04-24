<template>
  <!-- <div class="homepage"> -->
  <n-layout>
    <n-layout-header bordered>
      <div class="header">
        <img src="https://lf3-cdn-tos.bytescm.com/obj/static/xitu_juejin_web/e08da34488b114bd4c665ba2fa520a31.svg" @click="gotoPage('/')" />
        <n-space v-if="isLogin" align="center">
          <n-button type="primary" @click="showModal = true">我要发布</n-button>
          <n-button type="primary" @click="gotoPage(`/${getData('userInfo').userId}/space`)">个人中心</n-button>
          <n-text>{{ userInfo.userName }}，欢迎回来！</n-text>
          <n-button type="info" secondary @click="logout()"> 注销 </n-button>
        </n-space>
        <n-space v-else align="center">
          <n-button type="primary" @click="gotoPage('/login')">登录</n-button>
          <n-button type="primary" @click="gotoPage('/register')">注册</n-button>
        </n-space>
      </div>
    </n-layout-header>
    <n-layout-content>
      <router-view></router-view>
    </n-layout-content>
  </n-layout>
  <n-modal
    v-model:show="showModal"
    :mask-closable="false"
    preset="dialog"
    title="确认"
    content="你确认"
    positive-text="确认"
    negative-text="算了"
    @positive-click="onPositiveClick"
  >
    <n-form ref="formRef" :model="postInfo">
      <n-form-item path="userName" label="类别">
        <n-select v-model:value="postInfo.titleType" :options="typeOptions"></n-select>
      </n-form-item>
      <n-form-item path="userName" label="城市">
        <n-input v-model:value="postInfo.userAddress" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="userName" label="标题">
        <n-input v-model:value="postInfo.titleName" @keydown.enter.prevent />
      </n-form-item>
    </n-form>
  </n-modal>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NInput, NLayout, NLayoutHeader, NLayoutContent, NSpace, NButton, NText, NModal, NForm, NFormItem, NSelect } from 'naive-ui'
import { useRouter, RouterView } from 'vue-router'
import { getUserInfo, createArticle } from '../api'
import { getData, saveData } from '../utils/tools'

export default defineComponent({
  name: 'IndexPage',
  components: {
    NInput,
    NLayout,
    NLayoutHeader,
    NLayoutContent,
    NSpace,
    NButton,
    NText,
    RouterView,
    NModal,
    NForm,
    NFormItem,
    NSelect
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (localStorage.getItem('token')) {
        vm.setState('isLogin', true)
        vm.GetUserInfo({ userId: getData('token').userId })
      }
    })
  },
  setup() {
    const router = useRouter()
    const state = reactive({
      isLogin: false,
      userInfo: {},
      showModal: false,
      typeOptions: [
        { value: '1', label: '领养' },
        { value: '2', label: '寻宠' }
      ],
      postInfo: {
        userAddress: '成都',
        titleType: '1',
        titleName: 'Test'
      }
    })
    const setState = (name, data) => {
      state[name] = data
    }
    const GetUserInfo = async (data) => {
      const { data: res } = await getUserInfo(data)
      if (res.code === 200) {
        saveData('userInfo', res.data)
        setState('userInfo', res.data)
      }
    }
    const logout = () => {
      localStorage.removeItem('token')
      setState('isLogin', false)
      router.push('/login')
    }
    // 跳转到指定路由
    const gotoPage = (path) => {
      router.push(path)
    }
    const onPositiveClick = async () => {
      const { data: res } = await createArticle({
        userId: getData('token').userId,
        titleType: state.postInfo.titleType,
        userAddress: state.postInfo.userAddress,
        titleName: state.postInfo.titleName
      })
      router.push({
        name: 'write',
        params: {
          id: res.data.titleId
        }
      })
    }
    return { ...toRefs(state), setState, GetUserInfo, gotoPage, logout, onPositiveClick, getData }
  }
})
</script>
<style lang="scss" scoped>
.n-layout {
  // height: 100%;
  // margin-bottom: 20px;
  background-color: rgb(244, 245, 245);
  .n-layout-scroll-container {
    overflow-x: visible;
  }
}
.n-space {
  height: 100%;
}
img {
  height: 100%;
}
.n-layout-header {
  height: 60px;
  .header {
    height: 100%;
    max-width: 1600px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    img {
      height: 30px;
    }
  }
}
.n-layout-content {
  min-height: calc(100vh - 60px);
  width: 1200px;
  padding: 0 30px;
  margin: 0 auto;
  background-color: #fff;
}
.carousel-img {
  margin: 0 auto;
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
