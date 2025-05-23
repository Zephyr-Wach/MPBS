<template>
  <div class="layout">
    <Navbar class="navbar" />
    <div class="content-area">
      <Sidebar class="sidebar" />
      <div class="main-content">
        <router-view />
      </div>
    </div>

    <!-- 登录弹窗 -->
    <Login
        v-if="showLoginModal"
        @close="showLoginModal = false"
        @loginSuccess="onLoginSuccess"
    />
  </div>
</template>

<script setup>
import Sidebar from '@/components/SideBar.vue';
import Navbar from '@/components/Navbar.vue';
import Login from '@/components/Login.vue';
import { showLoginModal } from '@/router';

function onLoginSuccess(userData) {
  showLoginModal.value = false;

}
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
  position: fixed;
  height: calc(100vh - 30px);
  margin-top: 30px;
  flex: 1;
  display: flex;
  width: 100%;
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
