<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/composables/useAuth'

const showDropdown = ref(false)
const router = useRouter()

const { isLoggedIn, logout } = useAuth()

function handleLogout(): void {
  logout()
  router.push('/')
}
</script>

<template>
  <header class="navbar">
    <a href="/" class="logo" aria-label="首页">
      <img src="@/assets/logo.ico" alt="网站Logo" class="logo-img" />
    </a>
    <nav>
      <ul class="nav-links">
        <li v-if="!isLoggedIn">
          <!-- 未登录显示登录链接 -->
          <router-link to="/profile" class="dropdown-toggle">登录</router-link>
        </li>
        <li
            v-else
            class="dropdown"
            @mouseenter="showDropdown = true"
            @mouseleave="showDropdown = false"
        >
          <!-- 已登录显示个人中心并带下拉菜单 -->
          <router-link to="/profile" class="dropdown-toggle">个人中心</router-link>
          <transition name="fade">
            <div v-if="showDropdown" class="dropdown-menu">
              <button class="dropdown-item" @click="handleLogout">退出登录</button>
            </div>
          </transition>
        </li>
      </ul>
    </nav>
  </header>
</template>



<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 60px;
  background: linear-gradient(to right, #ffffff, #2de2be);
  color: #2c3e50;
  padding: 0 1rem 0 1px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 1000;
  box-sizing: border-box;
}

.logo {
  display: flex;
  align-items: center;
  background-color: #ffffff;
  padding: 5px 10px;
  border-radius: 4px;
  height: 40px;
}

.logo-img {
  height: 30px;
  width: auto;
}

.nav-links {
  list-style: none;
  display: flex;
  gap: 1.5rem;
  margin: 0;
  padding: 0;
}

.nav-links li {
  position: relative;
}

.nav-links a {
  color: #2c3e50;
  text-decoration: none;
  font-weight: 500;
  cursor: pointer;
}

.nav-links a:hover {
  text-decoration: underline;
}

.dropdown-toggle {
  padding: 0.5rem 0.75rem;
  display: inline-block;
}

.dropdown-menu {
  position: absolute;
  top: 110%;
  left: 0;
  background: white;
  border-radius: 8px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
  min-width: 140px;
  padding: 0.5rem 0;
  z-index: 2000;
  user-select: none;
  transition: opacity 0.25s ease, transform 0.25s ease;
}

.dropdown-item {
  padding: 10px 16px;
  cursor: pointer;
  color: #333;
  white-space: nowrap;
  border: none;
  background: none;
  font-size: 1rem;
  width: 100%;
  text-align: left;
  transition: background-color 0.2s ease;
}

.dropdown-item:hover {
  background-color: #2de2be;
  color: white;
  border-radius: 8px;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.25s ease, transform 0.25s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.fade-enter-to, .fade-leave-from {
  opacity: 1;
  transform: translateY(0);
}
</style>
