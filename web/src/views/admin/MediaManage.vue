<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { uploadFile, generateUrl, getMediaList, deleteMedia } from '@/api/admin/media';

const fileInput = ref<HTMLInputElement | null>(null);
const uploading = ref(false);
const error = ref<string | null>(null);
const uploadedUrl = ref<string>('');
const uploadedUrlShower = ref<string>('');
const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:6688';

// 媒体列表相关状态
const mediaList = ref<{
  id: string;
  filename: string;
  storagePath: string;
  uploaderId: string;
  createdAt: string;
  size: string;
  mimeType: string;
  isPublic?: string;
  coverUrl?: string;  // 如果有封面URL字段，可用此
}[]>([]);

const currentPage = ref(1);
const pageSize = 10;
const totalPages = ref(1);
const loadingList = ref(false);
const listError = ref<string | null>(null);

function onSelectFile() {
  if (fileInput.value) {
    fileInput.value.value = '';
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
  uploadedUrlShower.value = '';

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
    uploadedUrl.value = urlRes.data;
    uploadedUrlShower.value = baseUrl + urlRes.data;

    // 上传成功后刷新列表
    await fetchMediaList(currentPage.value, pageSize);
  } catch (e) {
    error.value = '上传过程中出现错误，请稍后重试';
  } finally {
    uploading.value = false;
  }
}

// 提取文件名（支持Windows和Unix路径）
function extractFilename(path: string) {
  if (!path) return '';
  const segments = path.replace(/\\/g, '/').split('/');
  return segments[segments.length - 1];
}

// 复制文本到剪贴板
// function copyToClipboard(text: string) {
//   navigator.clipboard.writeText(text).then(() => {
//     alert('复制成功');
//   }).catch(() => {
//     alert('复制失败，请手动复制');
//   });
// }
function copyToClipboard(text: string) {
  if (navigator.clipboard && window.isSecureContext) {
    // HTTPS 或 localhost
    navigator.clipboard.writeText(text).then(() => {
      alert('复制成功');
    }).catch(() => {
      // 失败则使用回退方法
      fallbackCopy(text);
    });
  } else {
    // HTTP或不支持Clipboard API环境，使用回退方法
    fallbackCopy(text);
  }
}

function fallbackCopy(text: string) {
  const textArea = document.createElement('textarea');
  textArea.value = text;

  // 避免页面滚动
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
    alert(successful ? '复制成功' : '复制失败，请手动复制');
  } catch (err) {
    alert('复制失败，请手动复制');
  }

  document.body.removeChild(textArea);
}

// 获取媒体列表
async function fetchMediaList(page: number, size: number) {
  loadingList.value = true;
  listError.value = null;
  try {
    const res = await getMediaList(page, size);
    if (res.code === 0 || res.code === 200) {
      mediaList.value = res.data.records;
      currentPage.value = res.data.current;
      totalPages.value = res.data.pages;
    } else {
      listError.value = res.message || '获取媒体列表失败';
    }
  } catch {
    listError.value = '请求媒体列表时出错';
  } finally {
    loadingList.value = false;
  }
}

// 删除媒体
async function onDeleteMedia(mediaId: string) {
  if (!confirm('确定要删除该媒体吗？')) return;
  try {
    const res = await deleteMedia(mediaId);
    if (res.code === 0 || res.code === 200) {
      alert('删除成功');
      await fetchMediaList(currentPage.value, pageSize);
    } else {
      alert('删除失败：' + res.message);
    }
  } catch {
    alert('删除请求出错');
  }
}

// 组件挂载时加载第一页列表
onMounted(() => {
  fetchMediaList(currentPage.value, pageSize);
});
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
      <a :href="uploadedUrlShower" target="_blank">{{ uploadedUrlShower }}</a>

      <p>封面访问链接：</p>
      <a :href="uploadedUrl" target="_blank">{{ uploadedUrl }}</a>

      <img
          v-if="uploadedUrl.match(/\.(jpeg|jpg|gif|png|svg)$/i)"
          :src="uploadedUrlShower"
          alt="文件预览"
      />
    </div>

    <hr />

    <h3>媒体文件列表</h3>
    <div v-if="listError" class="error">{{ listError }}</div>
    <div v-if="loadingList">加载中...</div>
    <ul v-if="mediaList.length > 0">
      <li v-for="item in mediaList" :key="item.id" class="media-item">
        <div class="media-info">
          <div>
            <strong>{{ item.filename }}</strong>
            <span> ({{ item.size }}, {{ item.mimeType }})</span>
          </div>

          <div class="links">
            <div>
              文件访问链接：
              <input
                  type="text"
                  readonly
                  :value="`/api/static/${extractFilename(item.storagePath)}`"
                  class="link-input"
              />
              <button
                  @click="copyToClipboard(`/api/static/${extractFilename(item.storagePath)}`)"
              >
                复制
              </button>
            </div>

            <div>
              封面访问链接：
              <input
                  type="text"
                  readonly
                  :value="`/static/${extractFilename(item.coverUrl || item.storagePath)}`"
                  class="link-input"
              />
              <button
                  @click="copyToClipboard(`/static/${extractFilename(item.coverUrl || item.storagePath)}`)"
              >
                复制
              </button>
            </div>
          </div>

          <button @click="onDeleteMedia(item.id)">删除</button>
        </div>

        <div v-if="item.mimeType.match(/^image\//)" class="thumb-container">
          <img
              :src="`/api/static/${extractFilename(item.storagePath)}`"
              alt="缩略图"
              class="thumbnail"
          />
        </div>
      </li>
    </ul>
    <div v-else>暂无媒体文件</div>

    <div class="pagination">
      <button
          :disabled="currentPage === 1"
          @click="fetchMediaList(currentPage - 1, pageSize)"
      >
        上一页
      </button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button
          :disabled="currentPage === totalPages"
          @click="fetchMediaList(currentPage + 1, pageSize)"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<style scoped>
.media-upload-container {
  max-width: 600px;
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
  margin-left: 10px;
}

button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.error {
  margin-top: 15px;
  color: #d93025;
  font-weight: bold;
  text-align: center;
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
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
}

.media-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
  padding: 8px 12px;
  border-bottom: 1px solid #ddd;
}

.media-info {
  flex: 1;
  text-align: left;
}

.links {
  margin: 8px 0;
}

.link-input {
  width: 300px;
  margin-left: 8px;
  padding: 4px 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.thumb-container {
  width: 80px;
  height: 80px;
  margin-left: 10px;
  flex-shrink: 0;
}

.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.pagination {
  margin-top: 20px;
}

.pagination button {
  margin: 0 10px;
}
</style>

