<template>
  <div class="page">
    <div class="desc">
      <h1>{{ title.titleName }}</h1>
      <p>城市 : {{ title.userAddress }}</p>
      <el-avatar size="small" :src="title.userAvatar"></el-avatar>
      <p>作者 : {{ title.userName }}</p>
      <p>
        发布于 : {{ new Date(title.gmtCreate).format("yyyy-MM-dd hh:mm:ss") }}
      </p>
      <p>
        最后修改时间 :
        {{ new Date(title.gmtModified).format("yyyy-MM-dd hh:mm:ss") }}
      </p>
      <p>贴子类型 : {{ title.titleType == 1 ? "领养贴" : "寻宠贴" }}</p>
      <div v-if="title.userId == this.$store.getters.getUserId">
        <el-button type="primary" @click="editArticle">修改贴子</el-button>
        <el-button type="primary" @click="deleteArticle">删除贴子</el-button>
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
          <div v-for="(item, index) in userCollections.list" :key="index">
            <el-checkbox
              :v-model="item.checked"
              @click="check(index)"
            ></el-checkbox>
            <i class="name">{{ item.collectionName }}</i>
            <span class="count"> {{ item.collectionCount }} / 1000 </span>
          </div>
          <div class="add">
            <el-input
              v-model="collectionName"
              placeholder="新建收藏夹"
            ></el-input>
            <el-button type="primary" @click="createCollection">确定</el-button>
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
    check(index) {
      console.log(11);
      this.userCollections.list[index].checked =
        !this.userCollections.list[index].checked;
      if (this.userCollections.list[index].checked) {
        this.checkList.push(this.userCollections.list[index].collectionId);
      }
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
            this.collectionName = ""
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
  },
  created() {
    this.init();
  },
};
</script>


<style lang="scss" scoped>
.page {
  margin: 70px;

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

  .dialong {
    position: relative;

    .el-dialog__body {
      padding-bottom: 50px !important;
      max-height: 300px !important;
      .show {
        .el-checkbox {
          margin-right: 10px;
          margin-bottom: 16px;
        }

        .name {
          font: italic small-caps bold 16px/24px Georgia, serif;
          margin-left: 10px;
          margin-right: 10px;
        }

        .count {
          position: absolute;
          right: 16px;
          font-size: 12px;
        }
      }

      .add {
        width: 100%;
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
    }
  }
}

::v-deep .el-dialog--center .el-dialog__body {
  padding-bottom: 50px !important;
}
</style>