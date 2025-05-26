<template>
  <div v-if="blog" class="blog-detail-container">
    <h1 class="title">{{ blog.title }}</h1>
    <small class="date">{{ formatDate(blog.createdAt) }}</small>
    <img
        v-if="blog.coverUrl"
        :src="getFullCoverUrl(blog.coverUrl)"
        alt="封面"
        class="cover-image"
    />
    <div class="content" v-html="blog.contentHtml || markdownToHtml(blog.contentMd)"></div>
    <CommentSection :postId="blog?.id" />
  </div>
  <div v-else class="loading">加载中...</div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getBlogDetail } from '@/api/public/blog';
import { marked } from 'marked';
import CommentSection from '@/components/CommentSection.vue';

const blog = ref<null | {
  id: string;
  title: string;
  coverUrl?: string;
  createdAt: string;
  contentMd: string;
  contentHtml?: string;
}>(null);

const route = useRoute();
const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:6688';

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
  margin: 2.5rem auto;
  padding: 1.5rem 2rem;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  background-color: #f9fafa;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  color: #333;
}

.title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2de2be;
  margin-bottom: 0.3rem;
  user-select: text;
}

.date {
  display: block;
  color: #409eff;
  font-size: 0.9rem;
  margin-bottom: 1.2rem;
  user-select: none;
}

.cover-image {
  max-width: 100%;
  border-radius: 8px;
  margin-bottom: 1.8rem;
  box-shadow: 0 2px 8px rgba(45, 226, 190, 0.3);
  transition: transform 0.3s ease;
  cursor: pointer;
}
.cover-image:hover {
  transform: scale(1.03);
}

.content {
  max-width: 100%;
  width: 100%;
  box-sizing: border-box;
  text-align: left;
  line-height: 1.75;
  font-size: 1.1rem;
  color: #444;
  border-top: 1px solid #ddd;
  padding-top: 1.5rem;
  user-select: text;
  overflow-wrap: break-word;
  white-space: normal;
  word-break: break-word;
}
.content * {
  white-space: normal !important;
  overflow-wrap: anywhere !important;
  word-break: break-word !important;
  box-sizing: border-box;
}

.content p {
  margin-bottom: 1rem;
}

.content img {
  max-width: 100%;
  height: auto;
  display: block;
  border-radius: 6px;
  margin: 1rem 0;
  box-shadow: 0 1px 5px rgba(64, 158, 255, 0.2);
}
/* 👇 修复 <pre><code> 内容不换行的问题 */
.content pre {
  white-space: pre-wrap;
  word-break: break-word;
  overflow-wrap: anywhere;
}
.content code {
  white-space: pre-wrap;
  word-break: break-word;
  overflow-wrap: anywhere;
}
.loading {
  text-align: center;
  color: #bbb;
  font-style: italic;
  padding: 3rem 0;
  user-select: none;
}

</style>
