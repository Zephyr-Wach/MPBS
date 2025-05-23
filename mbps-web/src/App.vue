<template>
  <router-view />
  <LoginModal
      v-if="showLoginModal"
      @close="showLoginModal = false"
      @showRegister="switchToRegister"
      @loginSuccess="handleLoginSuccess"
  />
  <RegisterModal
      v-if="showRegisterModal"
      @close="showRegisterModal = false"
      @registerSuccess="handleRegisterSuccess"

  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import LoginModal from '@/components/Login.vue';
import RegisterModal from '@/components/RegisterModal.vue';
import { showLoginModal } from '@/router';

const showRegisterModal = ref(false);

function switchToRegister() {
  showLoginModal.value = false;
  showRegisterModal.value = true;
}

function handleRegisterSuccess(userData: any) {
  showRegisterModal.value = false;
  // 这里可以自动登录或提示注册成功后去登录
  // 例如自动打开登录弹窗
  showLoginModal.value = true;
}
function handleLoginSuccess(userData: any) {
  showLoginModal.value = false; // 登录成功后关闭弹窗
  // 这里可以进一步处理登录成功后的逻辑，比如刷新用户状态等
}

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
*,
*::before,
*::after {
  box-sizing: border-box;
}
</style>
