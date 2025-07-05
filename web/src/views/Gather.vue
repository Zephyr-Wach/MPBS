<template>
  <div class="note-collection-container">
    <div class="left-panel">
      <el-input
          v-model="searchKeyword"
          placeholder="搜索笔记标题"
          clearable
          size="small"
          class="search-input"
      />
      <el-scrollbar class="note-list-scroll">
        <el-menu
            :default-active="currentNoteId ? String(currentNoteId) : ''"
            @select="handleSelect"
            :router="false"
        >
          <el-menu-item
              v-for="note in noteList"
              :key="note.id"
              :index="String(note.id)"
              class="note-menu-item"
          >
            {{ note.title }}
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </div>

    <div class="right-panel">
      <h2>{{ currentNote?.title || '📚 欢迎来到我的笔记&合集' }}</h2>
      <div v-if="currentNote" class="note-content" v-html="renderedMarkdown"></div>
      <div v-else class="empty-note">请选择左侧笔记查看内容</div>

      <div class="navigation">
        <el-button size="small" @click="goPrev" :disabled="!prevNoteId">上一篇</el-button>
        <span>当前是第 {{ currentIndex + 1 }} 篇，共 {{ noteList.length }} 篇</span>
        <el-button size="small" @click="goNext" :disabled="!nextNoteId">下一篇</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, watch, onMounted} from 'vue';
import axios from 'axios';
import MarkdownIt from 'markdown-it';
import debounce from 'lodash/debounce';

// 状态变量
const searchKeyword = ref('');
const noteList = ref([]);
const currentNoteId = ref(null);
const currentNote = ref(null);
const prevNoteId = ref(null);
const nextNoteId = ref(null);
const currentIndex = ref(-1);

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
});

const renderedMarkdown = computed(() => {
  if (currentNote && currentNote.value && currentNote.value.content) {
    return md.render(currentNote.value.content);
  }
  return '';
});

// 异步获取笔记列表，支持搜索关键词
const fetchNoteList = async () => {
  try {
    const res = await axios.get('/notes', {params: {search: searchKeyword.value.trim()}});
    noteList.value = res.data || [];
    if (noteList.value.length > 0) {
      // 如果当前选中笔记ID不在列表中，默认选第一个
      if (!currentNoteId.value || !noteList.value.find(n => n.id === currentNoteId.value)) {
        selectNote(noteList.value[0].id);
      }
    } else {
      currentNoteId.value = null;
      currentNote.value = null;
      prevNoteId.value = null;
      nextNoteId.value = null;
      currentIndex.value = -1;
    }
  } catch (error) {
    console.error('获取笔记列表失败:', error);
    noteList.value = [];
  }
};

// 选中笔记，获取详情
const selectNote = async (id) => {
  currentNoteId.value = id;
  currentNote.value = null;
  try {
    const res = await axios.get(`/notes/${id}`);
    currentNote.value = res.data;

    currentIndex.value = noteList.value.findIndex(n => n.id === id);
    prevNoteId.value = currentIndex.value > 0 ? noteList.value[currentIndex.value - 1].id : null;
    nextNoteId.value = currentIndex.value < noteList.value.length - 1 ? noteList.value[currentIndex.value + 1].id : null;
  } catch (error) {
    console.error('获取笔记详情失败:', error);
    currentNote.value = null;
  }
};

const handleSelect = (id) => {
  selectNote(Number(id));
};

const goPrev = () => {
  if (prevNoteId.value) selectNote(prevNoteId.value);
};

const goNext = () => {
  if (nextNoteId.value) selectNote(nextNoteId.value);
};

// 防抖搜索，避免频繁请求
const debouncedFetchNoteList = debounce(fetchNoteList, 300);
watch(searchKeyword, () => {
  debouncedFetchNoteList();
});

// 页面初始化加载列表
onMounted(() => {
  fetchNoteList();
});
</script>

<style scoped>
.note-collection-container {
  display: flex;
  height: 100%;
  min-height: 600px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgb(0 0 0 / 0.1);
}

.left-panel {
  width: 25%;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  padding: 15px 20px;
  background: #fafafa;
}

.search-input {
  width: 100%;
  height: 40px;
  margin-bottom: 12px;
  box-sizing: border-box;
}

.note-list-scroll {
  flex: 1;
  margin-top: 10px;
  max-height: calc(100vh - 140px);
  overflow-y: auto;
}

.el-menu-item {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

.right-panel {
  width: 75%;
  padding: 20px 30px;
  display: flex;
  flex-direction: column;
  background: #fff;
}

h2 {
  margin-bottom: 1rem;
  font-weight: 600;
  font-size: 24px;
}

.note-content {
  flex: 1;
  overflow-y: auto;
  border: 1px solid #eee;
  padding: 15px;
  border-radius: 6px;
  background-color: #fefefe;
  font-size: 16px;
  line-height: 1.6;
  color: #333;
}

.empty-note {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #999;
  font-size: 18px;
  font-style: italic;
  border: 1px dashed #ccc;
  border-radius: 6px;
}

.navigation {
  margin-top: 12px;
  text-align: center;
  user-select: none;
}

.navigation span {
  margin: 0 15px;
  font-weight: 500;
  color: #666;
}
</style>
