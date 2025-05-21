<template>
  <div class="sidebar">
    <p class="sidebar-title">我的博客</p>
    <ul class="menu">
      <li v-for="item in menuList" :key="item.id" class="menu-item">
        <router-link
            v-if="item.path"
            :to="item.path === '' ? '/' : item.path"
            class="menu-link"
            active-class="active-link"
            exact-active-class="active-link"
            :exact="item.path === '/' || item.path === ''"
        >
          <i v-if="item.icon" :class="item.icon"></i>
          {{ item.name }}
        </router-link>


        <div v-else class="menu-link disabled">
          <i v-if="item.icon" :class="item.icon"></i>
          {{ item.name }}
        </div>

        <ul v-if="item.children && item.children.length" class="submenu">
          <li v-for="child in item.children" :key="child.id" class="submenu-item">
            <router-link
                v-if="child.path"
                :to="child.path"
                class="menu-link"
                active-class="active-link"
                exact
            >
              <i v-if="child.icon" :class="child.icon"></i>
              {{ child.name }}
            </router-link>
            <div v-else class="menu-link disabled">
              <i v-if="child.icon" :class="child.icon"></i>
              {{ child.name }}
            </div>
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getSideBarList } from '@/api/public';

interface MenuItem {
  id: number;
  name: string;
  path: string | null;
  icon: string | null; // 这里可以是iconfont或字体图标的类名
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
  width: 220px;
  background-color: #fff;
  border-right: 1px solid #ddd;
  padding: 20px 15px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  height: 100vh;
  overflow-y: auto;
  box-sizing: border-box;
}

.sidebar-title {
  font-size: 1.4rem;
  font-weight: 700;
  margin-bottom: 25px;
  color: #333;
  user-select: none;
  padding-left: 8px;
  border-left: 4px solid #409eff; /* Vue蓝色 */
}

.menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  margin-bottom: 8px;
}

.menu-link {
  display: flex;
  align-items: center;
  color: #555;
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 4px;
  font-weight: 600;
  transition: background-color 0.2s ease, color 0.2s ease;
  cursor: pointer;
}

.menu-link i {
  margin-right: 10px;
  font-size: 16px;
  width: 18px;
  text-align: center;
  color: #909399;
}

.menu-link:hover {
  background-color: #409eff;
  color: #fff;
}

.active-link {
  background-color: #409eff;
  color: #fff;
  font-weight: 700;
}

.disabled {
  cursor: default;
  color: #bbb;
}

.submenu {
  list-style: none;
  padding-left: 25px;
  margin-top: 6px;
  border-left: 2px solid #f0f0f0;
}

.submenu-item {
  margin-bottom: 6px;
}

.submenu .menu-link {
  font-weight: 500;
  font-size: 0.95rem;
  padding: 6px 12px;
}

.submenu .menu-link i {
  font-size: 14px;
  color: #c0c4cc;
}

.submenu .menu-link:hover {
  background-color: #66b1ff;
}

.submenu .active-link {
  background-color: #66b1ff;
  color: #fff;
  font-weight: 600;
}
</style>
