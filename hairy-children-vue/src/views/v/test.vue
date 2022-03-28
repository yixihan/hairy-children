<template>
  <div>
    这里是认证测试页面
    <el-steps :active="active" finish-status="success" space="150px">
      <el-step title="步骤 1"></el-step>
      <el-step title="步骤 2"></el-step>
      <el-step title="步骤 2"></el-step>
    </el-steps>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 0,
    };
  },

  methods: {
    next() {
      if (this.active++ > 2) this.active = 0;
    },
  },
};
</script>

<style>
</style>