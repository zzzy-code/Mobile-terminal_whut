# 📱 社团活动信息管理应用  
一个用于高校社团的活动查询与报名 Android 应用

[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)]()
[![Build](https://img.shields.io/badge/build-success-brightgreen.svg)]()

> 支持活动发布、在线报名、用户登录等功能，结合 SQLite 和 Data Binding 构建现代化校园社交管理工具。

![App Screenshot](https://via.placeholder.com/300x600/9C27B0/FFFFFF?text=Demo+Image) <!-- 替换为实际截图 -->

---

## 🚀 功能特性

### 👤 用户系统
- 用户注册与登录（SHA-256 加密存储）
- 登录状态持久化（SharedPreferences）
- 一键安全退出

### 📅 活动管理
- 活动创建、编辑、删除（限创建者）
- 活动列表按时间排序展示
- 支持活动详情查看与在线报名
- 报名统计与重复报名限制

### 🔄 实时交互
- ActivityResult API 页面导航
- 列表刷新与数据联动更新
- 报名信息与权限动态控制

---

## 🧱 技术架构

| 模块           | 技术栈                                |
|----------------|----------------------------------------|
| **前端设计**     | Material Design + Jetpack              |
| **数据存储**     | SQLite（本地嵌入式数据库）              |
| **状态管理**     | ViewModel + Data Binding               |
| **用户加密**     | SHA-256 算法                           |
| **依赖管理**     | Gradle 7.x                             |

---

## 🛠️ 快速上手

### ✅ 环境依赖
- Android Studio Arctic Fox 或更高版本
- Android SDK 31+
- Java 11+

### ⚙️ 构建步骤

```bash
# 克隆项目
git clone https://github.com/yourusername/club-activity-app.git
cd club-activity-app
```

启用构建功能（`app/build.gradle`）：

```gradle
android {
    buildFeatures {
        dataBinding true
        viewBinding true
    }
}
```

构建清理建议：

```bash
./gradlew clean build
```

---

## 📂 数据模型（简化）

```kotlin
// 用户表
TABLE_USERS (user_id, username, password_hash)

// 活动表
TABLE_ACTIVITIES (activity_id, title, creator_id, datetime, description)

// 报名表
TABLE_ENROLLMENTS (enroll_id, user_id, activity_id UNIQUE(user_id, activity_id))
```

---

## 🔗 UI 示例代码

### 活动项展示（Data Binding）

```xml
<TextView
    android:text="@{activity.title}"
    android:textSize="18sp"
    android:textStyle="bold" />

<LinearLayout
    android:visibility="@{activity.isCreator ? View.VISIBLE : View.GONE}" />
```

### 安全退出逻辑

```kotlin
fun logout() {
    PreferenceHelper(context).clearSession()
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
}
```

---

## 🖼️ 界面设计一览

| 页面           | 核心组件                  | UI 说明                     |
|----------------|---------------------------|-----------------------------|
| 登录/注册页     | TextInputLayout + 按钮    | 紫白主题，信息提示优化       |
| 活动列表页      | RecyclerView + FAB        | 卡片式布局，支持下拉刷新     |
| 活动详情页      | ScrollView + 条件按钮组   | 分权限显示，分段展示         |
| 活动编辑页      | 表单控件 + 日期选择器     | 表单校验 + 数据双向绑定     |

---

## 🧩 常见问题解答

### ❓ 构建失败：`Couldn't delete R.jar`
> 原因：构建缓存未清理或权限问题  
✅ **解决：**
1. 手动删除 `app/build` 目录  
2. 管理员权限重新打开 Android Studio  
3. 执行 `./gradlew clean` 重试  

### ❓ 数据绑定不生效？
> 常见于布局文件未使用 `<layout>` 包裹  
✅ **排查步骤：**
- 布局顶层是否为 `<layout>`？
- 是否启用了 `dataBinding` 构建选项？
- 变量声明与使用是否匹配？

---

## 📄 License

本项目基于 [MIT License](https://mit-license.org/) 开源，欢迎自由使用与修改。

---
