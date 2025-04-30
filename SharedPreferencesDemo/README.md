# Android SharedPreferences 数据持久化实验

## 📌 项目简介

此项目展示了如何在 Android 应用中使用 `SharedPreferences` 实现数据的持久化存储与读取。`SharedPreferences` 是 Android 提供的一种轻量级数据存储方式，适用于存储键值对形式的数据，例如用户设置或配置信息。

## 🧪 实验目标

- 掌握 Android 常用控件及布局设计。
- 学会使用 `SharedPreferences` 实现本地数据保存与读取。
- 实现数据自动保存、加载与清空功能。

## ✅ 功能特性

- **信息录入**：用户可输入姓名、密码、电话、邮箱，并通过 `Spinner` 选择性别。
- **功能按钮**：
  - **保存**：将输入的数据存储至 `SharedPreferences`。
  - **读取**：加载保存的数据并显示至界面。
  - **清空**：清除界面上的所有输入内容。
- **自动保存机制**：在退出应用或关机时自动保存用户数据。

## 🧩 项目结构

```
├── app/
│   ├── src/main/java/    # Activity 主逻辑代码
│   └── res/              # 布局 XML 和资源文件
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
- `SharedPreferences` 存储的数据是基于键值对的，数据较小，不适合存储复杂结构的数据。
- 请确保在项目中正确配置权限及清除缓存操作。

## 🤝 贡献指南

欢迎贡献代码或提出 Issue 改进项目！提交 Pull Request 时请：

- 保持代码风格一致；
- 提供简洁明了的变更说明。

## 📄 许可证

本项目基于 [MIT License](https://mit-license.org/) 开源，欢迎自由使用与修改。
