<template>
  <!-- <div class="homepage"> -->
  <n-layout>
    <n-layout-header bordered>
      <div class="header">
        <img src="https://lf3-cdn-tos.bytescm.com/obj/static/xitu_juejin_web/e08da34488b114bd4c665ba2fa520a31.svg" @click="gotoPage('/')" />
        <n-space v-if="isLogin" align="center">
          <n-text>Hello, World</n-text>
          <n-button type="info"> 注销 </n-button>
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
import { NLayout, NLayoutHeader, NLayoutContent, NSpace, NButton } from 'naive-ui'
import { useRouter, RouterView } from 'vue-router'

export default defineComponent({
  name: 'IndexPage',
  components: {
    NLayout,
    NLayoutHeader,
    NLayoutContent,
    NSpace,
    NButton,
    RouterView
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (localStorage.getItem('jwt-token')) {
        vm.isLogin = true
      }
    })
  },
  setup() {
    const router = useRouter()
    const state = reactive({
      isLogin: false
    })
    // 跳转到指定路由
    const gotoPage = (path) => {
      router.push(path)
    }
    return { ...toRefs(state), gotoPage }
  }
})
</script>
<style lang="scss">
.n-layout {
  height: 100%;
  background-color: rgb(244, 245, 245);
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
