<!-- src/layout/Layout.vue -->
<template>
  <div class="layout">
    <Sidebar class="sidebar" />
    <div class="main-content">
      <router-view /> <!-- 页面内容在这里切换 -->
    </div>
  </div>
</template>

<script setup>
import Sidebar from '@/components/SideBar.vue'; // 确保路径正确
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}
.sidebar {
  width: 200px;
  background-color: #f8f9fa;
}
.main-content {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}
</style>
