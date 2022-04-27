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
      <div v-if="commentsPage.totalCount == 0">
        <el-empty
          :image-size="200"
          description="还没有评论, 快来抢沙发吧"
        ></el-empty>
      </div>
      <div v-for="(root, index) in commentList" :key="'root-' + index">
        <div class="show-comment">
          <div class="userAvatar">
            <el-avatar size="medium" :src="root.userAvatar"></el-avatar>
          </div>
          <div class="main-comment">
            <div class="userinfo-comment">
              <div class="info">
                <i>{{ root.userName }}</i>
                <i>
                  {{ new Date(root.gmtCreate).format("yyyy-MM-dd hh:mm:ss") }}
                </i>
              </div>
              <i
                class="el-icon-delete"
                @click="delRootComment(root.rootId)"
              ></i>
            </div>

            <div class="text-comment">
              <p>{{ root.content }}</p>
            </div>

            <div class="tag">
              <el-tag @click="likeComment(root.rootId)">
                <i class="el-icon-like-plus"></i>
                点赞 : {{ root.likeCount }}
              </el-tag>
              <el-tag>
                <i class="el-icon-chat-square"></i>
                评论 : {{ root.replyCount }}
              </el-tag>
            </div>

            <div
              class="show-comment"
              v-for="(son, index) in root.commentReplyList"
              :key="'son-' + index"
              style="width: 100%"
            >
              <div class="userAvatar">
                <el-avatar size="medium" :src="son.userAvatar"></el-avatar>
              </div>
              <div class="main-comment">
                <div class="userinfo-comment">
                  <div class="info">
                    <i>{{ root.userName }}</i>
                    <i>
                      {{
                        new Date(root.gmtCreate).format("yyyy-MM-dd hh:mm:ss")
                      }}
                    </i>
                  </div>
                  <i
                    class="el-icon-delete"
                    @click="delRootComment(root.rootId)"
                  ></i>
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
    this.init();
  },
  methods: {
    // 父评论 (评论文章)
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
            data.data.rootComment.userAvatar =
              "http://175.24.229.41:9421/" + data.data.rootComment.userAvatar;
            this.rootContent = "";
            if (this.commentsPage.list == null) {
              this.commentsPage.list = [];
            }
            this.commentsPage.list.push(data.data.rootComment);
            this.commentsPage.totalCount++;
            if (this.commentList.length < this.commentsPage.pageSize) {
              if (this.commentList == null) {
                this.commentList = [];
              }
              this.commentList.push(data.data.rootComment);
            }
          } else {
            this.$message({
              message: "评论失败",
              type: "error",
            });
          }
        });
      }
    },
    // 添加父评论
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
    // 子评论 (评论评论)
    replyComment() {},
    // 添加子评论
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
    // 获取文章的所有评论
    getComments() {
      this.getAllTitleComments().then(({ data }) => {
        this.commentsPage = data.data.page;
        if (
          this.commentsPage.list == null ||
          this.commentsPage.list.length == 0
        ) {
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
    // 获取文章的所有评论
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
    // 删除父评论
    delRootComment(rootId) {
      this.$confirm("是否删除该评论?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.deleteRootComment(rootId).then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.delRootCommentInCommentsPage(rootId);
            } else {
              this.$message({
                type: "error",
                message: "删除失败!",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 删除父评论 => 数据库数据
    async deleteRootComment(rootId) {
      const data = await this.$axios({
        url: "/comment/removeRootComment",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
          rootId: rootId,
        },
      });

      return data;
    },
    // 删除父评论 => 前端数据
    delRootCommentInCommentsPage(rootId) {
      // 在总数据中删除该评论内容
      for (let i = 0; i < this.commentsPage.list.length; i++) {
        if (this.commentsPage.list[i].rootId === rootId) {
          this.commentsPage.list.splice(i, 1);
          break;
        }
      }
      // 如果在当前页中也有该元素, 则也删除
      for (let i = 0; i < this.commentList.length; i++) {
        if (this.commentList[i].rootId === rootId) {
          this.commentList.splice(i, 1);
          break;
        }
      }
    },
    // 删除子评论
    delSonComment(rootId, replyId) {
      this.$confirm("是否删除该评论?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.deleteSonComment(replyId).then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.delSonCommentInCommentsPage(rootId, replyId);
            } else {
              this.$message({
                type: "error",
                message: "删除失败!",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 删除子评论 => 数据库数据
    async deleteSonComment(replyId) {
      const data = await this.$axios({
        url: "/comment/removeSonComment",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
          replyId: replyId,
        },
      });

      return data;
    },
    // 删除子评论 => 前端数据
    delSonCommentInCommentsPage(rootId, replyId) {
      // 在总数据中删除该评论内容
      for (let i = 0; i < this.commentsPage.list.length; i++) {
        if (this.commentsPage.list[i].rootId === rootId) {
          for (let j = 0; j < this.commentsPage.list[i].commentReplyList; j++) {
            if (
              this.commentsPage.list[i].commentReplyList[j].replyId ==
                replyId ||
              this.commentsPage.list[i].commentReplyList[j].replyCommentId ==
                replyId
            ) {
              this.commentsPage.list[i].commentReplyList.splice(i, 1);
            }
          }
          break;
        }
      }
      // 如果在当前页中也有该元素, 则也删除
      for (let i = 0; i < this.commentList.length; i++) {
        if (this.commentList[i].rootId === rootId) {
          for (let j = 0; j < this.commentList[i].commentReplyList; j++) {
            if (
              this.commentList[i].commentReplyList[j].replyId == replyId ||
              this.commentList[i].commentReplyList[j].replyCommentId == replyId
            ) {
              this.commentList[i].commentReplyList[j].splice(i, 1);
            }
          }
          break;
        }
      }
    },
    // 点赞评论
    likeComment(rootId) {
      this.$axios({
        url: "/comment/likeComment",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.$store.getters.getUserId,
          rootId: rootId,
        },
      }).then(({ data }) => {
        console.log(data);
        if (data.code == 200) {
          this.$message({
            message: "点赞成功",
            type: "success",
          });
          let rootComment = this.selectCommentsByRootId(rootId);
          rootComment.likeCount++;
        } else if (data.code == 555) {
          this.$message({
            message: data.msg,
            type: "error",
          });
        }
      });
    },
    // 通过 rootId 获取 父评论 对象
    selectCommentsByRootId(rootId) {
      for (var i = 0; i < this.commentsPage.list.length; i++) {
        if (this.commentsPage.list[i].rootId === rootId) {
          return this.commentsPage.list[i];
        }
      }

      return null;
    },
    // 获取每页有多少条父评论
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    // 换页更新每页的评论
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.commentList = this.commentsPage.list.slice(
        (val - 1) * this.commentsPage.pageSize,
        val * this.commentsPage.pageSize
      );
    },
    // 初始化评论页
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
      max-height: 46px !important;
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
          position: relative;
          width: 100%;
          i {
            &:first-child {
              font-size: 18px;
              font-weight: bolder;
            }
            margin-right: 8px;
            font-size: 12px;
          }

          .el-icon-delete {
            position: absolute;
            right: 0;
          }
          .info {
            position: absolute;
            left: 0;
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