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
            <el-avatar size="medium" :src="title.userAvatar"></el-avatar>
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
          this.$store.getters.getUrl + this.title.userAvatar;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin-top: 70px;

  .article-header {
    min-height: 150px;
    margin: 0 50px;
    margin-bottom: 50px;
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

  .article-main {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    margin: 0 50px;
    margin-bottom: 50px;
  }
}
</style>>
