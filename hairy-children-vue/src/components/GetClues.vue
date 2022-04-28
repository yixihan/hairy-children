<template>
  <div class="meetpets">
    <div class="adoption">
      <div class="null" v-if="isEmpty">
        <el-empty :image-size="200"></el-empty>
      </div>
      <ul>
        <li v-for="(item, index) in clueList" :key="index">
          <a href="javascript:;">
            <el-image
              style="width: 100px; height: 100px; margin-right: 16px"
              :src="item.imgs[0]"
              :preview-src-list="item.imgs"
            >
            </el-image>
            <div class="article">
              <div>
                <h3 class="title">
                  提交用户 :
                  <el-avatar size="small" :src="item.userAvatar"> </el-avatar>
                  <span v-text="item.userName"></span>
                </h3>
                <p>线索内容 : {{ item.clueContent }}</p>
              </div>
              <div class="petstatus">
                <el-tag>
                  发布于 :
                  {{ new Date(item.gmtCreate).format("yyyy-MM-dd hh:mm:ss") }}
                </el-tag>
                <el-tag class="clueed">
                  <span
                    v-text="item.isSuccess == 1 ? '已被采用' : '暂未被采用'"
                    @click="examine(item.clueId)"
                  ></span>
                </el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="clue.pageSize"
        :total="clue.totalCount"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
        :hide-on-single-page="clue.totalCount <= clue.pageSize"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import format from "@/utils/DateFormat.js";

export default {
  name: "GetClues",
  format,
  props: ["titleId", "authorId"],
  data() {
    return {
      clueList: [],
      clue: {
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
      this.clueList = this.clue.list.slice(
        (val - 1) * this.clue.pageSize,
        val * this.clue.pageSize
      );
    },
    async getUserClue() {
      const data = await this.$axios({
        url: "/clue/getAllTitleClues",
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
    setInfo() {
      this.userId = this.$route.params.userId;
      this.getUserClue().then(({ data }) => {
        console.log(data);
        this.clue = data.data.page;
        if (this.clue.list == null || this.clue.totalCount == 0) {
          this.isEmpty = true;
          return;
        }
        for (var i = 0; i < this.clue.list.length; i++) {
          if (this.clue.list[i].imgs == null) {
            this.clue.list[i].imgs = [];
            this.clue.list[i].imgs.push("/clue/default/default.png");
          }
          for (let j = 0; j < this.clue.list[i].imgs.length; j++) {
            this.clue.list[i].imgs[j] =
            this.$store.getters.getUrl + this.clue.list[i].imgs[j];
          }

          this.clue.list[i].userAvatar =
            this.$store.getters.getUrl + this.clue.list[i].userAvatar;
        }

        this.clueList = this.clue.list.slice(0, this.clue.pageSize);
      });
    },
    examine(clueId) {
      if (this.authorId != this.$store.getters.getUserId) {
        return;
      }
      this.$confirm("是否采用该用户提交的线索?", "提示", {
        confirmButtonText: "同意",
        cancelButtonText: "忽略",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.success(clueId, true).then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                type: "success",
                message: "已同意该领养申请!",
              });
              let clue = this.selectClueByclueId(clueId);
              clue.isSuccess = 1;
            } else {
              this.$message({
                type: "error",
                message: "出现错误, 请刷新页面重试",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    async success(clueId, isSuccess) {
      const data = await this.$axios({
        url: "/clue/success",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          clueId: clueId,
          isSuccess: isSuccess,
        },
      });

      return data;
    },
    selectClueByclueId(clueId) {
      let clue;
      for (var i = 0; i < this.clue.list.length; i++) {
        if (this.clue.list[i].clueId == clueId) {
          clue = this.clue.list[i];
        }
      }

      return clue;
    },
  },

  created() {
    this.setInfo();
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