<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getUserInfo } from '@/api/user/user';
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
const uploading = ref(false);  // 上传状态提示

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

// 新增：文件上传处理
const fileInput = ref<HTMLInputElement | null>(null);

function onAvatarClick() {
  if (fileInput.value) {
    fileInput.value.value = '';  // 清空上次选择
    fileInput.value.click();
  }
}

async function onFileChange(event: Event) {
  const files = (event.target as HTMLInputElement).files;
  if (!files || files.length === 0) return;

  const file = files[0];
  uploading.value = true;
  error.value = null;

  try {
    const uploadRes = await uploadFile(file);
    const urlRes = await generateUrl(uploadRes.data.id);

    const baseURL = import.meta.env.VITE_API_BASE_URL;

    if (userInfo.value) {
      userInfo.value.avatarUrl = baseURL + urlRes.data;
      console.info(userInfo.value.avatarUrl);
    }
  } catch (e) {
    error.value = '头像上传失败，请重试';
  } finally {
    uploading.value = false;
  }
}

</script>

<template>
  <div class="profile-container">
    <h2>个人中心</h2>
    <div v-if="error" class="error-msg">{{ error }}</div>

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
    </div>

    <div v-else class="loading">加载中...</div>
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
</style>
