<script setup lang="ts">
import { ref } from "vue";
import Sidebar from "./Sidebar.vue";
import Navbar from "./Navbar.vue";
import { useI18n } from "vue-i18n";

const { locale } = useI18n();
const showSidebar = ref(false);

function toggleSidebar() {
  showSidebar.value = !showSidebar.value;
}

function setLang(lang: string) {
  locale.value = lang;
}
</script>

<template>
  <div class="flex h-screen w-screen bg-gradient-to-br from-gray-50 to-gray-100 overflow-hidden p-4 gap-4">
    <!-- Desktop Sidebar -->
    <Sidebar
        class="hidden md:flex w-64 h-full rounded-2xl shadow-lg border border-gray-200/50 bg-white/80 backdrop-blur-md transition-all hover:shadow-xl flex-shrink-0"
    />

    <!-- Mobile Sidebar Drawer -->
    <transition name="slide">
      <div v-if="showSidebar" class="fixed inset-0 z-40 flex h-full">
        <div
            class="w-64 h-full p-4 overflow-y-auto rounded-r-2xl shadow-xl border-r border-gray-200/50 bg-white/90 backdrop-blur-md transition-all hover:shadow-2xl"
        >
          <Sidebar :isMobile="true" />
        </div>
        <div
            class="flex-1 bg-black/30 backdrop-blur-sm"
            @click="toggleSidebar"
        ></div>
      </div>
    </transition>

    <!-- Navbar + main -->
    <div class="flex flex-col flex-1 h-full rounded-2xl shadow-lg border border-gray-200/50 bg-white/80 backdrop-blur-md relative transition-all hover:shadow-xl overflow-hidden">
      <!-- Mobile sidebar button -->
      <button
          class="md:hidden absolute top-1/2 left-0 z-50 w-12 h-24 bg-white/90 backdrop-blur-md shadow-sm border border-gray-200/50 rounded-r-full flex items-center justify-center hover:scale-110 hover:shadow-xl transition-all"
          @click="toggleSidebar"
      >
        <svg
            class="w-6 h-6 text-gray-600"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
        >
          <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M9 5l7 7-7 7"
          />
        </svg>
      </button>

      <!-- Navbar -->
      <Navbar @toggleSidebar="toggleSidebar" @setLang="setLang" class="rounded-t-2xl" />

      <!-- Main content -->
      <main class="flex-1 w-full flex flex-col overflow-y-auto p-4">
        <router-view class="flex-1 w-full h-full" />
      </main>
    </div>
  </div>
</template>


<style scoped>
.slide-enter-active,
.slide-leave-active {
  transition: transform 0.4s cubic-bezier(0.25, 0.8, 0.25, 1),
  opacity 0.4s ease;
}
.slide-enter-from {
  transform: translateX(-100%);
  opacity: 0;
}
.slide-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}
</style>
