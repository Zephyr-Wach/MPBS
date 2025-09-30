<script setup lang="ts">
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gray-100">
    <h1 class="text-6xl font-bold text-red-500 mb-4">404</h1>
    <p class="text-xl text-gray-700 mb-6">
      {{ t("notFound") }}
    </p>
    <router-link
        to="/"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition"
    >
      {{ t("backToHome") }}
    </router-link>
  </div>
</template>