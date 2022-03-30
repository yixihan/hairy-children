<template>
  <div class="login">
    <div class="way">
      <span>重置密码</span>
    </div>
    <div>
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="第一步" />
        <el-step title="第二步" />
        <el-step title="第三步" />
      </el-steps>
    </div>

    <el-form
      ref="user"
      :model="user"
      label-width="100px"
      label-position="left"
      :rules="rules"
    >
      <div v-if="active == 0">
        <el-form-item label="邮箱 / 电话" prop="val" placeholder="请输入邮箱">
          <el-input v-model="user.val"></el-input>
        </el-form-item>
      </div>

      <div v-if="active == 1">
        <el-form-item label="新密码" prop="password" placeholder="请输入密码">
          <el-input
            v-model="user.password1"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="password" placeholder="请输入密码">
          <el-input
            v-model="user.password2"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" placeholder="请输入验证码">
          <el-input v-model="user.code" class="verify"></el-input>
          <el-button type="primary" @click="sendCode">发送验证码</el-button>
        </el-form-item>
      </div>
      <div v-if="active == 2">
        <p>密码修改成功!</p>
        <el-button type="success" @click="toLogin">登录</el-button>
      </div>
      <el-form-item>
        <el-button v-if="active < 2" type="primary" @click="next('user')"
          >下一步</el-button
        >
        <el-button v-if="active > 0 && active < 2" @click="pre"
          >上一步</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  inject: ["reload"],
  data() {
    return {
      flag: false,
      active: 0,
      user: {
        val: "",
        password1: "",
        passwprd2: "",
        code: "",
      },
      rules: {
        val: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "请正确输入", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
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
    // 步骤条下一步的方法
    next(formName) {
      // 数据校验
      this.$refs[formName].validate((valid) => {
        // 数据校验成功
        if (valid) {
          console.log(this.active);
          if (this.active == 0) {
            let isOk;
            isOk = this.verifyPhone();
            console.log("isOk : ", isOk);
            if (isOk && this.active++ > 2) {
              this.active = 1;
            }
          } else if (this.active == 1) {
            this.resetPass();
          } else {
            this.$message({
              message: "未知异常, 请刷新页面重试",
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
    // 步骤条上一步的方法
    pre() {
      if (this.active-- < 1) {
        this.active = 1;
      }
    },
    // 确认重置密码的账号
    async verify() {
      let verify = "";
      if (this.isMobile(this.user.val)) {
        verify = await this.verifyPhone();
      } else if (this.isEmail(this.user.val)) {
        verify = await this.verifyEmail();
      } else {
        this.$message ({
          message: "输入错误, 请仔细检查",
          type: 'error'
        })
      }
      console.log("verify : " + verify);
      return verify;
    },
    // 通过邮箱确认
    async verifyEmail() {
      const verify = await this.axios({
        url: "/code/verifyUserEmail",
        method: "post",
        data: this.user.val,
      });
      return verify;
    },
    // 通过电话确认
    async verifyPhone() {
      const verify = await this.axios({
        url: "/code/verifyUserPhone",
        method: "post",
        data: this.user.val,
      });
      console.log("verifyPhone : ", verify);
      return verify;
    },
    // 发送验证码
    sendCode() {
      // 输入的是手机号
      if (this.isMobile(this.user.val)) {
        // 发送验证码
        this.axios({
          url: "/code/sendCodeByPhone",
          data: this.user.val,
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
        // 输入的是邮箱
      } else if (this.isEmail(this.user.val)) {
        // 发送验证码
        this.axios({
          url: "/code/sendCodeByEmail",
          data: this.user.val,
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
        // 其余情况返回错误信息
        this.$message({
          message: "邮箱 / 电话输入错误, 请重新输入",
          type: "error",
        });
      }
    },
    // 重置密码
    resetPass() {
      // 数据校验
      if (this.isMobile(this.user.val)) {
        this.resetPasswordByPhone();
      } else if (this.isEmail(this.user.val)) {
        this.resetPasswordByEmail();
      } else {
        // 数据校验失败
        this.$message({
          message: "请正确输入",
          type: "error",
        });
        return false;
      }
    },
    // 通过电话重置密码
    resetPasswordByPhone() {
      // 校验验证码
      this.axios({
        url: "/code/verifyEmailCode",
        data: {
          email: this.user.val,
          code: this.user.code,
        },
        method: "post",
      }).then(({ data }) => {
        console.log(data);
        this.flag = data.data.verify;
        if (this.flag) {
          console.log();
        } else {
          this.$messgae({
            message: "验证码有误, 请仔细检查",
            type: "error",
          });
        }
      });
    },
    // 通过邮件重置密码
    resetPasswordByEmail() {
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
          console.log(this.flag);
        } else {
          this.$messgae({
            message: "验证码有误, 请仔细检查",
            type: "error",
          });
        }
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
  .el-steps {
    margin: 0 auto;
    display: contents !important;
    align-items: center !important;
    justify-content: center !important;
    margin-bottom: 20px;

    .is-horizontal {
      margin: 0 auto;
      padding-bottom: 10px;
    }
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