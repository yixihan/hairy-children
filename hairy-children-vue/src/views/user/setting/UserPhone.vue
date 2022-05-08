<template>
  <div class="phone">
    <div v-if="isBindPhone">
      <div class="msg">
        <i class="el-icon-mobile-phone"></i>
        <span>您已绑定电话</span>
      </div>
      <el-button class="primary button" @click="unboundPhone">
        取消绑定电话
      </el-button>
    </div>
    <div v-if="!isBindPhone">
      <div class="msg">
        <i class="el-icon-mobile-phone"></i>
        <span>您还未绑定电话</span>
      </div>
      <el-button class="primary button" @click="checked = !checked">
        绑定电话
      </el-button>
    </div>

    <div v-if="checked">
      <el-form
        ref="form"
        :model="form"
        label-width="80px"
        label-position="top"
        :rules="rules"
      >
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" class="submit" @click="submitForm('form')">
            绑定电话
          </el-button>
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
      isBindPhone: false,
      checked: false,
      userId: "",
      userInfo: "",
      form: {
        phone: "",
      },
      rules: {
        phone: [{ required: true, message: "请输入电话", trigger: "blur" }],
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
        if (valid && this.isPhone(this.form.phone)) {
          this.bindPhone().then(({ data }) => {
            console.log(data);
            if (data.code == 200) {
              this.$message({
                message: "绑定电话成功",
                type: "success",
              });
            } else {
              this.$message({
                message: "绑定电话失败, 请重试",
                type: "error",
              });
            }
            this.reloadUserMsg();
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
        phone: "",
      };
    },
    unboundPhone() {
      this.$axios({
        url: "/user/unboundPhone",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
      }).then(({ data }) => {
        if (data.code == 200) {
          this.$message({
            message: "解绑电话成功",
            type: "success",
          });
        } else {
          this.$message({
            message: "解绑电话失败, 请重试",
            type: "error",
          });
        }
        this.reloadUserMsg();
        this.reload();
      });
    },
    async bindPhone() {
      const ans = await this.$axios({
        url: "/user/bindPhone",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          phone: this.form.phone,
        },
      });
      return ans;
    },
    isPhone(s) {
      return /^1[0-9]{10}$/.test(s);
    },
    reloadUserMsg() {
      this.axios({
        url: "/user/getUserInfo",
        method: "post",
        data: {
          userId: this.$store.getters.getUserId,
        },
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
      }).then(({ data }) => {
        const userInfo = data.data;
        this.$store.commit("SET_USERINFO", userInfo);
      });
    },
  },
  created() {
    this.userId = this.$route.params.userId;
    this.getUserInfo(this.userId).then(({ data }) => {
      this.userInfo = data.data;
      if (this.userInfo.userPhone == null || this.userInfo.userPhone == "") {
        this.isBindPhone = false;
      } else {
        this.isBindPhone = true;
      }
    });
  },
  // 需要修改
  updated() {
    this.getUserInfo(this.userId).then(({ data }) => {
      this.userInfo = data.data;
      if (this.userInfo.userPhone == null || this.userInfo.userPhone == "") {
        this.isBindPhone = false;
      } else {
        this.isBindPhone = true;
      }
    });
  },
};
</script>

<style lang="scss" scoped>
.phone {
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
