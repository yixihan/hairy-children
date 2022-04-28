<template>
  <div class="page">
    <h2 class="title">编写贴子页面</h2>
    <div>
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="确定贴子标题与类型" />
        <el-step title="上传贴子预览图"></el-step>
        <el-step title="编写贴子" />
      </el-steps>
    </div>

    <el-form
      :model="form"
      :rules="rules"
      size="mini"
      ref="form"
      label-width="100px"
      label-position="left"
      v-if="active == 0"
      class="from"
    >
      <el-form-item label="贴子标题" prop="titleName">
        <el-input v-model="form.titleName"></el-input>
      </el-form-item>
      <el-form-item label="城市" prop="userAddress">
        <el-cascader
          size="mini"
          :options="options"
          v-model="form.selectedOptions"
          @change="handleChange"
        >
        </el-cascader>
      </el-form-item>
      <el-form-item label="贴子类型" prop="titleType">
        <el-radio-group v-model="form.titleType">
          <el-radio :label="1">领养</el-radio>
          <el-radio :label="2">寻宠</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetForm('form')">重置</el-button>
        <el-button type="primary" @click="next('form')" v-if="active == 0">
          下一步
        </el-button>
      </el-form-item>
    </el-form>

    <div class="uploadImg" v-if="active == 1">
      <el-upload
        class="avatar-uploader"
        :action="'http://175.24.229.41:9421/title/uploadImg/' + titleId"
        name="img"
        :headers="JwtToken"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
      >
        <i class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </div>
    <Edit v-if="active == 2" :titleId="titleId"></Edit>
  </div>
</template>

<script>
import {
  regionDataPlus,
  CodeToText,
  TextToCode,
} from "element-china-area-data";
import Edit from "@/components/EditArticle";

export default {
  inject: ["reload"],
  components: {
    Edit,
  },
  data() {
    return {
      form: {
        userId: "",
        userAddress: "",
        titleType: "",
        titleName: "",
        selectedOptions: [""],
      },
      JwtToken: "",
      titleId: "",
      active: 0,
      options: regionDataPlus,
      decodeCode: CodeToText,
      encodeCOde: TextToCode,
      rules: {
        titleName: [
          { required: true, message: "请输入贴子标题", trigger: "blur" },
        ],
        titleType: [
          { required: true, message: "请选择贴子类型", trigger: "change" },
        ],
        userAddress: [
          { required: true, message: "请选择发布城市", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    // 步骤条下一步的方法
    next(formName) {
      // 数据校验
      this.$refs[formName].validate((valid) => {
        // 数据校验成功
        if (valid) {
          console.log("现在是第 " + this.active + " 步");
          console.log("表单数据", this.form);
          // 创建贴子
          this.creatArticle().then(({ data }) => {
            if (data.code == 200) {
              this.titleId = data.data.titleId;
              this.active++;
            } else {
              this.$message({
                message: "贴子创建失败, 请稍后再试",
                type: "error",
              });
              this.reload();
            }
          });
        }
        // 其余情况, 弹出异常信息
        else {
          // 数据校验失败
          this.$message({
            message: "请正确输入",
            type: "error",
          });
          return false;
        }
      });
    },
    // 步骤条上一步的方法
    pre() {
      if (this.active-- < 1) {
        this.active = 1;
      }
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 城市区域码 => 城市
    handleChange(value) {
      let data = "";
      for (var i = 0; i < value.length; i++) {
        if (value[i] !== "") {
          data += this.decodeCode[value[i]];
        }
      }
      this.form.userAddress = data.replace("市辖区", "");
      console.log(this.form.userAddress);
    },

    // 创建贴子
    async creatArticle() {
      const data = await this.$axios({
        url: "/title/createTitle",
        method: "post",
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          userId: this.$store.getters.getUser.userId,
          userAddress: this.form.userAddress,
          titleName: this.form.titleName,
          titleType: this.form.titleType,
        },
      });

      return data;
    },
    // 校验头像文件
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 10;

      if (!isLt2M) {
        this.$message.error("图片大小不能超过 10MB!");
      }
      return isLt2M;
    },
    handleAvatarSuccess() {
      this.$message({
        message: "贴子预览图上传成功",
        type: "success",
      });
      this.active++;
    },
  },
  created() {
    this.JwtToken = JSON.parse(
      `{"Jwt-Token": "` + this.$store.getters.getToken + `"}`
    );
  },
};
</script>

<style lang="scss" scoped>
.page {
  margin-top: 70px;

  .title {
    font: italic small-caps bold 30px/40px Georgia, serif;
  }
  .write {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  .from {
    margin-top: 20px !important;
  }

  .uploadImg {
    margin: 50px auto auto auto;
    font-size: 300%;
  }
}
</style>>
