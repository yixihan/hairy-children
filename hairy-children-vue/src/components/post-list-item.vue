<template>
  <div class="list">
    <div v-for="item in list" :key="item.titleId" class="list-item" @click="gotoPage('/article/' + item.titleId)">
      <span class="item-title">{{ item.titleName }}</span>
      <span class="item-content">{{ abstractFn(item.titleContent) + ' ……' }}</span>
      <n-space>
        <span class="user-address">{{ item.userAddress }}</span>
        <n-time :time="item.gmtCreate" type="relative" />
        <div class="center">
          <n-icon size="16">
            <Favorite />
          </n-icon>
          <span class="like"> {{ item.likeCount }}</span>
        </div>
        <div class="center">
          <n-icon size="16">
            <Chat />
          </n-icon>
          <span class="comment"> {{ item.commentCount }}</span>
        </div>
        <div class="center">
          <n-icon size="16">
            <Star />
          </n-icon>
          <span class="collect"> {{ item.collectionCount }}</span>
        </div>
        <span v-if="item.isFinish === 1" class="status" style="color: green">{{ item.titleType === 1 ? '未领养' : '未寻回' }}</span>
        <span v-else class="status" style="color: red">{{ item.titleType === 1 ? '已领养' : '已寻回' }}</span>
      </n-space>
    </div>
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NTime, NSpace, NIcon } from 'naive-ui'
import { Favorite, Star, Chat } from '@vicons/carbon'
import { useRouter } from 'vue-router'
import { abstractFn } from '../utils/tools'

export default defineComponent({
  components: {
    NTime,
    NSpace,
    NIcon,
    Favorite,
    Star,
    Chat
  },
  props: {
    list: {
      type: Array,
      default: () => [
        {
          titleId: '1',
          title: '暂无'
        }
      ]
    }
  },
  setup() {
    const router = useRouter()
    const state = reactive({})
    const gotoPage = (path) => {
      router.push(path)
    }
    return { ...toRefs(state), abstractFn, gotoPage }
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
