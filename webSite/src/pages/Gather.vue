<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';
import katex from 'katex';
import 'katex/dist/katex.min.css';
import { getCollectionList, searchCollection, getNotesInCollection, getNote } from '@/api/normal/gather.ts';
import {useI18n} from "vue-i18n";


const {t} = useI18n()
const md = new MarkdownIt({
  highlight: (code, lang) => {
    return hljs.highlight(code, { language: lang || 'plaintext' }).value;
  },
});

const collections = ref<any[]>([]);
const selectedNoteId = ref<string | null>(null);
const noteContent = ref<string>('');
const searchKeyword = ref<string>('');
const collectionNotes = ref<Record<string, any[]>>({});

const renderMath = (content: string) => {
  let rendered = content;
  rendered = rendered.replace(/\$\$([\s\S]*?)\$\$/g, (_, tex) => {
    try {
      return katex.renderToString(tex.trim(), { displayMode: true, throwOnError: false });
    } catch (e) {
      return `<span class="text-red-500">LaTeX Error: ${e.message}</span>`;
    }
  });
  rendered = rendered.replace(/\$([^\$]+)\$/g, (_, tex) => {
    try {
      return katex.renderToString(tex.trim(), { displayMode: false, throwOnError: false });
    } catch (e) {
      return `<span class="text-red-500">LaTeX Error: ${e.message}</span>`;
    }
  });
  return rendered;
};

const fetchCollections = async () => {
  try {
    const res = await getCollectionList();
    collections.value = res.data.data;
    for (const collection of collections.value) {
      collectionNotes.value[collection.id] = [];
      await fetchNotes(collection.id);
    }
  } catch (error) {
    console.error(t('getCollectionFailed') + ":" + error);
  }
};

const searchCollections = async () => {
  try {
    if (searchKeyword.value.trim() === '') {
      fetchCollections();
    } else {
      const res = await searchCollection(searchKeyword.value);
      collections.value = res.data.data.records;
      collectionNotes.value = {};
      for (const collection of collections.value) {
        collectionNotes.value[collection.id] = [];
        await fetchNotes(collection.id);
      }
    }
  } catch (error) {
    console.error(t('searchCollectionFailed')+":"+ error);
  }
};

const fetchNotes = async (collectionId: string) => {
  try {
    const res = await getNotesInCollection(collectionId);
    collectionNotes.value[collectionId] = res.data.data;
    if (res.data.data.length > 0 && !selectedNoteId.value) {
      selectedNoteId.value = res.data.data[0].noteId;
    }
  } catch (error) {
    console.error(t('getNoteListFailed')+":"+ error);
  }
};

const fetchNoteContent = async (noteId: string) => {
  try {
    const res = await getNote(noteId);
    const rawContent = res.data.data.contentMd;
    const htmlContent = md.render(rawContent);
    noteContent.value = renderMath(htmlContent);
  } catch (error) {
    console.error(t('getNoteFailed')+":"+error);
    noteContent.value = `<p>${t('getNoteFailed')}</p>`;
  }
};

const copyCode = (event: Event) => {
  const button = event.target as HTMLElement;
  const codeBlock = button.nextElementSibling?.querySelector('code');
  if (codeBlock) {
    const code = codeBlock.innerText;
    navigator.clipboard.writeText(code).then(() => {
      button.textContent = t('copyed');
      setTimeout(() => {
        button.textContent = t('copy');
      }, 2000);
    });
  }
};

watch(selectedNoteId, (newId) => {
  if (newId) {
    fetchNoteContent(newId);
  }
});

onMounted(() => {
  fetchCollections();
});
</script>

<template>
  <div class="flex h-full bg-gray-100">

    <div class="w-1/4 bg-white shadow-md overflow-y-auto">

      <div class="p-4">
        <input
            v-model="searchKeyword"
            @input="searchCollections"
            type="text"
            :placeholder="t('searchCollection')"
            class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div class="p-4">
        <h2 class="text-lg font-bold mb-2">{{t('collection')}}</h2>
        <div>
          <details
              v-for="collection in collections"
              :key="collection.id"
              class="mb-2"
          >
            <summary
                class="p-2 cursor-pointer hover:bg-gray-100 rounded bg-blue-50 font-semibold"
            >
              {{ collection.title }}
            </summary>
            <ul class="pl-4">
              <li
                  v-for="note in collectionNotes[collection.id]"
                  :key="note.noteId"
                  @click="selectedNoteId = note.noteId"
                  :class="[
                  'p-2 cursor-pointer hover:bg-gray-100 rounded',
                  selectedNoteId === note.noteId ? 'bg-blue-100' : ''
                ]"
              >
                {{ note.noteTitle }}
              </li>
            </ul>
          </details>
        </div>
      </div>
    </div>

    <div class="w-3/4 p-6 overflow-y-auto">
      <div
          v-if="noteContent"
          class="prose max-w-none"
          v-html="noteContent"
          @click="copyCode"
      ></div>
      <div v-else class="text-gray-500">{{t('selectNote')}}</div>
    </div>
  </div>
</template>

<style>
.prose pre {
  position: relative;
  background-color: #f8f8f8;
  padding: 1rem;
  border-radius: 0.5rem;
}
.prose pre button {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background-color: #3b82f6;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  cursor: pointer;
}
.prose pre button:hover {
  background-color: #2563eb;
}
.prose h1 { font-size: 1.875rem; font-weight: 700; margin: 1rem 0; }
.prose h2 { font-size: 1.5rem; font-weight: 700; margin: 0.75rem 0; }
.prose h3 { font-size: 1.25rem; font-weight: 700; margin: 0.5rem 0; }
.prose strong { font-weight: 700; }
.prose em { font-style: italic; }
.prose p { margin-bottom: 1rem; line-height: 1.6; }

details > summary {
  list-style: none;
}
details > summary::-webkit-details-marker {
  display: none;
}
details > summary::before {
  content: '▶';
  display: inline-block;
  margin-right: 0.5rem;
  transition: transform 0.2s;
}
details[open] > summary::before {
  transform: rotate(90deg);
}
</style>