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
        <div class="btn-group">
          <button class="btn download-btn" @click="download(file.id)">下载</button>
          <button class="btn share-btn" @click="generateShareLink(file.id)">生成分享链接</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { getFilesList, uploadFile, downloadFile, FilesProcessDTO } from '@/api/user/cloud';
import { getFileShareLink } from '@/api/admin/file';

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

// const generateShareLink = async (fileId: string) => {
//   try {
//     const res = await getFileShareLink(fileId);
//     if (res.code === 0) {
//       const shareUrl = res.data;
//       await navigator.clipboard.writeText(shareUrl);
//       alert('分享链接已复制到剪贴板:\n' + shareUrl);
//     } else {
//       alert('生成分享链接失败: ' + res.msg);
//     }
//   } catch (err) {
//     alert('生成分享链接失败: ' + err);
//   }
// };
const generateShareLink = async (fileId: string) => {
  try {
    const res = await getFileShareLink(fileId);
    if (res.code === 0) {
      const shareUrl = res.data;
      await copyTextToClipboard(shareUrl);  // 调用新复制函数
    } else {
      alert('生成分享链接失败: ' + res.msg);
    }
  } catch (err) {
    alert('生成分享链接失败: ' + err);
  }
};

async function copyTextToClipboard(text: string) {
  if (navigator.clipboard && window.isSecureContext) {
    // HTTPS 或 localhost，优先使用现代API
    try {
      await navigator.clipboard.writeText(text);
      alert('分享链接已复制到剪贴板:\n' + text);
    } catch {
      fallbackCopy(text);
    }
  } else {
    // HTTP等不安全上下文，使用传统方法
    fallbackCopy(text);
  }
}

function fallbackCopy(text: string) {
  const textArea = document.createElement('textarea');
  textArea.value = text;
  textArea.style.position = 'fixed';  // 避免页面滚动
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
  max-width: 600px;
  margin: 40px auto;
  background-color: #fff;
  padding: 32px 40px;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(45, 226, 190, 0.15);
  font-family: "Helvetica Neue", Arial, sans-serif;
  color: #333;
  user-select: none;
}

h2 {
  color: #409eff;
  text-align: center;
  margin-bottom: 28px;
  font-size: 26px;
  font-weight: 700;
  letter-spacing: 1px;
}

.upload-area {
  text-align: center;
  margin-bottom: 32px;
}

.upload-label {
  background-color: #2de2be;
  color: #fff;
  padding: 12px 28px;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 700;
  font-size: 16px;
  box-shadow: 0 4px 12px rgba(45, 226, 190, 0.5);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  user-select: none;
  display: inline-block;
}

.upload-label:hover {
  background-color: #24c9a7;
  box-shadow: 0 6px 16px rgba(36, 201, 167, 0.6);
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
  background: #f5f7fa;
  border-left: 6px solid #409eff;
  margin-bottom: 16px;
  padding: 16px 20px;
  border-radius: 12px;
  box-shadow: 0 3px 12px rgba(64, 158, 255, 0.1);
  transition: transform 0.2s ease, box-shadow 0.3s ease;
}

.file-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.2);
}

.file-info {
  display: flex;
  flex-direction: column;
  max-width: 65%;
  overflow: hidden;
}

.filename {
  font-weight: 700;
  font-size: 16px;
  color: #2b2b2b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.filesize {
  font-size: 13px;
  color: #888;
  margin-top: 4px;
  user-select: text;
}

.btn-group {
  display: flex;
  gap: 12px;
}

.btn {
  padding: 8px 18px;
  border-radius: 8px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  border: none;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  user-select: none;
  box-shadow: 0 3px 8px rgba(0,0,0,0.08);
}

.download-btn {
  background-color: #409eff;
  color: #fff;
}

.download-btn:hover {
  background-color: #307fe8;
  box-shadow: 0 5px 15px rgba(48, 127, 232, 0.6);
}

.share-btn {
  background-color: #2de2be;
  color: #fff;
}

.share-btn:hover {
  background-color: #24c9a7;
  box-shadow: 0 5px 15px rgba(36, 201, 167, 0.6);
}
</style>
