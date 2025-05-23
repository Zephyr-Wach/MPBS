<template>
  <div class="edit-container">
    <h2>编辑博客</h2>
    <form @submit.prevent="submitEdit">
      <div class="form-group">
        <label for="title">标题</label>
        <input id="title" v-model="blog.title" required />
      </div>

      <div class="form-group">
        <label for="status">状态</label>
        <select id="status" v-model="blog.status" required>
          <option value="draft">草稿</option>
          <option value="published">已发布</option>
        </select>
      </div>

      <div class="form-group">
        <label for="contentMd">Markdown 内容</label>
        <textarea id="contentMd" v-model="blog.contentMd" rows="12" required></textarea>
      </div>

      <div class="form-group">
        <label for="coverUrl">封面图片 URL</label>
        <input id="coverUrl" v-model="blog.coverUrl" />
      </div>

      <div class="btn-group">
        <button type="submit">保存修改</button>
        <button type="button" @click="cancelEdit">取消</button>
      </div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getBlogDetail, updateBlog } from '@/api/admin/blog';

const route = useRoute();
const router = useRouter();

const blog = ref({
  id: '',
  title: '',
  contentMd: '',
  coverUrl: '',
  status: 'draft',
});

const blogId = route.params.id as string;

async function loadBlog() {
  try {
    const res = await getBlogDetail(blogId);
    if (res.code === 0) {
      Object.assign(blog.value, res.data);
    } else {
      alert('加载博客失败：' + res.message);
    }
  } catch (error) {
    alert('请求失败');
    console.error(error);
  }
}

async function submitEdit() {
  try {
    const res = await updateBlog(blogId, blog.value);
    if (res.code === 0) {
      alert('修改成功');
      router.push('/admin/blogs');
    } else {
      alert('修改失败：' + res.message);
    }
  } catch (error) {
    alert('请求失败');
    console.error(error);
  }
}

function cancelEdit() {
  router.back();
}

onMounted(() => {
  loadBlog();
});
</script>

<style scoped>
.edit-container {
  max-width: 700px;
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
input, select, textarea {
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
button[type="submit"] {
  background-color: #409eff;
  border: none;
  color: white;
  border-radius: 4px;
}
button[type="button"] {
  background-color: #eee;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
