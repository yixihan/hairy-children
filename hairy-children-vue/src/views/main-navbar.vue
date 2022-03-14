<template>
  <div>
    <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
    >
      <el-menu-item index="1">主页</el-menu-item>
      <el-menu-item id="release" index="5">
        <i class="el-icon-circle-plus">投稿</i>
      </el-menu-item>
      <el-menu-item id="collection" index="4">收藏</el-menu-item>
      <el-menu-item id="message" index="3"> 消息中心 </el-menu-item>
      <el-menu-item id="me" index="2">
        <span v-show="! isLogin">
          <el-link type="primary" @click="login">
            <el-avatar> 登录 </el-avatar>
          </el-link>
        </span>
        <span v-show="isLogin">
          <el-link type="danger" @click="toCenter">
            <el-avatar :src="avatar"></el-avatar>
          </el-link>
        </span>
      </el-menu-item>
      
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
      isLogin: false,
      avatar: '../../public/login.png',
      userName: ''
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    login () { //跳转登录页面进行登录
      this.$router.push("/login")
    },
    toCenter () {
      this.$router.push("/login")
    },
    main () {
        this.$router.push("/")
    }
    
  },
};
</script>

<style>
#me, #message, #index, #collection {
  float: right;
}
#release {
  float: right;
  width: -50px;
}

</style>