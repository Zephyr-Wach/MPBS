
# MPBS

#### 介绍
my personal blog site

# MPBS - 多模块博客系统

这是一个基于 Java + Gradle 的多模块博客系统，项目包含用户管理、认证授权、博客管理、文件管理等模块，采用分层架构设计。

## 项目结构

| 模块名         | 说明                   |
| ------------- | ---------------------- |
| mpbs-app      | 应用层，启动入口       |
| mpbs-auth     | 认证与授权模块         |
| mpbs-blog     | 博客内容管理模块       |
| mpbs-common   | 公共工具与基础模块     |
| mpbs-files    | 文件上传与管理模块     |
| mpbs-security | 安全相关配置           |
| mpbs-user     | 用户管理模块           |
| web           | 前端界面               |

## 技术栈

- Java 8
- Spring Boot
- Gradle 构建工具
- MyBatis 、 JPA
- MySQL 数据库
- Vue.js（前端模块 `web`）

## 快速开始

1. 克隆项目、

```
   git clone https://gitee.com/Zephyr-Wach/mpbs.git
   cd mpbs
```

2. 配置
   修改 application.yml，填写你的相关信息。

3.启动服务
```
./gradlew :mpbs-app:bootRun
```
4.前端启动
```
cd web
npm install
npm run serve
```

## 使用说明

* 访问 [http://localhost:80](http://localhost:80) 查看博客主页
* 管理员账号初始用户名/密码：admin/admin（请上线后修改）
* 支持文章增删改查，图片上传

## 贡献指南

欢迎提出issue或提交PR，一起完善博客功能。

---

感谢你的使用和关注！


