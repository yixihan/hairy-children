<template>
  <div class="meetpets">
    <div class="adoption">
      <div class="null" v-if="isEmpty">
        <h3>用户暂无贴子</h3>
      </div>
      <ul>
        <li v-for="(item, index) in articleList" :key="index">
          <a href="javascript:;">
            <img :src="'http://175.24.229.41:9421/' + item.titleImg" alt="" />
            <div class="article">
              <h3 class="title">{{ item.titleName }}</h3>
              <p>
                {{ item.titleContent }}
              </p>
              <div class="petstatus">
                <el-tag>点赞 : {{ item.likeCount }}</el-tag>
                <el-tag>评论 : {{ item.commentCount }}</el-tag>
                <el-tag>收藏 : {{ item.collectionCount }}</el-tag>
                <el-tag>发布城市 : {{ item.userAddress }}</el-tag>
                <el-tag
                  >发布于 :
                  {{ new Date(item.gmtCreate).format("yyyy-MM-dd") }}</el-tag
                >
                <el-tag class="adopted">{{
                  item.titleType == 1
                    ? item.isFinish == 1
                      ? "已领养"
                      : "未领养"
                    : item.isFinish == 1
                    ? "已找到"
                    : "未找到"
                }}</el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="article.pageSize"
        :total="article.totalCount"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
        v-if="!isEmpty"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import format from "../../../utils/DateFormat.js";

export default {
  format,
  data() {
    return {
      articleList: [],
      article: {
        currPage: 0,
        list: [],
        pageSize: 0,
        totalCount: 0,
        totalPage: 0,
      },
      userId: "",
      isEmpty: false,
    };
  },
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.articleList = this.article.list.slice(
        (val - 1) * this.article.pageSize,
        val * this.article.pageSize
      );
    },
    async getUserArticle() {
      const data = await this.$axios({
        url: "/title/getAllUserTitles",
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
      this.getUserArticle().then(({ data }) => {
        this.article = data.data.page;
        if (this.article.list.length == 0) {
          this.isEmpty = true;
          return;
        }

        this.articleList = this.article.list.slice(0, this.article.pageSize);
      });
    },
  },
  created() {
    this.setInfo();
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

            .time {
              position: relative;
              ::v-deep .el-tag {
                position: absolute;
                right: 65px;
                height: 25px;
                line-height: 25px;
              }
              .adopted {
                color: #1fb1e6;
              }
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
