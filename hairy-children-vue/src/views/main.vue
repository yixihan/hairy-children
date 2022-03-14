<template>
  <div>
    <template>
      <main-navbar />
      <div>
        <main-conetnt />
      </div>
    </template>
  </div>
</template>

<script>
import MainNavbar from './main-navbar.vue'
import MainConetnt from './main-content.vue'

export default {
  components: {
    MainNavbar,
    MainConetnt
  }
}
</script>

<style>

</style>