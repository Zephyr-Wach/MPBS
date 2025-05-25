<template>
  <div style="padding: 1rem; background-color: #f7f7f7;">
    <div
        v-for="blog in blogList"
        :key="blog.id"
        @click="goDetail(blog.id)"
        style="
        border: 1px solid #ddd;
        border-radius: 10px;
        padding: 1rem;
        margin-bottom: 1.5rem;
        background-color: #fff;
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        cursor: pointer;
        max-width: 75vw;
        aspect-ratio: 5 / 2;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
      "
    >
      <div style="font-size: 1.1rem; font-weight: bold; color: #2de2be;">
        {{ blog.title }}
        <span style="font-size: 0.8rem; color: #999;"> —— {{ formatDate(blog.createdAt) }}</span>
      </div>

      <div v-if="blog.coverUrl" style="margin: 1rem 0; flex-grow: 1; display: flex; justify-content: center; align-items: center;">
        <img
            :src="getFullCoverUrl(blog.coverUrl)"
            alt="封面"
            style="
            max-height: 100%;
            max-width: 100%;
            object-fit: cover;
            border-radius: 6px;
            width: auto;
            height: auto;
            max-height: 60%;
          "
        />
      </div>

      <div style="color: #444; font-size: 0.95rem; line-height: 1.4; overflow: hidden; text-overflow: ellipsis;">
        {{ blog.contentMd.slice(0, 100) + (blog.contentMd.length > 100 ? '...' : '') }}
      </div>
    </div>

    <div style="margin-top: 2rem; text-align: center;">
      <button
          @click="prevPage"
          :disabled="page === 1"
          :style="page === 1 ? disabledBtnStyle : btnStyle"
      >
        上一页
      </button>
      <button
          @click="nextPage"
          :disabled="page === pages"
          :style="page === pages ? disabledBtnStyle : btnStyle"
      >
        下一页
      </button>
      <span style="margin-left: 1rem; color: #666;">
        第 <span style="color: #2de2be; font-weight: bold;">{{ page }}</span> 页 / 共 {{ pages }} 页
      </span>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getBlogList } from '@/api/public/blog';

const page = ref(1);
const pages = ref(1);
const blogList = ref([]);

const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:6688';

function getFullCoverUrl(url) {
  if (!url) return '';
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  return baseUrl + url;
}

const router = useRouter();

function goDetail(id) {
  router.push({ path: `/blog/detail/${id}` });
}

async function loadBlogs() {
  try {
    const res = await getBlogList(page.value, 10);
    if (res.code === 0) {
      blogList.value = res.data.records;
      pages.value = res.data.pages;
    } else {
      alert('获取博客列表失败：' + res.message);
    }
  } catch (error) {
    alert('请求博客列表出错');
    console.error(error);
  }
}

function prevPage() {
  if (page.value > 1) {
    page.value--;
    loadBlogs();
  }
}

function nextPage() {
  if (page.value < pages.value) {
    page.value++;
    loadBlogs();
  }
}

function formatDate(dateStr) {
  const date = new Date(dateStr);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}

onMounted(() => {
  loadBlogs();
});

const btnStyle = {
  padding: '0.5rem 1rem',
  backgroundColor: '#409eff',
  color: '#fff',
  border: 'none',
  borderRadius: '5px',
  margin: '0 0.5rem',
  cursor: 'pointer',
};

const disabledBtnStyle = {
  ...btnStyle,
  backgroundColor: '#ccc',
  cursor: 'not-allowed',
};
</script>
