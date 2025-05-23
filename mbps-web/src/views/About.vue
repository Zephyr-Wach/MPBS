<template>
  <div class="about-contact">
    <section class="about">
      <h1>关于我</h1>
      <p>
        这是我的个人网站，主要用于分享和记录技术知识、学习日志以及项目经验。
        希望这里的内容能帮助到同样热爱技术的你。
      </p>
    </section>

    <section class="contact">
      <h2>联系方式</h2>
      <ul>
        <li>
          <strong>邮箱：</strong>
          <a href="mailto:wangc2801@outlook.com">wangc2801@outlook.com</a>
        </li>
        <li>
          <strong>GitHub：</strong>
          <a href="https://github.com/Zephyr-Wach" target="_blank" rel="noopener noreferrer">
            https://github.com/Zephyr-Wach
          </a>
        </li>
        <li>
          <strong>Gitee：</strong>
          <a href="https://gitee.com/Zephyr-Wach" target="_blank" rel="noopener noreferrer">
            https://gitee.com/Zephyr-Wach
          </a>
        </li>
      </ul>
    </section>
  </div>
</template>

<script setup lang="ts">
// 目前无逻辑
</script>

<style scoped>
.about-contact {
  max-width: 720px;
  margin: 3rem auto;
  padding: 2.5rem 2rem;
  background: #f9fafa;
  border-radius: 14px;
  box-shadow: 0 6px 18px rgb(0 0 0 / 0.08);
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  color: #333;
  user-select: text;
}

.about {
  margin-bottom: 3rem;
}

.about h1 {
  color: #2de2be;
  font-size: 2.8rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.about p {
  font-size: 1.2rem;
  line-height: 1.7;
  color: #555;
}

.contact h2 {
  color: #409eff;
  font-size: 2.4rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.contact ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.contact li {
  font-size: 1.1rem;
  margin-bottom: 1rem;
  line-height: 1.5;
}

.contact strong {
  color: #2de2be;
  width: 80px;
  display: inline-block;
}

.contact a {
  color: #409eff;
  text-decoration: none;
  transition: color 0.3s ease;
  user-select: text;
}

.contact a:hover {
  color: #2de2be;
  text-decoration: underline;
}
</style>
