<template>
  <div class="page">
    <div class="write-comment">
      <el-avatar size="medium" :src="userAvatar"></el-avatar>
      <el-input
        type="textarea"
        :rows="2"
        placeholder="期待你的神评"
        v-model="rootContent"
      >
      </el-input>
      <el-button type="primary" @click="replyTitie">评论</el-button>
    </div>

    <div class="show-comments">
      <div v-for="(root, index) in commentList" :key="'root-' + index">
        <div class="show-comment">
          <div class="userAvatar">
            <el-avatar size="medium" :src="root.userAvatar"></el-avatar>
          </div>
          <div class="main-comment">
            <div class="userinfo-comment">
              <i>{{ root.userName }}</i>
              <i>{{
                new Date(root.gmtCreate).format("yyyy-MM-dd hh:mm:ss")
              }}</i>
            </div>

            <div class="text-comment">
              <p>{{ root.content }}</p>
            </div>

            <div class="tag">
              <el-tag>
                <i class="el-icon-like-plus"></i>
                点赞 : {{ root.likeCount }}
              </el-tag>
              <el-tag>
                <i class="el-icon-chat-square"></i>
                评论 : {{ root.replyCount }}
              </el-tag>
            </div>

            <div
              v-for="(son, index) in root.commentReplyList"
              :key="'son-' + index"
            >
              <div class="show-comment">
                <div class="userAvatar">
                  <el-avatar size="medium" :src="son.userAvatar"></el-avatar>
                </div>
                <div class="main-comment">
                  <div class="userinfo-comment">
                    <i>{{ son.userName }}</i>
                    <i>{{
                      new Date(son.gmtCreate).format("yyyy-MM-dd hh:mm:ss")
                    }}</i>
                  </div>

                  <div class="text-comment">
                    <p>{{ son.content }}</p>
                  </div>

                  <div class="tag">
                    <el-tag>
                      <i class="el-icon-chat-square"></i>
                      评论
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="commentsPage.pageSize"
      :total="commentsPage.totalCount"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      class="pagination"
      :hide-on-single-page="commentsPage.totalCount <= commentsPage.pageSize"
    >
    </el-pagination>
  </div>
</template>

<script>
import format from "@/utils/DateFormat.js";
export default {
  format,
  inject: ["reload"],
  props: ["titleId"],
  data() {
    return {
      rootContent: "",
      sonContent: "",
      userAvatar: "",
      isCommentEmpty: false,
      commentsPage: {
        currPage: 0,
        list: [],
        pageSize: 0,
        totalCount: 0,
        totalPage: 0,
      },
      commentList: [],
    };
  },
  created() {
    console.log(this.titleId);
    this.init();
  },
  methods: {
    replyTitie() {
      if (this.rootContent == null || this.rootContent == "") {
        this.$message({
          message: "请正确输入",
          type: "error",
        });
      } else {
        this.addRootComment().then(({ data }) => {
          if (data.code == 200) {
            this.$message({
              message: "评论成功",
              type: "success",
            });
          } else {
            this.$message({
              message: "评论失败",
              type: "error",
            });
          }
          this.reload();
        });
      }
    },
    async addRootComment() {
      const data = await this.$axios({
        url: "/comment/addRootComment",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          answerId: this.titleId,
          content: this.rootContent,
        },
      });

      return data;
    },
    replyComment() {},
    async addSonComment(rootId, replyCommentId) {
      
      const data = await this.$axios({
        url: "/comment/addSonComment/" + this.titleId,
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          rootId: rootId,
          replyCommentId: replyCommentId,
          content: this.sonContent,
        },
      });

      return data;
    },
    getComments() {
      this.getAllTitleComments().then(({ data }) => {
        this.commentsPage = data.data.page;
        if (this.commentsPage.list.length == 0) {
          this.isCommentEmpty = true;
          return;
        }
        for (var i = 0; i < this.commentsPage.list.length; i++) {
          this.commentsPage.list[i].userAvatar =
            "http://175.24.229.41:9421/" + this.commentsPage.list[i].userAvatar;

          for (
            var j = 0;
            j < this.commentsPage.list[i].commentReplyList.length;
            j++
          ) {
            this.commentsPage.list[i].commentReplyList[j].userAvatar =
              "http://175.24.229.41:9421/" +
              this.commentsPage.list[i].commentReplyList[j].userAvatar;
          }
        }

        this.commentList = this.commentsPage.list.slice(
          0,
          this.commentsPage.pageSize
        );
      });
    },
    async getAllTitleComments() {
      const data = await this.$axios({
        url: "/comment/getAllTitleComment",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
        },
      });

      return data;
    },
    likeComment(rootId) {
      this.$axios({
        url: "/comment/getAllTitleComment",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
          rootId: rootId
        },
      }).then(({data})=>{
        console.log(data);
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.commentList = this.commentsPage.list.slice(
        (val - 1) * this.commentsPage.pageSize,
        val * this.commentsPage.pageSize
      );
    },
    init() {
      this.userAvatar =
        "http://175.24.229.41:9421/" + this.$store.getters.getUser.userAvatar;
      this.getComments();
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin: 70px 0 0 70px;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);

  .write-comment {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    margin: 10px;
    padding: 10px;
    display: flex;

    .el-button {
      margin-left: 10px !important;
    }

    .el-avatar {
      margin-right: 10px !important;
      margin-top: 10px !important;
    }
  }

  .show-comments {
    width: 100%;
    margin-top: 20px;
    & > div > .show-comment {
      border-bottom: 1px solid #ccc;
    }
    .show-comment {
      margin-bottom: 12px;
      display: flex;

      .userAvatar {
        // margin: 10px;
        width: 36px;
        height: 36px;
      }
      .main-comment {
        width: 100%;
        display: flex;
        padding: 0 0 10px 15px;
        flex-direction: column;
        align-content: center;
        div {
          align-self: flex-start;
        }
        .userinfo-comment {
          i {
            &:first-child {
              font-size: 18px;
              font-weight: bolder;
            }
            margin-right: 8px;
            font-size: 12px;
          }
        }
        .text-comment {
          padding: 12px 0;
        }
        .tag {
          cursor: pointer;
          margin-bottom: 16px;
          span {
            margin-right: 16px;
          }
        }
      }
    }
  }

  .tag {
    .el-tag {
      width: 90px;
      padding-left: 20px;
      position: relative;
      i {
        position: absolute;
        left: 8px;
        top: 8px;
      }
    }
    .el-icon-like {
      width: 15px;
      height: 15px;
      background: url("~@/assets/icon/like.png") no-repeat;
      background-size: 12px;
    }
    .el-icon-like-plus {
      width: 15px;
      height: 15px;
      background: url("~@/assets/icon/like-plus.png") no-repeat;
      background-size: 12px;
    }
  }
}
</style>