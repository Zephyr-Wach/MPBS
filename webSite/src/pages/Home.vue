<template>
  <div class="home h-full">
    <div class="hero">
      <h1 class="hero-title">欢迎来到我的博客 👋</h1>
      <p class="hero-subtitle">
        分享知识，记录生活，探索技术的无限可能。
      </p>
    </div>

    <section class="notice" ref="noticeSection">
      <h2>最新公告</h2>
      <ul>
        <li>📢 不定期更新分享技术文章，敬请关注。</li>
        <li>✨ 欢迎提出宝贵意见和建议！</li>
      </ul>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const noticeSection = ref<HTMLElement | null>(null);
</script>

<style scoped>
.home {
  min-height: 100vh;
  background: linear-gradient(135deg, #2de2be 0%, #409eff 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #fff;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  padding: 3rem 1rem 4rem;
  box-sizing: border-box;
}

.hero {
  max-width: 700px;
  text-align: center;
  padding: 3rem 2rem;
  background: rgba(255 255 255 / 0.1);
  border-radius: 20px;
  box-shadow: 0 8px 20px rgb(0 0 0 / 0.15);
  margin-bottom: 4rem;
  user-select: none;
  transition: transform 0.3s ease;
}
.hero:hover {
  transform: scale(1.03);
}

.hero-title {
  font-size: 3.6rem;
  font-weight: 800;
  margin-bottom: 1rem;
  text-shadow: 0 2px 10px rgba(45, 226, 190, 0.6);
}

.hero-subtitle {
  font-size: 1.4rem;
  line-height: 1.6;
  margin-bottom: 2.5rem;
  color: #e0f7f5;
  user-select: text;
}

.btn-primary {
  background: #fff;
  color: #409eff;
  font-weight: 700;
  padding: 0.8rem 2.5rem;
  font-size: 1.1rem;
  border: none;
  border-radius: 30px;
  cursor: pointer;
  box-shadow: 0 6px 12px rgb(64 158 255 / 0.5);
  transition: background-color 0.3s ease, color 0.3s ease;
  user-select: none;
}
.btn-primary:hover {
  background: #2de2be;
  color: #fff;
  box-shadow: 0 8px 16px rgb(45 226 190 / 0.7);
}

.notice {
  max-width: 700px;
  background: #ddd;
  color: #333;
  border-radius: 16px;
  padding: 2rem 3rem;
  box-shadow: 0 6px 20px rgb(0 0 0 / 0.1);
  user-select: none;
  font-weight: 600;
}

.notice h2 {
  margin-bottom: 1.5rem;
  color: #409eff;
  user-select: text;
  font-size: 2rem;
}

.notice ul {
  list-style: inside disc;
  line-height: 1.6;
  font-size: 1.1rem;
  padding-left: 0;
}

.notice li {
  margin-bottom: 1rem;
}
</style>
