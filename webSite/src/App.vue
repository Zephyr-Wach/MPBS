<template>
  <Layout />

  <transition
      enter-active-class="transition duration-200 ease-out"
      enter-from-class="opacity-0 scale-90"
      enter-to-class="opacity-100 scale-100"
      leave-active-class="transition duration-150 ease-in"
      leave-from-class="opacity-100 scale-100"
      leave-to-class="opacity-0 scale-90"
  >
    <div
        v-if="loginModal.visible"
        class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-xl shadow-lg w-full max-w-lg p-6 relative">
        <button
            @click="loginModal.close"
            class="absolute top-3 right-3 text-gray-500 hover:text-gray-700"
        >
          ✕
        </button>
        <LoginAndRegister />
      </div>
    </div>
  </transition>

</template>

<script setup lang="ts">
import Layout from "./layouts/Layout.vue"
import LoginAndRegister from "@/components/LoginAndRegister.vue"
import { useLoginModal } from "@/store/loginModal"

const loginModal = useLoginModal()
</script>
