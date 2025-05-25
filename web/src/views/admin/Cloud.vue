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
          <span class="filename" :title="file.filename">{{ file.filename }}</span>
          <span class="filesize">{{ file.size }} bytes</span>
        </div>
        <div class="btn-group">
          <button class="btn download-btn" @click="download(file.id)">下载</button>
          <button class="btn share-btn" @click="generateShareLink(file.id)">生成分享链接</button>
          <button class="btn del-btn" @click="delfile(file.id)">删除</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { getFilesList, uploadFile, downloadFile, FilesProcessDTO } from '@/api/admin/cloud';
import { getFileShareLink, delFile } from '@/api/admin/cloud';

const files = ref<FilesProcessDTO[]>([]);

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

const generateShareLink = async (fileId: string) => {
  try {
    const res = await getFileShareLink(fileId);
    if (res.code === 0) {
      const shareUrl = res.data;
      await copyTextToClipboard(shareUrl);
    } else {
      alert('生成分享链接失败: ' + res.msg);
    }
  } catch (err) {
    alert('生成分享链接失败: ' + err);
  }
};

const delfile = async (fileId: string) => {
  try {
    const res = await delFile(fileId);
    if (res.code === 0) {
      alert('删除成功');
      loadFiles();
    } else {
      alert('删除失败: ' + res.msg);
    }
  } catch (err) {
    alert('删除失败: ' + err);
  }
};

async function copyTextToClipboard(text: string) {
  if (navigator.clipboard && window.isSecureContext) {
    try {
      await navigator.clipboard.writeText(text);
      alert('分享链接已复制到剪贴板:\n' + text);
    } catch {
      fallbackCopy(text);
    }
  } else {
    fallbackCopy(text);
  }
}

function fallbackCopy(text: string) {
  const textArea = document.createElement('textarea');
  textArea.value = text;
  textArea.style.position = 'fixed';
  textArea.style.top = '0';
  textArea.style.left = '0';
  textArea.style.width = '2em';
  textArea.style.height = '2em';
  textArea.style.padding = '0';
  textArea.style.border = 'none';
  textArea.style.outline = 'none';
  textArea.style.boxShadow = 'none';
  textArea.style.background = 'transparent';

  document.body.appendChild(textArea);
  textArea.select();

  try {
    const successful = document.execCommand('copy');
    alert(successful ? '分享链接已复制到剪贴板:\n' + text : '复制失败，请手动复制链接');
  } catch {
    alert('复制失败，请手动复制链接');
  }

  document.body.removeChild(textArea);
}

loadFiles();
</script>

<style scoped>
.cloud {
  max-width: 640px;
  margin: 40px auto;
  background-color: #fff;
  padding: 36px 48px;
  border-radius: 18px;
  box-shadow: 0 12px 36px rgba(45, 226, 190, 0.18);
  font-family: "Helvetica Neue", Arial, sans-serif;
  color: #2b2b2b;
  user-select: none;
}

h2 {
  color: #409eff;
  text-align: center;
  margin-bottom: 32px;
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 1.2px;
}

.upload-area {
  text-align: center;
  margin-bottom: 36px;
}

.upload-label {
  background: linear-gradient(135deg, #2de2be 0%, #25c9a0 100%);
  color: #fff;
  padding: 14px 32px;
  border-radius: 14px;
  cursor: pointer;
  font-weight: 700;
  font-size: 17px;
  box-shadow: 0 6px 14px rgba(45, 226, 190, 0.7);
  user-select: none;
  display: inline-block;
  transition: background 0.3s ease, box-shadow 0.3s ease;
}

.upload-label:hover {
  background: linear-gradient(135deg, #24c9a7 0%, #1ea58a 100%);
  box-shadow: 0 8px 20px rgba(36, 201, 167, 0.8);
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
  background: #f0f2f5;
  border-left: 6px solid #409eff;
  margin-bottom: 18px;
  padding: 18px 24px;
  border-radius: 14px;
  box-shadow: 0 5px 16px rgba(64, 158, 255, 0.12);
  transition: transform 0.25s ease, box-shadow 0.35s ease;
}

.file-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 30px rgba(64, 158, 255, 0.22);
}

.file-info {
  display: flex;
  flex-direction: column;
  max-width: 65%;
  overflow: hidden;
}

.filename {
  font-weight: 700;
  font-size: 17px;
  color: #2b2b2b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.filesize {
  font-size: 14px;
  color: #888;
  margin-top: 6px;
  user-select: text;
}

.btn-group {
  display: flex;
  gap: 14px;
}

.btn {
  padding: 10px 20px;
  border-radius: 10px;
  font-weight: 600;
  font-size: 15px;
  cursor: pointer;
  border: none;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  user-select: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  min-width: 100px;
  text-align: center;
}

.download-btn {
  background-color: #409eff;
  color: #fff;
  box-shadow: 0 6px 18px rgba(64, 158, 255, 0.6);
}

.download-btn:hover {
  background-color: #307fe8;
  box-shadow: 0 8px 24px rgba(48, 127, 232, 0.85);
}

.share-btn {
  background-color: #2de2be;
  color: #fff;
  box-shadow: 0 6px 18px rgba(45, 226, 190, 0.6);
}

.share-btn:hover {
  background-color: #24c9a7;
  box-shadow: 0 8px 24px rgba(36, 201, 167, 0.85);
}

.del-btn {
  background-color: #ddd;
  color: #555;
  box-shadow: 0 4px 12px rgba(153, 153, 153, 0.4);
  transition: background-color 0.3s ease, box-shadow 0.3s ease, color 0.3s ease;
}

.del-btn:hover {
  background-color: #bbb;
  color: #222;
  box-shadow: 0 6px 20px rgba(136, 136, 136, 0.7);
}
</style>
