# Android 数据持久化实验仓库

📚 本仓库包含三个独立的 Android 数据持久化实验项目，展示了如何使用 SQLite 和 SharedPreferences 实现数据存储与操作。

---

## 🧪 实验列表

### 1. SQLite 数据持久化实验（CRUD完整版）
📌 **项目简介**  
演示如何通过 SQLite 数据库实现结构化数据的增删改查（CRUD）操作，包含动态数据加载与界面联动。

✅ **功能特性**
- 用户信息管理：添加、保存、删除、清空
- Spinner 动态加载数据库用户列表
- 选中用户自动展示详细信息
- 完整的 CRUD 操作实现

🛠 **技术要点**
- `SQLiteOpenHelper` 生命周期管理
- 数据库版本升级策略
- 数据绑定与界面联动

---

### 2. SQLite 数据持久化实验（基础版）
📌 **项目简介**  
适合快速上手 SQLite 数据库的基本操作，重点展示 CRUD 基础功能。此版本与完整版共享核心实现。

---

### 3. SharedPreferences 数据持久化实验
📌 **项目简介**  
演示轻量级键值对存储方案，适用于存储用户配置信息。

✅ **功能特性**
- 用户信息录入（文本/下拉框）
- 数据保存、读取、清空功能
- 应用退出时自动保存机制
- 数据缓存清理操作

🛠 **技术要点**
- `SharedPreferences` 读写操作
- 自动保存机制
- 简单数据类型存储实践

---

## 🚀 快速开始

### 环境要求
- Android Studio 最新版本
- Android SDK API 31+
- Java 11+

### 克隆与运行
```bash
git clone https://github.com/zzzy-code/Mobile-terminal_whut.git
```

1. **导入项目到 Android Studio**
   - 打开 Android Studio → `Open Existing Project`
   - 选择对应实验模块目录（每个实验独立运行）

2. **设备准备**
   - 连接 Android 设备（API 26+）或使用 AVD 模拟器

3. **构建并运行**
   - 选择模块 → 点击 `Run 'app'`

---

## ⚠️ 注意事项

### SQLite 实验
- 初次运行时，`user.db` 会自动创建
- 修改表结构时，请更新 `onUpgrade()` 方法
- 数据库版本号在 `BaseDBHelper` 中定义

### SharedPreferences 实验
- 数据存储路径：`data/data/<包名>/shared_prefs`
- 适合存储简单数据类型（如 String、Int、Bool 等）
- 使用 `apply()` 实现异步存储

---

## 🧩 项目结构
```
Mobile-terminal_whut/
├── SQLite-CRUD/               # 完整版SQLite实验
│   ├── app/
│   │   ├── src/main/java/     # Activity与DBHelper
│   │   └── res/               # 布局资源
├── SQLite-Basic/              # 基础版SQLite实验
└── SharedPreferences/         # 键值对存储实验
```

---

## 🤝 参与贡献

1. Fork 本仓库
2. 创建新分支 (`git checkout -b feature/your-feature`)
3. 提交修改 (`git commit -m 'Add some feature'`)
4. 推送分支 (`git push origin feature/your-feature`)
5. 发起 Pull Request

**代码规范**
- 遵循 [Android 官方编码规范](https://developer.android.com/kotlin/common-patterns)
- 数据库改动需更新相关文档
- 使用清晰的注释说明
