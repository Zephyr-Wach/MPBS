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
const permissionOptions = ['NORMAL', 'JUNIOR', 'INTERMEDIATE', 'SENIOR', 'ULTIMATE'];
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
  form.value = { ...user };
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
  <div class="user-management">
    <h2>用户管理</h2>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-if="error" class="error">{{ error }}</div>

    <table v-if="!loading && !error">
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
          <img :src="user.avatarUrl" alt="头像" class="avatar" />
        </td>
        <td>{{ user.userPermission }}</td>
        <td>
          <button class="btn edit-btn" @click="startEdit(user)">编辑</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="editingUser" class="edit-panel">
      <h3>编辑用户信息 - {{ editingUser.userName }}</h3>
      <form @submit.prevent="saveUser">
        <div class="form-row">
          <label>用户名:</label>
          <input v-model="form.userName" required />
        </div>
        <div class="form-row">
          <label>密码:</label>
          <input type="password" v-model="form.userPwd" placeholder="留空不修改密码" />
        </div>
        <div class="form-row">
          <label>邮箱:</label>
          <input type="email" v-model="form.email" />
        </div>
        <div class="form-row">
          <label>头像URL:</label>
          <input v-model="form.avatarUrl" />
        </div>
        <div class="form-row">
          <label>权限:</label>
          <select v-model="form.userPermission" required>
            <option v-for="perm in permissionOptions" :key="perm" :value="perm">
              {{ perm }}
            </option>
          </select>
        </div>


        <div class="form-actions">
          <button type="submit" class="btn save-btn">保存</button>
          <button type="button" class="btn cancel-btn" @click="cancelEdit">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.user-management {
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
  padding: 20px;
  background: #f9fafa;
  border-radius: 8px;
  max-width: 900px;
  margin: auto;
}

h2 {
  color: #2de2be;
  margin-bottom: 15px;
  font-weight: 700;
}

.loading {
  color: #409eff;
  font-weight: 600;
  margin-bottom: 15px;
}

.error {
  color: #e64c3c;
  font-weight: 600;
  margin-bottom: 15px;
}

table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 8px;
}

th {
  text-align: left;
  padding: 10px 12px;
  background-color: #2de2be;
  color: white;
  font-weight: 600;
  border-radius: 6px 6px 0 0;
}

td {
  padding: 10px 12px;
  background-color: #fff;
  border: 1px solid #ddd;
  vertical-align: middle;
  border-radius: 4px;
}

tbody tr:hover td {
  background-color: #e0f7f4;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #409eff;
}

.btn {
  cursor: pointer;
  font-weight: 600;
  border: none;
  border-radius: 4px;
  padding: 6px 14px;
  transition: background-color 0.3s ease;
}

.edit-btn {
  background-color: #409eff;
  color: white;
}

.edit-btn:hover {
  background-color: #2d7cd7;
}

.edit-panel {
  margin-top: 30px;
  padding: 20px;
  border: 2px solid #2de2be;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 2px 8px rgba(45, 226, 190, 0.2);
}

.edit-panel h3 {
  margin-bottom: 20px;
  color: #2de2be;
  font-weight: 700;
}

.form-row {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.form-row label {
  width: 90px;
  font-weight: 600;
  color: #409eff;
}

.form-row input {
  flex: 1;
  padding: 7px 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  transition: border-color 0.3s ease;
}

.form-row input:focus {
  outline: none;
  border-color: #2de2be;
  box-shadow: 0 0 5px #2de2be;
}

.form-actions {
  margin-top: 20px;
}

.save-btn {
  background-color: #2de2be;
  color: white;
  margin-right: 12px;
}

.save-btn:hover {
  background-color: #24b59f;
}

.cancel-btn {
  background-color: #ddd;
  color: #555;
}

.cancel-btn:hover {
  background-color: #ccc;
}
</style>
