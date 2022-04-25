<template>
  <el-footer class="footer">
    <router-link to="/" class="router-link">联系我们</router-link>
    <el-divider direction="vertical"></el-divider>
    <router-link to="/" class="router-link">意见反馈</router-link>
    <el-divider direction="vertical"></el-divider>
    <router-link to="/" class="router-link">举报不良信息</router-link>
    <el-divider direction="vertical"></el-divider>
    <router-link to="/" class="router-link">友情链接</router-link>
  </el-footer>
</template>

<script>
export default {
  name: "NavFooter",
};
</script>

<style lang="scss" scoped>
.footer {
  font: italic small-caps bold 14px/24px Georgia, serif;
}

a {
  
  text-decoration: none;
  padding-right: 10px;
  img {
    width: 100px;
    height: 100px;
    margin-right: 10px;
    object-fit: cover;
  }
}
a:hover {
  .title {
    color: #7da5b3 !important;
  }
}
</style>
