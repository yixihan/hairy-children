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
        <el-form-item label="申请理由" prop="adoptReason">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入申请理由"
            v-model="form.adoptReason"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="所在城市" prop="selectedOptions">
          <el-cascader
            size="mini"
            :options="options"
            v-model="form.selectedOptions"
            @change="handleChange"
          >
          </el-cascader>
        </el-form-item>
        <el-form-item label="申请人年龄" prop="adoptUserAge">
          <el-input-number
            v-model="form.adoptUserAge"
            controls-position="right"
            :min="1"
            :max="100"
          >
          </el-input-number>
        </el-form-item>
        <el-form-item label="电话" prop="adoptUserPhone">
          <el-input v-model="form.adoptUserPhone" placeholder="请输入联系电话">
          </el-input>
        </el-form-item>
        <el-form-item label="养宠理念" prop="adoptConcept">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入养宠理念"
            v-model="form.adoptConcept"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="接动物方式" prop="adoptWay">
          <el-input v-model="form.adoptWay" placeholder="请输入接动物方式">
          </el-input>
        </el-form-item>
        <el-form-item label="接受定期回访" required>
          <el-radio-group v-model="form.isReturnVisit">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="接受定期反馈领养信息" required>
          <el-radio-group v-model="form.isFeedback">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">确定</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
        <el-form-item label="图片上传">
          <el-upload
            class="upload-demo"
            :action="this.$store.getters.getUrl + '/adopt/updateImg/' + adoptId"
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
          <el-button type="success" @click="toAdopt">查看领养申请</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {
  regionDataPlus,
  CodeToText,
  TextToCode,
} from "element-china-area-data";
import { isMobile } from "../../utils/validate";

export default {
  isMobile,
  data() {
    return {
      adoptId: "",
      adopt: "",
      options: regionDataPlus,
      decodeCode: CodeToText,
      encodeCOde: TextToCode,
      form: {
        adoptReason: "",
        adoptUserAddress: "",
        adoptUserPhone: "",
        adoptConcept: "",
        adoptWay: "",
        adoptUserAge: "",
        isReturnVisit: 1,
        isFeedback: 1,
        selectedOptions: [""],
      },
      fileList: [],
      rules: {
        adoptReason: [
          { required: true, message: "请输入申请理由", trigger: "blur" },
        ],
        adoptUserPhone: [
          { min: 11, max: 11, message: "请正确输入", trigger: "blur" },
          { required: true, message: "请输入联系电话", trigger: "blur" },
        ],
        adoptConcept: [
          { required: true, message: "请输入养宠理念", trigger: "blur" },
        ],
        adoptWay: [
          { required: true, message: "请输入接动物方式", trigger: "blur" },
        ],
        adoptUserAge: [
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
        // adopt => form
        this.form.adoptReason = this.adopt.adoptReason;
        this.form.adoptUserAddress = this.adopt.adoptUserAddress;
        this.form.adoptUserPhone = this.adopt.adoptUserPhone;
        this.form.adoptConcept = this.adopt.adoptConcept;
        this.form.adoptWay = this.adopt.adoptWay;
        this.form.adoptUserAge = this.adopt.adoptUserAge;
        this.form.isReturnVisit = this.adopt.isReturnVisit;
        this.form.isFeedback = this.adopt.isFeedback;
        
        if (this.form.adoptUserPhone == "") {
          console.log(111);
          console.log(this.$store.getters.getUser.userPhone);
          this.form.adoptUserPhone = this.$store.getters.getUser.userPhone;
        }
        if (
          (this.form.adoptUserAge == 0 || this.form.adoptUserAge == null) &&
          (this.$store.getters.getUser.userBirth != null ||
            this.$store.getters.getUser.userBirth != "")
        ) {
          this.form.adoptUserAge =
            new Date(Date.now()).getFullYear() -
            new Date(this.$store.getters.getUser.userBirth).getFullYear();
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
    async updateAdopt() {
      const data = await this.$axios({
        url: "/adopt/updateAdopt",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.$store.getters.getUserId,
          adoptId: this.adoptId,
          adoptReason: this.form.adoptReason,
          adoptUserAddress: this.form.adoptUserAddress,
          adoptUserPhone: this.form.adoptUserPhone,
          adoptConcept: this.form.adoptConcept,
          adoptWay: this.form.adoptWay,
          adoptUserAge: this.form.adoptUserAge,
          isReturnVisit: this.form.isReturnVisit,
          isFeedback: this.form.isFeedback,
          isSuccess: this.adopt.isSuccess == -1 ? 0 : this.adopt.isSuccess,
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
      this.form.adoptUserAddress = data.replace("市辖区", "");
      console.log(this.form.adoptUserAddress);
    },
    handleRemove(file) {
      let url = file.img;
      console.log(url);
      this.$axios({
        url: "/adopt/deleteImg",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          adoptId: this.adoptId,
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
        if (valid && isMobile(this.form.adoptUserPhone)) {
          console.log(this.form);
          this.updateAdopt().then(({ data }) => {
            if (data.code == 200) {
              this.$message({
                message: "更新成功",
                type: "success",
              });
              this.adopt.adoptReason = this.form.adoptReason;
              this.adopt.adoptUserAddress = this.form.adoptUserAddress;
              this.adopt.adoptUserPhone = this.form.userPhone;
              this.adopt.adoptConcept = this.form.adoptConcept;
              this.adopt.adoptWay = this.form.adoptWay;
              this.adopt.adoptUserAge = this.form.adoptUserAge;
              this.adopt.isReturnVisit = this.form.isReturnVisit;
              this.adopt.isFeedback = this.form.isFeedback;
              this.adopt.isSuccess =
                this.adopt.isSuccess == -1 ? 0 : this.adopt.isSuccess;
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
      this.$router.push("/article/" + this.adopt.titleId);
    },
    toAdopt() {
      this.$router.push("/adopt/" + this.adoptId);
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
</style>