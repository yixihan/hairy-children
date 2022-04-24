<template>
  <div style="margin-top : 100px">
    <h1>测试页面</h1>
    <button @click="getUrl">按钮</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 1,
      url: "/title/2/名字测试.assets/1650815402493.png",
      user: {
        phone: "",
      },
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
    getUrl() {
      console.log(this.url);
      console.log(encodeURI(this.url));
    },
  },
};
</script>

<style>
</style>