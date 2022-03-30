<template>
  <div>
    <el-steps :active="active" finish-status="success">
      <el-step title="第一步" />
      <el-step title="第二步" />
      <el-step title="第三步" />
    </el-steps>

    <el-form
      ref="dataForm"
      label-position="left"
      label-width="100px"
      style="width: 400px; margin-left: 50px"
    >
      <div v-show="active == 1">
        <el-form-item label="设备名称">
          <el-input v-model="user.phone" placeholder="请输入" />
        </el-form-item>
      </div>
      <div v-show="active == 2"></div>
      <div v-show="active == 3"></div>
    </el-form>

    //第五步:设置上一步和下一步的按钮
    <el-button v-if="active < 4" style="margin-top: 12px" @click="next"
      >下一步</el-button
    >
    <el-button v-if="active > 1" style="margin-top: 12px" @click="pre"
      >上一步</el-button
    >
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 1,
      user: {
        phone: ""
      }
    };
  },

  methods: {
    // 步骤条下一步的方法
    next() {
      if (this.active++ > 3) this.active = 1;
    },
    // 步骤条上一步的方法
    pre() {
      if (this.active-- < 2) this.active = 1;
    },
  },
};
</script>

<style>
</style>