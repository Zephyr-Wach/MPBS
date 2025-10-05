<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';
import katex from 'katex';
import 'katex/dist/katex.min.css';
import { getCollectionList, searchCollection, getNotesInCollection, getNote } from '@/api/normal/gather.ts';

// 初始化 markdown-it
const md = new MarkdownIt({
  highlight: (code, lang) => {
    return hljs.highlight(code, { language: lang || 'plaintext' }).value;
  },
});

// 合集列表
const collections = ref<any[]>([]);
const selectedNoteId = ref<string | null>(null);
// 笔记内容
const noteContent = ref<string>('');
// 搜索关键字
const searchKeyword = ref<string>('');
// 合集的笔记列表
const collectionNotes = ref<Record<string, any[]>>({});

// 渲染 LaTeX 公式
const renderMath = (content: string) => {
  let rendered = content;
  // 渲染 $$...$$ 块公式
  rendered = rendered.replace(/\$\$([\s\S]*?)\$\$/g, (_, tex) => {
    try {
      return katex.renderToString(tex.trim(), { displayMode: true, throwOnError: false });
    } catch (e) {
      return `<span class="text-red-500">LaTeX Error: ${e.message}</span>`;
    }
  });
  // 渲染 $...$ 行内公式
  rendered = rendered.replace(/\$([^\$]+)\$/g, (_, tex) => {
    try {
      return katex.renderToString(tex.trim(), { displayMode: false, throwOnError: false });
    } catch (e) {
      return `<span class="text-red-500">LaTeX Error: ${e.message}</span>`;
    }
  });
  return rendered;
};

// 获取合集列表
const fetchCollections = async () => {
  try {
    const res = await getCollectionList();
    collections.value = res.data.data;
    // 初始化每个合集的笔记列表
    for (const collection of collections.value) {
      collectionNotes.value[collection.id] = [];
      await fetchNotes(collection.id);
    }
  } catch (error) {
    console.error('获取合集失败:', error);
  }
};

// 搜索合集
const searchCollections = async () => {
  try {
    if (searchKeyword.value.trim() === '') {
      fetchCollections();
    } else {
      const res = await searchCollection(searchKeyword.value);
      collections.value = res.data.data.records;
      // 更新搜索结果的笔记列表
      collectionNotes.value = {};
      for (const collection of collections.value) {
        collectionNotes.value[collection.id] = [];
        await fetchNotes(collection.id);
      }
    }
  } catch (error) {
    console.error('搜索合集失败:', error);
  }
};

// 获取笔记列表
const fetchNotes = async (collectionId: string) => {
  try {
    const res = await getNotesInCollection(collectionId);
    collectionNotes.value[collectionId] = res.data.data;
    // 如果当前合集没有选中的笔记，自动选择第一个笔记
    if (res.data.data.length > 0 && !selectedNoteId.value) {
      selectedNoteId.value = res.data.data[0].noteId;
    }
  } catch (error) {
    console.error('获取笔记列表失败:', error);
  }
};

// 获取笔记内容
const fetchNoteContent = async (noteId: string) => {
  try {
    const res = await getNote(noteId);
    const rawContent = res.data.data.contentMd;
    // 使用 markdown-it 解析 Markdown
    const htmlContent = md.render(rawContent);
    // 渲染 LaTeX 公式
    noteContent.value = renderMath(htmlContent);
  } catch (error) {
    console.error('获取笔记内容失败:', error);
    noteContent.value = '<p>加载笔记失败</p>';
  }
};

// 复制代码
const copyCode = (event: Event) => {
  const button = event.target as HTMLElement;
  const codeBlock = button.nextElementSibling?.querySelector('code');
  if (codeBlock) {
    const code = codeBlock.innerText;
    navigator.clipboard.writeText(code).then(() => {
      button.textContent = '已复制';
      setTimeout(() => {
        button.textContent = '复制';
      }, 2000);
    });
  }
};

// 监听笔记选择变化
watch(selectedNoteId, (newId) => {
  if (newId) {
    fetchNoteContent(newId);
  }
});

// 初始化
onMounted(() => {
  fetchCollections();
});
</script>

<template>
  <div class="flex h-full bg-gray-100">
    <!-- 左侧合集和笔记列表 -->
    <div class="w-1/4 bg-white shadow-md overflow-y-auto">
      <!-- 搜索框 -->
      <div class="p-4">
        <input
            v-model="searchKeyword"
            @input="searchCollections"
            type="text"
            placeholder="搜索合集..."
            class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      <!-- 合集列表（下拉框） -->
      <div class="p-4">
        <h2 class="text-lg font-bold mb-2">合集</h2>
        <div>
          <details
              v-for="collection in collections"
              :key="collection.id"
              class="mb-2"
          >
            <summary
                class="p-2 cursor-pointer hover:bg-gray-100 rounded bg-blue-50 font-semibold"
            >
              {{ collection.title }}
            </summary>
            <ul class="pl-4">
              <li
                  v-for="note in collectionNotes[collection.id]"
                  :key="note.noteId"
                  @click="selectedNoteId = note.noteId"
                  :class="[
                  'p-2 cursor-pointer hover:bg-gray-100 rounded',
                  selectedNoteId === note.noteId ? 'bg-blue-100' : ''
                ]"
              >
                {{ note.noteTitle }}
              </li>
            </ul>
          </details>
        </div>
      </div>
    </div>
    <!-- 右侧笔记内容 -->
    <div class="w-3/4 p-6 overflow-y-auto">
      <div
          v-if="noteContent"
          class="prose max-w-none"
          v-html="noteContent"
          @click="copyCode"
      ></div>
      <div v-else class="text-gray-500">请选择一篇笔记查看内容</div>
    </div>
  </div>
</template>

<style>
/* 自定义代码块样式 */
.prose pre {
  position: relative;
  background-color: #f8f8f8;
  padding: 1rem;
  border-radius: 0.5rem;
}
.prose pre button {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background-color: #3b82f6;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  cursor: pointer;
}
.prose pre button:hover {
  background-color: #2563eb;
}
.prose h1 { font-size: 1.875rem; font-weight: 700; margin: 1rem 0; }
.prose h2 { font-size: 1.5rem; font-weight: 700; margin: 0.75rem 0; }
.prose h3 { font-size: 1.25rem; font-weight: 700; margin: 0.5rem 0; }
.prose strong { font-weight: 700; }
.prose em { font-style: italic; }
.prose p { margin-bottom: 1rem; line-height: 1.6; }

/* 自定义下拉框样式 */
details > summary {
  list-style: none;
}
details > summary::-webkit-details-marker {
  display: none;
}
details > summary::before {
  content: '▶';
  display: inline-block;
  margin-right: 0.5rem;
  transition: transform 0.2s;
}
details[open] > summary::before {
  transform: rotate(90deg);
}
</style>