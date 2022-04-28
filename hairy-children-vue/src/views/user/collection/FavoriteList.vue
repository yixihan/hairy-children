<template>
  <div class="meetpets">
    <div class="adoption">
      <div class="head">
        <el-tag type="primary" @click="goback">返回</el-tag>
        <el-tag> 一共收藏了 : {{ page.totalCount }} 篇贴子 </el-tag>
      </div>
      <div v-if="isEmpty">
        <el-empty :image-size="200"></el-empty>
      </div>

      <ul>
        <li v-for="(item, index) in list" :key="index">
          <a href="javascript:;">
            <img :src="item.titleAvatar" alt="正在加载中" />
            <div class="article">
              <div class="top">
                <h3 class="title">{{ item.titleName }}</h3>
                <el-tag @click="delCollectionTitle(item.id)">
                  <i class="el-icon-delete"></i>
                  取消收藏
                </el-tag>
              </div>
              <p>{{ item.titleContent }}</p>
              <div class="petstatus">
                <el-tag>
                  收藏于 :
                  {{ new Date(item.gmtCreate).format("yyyy-MM-dd hh:mm:ss") }}
                </el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="page.pageSize"
        :total="page.totalCount"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
        :hide-on-single-page="page.totalCount <= page.pageSize"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  inject: ["reload"],
  data() {
    return {
      userId: "",
      collectionId: "",
      isEmpty: false,
      page: {
        totalCount: 0,
        pageSize: 0,
        totalPage: 0,
        currPage: 0,
        list: [],
      },
      list: [],
    };
  },
  created() {
    this.init();
  },
  methods: {
    // 初始化页面
    init() {
      this.userId = this.$route.params.userId;
      this.collectionId = this.$route.params.collectionId;

      this.getAllCollectionTitles().then(({ data }) => {
        this.page = data.data.page;

        if (this.page.list == null || this.page.list.length == 0) {
          this.isEmpty = true;
          return;
        }

        for (let i = 0; i < this.page.list.length; i++) {
          this.page.list[i].titleAvatar =
            this.$store.getters.getUrl + this.page.list[i].titleAvatar;
          this.page.list[i].titleAuthorAvatar =
          this.$store.getters.getUrl + this.page.list[i].titleAuthorAvatar;
        }

        this.list = this.page.list.slice(0, this.page.pageSize);
      });
    },
    // 获取收藏夹内文章信息
    async getAllCollectionTitles() {
      const data = await this.$axios({
        url: "/collection/getAllCollections",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userCollectionId: this.collectionId,
        },
      });

      return data;
    },
    // 取消收藏文章
    delCollectionTitle(id) {
      this.$confirm("确认是否取消收藏该文章", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteCollectionTitle(id).then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: data.msg,
              });
              this.dealPageData(id);
            } else {
              this.$message({
                type: "error",
                message: "取消收藏失败",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消删除",
          });
        });
    },
    async deleteCollectionTitle(id) {
      const data = await this.$axios({
        url: "/collection/deleteCollection",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          collectionTitleId: id,
        },
      });

      return data;
    },
    // 取消收藏文章后修改页面数据实现不刷新修改数据
    dealPageData(id) {
      for (let i = 0; i < this.page.list.length; i++) {
        if (this.page.list[i].id == id) {
          this.page.list.splice(i, 1);
        }
      }

      for (let i = 0; i < this.list.length; i++) {
        if (this.list[i].id == id) {
          this.list.splice(i, 1);
        }
      }
    },
    // 获取页面条数
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    // 更新页面信息
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.list = this.page.list.slice(
        (val - 1) * this.page.pageSize,
        val * this.page.pageSize
      );
    },
    goback() {
      this.$router.push("/center/" + this.userId + "/favorite/");
    },
  },
};
</script>

<style lang="scss" scoped>
* {
  margin: 0;
  padding: 0;
}
.meetpets {
  margin: 26px auto;
  width: 100%;
  width: 100%;
  max-width: 1280px;
  // height: 600px;
  background: #fff;
  box-sizing: border-box;
  display: flex;
  .adoption,
  .findpets {
    .head {
      position: relative;
      margin-bottom: 25px;

      .el-tag {
        &:first-child {
          position: absolute;
          left: 0;
          margin: 5px;
        }

        &:last-child {
          position: absolute;
          right: 0;
          margin: 5px;
        }
      }

      .el-button {
        position: absolute !important;
        left: 0 !important;
        max-height: 40px;
        max-width: 70px;
        margin: 5px;
      }
    }

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

            .top {
              display: flex;
              position: relative;
              margin-bottom: 10px;

              h3 {
                left: 0;
              }

              .el-tag {
                position: absolute;
                size: 50%;
                right: 0;
              }
            }
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