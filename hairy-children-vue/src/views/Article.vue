<template>
  <n-h1 strong prefix="bar" style="margin: 20px 0">{{ title.titleName }}</n-h1>
  <n-card style="margin: 20px 0">
    <n-grid :cols="24">
      <n-gi :span="6" style="border-right: 1px solid #ddd">
        <n-space align="center" vertical>
          <n-avatar round :src="'http://175.24.229.41:9421/' + userInfo.userAvatar" :size="60" object-fit="cover"></n-avatar>
          <n-text>{{ userInfo.userName }}</n-text>
        </n-space>
      </n-gi>
      <n-gi :span="18">
        <n-space vertical>
          <v-md-preview :text="title.titleContent ? title.titleContent : 'null'"></v-md-preview>
          <n-space justify="flex-end">
            <n-button v-if="getData('userInfo').userId === userInfo.userId" text @click="EditArticle">编辑</n-button>
            <!-- <n-button text @click="collectHandle">收藏</n-button> -->
            <span class="user-address">{{ title.userAddress }}</span>
            <n-time :time="title.gmtCreate" type="relative" />
            <div class="center" @click="likeArticleHandle">
              <n-icon size="16">
                <Favorite />
              </n-icon>
              <span class="like"> {{ title.likeCount }}</span>
            </div>
            <div class="center">
              <n-icon size="16">
                <Chat />
              </n-icon>
              <span class="comment"> {{ title.commentCount }}</span>
            </div>
            <div class="center" @click="showCollectionModal = true">
              <n-icon size="16">
                <Star />
              </n-icon>
              <span class="collect"> {{ title.collectionCount }}</span>
            </div>
            <span v-if="title.isFinish === 1" class="status" style="color: green">{{ title.titleType === 1 ? '未领养' : '未寻回' }}</span>
            <span v-else class="status" style="color: red">{{ title.titleType === 1 ? '已领养' : '已寻回' }}</span>
          </n-space>
        </n-space>
      </n-gi>
    </n-grid>
  </n-card>
  <n-h2>{{ title.titleType === 1 ? '领养' : '线索' }}</n-h2>
  <n-button @click="title.titleType === 1 ? (showAdoptModal = true) : (showClueModal = true)">新建</n-button>
  <div v-if="title.titleType === 1">
    <adopt-item v-for="item in list" :key="item.adoptId" :item="item"></adopt-item>
  </div>
  <div v-else>
    <clue-item v-for="item in list" :key="item.clueId" :item="item"></clue-item>
  </div>
  <n-h2>评论</n-h2>
  <n-space vertical justify="center">
    <n-input
      v-model:value="comment"
      type="textarea"
      placeholder="请输入你的评论"
      :autosize="{
        minRows: 3,
        maxRows: 3
      }"
    />
    <n-button @click="AddRootComment">发布</n-button>
  </n-space>
  <comment-item v-for="item in comments?.list" :key="item.rootId" :item="item"> </comment-item>
  <!-- 领养 -->
  <n-modal
    v-model:show="showAdoptModal"
    :mask-closable="false"
    preset="dialog"
    title="确认"
    content="你确认"
    positive-text="确认"
    negative-text="算了"
    @positive-click="onPositiveClick"
  >
    <n-form ref="formRef" :model="adoptInfo">
      <n-form-item path="userName" label="申请理由">
        <n-input v-model:value="adoptInfo.adoptReason"></n-input>
      </n-form-item>
      <n-form-item path="userName" label="城市">
        <n-input v-model:value="adoptInfo.adoptUserAddress" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="userName" label="年龄">
        <n-input v-model:value="adoptInfo.adoptUserAge" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="userName" label="联系方式">
        <n-input v-model:value="adoptInfo.adoptUserPhone" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="userName" label="养宠理念">
        <n-input v-model:value="adoptInfo.adoptConcept" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="userName" label="接动物方式">
        <n-input v-model:value="adoptInfo.adoptWay" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="userName" label="是否接受定期回访">
        <n-switch checked-value="1" unchecked-value="0" @update:value="adoptInfo.isReturnVisit">
          <template #checked>同意</template>
          <template #unchecked>不同意</template>
        </n-switch>
      </n-form-item>
      <n-form-item path="userName" label="是否能够定期反馈领养情况">
        <n-switch checked-value="1" unchecked-value="0" @update:value="adoptInfo.isFeedback">
          <template #checked>同意</template>
          <template #unchecked>不同意</template>
        </n-switch>
      </n-form-item>
    </n-form>
  </n-modal>
  <n-modal v-model:show="showImgUploadModal" preset="card" style="width: 600px">
    <n-upload :custom-request="customRequest" :default-file-list="fileList" list-type="image-card"> 点击上传 </n-upload>
  </n-modal>
  <!-- 线索 -->
  <n-modal
    v-model:show="showClueModal"
    :mask-closable="false"
    preset="dialog"
    title="新增线索"
    size="huge"
    :bordered="false"
    style="width: 600px"
    positive-text="确认"
    negative-text="算了"
    @positive-click="onCreateClueClick"
  >
    <n-input
      v-model:value="clueInfo.clueContent"
      type="textarea"
      placeholder="请输入你的线索"
      :autosize="{
        minRows: 3,
        maxRows: 3
      }"
    />
  </n-modal>
  <!-- 收藏夹 -->
  <n-modal
    v-model:show="showCollectionModal"
    class="custom-card"
    preset="card"
    title="收藏夹"
    size="huge"
    style="width: 600px"
    :bordered="false"
  >
    <div v-for="item in favorites" :key="item.collectionId">
      <n-space justify="space-between" align="center">
        <n-space vertical style="padding: 10px">
          <n-text style="font-size: 18px; font-weight: 700">{{ item.collectionName }}</n-text>
          <n-text>{{ item.collectionCount }} 条内容</n-text>
        </n-space>
        <n-button @click="collectArticle(item.collectionId)">收藏</n-button>
      </n-space>
    </div>
    <template #footer>
      <n-button v-if="!isAddCollection" @click="isAddCollection = true">新建收藏夹</n-button>
      <n-space v-else justify="space-between">
        <n-input v-model:value="collectionName"></n-input>
        <n-button @click="isAddCollection = false">取消</n-button>
        <n-button type="primary" @click="onCreateCollectionClick">确认</n-button>
      </n-space>
    </template>
  </n-modal>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import {
  NCard,
  NGrid,
  NGi,
  NSpace,
  NAvatar,
  NText,
  NTime,
  NIcon,
  NH1,
  NInput,
  NButton,
  useMessage,
  NH2,
  NForm,
  NFormItem,
  NModal,
  NSwitch,
  NUpload
} from 'naive-ui'
import { Favorite, Star, Chat } from '@vicons/carbon'
import commentItem from '../components/comment-item.vue'
import {
  getArticle,
  getUserInfo,
  addRootComment,
  getArticleComments,
  getArticleClues,
  getArticleAdoptions,
  createAdoption,
  updateAdoption,
  uploadAdoptionImg,
  createClue,
  updateClue,
  uploadClueImg,
  addCollection,
  getUserFavorites,
  addFavorite,
  likeArticle
} from '../api/index'
import adoptItem from '../components/adopt-item.vue'
import clueItem from '../components/clue-item.vue'
import { getData } from '../utils/tools'

export default defineComponent({
  name: 'ArticlePage',
  components: {
    NCard,
    NGrid,
    NGi,
    NSpace,
    NAvatar,
    NText,
    Favorite,
    Star,
    Chat,
    NTime,
    NIcon,
    NH1,
    NInput,
    NButton,
    commentItem,
    NH2,
    NModal,
    NForm,
    NFormItem,
    NSwitch,
    adoptItem,
    NUpload,
    clueItem
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.GetArticle(to.params.id)
    })
  },
  setup() {
    const router = useRouter()
    const message = useMessage()
    const state = reactive({
      title: {},
      userInfo: {},
      comment: '',
      comments: [],
      list: [],
      showAdoptModal: false,
      adoptInfo: {},
      showImgUploadModal: false,
      fileList: [],
      showClueModal: false,
      clueInfo: {},
      showCollectionModal: false,
      favorites: [],
      isAddCollection: false,
      collectionName: ''
    })
    const GetArticle = async (data) => {
      const { data: res } = await getArticle({ titleId: data })
      state.title = res.data?.title
      const { data: ress } = await getUserInfo({ userId: state.title.userId })
      state.userInfo = ress.data
      const { data: resss } = await getArticleComments({ titleId: state.title.titleId })
      // ?.可选参数，防止无评论时报错
      state.comments = resss.data?.page
      if (state.title.titleType === 1) {
        const { data: ressss } = await getArticleAdoptions({ titleId: state.title.titleId })
        state.list = ressss.data.page?.list
      } else {
        const { data: ressss } = await getArticleClues({ titleId: state.title.titleId })
        state.list = ressss.data.page?.list
      }
      const { data: resssss } = await getUserFavorites({ userId: state.userInfo.userId })
      state.favorites = resssss.data.page?.list
    }
    const GetUserInfo = async () => {
      const { data: res } = await getUserInfo()
      state.userInfo = res.data
    }
    const AddRootComment = async () => {
      const { data: res } = await addRootComment({
        answerId: state.title.titleId,
        content: state.comment
      })
      if (res.code === 200) {
        message.success('评论成功')
        router.go(0)
      } else {
        message.error(res.msg)
      }
    }
    const EditArticle = () => {
      router.push({
        name: 'write',
        params: {
          id: state.title.titleId
        }
      })
    }
    const customRequest = async ({ file }) => {
      const formData = new FormData()
      formData.append('imgs', file.file)
      if (state.title.titleType === 1) {
        const { data: res } = await uploadAdoptionImg(state.adoptInfo.adoptId, formData)
        if (res.code === 200) {
          message.success('上传成功')
          // state.adoptInfo.imgs.push(res.data)
        } else {
          message.error(res.msg)
        }
      } else {
        const { data: res } = await uploadClueImg(state.clueInfo.clueId, formData)
        if (res.code === 200) {
          message.success('上传成功')
          // state.clueInfo.imgs.push(res.data)
        } else {
          message.error(res.msg)
        }
      }
    }
    const onPositiveClick = () => {
      if (state.title.titleType === 1) {
        state.showAdoptModal = false
        createAdoption({
          userId: state.userInfo.userId,
          titleId: state.title.titleId
        }).then((res) => {
          state.adoptInfo.adoptId = res.data.data.adoptId
          updateAdoption({
            userId: state.userInfo.userId,
            adoptId: state.adoptInfo.adoptId,
            isSuccess: '0',
            ...state.adoptInfo
          }).then((ress) => {
            if (ress.data.code === 200) {
              message.success('申请成功')
              // router.go(0)
              state.showAdoptModal = false
              state.showImgUploadModal = true
            } else {
              message.error(res.data.msg)
            }
          })
        })
      }
    }
    const onCreateClueClick = () => {
      state.showClueModal = false
      createClue({
        userId: getData('userInfo').userId,
        titleId: state.title.titleId
      }).then((res) => {
        state.clueInfo.clueId = res.data.data.clueId
        updateClue({
          clueId: state.clueInfo.clueId,
          isSuccess: '0',
          ...state.clueInfo
        }).then((ress) => {
          if (ress.data.code === 200) {
            message.success('申请成功')
            // router.go(0)
            state.showClueModal = false
            state.showImgUploadModal = true
          } else {
            message.error(res.data.msg)
          }
        })
      })
    }
    const collectArticle = async (collectionId) => {
      const { data: res } = await addCollection({
        collectionId,
        titleId: state.title.titleId
      })
      if (res.code === 200) {
        message.success('收藏成功')
        state.showCollectionModal = false
      } else {
        message.error(res.msg)
      }
    }
    const onCreateCollectionClick = async () => {
      const { data: res } = await addFavorite({
        collectionName: state.collectionName
      })
      if (res.code === 200) {
        message.success('新建成功')
        state.isAddCollection = false
      } else {
        message.error(res.msg)
      }
    }
    const likeArticleHandle = async () => {
      const { data: res } = await likeArticle({ titleId: state.title.titleId, userId: getData('userInfo').userId })
      if (res.code === 200) {
        message.success('点赞成功')
        router.go(0)
      } else {
        message.error(res.msg)
      }
    }
    return {
      ...toRefs(state),
      GetArticle,
      GetUserInfo,
      AddRootComment,
      getData,
      EditArticle,
      onPositiveClick,
      customRequest,
      onCreateClueClick,
      collectArticle,
      onCreateCollectionClick,
      likeArticleHandle
    }
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
