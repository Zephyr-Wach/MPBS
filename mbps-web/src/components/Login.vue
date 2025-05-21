<template>
  <div class="modal-backdrop" @click.self="close">
    <div class="modal">
      <h2>登录</h2>
      <form @submit.prevent="handleLogin" class="login-form">
        <input v-model="userName" placeholder="用户名" required />
        <input type="password" v-model="userPwd" placeholder="密码" required />
        <div class="btn-group">
          <button type="submit" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
          <button type="button" class="close-btn" @click="close">关闭</button>
        </div>
      </form>
      <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { loginAndSaveToken } from '@/api/public/user.js';

const userName = ref('');
const userPwd = ref('');
const loading = ref(false);
const errorMsg = ref('');

const emit = defineEmits(['close', 'loginSuccess']);

function close() {
  emit('close');
}

async function handleLogin() {
  loading.value = true;
  errorMsg.value = '';
  try {
    const userData = await loginAndSaveToken(userName.value, userPwd.value);
    emit('loginSuccess', userData);
  } catch (e) {
    errorMsg.value = e.message || '登录失败，请重试';
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10000;
}
.modal {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 320px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.3);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.login-form input {
  padding: 8px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.btn-group {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

button {
  flex: 1;
  padding: 10px 0;
  font-size: 14px;
  cursor: pointer;
  border-radius: 4px;
  border: none;
  color: white;
  background-color: #3498db;
  transition: background-color 0.2s ease;
}

button:disabled {
  background-color: #7fb5d9;
  cursor: not-allowed;
}

.close-btn {
  background-color: #aaa;
}

.close-btn:hover {
  background-color: #888;
}

button:hover:not(:disabled) {
  background-color: #2980b9;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
