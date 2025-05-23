<template>
  <div style="padding: 1rem; max-width: 800px; margin: auto;">
    <h2>博客管理</h2>

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
        <button @click.stop="goEdit(blog.id)">修改</button>
        <button @click.stop="confirmDelete(blog.id)">删除</button>
      </div>
    </div>

    <div class="pagination">
      <button @click="prevPage" :disabled="page === 1">上一页</button>
      <button @click="nextPage" :disabled="page === pages">下一页</button>
      <span>第 {{ page }} 页 / 共 {{ pages }} 页</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getBlogList, deleteBlog } from '@/api/admin/blog';

const page = ref(1);
const pages = ref(1);
const blogList = ref([]);

const router = useRouter();

async function loadBlogs() {
  try {
    const res = await getBlogList(page.value, 10);
    if (res.code === 0) {
      blogList.value = res.data.records;
      pages.value = res.data.pages;
      page.value = res.data.current;
    } else {
      alert('获取博客列表失败：' + res.message);
    }
  } catch (error) {
    alert('请求博客列表出错');
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
  if (confirm('确定删除该博客？删除后不可恢复！')) {
    try {
      const res = await deleteBlog(id);
      if (res.code === 0) {
        alert('删除成功');
        // 如果当前页删除后无数据且不是第一页，页码减1
        if (blogList.value.length === 1 && page.value > 1) {
          page.value--;
        }
        loadBlogs();
      } else {
        alert('删除失败：' + res.message);
      }
    } catch (error) {
      alert('删除请求失败');
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

<style scoped>
.blog-item {
  border: 1px solid #ddd;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 6px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: default;
}
.blog-item:hover {
  box-shadow: 0 0 8px rgba(0,0,0,0.1);
}
.info {
  flex: 1;
  cursor: pointer;
}
.title {
  font-weight: bold;
  font-size: 1.1rem;
  color: #2de2be;
}
.date {
  font-size: 0.8rem;
  color: #999;
  margin-top: 0.3rem;
}
.summary {
  margin-top: 0.5rem;
  color: #444;
  font-size: 0.9rem;
  overflow-wrap: anywhere;  /* 自动换行，强力断词 */
  word-break: break-word;
  white-space: normal;      /* 允许换行 */
}

.actions button {
  margin-left: 0.5rem;
  padding: 0.4rem 0.8rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.actions button:first-child {
  background-color: #409eff;
  color: white;
}
.actions button:last-child {
  background-color: #f56c6c;
  color: white;
}
.pagination {
  margin-top: 1.5rem;
  text-align: center;
}
.pagination button {
  margin: 0 0.5rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
}
.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
