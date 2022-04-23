<template>
  <div class="list">
    <n-breadcrumb>
      <n-breadcrumb-item href="/home"> 首页 </n-breadcrumb-item>
      <n-breadcrumb-item href="#"> {{ typeName }} </n-breadcrumb-item>
    </n-breadcrumb>
    <div class="search">
      <n-space justify="space-between">
        <n-input v-model:value="requestInfo.titleName" type="text" style="width: 1000px"></n-input>
        <n-button type="primary" secondary @click="searchHandle"> 搜索 </n-button>
      </n-space>
    </div>
    <div class="filter">
      <n-list bordered>
        <n-list-item>
          <n-grid>
            <n-gi span="3" style="display: flex; align-items: center; justify-content: center">
              <span>选择城市</span>
            </n-gi>
            <n-gi span="3">
              <n-select v-model:value="requestInfo.city" :options="cities"> </n-select>
            </n-gi>
          </n-grid>
        </n-list-item>
        <n-list-item>
          <n-grid>
            <n-gi span="3" style="display: flex; align-items: center; justify-content: center">
              <span>{{ typeName }}状态</span>
            </n-gi>
            <n-gi span="12">
              <n-radio-group v-model:value="requestInfo.isFinish" name="statusgroup">
                <n-space>
                  <n-radio v-for="item in status" :key="item.value" :value="item.value">
                    {{ item.label }}
                  </n-radio>
                </n-space>
              </n-radio-group>
            </n-gi>
          </n-grid>
        </n-list-item>
        <n-list-item>
          <n-grid>
            <n-gi span="3" style="display: flex; align-items: center; justify-content: center">
              <span>发布时间</span>
            </n-gi>
            <n-gi span="12">
              <n-radio-group v-model:value="requestInfo.timeLimit" name="timegroup">
                <n-space>
                  <n-radio v-for="item in times" :key="item.value" :value="item.value">
                    {{ item.label }}
                  </n-radio>
                </n-space>
              </n-radio-group>
            </n-gi>
          </n-grid>
        </n-list-item>
        <n-list-item>
          <n-grid>
            <n-gi span="3" style="display: flex; align-items: center; justify-content: center">
              <span>排序</span>
            </n-gi>
            <n-gi span="3">
              <n-select :options="sortOptions" @update-value="sortHandle"> </n-select>
            </n-gi>
          </n-grid>
        </n-list-item>
      </n-list>
    </div>
    <div class="post-list">
      <postListItem :list="list" class="post-list-item"></postListItem>
    </div>
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NBreadcrumb, NBreadcrumbItem, NList, NListItem, NSelect, NGrid, NGi, NRadioGroup, NSpace, NRadio, NInput, NButton } from 'naive-ui'
import postListItem from '../components/post-list-item.vue'
import { getArticleList } from '../api/index'

export default defineComponent({
  name: 'ListPage',
  components: {
    NBreadcrumb,
    NBreadcrumbItem,
    NList,
    NListItem,
    NSelect,
    NGrid,
    NGi,
    NRadioGroup,
    NSpace,
    NRadio,
    NInput,
    NButton,
    postListItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.typeHandle(to.query.titleType)
      vm.searchHandle()
    })
  },
  setup() {
    const state = reactive({
      typeName: '领养',
      cities: [
        { label: '全部', value: '' },
        { label: '北京', value: '北京' },
        { label: '上海', value: '上海' },
        { label: '成都', value: '成都' }
      ],
      status: [
        {
          label: `待完成`,
          value: 1
        },
        {
          label: `已完成`,
          value: 2
        },
        {
          label: '不限',
          value: 3
        }
      ],
      times: [
        {
          label: '今日',
          value: 1
        },
        {
          label: '本周',
          value: 2
        },
        {
          label: '半年',
          value: 3
        },
        {
          label: '不限',
          value: 4
        }
      ],
      sortOptions: [
        {
          label: '按收藏数排序',
          value: 'collection'
        },
        {
          label: '按点赞排序',
          value: 'like'
        },
        {
          label: '按回复排序',
          value: 'reply'
        },
        {
          label: '按时间排序',
          value: 'time'
        }
      ],
      requestInfo: {
        titleName: '',
        titleType: 1,
        city: '',
        isFinish: 3,
        timeLimit: 4
      },
      list: []
    })
    const sortHandle = (value) => {
      state.requestInfo[value] = true
    }
    const searchHandle = async () => {
      const { data: res } = await getArticleList(state.requestInfo)
      state.list = res.data.page.list
    }
    const typeHandle = (value) => {
      console.log(value)
      state.requestInfo.titleType = value
      state.typeName = value === '1' ? '领养' : '寻宠'
    }
    return { ...toRefs(state), sortHandle, typeHandle, searchHandle }
  }
})
</script>
<style lang="scss" scoped>
.search {
  margin: 20px 0;
}
.list {
  margin: 10px 0;
}
.post-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 1000px;
}
.post-list-item {
  width: 100%;
}
</style>
