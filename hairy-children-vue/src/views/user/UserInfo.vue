<template>
  <div class="userInfo">
    <el-descriptions class="margin-top" :column="2" size="medium" border>
      <template
        slot="extra"
        v-if="userId == this.$store.getters.getUser.userId"
      >
        <button class="update" @click="updated">更新</button>
      </template>
      <el-descriptions-item label="用户头像">
        <template>
          <el-avatar v-if="!checked" class="avatar" :src="userInfo.userAvatar">
          </el-avatar>
          <el-upload
            class="avatar-uploader"
            action="http://175.24.229.41:9421/user-info/uploadAvatar"
            name="avatar"
            :headers="JwtToken"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            v-if="checked"
          >
            <el-avatar
              v-if="userInfo.userAvatar"
              :src="userInfo.userAvatar"
              class="avatar"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-descriptions-item>
      <el-descriptions-item label="用户名">
        <span v-text="userInfo.userName"></span>
      </el-descriptions-item>
      <el-descriptions-item label="用户 ID">
        <span v-text="userInfo.userId"></span>
      </el-descriptions-item>
      <el-descriptions-item
        label="所在城市"
        v-if="userInfo.userAddress != null"
      >
        <span v-text="userInfo.userAddress" v-if="!checked"></span>
        <el-cascader
          v-if="checked"
          size="mini"
          :options="options"
          v-model="selectedOptions"
          @change="handleChange"
        >
        </el-cascader>
        <el-switch
          v-model="userInfo.addressShow"
          active-text="展示"
          inactive-text="隐藏"
          :active-value="1"
          :inactive-value="0"
          v-if="checked"
        >
        </el-switch>
      </el-descriptions-item>
      <el-descriptions-item
        label="真实姓名"
        v-if="userInfo.userRealName != null"
      >
        <span v-text="userInfo.userRealName"></span>
      </el-descriptions-item>
      <el-descriptions-item
        label="身份证号"
        v-if="userInfo.userIdentityCard != null"
      >
        <span v-text="userInfo.userIdentityCard"></span>
      </el-descriptions-item>
      <el-descriptions-item
        label="性别"
        v-if="userInfo.userGender != null && userInfo.userGender != ''"
      >
        <span v-text="userInfo.userGender" v-if="!checked"></span>
        <el-input v-model="userInfo.userGender" v-if="checked"></el-input>
        <el-switch
          v-model="userInfo.genderShow"
          active-text="展示"
          inactive-text="隐藏"
          :active-value="1"
          :inactive-value="0"
          v-if="checked"
        >
        </el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="年龄" v-if="userInfo.userAge != null">
        <span v-text="userInfo.userAge" v-if="!checked"></span>
        <el-date-picker
          type="date"
          v-model="userInfo.userBirth"
          v-if="checked"
          style="width: 100%"
        >
        </el-date-picker>
        <el-switch
          v-model="userInfo.birthShow"
          active-text="展示"
          inactive-text="隐藏"
          :active-value="1"
          :inactive-value="0"
          v-if="checked"
        >
        </el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="手机号" v-if="userInfo.userPhone != null">
        <span v-text="userInfo.userPhone"></span>
      </el-descriptions-item>
      <el-descriptions-item label="邮箱" v-if="userInfo.userEmail != null">
        <span v-text="userInfo.userEmail"></span>
      </el-descriptions-item>
      <el-descriptions-item label="个人宣言">
        <span v-text="userInfo.userAutograph" v-if="!checked"></span>
        <el-input
          type="textarea"
          v-model="userInfo.userAutograph"
          :rows="2"
          v-if="checked"
        >
        </el-input>
      </el-descriptions-item>
      <el-descriptions-item label="目前养宠情况">
        <span v-text="userInfo.userPetCond" v-if="!checked"></span>
        <el-input
          type="textarea"
          v-model="userInfo.userPetCond"
          :rows="2"
          v-if="checked"
        >
        </el-input>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
import {
  regionDataPlus,
  CodeToText,
  TextToCode,
} from "element-china-area-data";

export default {
  inject: ["reload"],
  data() {
    return {
      userInfo: "",
      checked: false,
      JwtToken: "",
      userId: "",
      options: regionDataPlus,
      decodeCode: CodeToText,
      encodeCOde: TextToCode,
      selectedOptions: [],
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
    updated() {
      this.checked = !this.checked;
      if (!this.checked) {
        this.updateUserInfo().then(({ data }) => {
          console.log(data);
          if (data.code === 200) {
            this.$message({
              message: "用户信息更新成功",
              type: "success",
            });
          } else {
            this.$message({
              message: "用户信息更新失败, 请重试",
              type: "error",
            });
          }
          this.getUserInfo(this.userId).then(({ data }) => {
            this.userInfo = data.data;
            this.userInfo.userAvatar =
              "http://175.24.229.41:9421/" + this.userInfo.userAvatar;
            if (this.userInfo.userBirth != null) {
              this.userInfo.userAge =
                new Date(Date.now()).getFullYear() -
                new Date(this.userInfo.userBirth).getFullYear();
            }
          });
          this.reload();
        });
      }
    },
    async updateUserInfo() {
      const ans = await this.$axios({
        url: "/user-info/updateUserInfo",
        method: "post",
        data: {
          userGender: this.userInfo.userGender,
          userBirth: this.userInfo.userBirth,
          userAddress: this.userInfo.userAddress,
          userPetCond: this.userInfo.userPetCond,
          userAutograph: this.userInfo.userAutograph,
          addressShow: this.userInfo.addressShow,
          birthShow: this.userInfo.birthShow,
          genderShow: this.userInfo.genderShow,
        },
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
      });

      return ans;
    },
    handleAvatarSuccess() {
      this.$message({
        message: "头像更新成功, 点击更新按钮刷新页面",
        type: "success",
      });
    },
    handleChange(value) {
      let data = "";
      for (var i = 0; i < value.length; i++) {
        if (value[i] !== "") {
          data += this.decodeCode[value[i]];
        }
      }
      this.userInfo.userAddress = data.replace("市辖区", "");
      console.log(this.userInfo.userAddress);
    },
    // 校验头像文件
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
  },
  created() {
    this.userId = this.$route.params.userId;
    this.getUserInfo(this.userId).then(({ data }) => {
      this.userInfo = data.data;
      this.userInfo.userAvatar =
        "http://175.24.229.41:9421/" + this.userInfo.userAvatar;
      if (this.userInfo.userBirth != null) {
        this.userInfo.userAge =
          new Date(Date.now()).getFullYear() -
          new Date(this.userInfo.userBirth).getFullYear();
      }
    });
    this.JwtToken = JSON.parse(
      `{"Jwt-Token": "` + this.$store.getters.getToken + `"}`
    );
  },
  mounted() {},
};
</script>

<style lang="scss" scoped>
.userInfo {
  margin-top: 50px;

  .update {
    float: right;
    margin-right: 5px;
    background: hsl(212, 52%, 50%);
    border-color: hsl(212, 52%, 50%);
    color: #fff;
    line-height: 1;
    white-space: nowrap;
    cursor: pointer;
    -webkit-appearance: none;
    text-align: center;
    box-sizing: border-box;
    outline: 0;
    transition: 0.1s;
    font-weight: 500;
    padding: 5px;
    font-size: 14px;
    border-radius: 4px;
  }
  ::v-deep .avatar {
    img {
      width: 100% !important;
      object-fit: cover;
    }
  }
  .updateAvatar {
    width: 40 !important;
    height: 40 !important;
    object-fit: cover;
  }
}
</style>