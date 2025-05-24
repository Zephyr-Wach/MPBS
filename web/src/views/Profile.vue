<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getUserInfo, updateUserInfo, updatePassword } from '@/api/user/user';
import { uploadFile, generateUrl } from '@/api/public/media';

interface UserInfo {
  userId: string;
  userName: string;
  userPwd: string | null;
  email: string | null;
  avatarUrl: string | null;
  userPermission: string;
}

const userInfo = ref<UserInfo | null>(null);
const error = ref<string | null>(null);
const successMsg = ref<string | null>(null);
const uploading = ref(false);

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
      <div class="info-row"><span>邮箱:</span> {{ userInfo.email ?? '未设置' }}</div>
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
            {{ loadingPassword ? '提交中...' : '提交修改' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  background-color: #fafafa;
}

h2 {
  text-align: center;
  margin-bottom: 24px;
  color: #333;
}

.error-msg {
  color: #d93025;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.success-msg {
  color: #4caf50;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.avatar-box {
  width: 100px;
  height: 100px;
  margin: 0 auto 20px auto;
  border: 2px dashed #bbb;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  color: #666;
  font-size: 12px;
  text-align: center;
  padding: 8px;
  word-break: break-word;
  position: relative;
  overflow: hidden;
}

.avatar-text {
  line-height: 1.2;
}

.info-row {
  display: flex;
  justify-content: space-between;
  font-size: 16px;
  color: #444;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 6px;
}

.info-row span {
  font-weight: 600;
  color: #222;
}

.loading {
  text-align: center;
  color: #888;
  font-style: italic;
}

/* 弹窗遮罩 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

/* 弹窗内容 */
.modal-content {
  background: white;
  padding: 24px;
  width: 320px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgb(0 0 0 / 0.2);
  display: flex;
  flex-direction: column;
}

.modal-content h3 {
  margin-bottom: 16px;
  text-align: center;
}

.modal-content input {
  margin-bottom: 12px;
  padding: 8px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.modal-actions button {
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
}

.modal-actions button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
