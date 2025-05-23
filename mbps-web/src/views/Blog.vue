<template>
  <div>
    <div v-for="blog in blogList" :key="blog.id" class="blog-item" style="margin-bottom: 1rem; border-bottom: 1px solid #eee; padding-bottom: 1rem;">
      <img
          v-if="blog.coverUrl"
          :src="getFullCoverUrl(blog.coverUrl)"
          alt="封面"
          style="width: 150px; height: 100px; object-fit: cover; float: right; margin-left: 1rem;"
      />
      <h3>{{ blog.title }}</h3>
      <small>{{ formatDate(blog.createdAt) }}</small>
      <p>{{ blog.contentMd.slice(0, 100) + (blog.contentMd.length > 100 ? '...' : '') }}</p>
      <div style="clear: both;"></div>
    </div>

    <button @click="prevPage" :disabled="page === 1">上一页</button>
    <button @click="nextPage" :disabled="page === pages">下一页</button>
    <span> 第 {{ page }} 页 / 共 {{ pages }} 页 </span>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getBlogList } from '@/api/admin/blog';

const page = ref(1);
const pages = ref(1);
const blogList = ref<Array<{
  id: string;
  title: string;
  coverUrl?: string;
  createdAt: string;
  contentMd: string;
}>>([]);

const baseUrl = 'http://localhost:6688'; // 你服务器地址，注意端口

function getFullCoverUrl(url: string) {
  if (!url) return '';
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  return baseUrl + url;
}

const loadBlogs = async () => {
  const res = await getBlogList(page.value, 10);
  if (res.code === 0) {
    blogList.value = res.data.records;
    pages.value = res.data.pages;
  } else {
    alert('获取博客列表失败：' + res.message);
  }
};

const prevPage = () => {
  if (page.value > 1) {
    page.value--;
    loadBlogs();
  }
};

const nextPage = () => {
  if (page.value < pages.value) {
    page.value++;
    loadBlogs();
  }
};

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
};

onMounted(loadBlogs);
</script>
