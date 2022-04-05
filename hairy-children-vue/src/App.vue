<template>
  <n-config-provider :locale="zhCN" :date-locale="dateZhCN">
    <n-message-provider>
      <n-dialog-provider>
        <div>
          <router-view></router-view>
        </div>
      </n-dialog-provider> </n-message-provider
  ></n-config-provider>
</template>

<script>
import { defineComponent } from 'vue'
import { RouterView } from 'vue-router'
import { NMessageProvider, NDialogProvider, NConfigProvider, zhCN, dateZhCN } from 'naive-ui'

export default defineComponent({
  components: {
    NMessageProvider,
    NDialogProvider,
    NConfigProvider,
    RouterView
  },
  setup() {
    return { zhCN, dateZhCN }
  }
})
</script>

<style scoped>
html,
body {
  height: 100%;
}
#app {
  height: 100%;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-size: 16px;
}
* {
  margin: 0;
  padding: 0;
}
div {
  min-height: 100vh;
}
</style>
