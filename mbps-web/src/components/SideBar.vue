<template>
  <div class="sidebar">
    <ul class="menu">
      <p>MBPS</p>
      <li v-for="item in menuList" :key="item.id">
        <div class="menu-item">
          <router-link v-if="item.path" :to="item.path">{{ item.name }}</router-link>
          <span v-else>{{ item.name }}</span>
        </div>

        <!-- 子菜单递归渲染 -->
        <ul v-if="item.children && item.children.length" class="submenu">
          <li v-for="child in item.children" :key="child.id">
            <div class="menu-item">
              <router-link v-if="child.path" :to="child.path">{{ child.name }}</router-link>
              <span v-else>{{ child.name }}</span>
            </div>

            <!-- 如果还有更深层子菜单，可以继续递归，这里不再演示（通常两层就够） -->
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getSideBarList } from '@/api/public/index.ts';

interface MenuItem {
  id: number;
  name: string;
  path: string | null;
  icon: string | null;
  children: MenuItem[];
  parentId: number | null;
}

const menuList = ref<MenuItem[]>([]);

onMounted(async () => {
  try {
    const res = await getSideBarList();
    menuList.value = res || [];
  } catch (error) {
    console.error('获取菜单失败:', error);
  }
});
</script>

<style scoped>
.sidebar {
  width: 200px;
  background-color: #f8f9fa;
  padding: 10px;
}
.menu {
  list-style: none;
  padding: 0;
  margin: 0;
}
.menu-item {
  padding: 5px 10px;
  font-weight: bold;
}
.submenu {
  list-style: none;
  padding-left: 15px;
}
</style>
