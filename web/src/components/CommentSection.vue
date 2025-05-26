<template>
  <div class="comments-section" style="margin-top: 2rem; max-width: 800px; margin-left:auto; margin-right:auto;">
    <h3>评论区</h3>

    <div v-if="loading">加载中...</div>
    <div v-else>
      <div v-if="commentTree.length === 0">暂无评论，快来抢沙发！</div>

      <ul>
        <li v-for="comment in commentTree" :key="comment.id" style="margin-bottom: 1rem; border-bottom: 1px solid #eee; padding-bottom: 0.5rem;">
          <div>
            <strong>{{ comment.userName }}</strong>
            <small style="color:#999;">{{ formatDate(comment.createdAt) }}</small>

            <!-- 删除按钮 -->
            <button
                v-if="canDeleteComment(comment.userId)"
                @click="deleteThisComment(comment.id)"
                style="float:right; color:red; border:none; background:none; cursor:pointer;"
            >删除</button>

            <!-- 回复按钮 -->
            <button
                @click="toggleReply(comment.id)"
                style="float:right; margin-right: 10px; color:#409eff; border:none; background:none; cursor:pointer;"
            >{{ replyCommentId === comment.id ? '取消回复' : '回复' }}</button>
          </div>
          <p>{{ comment.content }}</p>

          <!-- 回复输入框 -->
          <div v-if="replyCommentId === comment.id" style="margin-top: 0.5rem;">
            <textarea
                v-model="replyContent"
                rows="2"
                style="width: 100%;"
                placeholder="请输入回复内容"
            ></textarea>
            <button
                @click="submitComment(comment.id)"
                :disabled="submitLoading"
                style="margin-top: 0.5rem; background:#67c23a; color:white; border:none; border-radius: 4px; padding: 0.3rem 1rem;"
            >{{ submitLoading ? '提交中...' : '提交回复' }}</button>
          </div>

          <!-- 子评论 -->
          <ul v-if="comment.children && comment.children.length" style="margin-left: 1.5rem; margin-top:0.5rem;">
            <li v-for="child in comment.children" :key="child.id" style="margin-bottom: 0.5rem;">
              <strong>{{ child.userName }}</strong>
              <small style="color:#999;">{{ formatDate(child.createdAt) }}</small>

              <!-- 删除按钮 -->
              <button
                  v-if="canDeleteComment(child.userId)"
                  @click="deleteThisComment(child.id)"
                  style="float:right; color:red; border:none; background:none; cursor:pointer;"
              >删除</button>

              <p>{{ child.content }}</p>
            </li>
          </ul>
        </li>
      </ul>

      <!-- 顶级评论 -->
      <div style="margin-top: 2rem;">
        <h4>发表评论</h4>
        <textarea
            v-model="newComment"
            placeholder="请输入评论内容"
            rows="4"
            style="width: 100%;"
            :disabled="!isLoggedIn"
        ></textarea>
        <button
            @click="submitComment(null)"
            :disabled="submitLoading || !isLoggedIn"
            style="margin-top: 0.5rem; padding: 0.5rem 1rem; background:#409eff; color:#fff; border:none; border-radius: 4px;"
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
import { addComment, deleteComment } from '@/api/user/comment';
import { getUserInfo } from '@/api/user/user';
import { useRouter } from 'vue-router';

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
const replyCommentId = ref<string | null>(null); // 当前正在回复的评论id
const replyContent = ref('');

const currentUser = ref({ userId: '', userPermission: '' });
const isLoggedIn = ref(false);

function formatDate(dateStr) {
  const date = new Date(dateStr);
  return date.toLocaleString();
}

async function loadUser() {
  try {
    const res = await getUserInfo();
    if (res.code === 0) {
      currentUser.value = res.data;
      isLoggedIn.value = true;
    } else {
      isLoggedIn.value = false;
    }
  } catch (e) {
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
  if (replyCommentId.value === commentId) {
    replyCommentId.value = null;
    replyContent.value = '';
  } else {
    replyCommentId.value = commentId;
    replyContent.value = '';
  }
}

function canDeleteComment(commentUserId: string) {
  const permission = currentUser.value.userPermission;
  const allowed = ['INTERMEDIATE', 'SENIOR', 'ULTIMATE'];
  return (
      isLoggedIn.value &&
      (commentUserId === currentUser.value.userId || allowed.includes(permission))
  );
}

async function submitComment(parentId: string | null) {
  if (!isLoggedIn.value) {
    router.push({ name: 'Login' });
    return;
  }

  const content = parentId ? replyContent.value.trim() : newComment.value.trim();
  if (!content) {
    alert('请输入评论内容');
    return;
  }

  submitLoading.value = true;
  try {
    const res = await addComment({
      postId: props.postId,
      parentId: parentId || null,
      content: content,
    });
    if (res.code === 0) {
      newComment.value = '';
      replyContent.value = '';
      replyCommentId.value = null;
      await loadComments();
    } else {
      alert('评论提交失败：' + res.message);
    }
  } catch (e) {
    alert('提交评论出错');
    console.error(e);
  } finally {
    submitLoading.value = false;
  }
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
