<template>
  <div class="login">
    <el-row :gutter="20">
      <el-col :span="6" :offset="8">
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
        >
          <el-form-item :label="msg1">
            <el-input
              v-show="msg1 == '用户名'"
              v-model="user.userName"
            ></el-input>
            <el-input v-show="msg1 == '邮箱'" v-model="user.email"></el-input>
            <el-input v-show="msg1 == '电话'" v-model="user.phone"></el-input>
          </el-form-item>
          <el-form-item :label="msg2">
            <el-input
              v-model="user.password"
              v-show="msg2 == '密码'"
            ></el-input>
            <el-input
              v-model="user.code"
              v-if="msg2 != '密码'"
              class="verify"
            ></el-input>
            <el-button type="primary" v-if="msg2 != '密码'" @click="sendCode"
              >发送验证码</el-button
            >
          </el-form-item>
          <el-form-item>
            <el-button @click="register">注册</el-button>
            <el-button type="success" @click="onSubmit">登录</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
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
    };
  },
  methods: {
    // 登录
    onSubmit() {
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
    // 校验验证码
    verifyCode() {
      // 校验验证码是否为空
      if (this.user.code == null || this.user.code == "") {
        this.$message({
          message: "请输入验证码",
          type: "error",
        });
        return false;
      }

      if (this.msg1 == "邮箱") {
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
          this.flag = true;
        });
      } else if (this.msg1 == "电话") {
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
          return data.code == 200;
        });
      }
    },
    // 通过用户名 + 密码登录
    loginByName() {
      // 校验用户名是否输入
      if (this.user.userName == null || this.user.userName == "") {
        this.$message({
          message: "请输入用户名",
          type: "error",
        });
        return;
      }

      // 校验密码是否输入
      if (this.user.password == null || this.user.password == "") {
        this.$message({
          message: "请输入密码",
          type: "error",
        });
        return;
      }

      this.axios({
        url: "/v/login",
        method: "post",
        data: {
          'userName': this.user.userName,
          'password': this.user.password 
        }
      }).then( ({data}) => {
        this.setInfo(data);
      })
    },
    // 通过邮箱 + 验证码登录
    loginByEmail() {
      // 校验邮箱
      if (this.user.email == null || this.user.email == "") {
        this.$message({
          message: "请输入邮箱",
          type: "error",
        });
        return;
      }
      // 校验邮箱正确性
      if (!this.isEmail(this.user.email)) {
        this.$message({
          message: "邮箱格式不正确, 请重新输入",
          type: "error",
        });
        return;
      }

      // 校验验证码是否正确
      this.verifyCode();
      if (!this.flag) {
        return;
      }

      // 登录
      this.axios({
        url: "/v/loginByEmail",
        data: this.user.email,
        method: "post",
      }).then(({ data }) => {
        this.setInfo(data);
      });
    },
    // 通过电话 + 验证码登录
    loginByPhone() {
      // 校验电话
      if (this.user.phone == null || this.user.phone == "") {
        this.$message({
          message: "请输入电话",
          type: "error",
        });
        return;
      }
      // 校验手机号输入是否正确
      if (!this.isMobile(this.user.phone)) {
        this.$message({
          message: "手机号格式不正确, 请重新输入",
          type: "error",
        });
        return;
      }

      // 校验验证码是否正确
      this.verifyCode();
      if (!this.flag) {
        return;
      }

      // 登录
      this.axios({
        url: "/v/loginByPhone",
        data: this.user.phone,
        method: "post",
      }).then(({ data }) => {
        this.setInfo(data);
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
        data: userId,
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
    // 跳转到主页界面
    register() {
      this.$router.push("/registry");
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
</style>