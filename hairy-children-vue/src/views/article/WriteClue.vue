<template>
  <div class="page">
    <div class="form">
      <el-form
        :model="form"
        :rules="rules"
        ref="form"
        label-width="200px"
        label-position="right"
        size="mini"
      >
        <el-form-item label="线索内容" prop="clueContent">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入线索内容"
            v-model="form.clueContent"
          >
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">确定</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
        <el-form-item label="图片上传">
          <el-upload
            class="upload-demo"
            :action="this.$store.getters.getUrlclue + '/updateImg/' + clueId"
            :headers="JwtToken"
            name="imgs"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-success="handleSuccess"
            :file-list="fileList"
            list-type="picture"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件, 且不超过2MB
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="toArticle">返回贴子</el-button>
          <el-button type="success" @click="toClue">查看线索内容</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      clueId: "",
      clue: "",
      form: {
        clueContent: "",
      },
      fileList: [],
      rules: {
        clueContent: [
          { required: true, message: "请输入申请理由", trigger: "blur" },
        ],
        clueUserPhone: [
          { min: 11, max: 11, message: "请正确输入", trigger: "blur" },
          { required: true, message: "请输入联系电话", trigger: "blur" },
        ],
        clueConcept: [
          { required: true, message: "请输入养宠理念", trigger: "blur" },
        ],
        clueWay: [
          { required: true, message: "请输入接动物方式", trigger: "blur" },
        ],
        clueUserAge: [
          { required: true, message: "请输入年龄", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      // 设置 JwtToken
      this.JwtToken = JSON.parse(
        `{"Jwt-Token": "` + this.$store.getters.getToken + `"}`
      );
      // 获取 clueId
      this.clueId = this.$route.params.clueId;
      // 获取 领养申请 信息
      this.getClue().then(({ data }) => {
        this.clue = data.data.clue;

        if (this.clue.imgs != null) {
          for (let i = 0; i < this.clue.imgs.length; i++) {
            let item = {
              img: this.clue.imgs[i],
              url: this.$store.getters.getUrl + this.clue.imgs[i],
            };

            this.fileList.push(item);
          }
        }
        // clue => form
        this.form.clueContent = this.clue.clueContent;
        this.form.clueUserAddress = this.clue.clueUserAddress;
        this.form.clueUserPhone = this.clue.clueUserPhone;
        this.form.clueConcept = this.clue.clueConcept;
        this.form.clueWay = this.clue.clueWay;
        this.form.clueUserAge = this.clue.clueUserAge;
        this.form.isReturnVisit = this.clue.isReturnVisit;
        this.form.isFeedback = this.clue.isFeedback;
      });

      // 获取用户信息
      if (this.form.clueUserPhone == null || this.form.clueUserPhone == "") {
        this.form.clueUserPhone = this.$store.getters.getUser.userPhone;
      }
      if (
        (this.form.clueUserAge == null || this.form.clueUserAge == "") &&
        (this.$store.getters.getUser.userBirth != null ||
          this.$store.getters.getUser.userBirth != "")
      ) {
        this.form.clueUserAge =
          new Date(Date.now()).getFullYear() -
          new Date(this.$store.getters.getUser.userBirth).getFullYear();
      }
      if (this.form.isReturnVisit == null || this.form.isReturnVisit == "") {
        this.form.isReturnVisit = 1;
      }
      if (this.form.isFeedback == null || this.form.isFeedback == "") {
        this.form.isFeedback = 1;
      }
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
    async updateClue() {
      const data = await this.$axios({
        url: "/clue/updateClue",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.$store.getters.getUserId,
          clueId: this.clueId,
          clueContent: this.form.clueContent,
          clueUserAddress: this.form.clueUserAddress,
          clueUserPhone: this.form.clueUserPhone,
          clueConcept: this.form.clueConcept,
          clueWay: this.form.clueWay,
          clueUserAge: this.form.clueUserAge,
          isReturnVisit: this.form.isReturnVisit,
          isFeedback: this.form.isFeedback,
          isSuccess: this.clue.isSuccess == -1 ? 0 : this.clue.isSuccess,
        },
      });

      return data;
    },
    // 城市区域码 => 城市
    handleChange(value) {
      let data = "";
      for (var i = 0; i < value.length; i++) {
        if (value[i] !== "") {
          data += this.decodeCode[value[i]];
        }
      }
      this.form.clueUserAddress = data.replace("市辖区", "");
      console.log(this.form.clueUserAddress);
    },
    handleRemove(file) {
      let url = file.img;
      console.log(url);
      this.$axios({
        url: "/clue/deleteImg",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          clueId: this.clueId,
          imgUrl: url,
        },
      }).then(({ data }) => {
        if (data.code == 200) {
          this.$message({
            message: data.msg,
            type: "success",
          });
        } else {
          this.$message({
            message: data.msg,
            type: "error",
          });
        }
      });
    },
    handlePreview(file) {
      console.log(file);
    },
    handleSuccess(res) {
      let data = res;
      if (data.code == 200) {
        this.$message({
          message: data.msg,
          type: "success",
        });
        let item = {
          img: data.data.imgList[0],
          url: this.$store.getters.getUrl + data.data.imgList[0],
        };

        this.fileList.push(item);
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.form);
          this.updateClue().then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                message: "更新成功",
                type: "success",
              });
              this.clue.clueContent = this.form.clueContent;
              this.clue.clueUserAddress = this.form.clueUserAddress;
              this.clue.clueUserPhone = this.form.userPhone;
              this.clue.clueConcept = this.form.clueConcept;
              this.clue.clueWay = this.form.clueWay;
              this.clue.clueUserAge = this.form.clueUserAge;
              this.clue.isReturnVisit = this.form.isReturnVisit;
              this.clue.isFeedback = this.form.isFeedback;
              this.clue.isSuccess =
                this.clue.isSuccess == -1 ? 0 : this.clue.isSuccess;
              this.$message({
                message: "更新成功",
                type: "success",
              });
            }
          });
        } else {
          this.$message({
            message: "请正确输入",
            type: "error",
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    toArticle() {
      this.$router.push("/article/" + this.clue.titleId);
    },
    toClue() {
      this.$router.push("/clue/" + this.clueId);
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin-top: 100px;

  .form {
    width: 1240px;
    margin: 0 auto;
    .el-form {
      width: 75% !important;
    }
  }
}

.el-form-item__content {
  margin-left: 150px !important;
}
</style>>
