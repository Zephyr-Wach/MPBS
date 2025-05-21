<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getUserInfo } from '@/api/user/user';

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
</script>

<template>
  <div class="profile-container">
    <h2>个人中心</h2>
    <div v-if="error" class="error-msg">{{ error }}</div>

    <div v-else-if="userInfo" class="profile-info">
      <div class="avatar-box">
        <p class="avatar-text">{{ userInfo.avatarUrl ?? '暂无头像路径' }}</p>
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
