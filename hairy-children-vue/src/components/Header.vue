<template>
  <div class="nav">
    <ul>
      <li class="logo" @click="toMain"></li>
      <li class="col" v-if="!isLogin" @click="toLogin">登录</li>
      <li class="col" v-if="!isLogin" @click="toRegister">注册</li>
      <span class="col text" v-if="isLogin">{{ userName }}, 欢迎回来</span>
      <li class="col" v-if="isLogin" @click="toCenter">
        <i class="el-icon-user"></i>个人中心
      </li>

      <li class="col" v-if="isLogin" @click="toWriteArticle">
        <i class="el-icon-plus"></i>我要发布
      </li>
      <li class="col" v-if="isLogin" @click="cancellation">注销</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {};
  },
  methods: {
    // 跳转到注册界面
    toMain() {
      this.$router.push("/");
    },
    // 跳转到注册界面
    toRegister() {
      this.$router.push("/register");
    },
    // 跳转到登录界面
    toLogin() {
      this.$router.push("/login");
    },
    // 注销
    cancellation() {
      this.$store.commit("REMOVE_INFO");
      this.$router.push("/");
    },
    toCenter() {
      this.$router.push({
        path: "/center/" + this.$store.getters.getUser.userId,
      });
    },
    toWriteArticle() {
      console.log(this.$store.getters.getUser.userIdentityCard);
      if (
        this.$store.getters.getUser.userIdentityCard == null ||
        this.$store.getters.getUser.userIdentityCard == ""
      ) {
        this.open();
      } else {
        this.$router.push("/article/create");
      }
    },
    open() {
      this.$confirm("您还没实名认证, 请先实名认证?", "提示", {
        confirmButtonText: "实名认证",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.$router.push(
            "/center/" +
              this.$store.getters.getUser.userId +
              "/setting/authentication"
          );
        })
        .catch(() => {});
    },
  },
  computed: {
    isLogin() {
      return (
        this.$store.getters.getToken != null &&
        this.$store.getters.getToken != ""
      );
    },
    userName() {
      if (
        this.$store.getters.getUser != null &&
        this.$store.getters.getUser != ""
      ) {
        return this.$store.getters.getUser.userName == null
          ? ""
          : this.$store.getters.getUser.userName;
      } else {
        return "";
      }
    },
  },
};
</script>

<style scoped lang="scss">
.nav {
  z-index: 9;
  position: fixed;
  top: 0;
  height: 70px;
  // padding: 10px 0;
  width: 100%;
  display: flex;
  justify-content: center;
  // color: #ffffff;
  background: #fff;
  ul {
    height: 70px;
    width: 100%;
    max-width: 1280px;
    display: flex;
    position: fixed;
    top: 0;
    margin: 0 auto;
    padding: 0;
    justify-content: center;
    align-items: center;
    line-height: 20px;
    list-style: none;
    li:not(:first-child) {
      flex-grow: 1;
      height: 100%;
      // width: 200px;
      min-width: 150px;
      line-height: 70px;
      user-select: none;
      cursor: pointer;
      transition: background 0.1s;
      &:hover {
        background: #76a2b1;
        color: #fff;
      }
    }
    .logo {
      margin: 10px 0;
      box-sizing: border-box;
      flex-grow: 16;
      height: 50px;
      min-width: 270px;
      background: url("~@/assets/img/logo/6a8134a155fe420ea789179ecc7ad64a.png")
        no-repeat;
    }
    .text {
      margin-right: 5px;
    }
  }
}

button {
  position: static !important;
}
</style>
