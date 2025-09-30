<script setup lang="ts">
import { reactive, ref, computed } from "vue"
import { useI18n } from "vue-i18n"
import { loginByEmail} from "@/api/users.ts";
import {useUserStore} from "@/store/userStore.ts";
import { useLoginModal } from '@/store/loginModal'

const loginModal = useLoginModal()
const userStore = useUserStore()
const { t } = useI18n()
const mode = ref<"login" | "register" | "forgot">("login")

const form = reactive({
  username: "",
  email: "",
  password: "",
  confirmPassword: ""
})

const errors = reactive({
  username: "",
  email: "",
  password: "",
  confirmPassword: ""
})

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

const modeText = computed(() => {
  if (mode.value === "login") return "login"
  if (mode.value === "register") return "register"
  return "refindPwd"
})

// 表单校验
const validate = () => {
  // 清空所有错误
  errors.username = ""
  errors.email = ""
  errors.password = ""
  errors.confirmPassword = ""

  let valid = true

  if (!emailRegex.test(form.email)) {
    errors.email = "enterIllegalEmail"
    valid = false
  }

  if (mode.value === "register") {
    if (!form.username.trim()) {
      errors.username = "enterUserName"
      valid = false
    }
    if (!form.password) {
      errors.password = "enterPwd"
      valid = false
    }
    if (form.password !== form.confirmPassword) {
      errors.confirmPassword = "differentPwdError"
      valid = false
    }
  }

  if (mode.value === "login") {
    if (!form.password) {
      errors.password = "enterPwd"
      valid = false
    }
  }

  if (mode.value === "forgot") {

  }

  return valid
}

const handleSubmit = async () => {
  if (!validate()) return

  if (mode.value === "login") {
    try {
      const user = { email: form.email, userPwd: form.password }
      const res = await loginByEmail(user)
      userStore.login({
        userId: res.data.data.userId,
        userName: res.data.data.userName,
        token: res.data.data.token,
        refreshToken: res.data.data.refreshToken
      })
      loginModal.close()
    } catch (err) {
      console.error("login error:", err)
    }
  } else if (mode.value === "register") {
    // TODO register
    console.log("register:", form.username, form.email, form.password)
  } else {
    // TODO forget pwd
    console.log("forgot pwd:", form.email)
  }
}

</script>

<template>
  <div class="w-full max-w-md">
    <h2 class="text-2xl font-bold text-center mb-6">{{ t(modeText) }}</h2>

    <form @submit.prevent="handleSubmit" class="space-y-4 relative" novalidate>
    <!-- login -->
      <transition
          enter-active-class="transition duration-300 ease-out"
          enter-from-class="opacity-0 translate-x-6"
          enter-to-class="opacity-100 translate-x-0"
          leave-active-class="transition duration-200 ease-in absolute w-full"
          leave-from-class="opacity-100 translate-x-0"
          leave-to-class="opacity-0 -translate-x-6"
      >
        <div v-if="mode==='login'" key="login">
          <div>
            <label class="block text-sm font-medium mb-1">{{ t("email")}}</label>
            <input
                v-model="form.email"
                type="email"
                class="w-full rounded-lg border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :placeholder="t('enterEmail')"
            />
            <p v-if="errors.email" class="text-red-500 text-sm mt-1">{{ t(errors.email) }}</p>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">{{ t("pwd") }}</label>
            <input
                v-model="form.password"
                type="password"
                class="w-full rounded-lg border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :placeholder="t('enterPwd')"
            />
            <p v-if="errors.password" class="text-red-500 text-sm mt-1">{{ t(errors.password) }}</p>
          </div>
        </div>
      </transition>

      <!-- register -->
      <transition
          enter-active-class="transition duration-300 ease-out"
          enter-from-class="opacity-0 -translate-x-6"
          enter-to-class="opacity-100 translate-x-0"
          leave-active-class="transition duration-200 ease-in absolute w-full"
          leave-from-class="opacity-100 translate-x-0"
          leave-to-class="opacity-0 translate-x-6"
      >
        <div v-if="mode==='register'" key="register">
          <div>
            <label class="block text-sm font-medium mb-1">{{ t("userName") }}</label>
            <input
                v-model="form.username"
                type="text"
                class="w-full rounded-lg border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :placeholder="t('enterUserName')"
            />
            <p v-if="errors.username" class="text-red-500 text-sm mt-1">{{ t(errors.username) }}</p>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">{{ t("email") }}</label>
            <input
                v-model="form.email"
                type="email"
                class="w-full rounded-lg border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :placeholder="t('enterEmail')"
            />
            <p v-if="errors.email" class="text-red-500 text-sm mt-1">{{ t(errors.email) }}</p>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">{{ t("pwd") }}</label>
            <input
                v-model="form.password"
                type="password"
                class="w-full rounded-lg border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :placeholder="t('enterPwd')"
            />
            <p v-if="errors.password" class="text-red-500 text-sm mt-1">{{ t(errors.password) }}</p>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">{{ t("confirmPwd") }}</label>
            <input
                v-model="form.confirmPassword"
                type="password"
                class="w-full rounded-lg border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :placeholder="t('enterConfirmPwd')"
            />
            <p v-if="errors.confirmPassword" class="text-red-500 text-sm mt-1">{{ t(errors.confirmPassword) }}</p>
          </div>
        </div>
      </transition>

      <!-- forget pwd -->
      <transition
          enter-active-class="transition duration-300 ease-out"
          enter-from-class="opacity-0 translate-y-4"
          enter-to-class="opacity-100 translate-y-0"
          leave-active-class="transition duration-200 ease-in absolute w-full"
          leave-from-class="opacity-100 translate-y-0"
          leave-to-class="opacity-0 -translate-y-4"
      >
        <div v-if="mode==='forgot'" key="forgot">
          <div>
            <label class="block text-sm font-medium mb-1">{{ t("email") }}</label>
            <input
                v-model="form.email"
                type="email"
                class="w-full rounded-lg border px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :placeholder="t('enterRegisterEmail')"
            />
            <p v-if="errors.email" class="text-red-500 text-sm mt-1">{{ t(errors.email) }}</p>
          </div>
          <p class="text-gray-500 text-sm mt-2">
            {{ t("sendingPwdEmail") }}
          </p>
        </div>
      </transition>

      <!-- submit -->
      <button
          type="submit"
          class="w-full bg-blue-600 text-white py-2 rounded-lg font-medium hover:bg-blue-700 transition"
      >
        {{ t(modeText) }}
      </button>
    </form>

    <!-- change model -->
    <p class="mt-4 text-center text-sm text-gray-600 space-y-2">
      <template v-if="mode==='login'">
        {{ t("noCount") }}？
        <button @click="mode='register'" class="text-blue-600 hover:underline">{{ t("goRegister") }}</button><br />
        {{ t("forgetPwd") }}？
        <button @click="mode='forgot'" class="text-blue-600 hover:underline">{{ t("refindPwd") }}</button>
      </template>
      <template v-else-if="mode==='register'">
        {{ t("hadCount") }}？
        <button @click="mode='login'" class="text-blue-600 hover:underline">{{ t("goLogin") }}</button>
      </template>
      <template v-else>
        {{ t("gotPwd") }}？
        <button @click="mode='login'" class="text-blue-600 hover:underline">{{ t("backLogin") }}</button>
      </template>
    </p>
  </div>
</template>
