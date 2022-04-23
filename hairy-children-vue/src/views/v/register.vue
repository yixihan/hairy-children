<template>
  <div class="login">
    <div class="way">
      <span>注册</span>
    </div>
    <el-form
      ref="user"
      :model="user"
      label-width="80px"
      label-position="left"
      :rules="rules"
    >
      <el-form-item label="用户名" prop="userName" placeholder="请输入用户名">
        <el-input v-model="user.userName"></el-input>
      </el-form-item>
      <el-form-item
        label="密码"
        prop="userPassword"
        placeholder="请输入密码 (8-16位)"
      >
        <el-input v-model="user.userPassword" type="password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="toLogin">登录</el-button>
        <el-button type="success" @click="onSubmit('user')">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      flag: false,
      user: {
        userName: "",
        userPassword: "",
      },
      rules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        userPassword: [
          { required: true, message: "请输入活动名称", trigger: "blur" },
          {
            min: 8,
            max: 16,
            message: "长度在 8 到 16 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    // 提交表单
    onSubmit(formName) {
      // 数据校验
      this.$refs[formName].validate((valid) => {
        // 数据校验成功
        if (valid) {
          // 注册
          this.register();
        } else {
          // 数据校验失败
          this.$message({
            message: "请正确输入",
            type: "error",
          });
          return false;
        }
      });
    },
    // 注册
    register() {
      // 校验用户名是否已被注册
      this.axios({
        url: "/code/verifyUserName",
        method: "post",
        data: {
          userName: this.user.userName
        },
      }).then(({ data }) => {
        console.log("verify", data.data.verify);
        this.flag = data.data.verify;
        console.log("flag", this.flag);
        // 如果用户名未被注册
        if (this.flag) {
          this.axios({
            url: "/v/register",
            method: "post",
            data: {
              userName: this.user.userName,
              userPassword: this.user.userPassword
            },
          }).then(({ data }) => {
            console.log(data);
            // 如果用户名已被注册
            this.$message({
              message: "注册成功!",
              type: "success",
            });
            this.toLogin();
          });
        } else {
          // 如果用户名已被注册
          this.$message({
            message: "该用户名已被注册",
            type: "error",
          });
        }
      });
    },
    // 跳转到注册界面
    toLogin() {
      this.$router.push("/login");
    },
  },
};
</script>

<style lang="scss" scoped>
.login {
  padding-top: 60px;
  margin: 0 auto;
  .way {
    margin: 0 auto;
    padding: 10px;
    padding-bottom: 20px;
  }
  .el-form-item__content ::deep {
    margin-left: 80px;
    position: relative;
  }
}

.verify {
  position: absolute !important;
  left: 0 !important;
  width: 150px !important;
  display: inline-block !important;
}
.el-button--primary {
  position: absolute;
  right: 0;
  display: inline-block;
}

#code {
  display: none;
}
.el-step {
  flex-basis: 50%;
  margin-right: 0px;
  width: 150px !important;
}
</style>