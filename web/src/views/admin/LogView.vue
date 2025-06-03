<template>
  <div
      class="card"
      style="padding:16px; max-width: 1000px; margin: 20px auto; border:1px solid #ccc; border-radius:4px;"
  >
    <form
        @submit.prevent="fetchLogs"
        style="display:flex; flex-wrap:wrap; gap:16px; align-items:center;"
    >
      <div>
        <label>用户账号：</label>
        <input v-model="filters.userAccount" placeholder="请输入用户账号" />
      </div>
      <div>
        <label>操作类型：</label>
        <input v-model="filters.operationType" placeholder="请输入操作类型" />
      </div>
      <div>
        <label>开始时间：</label>
        <input type="datetime-local" v-model="filters.startTime" />
      </div>
      <div>
        <label>结束时间：</label>
        <input type="datetime-local" v-model="filters.endTime" />
      </div>
      <div>
        <button type="submit">查询</button>
        <button type="button" @click="resetFilters">重置</button>
      </div>
    </form>

    <table
        border="1"
        cellspacing="0"
        cellpadding="8"
        style="width: 100%; margin-top: 20px; border-collapse: collapse;"
    >
      <thead>
      <tr>
        <th>用户账号</th>
        <th>操作类型</th>
        <th>操作时间</th>
        <th>源IP</th>
        <th>目标IP</th>
        <th>源端口</th>
        <th>客户端硬件</th>
        <th>操作详情</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="log in logs" :key="log.id">
        <td>{{ log.userAccount }}</td>
        <td>{{ log.operationType }}</td>
        <td>{{ formatDateTime(log.operationTime) }}</td>
        <td>{{ log.sourceIp }}</td>
        <td>{{ log.targetIp }}</td>
        <td>{{ log.sourcePort }}</td>
        <td>{{ log.clientHardware }}</td>
        <td>{{ log.operationDetail }}</td>
      </tr>
      <tr v-if="logs.length === 0">
        <td colspan="8" style="text-align: center;">暂无数据</td>
      </tr>
      </tbody>
    </table>

    <div
        style="margin-top: 12px; display: flex; justify-content: flex-end; gap: 12px; align-items: center;"
    >
      <button :disabled="page === 1" @click="handlePageChange(page - 1)">
        上一页
      </button>
      <span>第 {{ page }} 页 / 共 {{ totalPages }} 页</span>
      <button :disabled="page === totalPages" @click="handlePageChange(page + 1)">
        下一页
      </button>
      <select v-model.number="pageSize" @change="handleSizeChange">
        <option :value="10">10</option>
        <option :value="20">20</option>
        <option :value="50">50</option>
      </select>
    </div>
  </div>
</template>

<script setup>
import {ref, computed} from 'vue';
import {getLogsList} from '@/api/admin/log';

const filters = ref({
  userAccount: '',
  operationType: '',
  startTime: '',
  endTime: '',
});

const logs = ref([]);
const total = ref(0);
const page = ref(1);
const pageSize = ref(10);

const totalPages = computed(() => {
  return Math.ceil(total.value / pageSize.value) || 1;
});

function formatDateTime(value) {
  if (!value) return '';
  const date = new Date(value);
  return date.toLocaleString();
}

async function fetchLogs() {
  const params = {
    userAccount: filters.value.userAccount || undefined,
    operationType: filters.value.operationType || undefined,
    startTime: filters.value.startTime
        ? filters.value.startTime.replace('T', ' ') + ':00'
        : undefined,
    endTime: filters.value.endTime
        ? filters.value.endTime.replace('T', ' ') + ':00'
        : undefined,
    page: page.value,
    size: pageSize.value,
  };

  try {
    const res = await getLogsList(params);
    // 根据你返回的字段，赋值
    logs.value = res.records || [];
    total.value = res.total || 0;
    page.value = res.current || 1;
    pageSize.value = res.size || 10;
  } catch (error) {
    console.error('获取日志失败:', error);
    logs.value = [];
    total.value = 0;
  }
}

function resetFilters() {
  filters.value.userAccount = '';
  filters.value.operationType = '';
  filters.value.startTime = '';
  filters.value.endTime = '';
  page.value = 1;
  pageSize.value = 10;
  fetchLogs();
}

function handlePageChange(newPage) {
  if (newPage < 1 || newPage > totalPages.value) return;
  page.value = newPage;
  fetchLogs();
}

function handleSizeChange(event) {
  pageSize.value = event.target.value;
  page.value = 1;
  fetchLogs();
}

// 组件挂载时自动加载一次数据
fetchLogs();
</script>
<style scoped>
.card {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 16px;
  max-width: 100%;
  margin: 20px auto;
  box-shadow: 0 2px 10px rgba(64, 158, 255, 0.1);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

/* 表单样式 */
form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

form > div {
  display: flex;
  flex-direction: column;
  font-weight: 600;
  color: #409eff;
}

form label {
  margin-bottom: 4px;
  font-size: 0.9rem;
}

form input[type="text"],
form input[type="datetime-local"] {
  padding: 6px 10px;
  border-radius: 8px;
  border: 1.5px solid #ddd;
  font-size: 0.95rem;
  outline: none;
  transition: border-color 0.3s ease;
  width: 160px;
}

form input[type="text"]:focus,
form input[type="datetime-local"]:focus {
  border-color: #33AAEE;
  box-shadow: 0 0 6px #33AAEE;
}

/* 按钮组 */
form div:last-child {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

form button {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  font-size: 1rem;
  transition: background-color 0.3s ease;
  user-select: none;
}

form button:hover {
  background-color: #33AAEE;
}

form button[type="button"] {
  background-color: #ddd;
  color: #555;
}

form button[type="button"]:hover {
  background-color: #bbb;
}

/* 表格样式 */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  font-size: 0.95rem;
}

thead tr {
  background-color: #33AAEE;
  color: white;
  font-weight: 700;
}

th, td {
  padding: 12px 10px;
  border: 1px solid #ddd;
  text-align: center;
  vertical-align: middle;
}

tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

tbody tr:hover {
  background-color: #e0f7f1;
  cursor: default;
}

tbody tr td {
  color: #333;
}

/* 无数据行 */
tbody tr td[colspan] {
  font-style: italic;
  color: #999;
}

/* 分页控制 */
div[style*="justify-content: flex-end"] {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  align-items: center;
  font-size: 0.9rem;
  color: #444;
}

div[style*="justify-content: flex-end"] button {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

div[style*="justify-content: flex-end"] button:disabled {
  background-color: #ddd;
  color: #999;
  cursor: not-allowed;
}

div[style*="justify-content: flex-end"] button:not(:disabled):hover {
  background-color: #33AAEE;
}

div[style*="justify-content: flex-end"] select {
  padding: 6px 10px;
  border-radius: 10px;
  border: 1.5px solid #ddd;
  font-weight: 600;
  cursor: pointer;
  outline: none;
  transition: border-color 0.3s ease;
}

div[style*="justify-content: flex-end"] select:focus {
  border-color: #33AAEE;
  box-shadow: 0 0 6px #33AAEE;
}

</style>
