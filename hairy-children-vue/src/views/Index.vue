<template>
  <div class="index">
    <div>
      <carousel></carousel>
      <meet-pets></meet-pets>
      <nav-footer></nav-footer>
    </div>
  </div>
</template>

<script>
import Carousel from "../components/Carousel.vue";
import MeetPets from "../components/MeetPets.vue";
import NavFooter from "../components/NavFooter.vue";

export default {
  name: "index",
  components: {
    Carousel,
    MeetPets,
    NavFooter,
  },
};
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
