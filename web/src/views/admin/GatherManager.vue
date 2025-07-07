<template>
  <div class="note-collection-container">
    <div class="left-panel">
      <el-input
          v-model="searchKeyword"
          placeholder="搜索合集标题"
          clearable
          size="small"
          class="search-input"
      />


      <el-scrollbar class="note-list-scroll">
        <el-scrollbar class="note-list-scroll">
          <el-menu
              :default-active="currentNoteId ? String(currentNoteId) : ''"
              :default-openeds="openedCollections"
              @open="handleOpenCollection"
              @select="handleSelectNote"
              :router="false"
              unique-opened
          >
            <el-sub-menu
                v-for="collection in collectionList"
                :key="collection.id"
                :index="String(collection.id)"
                :title="collection.title"
            >
              <template #title>
                {{ collection.title }}
              </template>
              <template v-if="collectionNotes[collection.id]?.length > 0">
                <el-menu-item
                    v-for="note in collectionNotes[collection.id]"
                    :key="note.noteId"
                    :index="String(note.noteId)"
                >
                  {{ note.noteTitle }}
                </el-menu-item>
              </template>
              <template v-else>
                <el-menu-item disabled>暂无文章</el-menu-item>
              </template>
            </el-sub-menu>
          </el-menu>
        </el-scrollbar>
      </el-scrollbar>


    </div>

    <div class="right-panel">
      <h2>{{ currentNote?.title || '📚 欢迎来到我的笔记&合集' }}</h2>
      <div v-if="currentNote" class="note-content" v-html="renderedMarkdown"></div>
      <div v-else class="empty-note">请选择左侧笔记查看内容</div>

      <!--      <div class="navigation">-->
      <!--        <el-button size="small" @click="goPrev" :disabled="!hasPrev">上一篇</el-button>-->
      <!--        <span>当前是第 {{ currentIndex + 1 }} 篇，共 {{ currentNoteList.length }} 篇</span>-->
      <!--        <el-button size="small" @click="goNext" :disabled="!hasNext">下一篇</el-button>-->
      <!--      </div>-->
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import MarkdownIt from 'markdown-it';
import debounce from 'lodash/debounce';
import request from '@/utils/request';

const searchKeyword = ref('');
const collectionList = ref([]);
const collectionNotes = ref({});
const openedCollections = ref([]);
const currentNoteId = ref(null);
const currentNote = ref(null);
const currentIndex = ref(-1);

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
});

// 渲染当前笔记 markdown 内容
const renderedMarkdown = computed(() => {
  if (currentNote.value && currentNote.value.contentMd) {
    return md.render(currentNote.value.contentMd);
  }
  return '';
});


// 获取合集列表接口
const fetchCollectionList = async () => {
  try {
    let res;
    const kw = searchKeyword.value.trim();

    if (!kw) {
      res = await request.get('/ULTIMATE/gather/list');
      if (res.code === 0 && Array.isArray(res.data)) {
        collectionList.value = res.data;
      } else {
        collectionList.value = [];
      }
    } else {
      res = await request.get('/ULTIMATE/gather/search', {
        params: { keyword: kw },
      });
      if (res.code === 0 && res.data && Array.isArray(res.data.records)) {
        collectionList.value = res.data.records;
      } else {
        collectionList.value = [];
      }
    }

    // 重置相关状态，只要 collectionList 赋值成功都清空
    if (collectionList.value.length > 0) {
      collectionNotes.value = {};
      openedCollections.value = [];
      currentNote.value = null;
      currentNoteId.value = null;
      currentIndex.value = -1;
    }
  } catch (error) {
    console.error('获取合集列表失败:', error);
    collectionList.value = [];
  }
};

// 合集展开时，动态加载该合集笔记列表
const handleOpenCollection = async (collectionId) => {
  if (!collectionNotes.value[collectionId]) {
    try {
      const res = await request.get(`/public/relation/queryGatherNotes?gatherId=${collectionId}`);
      if (res.code === 0 && Array.isArray(res.data)) {
        collectionNotes.value[collectionId] = res.data;
      } else {
        collectionNotes.value[collectionId] = [];
      }
    } catch (error) {
      console.error('获取合集笔记失败:', error);
      collectionNotes.value[collectionId] = [];
    }
  }
  if (!openedCollections.value.includes(collectionId)) {
    openedCollections.value.push(collectionId);
  }
};

// 点击笔记选中
const handleSelectNote = async (noteId) => {
  const res = await request.get(`/public/gather/getNote?noteId=${noteId}`);
  currentNote.value = res.data;
};

// 当前笔记所属合集笔记列表（用于上一篇下一篇）
const currentNoteList = computed(() => {
  if (!currentNoteId.value) return [];
  for (const cid in collectionNotes.value) {
    if (collectionNotes.value[cid].some(n => n.id === currentNoteId.value)) {
      return collectionNotes.value[cid];
    }
  }
  return [];
});

// 判断是否有上一篇、下一篇
const hasPrev = computed(() => currentIndex.value > 0);
const hasNext = computed(() => currentIndex.value < currentNoteList.value.length - 1);

// 上一篇
const goPrev = () => {
  if (hasPrev.value) {
    const prevNote = currentNoteList.value[currentIndex.value - 1];
    handleSelectNote(prevNote.id);
  }
};
// 下一篇
const goNext = () => {
  if (hasNext.value) {
    const nextNote = currentNoteList.value[currentIndex.value + 1];
    handleSelectNote(nextNote.id);
  }
};

// 搜索防抖
const debouncedFetchCollectionList = debounce(fetchCollectionList, 300);
watch(searchKeyword, () => {
  debouncedFetchCollectionList();
});

// 页面初始化加载合集
onMounted(() => {
  fetchCollectionList();
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
