<template>
  <div class="page">
    <div class="article">
      <Article :titleId="titleId"></Article>
    </div>

    <div class="adopt-or-clue">
      <h2 v-if="titleType == 1">领养申请列表</h2>
      <Adopt v-if="titleType == 1" :titleId="titleId"></Adopt>
      <h2 v-if="titleType == 2">线索列表</h2>
      <Clue v-if="titleType == 2" :titleId="titleId"></Clue>
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
    init() {
      this.getTitleType().then(({ data }) => {
        this.titleType = data.data.titleType;
      });
    },
  },
  created() {
    this.titleId = this.$route.params.titleId;
    this.init();
  },
};
</script>


<style lang="scss" scoped>
.page {
  margin: 50px auto auto auto;
  width: 1240px;

  .article {
    margin: 0 auto;
    padding: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  }

  .adopt-or-clue {
    margin: 0 auto;
  }

  .comment {
    margin: 0 auto;
  }
}
</style>