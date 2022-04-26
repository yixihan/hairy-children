<template>
  <div class="page">
    <div class="search">
      <div class="title">领养</div>
      <el-form
        :model="data"
        :rules="rules"
        ref="data"
        label-position="left"
        label-width="100px"
        size="mini"
      >
        <el-form-item label="贴子标题" prop="titleName">
          <el-input v-model="data.titleName"></el-input>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-cascader
            size="mini"
            :options="options"
            v-model="data.selectedOptions"
            @change="handleChange"
          >
          </el-cascader>
        </el-form-item>
        <el-form-item label="按发布时间" prop="timeLimit">
          <el-radio-group v-model="data.timeLimit">
            <el-radio :label="1">今日</el-radio>
            <el-radio :label="2">本周</el-radio>
            <el-radio :label="3">半年内</el-radio>
            <el-radio :label="4">不限</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="领养状态" prop="isFinish">
          <el-radio-group v-model="data.isFinish">
            <el-radio :label="0">已被领养</el-radio>
            <el-radio :label="1">待领养</el-radio>
            <el-radio :label="3">不限</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="isFinish">
          <el-select v-model="data.sort" placeholder="默认排序">
            <el-option
              v-for="item in sortOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('data')">搜索</el-button>
          <el-button @click="resetForm('data')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="meetpets" v-if="isSearch">
      <div class="adoption">
        <div class="null" v-if="isEmpty">
          <el-empty :image-size="200"></el-empty>
        </div>
        <ul>
          <li v-for="(item, index) in articleList" :key="index">
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
          :page-size="articles.pageSize"
          :total="articles.totalCount"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          class="pagination"
          v-if="!isEmpty"
          :hide-on-single-page="articles.totalCount <= articles.pageSize"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import {
  regionDataPlus,
  CodeToText,
  TextToCode,
} from "element-china-area-data";
export default {
  inject: ["reload"],
  data() {
    return {
      data: {
        titleName: "",
        titleType: 1,
        timeLimit: 4,
        city: "",
        isFinish: 3,
        sort: 0,
        time: false,
        like: false,
        reply: false,
        collection: false,
        selectedOptions: [""],
      },
      options: regionDataPlus,
      decodeCode: CodeToText,
      encodeCOde: TextToCode,
      sortOptions: [
        {
          value: 0,
          label: "默认排序",
        },
        {
          value: 1,
          label: "发布时间",
        },
        {
          value: 2,
          label: "点赞数",
        },
        {
          value: 3,
          label: "回复数",
        },
        {
          value: 4,
          label: "收藏数",
        },
      ],
      isSearch: false,
      isEmpty: false,
      rules: {},
      articles: {
        currPage: 0,
        list: [],
        pageSize: 0,
        totalCount: 0,
        totalPage: 0,
      },
      articleList: [],
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.dealData();
          this.clearArticles();
          console.log(this.data);
          this.getArticles().then(({ data }) => {
            console.log(data);
            this.articles = data.data.page;
            if (this.articles.list.length == 0) {
              this.isEmpty = true;
            }
            this.articleList = this.articles.list.slice(
              0,
              this.articles.pageSize
            );
            this.resetData();
            this.isSearch = true;
          });
        } else {
          this.$message({
            message: "出现未知错误, 请刷新页面重试",
            type: "error",
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.clearArticles();
      this.resetData();
      this.isEmpty = false;
      this.isSearch = false;
    },
    handleChange(value) {
      let data = "";
      for (var i = 0; i < value.length; i++) {
        if (value[i] !== "") {
          data += this.decodeCode[value[i]];
        }
      }
      this.data.city = data.replace("市辖区", "");
      console.log(this.data.city);
    },
    dealData() {
      this.isEmpty = false;
      this.isSearch = false;
      
      // 处理 sort
      if (this.data.sort == 1) {
        this.data.time = true;
      } else if (this.data.sort == 2) {
        this.data.like = true;
      } else if (this.data.sort == 3) {
        this.data.reply = true;
      } else if (this.data.sort == 4) {
        this.data.collection = true;
      }
    },
    resetData() {
      this.data.titleName = "";
      this.data.timeLimit = 4;
      this.data.isFinish = 3;
      this.data.city = "";
      this.data.sort = 0;
      this.data.time = false;
      this.data.like = false;
      this.data.reply = false;
      this.data.collection = false;
    },
    clearArticles() {
      this.articles = {
        currPage: 0,
        list: [],
        pageSize: 0,
        totalCount: 0,
        totalPage: 0,
      };
      this.articleList = [];
    },
    async getArticles() {
      const data = await this.$axios({
        url: "/title/getAllTitles",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleType: this.data.titleType,
          titleName: this.data.titleName,
          timeLimit: this.data.timeLimit,
          city: this.data.city,
          isFinish: this.data.isFinish,
          time: this.data.time,
          like: this.data.like,
          reply: this.data.reply,
          collection: this.data.collection,
        },
      });
      return data;
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.articleList = this.articles.list.slice(
        (val - 1) * this.articles.pageSize,
        val * this.articles.pageSize
      );
    },
    toArticle (titleId) {
      this.$router.push('/article/' + titleId)
    }
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin-top: 70px;
  width: 100%;

  .search {
    width: 1276px;
    margin: 0 auto;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: flex;
    ::v-deep .el-form-item__content {
      display: flex !important;
      justify-content: center;
      margin-bottom: 0 !important;
      .el-button {
        margin-right: 10px;
        margin-bottom: 16px !important;
      }
      .el-button--primary {
        position: inherit;
      }
    }

    .title {
      font: italic small-caps bold 30px/45px Georgia, serif;
      letter-spacing: 10px;
      margin: 20px 20px 90px 20px;
      padding: 20px;
      writing-mode: vertical-lr;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    }
    .el-form {
      width: 90% !important;
      margin: 0 auto;
    }
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
}
.el-input {
  width: 50%;
  max-width: 500px;
}
</style>>
