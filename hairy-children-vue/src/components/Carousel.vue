<template>
  <div class="carousel">
    <el-carousel :interval="5000" arrow="always" height="500px">
      <el-carousel-item v-for="(item, index) in indexImgs" :key="index">
        <a href="javascript:;">
          <img :src="item" alt="">
        </a>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  name: 'Carousel',
  data () {
    return {
      indexImgs: [
        require('@/assets/img/cat/wallhaven-1kwvov.jpg'),
        require('@/assets/img/cat/wallhaven-j3ly1w.jpg'),
        require('@/assets/img/cat/wallhaven-l322ql.jpg'),
        require('@/assets/img/cat/wallhaven-l37qjl.jpg')
      ]
    }
  }
}
</script>

<style scoped lang="scss">
.carousel {
  width: 100%;
  max-width: 1280px;
  margin: 0 auto;
}
  .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 500px;
    margin: 0;
  }
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
</style>
