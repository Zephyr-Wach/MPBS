<template>
  <div class="cloud">
    <h2>文件列表</h2>
    <div class="upload-area">
      <label class="upload-label">
        📁 上传文件
        <input type="file" @change="handleFileChange" hidden />
      </label>
    </div>

    <ul class="file-list">
      <li v-for="file in files" :key="file.id" class="file-item">
        <div class="file-info">
          <span class="filename">{{ file.filename }}</span>
          <span class="filesize">{{ file.size }} bytes</span>
        </div>
        <button class="download-btn" @click="download(file.id)">下载</button>
        <button class="share-btn" @click="generateShareLink(file.id)">生成分享链接</button>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { getFilesList, uploadFile, downloadFile, FilesProcessDTO } from '@/api/user/cloud';
import {getFileShareLink } from '@/api/admin/file';

const files = ref<FilesProcessDTO[]>([]);

// 获取文件列表
const loadFiles = async () => {
  try {
    const res = await getFilesList();
    if (res.code === 0) {
      files.value = res.data;
    } else {
      alert('获取文件列表失败: ' + res.msg);
    }
  } catch (err) {
    alert('获取文件列表失败: ' + err);
  }
};

// 上传处理
const handleFileChange = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (!target.files || target.files.length === 0) return;

  try {
    const file = target.files[0];
    const res = await uploadFile(file);
    if (res.code === 0) {
      alert('上传成功');
      loadFiles();
    } else {
      alert('上传失败: ' + res.msg);
    }
  } catch (err) {
    alert('上传失败: ' + err);
  }
};

const download = async (id: string) => {
  try {
    const blob = await downloadFile(id);
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    const fileInfo = files.value.find(f => f.id === id);
    link.href = url;
    link.setAttribute('download', fileInfo?.filename || 'file');
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
  } catch (err) {
    alert('下载失败: ' + err);
  }
};
// 生成分享链接并复制到剪贴板
const generateShareLink = async (fileId: string) => {
  try {
    const res = await getFileShareLink(fileId);
    if (res.code === 0) {
      const shareUrl = res.data; // 后端返回的分享链接
      await navigator.clipboard.writeText(shareUrl);
      alert('分享链接已复制到剪贴板:\n' + shareUrl);
    } else {
      alert('生成分享链接失败: ' + res.msg);
    }
  } catch (err) {
    alert('生成分享链接失败: ' + err);
  }
};
loadFiles();
</script>

<style scoped>
.cloud {
  max-width: 600px;
  margin: 40px auto;
  background-color: white;
  padding: 30px 40px;
  border-radius: 16px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  font-family: "Helvetica Neue", sans-serif;
  color: #333;
}

h2 {
  color: #409eff;
  text-align: center;
  margin-bottom: 24px;
  font-size: 24px;
}

.upload-area {
  text-align: center;
  margin-bottom: 24px;
}

.upload-label {
  background-color: #2de2be;
  color: white;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
  display: inline-block;
}

.upload-label:hover {
  background-color: #28c7aa;
}

.file-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.file-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f9f9f9;
  border-left: 4px solid #409eff;
  margin-bottom: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  transition: box-shadow 0.3s ease;
}

.file-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.file-info {
  display: flex;
  flex-direction: column;
}

.filename {
  font-weight: 600;
}

.filesize {
  font-size: 12px;
  color: #666;
}

.download-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 6px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.download-btn:hover {
  background-color: #307fe8;
}
</style>
