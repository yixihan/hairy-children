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
          <el-menu-item index="收藏夹">
            <i class="el-icon-star-on"></i>
            <span slot="title">收藏夹</span>
          </el-menu-item>
          <el-submenu
            index="账户设置"
            v-if="userId == this.$store.getters.getUser.userId"
          >
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
              <el-badge :value="unreadCount" class="item">
                <i class="el-icon-chat-line-square"></i>
                <span>个人信箱</span>
              </el-badge>
            </template>
            <el-menu-item-group>
              <el-badge :value="adoptUnreadCount" class="item">
                <el-menu-item index="收到的领养申请">
                  收到的领养申请
                </el-menu-item>
              </el-badge>
              <el-badge :value="clueUnreadCount" class="item">
                <el-menu-item index="收到的线索">收到的线索</el-menu-item>
              </el-badge>

              <el-badge :value="commentUnreadCount" class="item">
                <el-menu-item index="收到的评论">收到的评论</el-menu-item>
              </el-badge>
              <el-badge :value="replyUnreadCount" class="item">
                <el-menu-item index="收到的回复">收到的回复</el-menu-item>
              </el-badge>
              <el-badge :value="likeUnreadCount" class="item">
                <el-menu-item index="收到的赞">收到的赞</el-menu-item>
              </el-badge>
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
      clueUnreadCount: 0,
      adoptUnreadCount: 0,
      commentUnreadCount: 0,
      replyUnreadCount: 0,
      likeUnreadCount: 0,
      unreadCount: 0,
    };
  },
  created() {
    this.init();
  },
  methods: {
    // 初始化页面信息
    init() {
      this.userId = this.$route.params.userId;
      this.getUnreadCount();
    },
    // 导航栏路由跳转
    select(key, keyPath) {
      this.navList = [];
      for (var i = 0; i < keyPath.length; i++) {
        this.navList.push(keyPath[i]);
      }
      this.$router.push({ name: this.navList[this.navList.length - 1] });
    },
    // 获取未读的线索数
    async getClueUnReadCount() {
      const data = await this.$axios({
        url: "/mailbox/getUnReadClueMailBoxCount",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.userId,
        },
      });
      return data;
    },
    // 获取未读的领养申请数
    async getAdoptUnReadCount() {
      const data = await this.$axios({
        url: "/mailbox/getUnReadAdoptMailBoxCount",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.userId,
        },
      });
      return data;
    },
    // 获取未读的评论数
    async getCommentUnReadCount() {
      const data = await this.$axios({
        url: "/mailbox/getUnReadCommentMailBoxCount",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.userId,
        },
      });
      return data;
    },
    // 获取未读的回复数
    async getReplyUnReadCount() {
      const data = await this.$axios({
        url: "/mailbox/getUnReadReplyMailBoxCount",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.userId,
        },
      });
      return data;
    },
    // 获取未读的文章点赞数
    async getTitleLikeUnReadCount() {
      const data = await this.$axios({
        url: "/mailbox/getUnReadTitleLikeMailBoxCount",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.userId,
        },
      });
      return data;
    },
    // 获取未读的评论点赞数
    async getCommentLikeUnReadCount() {
      const data = await this.$axios({
        url: "/mailbox/getUnReadCommentLikeMailBoxCount",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.userId,
        },
      });
      return data;
    },
    // 获取信箱里所有未读数
    getUnreadCount() {
      this.getClueUnReadCount().then(({ data }) => {
        this.clueUnreadCount = data.data.count;
        this.unreadCount += data.data.count;
      });

      this.getAdoptUnReadCount().then(({ data }) => {
        this.adoptUnreadCount = data.data.count;
        this.unreadCount += data.data.count;
      });

      this.getCommentUnReadCount().then(({ data }) => {
        this.commentUnreadCount = data.data.count;
        this.unreadCount += data.data.count;
      });

      this.getReplyUnReadCount().then(({ data }) => {
        this.replyUnreadCount = data.data.count;
        this.unreadCount += data.data.count;
      });

      this.getCommentLikeUnReadCount().then(({ data }) => {
        this.likeUnreadCount += data.data.count;
        this.unreadCount += data.data.count;
      });

      this.getTitleLikeUnReadCount().then(({ data }) => {
        this.likeUnreadCount += data.data.count;
        this.unreadCount += data.data.count;
      });
    },
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