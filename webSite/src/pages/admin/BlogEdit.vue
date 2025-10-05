<script lang="ts" setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { updateBlog } from '@/api/admin/blog';
import { getBlogDetail } from '@/api/normal/blog';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';
import {useI18n} from "vue-i18n";

// TODO 1.某行文本太长了就会超出容器，要自动换行
// TODO 2.md没有对$ $或$$ $$包裹的latex的转换渲染

const {t} = useI18n()
// 初始化 markdown-it 渲染器
const md: MarkdownIt = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: (str: string, lang: string): string => {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre class="hljs"><code>${hljs.highlight(str, { language: lang }).value}</code></pre>`;
      } catch (_) {}
    }
    const temp = new MarkdownIt();
    return `<pre class="hljs"><code>${temp.utils.escapeHtml(str)}</code></pre>`;
  }
});

const route = useRoute();
const router = useRouter();

const blogId = route.params.id as string;

const blog = ref({
  id: '',
  title: '',
  contentMd: '',
  coverUrl: '',
  contentHtml: '',
});

const loading = ref(false);

const renderedHtml = computed(() => md.render(blog.value.contentMd || ''));

watch(
    () => blog.value.contentMd,
    (newMd) => {
      blog.value.contentHtml = md.render(newMd || '');
    },
    { immediate: true }
);

// 加载博客详情
async function loadBlog() {
  try {
    const res = await getBlogDetail(blogId);
    if (res.data.code === 200) {
      Object.assign(blog.value, res.data.data);
    } else {
      alert(t('loadBlogFailed') +":"+ res.data.message);
    }
  } catch (error) {
    alert(t('loadBlogFailed'));
    console.error(error);
  }
}

// 提交编辑内容
async function submitEdit() {
  if (loading.value) return;

  if (!blog.value.title.trim()) {
    alert(t('enterTittle'));
    return;
  }
  if (!blog.value.contentMd.trim()) {
    alert(t('enterContent'));
    return;
  }

  loading.value = true;

  try {
    const res = await updateBlog(blogId, blog.value);
    if (res.data.code === 200) {
      alert(t('changeSuccess'));
      await router.push('/admin/blogs');
    } else {
      alert(t('changeFailed') +":"+ res.data.message);
    }
  } catch (error) {
    if (error instanceof Error) {
      alert(t('getFailed') +":"+ error.message);
    } else {
      alert(t('getFailed') +":"+ error);
    }
    console.error(error);
  } finally {
    loading.value = false;
  }
}

function cancelEdit() {
  router.back();
}

onMounted(() => {
  loadBlog();
});
</script>

<template>
  <div class="edit-container">
    <h2>{{t('editBlog')}}</h2>
    <form @submit.prevent="submitEdit" style="display: flex; gap: 1rem; height: 70vh;">
      <div
          class="preview"
          style="flex: 1; padding: 1rem; border-left: 1px solid #ccc; overflow-y: auto;"
          v-html="renderedHtml"
      ></div>
      <div style="flex: 1; display: flex; flex-direction: column;">
        <div class="form-group">
          <label for="title">{{t('tittle')}}</label>
          <input id="title" v-model="blog.title" required />
        </div>

        <div class="form-group" style="flex: 1; display: flex; flex-direction: column;">
          <label for="contentMd">{{t('mdContent')}}</label>
          <textarea
              id="contentMd"
              v-model="blog.contentMd"
              rows="12"
              required
              style="flex: 1; font-family: monospace; resize: none;"
          ></textarea>
        </div>

        <div class="form-group">
          <label for="coverUrl">{{t('CoverUrl')}}</label>
          <input id="coverUrl" v-model="blog.coverUrl" />
        </div>

        <div class="btn-group">
          <button type="submit" :disabled="loading">
            {{ loading ? t('saving') : t('saveChanges') }}
          </button>
          <button type="button" @click="cancelEdit">{{t('cancel')}}</button>
        </div>
      </div>


    </form>
  </div>
</template>

<style scoped>
.edit-container {
  max-width: 1000px;
  margin: auto;
  padding: 1rem;
}
.form-group {
  margin-bottom: 1rem;
}
label {
  display: block;
  font-weight: bold;
  margin-bottom: 0.3rem;
}
input,
select,
textarea {
  width: 100%;
  padding: 0.5rem;
  box-sizing: border-box;
  font-size: 1rem;
}
textarea {
  font-family: monospace;
  resize: vertical;
}
.btn-group {
  margin-top: 1.5rem;
}
button {
  padding: 0.5rem 1rem;
  margin-right: 1rem;
  cursor: pointer;
}
button[type='submit'] {
  background-color: #409eff;
  border: none;
  color: white;
  border-radius: 4px;
}
button[type='button'] {
  background-color: #eee;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.preview {
  background: #fff;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
  "Helvetica Neue", Arial, sans-serif;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  overflow-wrap: break-word;
}
.markdown-preview pre code {
  white-space: pre-wrap;
}

</style>
