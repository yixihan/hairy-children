<template>
  <div class="login">
    <el-row :gutter="20">
      <el-col :span="6" :offset="8">
        <div class="way">
          <span>重置密码</span>
        </div>
        <el-steps :active="active" finish-status="success">
          <el-step title="步骤 1"> </el-step>
          <el-step title="步骤 2"></el-step>
          <el-step title="步骤 3"></el-step>
        </el-steps>
        <el-form
          ref="user"
          :model="user"
          label-width="100px"
          label-position="left"
          :rules="rules"
          :v-if="active == 0"
        >
          <el-form-item label="邮箱 / 电话" prop="val" placeholder="请输入邮箱">
            <el-input v-model="user.val"></el-input>
          </el-form-item>

          <el-form-item label="验证码" prop="code" placeholder="请输入验证码">
            <el-input v-model="user.code" class="verify"></el-input>
            <el-button type="primary" @click="sendCode">发送验证码</el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="toLogin">登录</el-button>
            <el-button type="success" @click="onSubmit('user')">重置</el-button>
          </el-form-item>
        </el-form>
        <el-button style="margin-top: 12px" @click="next">下一步</el-button>
      </el-col>
    </el-row>
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
        code: "",
      },
      rules: {
        val: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "请正确输入", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    next() {
      if (this.active++ > 2) this.active = 0;
    },
    // 登录
    onSubmit(formName) {
      // 数据校验
      this.$refs[formName].validate((valid) => {
        // 数据校验成功
        if (valid) {
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