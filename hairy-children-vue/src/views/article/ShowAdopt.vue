<template>
  <div class="page">
    <div class="adopt-info">
      <el-descriptions
        class="margin-top"
        title="领养申请详情"
        :column="2"
        border
      >
        <template
          slot="extra"
          v-if="adopt.userId == this.$store.getters.getUserId"
        >
          <el-button type="primary" size="small" @click="toEditAdopt">
            <i class="el-icon-edit"></i> 更新
          </el-button>
          <el-button type="danger" size="small" @click="delAdopt">
            <i class="el-icon-delete"></i> 删除
          </el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          <span v-text="adopt.userName" @click="toUserCenter"></span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          {{ adopt.adoptUserPhone }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            贴子传送门
          </template>
          <span v-text="adopt.titleName" @click="toArticle"></span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            申请人年龄
          </template>
          {{
            new Date(Date.now()).getFullYear() -
            new Date(this.$store.getters.getUser.userBirth).getFullYear()
          }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            所在城市
          </template>
          {{ adopt.adoptUserAddress }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            接动物方式
          </template>
          {{ adopt.adoptWay }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            是否接受定期回访
          </template>
          {{ adopt.isReturnVisit == 1 ? "是" : "否" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            是否接受定期反馈领养信息
          </template>
          {{ adopt.isFeedback == 1 ? "是" : "否" }}
        </el-descriptions-item>
      </el-descriptions>

      <div class="adopt-main">
        <div class="adopt-concept">
          <i>
            养宠理念 :
            {{ adopt.adoptConcept }}
          </i>
        </div>

        <div class="adopt-reason">
          <i>
            申请理由 :
            {{ adopt.adoptReason }}
          </i>
        </div>
      </div>

      <div class="adopt-imgs">
        <div class="img-title">
          <i>附图</i>
        </div>
        <div>
          <el-image
            v-for="(item, index) in fileList"
            :key="index"
            :src="item.url"
            lazy
          ></el-image>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      adoptId: "",
      adopt: "",
      fileList: [],
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      // 获取 adoptId
      this.adoptId = this.$route.params.adoptId;
      // 获取 领养申请 信息
      this.getAdopt().then(({ data }) => {
        this.adopt = data.data.adopt;

        if (this.adopt.imgs != null) {
          for (let i = 0; i < this.adopt.imgs.length; i++) {
            let item = {
              img: this.adopt.imgs[i],
              url: this.$store.getters.getUrl + this.adopt.imgs[i],
            };

            this.fileList.push(item);
          }
        }
      });
    },
    async getAdopt() {
      const data = await this.$axios({
        url: "/adopt/getAdopt",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          adoptId: this.adoptId,
        },
      });

      return data;
    },
    toEditAdopt() {
      this.$router.push("/adopt/" + this.adoptId + "/edit");
    },
    delAdopt() {
      this.$confirm("此操作将永久删除该领养申请, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteAdopt().then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                message: data.msg,
                type: "success",
              });
              this.toArticle();
            } else {
              this.$message({
                message: data.msg,
                type: "error",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    async deleteAdopt() {
      const data = await this.$axios({
        url: "/adopt/deleteAdopt",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          adoptId: this.adoptId,
        },
      });

      return data;
    },
    toArticle() {
      this.$router.push("/article/" + this.adopt.titleId);
    },
    toUserCenter() {
      this.$router.push("/center/" + this.adopt.userId);
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin-top: 80px;

  .adopt-info {
    width: 1240px;
    margin: 10px 50px 20px 50px;
    padding: 50px;
    border-radius: 30px;
    border: black;

    .adopt-main {
      display: flex;
      flex-direction: column;

      .adopt-concept {
        display: inline-flex;
        margin-top: 16px;
        margin-bottom: 16px;
      }
      .adopt-reason {
        display: inline-flex;
      }
    }

    .adopt-imgs {
      margin-top: 50px;
      width: 100%;
      .img-title {
        display: flex;
        justify-content: flex-start;
        font-size: 16px;
        margin-block-start: 1em;
        margin-block-end: 1em;
        margin-inline-start: 0px;
        margin-inline-end: 0px;
        font-weight: bold;
      }
    }
  }
}
</style>