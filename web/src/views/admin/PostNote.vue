<template>
  <div class="markdown-editor" style="display: flex; height: 70vh;">
    <div class="preview" style="flex: 1; padding: 1rem; border-right: 1px solid #ccc; overflow-y: auto;">
      <div v-html="renderedHtml" style="text-align: left;"></div>
    </div>
    <div class="editor" style="flex: 1; padding: 1rem; display: flex; flex-direction: column;">
      <textarea
          v-model="markdown"
          style="flex: 1; width: 100%; resize: none; font-family: monospace; font-size: 14px;"
          placeholder="在这里输入 Markdown 内容..."
      ></textarea>
    </div>
  </div>

  <div style="padding: 1rem; border-top: 1px solid #ccc;">
    <input
        v-model="title"
        type="text"
        placeholder="请输入笔记标题"
        style="width: 300px; padding: 0.5rem; font-size: 16px; margin-right: 1rem;"
    />

    <button @click="handleAddCollection" :disabled="collectionloading" style="margin-left: 1rem; padding: 0.5rem 1rem;">
      <template v-if="collectionloading">添加中...</template>
      <template v-else-if="selectedCollection">已选合集：{{ selectedCollection.title }}</template>
      <template v-else>选择合集</template>
    </button>

    <div v-if="showCollectionSelector" class="modal-overlay" @click.self="closeSelector">
      <div class="collectionSelector">
        <h3 v-if="selectedCollection">已选择合集：{{ selectedCollection.title }}</h3>
        <h3 v-else>选择合集</h3>
        <div v-if="collectionloading">加载中...</div>
        <div v-else-if="collections.length === 0">暂无合集</div>
        <ul v-else>
          <li
              v-for="item in collections"
              :key="item.id"
              class="item"
              :class="{ selected: selectedCollection && selectedCollection.id === item.id }"
              @click="selectCollection(item)"
          >
            {{ item.title }}
          </li>
        </ul>
        <button @click="closeSelector">关闭</button>
      </div>
    </div>

    <button @click="handleManageCollection" :disabled="ManageCollectionloading" style="margin-left: 1rem; padding: 0.5rem 1rem;">
      <template v-if="ManageCollectionloading">管理中...</template>
      <template v-else>管理合集</template>
    </button>

    <div v-if="showManageCollection" class="modal-overlay" @click.self="closeManageCollection">
      <div class="collectionSelector">
        <h3>合集管理</h3>
        <div v-if="ManageCollectionloading">加载中...</div>
        <div v-else-if="collections.length === 0">暂无合集</div>
        <ul>
          <li v-for="item in collections" :key="item.id">
            <div>
              <input v-if="item.editing" v-model="item.title" @blur="saveCollection(item)" />
              <div v-else style="white-space: pre-wrap; margin-bottom: 0.5rem;">
                {{ item.title }} ---- {{ item.description }}
              </div>

              <div>
                <button @click="toggleExpand(item)">{{ item.expanded ? '收起' : '展开' }}</button>
                <button @click="() => item.editing = true">重命名</button>
                <button @click="togglePublic(item)">{{ item.isPublic === '1' ? '设为私有' : '设为公开' }}</button>
                <button @click="deleteCollection(item)">删除</button>
              </div>
            </div>


            <div v-if="item.expanded">
              <p v-if="!item.notes">加载中...</p>
              <ul v-else>
                <li v-for="note in item.notes" :key="note.id">{{ note }}</li>
              </ul>
            </div>
          </li>
        </ul>
        <button @click="closeManageCollection">关闭</button>
      </div>
    </div>

    <section>
      <label for="isPublic">是否公开：</label>
      <select v-model="isPublic" id="isPublic">
        <option value="0">否</option>
        <option value="1">是</option>
      </select>
    </section>

    <button @click="handlePostNote" :disabled="loading" style="margin-left: 1rem; padding: 0.5rem 1rem;">
      {{ loading ? '发布中...' : '发布笔记' }}
    </button>
  </div>
</template>

<script lang="ts" setup>
import draggable from 'vuedraggable';
import { ref, computed } from 'vue';
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
</script>

<style scoped>
.markdown-output pre {
  background: #f6f8fa;
  padding: 1em;
  border-radius: 4px;
  overflow-x: auto;
}
.modal-overlay {
  position: fixed;
  top: 50px;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}
.collectionSelector {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 20px rgba(0,0,0,0.2);
}
.item {
  padding: 0.5rem;
  cursor: pointer;
  border-radius: 4px;
  margin: 0.25rem 0;
  transition: background-color 0.2s;
}
.item:hover {
  background-color: #f0f0f0;
}
.item.selected {
  background-color: #409eff;
  color: white;
}
</style>
