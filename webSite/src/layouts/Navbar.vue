<script setup lang="ts">
import { useLoginModal } from "@/store/loginModal"
import { useUserStore } from "@/store/userStore.ts"
import { useI18n } from "vue-i18n"

const { t } = useI18n()
const userStore = useUserStore()
const loginModal = useLoginModal()

const logout = () => userStore.logout()
</script>

<template>
  <header
      class="grid grid-cols-3 items-center bg-white/70 backdrop-blur-md border-b border-gray-200 px-4 h-14 transition-all"
  >
    <!-- change language -->
    <div class="flex items-center gap-2">
      {{ t("setLanguage") }}
      <select
          @change="$emit('setLang', ($event.target as HTMLSelectElement).value)"
          class="px-2 py-1 rounded border border-gray-300 hover:bg-gray-200 transition-colors"
      >
        <option value="zh">简体中文</option>
        <option value="en">English</option>
      </select>
    </div>

    <!-- tittle -->
    <div class="flex justify-center">
      <h1 class="text-lg font-semibold">my-vue-template</h1>
    </div>

    <!-- user -->
    <div class="flex items-center gap-3 justify-end">
      <template v-if="userStore.isLoggedIn">
        <RouterLink
            to="/userProfile"
            class="px-2 py-1 rounded hover:bg-gray-100 transition-colors"
        >
          {{ t("userCenter") }}
        </RouterLink>
        <button
            @click="logout"
            class="px-2 py-1 rounded hover:bg-gray-100 transition-colors"
        >
          {{ t("logout") }}
        </button>
      </template>

      <button
          v-else
          @click="() => loginModal.open()"
          class="px-2 py-1 rounded hover:bg-gray-100 transition-colors"
      >
        {{ t("login") }}
      </button>

    </div>

  </header>
</template>
