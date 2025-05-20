<template>
  <header class="navbar">
    <h1 class="logo">我的博客</h1>
    <nav>
      <ul>
        <li><a href="#">首页</a></li>
        <li><a href="#">关于</a></li>
        <li><a href="#">文章</a></li>
      </ul>
    </nav>
  </header>
</template>

<script setup lang="ts">
// 无状态组件，逻辑不复杂不需要定义props
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background-color: #333;
  color: white;
}
.navbar ul {
  list-style: none;
  display: flex;
  gap: 1rem;
}
.navbar a {
  color: white;
  text-decoration: none;
}
</style>
