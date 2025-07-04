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
        placeholder="请输入文章标题"
        style="width: 300px; padding: 0.5rem; font-size: 16px; margin-right: 1rem;"
    />
    <input
        v-model="coverUrl"
        type="text"
        placeholder="封面图片 URL (可选)"
        style="width: 300px; padding: 0.5rem; font-size: 16px; margin-right: 1rem;"
    />
    <button @click="handlePostBlog" :disabled="loading" style="margin-left: 1rem; padding: 0.5rem 1rem;">
      {{ loading ? '发布中...' : '发布博客' }}
    </button>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import { postBlog } from '@/api/admin/blog';

// 表单数据
const title = ref('');
// const markdown = ref('');
const coverUrl = ref('');
const status = ref('published');
const loading = ref(false);

// 实时渲染 HTML
// const renderedHtml = computed(() => marked.parse(markdown.value));
const md = new MarkdownIt({
  html: true, // 允许 HTML 标签
  linkify: true, // 自动链接
  typographer: true, // 替换一些排版符号
  highlight: (str, lang) => {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre class="hljs"><code>${hljs.highlight(str, { language: lang }).value}</code></pre>`
      } catch (_) {}
    }
    return `<pre class="hljs"><code>${md.utils.escapeHtml(str)}</code></pre>`
  }
})

const markdown = ref(`# Hello Markdown!\n\n\`\`\`js\nconsole.log("hello")\n\`\`\``)

const renderedHtml = computed(() => md.render(markdown.value))




// 发布博客
const handlePostBlog = async () => {
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
    const res = await postBlog({
      title: title.value.trim(),
      contentMd: markdown.value.trim(),
      contentHtml: renderedHtml.value,
      coverUrl: coverUrl.value.trim() || undefined,
      status: status.value,
    });

    if (res.code === 0) {
      alert('发布成功');
      title.value = '';
      markdown.value = '';
      coverUrl.value = '';
      status.value = 'published';
    } else {
      alert('发布失败: ' + res.message);
    }
  } catch (error) {
    alert('请求出错: ' + error);
  } finally {
    loading.value = false;
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
.markdown-output code {
  font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
}

.markdown-preview pre code {
  white-space: pre-wrap;
}


</style>
