<template>
  <!-- <div class="homepage"> -->
  <n-layout>
    <n-layout-header bordered>
      <div class="header">
        <img src="https://lf3-cdn-tos.bytescm.com/obj/static/xitu_juejin_web/e08da34488b114bd4c665ba2fa520a31.svg" @click="gotoPage('/')" />
        <n-space v-if="isLogin" align="center">
          <n-button type="primary">我要发布</n-button>
          <n-button type="primary">个人中心</n-button>
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
    <!-- <n-layout-footer>成府路</n-layout-footer> -->
  </n-layout>
  <!-- </div> -->
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NLayout, NLayoutHeader, NLayoutContent, NSpace, NButton, NText } from 'naive-ui'
import { useRouter, RouterView } from 'vue-router'
import { getUserInfo } from '../api'
import { getData, saveData } from '../utils/tools'

export default defineComponent({
  name: 'IndexPage',
  components: {
    NLayout,
    NLayoutHeader,
    NLayoutContent,
    NSpace,
    NButton,
    NText,
    RouterView
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
      userInfo: {}
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
    return { ...toRefs(state), setState, GetUserInfo, gotoPage, logout }
  }
})
</script>
<style lang="scss">
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
