<template>
  <div class="page">
    <div class="article">
      <Article :titleId="titleId"></Article>
    </div>

    <div class="adopt-or-clue">
      <div v-if="titleType == 1">
        <h2>领养申请列表</h2>
        <el-button type="primary" @click="toCreatAdopt"> 申请领养 </el-button>
        <Adopt :titleId="titleId" :authorId="title.userId"></Adopt>
      </div>
      <div v-if="titleType == 2">
        <h2>线索列表</h2>
        <el-button type="primary" @click="toCreatClue"> 提供线索 </el-button>
        <Clue :titleId="titleId" :authorId="title.userId"></Clue>
      </div>
    </div>

    <div class="comment">
      <h2>评论区</h2>
      <Comment :titleId="titleId"></Comment>
    </div>
  </div>
</template>

<script>
import Article from "@/components/GetArticle.vue";
import Adopt from "@/components/GetAdopts.vue";
import Clue from "@/components/GetClues.vue";
import Comment from "@/components/GetComments.vue";

export default {
  components: {
    Article,
    Adopt,
    Clue,
    Comment,
  },
  data() {
    return {
      titleId: "",
      titleType: "",
      title: "",
    };
  },
  methods: {
    async getTitleType() {
      const data = await this.$axios({
        url: "/title/getTitleType",
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
    async getTitle() {
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
      this.titleId = this.$route.params.titleId;
      this.getTitleType().then(({ data }) => {
        if (data.code == 200) {
          this.titleType = data.data.titleType;
        } else {
          this.$message({
            type: "error",
            message: "没有该贴子",
          });
          this.$router.push("/");
        }
      });

      this.getTitle().then(({ data }) => {
        if (data.code == 200) {
          this.title = data.data.title;
        }
      });
    },
    toCreatAdopt() {
      if (this.verify()) {
        this.open();
        return;
      }
      this.creatAdopt().then(({ data }) => {
        if (data.code == 200) {
          this.$message({
            type: "success",
            message: data.msg,
          });
          this.$router.push("/adopt/" + data.data.adoptId + "/edit");
        } else {
          this.$message({
            type: "error",
            message: data.msg,
          });
        }
      });
    },
    async creatAdopt() {
      const data = await this.$axios({
        url: "/adopt/creatAdopt",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
          userId: this.$store.getters.getUserId,
        },
      });

      return data;
    },
    toCreatClue() {
      if (this.verify()) {
        this.open();
        return;
      }
      this.creatClue().then(({ data }) => {
        if (data.code == 200) {
          this.$message({
            type: "success",
            message: data.msg,
          });
          this.$router.push("/clue/" + data.data.clueId + "/edit");
        } else {
          this.$message({
            type: "error",
            message: data.msg,
          });
        }
      });
    },
    async creatClue() {
      const data = await this.$axios({
        url: "/clue/creatClue",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
          userId: this.$store.getters.getUserId,
        },
      });

      return data;
    },
    verify() {
      return (
        this.$store.getters.getUser.userIdentityCard == null ||
        this.$store.getters.getUser.userIdentityCard == ""
      );
    },
    open() {
      this.$confirm("您还没实名认证, 请先实名认证?", "提示", {
        confirmButtonText: "实名认证",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.$router.push(
            "/center/" +
              this.$store.getters.getUser.userId +
              "/setting/authentication"
          );
        })
        .catch(() => {});
    },
  },
  created() {
    this.init();
  },
};
</script>


<style lang="scss" scoped>
.page {
  margin: 80px auto auto auto;
  width: 1240px;

  .article {
    margin: 0 auto;

    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  }

  .adopt-or-clue {
    margin-top: 50px !important;
    margin: 0 auto;
  }

  .comment {
    margin: 0 auto;
  }
}
</style>