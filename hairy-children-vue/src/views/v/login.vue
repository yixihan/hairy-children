<template>
  <div class="login">
    <div class="way">
      <span @click="changeLoginWay(1)">用户名登录</span>
      <el-divider direction="vertical"></el-divider>
      <span @click="changeLoginWay(2)">邮箱登录</span>
      <el-divider direction="vertical"></el-divider>
      <span @click="changeLoginWay(3)">电话登录</span>
    </div>

    <el-form
      ref="user"
      :model="user"
      label-width="80px"
      label-position="left"
      :rules="rules"
    >
      <el-form-item
        label="用户名"
        v-if="msg1 == '用户名'"
        prop="userName"
        placeholder="请输入用户名"
      >
        <el-input v-model="user.userName"></el-input>
      </el-form-item>
      <el-form-item
        label="邮箱"
        v-if="msg1 == '邮箱'"
        prop="email"
        placeholder="请输入邮箱"
      >
        <el-input v-show="msg1 == '邮箱'" v-model="user.email"></el-input>
      </el-form-item>
      <el-form-item
        label="电话"
        v-if="msg1 == '电话'"
        prop="phone"
        placeholder="请输入电话"
      >
        <el-input v-show="msg1 == '电话'" v-model="user.phone"></el-input>
      </el-form-item>

      <el-form-item
        label="密码"
        v-if="msg2 == '密码'"
        prop="password"
        placeholder="请输入密码 (8-16位)"
      >
        <el-input v-model="user.password" type="password"></el-input>
      </el-form-item>
      <el-form-item
        label="验证码"
        v-if="msg2 != '密码'"
        prop="code"
        placeholder="请输入验证码"
      >
        <el-input v-model="user.code" class="verify"></el-input>
        <el-button type="primary" @click="sendCode">发送验证码</el-button>
      </el-form-item>

      <el-form-item label="" class="pa">
        <router-link to="/resetPassword" class="resetPassword"
          >忘记密码?</router-link
        >
      </el-form-item>

      <el-form-item class="submit">
        <el-button @click="toRegister">注册</el-button>
        <el-button type="success" @click="onSubmit('user')">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  inject: ["reload"],
  data() {
    return {
      msg1: "用户名",
      msg2: "密码",
      type: 1,
      flag: false,
      user: {
        userName: "",
        phone: "",
        email: "",
        password: "",
        code: "",
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
        phone: [
          { required: true, message: "请输入电话", trigger: "blur" },
          { min: 11, max: 11, message: "请正确输入", trigger: "blur" },
        ],
        email: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 8,
            max: 16,
            message: "长度在 8 到 16 个字符",
            trigger: "blur",
          },
        ],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "请正确输入", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    // 登录
    onSubmit(formName) {
      // 数据校验
      this.$refs[formName].validate((valid) => {
        // 数据校验成功
        if (valid) {
          console.log("登录方式 : ");
          if (this.type == 1) {
            console.log("用户名 + 密码 ");
            this.loginByName();
          } else if (this.type == 2) {
            console.log("邮箱 + 验证码");
            this.loginByEmail();
          } else if (this.type == 3) {
            console.log("电话 + 验证码 ");
            this.loginByPhone();
          } else {
            this.$messgae({
              message: "未知错误, 请刷新页面重试",
              type: "error",
            });
          }
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
    // 切换登录方式
    changeLoginWay(type) {
      if (type == 1) {
        this.type = 1;
        this.msg1 = "用户名";
        this.msg2 = "密码";
      } else if (type == 2) {
        this.type = 2;
        this.msg1 = "邮箱";
        this.msg2 = "验证码";
      } else if (type == 3) {
        this.type = 3;
        this.msg1 = "电话";
        this.msg2 = "验证码";
      }
    },
    // 发送验证码
    sendCode() {
      if (this.msg1 == "电话") {
        // 校验手机号输入是否正确
        if (!this.isMobile(this.user.phone)) {
          this.$message({
            message: "手机号格式不正确, 请重新输入",
            type: "error",
          });
          return;
        }

        // 发送验证码
        this.axios({
          url: "/code/sendCodeByPhone",
          data: this.user.phone,
          method: "post",
        }).then(({ data }) => {
          console.log(data);
          if (data.code == 200) {
            this.$message({
              message: "验证码发送成功, 请注意查收",
              type: "success",
            });
          }
        });
      } else if (this.msg1 == "邮箱") {
        // 校验邮箱输入是否正确
        if (!this.isEmail(this.user.email)) {
          this.$message({
            message: "邮箱格式不正确, 请重新输入",
            type: "error",
          });
          return;
        }

        // 发送验证码
        this.axios({
          url: "/code/sendCodeByEmail",
          data: this.user.email,
          method: "post",
        }).then(({ data }) => {
          console.log(data);
          if (data.code == 200) {
            this.$message({
              message: "验证码发送成功, 请注意查收",
              type: "success",
            });
          }
        });
      } else {
        this.$messgae({
          message: "未知错误, 请刷新页面重试",
          type: "error",
        });
      }
    },
    // 通过用户名 + 密码登录
    loginByName() {
      this.axios({
        url: "/v/login",
        method: "post",
        data: {
          userName: this.user.userName,
          password: this.user.password,
        },
      }).then(({ data }) => {
        this.setInfo(data);
      });
    },
    // 通过邮箱 + 验证码登录
    loginByEmail() {
      // 校验邮箱输入是否正确
      if (!this.isEmail(this.user.email)) {
        this.$message({
          message: "邮箱格式不正确, 请重新输入",
          type: "error",
        });
        return false;
      }

      // 校验验证码
      this.axios({
        url: "/code/verifyEmailCode",
        data: {
          email: this.user.email,
          code: this.user.code,
        },
        method: "post",
      }).then(({ data }) => {
        console.log(data);
        this.flag = data.data.verify;
        if (this.flag) {
          // 登录
          this.axios({
            url: "/v/loginByEmail",
            data: this.user.email,
            method: "post",
          }).then(({ data }) => {
            this.setInfo(data);
          });
        } else {
          this.$messgae({
            message: "验证码有误, 请仔细检查",
            type: "error",
          });
        }
      });
    },
    // 通过电话 + 验证码登录
    loginByPhone() {
      // 校验手机号输入是否正确
      if (!this.isMobile(this.user.phone)) {
        this.$message({
          message: "手机号格式不正确, 请重新输入",
          type: "error",
        });
        return false;
      }

      // 校验验证码
      this.axios({
        url: "/code/verifyPhoneCode",
        data: {
          phone: this.user.phone,
          code: this.user.code,
        },
        method: "post",
      }).then(({ data }) => {
        console.log(data);
        this.flag = data.data.verify;
        if (this.flag) {
          // 登录
          this.axios({
            url: "/v/loginByPhone",
            data: this.user.phone,
            method: "post",
          }).then(({ data }) => {
            this.setInfo(data);
          });
        } else {
          this.$messgae({
            message: "验证码有误, 请仔细检查",
            type: "error",
          });
        }
      });
    },
    // 登录成功后, 将 userInfo 和 token 存入全局库
    setInfo(data) {
      const jwt = data.data.token;

      //存储(共享)全局变量jwt和userInfo
      this.$store.commit("SET_TOKEN", jwt);

      // 获取 userInfo
      this.getUserInfo(data.data.userId, jwt);

      // 重载页面, 跳转到主页
      this.reload();
      this.$router.push("/");
    },
    // 登录后获取用户信息并存入全局变量中
    getUserInfo(userId, jwt) {
      this.axios({
        url: "/user/getUserInfo",
        method: "post",
        data: {
          userId: userId
        },
        headers: {
          "Jwt-Token": jwt,
        },
      }).then(({ data }) => {
        const userInfo = data.data;
        this.$store.commit("SET_USERINFO", userInfo);
      });
    },
    // 校验手机号是否合规
    isMobile(s) {
      return /^1[0-9]{10}$/.test(s);
    },
    // 校验邮箱是否合规
    isEmail(s) {
      return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(
        s
      );
    },
    // 跳转到注册界面
    toRegister() {
      this.$router.push("/register");
    },
  },
};
</script>

<style>
.login {
  padding-top: 60px;
  margin: 0 auto;
}
.el-form-item__content ::deep {
  margin-left: 80px;
  position: relative;
}
.verify {
  position: absolute !important;
  left: 0 !important;
  width: 150px !important;
  display: inline-block !important;
}
.pa {
  height: 40px;
  line-height: 40px;
}
.submit > .el-form-item__content {
  margin: 0 !important;
}
.el-form-item__content {
  width: auto;
  height: 100%;
  position: relative;
}
.el-form {
  width: 25%;
  margin: 0 auto;
}
.el-form-item__content > a {
  position: absolute;
  right: 0;
}
.el-button--primary {
  position: absolute;
  right: 0;
  display: inline-block;
}
.way {
  margin: 0 auto;
  padding: 10px;
  padding-bottom: 20px;
}
#code {
  display: none;
}
.resetPassword {
  position: absolute !important;
  flex: right !important;
  margin-bottom: 50px;
}
</style>