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
      <el-tag>
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
    };
  },
  methods: {
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
    init() {
      this.getArticle().then(({ data }) => {
        console.log(data);
        this.title = data.data.title;
        this.title.userAvatar =
          "http://175.24.229.41:9421/" + this.title.userAvatar;
      });
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
}
</style>