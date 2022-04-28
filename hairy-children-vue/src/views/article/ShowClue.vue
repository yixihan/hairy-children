<template>
  <div class="page">
    <div class="clue-info">
      <el-descriptions class="margin-top" title="线索详情" :column="2" border>
        <template
          slot="extra"
          v-if="clue.userId == this.$store.getters.getUserId"
        >
          <el-button type="primary" size="small" @click="toEditClue">
            <i class="el-icon-edit"></i> 更新
          </el-button>
          <el-button type="danger" size="small" @click="delClue">
            <i class="el-icon-delete"></i> 删除
          </el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          <span v-text="clue.userName" @click="toUserCenter"></span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            贴子传送门
          </template>
          <span v-text="clue.titleName" @click="toArticle"></span>
        </el-descriptions-item>
      </el-descriptions>

      <div>
        <p>
          线索内容 :
          {{ clue.clueContent }}
        </p>
      </div>
    </div>

    <div class="clue-imgs">
      <h3>附图</h3>
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
</template>

<script>
export default {
  data() {
    return {
      clueId: "",
      clue: "",
      fileList: [],
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      // 获取 clueId
      this.clueId = this.$route.params.clueId;
      // 获取 领养申请 信息
      this.getClue().then(({ data }) => {
        this.clue = data.data.clue;

        if (this.clue.imgs != null) {
          for (let i = 0; i < this.clue.imgs.length; i++) {
            let item = {
              img: this.clue.imgs[i],
              url: "http://175.24.229.41:9421/" + this.clue.imgs[i],
            };

            this.fileList.push(item);
          }
        }
      });
    },
    async getClue() {
      const data = await this.$axios({
        url: "/clue/getClue",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          clueId: this.clueId,
        },
      });

      return data;
    },
    toEditClue() {
      this.$router.push("/clue/" + this.clueId + "/edit");
    },
    delClue() {
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteClue().then(({ data }) => {
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
    async deleteClue() {
      const data = await this.$axios({
        url: "/clue/deleteClue",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          clueId: this.clueId,
        },
      });

      return data;
    },
    toArticle() {
      this.$router.push("/article/" + this.clue.titleId);
    },
    toUserCenter() {
      this.$router.push("/center/" + this.clue.userId);
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin-top: 80px;

  .clue-info {
    width: 1240px;
    margin: 10px 50px 20px 50px;
    padding: 50px;
    border-radius: 30px;
    border: black;
  }
}
.el-descriptions-row {
  span {
    cursor: pointer;
  }
}
</style>