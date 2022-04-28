<template>
  <div class="meetpets">
    <div class="adoption">
      <div class="null" v-if="isEmpty">
        <el-empty :image-size="200"></el-empty>
      </div>
      <ul>
        <li v-for="(item, index) in adoptList" :key="index">
          <a href="javascript:;" @click="toAdopt(item.adoptId)">
            <img
              :src="item.imgs[0]"
              alt="正在加载中"
            />
            <div class="article">
              <h3 class="title">文章 : {{ item.titleName }}</h3>
              <p>申请理由 : {{ item.adoptReason }}</p>
              <div class="petstatus">
                <el-tag>{{ item.adoptUserAddress }}</el-tag>
                <el-tag>
                  申请于 :
                  {{ new Date(item.gmtCreate).format("yyyy-MM-dd") }}
                </el-tag>
                <el-tag class="adopted">
                  {{
                    item.isSuccess == 1
                      ? "已通过"
                      : item.isSuccess == 0
                      ? "暂未审核"
                      : "已拒绝"
                  }}
                </el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="adopt.pageSize"
        :total="adopt.totalCount"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
        v-if="!isEmpty"
        :hide-on-single-page="adopt.totalCount <= adopt.pageSize"
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
      adoptList: [],
      adopt: {
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
      this.adoptList = this.adopt.list.slice(
        (val - 1) * this.adopt.pageSize,
        val * this.adopt.pageSize
      );
    },
    async getUserAdopt() {
      const data = await this.$axios({
        url: "/adopt/getAllUserAdopts",
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
      this.getUserAdopt().then(({ data }) => {
        console.log(data);
        this.adopt = data.data.page;
        if (this.adopt.list == null || this.adopt.list.length == 0) {
          this.isEmpty = true;
          return;
        }
        for (var i = 0; i < this.adopt.list.length; i++) {
          if (this.adopt.list[i].imgs == null) {
            this.adopt.list[i].imgs = [];
            this.adopt.list[i].imgs.push("/adopt/default/default.png");
          }
          this.adopt.list[i].imgs[0] = this.$store.getters.getUrl + this.adopt.list[i].imgs[0]
        }

        this.adoptList = this.adopt.list.slice(0, this.adopt.pageSize);
      });
    },
    toAdopt(adoptId) {
      this.$router.push("/adopt/" + adoptId);
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