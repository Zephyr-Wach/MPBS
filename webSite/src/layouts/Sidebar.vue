<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { RouterLink } from 'vue-router'
import { fetchSideBar } from "@/api/general.ts";

interface SideBarItem {
  id: number
  name: string
  path?: string
  children?: SideBarItem[]
}

defineProps<{ isMobile?: boolean }>()
const { t } = useI18n()

// 默认菜单
const sideBar = ref<SideBarItem[]>([
  { id: 1, name: "Home", path: "/" },
  { id: 2, name: "userCenter", path: "/userCenter" },
  {
    id: 3,
    name: "List1",
    children: [
      { id: 31, name: "a1", path: "/t1" },
      { id: 32, name: "a2", path: "/t2" },
    ]
  },
  {
    id: 4,
    name: "List2",
    children: [
      { id: 41, name: "b1", path: "/a" },
      { id: 42, name: "b2", path: "/b" },
    ]
  }
])

const collapseState = ref<Record<number, boolean>>({})
const initCollapseState = (menu: SideBarItem[]) => {
  collapseState.value = {}
  menu.forEach(item => {
    if (item.children) collapseState.value[item.id] = false
  })
}
initCollapseState(sideBar.value)

const activeId = ref<number | null>(null)
const setActive = (id: number) => activeId.value = id

const toggle = (item: SideBarItem) => {
  if (!item.children) return
  collapseState.value[item.id] = !collapseState.value[item.id]
  activeId.value = item.id
}

const setSideBarMenu = async () => {
  try {
    const res = await fetchSideBar()
    sideBar.value = res.data.data;
    initCollapseState(sideBar.value)
  } catch (err) {
    console.error('get sidebar error:', err)
  }
}

onMounted(() => {
  setSideBarMenu()
})
</script>

<template>
  <aside
      class="bg-white/70 backdrop-blur-md border-r border-gray-200 w-64 p-4 flex flex-col overflow-y-auto h-full transition-all"
      :class="{ 'hidden md:flex': !isMobile }"
  >
    <h2 class="text-xl font-semibold mb-6">{{ t("menu") }}</h2>
    <nav class="flex flex-col gap-3">
      <template v-for="item in sideBar" :key="item.id">

        <RouterLink
            v-if="item.path"
            :to="item.path!"
            @click="setActive(item.id)"
            :class="[
            'px-3 py-2 rounded-lg transition-colors duration-200 hover:bg-blue-100',
            activeId === item.id ? 'bg-blue-200 text-white' : ''
          ]"
        >
          {{ t(item.name) }}
        </RouterLink>

        <div v-else class="flex flex-col">
          <div
              class="px-3 py-2 font-semibold cursor-pointer flex justify-between items-center rounded-lg transition-colors duration-200 hover:bg-blue-100"
              @click="toggle(item)"
              :class="activeId === item.id ? 'bg-blue-200 text-white' : ''"
          >
            {{ t(item.name) }}
            <span class="ml-2 transition-transform" :class="collapseState[item.id] ? 'rotate-45' : 'rotate-0'">+</span>
          </div>

          <transition name="slide-fade">
            <div
                v-show="collapseState[item.id]"
                class="ml-4 mt-2 flex flex-col gap-2"
            >
              <RouterLink
                  v-for="child in item.children"
                  :key="child.id"
                  :to="child.path!"
                  @click="setActive(child.id)"
                  :class="[
                  'px-3 py-2 rounded-lg transition-colors duration-200 hover:bg-blue-100',
                  activeId === child.id ? 'bg-blue-200 text-white' : ''
                ]"
              >
                {{ t(child.name) }}
              </RouterLink>
            </div>
          </transition>
        </div>
      </template>
    </nav>
  </aside>
</template>

<style scoped>
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
