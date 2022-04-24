<template>
  <div class="center">
    <div class="main">
      <div class="nav">
        <el-menu
          default-active="1"
          class="el-menu-vertical-demo"
          @select="select"
          :unique-opened="true"
        >
          <el-menu-item index="账户信息">
            <i class="el-icon-user"></i>
            <span slot="title">账户信息</span>
          </el-menu-item>
          <el-submenu index="账户设置">
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>账户设置</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="实名认证">实名认证</el-menu-item>
              <el-menu-item index="邮箱设置">邮箱设置</el-menu-item>
              <el-menu-item index="电话设置">电话设置</el-menu-item>
              <el-menu-item index="更改密码">更改密码</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="历史信息">
            <template slot="title">
              <i class="el-icon-chat-line-round"></i>
              <span>历史信息</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="过往发布">过往发布</el-menu-item>
              <el-menu-item index="过往评论">过往评论</el-menu-item>
              <el-menu-item index="提交的线索">提交的线索</el-menu-item>
              <el-menu-item index="申请的领养">申请的领养</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="个人信箱">
            <template slot="title">
              <i class="el-icon-chat-line-square"></i>
              <span>个人信箱</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="收到的领养申请">收到的领养申请</el-menu-item>
              <el-menu-item index="收到的线索">收到的线索</el-menu-item>
              <el-menu-item index="收到的评论">收到的评论</el-menu-item>
              <el-menu-item index="收到的回复">收到的回复</el-menu-item>
              <el-menu-item index="收到的赞">收到的赞</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </div>
      <div class="view">
        <div class="header">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: `/center/` + userId }"
              >首页</el-breadcrumb-item
            >
            <el-breadcrumb-item v-for="(item, index) in navList" :key="index">{{
              item
            }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: "",
      navList: ["账户信息"],
    };
  },
  methods: {
    select(key, keyPath) {
      this.navList = [];
      for (var i = 0; i < keyPath.length; i++) {
        this.navList.push(keyPath[i]);
      }
      this.$router.push({ name: this.navList[this.navList.length - 1] });
    },
  },
  created() {
    this.userId = this.$route.params.userId;
  },
};
</script>

<style lang="scss" scoped>
.center {
  margin: 0 auto;
  margin-top: 100px;
  padding-top: 10px;
  width: 100%;
  height: 100%;

  .header {
    margin-left: 0 auto;
    height: 20px;
  }
  .main {
    display: flex;
    padding-right: 10px;
    box-sizing: border-box;
    justify-content: space-between;
    .nav {
      margin-top: 120px;
      flex: 2;
    }
    .view {
      margin-left: 40px;
      flex: 8;
    }
  }
}
</style>