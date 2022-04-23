<template>
  <div class="register">
    <n-form ref="formRef" :model="registerInfo" :rules="rules">
      <div class="head">
        <n-h2 prefix="bar">
          <n-text> 注册 </n-text>
        </n-h2>
      </div>
      <n-form-item path="username" label="账号">
        <n-input v-model:value="registerInfo.userName" @keydown.enter.prevent />
      </n-form-item>
      <n-form-item path="password" label="密码">
        <n-input v-model:value="registerInfo.userPassword" type="password" @keydown.enter.prevent />
      </n-form-item>
      <n-row :gutter="[0, 24]">
        <n-col :span="24">
          <div style="display: flex; justify-content: flex-end">
            <n-button round type="primary" @click="registerHandle"> 注册 </n-button>
          </div>
        </n-col>
      </n-row>
    </n-form>
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs } from 'vue'
import { NForm, NFormItem, NInput, NButton, NRow, NCol, NH2, NText } from 'naive-ui'
import { register } from '../api'

export default defineComponent({
  name: 'RegisterPage',
  components: {
    NForm,
    NFormItem,
    NInput,
    NButton,
    NRow,
    NCol,
    NH2,
    NText
  },
  setup() {
    const state = reactive({
      registerInfo: {
        userName: '',
        userPassword: ''
      }
    })
    const registerHandle = async () => {
      const { data: res } = await register(state.registerInfo)
      if (res.code === 200) {
        alert('注册成功')
      }
    }
    return { ...toRefs(state), registerHandle }
  }
})
</script>
<style lang="scss" scoped>
.register {
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
