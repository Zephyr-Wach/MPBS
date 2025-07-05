<template>
  <div class="markdown-editor-container" style="padding: 1rem;">
    <div class="editor-main" style="display: flex; height: 70vh; gap: 1rem;">
      <!-- Markdown 预览 -->
      <div class="preview" style="flex: 1; padding: 1rem; border: 1px solid #ddd; border-radius: 8px; background: #fdfdfd; overflow-y: auto; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
        <div v-html="renderedHtml" style="text-align: left;"></div>
      </div>

      <!-- Markdown 编辑器 -->
      <div class="editor" style="flex: 1; display: flex; flex-direction: column;">
        <textarea
            v-model="markdown"
            style="flex: 1; width: 100%; resize: none; font-family: monospace; font-size: 14px; padding: 1rem; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);"
            placeholder="在这里输入 Markdown 内容..."
        ></textarea>
      </div>
    </div>

    <!-- 标题 + 控制区 -->
    <div style="margin-top: 1.5rem; border-top: 1px solid #e0e0e0; padding-top: 1rem; display: flex; flex-wrap: wrap; align-items: center; gap: 1rem;">
      <input
          v-model="title"
          type="text"
          placeholder="请输入笔记标题"
          style="width: 300px; padding: 0.5rem 0.75rem; font-size: 16px; border: 1px solid #ccc; border-radius: 6px;"
      />

      <button @click="handleAddCollection" :disabled="collectionloading" class="btn">
        <template v-if="collectionloading">添加中...</template>
        <template v-else-if="selectedCollection">已选合集：{{ selectedCollection.title }}</template>
        <template v-else>选择合集</template>
      </button>

      <button @click="handleManageCollection" :disabled="ManageCollectionloading" class="btn">
        <template v-if="ManageCollectionloading">管理中...</template>
        <template v-else>管理合集</template>
      </button>

      <label for="isPublic">是否公开：</label>
      <select v-model="isPublic" id="isPublic" style="padding: 0.4rem 0.6rem; border-radius: 4px; border: 1px solid #ccc;">
        <option value="0">否</option>
        <option value="1">是</option>
      </select>

      <button @click="handlePostNote" :disabled="loading" class="btn primary">
        {{ loading ? '发布中...' : '发布笔记' }}
      </button>
    </div>

    <!-- 选择合集弹窗 -->
    <div v-if="showCollectionSelector" class="modal-overlay" @click.self="closeSelector">
      <div class="modal-content">
        <h3>{{ selectedCollection ? '已选择合集：' + selectedCollection.title : '选择合集' }}</h3>
        <input
            v-model="selectorSearchKeyword"
            placeholder="搜索合集"
            style="margin: 0.5rem 0; padding: 0.5rem; width: 100%;"
        />
        <div v-if="collectionloading">加载中...</div>
        <div v-else-if="collections.length === 0">暂无合集</div>
        <ul v-else>
          <li
              v-for="item in collections"
              :key="item.id"
              class="modal-item"
              :class="{ selected: selectedCollection && selectedCollection.id === item.id }"
              @click="selectCollection(item)"
          >
            {{ item.title }}
          </li>
        </ul>
        <button @click="closeSelector" class="btn small">关闭</button>
      </div>
    </div>

    <!-- 管理合集弹窗 -->
    <div v-if="showManageCollection" class="modal-overlay" @click.self="closeManageCollection">
      <div class="modal-content">
        <h3>合集管理</h3>
        <div style="margin-bottom: 1rem; border-bottom: 1px solid #ccc; padding-bottom: 1rem;">
          <input
              v-model="newCollection.title"
              placeholder="合集标题"
              style="margin-bottom: 0.5rem; padding: 0.5rem; width: 100%; border: 1px solid #ccc; border-radius: 6px;"
          />
          <input
              v-model="newCollection.description"
              placeholder="合集描述"
              style="margin-bottom: 0.5rem; padding: 0.5rem; width: 100%; border: 1px solid #ccc; border-radius: 6px;"
          />
          <select v-model="newCollection.isPublic" style="padding: 0.5rem; width: 100%; border: 1px solid #ccc; border-radius: 6px;">
            <option value="1">公开</option>
            <option value="0">私有</option>
          </select>
          <button @click="createCollection" class="btn small" style="margin-top: 0.5rem;">新增合集</button>
        </div>
        <input
            v-model="searchKeyword"
            placeholder="搜索合集"
            style="margin-bottom: 1rem; padding: 0.5rem; width: 100%; border: 1px solid #ccc; border-radius: 6px;"
        />

        <div v-if="ManageCollectionloading">加载中...</div>
        <div v-else-if="collections.length === 0">暂无合集</div>
        <ul>
          <li v-for="item in collections" :key="item.id" style="margin-bottom: 1rem;">
            <div style="margin-bottom: 0.5rem;">
              <input v-if="item.editing" v-model="item.title" @blur="saveCollection(item)" />
              <div v-else>{{ item.title }} ---- {{ item.description }}</div>
            </div>
            <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
              <button @click="toggleExpand(item)" class="btn small">{{ item.expanded ? '收起' : '展开' }}</button>
              <button @click="() => item.editing = true" class="btn small">重命名</button>
              <button @click="togglePublic(item)" class="btn small">
                {{ item.isPublic === '1' ? '设为私有' : '设为公开' }}
              </button>
              <button @click="deleteCollection(item)" class="btn small danger">删除</button>
            </div>

            <!-- 拖动文章 -->
            <div v-if="item.expanded" style="margin-top: 0.5rem;">
              <p v-if="!item.notes">加载中...</p>
              <draggable v-else v-model="item.notes" item-key="id" @end="() => onNotesDragEnd(item)" tag="ul">
                <template #item="{ element }">
                  <li style="padding: 6px 10px; border: 1px solid #ddd; margin-bottom: 4px; border-radius: 4px; background: #f9f9f9; cursor: grab;">
                    {{ element }}
                  </li>
                </template>
              </draggable>
            </div>
          </li>
        </ul>
        <button @click="closeManageCollection" class="btn small">关闭</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import debounce from 'lodash/debounce';
import request from '@/utils/request';
import draggable from 'vuedraggable';
import { ref, computed, watch } from 'vue';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';
import {
  postNote,
  listCollections,
  listNotesFromCollection,
  delCollection,
  updateCollection
} from '@/api/admin/Gather';

const selectorSearchKeyword = ref('');
const title = ref('');
const coverUrl = ref('');
const isPublic = ref('0');
const loading = ref(false);
const collectionloading = ref(false);
const ManageCollectionloading = ref(false);
const showCollectionSelector = ref(false);
const showManageCollection = ref(false);
const collections = ref([]);
const selectedCollection = ref(null);
const searchKeyword = ref('');
const markdown = ref(`# Hello Markdown!\n\n\
\`\`\`js\nconsole.log("hello")\n\`\`\``);
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: (str, lang) => {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre class="hljs"><code>${hljs.highlight(str, { language: lang }).value}</code></pre>`;
      } catch (_) {}
    }
    return `<pre class="hljs"><code>${md.utils.escapeHtml(str)}</code></pre>`;
  }
});

const newCollection = ref({
  title: '',
  description: '',
  isPublic: '1',
});

const createCollection = async () => {
  if (!newCollection.value.title.trim()) {
    alert('合集标题不能为空');
    return;
  }
  ManageCollectionloading.value = true;
  try {
    const res = await request({
      url: '/ULTIMATE/gather/createCollection',
      method: 'POST',
      data: {
        title: newCollection.value.title.trim(),
        description: newCollection.value.description.trim(),
        isPublic: newCollection.value.isPublic,
      },
    });
    if (res.code === 0) {
      alert('新增合集成功');
      // 清空输入框
      newCollection.value.title = '';
      newCollection.value.description = '';
      newCollection.value.isPublic = '1';
      // 重新加载合集列表
      await handleManageCollection();
    } else {
      alert('新增失败：' + res.message);
    }
  } catch (error) {
    alert('请求失败：' + error);
  } finally {
    ManageCollectionloading.value = false;
  }
};


const renderedHtml = computed(() => md.render(markdown.value));

const handlePostNote = async () => {
  if (!title.value.trim() || !markdown.value.trim()) {
    alert('标题和内容不能为空');
    return;
  }
  loading.value = true;
  try {
    const res = await postNote({
      title: title.value.trim(),
      contentMd: markdown.value.trim(),
      isPublic: isPublic.value,
    });
    if (res.code === 0) {
      addToCollection(res.data);
      alert('发布成功');
      title.value = '';
      markdown.value = '';
      isPublic.value = '0';
    } else {
      alert('发布失败: ' + res.message);
    }
  } catch (error) {
    alert('请求出错: ' + error);
  } finally {
    loading.value = false;
  }
};

const addToCollection = async (noteId: string) => {
  const res = await request({
    url: `/ULTIMATE/gather/addRelation`,
    method: 'post',
    data: {
      gatherId: selectedCollection.value.id,
      noteId: noteId,
    },
  });
};


const doSelectorSearch = debounce(async (keyword) => {
  const res = await request({
    url: `/public/gather/search?keyword=${encodeURIComponent(keyword)}`,
    method: 'get',
  });

  if (res.code === 0 && Array.isArray(res.data.records)) {
    collections.value = res.data.records;
  }
}, 300);

watch(selectorSearchKeyword, async (newVal) => {
  if (!newVal) {
    const res = await listCollections();
    if (res.code === 0) {
      collections.value = res.data;
    }
  } else {
    doSelectorSearch(newVal);
  }
});

const openSelector = async () => {
  showCollectionSelector.value = true;
  collectionloading.value = true;

  try {
    const res = await listCollections();
    if (res.code === 0) {
      collections.value = res.data;
    }
  } finally {
    collectionloading.value = false;
  }
};


const handleAddCollection = async () => {
  showCollectionSelector.value = true;
  collectionloading.value = true;
  try {
    const res = await listCollections();
    if (res.code === 0) {
      collections.value = res.data.map(c => ({ ...c, editing: false, expanded: false, notes: null }));
    }
  } finally {
    collectionloading.value = false;
  }
};

const handleManageCollection = async () => {
  showManageCollection.value = true;
  ManageCollectionloading.value = true;
  try {
    const res = await listCollections();
    if (res.code === 0) {
      collections.value = res.data.map(c => ({ ...c, editing: false, expanded: false, notes: null }));
    }
  } finally {
    ManageCollectionloading.value = false;
  }
};

const selectCollection = (item) => {
  selectedCollection.value = item;
  console.log(selectedCollection.value);
  showCollectionSelector.value = false;
};

const closeSelector = () => (showCollectionSelector.value = false);
const closeManageCollection = () => (showManageCollection.value = false);

const toggleExpand = async (item) => {
  if (!item.expanded && !item.notes) {
    try {
      const res = await listNotesFromCollection({ gatherId: item.id });
      item.notes = res.data;
      // console.l
    } catch (e) {
      item.notes = [];
    }
  }
  item.expanded = !item.expanded;
};

const saveCollection = async (item) => {
  try {
    await updateCollection({
      title: item.title,
      description: item.description,
      isPublic: item.isPublic,
    }, item.id);
    item.editing = false;
  } catch (e) {
    alert('更新失败');
  }
};

const togglePublic = async (item) => {
  item.isPublic = item.isPublic === '1' ? '0' : '1';
  await saveCollection(item);
};

const deleteCollection = async (item) => {
  if (!confirm('确认删除该合集吗？')) return;
  try {
    await delCollection(item.id);
    collections.value = collections.value.filter(c => c.id !== item.id);
  } catch (e) {
    alert('删除失败');
  }
};
//houxu an
async function onNotesDragEnd(item) {
  try {
    const order = {};
    item.notes.forEach((note, idx) => {
      order[note] = idx + 1;
    });

    await request({
      url: '/ULTIMATE/gather/updateOrder',
      method: 'post',
      data: {
        gatherId: item.id,
        order,
      },
    });

    alert('排序保存成功');
  } catch (e) {
    alert('排序保存失败');
    console.error('排序保存失败的错误:', e);
  }
}

const doSearch = debounce(async (keyword) => {
  const res = await request({
    url: `/public/gather/search?keyword=${encodeURIComponent(keyword)}`,
    method: 'get',
  });

  if (res.code === 0 && Array.isArray(res.data.records)) {
    collections.value = res.data.records.map(c => ({
      ...c,
      editing: false,
      expanded: false,
      notes: null
    }));
  }
}, 300);


watch(searchKeyword, (newVal) => {
  if (!newVal) {
    handleManageCollection(); // 重新加载
  } else {
    doSearch(newVal);
  }
});


</script>


<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal-content {
  background: #fff;
  padding: 2rem;
  width: 500px;
  max-height: 80vh;
  overflow-y: auto;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}
.modal-item {
  padding: 0.6rem 1rem;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.modal-item.selected {
  background-color: #e6f7ff;
  font-weight: bold;
}

.btn {
  background: #f0f0f0;
  border: 1px solid #ccc;
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
  cursor: pointer;
  transition: 0.2s;
}
.btn:hover {
  background: #e0e0e0;
}
.btn.primary {
  background-color: #409eff;
  color: white;
  border-color: #409eff;
}
.btn.primary:hover {
  background-color: #66b1ff;
}
.btn.small {
  font-size: 13px;
  padding: 0.3rem 0.6rem;
}
.btn.danger {
  background-color: #ff4d4f;
  color: white;
  border-color: #ff4d4f;
}
.btn.danger:hover {
  background-color: #ff7875;
}
</style>