<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import MarkdownIt from 'markdown-it';
import debounce from 'lodash/debounce';
import request from '@/utils/request';
import {ElMessage} from "element-plus";

const searchKeyword = ref('');
const collectionList = ref([]);
const collectionNotes = ref({});
const openedCollections = ref([]);
const currentNoteId = ref(null);
const currentNote = ref(null);
const currentIndex = ref(-1);

const editingCollectionId = ref(null)
const editCollectionTitle = ref(null)
const editdescription = ref(null)
const editInputRef = ref(null)

function startRenamingCollection(collection) {
  console.log('startRenamingCollection', collection)
  editingCollectionId.value = collection.id
  editCollectionTitle.value = collection.title
  editdescription.value = collection.description
  nextTick(() => {
    if (editInputRef.value?.focus) {
      editInputRef.value.focus()
    }
  })
}

async function submitRename(collection) {
  if (editCollectionTitle.value.trim() === '') {
    ElMessage.warning('标题不能为空')
    return
  }

  if (editCollectionTitle.value !== collection.title) {
    const res = await request({
      url: `/ULTIMATE/gather/updateCollection?gatherId=${editingCollectionId.value}`,
      method: 'POST',
      data: {
        title: editCollectionTitle.value.trim(),
        description: collection.description.trim(),
        isPublic: collection.isPublic.trim(),
      },
    });
    if (res.code === 0) {
      collection.title = editCollectionTitle.value
      editingCollectionId.value = null
      ElMessage.success('重命名成功')
    } else {
      ElMessage.error('重命名失败')
      editingCollectionId.value = null
    }
  }
}

async function changeCollectionPublic(collection) {
  const res = await request({
    url: `/ULTIMATE/gather/updateCollection?gatherId=${collection.id}`,
    method: 'POST',
    data: {
      title: collection.title.trim(),
      description: collection.description.trim(),
      isPublic: collection.isPublic.trim() === "1"?"0":"1",
    },
  });
  if (res.code === 0) {
    ElMessage.success('切换成功')
    collection.isPublic = collection.isPublic.trim() === "1"?"0":"1"
  } else {
    ElMessage.error('切换失败')
  }
}

async function deleteCollection(collectionId) {
  if (!confirm('确认删除该合集吗？')) return;
  try {
    const res = await request.get(`/ULTIMATE/gather/deleteCollection?gatherId=${collectionId}`);
    if (res.code === 0) {
      ElMessage.success('删除成功')
      collectionList.value = collectionList.value.filter(c => c.id !== collectionId)
    } else {
      ElMessage.error('删除失败')
    }
  } catch (e) {
    alert('删除失败');
  }
}

const editingNoteId = ref(null)
const editNoteTitle = ref(null)
const noteInputRef = ref(null)

function startRenamingNote(note) {
  editingNoteId.value = note.noteId
  editNoteTitle.value = note.noteTitle
  nextTick(() => {
    noteInputRef.value?.focus()
  })
}

async function submitRenameNote(note) {
  const IsPublic = String(note.isPublic).trim() === '1' ? '1' : '0';
  if (!editNoteTitle.value.trim()) {
    ElMessage.warning('标题不能为空')
    return
  }
  const res = await request({
    url: `/ULTIMATE/gather/updateNote?noteId=${note.noteId}`,
    method: 'POST',
    data: {
      title: editNoteTitle.value.trim(),
      contentMd : "notUpdate",
      isPublic: IsPublic,
    }
  })
  if (res.code === 0) {
    note.noteTitle = editNoteTitle.value.trim()
    editingNoteId.value = null
    ElMessage.success('重命名成功')
  } else {
    editingNoteId.value = null
    ElMessage.error('重命名失败')
  }
  editingNoteId.value = null
}

async function changeNotePublic(note) {
  const newIsPublic = String(note.isPublic).trim() === '1' ? '0' : '1';  // 先计算切换后的状态

  const res = await request({
    url: `/ULTIMATE/gather/updateNote?noteId=${note.noteId}`,
    method: 'POST',
    data: {
      isPublic: newIsPublic,
      title: note.noteTitle.trim(),
      contentMd : "notUpdate"
    },
  });
  if (res.code === 0) {
    note.isPublic = newIsPublic;
    ElMessage.success('切换成功');
  } else {
    ElMessage.error('切换失败');
  }
}


async function deleteNote(note) {
  if (!confirm(`确定要删除「${note.noteTitle}」吗？`)) return;
  const res = await request.get(`/ULTIMATE/gather/deleteNote?noteId=${note.noteId}`);
  if (res.code === 0) {
    // 从 collectionNotes 中移除
    for (const id in collectionNotes.value) {
      collectionNotes.value[id] = collectionNotes.value[id].filter(n => n.noteId !== note.noteId);
    }
    // 如果当前查看的是该文章，清空当前内容
    if (currentNoteId.value === note.noteId) {
      currentNoteId.value = null;
      currentNote.value = null;
    }
    ElMessage.success('删除成功');
  } else {
    ElMessage.error('删除失败');
  }
}

const isEditMd = ref(false);
const editContent = ref('');

async function startEditingMd() {
  if (currentNote.value?.contentMd) {
    editContent.value = currentNote.value.contentMd;
  }
  isEditMd.value = true;
}

async function endEditingMd() {
  const res = await request({
    url: `/ULTIMATE/gather/updateNote?noteId=${currentNote.value.id}`,
    method: 'POST',
    data: {
      contentMd: editContent.value,
      title: "notUpdate",
      isPublic: 2,
    },
  });
  if (res.code === 0) {
    ElMessage.success('内容已保存');
    currentNote.value.contentMd = editContent.value;
  } else {
    ElMessage.error('保存失败');
  }
  isEditMd.value = false;
}

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
});

const renderedMarkdown = computed(() => {
  if (isEditMd.value) {
    return md.render(editContent.value || '');
  } else if (currentNote.value?.contentMd) {
    return md.render(currentNote.value.contentMd);
  }
  return '';
});

// 获取合集列表接口
const fetchCollectionList = async () => {
  try {
    let res;
    const kw = searchKeyword.value.trim();

    if (!kw) {
      res = await request.get('/ULTIMATE/gather/list');
      if (res.code === 0 && Array.isArray(res.data)) {
        collectionList.value = res.data;
      } else {
        collectionList.value = [];
      }
    } else {
      res = await request.get('/ULTIMATE/gather/search', {
        params: { keyword: kw },
      });
      if (res.code === 0 && res.data && Array.isArray(res.data.records)) {
        collectionList.value = res.data.records;
      } else {
        collectionList.value = [];
      }
    }

    // 重置相关状态，只要 collectionList 赋值成功都清空
    if (collectionList.value.length > 0) {
      collectionNotes.value = {};
      openedCollections.value = [];
      currentNote.value = null;
      currentNoteId.value = null;
      currentIndex.value = -1;
    }
  } catch (error) {
    console.error('获取合集列表失败:', error);
    collectionList.value = [];
  }
};

// 合集展开时，动态加载该合集笔记列表
const handleOpenCollection = async (collectionId) => {
  if (!collectionNotes.value[collectionId]) {
    try {
      const res = await request.get(`/ULTIMATE/gather/queryGatherNotes?gatherId=${collectionId}`);
      if (res.code === 0 && Array.isArray(res.data)) {
        collectionNotes.value[collectionId] = res.data;
      } else {
        collectionNotes.value[collectionId] = [];
      }
    } catch (error) {
      console.error('获取合集笔记失败:', error);
      collectionNotes.value[collectionId] = [];
    }
  }
  if (!openedCollections.value.includes(collectionId)) {
    openedCollections.value.push(collectionId);
  }
};

// 点击笔记选中
const handleSelectNote = async (noteId) => {
  const res = await request.get(`/public/gather/getNote?noteId=${noteId}`);
  currentNote.value = res.data;
};

// 搜索防抖
const debouncedFetchCollectionList = debounce(fetchCollectionList, 300);
watch(searchKeyword, () => {
  debouncedFetchCollectionList();
});

// 页面初始化加载合集
onMounted(() => {
  fetchCollectionList();
});
</script>

<template>
  <div class="note-collection-container">
    <div class="left-panel">
      <div v-if = !isEditMd>
        <el-input
            v-model="searchKeyword"
            placeholder="搜索合集标题"
            clearable
            size="small"
            class="search-input"
        />

        <el-scrollbar class="note-list-scroll">
          <el-scrollbar class="note-list-scroll">
            <!--          合集部分-->
            <el-menu
                :default-active="currentNoteId ? String(currentNoteId) : ''"
                :default-openeds="openedCollections"
                @open="handleOpenCollection"
                @select="handleSelectNote"
                :router="false"
                unique-opened
            >
              <el-sub-menu
                  v-for="collection in collectionList"
                  :key="collection.id"
                  :index="String(collection.id)"
                  :title="collection.title"
              >
                <template #title v-if="editingCollectionId === collection.id">
                  <el-input
                      v-model="editCollectionTitle"
                      size="small"
                      @blur="submitRename(collection)"
                      @keydown.enter="submitRename(collection)"
                      ref="editInputRef"
                      style="width: 150px"
                      :maxlength="30"
                      show-word-limit
                  />
                </template>
                <template #title v-else>
                  {{ collection.title }}
                  <el-button
                      type="text"
                      size="small"
                      class="rename-collection"
                      @click="startRenamingCollection(collection)"
                  >重命名</el-button>
                  <el-button
                      type="text"
                      size="small"
                      class="add-note"
                      @click="changeCollectionPublic(collection)"
                  >
                    {{ collection.isPublic === '1' ? '改为私密' : '改为公开' }}
                  </el-button>
                  <el-button
                      type="text"
                      size="small"
                      class="delete-collection"
                      @click="deleteCollection(collection.id)"
                  >删除</el-button>
                </template>

                <!--              文章部分-->
                <template v-if="collectionNotes[collection.id]?.length > 0">
                  <el-menu-item
                      v-for="note in collectionNotes[collection.id]"
                      :key="note.noteId"
                      :index="String(note.noteId)"
                  >
                    <template v-if="editingNoteId === note.noteId">
                      <el-input
                          v-model="editNoteTitle"
                          size="small"
                          ref="noteInputRef"
                          @blur="submitRenameNote(note)"
                          @keydown.enter="submitRenameNote(note)"
                          style="width: 140px"
                      />
                    </template>
                    <template v-else>
                      {{ note.noteTitle }}
                      <el-button
                          type="text"
                          size="small"
                          @click.stop="startRenamingNote(note)"
                      >重命名</el-button>
                      <el-button
                          type="text"
                          size="small"
                          @click.stop="changeNotePublic(note)"
                      >
                        {{ note.isPublic == '1' ? '改为私密' : '改为公开' }}
                      </el-button>
                      <el-button
                          type="text"
                          size="small"
                          @click.stop="deleteNote(note)"
                      >删除</el-button>
                    </template>
                  </el-menu-item>
                </template>
                <template v-else>
                  <el-menu-item disabled>暂无文章</el-menu-item>
                </template>
              </el-sub-menu>
            </el-menu>

          </el-scrollbar>
        </el-scrollbar>
      </div>
      <div v-else>
        <textarea v-model="editContent" class="markdown-editor" placeholder="请输入 Markdown 内容"></textarea>
      </div>
    </div>

    <div class="right-panel">
      <h2>{{ currentNote?.title || '📚 管理笔记&合集' }}</h2>
      <div v-if="currentNote">
        <div v-if = isEditMd>
          <el-button @click="endEditingMd">保存编辑</el-button>
        </div>
        <div v-else>
          <el-button @click="startEditingMd">编辑</el-button>
        </div>
      </div>
      <div v-if="currentNote" class="note-content" v-html="renderedMarkdown"></div>
      <div v-else class="empty-note">请选择左侧笔记查看内容</div>
    </div>

  </div>
</template>

<style scoped>
.note-collection-container {
  display: flex;
  height: 100%;
  min-height: 600px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.left-panel {
  width: 25%;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  padding: 15px 20px;
  background: linear-gradient(to bottom, #f7faff, #f0f3fa);
}

.search-input {
  width: 100%;
  height: 40px;
  margin-bottom: 12px;
  box-sizing: border-box;
}

.note-list-scroll {
  flex: 1;
  margin-top: 10px;
  max-height: calc(100vh - 140px);
  overflow-y: auto;
}

/* 合集与文章菜单 hover 效果 */
.el-menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.el-menu-item > span {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.el-menu-item .el-button {
  margin-left: 4px;
  flex-shrink: 0;
}


/* 操作按钮 hover 效果 */
.rename-collection,
.add-note,
.delete-collection {
  margin-left: 4px;
  color: #666;
  transition: color 0.2s;
}

.rename-collection:hover,
.add-note:hover,
.delete-collection:hover {
  color: #409EFF;
}

.right-panel {
  width: 75%;
  padding: 20px 30px;
  display: flex;
  flex-direction: column;
  background: #fff;
}

h2 {
  margin-bottom: 1rem;
  font-weight: 600;
  font-size: 24px;
  color: #333;
}

.note-content {
  flex: 1;
  overflow-y: auto;
  border: 1px solid #eee;
  padding: 15px;
  border-radius: 8px;
  background-color: #fefefe;
  font-size: 16px;
  line-height: 1.6;
  color: #333;
}

.empty-note {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #999;
  font-size: 18px;
  font-style: italic;
  border: 1px dashed #ccc;
  border-radius: 6px;
}

/* Markdown 编辑器优化 */
.markdown-editor {
  width: 100%;
  height: calc(100vh - 180px);
  font-family: monospace;
  font-size: 14px;
  line-height: 1.6;
  padding: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  resize: none;
  box-sizing: border-box;
  background-color: #fdfdfd;
  transition: box-shadow 0.2s ease;
}

.markdown-editor:focus {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
  outline: none;
}

</style>