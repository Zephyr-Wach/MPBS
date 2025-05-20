<template>
  <aside class="sidebar">
    <ul>
      <li><a href="#">分类 1</a></li>
      <li><a href="#">分类 2</a></li>
      <li><a href="#">标签</a></li>
    </ul>
  </aside>
</template>

<script setup lang="ts">
</script>

<style scoped>
.sidebar {
  width: 200px;
  padding: 1rem;
  background-color: #f4f4f4;
  height: 100vh;
}
.sidebar ul {
  list-style: none;
  padding: 0;
}
.sidebar li {
  margin-bottom: 1rem;
}
.sidebar a {
  text-decoration: none;
  color: #333;
}
</style>
