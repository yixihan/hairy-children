<template>
  <div class="meetpets">
    <div class="adoption">
      <div class="messageTag">
        <el-tag>未读数 : {{ unreadCount }}</el-tag>
        <el-tag>消息总数 : {{ count }}</el-tag>
        <el-tag @click="readAll" v-if="userId == myUserId">全部阅读</el-tag>
      </div>
      <div class="null" v-if="isEmpty">
        <el-empty :image-size="200"></el-empty>
      </div>
      <ul>
        <li v-for="(item, index) in messageList" :key="index">
          <a href="javascript:;">
            <img
              :src="'http://175.24.229.41:9421/' + item.sendUserAvatar"
              alt="正在加载中"
            />
            <div class="article">
              <h3 class="title">来自 {{ item.sendUserName }} 的点赞</h3>
              <p v-if="item.rootId != null">类型 : 评论点赞</p>
              <p v-else>类型 : 文章点赞</p>
              <div class="petstatus">
                <el-tag
                  >发送于 :
                  {{ new Date(item.gmtCreate).format("yyyy-MM-dd") }}</el-tag
                >
                <el-tag @click="read(index)" v-if="userId == myUserId">{{
                  item.isRead === 0 ? "未读" : "已读"
                }}</el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="message.pageSize"
        :total="message.totalCount"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
        v-if="!isEmpty"
        :hide-on-single-page="message.totalCount <= message.pageSize"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import format from "../../../utils/DateFormat.js";

export default {
  inject: ["reload"],
  format,
  data() {
    return {
      messageList: [],
      message: {
        currPage: 0,
        list: [],
        pageSize: 0,
        totalCount: 0,
        totalPage: 0,
      },
      userId: "",
      myUserId: "",
      isEmpty: false,
      count: 0,
      unreadCount: 0,
    };
  },
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.messageList = this.message.list.slice(
        (val - 1) * this.message.pageSize,
        val * this.message.pageSize
      );
    },
    async getTitleLikeMessage() {
      const data = await this.$axios({
        url: "/mailbox/getTitleLikeMessages",
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
    async getCommentLikeMessage() {
      const data = await this.$axios({
        url: "/mailbox/getCommentLikeMessages",
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
    async getTitleLikeCount() {
      const data = await this.$axios({
        url: "/mailbox/getTitleLikeMailBoxCount",
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
    async getCommentLikeCount() {
      const data = await this.$axios({
        url: "/mailbox/getCommentLikeMailBoxCount",
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

    setInfo() {
      this.userId = this.$route.params.userId;
      this.getCommentLikeMessage().then(({ data }) => {
        this.message = data.data.page;
        console.log(this.message);

        this.getTitleLikeMessage().then(({ data }) => {
          let titleLikeList = data.data.page;
          console.log(titleLikeList);
          this.message.list = this.message.list.concat(titleLikeList.list);
          this.message.totalCount += titleLikeList.totalCount;
          this.message.totalPage =
            this.message.totalCount / this.message.pageSize;
          this.message.totalPage =
            this.message.totalCount % this.message.pageSize == 0 ? 0 : 1;
          if (this.message.list.length == 0) {
            this.isEmpty = true;
            return;
          }
          console.log(this.message);
          this.messageList = this.message.list.slice(0, this.message.pageSize);
        });
      });

      this.getCommentLikeUnReadCount().then(({ data }) => {
        this.unreadCount += data.data.count;
      });

      this.getTitleLikeUnReadCount().then(({ data }) => {
        this.unreadCount += data.data.count;
      });

      this.getCommentLikeCount().then(({ data }) => {
        this.count += data.data.count;
      });

      this.getTitleLikeCount().then(({ data }) => {
        this.count += data.data.count;
      });
    },
    read(index) {
      if (this.messageList[index].rootId == null) {
        this.readTitleLikeMessage(index).then(({ data }) => {
          if (data.code == 200) {
            this.$message({
              message: "消息已成功阅读",
              type: "success",
            });
          } else {
            this.$message({
              message: "更新失败, 请重试",
              type: "error",
            });
          }
          this.reload();
        });
      } else {
        this.readCommentLikeMessage(index).then(({ data }) => {
          if (data.code == 200) {
            this.$message({
              message: "消息已成功阅读",
              type: "success",
            });
          } else {
            this.$message({
              message: "更新失败, 请重试",
              type: "error",
            });
          }
          this.reload();
        });
      }
    },
    readAll() {
      for (var i = 0; i < this.message.list.length; i++) {
        if (this.message.list[i].rootId == null) {
          this.readTitleLikeMessageById(this.message.list[i].id).then(
            ({ data }) => {
              if (data.code != 200) {
                this.$message({
                  message: "更新失败, 请重试",
                  type: "error",
                });
                this.reload();
                return;
              }
            }
          );
        } else {
          this.readCommentLikeMessageById(this.message.list[i].id).then(
            ({ data }) => {
              if (data.code != 200) {
                this.$message({
                  message: "更新失败, 请重试",
                  type: "error",
                });
                this.reload();
                return;
              }
            }
          );
        }
      }
      this.$message({
        message: "消息已成功阅读",
        type: "success",
      });
      this.reload();
    },
    async readTitleLikeMessageById(id) {
      const data = await this.$axios({
        url: "/mailbox/readTitleLikeMailBox",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          id: id,
        },
      });

      return data;
    },
    async readCommentLikeMessageById(id) {
      const data = await this.$axios({
        url: "/mailbox/readCommentLikeMailBox",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          id: id,
        },
      });

      return data;
    },
    async readTitleLikeMessage(index) {
      const data = await this.$axios({
        url: "/mailbox/readTitleLikeMailBox",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          id: this.messageList[index].id,
        },
      });

      return data;
    },
    async readCommentLikeMessage(index) {
      const data = await this.$axios({
        url: "/mailbox/readCommentLikeMailBox",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          id: this.messageList[index].id,
        },
      });

      return data;
    },
  },
  created() {
    this.setInfo();
    this.myUserId = this.$store.getters.getUser.userId;
  },
};
</script>


<style scoped lang="scss">
* {
  margin: 0;
  padding: 0;
}
.meetpets {
  margin: 26px auto;
  width: 100%;
  max-width: 1280px;
  // height: 600px;
  background: #fff;
  box-sizing: border-box;
  display: flex;
  .adoption,
  .findpets {
    border: 2px solid #7da5b3;
    background: #fff;
    width: 100%;
    h2 {
      height: 80px;
      line-height: 80px;
      color: #7da5b3;
      font-size: 26px;
    }
    ul {
      list-style: none;
      padding: 20px;
      width: 100%;
      box-sizing: border-box;
      height: 720px;
      background: #fbfbfb;
      li {
        width: 100%;
        height: 120px;
        padding: 10px;
        box-sizing: border-box;
        margin-bottom: 20px;
        background: #fff;
        a {
          display: flex;
          text-decoration: none;
          padding-right: 10px;
          img {
            width: 100px;
            height: 100px;
            margin-right: 10px;
            object-fit: cover;
          }
          // 帖子条
          .article {
            width: 100%;
            .title {
              color: #313131;
              height: 30px;
              line-height: 30px;
              font-size: 18px;
              text-align: start;
            }
            p {
              color: #acacac;
              text-align: start;
              font-size: 14px;
              // 文字超出折叠
              overflow: hidden;
              display: -webkit-box; //将对象作为弹性伸缩盒子模型显示;
              text-overflow: ellipsis; //溢出部分用省略号代替
              -webkit-line-clamp: 2; //设置文本显示两行
              -webkit-box-orient: vertical; //从上到下排列子元素;
              white-space: normal;
            }
            .petstatus {
              margin-top: 8px;
              height: 25px;
              line-height: 25px;
              position: relative;
              display: flex;
              justify-content: end;

              span {
                margin-left: 10px;
              }
              .adopted {
                color: #1fb1e6;
              }
            }
          }
          a:hover {
            .title {
              color: #7da5b3 !important;
            }
          }
        }
      }
    }
    .messageTag {
      margin-top: 8px;
      margin-right: 8px;
      height: 25px;
      line-height: 25px;
      position: relative;
      display: flex;
      justify-content: end;

      span {
        margin-left: 10px;
      }
      .adopted {
        color: #1fb1e6;
      }
    }
    .foot {
      height: 80px;
      width: 100%;
      .more {
        float: right;
        margin-right: 5px;
        background: #5cc1e9;
        border-color: #5cc1e9;
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
    }

    .pagination {
      margin: 10px;
      padding: 10px;
    }
  }
}
</style>