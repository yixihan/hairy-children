<template>
  <n-card>
    <n-space vertical justify="center" align="center">
      <n-avatar round :size="120" :src="`http://175.24.229.41:9421/${userInfo.userAvatar}?temp=${Math.random()}`"></n-avatar>
      <n-upload :custom-request="uploadAvatarRequest">上传头像</n-upload>
    </n-space>
    <n-form v-if="isShow" label-align="left" label-placement="left" label-width="120">
      <n-form-item label="用户名">{{ userInfo.userName }}</n-form-item>
      <n-form-item label="用户ID">{{ userInfo.userId }}</n-form-item>
      <!-- <n-form-item label="所在城市">{{ userInfo.userAddress }}</n-form-item> -->
      <n-form-item label="真实姓名">{{ userInfo.userRealName || '尚未实名认证' }}</n-form-item>
      <n-form-item label="身份证号">{{ userInfo.userIdentityCard || '尚未实名认证' }}</n-form-item>
      <n-form-item label="性别">{{ userInfo.userGender }}</n-form-item>
      <n-form-item label="生日">
        <n-time :time="userInfo.userBirth" format="yyyy-MM-dd" />
      </n-form-item>
      <n-form-item label="手机号">{{ userInfo.userPhone || '尚未绑定手机号' }}</n-form-item>
      <n-form-item label="家庭住址">{{ userInfo.userAddress }}</n-form-item>
      <n-form-item label="邮箱">{{ userInfo.userEmail }}</n-form-item>
      <n-form-item label="养宠情况">{{ userInfo.userPetCond }}</n-form-item>
      <n-form-item label="个人宣言">{{ userInfo.userAutograph }}</n-form-item>
    </n-form>
    <n-form v-else label-align="left" label-placement="left" label-width="120">
      <n-form-item label="用户名">
        <!-- <n-input v-model:value="userInfo.userName"></n-input> -->
        {{ userInfo.userName }}
      </n-form-item>
      <n-form-item label="用户ID">
        {{ userInfo.userId }}
      </n-form-item>
      <!-- <n-form-item label="所在城市">
        <n-input v-model:value="userInfo.userAddress"></n-input>
      </n-form-item> -->
      <n-form-item label="真实姓名">{{ userInfo.userRealName || '尚未实名认证' }}</n-form-item>
      <n-form-item label="身份证号">{{ userInfo.userIdentityCard || '尚未实名认证' }}</n-form-item>
      <n-form-item label="性别">
        <n-radio-group v-model:value="userInfo.userGender">
          <n-space>
            <n-radio value="男" label="男">男</n-radio>
            <n-radio value="女" label="女">女</n-radio>
          </n-space>
        </n-radio-group>
        <n-switch v-model:value="userInfo.genderShow" :checked-value="1" :unchecked-value="0" style="margin-left: 20px">
          <template #checked>公开</template>
          <template #unchecked>隐藏</template>
        </n-switch>
      </n-form-item>
      <n-form-item label="生日">
        <n-date-picker v-model:value="userInfo.userBirth" type="date" clearable />
        <!-- 是否展示生日 -->
        <n-switch v-model:value="userInfo.birthShow" :checked-value="1" :unchecked-value="0" style="margin-left: 20px">
          <template #checked>公开</template>
          <template #unchecked>隐藏</template>
        </n-switch>
      </n-form-item>
      <n-form-item label="手机号">
        <!-- <n-input v-model:value="userInfo.userPhone"></n-input> -->
        {{ userInfo.userPhone || '尚未绑定手机号' }}
      </n-form-item>
      <n-form-item label="家庭住址">
        <n-input v-model:value="userInfo.userAddress"></n-input>
        <!-- 是否展示地址 -->
        <n-switch v-model:value="userInfo.addressShow" :checked-value="1" :unchecked-value="0" style="margin-left: 20px">
          <template #checked>公开</template>
          <template #unchecked>隐藏</template>
        </n-switch>
      </n-form-item>
      <n-form-item label="邮箱">
        <n-input v-model:value="userInfo.userEmail"></n-input>
      </n-form-item>
      <n-form-item label="养宠情况">
        <n-input v-model:value="userInfo.userPetCond"></n-input>
      </n-form-item>
      <n-form-item label="个人宣言">
        <n-input
          v-model:value="userInfo.userAutograph"
          type="textarea"
          :autosize="{
            minRows: 3,
            maxRows: 3
          }"
        ></n-input>
      </n-form-item>
    </n-form>
    <n-space justify="center">
      <!-- <n-button>实名认证</n-button> -->
      <n-button v-if="isShow" @click="isShow = false">编辑信息</n-button>
      <n-button v-else @click="updateUserInfoHandle">提交信息</n-button>
    </n-space>
  </n-card>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import {
  NAvatar,
  NUpload,
  NInput,
  NSpace,
  NButton,
  NForm,
  NFormItem,
  NCard,
  NRadioGroup,
  NRadio,
  NSwitch,
  useMessage,
  NDatePicker,
  NTime
} from 'naive-ui'
import { useRouter } from 'vue-router'
import { getUserInfo, uploadAvatar, updateUserInfo } from '../../api'

export default defineComponent({
  name: 'SpacePage',
  components: {
    NAvatar,
    NUpload,
    NInput,
    NSpace,
    NButton,
    NForm,
    NFormItem,
    NCard,
    NRadioGroup,
    NRadio,
    NSwitch,
    NDatePicker,
    NTime
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
      userInfo: {},
      isShow: true
    })
    const setState = (name, data) => {
      state[name] = data
    }
    const uploadAvatarRequest = ({ file }) => {
      const formData = new FormData()
      formData.append('avatar', file.file)
      uploadAvatar(formData).then((res) => {
        if (res.data.code === 200) {
          message.success('上传成功')
        } else {
          message.error('上传失败，请重试')
        }
      })
    }
    const updateUserInfoHandle = () => {
      updateUserInfo(state.userInfo).then((res) => {
        if (res.data.code === 200) {
          message.success('修改成功')
          router.go(0)
        } else {
          message.error('修改失败，请重试')
        }
      })
    }

    return { ...toRefs(state), setState, uploadAvatarRequest, updateUserInfoHandle }
  }
})
</script>
<style lang="scss" scoped></style>
