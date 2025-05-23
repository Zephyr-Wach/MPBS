<script setup lang="ts">
import { ref } from 'vue';
import { uploadFile, generateUrl } from '@/api/public/media';

const fileInput = ref<HTMLInputElement | null>(null);
const uploading = ref(false);
const error = ref<string | null>(null);
const uploadedUrl = ref<string>('');  // 最终访问的完整URL

const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:6688'; // 服务器基础URL，建议用环境变量

function onSelectFile() {
  if (fileInput.value) {
    fileInput.value.value = ''; // 清空上次选中文件
    fileInput.value.click();
  }
}

async function onFileChange(event: Event) {
  const files = (event.target as HTMLInputElement).files;
  if (!files || files.length === 0) return;

  const file = files[0];
  uploading.value = true;
  error.value = null;
  uploadedUrl.value = '';

  try {
    // 1. 上传文件
    const uploadRes = await uploadFile(file);
    if (uploadRes.code !== 0) {
      error.value = '上传失败：' + uploadRes.message;
      return;
    }

    // 2. 生成访问URL
    const urlRes = await generateUrl(uploadRes.data.id);
    if (urlRes.code !== 0) {
      error.value = '生成URL失败：' + urlRes.message;
      return;
    }

    // 3. 拼接完整URL
    uploadedUrl.value = baseUrl + urlRes.data;
  } catch (e) {
    error.value = '上传过程中出现错误，请稍后重试';
  } finally {
    uploading.value = false;
  }
}
</script>

<template>
  <div class="media-upload-container">
    <h2>媒体上传</h2>

    <button @click="onSelectFile" :disabled="uploading">
      {{ uploading ? '上传中...' : '选择文件上传' }}
    </button>
    <input
        ref="fileInput"
        type="file"
        style="display:none"
        @change="onFileChange"
    />

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="uploadedUrl" class="preview">
      <p>文件访问链接：</p>
      <a :href="uploadedUrl" target="_blank">{{ uploadedUrl }}</a>

      <!-- 简单图片预览，若不是图片不会显示 -->
      <img v-if="uploadedUrl.match(/\.(jpeg|jpg|gif|png|svg)$/i)" :src="uploadedUrl" alt="文件预览" />
    </div>
  </div>
</template>

<style scoped>
.media-upload-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
  text-align: center;
}

button {
  background-color: #409eff;
  border: none;
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 6px;
  cursor: pointer;
}

button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.error {
  margin-top: 15px;
  color: #d93025;
  font-weight: bold;
}

.preview {
  margin-top: 20px;
  text-align: left;
  word-break: break-all;
}

.preview img {
  max-width: 100%;
  margin-top: 10px;
  border-radius: 6px;
  box-shadow: 0 0 8px rgba(0,0,0,0.1);
}
</style>
