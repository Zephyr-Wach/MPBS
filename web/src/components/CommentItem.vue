<template>
  <li class="comment-item">
    <div class="comment-header">
      <div class="comment-user">
        <strong>{{ comment.userName }}</strong>
        <small>{{ formatDate(comment.createdAt) }}</small>
      </div>
      <div class="comment-actions">
        <button @click="emit('toggle-reply', comment.id)">
          {{ replyCommentId === comment.id ? '取消回复' : '回复' }}
        </button>
        <button
            v-if="canDeleteComment(comment.userId)"
            @click="emit('delete-comment', comment.id)"
            class="delete-btn"
        >删除</button>
      </div>
    </div>

    <p class="comment-content">{{ comment.content }}</p>

    <div v-if="replyCommentId === comment.id" class="reply-box">
      <textarea
          v-model="replyContent"
          rows="2"
          placeholder="请输入回复内容"
      ></textarea>
      <button
          @click="submitReply"
          :disabled="submitLoading"
      >
        {{ submitLoading ? '提交中...' : '提交回复' }}
      </button>
    </div>

    <!-- 子评论递归渲染 -->
    <ul v-if="comment.children?.length" class="child-comments">
      <CommentItem
          v-for="child in comment.children"
          :key="child.id"
          :comment="child"
          :current-user="currentUser"
          :reply-comment-id="replyCommentId"
          :submit-loading="submitLoading"
          @toggle-reply="emit('toggle-reply', $event)"
          @submit-comment="emit('submit-comment', $event)"
          @delete-comment="emit('delete-comment', $event)"
      />
    </ul>
  </li>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import CommentItem from './CommentItem.vue';

const props = defineProps<{
  comment: any;
  currentUser: any;
  replyCommentId: string | null;
  submitLoading: boolean;
}>();

const emit = defineEmits(['toggle-reply', 'submit-comment', 'delete-comment']);

const replyContent = ref('');

function submitReply() {
  emit('submit-comment', { parentId: props.comment.id, content: replyContent.value });
  replyContent.value = '';  // 提交后清空输入框
}


function formatDate(dateStr: string) {
  const date = new Date(dateStr);
  return date.toLocaleString();
}

function canDeleteComment(commentUserId: string) {
  const permission = props.currentUser.userPermission;
  const allowed = ['INTERMEDIATE', 'SENIOR', 'ULTIMATE'];
  return (
      commentUserId === props.currentUser.userId ||
      allowed.includes(permission)
  );
}
</script>
<style>
.comment-item {
  list-style: none;
  margin-bottom: 1.5rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #fdfdfd;
  transition: box-shadow 0.2s ease;
}
.comment-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.comment-user strong {
  color: #2de2be;
  font-size: 1rem;
}
.comment-user small {
  color: #999;
  font-size: 0.75rem;
  margin-left: 0.5rem;
}

.comment-actions button {
  background: none;
  border: none;
  color: #409eff;
  cursor: pointer;
  margin-left: 0.5rem;
  font-size: 0.85rem;
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  transition: background 0.2s;
}
.comment-actions button:hover {
  background-color: rgba(64, 158, 255, 0.1);
}
.delete-btn {
  color: #f56c6c;
}
.delete-btn:hover {
  background-color: rgba(245, 108, 108, 0.1);
}

.comment-content {
  font-size: 0.95rem;
  color: #333;
  margin: 0.5rem 0;
  line-height: 1.5;
}

.reply-box {
  margin-top: 0.5rem;
}
.reply-box textarea {
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 0.5rem;
  resize: vertical;
  font-size: 0.9rem;
  font-family: inherit;
  transition: border 0.2s;
}
.reply-box textarea:focus {
  border-color: #2de2be;
  outline: none;
}

.reply-box button {
  margin-top: 0.5rem;
  background-color: #2de2be;
  color: #fff;
  border: none;
  padding: 0.4rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background 0.2s;
}
.reply-box button:hover {
  background-color: #29c9ab;
}
.reply-box button:disabled {
  background-color: #b2f0e4;
  cursor: not-allowed;
}

.child-comments {
  margin-top: 1rem;
  padding-left: 1rem;
  border-left: 2px solid #ddd;
}

</style>