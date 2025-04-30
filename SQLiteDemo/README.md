# Android SQLite 数据持久化实验

## 📌 项目简介

此项目展示了如何在 Android 应用中使用 `SQLite` 数据库进行数据持久化存储。通过实现数据的增、删、改、查（CRUD）操作，学习如何管理和操作结构化的数据。

## 🧪 实验目标

- 掌握 `SQLiteOpenHelper` 的使用与数据库生命周期管理。
- 实现对用户数据的新增、查询、更新与删除（CRUD）功能。
- 熟悉动态数据加载与界面数据联动。

## ✅ 功能特性

- **用户信息管理功能**：
  - **添加**：新增用户信息至数据库。
  - **保存**：更新当前用户记录。
  - **删除**：删除选中用户记录。
  - **清空**：清空界面输入内容。
  
- **动态加载功能**：
  - 启动应用时，从数据库中加载用户名列表至 `Spinner`。
  - 选中某用户名后，自动显示对应用户信息。

- **数据持久化**：所有数据操作均通过 SQLite 数据库完成。

## 🧩 项目结构

```
└── app/
    ├── src/main/java/    # Activity + 数据库操作代码
    └── res/              # 布局 XML 和资源文件
```

## 🚀 安装与运行

1. **克隆项目代码**：

   ```bash
   git clone https://github.com/zzzy-code/Mobile-terminal_whut.git
   ```

2. **导入到 Android Studio**：
   - 打开 Android Studio，点击 `Open an Existing Project`。
   - 选择克隆下来的项目文件夹。

3. **运行项目**：
   - 连接真机或启动 Android 模拟器。
   - 点击 `Run 'app'` 进行构建与运行。

## ⚠️ 注意事项

- 请确保已在 Android Studio 中配置最新版 Android SDK。
- SQLite 实验首次启动会自动创建数据库和数据表。
- 如需更改数据库结构或版本，请在 `BaseDBHelper` 的 `onUpgrade()` 方法中更新表定义及版本号。

## 🤝 贡献指南

欢迎贡献代码或提出 Issue 改进项目！提交 Pull Request 时请：

- 保持代码风格一致；
- 提供简洁明了的变更说明；
- 如涉及数据库结构调整，请更新相关文档注释。

## 📄 许可证

本项目基于 [MIT License](https://mit-license.org/) 开源，欢迎自由使用与修改。
