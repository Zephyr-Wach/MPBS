<template>
  <div class="search-wrapper">
    <div class="search-bar">
      <input
          v-model="keyword"
          @input="handleTitleSearch"
          @keyup.enter="handleSearch"
          placeholder="🔍 输入关键词搜索博客"
          class="search-input"
      />
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>

    <ul v-if="showSuggestions" class="suggestion-box">
      <li
          v-for="item in suggestions"
          :key="item.id"
          class="suggestion-item"
          @click="selectSuggestion(item.title)"
      >
        {{ item.title }}
      </li>
    </ul>
  </div>

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
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { getBlogList, searchBlog, searchBlogTitle, BlogTitleItem } from '@/api/public/blog';

const page = ref(1);
const pages = ref(1);
const blogList = ref([]);
const keyword = ref('');
const isSearching = ref(false);
const router = useRouter();
const suggestions = ref<BlogTitleItem[]>([]);
const showSuggestions = ref(false);

const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:6688';

let searchTimer: number | null = null;

function selectSuggestion(title: string) {
  keyword.value = title;
  showSuggestions.value = false;
  handleSearch();
}

function handleTitleSearch() {
  if (searchTimer) clearTimeout(searchTimer);
  searchTimer = window.setTimeout(async () => {
    const kw = keyword.value.trim();
    if (!kw) {
      suggestions.value = [];
      showSuggestions.value = false;
      return;
    }
    try {
      const res = await searchBlogTitle(kw);
      if (res.code === 0) {
        suggestions.value = res.data.records.slice(0, 5);
        showSuggestions.value = suggestions.value.length > 0;
      }
    } catch (e) {
      console.error('搜索联想失败', e);
      showSuggestions.value = false;
    }
  }, 300);
}

function onClickOutside(event: MouseEvent) {
  const target = event.target as HTMLElement;
  if (!target.closest('.search-wrapper')) {
    showSuggestions.value = false;
  }
}

onMounted(() => {
  document.addEventListener('click', onClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', onClickOutside);
});


function getFullCoverUrl(url) {
  if (!url) return '';
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  return baseUrl + url;
}

function goDetail(id) {
  router.push({ path: `/blog/detail/${id}` });
}

function handleSearch() {
  page.value = 1;
  isSearching.value = !!keyword.value.trim();
  loadBlogs();
}

async function loadBlogs() {
  try {
    let res;
    if (isSearching.value && keyword.value.trim()) {
      res = await searchBlog(keyword.value.trim(), page.value, 10);
    } else {
      res = await getBlogList(page.value, 10);
    }
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
<style scoped>
.search-wrapper {
  max-width: 75vw;
  margin: 1rem auto;
  position: relative;
  z-index: 10;
}

.search-bar {
  display: flex;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #ddd;
  background-color: #fff;
}

.search-input {
  flex: 1;
  padding: 0.6rem 1rem;
  border: none;
  font-size: 1rem;
  outline: none;
}

.search-btn {
  background-color: #2de2be;
  color: #fff;
  padding: 0 1rem;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background-color: #22c2a6;
}

.suggestion-box {
  position: absolute;
  top: calc(100% + 0.25rem);
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  max-height: 240px;
  overflow-y: auto;
  list-style: none;
  padding: 0;
  margin: 0.25rem 0 0;
}
.suggestion-box {
  padding: 8px 0;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  max-height: 240px;
  overflow-y: auto;
  list-style: none;
  margin: 0;
  /* 禁止默认padding和margin */
  box-sizing: border-box;
}

.suggestion-item {
  padding: 8px 16px;
  cursor: pointer;
  transition: background-color 0.2s, box-shadow 0.2s;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  -webkit-line-clamp: 2;
  text-overflow: ellipsis;
  word-break: break-word;
  margin: 0;

  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
  background-color: #fff;
  line-height: 1.4em;
  max-height: 2.8em;
  white-space: normal;
}

.suggestion-item + .suggestion-item {
  margin-top: 4px; /* 只让相邻条目间有间距 */
}

.suggestion-item:hover {
  background-color: #f2f9f7;
  box-shadow: 0 2px 6px rgba(34, 194, 166, 0.3);
}

</style>
