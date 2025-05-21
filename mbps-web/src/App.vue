<template>
  <router-view />
</template>

<script setup lang="ts">
</script>

<!-- 全局样式必须写在非 scoped 样式块中 -->
<style>
html, body, #app {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}
* {
  box-sizing: border-box;
}

</style>
