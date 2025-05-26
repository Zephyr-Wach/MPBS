<template>
  <div class="modal-backdrop" @click.self="close">
    <div class="modal">
      <h2>登录</h2>
      <!-- 切换登录方式按钮 -->
      <div style="text-align:center; margin-bottom:12px;">
        <button @click="toggleLoginType" style="background:none; border:none; color:#3498db; cursor:pointer;">
          {{ isEmailLogin ? '切换为用户名密码登录' : '切换为邮箱验证码登录' }}
        </button>
      </div>

      <!-- 用户名密码登录 -->
      <form v-if="!isEmailLogin" @submit.prevent="handleUserLogin" class="login-form">
        <input v-model="userName" placeholder="用户名" required />
        <input type="password" v-model="userPwd" placeholder="密码" required />
        <div class="btn-group">
          <button type="submit" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
          <button type="button" class="close-btn" @click="close">关闭</button>
        </div>
      </form>

      <!-- 邮箱验证码登录 -->
      <form v-else @submit.prevent="handleEmailLogin" class="login-form">
        <input v-model="email" type="email" placeholder="邮箱" required />
        <div style="display:flex; gap: 8px;">
          <input v-model="code" placeholder="验证码" required style="flex:1;" />
          <button type="button" @click="sendCode" :disabled="codeSending && countdown > 0" style="width: 100px;">
            {{ countdown > 0 ? `${countdown}s后重发` : '发送验证码' }}
          </button>

        </div>
        <div class="btn-group">
          <button type="submit" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
          <button type="button" class="close-btn" @click="close">关闭</button>
        </div>
      </form>
      <p class="register-tip">
        还没有账号，
        <a href="#" @click.prevent="$emit('showRegister')">点击注册</a>
      </p>
      <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted } from 'vue';
import { loginAndSaveToken, EmailLoginAndSaveToken } from '@/api/public/user';
import { sendCodeToEmail } from '@/api/public/email';

const userName = ref('');
const userPwd = ref('');
const loading = ref(false);
const errorMsg = ref('');
const isEmailLogin = ref(false);
const email = ref('');
const code = ref('');
const codeSending = ref(false);
const countdown = ref(0);

let timer = null;

const emit = defineEmits(['close', 'loginSuccess']);

function close() {
  emit('close');
}

function isValidEmail(email) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
}

function toggleLoginType() {
  errorMsg.value = '';
  isEmailLogin.value = !isEmailLogin.value;
  userName.value = '';
  userPwd.value = '';
  email.value = '';
  code.value = '';
  codeSending.value = false;
  countdown.value = 0;
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
}

async function handleUserLogin() {
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

async function sendCode() {
  if (!email.value) {
    errorMsg.value = '请输入邮箱';
    return;
  }
  if (!isValidEmail(email.value)) {
    errorMsg.value = '邮箱格式不正确';
    return;
  }
  errorMsg.value = '';
  codeSending.value = true;

  try {
    await sendCodeToEmail(email.value);
    countdown.value = 60;
    timer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(timer);
        timer = null;
        codeSending.value = false;
      }
      }, 1000);
  } catch (e) {
    errorMsg.value = e.message || '发送验证码失败';
    codeSending.value = false;
  }
}

async function handleEmailLogin() {
  if (!isValidEmail(email.value)) {
    errorMsg.value = '邮箱格式不正确';
    return;
  }
  loading.value = true;
  errorMsg.value = '';
  try {
    const userData = await EmailLoginAndSaveToken(email.value, code.value);
    emit('loginSuccess', userData);
  } catch (e) {
    errorMsg.value = e.message || '邮箱登录失败，请重试';
  } finally {
    loading.value = false;
  }
}

onUnmounted(() => {
  if (timer) {
    clearInterval(timer);
    timer = null;
    codeSending.value = false;
  }
});
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
