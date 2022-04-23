<template>
  <div class="home">
    <n-carousel
      effect="card"
      prev-slide-style="transform: translateX(-150%) translateZ(-800px);"
      next-slide-style="transform: translateX(50%) translateZ(-800px);"
      style="height: 240px; margin: 20px 0"
      :show-dots="true"
      :autoplay="true"
      :loop="true"
    >
      <n-carousel-item :style="{ width: '60%' }">
        <img class="carousel-img" src="https://naive-ui.oss-cn-beijing.aliyuncs.com/carousel-img/carousel1.jpeg" />
      </n-carousel-item>
      <n-carousel-item :style="{ width: '60%' }">
        <img class="carousel-img" src="https://naive-ui.oss-cn-beijing.aliyuncs.com/carousel-img/carousel2.jpeg" />
      </n-carousel-item>
      <n-carousel-item :style="{ width: '60%' }">
        <img class="carousel-img" src="https://naive-ui.oss-cn-beijing.aliyuncs.com/carousel-img/carousel3.jpeg" />
      </n-carousel-item>
      <n-carousel-item :style="{ width: '60%' }">
        <img class="carousel-img" src="https://naive-ui.oss-cn-beijing.aliyuncs.com/carousel-img/carousel4.jpeg" />
      </n-carousel-item>
    </n-carousel>
    <div class="hot-list">
      <post-list title="领养" :list="adoptList"></post-list>
      <post-list title="寻宠" :list="clueList"></post-list>
    </div>
    <div class="bottom">
      <n-button type="info" secondary>我要反馈</n-button>
      <img src="https://naive-ui.oss-cn-beijing.aliyuncs.com/carousel-img/carousel4.jpeg" alt="" />
    </div>
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NCarousel, NCarouselItem, NButton } from 'naive-ui'
import postList from '../components/post-list.vue'
import { getArticleList } from '../api/index'

export default defineComponent({
  name: 'HomePage',
  components: {
    NCarousel,
    NCarouselItem,
    postList,
    NButton
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.GetArticleList({
        titleName: '',
        timeLimit: '4', // 不限
        city: '',
        isFinish: '3', // 不限
        reply: true
      })
    })
  },
  setup() {
    const state = reactive({
      adoptList: [],
      clueList: []
    })
    const GetArticleList = async (data) => {
      const { data: res1 } = await getArticleList({ titleType: '1', ...data })
      state.adoptList = res1.data.page.list
      const { data: res2 } = await getArticleList({ titleType: '2', ...data })
      state.clueList = res2.data.page.list
    }
    return { ...toRefs(state), GetArticleList }
  }
})
</script>
<style lang="scss" scoped>
.home {
  // height: 3000px;
  margin-bottom: 20px;
}
.hot-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  margin: 20px 0;
}
.bottom {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  img {
    margin-top: 10px;
    height: 200px;
    width: 800px;
  }
}
</style>
