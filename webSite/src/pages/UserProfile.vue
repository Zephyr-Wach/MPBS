<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
interface UserProfileItem {
  key: string
  label: string
  value: any
}

const userProfile = ref<UserProfileItem[]>([])

const getValue = (key: string) => userProfile.value.find(i => i.key === key)?.value

const editingCard = ref('') // '': 'basic', 'contact', 'security'
const formData = reactive({
  avatar: '',
  name: '',
  bio: '',
  email: '',
  location: '',
  website: '',
  company: '',
  phone: '',
  oldPassword:'',
  newPassword:'',
  confirmPassword:'',
  twoFA: false
})

interface UserProfile {
  avatar?: string
  name?: string
  username?: string
  bio?: string
  email?: string
  location?: string
  website?: string
  company?: string
  phone?: string
  twoFA?: boolean
}

// 从后端获取用户信息
const setUserProfile = async () => {
  try {
    // TODO fetch UserProfile
    // const res = await fetchUserProfile()
    // const data: UserProfile = res.data.data
    // userProfile.value = (Object.keys(data) as (keyof UserProfile)[]).map(key => ({
    //   key: key as string,
    //   label: key,
    //   value: data[key as keyof UserProfile]
    // }))
  } catch (err) {
    console.error('get user profile error:', err)
  }
}

const openEdit = (card: string) => {
  editingCard.value = card
  if (card === 'basic') {
    formData.avatar = getValue('avatar')
    formData.name = getValue('name')
    formData.bio = getValue('bio')
  } else if (card === 'contact') {
    formData.email = getValue('email')
    formData.location = getValue('location')
    formData.website = getValue('website')
    formData.company = getValue('company')
  } else if (card === 'security') {
    formData.phone = getValue('phone')
    formData.twoFA = getValue('2fa')
  }
}

const closeEdit = () => editingCard.value = ''

const saveEdit = () => {
  if (editingCard.value === 'basic') {
    ['avatar', 'name', 'bio'].forEach(key => {
      const item = userProfile.value.find(i => i.key === key)
      if (item) item.value = formData[key as keyof typeof formData]
    })
    console.log('PATCH /user/profile:', formData.avatar, formData.name, formData.bio)
  } else if (editingCard.value === 'contact') {
    ['email', 'location', 'website', 'company'].forEach(key => {
      const item = userProfile.value.find(i => i.key === key)
      if (item) item.value = formData[key as keyof typeof formData]
    })
    console.log('PATCH /user/contact:', formData.email, formData.location, formData.website, formData.company)
  } else if (editingCard.value === 'security') {
    ['phone', 'twoFA'].forEach(key => {
      const item = userProfile.value.find(i => i.key === key)
      if (item) item.value = formData[key as keyof typeof formData]
    })
    console.log('PATCH /user/security:', formData.phone, formData.twoFA)
  } else if (editingCard.value === 'securityPassword') {
    console.log('PATCH /user/securityPwd:', formData.oldPassword, formData.newPassword, formData.confirmPassword)
  }
  closeEdit()
}

onMounted(() => {
  setUserProfile()
})
</script>

<template>
  <div class="h-full bg-gray-100 p-6 ">
    <div class="max-w-5xl mx-auto space-y-6">

      <!-- avatar & basic info -->
      <div class="relative bg-white rounded-2xl shadow-lg p-8 flex flex-col md:flex-row items-center md:items-start gap-6">
        <img
            :src="getValue('avatar')"
            class="w-36 h-36 rounded-full border-4 border-blue-200 shadow-lg cursor-pointer"
            @click="openEdit('basic')"
        />
        <div class="flex-1 space-y-2 text-center md:text-left">
          <h1 class="text-3xl font-bold">{{ getValue('name') }}</h1>
          <p class="text-gray-500 text-lg">@{{ getValue('username') }}</p>
          <p class="text-gray-700">{{ getValue('bio') }}</p>
        </div>
        <button
            @click="openEdit('basic')"
            class="absolute bottom-4 right-4 px-4 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
        >{{ t("change") }}</button>
      </div>

      <!-- Statistics -->
      <div class="bg-white rounded-2xl shadow-lg p-6 flex justify-around">
        <div class="text-center">
          <p class="font-bold text-xl">{{ getValue('repos') }}</p>
          <p class="text-gray-500">{{ t("Repositories") }}</p>
        </div>
        <div class="text-center">
          <p class="font-bold text-xl">{{ getValue('followers') }}</p>
          <p class="text-gray-500">{{ t("Followers") }}</p>
        </div>
        <div class="text-center">
          <p class="font-bold text-xl">{{ getValue('following') }}</p>
          <p class="text-gray-500">{{ t("Following") }}</p>
        </div>
      </div>

      <div class="flex flex-col md:flex-row gap-6">

        <!-- Contact -->
        <div class="bg-white rounded-2xl shadow-lg p-6 flex-1">
          <div class="flex justify-between items-center mb-2">
            <h2 class="text-xl font-semibold">{{ t("ContactInfo") }}</h2>
            <button
                @click="openEdit('contact')"
                class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
            >{{ t("change") }}</button>
          </div>
          <ul class="space-y-1 text-gray-700">
            <li><span class="font-semibold">{{ t("email") }}:</span> {{ getValue('email') }}</li>
            <li><span class="font-semibold">{{ t("Location") }}:</span> {{ getValue('location') }}</li>
            <li>
              <span class="font-semibold">{{ t("Website") }}:</span>
              <a :href="getValue('website')" target="_blank" class="text-blue-500 hover:underline">
                {{ getValue('website') }}
              </a>
            </li>
            <li><span class="font-semibold">{{ t("Company") }}:</span> {{ getValue('company') }}</li>
          </ul>
        </div>

        <!-- Security Info -->
        <div class="bg-white rounded-2xl shadow-lg p-6 flex-1">
          <div class="flex justify-between items-center mb-2">
            <h2 class="text-xl font-semibold">{{ t("Security") }}</h2>
          </div>
          <ul class="space-y-2 text-gray-700">
            <li class="relative">
              <span class="font-semibold">{{ t("pwd") }}:</span> ******
              <button
                  @click="openEdit('securityPassword')"
                  class="absolute right-0 px-2 py-0.5 bg-blue-500 text-white rounded hover:bg-blue-600 text-sm"
              >
                {{ t("changePwd") }}
              </button>
            </li>

            <li class="relative">
              <span class="font-semibold">{{ t("Phone") }}:</span> {{ getValue('phone') || t('unbound') }}
              <button
                  @click="openEdit('security')"
                  class="absolute right-0 px-2 py-0.5 bg-blue-500 text-white rounded hover:bg-blue-600 text-sm"
              >
                {{ t("changePhone") }}
              </button>
            </li>

            <li>
              <span class="font-semibold">{{ t("Two-factor") }}</span> {{ getValue('2fa') ? t('enabled') : t('disabled') }}
            </li>
          </ul>


        </div>

      </div>

      <!-- Account Info -->
      <div class="bg-white rounded-2xl shadow-lg p-6 space-y-2">
        <h2 class="text-xl font-semibold mb-2">{{ t("AccountInfo") }}</h2>
        <p><span class="font-semibold">{{ t("Joined") }}:</span> {{ getValue('joined') }}</p>
        <p><span class="font-semibold">{{ t("userName") }}:</span> {{ getValue('username') }}</p>
      </div>

    </div>

    <div v-if="editingCard" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white w-full max-w-lg rounded-2xl shadow-lg p-6">
        <h2 class="text-2xl font-bold mb-4">
          {{ editingCard === 'basic' ? t('editBasicInfo') : editingCard === 'contact' ? t('editContactInfo') : t('editSecurityInfo') }}
        </h2>
        <form @submit.prevent="saveEdit" class="space-y-3">

          <template v-if="editingCard==='basic'">
            <label class="block">
              <span class="font-semibold">{{ t("AvatarURL") }}</span>
              <input type="text" v-model="formData.avatar" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block">
              <span class="font-semibold">{{ t("Name") }}</span>
              <input type="text" v-model="formData.name" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block">
              <span class="font-semibold">{{ t("Bio") }}</span>
              <textarea v-model="formData.bio" class="w-full p-2 border rounded mt-1"></textarea>
            </label>
          </template>

          <template v-if="editingCard==='contact'">
            <label class="block">
              <span class="font-semibold">{{ t("email") }}</span>
              <input type="email" v-model="formData.email" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block">
              <span class="font-semibold">{{ t("Location") }}</span>
              <input type="text" v-model="formData.location" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block">
              <span class="font-semibold">{{ t("Website") }}</span>
              <input type="text" v-model="formData.website" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block">
              <span class="font-semibold">{{ t("Company") }}</span>
              <input type="text" v-model="formData.company" class="w-full p-2 border rounded mt-1" />
            </label>
          </template>

          <template v-if="editingCard==='security'">
            <label class="block">
              <span class="font-semibold">{{ t("Phone") }}</span>
              <input type="text" v-model="formData.phone" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block flex items-center gap-2 mt-2">
              <input type="checkbox" v-model="formData.twoFA" />
              <span class="font-semibold">{{ t("Two-factor") }}</span>
            </label>
          </template>

          <template v-if="editingCard==='securityPassword'">
            <label class="block mt-2">
              <span class="font-semibold">{{ t("oldPwd") }}</span>
              <input type="password" v-model="formData.oldPassword" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block mt-2">
              <span class="font-semibold">{{ t("newPwd") }}</span>
              <input type="password" v-model="formData.newPassword" class="w-full p-2 border rounded mt-1" />
            </label>
            <label class="block mt-2">
              <span class="font-semibold">{{ t("confirmNewPwd") }}</span>
              <input type="password" v-model="formData.confirmPassword" class="w-full p-2 border rounded mt-1" />
            </label>
          </template>

          <div class="flex justify-end gap-2 mt-4">
            <button type="button" @click="closeEdit" class="px-4 py-1 border rounded">{{ t("cancel") }}</button>
            <button type="submit" class="px-4 py-1 bg-blue-500 text-white rounded hover:bg-blue-600">{{ t("save") }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
