<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getUserInfoList, updateUserInfo } from '@/api/admin/user';

interface UserInfoDTO {
  userId: string;
  userName?: string;
  userPwd?: string;
  email?: string;
  avatarUrl?: string;
  userPermission?: string;
}

const users = ref<UserInfoDTO[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const editingUser = ref<UserInfoDTO | null>(null);
const form = ref<UserInfoDTO>({
  userId: '',
  userName: '',
  userPwd: '',
  email: '',
  avatarUrl: '',
  userPermission: '',
});

async function fetchUsers() {
  loading.value = true;
  error.value = null;
  try {
    const res = await getUserInfoList();
    users.value = res.data || [];
  } catch (e) {
    error.value = '获取用户列表失败';
  } finally {
    loading.value = false;
  }
}

function startEdit(user: UserInfoDTO) {
  editingUser.value = user;
  form.value = { ...user }; // 复制一份避免直接修改
}

async function saveUser() {
  if (!editingUser.value) return;
  try {
    const success = await updateUserInfo(editingUser.value.userId, form.value);
    if (success) {
      alert('更新成功');
      editingUser.value = null;
      await fetchUsers();
    } else {
      alert('更新失败');
    }
  } catch {
    alert('更新异常');
  }
}

function cancelEdit() {
  editingUser.value = null;
}

onMounted(() => {
  fetchUsers();
});
</script>

<template>
  <div>
    <h2>用户管理</h2>

    <div v-if="loading">加载中...</div>
    <div v-if="error" style="color:red">{{ error }}</div>

    <table border="1" cellspacing="0" cellpadding="5" v-if="!loading && !error">
      <thead>
      <tr>
        <th>用户名</th>
        <th>邮箱</th>
        <th>头像</th>
        <th>权限</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.userId">
        <td>{{ user.userName }}</td>
        <td>{{ user.email }}</td>
        <td>
          <img :src="user.avatarUrl" alt="头像" style="width:40px; height:40px; border-radius:50%" />
        </td>
        <td>{{ user.userPermission }}</td>
        <td>
          <button @click="startEdit(user)">编辑</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="editingUser" style="margin-top:20px; border:1px solid #ccc; padding:10px;">
      <h3>编辑用户信息 - {{ editingUser.userName }}</h3>
      <form @submit.prevent="saveUser">
        <div>
          <label>用户名: </label>
          <input v-model="form.userName" required />
        </div>
        <div>
          <label>密码: </label>
          <input type="password" v-model="form.userPwd" placeholder="留空不修改密码" />
        </div>
        <div>
          <label>邮箱: </label>
          <input type="email" v-model="form.email" />
        </div>
        <div>
          <label>头像URL: </label>
          <input v-model="form.avatarUrl" />
        </div>
        <div>
          <label>权限: </label>
          <input v-model="form.userPermission" />
        </div>

        <button type="submit">保存</button>
        <button type="button" @click="cancelEdit">取消</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}
th, td {
  text-align: left;
}
form > div {
  margin-bottom: 10px;
}
label {
  display: inline-block;
  width: 80px;
}
input {
  padding: 5px;
  width: 200px;
}
button {
  margin-right: 10px;
  padding: 5px 10px;
}
</style>
