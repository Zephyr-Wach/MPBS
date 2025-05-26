<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getUserInfo, updateUserInfo, updatePassword } from '@/api/user/user';
import { uploadFile, generateUrl } from '@/api/admin/media';
import { checkEmail, checkEmailExist } from '@/api/user/email';
import { sendCodeToEmail } from '@/api/public/email';

interface UserInfo {
  userId: string;
  userName: string;
  userPwd: string | null;
  email: string | null;
  emailStatus: string;  // 'confirmed' | 'unconfirmed' | 'undefined'
  avatarUrl: string | null;
  userPermission: string;
}


const userInfo = ref<UserInfo | null>(null);
const error = ref<string | null>(null);
const successMsg = ref<string | null>(null);
const uploading = ref(false);
const sendCodeCooldown = ref(0);
let cooldownTimer: number | null = null;

async function fetchUserInfo() {
  try {
    const res = await getUserInfo();
    if (res.code === 0 && res.data) {
      userInfo.value = res.data;
    } else {
      error.value = res.message || '获取用户信息失败';
    }
  } catch (e) {
    error.value = '接口请求异常';
  }
}

onMounted(() => {
  fetchUserInfo();
});

// 文件上传相关
const fileInput = ref<HTMLInputElement | null>(null);

function onAvatarClick() {
  if (fileInput.value) {
    fileInput.value.value = '';
    fileInput.value.click();
  }
}

async function onFileChange(event: Event) {
  const files = (event.target as HTMLInputElement).files;
  if (!files || files.length === 0) return;

  const file = files[0];
  uploading.value = true;
  error.value = null;
  successMsg.value = null;

  try {
    const uploadRes = await uploadFile(file);
    const urlRes = await generateUrl(uploadRes.data.id);

    const baseURL = import.meta.env.VITE_API_BASE_URL;

    if (userInfo.value) {
      userInfo.value.avatarUrl = baseURL + urlRes.data;
      // 头像上传后不自动保存，保持原逻辑
    }
  } catch (e) {
    error.value = '头像上传失败，请重试';
  } finally {
    uploading.value = false;
  }
}

function validateEmail(email: string): boolean {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
}

async function updateInfo() {
  if (!userInfo.value) return;
  error.value = null;
  successMsg.value = null;

  if (userInfo.value.email && !validateEmail(userInfo.value.email)) {
    error.value = '邮箱格式不正确';
    return;
  }

  try {
    const res = await updateUserInfo(
        userInfo.value.userName,
        userInfo.value.avatarUrl ?? '',
        userInfo.value.email ?? ''
    );
    if (res.code === 0 && res.data) {
      userInfo.value = res.data;
      successMsg.value = '信息更新成功';
    } else {
      error.value = res.message || '更新失败';
    }
  } catch (e) {
    error.value = '接口请求异常';
  }
}

// --- 新增修改密码弹窗相关 ---

const showPwdDialog = ref(false);

const oldPwd = ref('');
const newPwd = ref('');
const confirmNewPwd = ref('');

const loadingPassword = ref(false);

function openPwdDialog() {
  error.value = null;
  successMsg.value = null;
  oldPwd.value = '';
  newPwd.value = '';
  confirmNewPwd.value = '';
  showPwdDialog.value = true;
}

function closePwdDialog() {
  showPwdDialog.value = false;
}

// 修改密码提交
async function submitChangePassword() {
  error.value = null;
  successMsg.value = null;

  if (!oldPwd.value) {
    error.value = '请输入旧密码';
    return;
  }
  if (!newPwd.value) {
    error.value = '请输入新密码';
    return;
  }
  if (newPwd.value !== confirmNewPwd.value) {
    error.value = '两次输入的新密码不一致';
    return;
  }

  if (!userInfo.value) {
    error.value = '用户信息异常，无法修改密码';
    return;
  }

  loadingPassword.value = true;
  try {
    const res = await updatePassword({
      userId: userInfo.value.userId,
      oldPassword: oldPwd.value,
      newPassword: newPwd.value
    });
    if (res.code === 0) {
      successMsg.value = '密码修改成功';
      setTimeout(() => {
        closePwdDialog();
      }, 1500);
    } else {
      error.value = res.message || '密码修改失败';
    }
  } catch (e) {
    error.value = '接口请求异常';
  } finally {
    loadingPassword.value = false;
  }
}

// --- 新增邮箱验证相关 ---

const emailVerificationDialog = ref(false);
const emailForVerification = ref('');  // 待验证邮箱
const emailCode = ref('');
const emailError = ref<string | null>(null);
const emailSuccess = ref<string | null>(null);
const sendingCode = ref(false);
const verifying = ref(false);

function handleEmailClick() {
  if (userInfo.value?.emailStatus !== 'confirmed') {
    openEmailVerification();
  }
}

function openEmailVerification() {
  emailError.value = null;
  emailSuccess.value = null;
  emailCode.value = '';
  if (userInfo.value?.email) {
    emailForVerification.value = userInfo.value.email;
  } else {
    emailForVerification.value = '';
  }
  emailVerificationDialog.value = true;
}

async function sendVerificationCode() {
  emailError.value = null;
  emailSuccess.value = null;

  if (!emailForVerification.value) {
    emailError.value = '请输入邮箱地址';
    return;
  }
  if (!validateEmail(emailForVerification.value)) {
    emailError.value = '邮箱格式不正确';
    return;
  }

  if (sendCodeCooldown.value > 0) {
    emailError.value = `请等待${sendCodeCooldown.value}秒后再发送`;
    return;
  }

  // 如果是新增邮箱，先检查是否已注册
  if (userInfo.value?.emailStatus === 'undefined') {
    try {
      const existRes = await checkEmailExist(emailForVerification.value);
      if (existRes.code === 0 && existRes.data.exists) {
        emailError.value = '该邮箱已被注册';
        return;
      }
    } catch {
      emailError.value = '邮箱校验接口异常';
      return;
    }
  }

  sendingCode.value = true;
  try {
    const sendRes = await sendCodeToEmail(emailForVerification.value);
    if (sendRes.code === 0) {
      emailSuccess.value = '验证码已发送，请查收邮箱';
      // 开始冷却倒计时60秒
      sendCodeCooldown.value = 60;
      cooldownTimer = setInterval(() => {
        sendCodeCooldown.value--;
        if (sendCodeCooldown.value <= 0 && cooldownTimer) {
          clearInterval(cooldownTimer);
          cooldownTimer = null;
        }
      }, 1000);
    } else {
      emailError.value = sendRes.message || '验证码发送失败';
    }
  } catch {
    emailError.value = '验证码发送异常';
  } finally {
    sendingCode.value = false;
  }
}

async function verifyEmailCode() {
  emailError.value = null;
  emailSuccess.value = null;

  if (!emailCode.value) {
    emailError.value = '请输入验证码';
    return;
  }
  verifying.value = true;
  try {
    const verifyRes = await checkEmail({
      email: emailForVerification.value,
      code: emailCode.value
    });
    if (verifyRes.code === 0) {
      emailSuccess.value = '邮箱验证成功';
      // 更新用户信息状态和邮箱
      if (userInfo.value) {
        userInfo.value.email = emailForVerification.value;
        userInfo.value.emailStatus = 'confirmed';
      }
      emailVerificationDialog.value = false;
    } else {
      emailError.value = verifyRes.message || '验证码验证失败';
    }
  } catch {
    emailError.value = '邮箱验证接口异常';
  } finally {
    verifying.value = false;
  }
}

</script>

<template>
  <div class="profile-container">
    <h2>个人中心</h2>
    <div v-if="error" class="error-msg">{{ error }}</div>
    <div v-if="successMsg" class="success-msg">{{ successMsg }}</div>

    <div v-else-if="userInfo" class="profile-info">
      <div class="avatar-box" @click="onAvatarClick" style="cursor:pointer;position:relative;">
        <template v-if="uploading">
          <span>上传中...</span>
        </template>
        <template v-else>
          <img
              v-if="userInfo.avatarUrl"
              :src="userInfo.avatarUrl"
              alt="头像"
              style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;"
          />
          <p v-else class="avatar-text">点击上传头像</p>
        </template>
        <input
            ref="fileInput"
            type="file"
            accept="image/*"
            @change="onFileChange"
            style="display:none"
        />
      </div>
      <div class="info-row"><span>用户ID:</span> {{ userInfo.userId }}</div>
      <div class="info-row"><span>用户名:</span> {{ userInfo.userName }}</div>
      <!-- 主界面邮箱展示 -->
      <div class="info-row">
        <span>邮箱:</span>
        <input
            type="email"
            :value="userInfo.email || ''"
            readonly
            placeholder="未设置邮箱"
            class="input-email"

            style="cursor: pointer; background-color: #f9f9f9;"
        />
      </div>


      <div class="info-row">
        <span>邮箱状态:</span>
        <template v-if="userInfo.emailStatus === 'confirmed'">
          <span style="color: green;">已验证</span>
        </template>
        <template v-else-if="userInfo.emailStatus === 'unconfirmed'">
          <span style="color: orange;">未验证</span>
          <button @click="openEmailVerification" style="margin-left: 8px;">验证邮箱</button>
        </template>
        <template v-else-if="userInfo.emailStatus === 'undefined'">
          <span style="color: gray;">未设置邮箱</span>
          <button @click="handleEmailClick" style="margin-left: 8px;">添加邮箱</button>
        </template>
      </div>

      <div class="info-row"><span>权限:</span> {{ userInfo.userPermission }}</div>
      <button class="update-button" @click="updateInfo">修改</button>

      <!-- 新增：修改密码按钮 -->
      <button class="update-button" style="margin-top: 12px;" @click="openPwdDialog">修改密码</button>
    </div>

    <div v-else class="loading">加载中...</div>

    <!-- 修改密码弹窗 -->
    <div v-if="showPwdDialog" class="modal-overlay" @click.self="closePwdDialog">
      <div class="modal-content">
        <h3>修改密码</h3>

        <div v-if="error" class="error-msg">{{ error }}</div>
        <div v-if="successMsg" class="success-msg">{{ successMsg }}</div>

        <input
            type="password"
            v-model="oldPwd"
            placeholder="请输入旧密码"
            autocomplete="current-password"
        />
        <input
            type="password"
            v-model="newPwd"
            placeholder="请输入新密码"
            autocomplete="new-password"
        />
        <input
            type="password"
            v-model="confirmNewPwd"
            placeholder="确认新密码"
            autocomplete="new-password"
        />

        <div class="modal-actions">
          <button @click="closePwdDialog" :disabled="loadingPassword">取消</button>
          <button @click="submitChangePassword" :disabled="loadingPassword">
            {{ loadingPassword ? '提交中...' : '提交' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 邮箱验证弹窗 -->
    <div
        v-if="emailVerificationDialog"
        class="modal-overlay"
        @click.self="emailVerificationDialog = false"
    >
      <div class="modal-content">
        <h3>{{ userInfo?.emailStatus === 'undefined' ? '添加邮箱并验证' : '邮箱验证' }}</h3>

        <div v-if="emailError" class="error-msg">{{ emailError }}</div>
        <div v-if="emailSuccess" class="success-msg">{{ emailSuccess }}</div>

        <input
            type="email"
            v-model="emailForVerification"
            placeholder="请输入邮箱"
            :disabled="userInfo?.emailStatus === 'unconfirmed'"
            style="margin-bottom: 12px;"
        />
        <div style="display: flex; gap: 8px; margin-bottom: 12px;">
          <input
              type="text"
              v-model="emailCode"
              placeholder="请输入验证码"
              style="flex: 1;"
          />
          <button
              @click="sendVerificationCode"
              :disabled="sendingCode || sendCodeCooldown > 0 || !emailForVerification || !validateEmail(emailForVerification)"
          >
            {{ sendingCode ? '发送中...' : sendCodeCooldown > 0 ? `重新发送(${sendCodeCooldown}s)` : '发送验证码' }}
          </button>

        </div>

        <div class="modal-actions">
          <button @click="emailVerificationDialog = false" :disabled="verifying">取消</button>
          <button @click="verifyEmailCode" :disabled="verifying">
            {{ verifying ? '验证中...' : '提交验证' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 400px;
  margin: 20px auto;
  font-family: Arial, sans-serif;
}
.avatar-box {
  width: 100px;
  height: 100px;
  margin-bottom: 12px;
  background-color: #ddd;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  overflow: hidden;
}
.avatar-text {
  color: #666;
  font-size: 14px;
}
.info-row {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}
.info-row span:first-child {
  width: 80px;
  font-weight: 600;
}
.input-email {
  flex: 1;
  padding: 4px 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.update-button {
  width: 100%;
  padding: 8px 0;
  background-color: #409eff;
  border: none;
  color: white;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
}
.update-button:hover {
  background-color: #66b1ff;
}
.error-msg {
  color: #f56c6c;
  margin-bottom: 12px;
}
.success-msg {
  color: #67c23a;
  margin-bottom: 12px;
}
.loading {
  text-align: center;
  color: #999;
  margin-top: 40px;
}

/* 弹窗通用样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}
.modal-content {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  width: 320px;
  max-width: 90vw;
  box-sizing: border-box;
}
.modal-content h3 {
  margin-bottom: 16px;
  font-weight: 600;
}
.modal-content input {
  width: 100%;
  padding: 6px 10px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
.modal-actions button {
  padding: 6px 12px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
}
.modal-actions button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.modal-actions button:first-child {
  background-color: #f0f0f0;
  color: #333;
}
.modal-actions button:last-child {
  background-color: #409eff;
  color: white;
}
.modal-actions button:last-child:hover:not(:disabled) {
  background-color: #66b1ff;
}
</style>
