<template>
  <div class="login">
    <n-form ref="formRef" :model="loginInfo" :rules="rules">
      <div class="head">
        <n-h2 prefix="bar">
          <n-text> 登录 </n-text>
        </n-h2>
        <n-space justify="end">
          <n-button text :text-color="loginType === 'username' ? '#6cf' : '#999'" @click="switchLogin('username')">账号登录</n-button>
          <n-button text :text-color="loginType === 'email' ? '#6cf' : '#999'" @click="switchLogin('email')">邮箱登录</n-button>
          <n-button text :text-color="loginType === 'phone' ? '#6cf' : '#999'" @click="switchLogin('phone')">手机登录</n-button>
        </n-space>
      </div>
      <n-form-item path="userName" label="账号">
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
import { NForm, NFormItem, NInput, NButton, NRow, NCol, NH2, NSpace, NText, useMessage } from 'naive-ui'
import { useRouter } from 'vue-router'
import { login, loginByEmail, loginByPhone } from '../api'
import { saveData } from '../utils/tools'

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
    const router = useRouter()
    const message = useMessage()
    const state = reactive({
      loginInfo: {
        userName: '',
        password: ''
      },
      loginType: 'username'
    })
    const switchLogin = (type) => {
      state.loginType = type
    }
    const rules = {
      userName: [
        {
          required: true,
          validator(rule, value) {
            switch (state.loginType) {
              case 'username':
                console.log(value)
                if (!value) return new Error('请输入账号')
                if (value.length < 4 || value.length > 12) return new Error('账号长度为4-12位')
                break
              case 'email':
                if (!value) return new Error('请输入邮箱')
                if (!/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(value)) return new Error('邮箱格式不正确')
                break
              case 'phone':
                if (!value) return new Error('请输入手机号')
                if (!/^1[3456789]\d{9}$/.test(value)) return new Error('手机号码格式不正确')
                break
              default:
                break
            }
            return true
          },
          trigger: ['input', 'blur']
        }
      ],
      password: [
        {
          required: true,
          message: '请输入密码'
        }
      ]
    }
    const loginHandle = async () => {
      let res = {}
      const { userName, password } = state.loginInfo
      switch (state.loginType) {
        case 'username':
          res = await login({ userName, password })
          break
        case 'email':
          res = await loginByEmail({ email: userName })
          break
        case 'phone':
          res = await loginByPhone()
          break
        default:
          break
      }
      res = res.data
      if (res.code === 200) {
        saveData('token', { userId: res.data.userId, token: res.data.token, exceedTime: 1000 * 60 * 60 * 24 * 7, time: Date.now() })
        message.success('登录成功')
        router.push('/')
      } else {
        message.error(res.message)
      }
    }
    return { ...toRefs(state), rules, loginHandle, switchLogin }
  }
})
</script>
<style lang="scss" scoped>
.login {
  height: calc(100vh - 60px);
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
  box-shadow: 2px 2px 10px 3px rgb(209, 222, 240);
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
