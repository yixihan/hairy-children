<template>
  <div class="meetpets">
    <div class="adoption">
      <h2 @click="toAdoptSearch">领养</h2>
      <ul>
        <li v-for="(item, index) in adoptArticles.list" :key="index">
          <a href="javascript:;" @click="toArticle(item.titleId)">
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
                <el-tag class="adopted">{{
                  item.isFinish == 1 ? "已领养" : "未领养"
                }}</el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>
      <div class="foot">
        <button class="more" @click="toAdoptSearch">mroe</button>
      </div>
    </div>
    <div class="findpets">
      <h2 @click="toFindPetSearch">寻宠</h2>
      <ul>
        <li v-for="(item, index) in findPetsArticles.list" :key="index">
          <a href="javascript:;" @click="toArticle(item.titleId)">
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
                <el-tag class="adopted">{{
                  item.isFinish == 1 ? "已找回" : "未找回"
                }}</el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>

      <div class="foot">
        <button class="more" @click="toFindPetSearch">mroe</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MeetPets",
  data() {
    return {
      adoptArticles: {
        currPage: 0,
        list: [],
        pageSize: 0,
        totalCount: 0,
        totalPage: 0,
      },
      findPetsArticles: {
        currPage: 0,
        list: [],
        pageSize: 0,
        totalCount: 0,
        totalPage: 0,
      },
    };
  },
  methods: {
    async getArticles(titleType) {
      const data = await this.$axios({
        url: "/title/getAllTitles",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleType: titleType,
          titleName: "",
          timeLimit: "4",
          city: "",
          isFinish: "3",
          time: "false",
          like: "false",
          reply: "false",
          collection: "false",
        },
      });

      return data;
    },
    setInfo() {
      // 获取领养贴
      this.getArticles(1).then(({ data }) => {
        this.adoptArticles = data.data.page;
        this.adoptArticles.list = this.adoptArticles.list.slice(0, 5);
      });

      // 获取寻宠贴
      this.getArticles(2).then(({ data }) => {
        this.findPetsArticles = data.data.page;
        this.findPetsArticles.list = this.findPetsArticles.list.slice(0, 5);
      });
    },
    toFindPetSearch() {
      this.$router.push("/search/findPet");
    },
    toAdoptSearch() {
      this.$router.push("/search/adopt");
    },
    toArticle (titleId) {
      this.$router.push('/article/' + titleId)
    }
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