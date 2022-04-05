<template>
  <div class="box">
    <span class="title">{{ title }}</span>
    <div class="list">
      <div v-for="item in list" :key="item.titleId" class="list-item">
        <span class="item-title">{{ item.titleName }}</span>
        <!-- <span class="item-content">{{ item.titleContent }}</span> -->
        <span class="item-content">this is a test content</span>
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
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NTime, NSpace, NIcon } from 'naive-ui'
import { Favorite, Star, Chat } from '@vicons/carbon'

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
    title: {
      type: String,
      default: '标题'
    },
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
    const state = reactive({})
    return { ...toRefs(state) }
  }
})
</script>
<style lang="scss" scoped>
.box {
  width: 400px;
  border: 3px solid #000;
  // background-color: rgb(235, 235, 235);
}
.list {
  display: flex;
  flex-direction: column;
}
.title {
  display: block;
  width: 120px;
  margin: 10px auto;
  font-size: 34px;
  font-weight: bold;
  text-align: center;
  border: 3px solid #000;
}
.center {
  display: flex;
  align-items: center;
}
.status {
  font-weight: 700;
}
.list-item {
  // padding: 20px;
  // width: 100%;
  margin: 10px;
  padding: 10px;
  border: 2px solid #000;
  border-radius: 5px;
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
.list-item:hover {
  // box-shadow: 0px 0px 2px 2px rgb(255, 255, 255);
}
</style>
