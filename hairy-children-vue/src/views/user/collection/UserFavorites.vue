<template>
  <div class="meetpets">
    <div class="adoption">
      <div v-if="isEmpty">
        <el-empty :image-size="200"></el-empty>
      </div>
      <div class="add">
        <el-tag @click="createCollection">
          <i class="el-icon-plus"></i>
          添加收藏夹
        </el-tag>
      </div>

      <ul>
        <li v-for="(item, index) in list" :key="index">
          <a href="javascript:;">
            <div class="article">
              <div class="main">
                <h2 class="title" @click="toCollection(item.collectionId)">
                  {{ item.collectionName }}
                </h2>
                <div class="tag">
                  <el-tag
                    @click="
                      updateCollection(item.collectionName, item.collectionId)
                    "
                  >
                    <i class="el-icon-top"></i>
                    更新收藏夹
                  </el-tag>
                  <el-tag @click="delCollection(item.collectionId)">
                    <i class="el-icon-delete"></i>
                    删除收藏夹
                  </el-tag>
                </div>
              </div>
              <div class="petstatus">
                <el-tag>
                  创建于 :
                  {{ new Date(item.gmtCreate).format("yyyy-MM-dd hh:mm:ss") }}
                </el-tag>
                <el-tag> 收藏文章 : {{ item.collectionCount }} </el-tag>
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
      this.getUserCollections().then(({ data }) => {
        this.page = data.data.page;

        if (this.page.list.length == 0) {
          this.isEmpty = true;
          return;
        }

        this.list = this.page.list.slice(0, this.page.pageSize);
      });
    },
    // 获取用户所有的收藏夹
    async getUserCollections() {
      const data = await this.$axios({
        url: "/collection/getAllFavorites",
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
    createCollection() {
      this.$prompt("请输入收藏夹名", "提示", {
        confirmButtonText: "创建",
        cancelButtonText: "取消",
        inputValidator: this.inputValidator,
        inputErrorMessage: "请输入收藏夹名",
      })
        .then(({ value }) => {
          this.addCollection(value).then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: data.msg,
              });
              this.reload();
            } else {
              this.$message({
                type: "error",
                message: "收藏夹创建失败",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入",
          });
        });
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
    // 更新收藏夹
    updateCollection(collectionName, collectionId) {
      this.$prompt("请输入收藏夹名", "提示", {
        confirmButtonText: "更新",
        cancelButtonText: "取消",
        inputValidator: this.inputValidator,
        inputValue: collectionName,
        inputErrorMessage: "请输入收藏夹名",
      })
        .then(({ value }) => {
          this.editCollection(value, collectionId).then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: data.msg,
              });
              this.reload();
            } else {
              this.$message({
                type: "error",
                message: "收藏夹修改失败",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消更新",
          });
        });
    },
    // 更新收藏夹 => 数据库数据
    async editCollection(collectionName, collectionId) {
      const data = await this.$axios({
        url: "/collection/updateFavorites",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          collectionName: collectionName,
          collectionId: collectionId,
        },
      });

      return data;
    },
    // 删除收藏夹
    delCollection(collectionId) {
      this.$confirm("此操作将永久删除该收藏夹, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteCollection(collectionId).then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: data.msg,
              });
              this.reload();
            } else {
              this.$message({
                type: "error",
                message: "收藏夹删除失败",
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
    // 删除收藏夹 => 数据库数据
    async deleteCollection(userCollectionId) {
      const data = await this.$axios({
        url: "/collection/deleteFavorites",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userCollectionId: userCollectionId,
        },
      });

      return data;
    },
    // 跳转到收藏夹列表页面
    toCollection(collectionId) {
      this.$router.push("/center/" + this.userId + "/favorite/" + collectionId);
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
    .add {
      display: flex;
      width: 100%;
      right: 0;
      margin: 5px;
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

            .main {
              display: flex;
              position: relative;
              .title {
                position: absolute;
                left: 0;
                color: #313131;
                height: 30px;
                line-height: 30px;
                font-size: 18px;
                text-align: start;
              }

              .tag {
                position: absolute;
                right: 0;

                .el-tag {
                  margin-right: 5px;
                }
              }
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
              margin-top: 68px;
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