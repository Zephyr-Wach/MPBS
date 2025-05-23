<template>
  <div class="markdown-editor" style="display: flex; height: 90vh;">
    <!-- 左侧：实时渲染 -->
    <div class="preview" style="flex: 1; padding: 1rem; border-right: 1px solid #ccc; overflow-y: auto;">
      <div v-html="renderedHtml"></div>
    </div>

    <!-- 右侧：Markdown 输入 -->
    <div class="editor" style="flex: 1; padding: 1rem;">
      <textarea
          v-model="markdown"
          style="width: 100%; height: 100%; resize: none; font-family: monospace; font-size: 14px;"
          placeholder="在这里输入 Markdown 内容..."
      ></textarea>
    </div>

  </div>

  <div style="padding: 1rem; border-top: 1px solid #ccc;">
    <input
        v-model="title"
        type="text"
        placeholder="请输入文章标题"
        style="width: 300px; padding: 0.5rem; font-size: 16px;"
    />
    <button @click="handlePostBlog" :disabled="loading" style="margin-left: 1rem; padding: 0.5rem 1rem;">
      {{ loading ? '发布中...' : '发布博客' }}
    </button>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { marked } from 'marked';
import { postBlog } from '@/api/admin/blog';

// markdown 文本
const markdown = ref('');

// 文章标题
const title = ref('');

// 加载状态
const loading = ref(false);

// 实时渲染 html
const renderedHtml = computed(() => {
  return marked.parse(markdown.value);
});

// 发表博客
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
      // contentHtml 可留空，后端可以做渲染
      status: 'published',
    });
    if (res.code === 0) {
      alert('发布成功');
      // 发布成功后清空输入
      title.value = '';
      markdown.value = '';
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
/* 可以根据需要自定义样式 */
</style>
