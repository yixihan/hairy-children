<template>
  <div id="app">
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
export default {
  name: "App",

  provide() {
    return {
      reload: this.reload,
    };
  },

  data() {
    return {
      isRouterAlive: true,
    };
  },
  created() {
    this.init();
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    async getUserInfo() {
      const data = await this.axios({
        url: "/user/getUserInfo",
        method: "post",
        data: {
          userId: this.$store.getters.getUserId,
        },
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
      });

      return data;
    },
    init() {
      if (this.$store.getters.getUserId != null && this.$store.getters.getUserId != "") {
        this.getUserInfo().then(({ data }) => {
          const userInfo = data.data;
          this.$store.commit("SET_USERINFO", userInfo);
        });
      }
    },
  },
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

::v-deep .el-button--primary {
  position: sticky !important;
  width: 100px !important;
}
</style>
