<template>
  <div class="comments-section">
    <h3 class="section-title">评论区</h3>

    <div v-if="loading" class="loading-text">加载中...</div>

    <div v-else>
      <div v-if="commentTree.length === 0" class="no-comments">暂无评论，快来抢沙发！</div>

      <ul class="comment-list">
        <CommentItem
            v-for="comment in commentTree"
            :key="comment.id"
            :comment="comment"
            :current-user="currentUser"
            :reply-comment-id="replyCommentId"
            :submit-loading="submitLoading"
            @toggle-reply="toggleReply"
            @submit-comment="submitCommentFromChild"
            @delete-comment="deleteThisComment"
        />
      </ul>

      <!-- 顶级评论输入 -->
      <div class="new-comment">
        <h4>发表评论</h4>
        <textarea
            v-model="newComment"
            placeholder="请输入评论内容"
            rows="4"
            :disabled="!isLoggedIn"
        ></textarea>
        <button
            @click="submitComment(null, newComment)"
            :disabled="submitLoading || !isLoggedIn"
        >


          {{ submitLoading ? '提交中...' : isLoggedIn ? '提交评论' : '请先登录' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getCommentList } from '@/api/public/comment';
import { addCommentWithEmailCheck, deleteComment } from '@/api/user/comment';
import { getUserInfo } from '@/api/user/user';
import { useRouter } from 'vue-router';
import CommentItem from './CommentItem.vue';

const router = useRouter();

const props = defineProps({
  postId: {
    type: String,
    required: true,
  }
});

const commentTree = ref([]);
const loading = ref(false);
const submitLoading = ref(false);
const newComment = ref('');
const replyCommentId = ref<string | null>(null);

const currentUser = ref({ userId: '', userPermission: '' });
const isLoggedIn = ref(false);

async function loadUser() {
  try {
    const res = await getUserInfo();
    if (res.code === 0) {
      currentUser.value = res.data;
      isLoggedIn.value = true;
    }
  } catch {
    isLoggedIn.value = false;
  }
}

async function loadComments() {
  loading.value = true;
  try {
    const res = await getCommentList(props.postId);
    if (res.code === 0) {
      commentTree.value = res.data;
    } else {
      alert('获取评论失败：' + res.message);
    }
  } catch (e) {
    alert('请求评论出错');
    console.error(e);
  } finally {
    loading.value = false;
  }
}

function toggleReply(commentId: string) {
  if (!isLoggedIn.value) {
    router.push({ name: 'Login' });
    return;
  }
  replyCommentId.value = replyCommentId.value === commentId ? null : commentId;
}

async function submitComment(parentId: string | null, content: string) {
  if (!isLoggedIn.value) {
    router.push({ name: 'Login' });
    return;
  }

  content = content.trim();
  if (!content) {
    alert('请输入评论内容');
    return;
  }

  submitLoading.value = true;
  try {
    const res = await addCommentWithEmailCheck({
      postId: props.postId,
      parentId,
      content
    });
    if (res.code === 0) {
      newComment.value = '';
      replyCommentId.value = null;
      await loadComments();
    } else {
      alert('评论失败：' + res.message);
    }
  } catch (e: any) {
    // 如果是邮箱没验证的自定义错误，提示对应信息
    if (e.message === '请完成邮箱验证再评论') {
      alert(e.message);
    } else {
      alert('提交评论出错');
    }
    console.error(e);
  } finally {
    submitLoading.value = false;
  }
}


function submitCommentFromChild({ parentId, content }: { parentId: string, content: string }) {
  submitComment(parentId, content);
}


async function deleteThisComment(id: string) {
  if (!confirm('确认删除这条评论吗？')) return;
  try {
    const res = await deleteComment(id);
    if (res.code === 0) {
      await loadComments();
    } else {
      alert('删除失败：' + res.message);
    }
  } catch (e) {
    alert('删除评论出错');
    console.error(e);
  }
}

onMounted(async () => {
  await loadUser();
  await loadComments();
});
</script>

<style scoped>
.comments-section {
  max-width: 700px;
  margin: 0 auto;
  padding: 1.5rem;
  background-color: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.1);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2de2be;
  margin-bottom: 1rem;
  border-bottom: 2px solid #409eff;
  padding-bottom: 0.3rem;
}

.loading-text,
.no-comments {
  text-align: center;
  color: #999;
  font-style: italic;
  margin: 2rem 0;
}

.comment-list {
  list-style: none;
  padding-left: 0;
  margin-bottom: 2rem;
}

.new-comment {
  border-top: 1px solid #ddd;
  padding-top: 1rem;
}

.new-comment h4 {
  font-weight: 600;
  color: #409eff;
  margin-bottom: 0.75rem;
}

.new-comment textarea {
  width: 100%;
  border-radius: 10px;
  border: 1.5px solid #ddd;
  padding: 0.75rem 1rem;
  resize: vertical;
  font-size: 1rem;
  color: #444;
  transition: border-color 0.3s ease;
  outline: none;
}

.new-comment textarea:focus {
  border-color: #2de2be;
  box-shadow: 0 0 6px #2de2beaa;
}

.new-comment textarea:disabled {
  background-color: #f5f5f5;
  color: #aaa;
  cursor: not-allowed;
}

.new-comment button {
  margin-top: 0.75rem;
  padding: 0.55rem 1.4rem;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
  user-select: none;
}

.new-comment button:disabled {
  background-color: #ddd;
  color: #999;
  cursor: not-allowed;
}

.new-comment button:not(:disabled):hover {
  background-color: #2de2be;
}

</style>
