<template>
  <header class="navbar">
    <h1 class="logo">
      <img src="@/assets/logo.ico" alt="logo" class="logo-img" />
    </h1>
    <nav>
      <ul class="nav-links">
        <router-link to="/about">关于</router-link>
<!--        <li><a href="#contact">联系</a></li>-->

        <router-link to="/profile">个人中心</router-link>
      </ul>
    </nav>
  </header>
</template>

<script setup>
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 60px;
  /* 从左到右，渐变 */
  background: linear-gradient(to right, #ffffff, #2de2be);
  color: #2c3e50; /* 文字深色，和背景对比 */
  padding: 0 1rem 0 1px; /* 上右下左 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 1000;
  box-sizing: border-box;
}

/* logo区域背景白色，和渐变区区分 */
.logo {
  height: 30px;
  font-size: 1.5rem;
  font-weight: bold;
  display: flex;
  align-items: center;
  background-color: #ffffff;
  padding: 5px 10px;
  border-radius: 4px;
}

.logo-img {
  height: 40px;
  width: auto;
}

.nav-links {
  list-style: none;
  display: flex;
  gap: 1.5rem;
}

.nav-links a {
  color: #2c3e50; /* 深色文字 */
  text-decoration: none;
  font-weight: 500;
}

.nav-links a:hover {
  text-decoration: underline;
}
</style>
