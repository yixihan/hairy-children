<template>
  <div class="login">
    <div class="way">
      <span>更改密码</span>
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
        <el-form-item label="新密码" prop="password1" placeholder="请输入密码">
          <el-input
            v-model="user.password1"
            type="password"
            placeholder="请输入密码"
          >
          </el-input>
        </el-form-item>
        <el-form-item
          label="确认密码"
          prop="password2"
          placeholder="请输入密码"
        >
          <el-input
            v-model="user.password2"
            type="password"
            placeholder="请输入密码"
          >
          </el-input>
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
        <el-button v-if="active < 2" type="primary" @click="next('user')">
          下一步
        </el-button>
        <el-button v-if="active > 0 && active < 2" @click="pre">
          上一步
        </el-button>
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
        password2: "",
        code: "",
      },
      rules: {
        val: [{ required: true, message: "请输入", trigger: "blur" }],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "请正确输入", trigger: "blur" },
        ],
        password1: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 8,
            max: 16,
            message: "长度在 8 到 16 个字符",
            trigger: "blur",
          },
        ],
        password2: [
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
          console.log("现在是第 " + this.active + " 步");
          // 第一步, 校验邮箱/电话是否绑定用户
          if (this.active == 0) {
            this.verify().then((result) => {
              console.log("是否已被绑定 : ", !result);
              if (!result) {
                this.active = 1;
              } else {
                this.$message({
                  message: "请正确输入",
                  type: "error",
                });
              }
            });
          }
          // 第二步, 校验密码输入, 验证码
          else if (this.active == 1) {
            // 先判断两个密码是否一致
            if (this.user.password1 != this.user.password2) {
              this.$message({
                message: "密码不一致, 请仔细检查",
                type: "error",
              });
              return;
            }
            // 再判断验证码是否正确
            this.verifyCode().then((result) => {
              console.log("验证码是否正确 : ", result);
              if (result) {
                // 最后进行更改密码的操作
                this.resetPass().then((result) => {
                  console.log("result : ", result);
                  if (result.data.code == 200) {
                    this.active = 2;
                  }
                });
              }
            });
          }
          // 其余情况, 弹出异常信息
          else {
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
      }
      console.log("verify : ", verify);
      return verify;
    },
    // 通过邮箱确认
    async verifyEmail() {
      const verify = await this.axios({
        url: "/code/verifyUserEmail",
        method: "post",
        data: {
          email: this.user.val,
        },
      });
      return verify.data.data.verify;
    },
    // 通过电话确认
    async verifyPhone() {
      const verify = await this.axios({
        url: "/code/verifyUserPhone",
        method: "post",
        data: {
          phone: this.user.val,
        },
      });
      return verify.data.data.verify;
    },
    // 发送验证码
    sendCode() {
      // 输入的是手机号
      if (this.isMobile(this.user.val)) {
        // 发送验证码
        this.axios({
          url: "/code/sendCodeByPhone",
          data: {
            phone: this.user.val,
          },
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
          data: {
            email: this.user.val,
          },
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
    // 校验验证码
    async verifyCode() {
      let data;

      if (this.isMobile(this.user.val)) {
        data = this.verifyCodeByPhone();
      } else if (this.isEmail(this.user.val)) {
        data = this.verifyCodeByEmail();
      }

      return data;
    },
    // 通过电话校验验证码
    async verifyCodeByPhone() {
      const verify = await this.axios({
        url: "/code/verifyPhoneCode",
        data: {
          phone: this.user.val,
          code: this.user.code,
        },
        method: "post",
      });
      return verify.data.data.verify;
    },
    // 通过邮箱校验验证码
    async verifyCodeByEmail() {
      const verify = await this.axios({
        url: "/code/verifyEmailCode",
        data: {
          email: this.user.val,
          code: this.user.code,
        },
        method: "post",
      });
      return verify.data.data.verify;
    },
    // 重置密码
    async resetPass() {
      let ans;

      if (this.isMobile(this.user.val)) {
        ans = this.resetPassByPhone();
      } else if (this.isEmail(this.user.val)) {
        ans = this.resetPassByEmail();
      }

      return ans;
    },
    async resetPassByPhone() {
      const ans = await this.axios({
        url: "/v/resetPasswordByPhone",
        data: {
          phone: this.user.val,
          password: this.user.password1,
        },
        method: "post",
      });

      return ans;
    },
    async resetPassByEmail() {
      const ans = await this.axios({
        url: "/v/resetPasswordByEmail",
        data: {
          email: this.user.val,
          password: this.user.password1,
        },
        method: "post",
      });

      return ans;
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
  width: 40%;
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
  margin-right: 35px;
}
.el-button--primary {
  position: absolute !important;
  right: 0 !important;
  display: inline-block;
  width: 30% !important;
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
