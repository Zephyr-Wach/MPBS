<template>
  <div class="markdown-editor" style="display: flex; height: 70vh;">
    <div class="preview" style="flex: 1; padding: 1rem; border-right: 1px solid #ccc; overflow-y: auto;">
      <div v-html="renderedHtml" style="text-align: left;"></div>
    </div>
    <!-- 右侧：Markdown 输入 -->
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
      <template v-if="collectionloading">
        添加中...
      </template>
      <template v-else-if="selectedCollection">
        已选合集：{{ selectedCollection.title }}
      </template>
      <template v-else>
        选择合集
      </template>
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
import { ref, computed } from 'vue';
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import { postNote, listCollections } from '@/api/admin/Gather';

// 表单数据
const title = ref('');
const coverUrl = ref('');
const isPublic = ref('0');
const loading = ref(false);
const collectionloading = ref(false);
const showCollectionSelector = ref(false);
const collections = ref([]);
const selectedCollection = ref(null);
const markdown = ref(`# Hello Markdown!\n\n\`\`\`js\nconsole.log("hello")\n\`\`\``)
const renderedHtml = computed(() => md.render(markdown.value))
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: (str, lang) => {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre class="hljs"><code>${hljs.highlight(str, { language: lang }).value}</code></pre>`
      } catch (_) {}
    }
    return `<pre class="hljs"><code>${md.utils.escapeHtml(str)}</code></pre>`
  }
})

const handlePostNote = async () => {
  if (!title.value.trim()) {
    alert('请输入文章标题');
    return;
  }
  if (!markdown.value.trim()) {
    alert('请输入文章内容');
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
      coverUrl.value = '';
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
  showCollectionSelector.value = true
  collectionloading.value = true
  try {
    const res = await listCollections();
    if (res.code === 0) {
      collections.value = res.data;
    } else {
      console.error('加载失败:', res.message);
    }
  } catch (e) {
    console.error('请求异常:', e)
  } finally {
    collectionloading.value = false;
  }
}

const selectCollection = (item) => {
  selectedCollection.value = item;
  showCollectionSelector.value = false; // 选择后关闭弹窗（可选）
  console.log('选中了合集:', item);
};

const closeSelector = () => {
  showCollectionSelector.value = false;
};

</script>

<style scoped>
.markdown-output pre {
  background: #f6f8fa;
  padding: 1em;
  border-radius: 4px;
  overflow-x: auto;
}
.markdown-output code {
  font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
}

.markdown-preview pre code {
  white-space: pre-wrap;
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