<template>
  <div>
    <main-navbar />
    <el-main>
      <router-view></router-view>
    </el-main>
  </div>
</template>

<script>
import MainNavbar from "../components/main-navbar.vue";
export default {
  components: {
    MainNavbar,
  },
};
</script>

<style>
</style>