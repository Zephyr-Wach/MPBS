<template>
  <div class="modal-backdrop" @click.self="close">
    <div class="modal">
      <h2>注册</h2>
      <form @submit.prevent="handleRegister" class="register-form">
        <input
            v-model="userName"
            placeholder="用户名"
            @blur="checkUserNameExist"
            :class="{ invalid: userNameError }"
            required
        />
        <p v-if="userNameError" class="error">{{ userNameError }}</p>

        <input
            type="password"
            v-model="userPwd"
            placeholder="密码"
            required
        />

        <input
            type="password"
            v-model="confirmPwd"
            placeholder="确认密码"
            :class="{ invalid: confirmPwdError }"
            required
        />
        <p v-if="confirmPwdError" class="error">{{ confirmPwdError }}</p>

        <div class="btn-group">
          <button type="submit" >
            {{ loading ? '注册中...' : '注册' }}
          </button>
          <button type="button" class="close-btn" @click="close">关闭</button>
        </div>
      </form>
      <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { registerAndSaveToken, checkUserName } from '@/api/public/user.js';

const userName = ref('');
const userPwd = ref('');
const confirmPwd = ref('');
const loading = ref(false);
const errorMsg = ref('');

const userNameError = ref('');
const confirmPwdError = ref('');

const emit = defineEmits(['close', 'registerSuccess']);

// 校验用户名是否已注册
async function checkUserNameExist() {
  if (!userName.value.trim()) {
    userNameError.value = '用户名不能为空';
    return;
  }
  try {
    const res = await checkUserName({ userName: userName.value });
    if (res.code === 400) {
      userNameError.value = '用户名已存在';
    } else if (res.code === 0) {
      userNameError.value = '';
    } else {
      userNameError.value = '用户名校验异常，请稍后重试';
    }
  } catch (error) {
    userNameError.value = '用户名校验失败，请稍后重试';
  }
}


// 校验确认密码是否一致
watch([userPwd, confirmPwd], () => {
  if (confirmPwd.value && confirmPwd.value !== userPwd.value) {
    confirmPwdError.value = '两次输入密码不一致';
  } else {
    confirmPwdError.value = '';
  }
});

function close() {
  emit('close');
}

async function handleRegister() {
  if (userNameError.value || confirmPwdError.value) {
    return;
  }
  loading.value = true;
  errorMsg.value = '';
  try {
    const userData = await registerAndSaveToken(userName.value, userPwd.value);
    emit('registerSuccess', userData); // 通知注册成功
  } catch (e: any) {
    errorMsg.value = e.message || '注册失败，请重试';
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
.register-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.register-form input {
  padding: 8px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  outline: none;
  transition: border-color 0.2s ease;
}
.register-form input.invalid {
  border-color: red;
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
  background-color: #2ecc71;
  transition: background-color 0.2s ease;
}
button:disabled {
  background-color: #7fbf9d;
  cursor: not-allowed;
}
.close-btn {
  background-color: #aaa;
}
.close-btn:hover {
  background-color: #888;
}
button:hover:not(:disabled) {
  background-color: #27ae60;
}
.error {
  color: red;
  margin-top: 4px;
  font-size: 13px;
  text-align: left;
}
</style>
