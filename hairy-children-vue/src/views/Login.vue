<template>
  <div class="login">
    <n-form ref="formRef" :model="loginInfo" :rules="rules">
      <div class="head">
        <n-h2 prefix="bar">
          <n-text> 登录 </n-text>
        </n-h2>
        <n-space justify="end">
          <n-button text @click="switchLogin('username')">账号登录</n-button>
          <n-button text @click="switchLogin('email')">邮箱登录</n-button>
          <n-button text @click="switchLogin('phone')">手机登录</n-button>
        </n-space>
      </div>
      <n-form-item path="username" label="账号">
        <n-input v-model:value="loginInfo.userName" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="password" label="密码">
        <n-input v-model:value="loginInfo.password" type="password" @keydown.enter.prevent />
      </n-form-item>
      <n-row :gutter="[0, 24]">
        <n-col :span="24">
          <div style="display: flex; justify-content: flex-end">
            <n-button round type="primary" @click="loginHandle"> 登录 </n-button>
          </div>
        </n-col>
      </n-row>
    </n-form>
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NForm, NFormItem, NInput, NButton, NRow, NCol, NH2, NSpace, NText } from 'naive-ui'
import { login } from '../api'

export default defineComponent({
  name: 'LoginPage',
  components: {
    NForm,
    NFormItem,
    NInput,
    NButton,
    NRow,
    NCol,
    NH2,
    NSpace,
    NText
  },
  setup() {
    const state = reactive({
      loginInfo: {
        userName: '',
        password: ''
      }
    })
    const loginHandle = async () => {
      const { data: res } = await login(state.loginInfo)
      if (res.code === 200) {
        localStorage.setItem('jwt-token', res.data.token)
      }
    }
    return { ...toRefs(state), loginHandle }
  }
})
</script>
<style lang="scss" scoped>
.login {
  height: 100%;
  // background-color: rgb(89, 147, 165);
  display: flex;
  justify-content: center;
  align-items: center;
}
.n-form {
  width: 400px;
  padding: 30px 40px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0px 0px 100px 40px rgb(209, 222, 240);
  .head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    .n-h2 {
      margin: 0;
    }
  }
}
</style>
