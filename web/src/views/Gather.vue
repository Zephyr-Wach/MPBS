<template>
  <div class="note-collection-container">
    <div class="left-panel">
      <el-input
          v-model="searchKeyword"
          placeholder="搜索笔记标题"
          clearable
          @input="fetchNoteList"
          size="small"
          class="search-input"
      />
      <el-scrollbar class="note-list-scroll">
        <el-menu
            :default-active="currentNoteId ? String(currentNoteId) : ''"
            @select="handleSelect"
            router={false}
        >
          <el-menu-item
              v-for="note in noteList"
              :key="note.id"
              :index="String(note.id)"
          >
            {{ note.title }}
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </div>

    <div class="right-panel">
      <h2>{{ currentNote?.title || '📚 欢迎来到我的笔记&合集' }}</h2>
      <div v-html="currentNote?.content" class="note-content"></div>
      <div class="navigation">
        <el-button size="small" @click="goPrev" :disabled="!prevNoteId">上一篇</el-button>
        <span>当前是第 {{ currentIndex + 1 }} 篇，共 {{ noteList.length }} 篇</span>
        <el-button size="small" @click="goNext" :disabled="!nextNoteId">下一篇</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

const searchKeyword = ref('')
const noteList = ref([])
const currentNoteId = ref(null)
const currentNote = ref(null)
const prevNoteId = ref(null)
const nextNoteId = ref(null)
const currentIndex = ref(-1)

const fetchNoteList = async () => {
  const res = await axios.get('/notes', { params: { search: searchKeyword.value } })
  noteList.value = res.data
  if (noteList.value.length > 0) {
    if (!currentNoteId.value || !noteList.value.find(n => n.id === currentNoteId.value)) {
      selectNote(noteList.value[0].id)
    }
  } else {
    currentNoteId.value = null
    currentNote.value = null
    prevNoteId.value = null
    nextNoteId.value = null
    currentIndex.value = -1
  }
}

const selectNote = async (id) => {
  currentNoteId.value = id
  currentNote.value = null
  const res = await axios.get(`/notes/${id}`)
  currentNote.value = res.data

  currentIndex.value = noteList.value.findIndex(n => n.id === id)
  prevNoteId.value = currentIndex.value > 0 ? noteList.value[currentIndex.value - 1].id : null
  nextNoteId.value = currentIndex.value < noteList.value.length - 1 ? noteList.value[currentIndex.value + 1].id : null
}

const handleSelect = (id) => {
  selectNote(Number(id))
}

const goPrev = () => {
  if (prevNoteId.value) selectNote(prevNoteId.value)
}

const goNext = () => {
  if (nextNoteId.value) selectNote(nextNoteId.value)
}

onMounted(() => {
  fetchNoteList()
})

watch(searchKeyword, () => {
  fetchNoteList()
})
</script>

<style scoped>


.note-collection-container {
  display: flex;
  height: 100%;
  min-height: 600px; /* 或者你自己调整 */
}

.left-panel {
  width: 25%;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  padding: 15px 20px;
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
}

.right-panel {
  width: 75%;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.note-content {
  flex: 1;
  overflow-y: auto;
  border: 1px solid #eee;
  padding: 10px;
  margin-bottom: 10px;
}

.navigation {
  text-align: center;
}
</style>
