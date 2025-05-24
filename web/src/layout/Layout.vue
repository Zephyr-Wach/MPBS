<template>
  <div class="layout">
    <Navbar class="navbar" />
    <div class="content-area">
      <Sidebar class="sidebar" />
      <div class="main-content">
        <router-view />
        <!-- 页脚备案信息 -->
        <footer class="footer">
          <div class="footer-content">
            <p>© 2025 MPBS. 本站内容已在 <a href="https://github.com/Zephyr-Wach/MPBS" target="_blank">GitHub</a> 开源，遵循 MIT 协议。</p>
            <p>本站为个人技术展示用途，未开展任何商业行为。</p>
            <p class="record">
              <span>ICP备案号：<a href="https://beian.miit.gov.cn" target="_blank">填写备案号后替换这里</a></span>
            </p>
          </div>
        </footer>
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
  background-color: #0b98ef;
}

.content-area {
  flex: 1;
  display: flex;
  width: 100%;
  overflow: hidden;
}

.sidebar {
  width: 200px;
  background-color: #f8f9fa;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 16px;
  overflow-y: auto;
}

/* 新增的页脚样式：暗色风格，不固定位置 */
.footer {
  margin-top: 40px;
  text-align: center;
  font-size: 13px;
  color: #bbb;
  background-color: #0f7b88;
  padding: 12px 0;
  border-radius: 8px;
}
.footer a {
  color: #bbb;
  text-decoration: none;
}
.footer a:hover {
  text-decoration: underline;
}

</style>
