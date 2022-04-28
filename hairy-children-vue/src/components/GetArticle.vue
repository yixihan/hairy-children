<template>
  <div class="page">
    <div class="article-header">
      <div class="article-title">
        <h1 class="title-name">{{ title.titleName }}</h1>
        <div class="title-tag">
          <el-tag>
            {{ title.isFinish == 1 ? "已完成" : "未完成" }}
          </el-tag>
          <el-tag>
            {{ title.titleType == 1 ? "领养贴" : "寻宠贴" }}
          </el-tag>
        </div>
      </div>
      <div class="article-info">
        <div class="author-info">
          <div class="author-avatar">
            <el-avatar size="meduim" :src="title.userAvatar"></el-avatar>
          </div>
          <div class="author-name">
            <i>{{ title.userName }}</i>
          </div>
          <div class="author-city">
            <i>{{ title.userAddress }}</i>
          </div>
        </div>

        <div class="time-info">
          <i>
            发布于 :
            {{ new Date(title.gmtCreate).format("yyyy-MM-dd hh:mm:ss") }}
          </i>
          <i>
            最后修改时间 :
            {{ new Date(title.gmtModified).format("yyyy-MM-dd hh:mm:ss") }}
          </i>
        </div>
      </div>
      <div class="button" v-if="title.userId == this.$store.getters.getUserId">
        <el-tag class="update" type="primary" @click="editArticle"
          >修改贴子</el-tag
        >
        <el-tag class="delete" type="primary" @click="deleteArticle"
          >删除贴子</el-tag
        >
        <el-tag class="finish" type="primary" @click="finishArticle"
          >修改贴子状态</el-tag
        >
      </div>
    </div>

    <mavon-editor
      class="markdown"
      :value="title.titleContent"
      :subfield="false"
      defaultOpen="preview"
      :toolbarsFlag="false"
      :editable="false"
      :scrollStyle="true"
      style="min-height: 20px"
    ></mavon-editor>

    <div class="dialong">
      <el-dialog
        title="添加到收藏夹"
        :visible.sync="dialogTableVisible"
        center
        width="30%"
      >
        <div class="show">
          <div
            class="collection-main"
            v-for="(item, index) in userCollections.list"
            :key="index"
          >
            <input
              type="checkbox"
              v-model="item.checked"
              @click="check(item)"
            />
            <i class="name">{{ item.collectionName }}</i>
            <span class="count"> {{ item.collectionCount }} / 1000 </span>
          </div>
          <div class="add">
            <el-input
              v-model="collectionName"
              placeholder="新建收藏夹"
            ></el-input>
            <el-button type="primary" @click="createCollection">新建</el-button>
          </div>
          <div class="submit">
            <el-button type="primary" @click="sumbit">确定</el-button>
          </div>
        </div>
      </el-dialog>
    </div>

    <div class="tag">
      <el-tag @click="likeTitle">
        <i v-if="!isLike" class="el-icon-like"></i>
        <i v-else class="el-icon-like-plus"></i>
        点赞 : {{ title.likeCount }}
      </el-tag>
      <el-tag>
        <i class="el-icon-chat-square"></i>
        评论 : {{ title.commentCount }}
      </el-tag>
      <el-tag @click="collectionTitle">
        <i v-if="!isCollection" class="el-icon-star-off"></i>
        <i v-else class="el-icon-star-on"></i>
        收藏 : {{ title.collectionCount }}
      </el-tag>
    </div>
  </div>
</template>

<script>
import format from "@/utils/DateFormat.js";

export default {
  format,
  props: ["titleId"],
  data() {
    return {
      title: "",
      isCollection: false,
      isLike: false,
      userCollections: {
        totalCount: 2,
        pageSize: 5,
        totalPage: 1,
        currPage: 0,
        list: [],
      },
      dialogTableVisible: false,
      checkList: [],
      collectionName: "",
    };
  },
  methods: {
    init() {
      this.getArticle().then(({ data }) => {
        this.title = data.data.title;
        this.title.userAvatar =
          "http://175.24.229.41:9421/" + this.title.userAvatar;
      });

      this.getUserCollections().then(({ data }) => {
        this.userCollections = data.data.page;

        for (let i = 0; i < this.userCollections.list.length; i++) {
          this.userCollections.list[i].checked = false;
        }
      });
    },
    async getArticle() {
      const data = await this.$axios({
        url: "/title/getTitle",
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
    likeTitle() {
      this.$axios({
        url: "/title/like",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.$store.getters.getUserId,
          titleId: this.titleId,
        },
      }).then(({ data }) => {
        console.log(data);
        if (data.code == 200) {
          this.$message({
            message: "点赞成功",
            type: "success",
          });
          this.title.likeCount++;
          this.isLike = true;
        } else if (data.code == 555) {
          this.$message({
            message: data.msg,
            type: "error",
          });
        }
      });
    },
    editArticle() {
      this.$router.push("/article/" + this.titleId + "/edit");
    },
    deleteArticle() {
      this.$confirm("确认是否删除该贴子?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.delete().then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.$router.push("/");
            } else {
              this.$message({
                type: "error",
                message: "删除失败, 请重试!",
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
    async delete() {
      const data = await this.$axios({
        url: "/title/deleteTitle",
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
    async getUserCollections() {
      const data = await this.$axios({
        url: "/collection/getAllFavorites",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.$store.getters.getUserId,
        },
      });

      return data;
    },
    collectionTitle() {
      this.dialogTableVisible = true;
    },
    check(item) {
      item.checked = !item.checked;
      if (item.checked) {
        for (let i = 0; i < this.checkList.length; i++) {
          if (this.checkList[i] == item.collectionId) {
            return;
          }
        }
        this.checkList.push(item.collectionId);
      } else {
        for (let i = 0; i < this.checkList.length; i++) {
          if (this.checkList[i] == item.collectionId) {
            this.checkList.splice(i, 1);
          }
        }
      }
    },
    sumbit() {
      for (let i = 0; i < this.checkList.length; i++) {
        this.addTitleCollection(this.checkList[i]).then(({ data }) => {
          if (data.code == 200) {
            this.$message({
              type: "success",
              message: data.msg,
            });
            this.title.collectionCount++;
          } else {
            this.$message({
              type: "error",
              message: data.msg,
            });
          }
        });
      }
      this.getUserCollections().then(({ data }) => {
        this.userCollections = data.data.page;

        for (let i = 0; i < this.userCollections.list.length; i++) {
          this.userCollections.list[i].checked = false;
        }
      });
      this.checkList = [];

      this.dialogTableVisible = false;
    },
    async addTitleCollection(collectionId) {
      const data = await this.$axios({
        url: "/collection/addCollection",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          collectionId: collectionId,
          titleId: this.titleId,
        },
      });

      return data;
    },
    createCollection() {
      if (!this.inputValidator(this.collectionName)) {
        this.$message({
          type: "error",
          message: "请正确输入",
        });
      } else {
        this.addCollection(this.collectionName).then(({ data }) => {
          if (data.code == 200) {
            this.$message({
              type: "success",
              message: data.msg,
            });
            this.collectionName = "";
            let item = data.data.data;
            console.log(item);
            item.checked = false;
            console.log(item);
            this.userCollections.list.push(item);
          } else {
            this.$message({
              type: "error",
              message: "收藏夹创建失败",
            });
          }
        });
      }
    },
    // 校验弹窗输入
    inputValidator(val) {
      return val != null && val != "";
    },
    // 创建收藏夹 => 数据库数据
    async addCollection(collectionName) {
      const data = await this.$axios({
        url: "/collection/createFavorites",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          collectionName: collectionName,
        },
      });

      return data;
    },
    finishArticle() {
      this.$confirm("确定已完成贴子任务", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.finish().then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: "更新成功!",
              });
              this.title.isFinish = 1;
            } else {
              this.$message({
                type: "error",
                message: "更新失败!",
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
    async finish() {
      const data = await this.$axios({
        url: "/title/finish",
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
  },
  created() {
    this.init();
  },
};
</script>


<style lang="scss" scoped>
.page {
  margin: 70px;
  position: relative;

  .article-header {
    min-height: 150px;
    .article-title {
      position: relative;
      margin-bottom: 20px;
      height: 32px;
      padding: 30px 0 0 0;
      background: transparent;
      font-family: "Helvetica Neue", Helvetica, "PingFang SC",
        "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;

      .title-name {
        position: absolute;
        left: 0;
        margin: 0 0 0 25px;
        font-size: 24px;
        font-weight: bold;
        color: #3f3f3f;
      }

      .title-tag {
        position: absolute;
        right: 6px;

        .el-tag {
          margin-right: 5px;
        }
      }
    }

    .article-info {
      height: 50px;
      width: 100%;
      position: relative;
      margin-bottom: 10px;

      .author-info {
        position: absolute;
        left: 5px;
        display: flex;

        .author-avatar {
          margin-right: 16px;
          margin-left: 25px;
        }

        .author-name {
          margin-top: 11px;
          margin-right: 16px;
          font-size: 18px;
        }

        .author-city {
          margin-top: 15px;
          margin-right: 16px;
          font-size: 12px;
        }
      }

      .time-info {
        position: absolute;
        right: 5px;
        font-size: 12px;

        i {
          margin-right: 6px;
        }
      }
    }
    .button {
      margin-bottom: 15px;
      display: flex;
      justify-content: flex-end;
      .el-tag {
        margin-right: 5px;
      }
    }
  }

  .tag {
    position: absolute;
    right: 0;
    height: 50px;
    margin-bottom: 40px;
    .el-tag {
      width: 90px;
      padding-left: 20px;
      margin-right: 6px;
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

  .dialong {
    position: relative;

    .el-dialog__body {
      padding-bottom: 50px !important;

      .show {
        .collection-main {
          margin-bottom: 10px;
          .el-checkbox {
            position: relative;

            margin-right: 10px;
            margin-bottom: 16px;
          }

          .name {
            margin-left: 10px;
            margin-right: 10px;
            display: inline-block;
            overflow: hidden;
          }

          .count {
            float: right;

            right: 16px;
            font-size: 12px;
          }
        }
      }

      .add {
        width: 100%;
        height: 50px;
        position: relative;

        .el-input {
          position: absolute;
          left: 5px;
        }

        .el-button {
          position: absolute !important;
          right: -5px !important;
        }
      }

      .submit {
        height: 40px;
        widows: 100% !important;
        display: flex;

        .el-button--primary {
          flex: auto;
          margin: 0 auto;
          max-width: 200px;
        }
      }
    }
  }
}

::v-deep .el-dialog--center .el-dialog__body {
  padding-bottom: 50px !important;
  height: 300px !important;
  overflow: auto !important;
}
</style>