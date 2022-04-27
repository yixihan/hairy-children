<template>
  <div class="page">
    <div class="article-info">
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
    </div>

    <div class="article-main">
      <EditArticle :titleId="titleId"></EditArticle>
    </div>
  </div>
</template>

<script>
import EditArticle from "../../components/EditArticle.vue";

export default {
  components: {
    EditArticle,
  },
  data() {
    return {
      titleId: "",
      title: "",
    };
  },
  created() {
    this.init();
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
      this.titleId = this.$route.params.titleId;
      this.getArticle().then(({ data }) => {
        console.log(data);
        this.title = data.data.title;
        this.title.userAvatar =
          "http://175.24.229.41:9421/" + this.title.userAvatar;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin-top: 70px;

  .article-info {
    border-radius: 30px;
    margin: 0 50px;
    padding: 50px;
  }

  .article-main {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    margin: 0 50px;
    margin-bottom: 50px;
  }
}
</style>>
