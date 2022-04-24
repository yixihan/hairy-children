<template>
  <div class="email">
    <div v-if="isBindEmail">
      <div class="msg">
        <i class="el-icon-message"></i>
        <span>您已绑定邮箱</span>
      </div>
      <el-button class="primary button" @click="unboundEmail"
        >取消绑定邮箱</el-button
      >
    </div>
    <div v-if="!isBindEmail">
      <div class="msg">
        <i class="el-icon-message"></i>
        <span>您还未绑定邮箱</span>
      </div>
      <el-button class="primary button" @click="checked = !checked"
        >绑定邮箱</el-button
      >
    </div>

    <div v-if="checked">
      <el-form
        ref="form"
        :model="form"
        label-width="80px"
        label-position="top"
        :rules="rules"
      >
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" class="submit" @click="submitForm('form')"
            >绑定邮箱</el-button
          >
          <el-button type="primary" @click="unSubmit">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  inject: ["reload"],
  data() {
    return {
      isBindEmail: false,
      checked: false,
      userId: "",
      userInfo: "",
      form: {
        email: "",
      },
      rules: {
        email: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
      },
    };
  },
  methods: {
    async getUserInfo(userId) {
      const ans = await this.$axios({
        url: "/user/getUserInfo",
        method: "post",
        data: {
          userId: userId,
        },
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
      });
      return ans;
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid && this.isEmail(this.form.email)) {
          this.bindEmail().then(({ data }) => {
            console.log(data);
            if (data.code == 200) {
              this.$message({
                message: "绑定邮箱成功",
                type: "success",
              });
            } else {
              this.$message({
                message: "绑定邮箱失败, 请重试",
                type: "error",
              });
            }
            this.reload();
          });
        } else {
          this.$message({
            message: "请正确输入",
            type: "error",
          });
          return false;
        }
      });
    },
    unSubmit() {
      this.checked = !this.checked;
      this.form = {
        email: "",
      };
    },
    unboundEmail() {
      this.$axios({
        url: "/user/unboundEmail",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
      }).then(({ data }) => {
        if (data.code == 200) {
          this.$message({
            message: "解绑邮箱成功",
            type: "success",
          });
        } else {
          this.$message({
            message: "解绑邮箱失败, 请重试",
            type: "error",
          });
        }
        this.reload();
      });
    },
    async bindEmail() {
      const ans = await this.$axios({
        url: "/user/bindEmail",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          email: this.form.email,
        },
      });
      return ans;
    },
    isEmail(s) {
      return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(
        s
      );
    },
  },
  created() {
    this.userId = this.$route.params.userId;
    this.getUserInfo(this.userId).then(({ data }) => {
      this.userInfo = data.data;
      if (this.userInfo.userEmail == null || this.userInfo.userEmail == "") {
        this.isBindEmail = false;
      } else {
        this.isBindEmail = true;
      }
    });
  },
  // 需要修改
  updated() {
    this.getUserInfo(this.userId).then(({ data }) => {
      this.userInfo = data.data;
      if (this.userInfo.userEmail == null || this.userInfo.userEmail == "") {
        this.isBindEmail = false;
      } else {
        this.isBindEmail = true;
      }
    });
  },
};
</script>

<style lang="scss" scoped>
.email {
  margin: 100px;
  padding-top: auto;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .msg {
    margin: 10px;
    padding: 10px;
  }

  .button {
    margin: 10px;
    padding: 10px;
  }

  .submit {
    float: left;
  }

  .el-form-item {
    margin: 10px !important;
    padding: 10px !important;
  }
}
</style>