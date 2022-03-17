<template>
  <div>
    <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      :router="router"
    >
      <el-menu-item index="main">主页</el-menu-item>
      <el-menu-item id="release" index="release"> 投稿 </el-menu-item>
      <el-menu-item id="message" index="message"> 消息中心 </el-menu-item>
      <el-menu-item id="login" v-show="!isLogin" index="login">
        <span>
          <el-link type="primary">
            <el-avatar> 登录 </el-avatar>
          </el-link>
        </span>
      </el-menu-item>
      <el-menu-item id="me" v-show="isLogin" index="center">
        <span>
          <el-link type="danger">
            <el-avatar :src="avatar"></el-avatar>
          </el-link>
        </span>
      </el-menu-item>
      <el-menu-item index="test">
        <span>点击</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: "1",
      router: true,
      isLogin: false,
      avatar: "",
      userName: "",
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    getUserInfo() {
      let userInfo = this.$store.getters.getUser;
      console.log("userInfo : ", userInfo)
      if (userInfo != null) {
        this.userName = this.$store.getters.getUser.userName;
        this.avatar = "http://" + this.$store.getters.getUser.userAvatar;
        console.log(this.userName);
        console.log(this.avatar);
        this.isLogin = true;
      } else {
        this.userName = "";
        this.avatar = "";
        this.isLogin = false;
      }
    },
  },
  mounted() {
    this.getUserInfo();
  },
};
</script>

<style>
#me,
#message,
#index ,
#login {
  float: right;
}
#release {
  float: right;
}
</style>