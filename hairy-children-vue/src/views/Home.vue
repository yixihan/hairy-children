<template>
  <div class="home">
    <Header></Header>
    <BackTop></BackTop>
    <router-view/>
  </div>
</template>

<script>
// @ is an alias to /src
import Header from '@/components/Header.vue'
import BackTop from '../components/BackTop.vue'

export default {
  name: 'Home',
  components: {
    Header,
    BackTop
  }
}
</script>
