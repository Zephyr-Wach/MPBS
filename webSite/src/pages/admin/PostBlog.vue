<script lang="ts" setup>
import { ref, computed } from 'vue';
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import { postBlog } from '@/api/admin/blog';
import {useI18n} from "vue-i18n";
// TODO 1.某行文本太长了就会超出容器，要自动换行
// TODO 2.md没有对$ $或$$ $$包裹的latex的转换渲染
// TODO 3.检查新发布blog的时间问题
// TODO 4.样式tailwind化
// 表单数据
const title = ref('');
const {t} = useI18n()
const coverUrl = ref('');
const status = ref('published');
const loading = ref(false);

// 实时渲染 HTML
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

const markdown = ref(`# Hello Markdown!\n\n\`\`\`js\nconsole.log("hello")\n\`\`\``)

const renderedHtml = computed(() => md.render(markdown.value))

// 发布博客
const handlePostBlog = async () => {
  if (!title.value.trim()) {
    alert(t('enterTittle'));
    return;
  }
  if (!markdown.value.trim()) {
    alert(t('enterContent'));
    return;
  }

  loading.value = true;
  try {
    const res = await postBlog({
      title: title.value.trim(),
      contentMd: markdown.value.trim(),
      coverUrl: coverUrl.value.trim() || undefined,
      status: status.value,
    });

    if (res.data.code === 200) {
      alert(t('postSuccess'));
      title.value = '';
      markdown.value = '';
      coverUrl.value = '';
      status.value = 'published';
    } else {
      alert(t('postFailed') +":"+ res.data.message);
    }
  } catch (error) {
    alert( t('getFailed') + ":" + error);
  } finally {
    loading.value = false;
  }
};
</script>

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
          :placeholder="t('enterMdHere')"
      ></textarea>
    </div>
  </div>

  <div style="padding: 1rem; border-top: 1px solid #ccc;">
    <input
        v-model="title"
        type="text"
        :placeholder="t('enterTittle')"
        style="width: 300px; padding: 0.5rem; font-size: 16px; margin-right: 1rem;"
    />
    <input
        v-model="coverUrl"
        type="text"
        :placeholder="t('enterCoverUrl')"
        style="width: 300px; padding: 0.5rem; font-size: 16px; margin-right: 1rem;"
    />
    <button @click="handlePostBlog" :disabled="loading" style="margin-left: 1rem; padding: 0.5rem 1rem;">
      {{ loading ? t('posting') : t('postBlog') }}
    </button>
  </div>
</template>

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


</style>
