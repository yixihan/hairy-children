<template>
  <div class="index">
    <div>
      <carousel></carousel>
      <meet-pets></meet-pets>
    </div>
  </div>
</template>

<script>
import Carousel from '../components/Carousel.vue'
import MeetPets from '../components/MeetPets.vue'
export default {
  name: 'index',
  components: {
    Carousel,
    MeetPets
  }
}
</script>

<style>
.index {
  width: 100%;
  height: 100vh;
  background: #fbfbfb;
  margin: 0;
  margin-top: 70px;
}
</style>
