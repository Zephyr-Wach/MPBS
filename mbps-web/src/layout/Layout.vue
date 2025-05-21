<template>
  <div class="layout">
    <Navbar class="navbar" />
    <div class="content-area">
      <Sidebar class="sidebar" />
      <div class="main-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import Sidebar from '@/components/SideBar.vue';
import Navbar from '@/components/Navbar.vue';
</script>

<style scoped>
.layout {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  height: 30px;
  flex-shrink: 0;
  width: 100%;
  background-color: #2c3e50;
}

.content-area {
  margin-top: 30px;
  flex: 1;
  display: flex; /* 横向排列 */
  width: 100%;
}

.sidebar {
  width: 200px;
  background-color: #f8f9fa;
}

.main-content {
  flex: 1; /* 自适应剩余宽度 */
  padding: 16px;
  overflow-y: auto;
}

</style>
