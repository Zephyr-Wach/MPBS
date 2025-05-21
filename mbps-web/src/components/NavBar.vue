<template>
  <header class="navbar">
    <h1 class="logo">我的博客</h1>
    <nav>
      <ul class="nav-links">
        <li><a href="#about">关于</a></li>
        <li><a href="#contact">联系</a></li>
        <li><a href="#profile">个人中心</a></li>
      </ul>
    </nav>
  </header>
</template>


<script setup>
// 这里可以添加逻辑，比如响应式菜单、点击事件等
</script>

<style scoped>

.navbar {
  position: fixed;      /* 固定在视口顶部 */
  top: 0;
  left: 0;
  width: 100vw;         /* 100视口宽度，确保铺满 */
  height: 60px;         /* 给一个固定高度 */
  background-color: #2c3e50;
  color: #fff;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 1000;        /* z-index高于sidebar，确保覆盖 */
  box-sizing: border-box;
}
.content-area {
  margin-top: 60px; /* navbar高度 */
  display: flex;
  height: calc(100vh - 60px);
}




.logo {
  font-size: 1.5rem;
  font-weight: bold;
}

.nav-links {
  list-style: none;
  display: flex;
  gap: 1.5rem;
}

.nav-links a {
  color: #fff;
  text-decoration: none;
  font-weight: 500;
}

.nav-links a:hover {
  text-decoration: underline;
}
</style>
