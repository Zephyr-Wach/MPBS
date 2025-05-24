# MPBS - 多模块博客系统

MPBS（My Personal Blog Site）是一个基于 Java + Gradle 构建的多模块博客系统，支持用户认证、博客管理、文件上传、角色权限、媒体展示等功能，适合学习与个人博客部署。

- 🌐 演示站点：[http://mpbs.tech](http://mpbs.tech)
- 🪪 开源协议：[MIT License](LICENSE)
- 👤 项目作者：Zephyr
- 📥 欢迎贡献：Issue / PR

---

## 🏗️ 项目结构

| 模块名         | 说明                   |
| ------------- | ---------------------- |
| `mpbs-app`      | 应用层，启动入口       |
| `mpbs-auth`     | 认证与授权模块         |
| `mpbs-blog`     | 博客内容管理模块       |
| `mpbs-common`   | 公共工具与基础模块     |
| `mpbs-files`    | 文件上传与管理模块     |
| `mpbs-security` | 安全相关配置           |
| `mpbs-user`     | 用户管理模块           |
| `web`           | 前端界面（Vue 3）      |

---

## 🖼️ 界面预览

1.管理面板

![云盘](/web/public/1-1.png)
![用户管理](/web/public/1-2.png)
![Blog管理](/web/public/1-3.png)
![媒体管理](/web/public/1-4.png)
![发表Blog](/web/public/1-5.png)
![个人中心](/web/public/2-4.png)

2.游客面板
![首页](/web/public/2-1.png)
![Blog](/web/public/2-2.png)
![关于&联系](/web/public/2-3.png)

## 🧰 技术栈

- Java 8、Spring Boot、MyBatis、JPA
- MySQL 数据库
- Gradle 构建工具
- Vue 3 + Vite（前端）

---

## 🚀 快速开始

### 1. 克隆项目

```
git clone https://gitee.com/Zephyr-Wach/mpbs.git
cd mpbs
```

2. 配置
   修改 application.yml，填写你的相关信息。

3. 启动服务
```
./gradlew :mpbs-app:bootRun
```
4. 前端启动
```
cd web
npm install
npm run serve
```

## 📘 使用说明

* 访问 [http://localhost:80](http://localhost:80) 查看博客主页
* 管理员账号初始用户名/密码：admin/admin（请上线后修改）
* 支持文章增删改查，图片上传

## 🤝 贡献指南

欢迎提出issue或提交PR，一起完善博客功能。

---

感谢你的使用和关注！

## 📄 开源许可
本项目采用 MIT 协议开源，详情请见 [LICENSE](./LICENSE) 文件。
