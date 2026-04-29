# 🏋️‍♂️ Apex Fitness | 现代高端健身俱乐部管理系统 (Gymnasium Project)

本项目是一个全栈分离的现代化健身房管理系统。系统包含具有微服务架构的强力 Java 后端，以及采用现代沉浸式风格的高端 Vue 3 前端界面。

## 🏗️ 系统架构

### 前端：`gym-front`
前端基于现代 Web 技术栈构建，采用高端具有“力量感”的 UI/UX 设计，为管理人员及会员提供沉浸式操作体验。
- **核心技术栈**：Vue 3 + Composition API、Vite、TypeScript
- **UI 框架及图表**：Element Plus、ECharts、TailwindCSS / SCSS
- **状态管理与路由**：Pinia + \`pinia-plugin-persist\`、Vue Router 4
- **亮点板块**：全屏视频 Hero Section、带有 Glassmorphism（玻璃拟态）和卡片发光交互（Hover Glow）的高端首页仪表盘。

### 后端：`gymnasium-parent-project`
后端采用 Java Spring Boot 构建的微服务 / 分布式架构设计，各业务模块解耦，高内聚低耦合。
包含以下子模块：
- **`gymnasium-common`**：通用模块（存放实体类、服务通用接口、工具类等）
- **`gymnasium-service-course`**：课程服务提供方（课程表、私教分配等）
- **`gymnasium-service-goods`**：商品服务提供方（健身装备、补剂热销商品管理等）
- **`gymnasium-service-member`**：会员服务提供方（会员注册、健身卡管理等）
- **`gymnasium-service-menu`**：菜单 / 权限服务提供方（系统菜单页面控制）
- **`gymnasium-service-role`**：角色服务提供方（员工权限及角色分配）
- **`gymnasium-service-suggest`**：意见建议服务提供方（会员反馈跟踪）
- **`gymnasium-service-user`**：用户服务提供方（系统员工、教练基础管理）
- **`gymnasium-service-web`**：服务消费方（Web API 网关暴露层及接口组合层）

---

## 🚀 快速启动指南

### 1. 启动后端微服务

**前置依赖**：
- Java 8 / 11 / 17 (根据具体配置而定)
- Maven 3.6+
- 需要的中间件（如 MySQL, Redis 等，请参照各服务内 `application.yml` 配置）

**启动方式**：
你需要分别启动负责具体业务的多个服务模块。你可以进入各子模块目录中执行：
```bash
cd gymnasium-parent-project/gymnasium-service-xxx
mvn spring-boot:run
```
*(提示：本项目支持通过 PowerShell 脚本一键启动所有微服务。)*

### 2. 启动前端服务

**前置依赖**：
- Node.js 
- npm / yarn / pnpm

**构建与启动**：
```bash
# 1. 进入前端工程目录
cd gym-front

# 2. 安装前端依赖
npm install

# 3. 本地启动开发服务器
npm run dev

# 4. 生产环境打包 
npm run build
```
前端默认将会运行在：`http://localhost:8080` (基于 `vite.config.ts` 中的配置，或者控制台实际输出端口)。

---

## 🌟 核心功能特性

1. **核心数据数据大盘**：实时统计在线会员、员工教练总数、器材总数与订单数。。
2. **多终端响应式设计**：前端首页集成多段响应式呈现布局，移动端自带极简汉堡菜单交互。
3. **服务网格与高可用**：基于微服务构建的强健后台，保证了高并发预约课程及商场交易时的可靠性。
4. **课程与排课管理**：可视化管理团操课、1V1私教课的状态。
5. **商品与装备管理**：提供基于 ECharts 驱动的“热销商品”、“热销会员卡”、“热销课程”等业务图形化分析大屏。

---

> 版权所有 &copy; 2026. Reserved by Apex Fitness System Project.
