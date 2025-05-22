<template>
  <div class="cloud">
    <h2>文件列表</h2>
    <input type="file" @change="handleFileChange" />
    <ul>
      <li v-for="file in files" :key="file.id">
        {{ file.filename }} - {{ file.size }} bytes
        <button @click="download(file.id)">下载</button>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { getFilesList, uploadFile, downloadFile, FilesProcessDTO } from '@/api/user/cloud';

const files = ref<FilesProcessDTO[]>([]);

// 获取文件列表
const loadFiles = async () => {
  try {
    const res = await getFilesList();
    // 假设接口返回格式是 { code: 0, msg: "success", data: [...] }
    if (res.code === 0) {
      files.value = res.data;
    } else {
      alert('获取文件列表失败: ' + res.msg);
    }
  } catch (err) {
    alert('获取文件列表失败: ' + err);
  }
};

// 处理上传文件
const handleFileChange = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (!target.files || target.files.length === 0) return;

  try {
    const file = target.files[0];
    const res = await uploadFile(file);
    if (res.code === 0) {
      alert('上传成功');
      loadFiles(); // 上传成功后刷新列表
    } else {
      alert('上传失败: ' + res.msg);
    }
  } catch (err) {
    alert('上传失败: ' + err);
  }
};

// 下载文件
const download = async (id: string) => {
  try {
    const response = await downloadFile(id);
    if (response) {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      // 这里可根据files找对应文件名或者用id
      const fileInfo = files.value.find(f => f.id === id);
      link.setAttribute('download', fileInfo?.filename || 'file');
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    }
  } catch (err) {
    alert('下载失败: ' + err);
  }
};

loadFiles();
</script>

<style scoped>
.cloud {
  padding: 20px;
}
</style>
