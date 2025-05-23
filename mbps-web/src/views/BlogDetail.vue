<template>
  <div v-if="blog" class="blog-detail-container">
    <h1>{{ blog.title }}</h1>
    <small>{{ formatDate(blog.createdAt) }}</small>
    <img
        v-if="blog.coverUrl"
        :src="getFullCoverUrl(blog.coverUrl)"
        alt="封面"
        style="max-width: 100%; margin: 1rem 0;"
    />
    <!-- contentHtml 优先显示，如果没有则显示 contentMd -->
    <div v-html="blog.contentHtml || markdownToHtml(blog.contentMd)"></div>
  </div>
  <div v-else class="loading">加载中...</div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getBlogDetail } from '@/api/admin/blog';
import { marked } from 'marked';

const blog = ref<null | {
  id: string;
  title: string;
  coverUrl?: string;
  createdAt: string;
  contentMd: string;
  contentHtml?: string;
}>(null);

const route = useRoute();
const baseUrl = 'http://localhost:6688';

function getFullCoverUrl(url: string) {
  if (!url) return '';
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  return baseUrl + url;
}

function formatDate(dateStr: string) {
  const date = new Date(dateStr);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}

function markdownToHtml(md: string) {
  return marked(md);
}

const loadDetail = async () => {
  const id = route.params.id as string;
  const res = await getBlogDetail(id);
  if (res.code === 0) {
    blog.value = res.data;
  } else {
    alert('获取文章详情失败：' + res.message);
  }
};

onMounted(loadDetail);
</script>

<style scoped>
.blog-detail-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 1rem;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.loading {
  text-align: center;
  color: #888;
  font-style: italic;
}
</style>
