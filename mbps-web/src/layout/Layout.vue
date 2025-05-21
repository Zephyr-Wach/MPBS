<!-- src/layout/Layout.vue -->
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
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  height: 60px;
  flex-shrink: 0;
  z-index: 10;
}
.content-area {
  margin-top: 60px; /* navbar高度 */
  display: flex;
  height: calc(100vh - 60px);
}


.sidebar {
  width: 200px;
  background-color: #f8f9fa;
  z-index: 1;
}

.main-content {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}
</style>
