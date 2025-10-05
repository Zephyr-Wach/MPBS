<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { deleteBlog } from '@/api/admin/blog';
import { getBlogList } from '@/api/normal/blog';
import type { BlogListItem } from '@/api/normal/blog';
import {useI18n} from "vue-i18n";

const { t } = useI18n()
const page = ref(1);
const pages = ref(1);
const blogList = ref<BlogListItem[]>([]);

const router = useRouter();

async function loadBlogs() {
  try {
    const res = await getBlogList({page: page.value, size: 10});
    if (res.data.code === 200) {
      blogList.value = res.data.data.records;
      pages.value = res.data.data.pages;
      page.value = res.data.data.current;
    } else {
      alert(t('GetBlogListFailed') +":"+ res.data.message);
    }
  } catch (error) {
    alert(t('GetBlogListFailed'));
    console.error(error);
  }
}

function goDetail(id: string) {
  router.push({ path: `/blog/detail/${id}` });
}

function goEdit(id: string) {
  router.push({ path: `/admin/blogs/edit/${id}` });
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

async function confirmDelete(id: string) {
  if (confirm(t('ConfirmDelBlog'))) {
    try {
      const res = await deleteBlog(id);
      if (res.data.code === 200) {
        alert(t('DelSuccess'));
        // 如果当前页删除后无数据且不是第一页，页码减1
        if (blogList.value.length === 1 && page.value > 1) {
          page.value--;
        }
        loadBlogs();
      } else {
        alert(t('DelFailure') +":"+ res.data.message);
      }
    } catch (error) {
      alert(t('DelFailure'));
      console.error(error);
    }
  }
}

function formatDate(dateStr: string) {
  const date = new Date(dateStr);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}

onMounted(() => {
  loadBlogs();
});
</script>

<template>
  <div style="padding: 1rem; max-width: 800px; margin: auto;">
    <h2>{{t('BlogManage')}}</h2>

    <div
        v-for="blog in blogList"
        :key="blog.id"
        class="blog-item"
    >
      <div class="info" @click="goDetail(blog.id)">
        <div class="title">{{ blog.title }}</div>
        <div class="date">{{ formatDate(blog.createdAt) }}</div>
        <div class="summary">{{ blog.contentMd.slice(0, 100) + (blog.contentMd.length > 100 ? '...' : '') }}</div>
      </div>

      <div class="actions">
        <button @click.stop="goEdit(blog.id)">{{t('change')}}</button>
        <button @click.stop="confirmDelete(blog.id)">{{t('del')}}</button>
      </div>
    </div>

    <div class="pagination">
      <button @click="prevPage" :disabled="page === 1">{{t('theFormer')}}</button>
      <button @click="nextPage" :disabled="page === pages">{{t('theLater')}}</button>
      <span>{{t('page')}} {{ page }}{{t('allPages')}}{{ pages }} {{t('pa')}}</span>
    </div>
  </div>
</template>

<style scoped>
.blog-item {
  border: 1px solid #e5e7eb;
  padding: 1rem;
  margin-bottom: 1.2rem;
  border-radius: 10px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  background: #fff;
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}
.blog-item:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.06);
  transform: translateY(-2px);
}

.info {
  flex: 1;
  cursor: pointer;
  padding-right: 1rem;
}
.title {
  font-weight: 600;
  font-size: 1.2rem;
  color: #1e1e1e;
  margin-bottom: 0.4rem;
  transition: color 0.2s;
}
.title:hover {
  color: #409eff;
}
.date {
  font-size: 0.8rem;
  color: #aaa;
  margin-bottom: 0.5rem;
}
.summary {
  font-size: 0.9rem;
  color: #444;
  overflow-wrap: anywhere;
  word-break: break-word;
  line-height: 1.5;
}

.actions {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.actions button {
  padding: 0.4rem 0.9rem;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.2s;
  white-space: nowrap;
}
.actions button:first-child {
  background: #409eff;
  color: white;
}
.actions button:first-child:hover {
  background: #66b1ff;
}
.actions button:last-child {
  background: #f56c6c;
  color: white;
}
.actions button:last-child:hover {
  background: #f78989;
}

.pagination {
  margin-top: 2rem;
  text-align: center;
  font-size: 0.95rem;
  color: #555;
}
.pagination button {
  margin: 0 0.4rem;
  padding: 0.5rem 1rem;
  border: none;
  background-color: #f0f0f0;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}
.pagination button:hover:not(:disabled) {
  background-color: #d9ecff;
  color: #409eff;
}
.pagination button:disabled {
  background-color: #e0e0e0;
  color: #999;
  cursor: not-allowed;
}

</style>
