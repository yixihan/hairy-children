<template>
  <div class="authentication">
    <div v-if="isAuthentication">
      <div class="msg">
        <i class="el-icon-postcard"></i>
        <span>您已完成个人实名认证</span>
      </div>
      <el-button class="primary button" @click="checked = !checked">
        修改实名认证
      </el-button>
    </div>
    <div v-if="!isAuthentication">
      <div class="msg">
        <i class="el-icon-postcard"></i>
        <span>您还未完成个人实名认证</span>
      </div>
      <el-button class="primary button" @click="checked = !checked">
        实名认证
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
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName"></el-input>
        </el-form-item>
        <el-form-item label="身份证" prop="identityCard">
          <el-input v-model="form.identityCard"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" class="submit" @click="submitForm('form')">
            认证
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
      isAuthentication: false,
      checked: false,
      userId: "",
      userInfo: "",
      form: {
        realName: "",
        identityCard: "",
      },
      rules: {
        realName: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
        ],
        identityCard: [
          { required: true, message: "请输入身份证号", trigger: "blur" },
          { min: 18, max: 18, message: "请正确输入", trigger: "blur" },
        ],
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
        if (valid && this.isIdentityCard(this.form.identityCard)) {
          this.authentication().then(({ data }) => {
            console.log(data);
            if (data.code == 200) {
              this.$message({
                message: "实名认证成功",
                type: "success",
              });
              this.reload();
            } else {
              this.$message({
                message: "实名认证失败, 请重试",
                type: "error",
              });
            }
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
        realName: "",
        identityCard: "",
      };
    },
    async authentication() {
      const ans = await this.$axios({
        url: "/user-info/authentication",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          realName: this.form.realName,
          identityCard: this.form.identityCard,
        },
      });
      return ans;
    },
    isIdentityCard(identityCard) {
      var pattern = /\d{17}[0-9Xx]|\d{15}/g;
      return pattern.test(identityCard);
    },
  },
  created() {
    this.userId = this.$route.params.userId;
    this.getUserInfo(this.userId).then(({ data }) => {
      this.userInfo = data.data;
      if (
        this.userInfo.userIdentityCard == null ||
        this.userInfo.userIdentityCard == ""
      ) {
        this.isAuthentication = false;
      } else {
        this.isAuthentication = true;
      }
    });
  },
};
</script>

<style lang="scss" scoped>
.authentication {
  margin: 100px;
  padding: 10px;
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