# Application

`org.lro.steven.aq.simpleminecraftlauncher` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/Application.java#L58)

启动器主类：加载配置、构建界面并在 JavaFX 中显示主窗口。

## 方法

### start

```java
public void start(Stage primaryStage) throws Exception
```

第 69 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `primaryStage` — 参数

**异常**

- `Exception` — 异常

---

### loadRoot

```java
private Parent loadRoot(Metadata metadata) throws IOException
```

第 82 行

加载 Root。

**参数**

- `metadata` — 参数

**返回**：返回值

**异常**

- `IOException` — 异常

---

### createController

```java
private Object createController(Class<?> controllerClass, Metadata metadata)
```

第 98 行

创建 Controller。

**参数**

- `controllerClass` — 参数
- `metadata` — 参数

**返回**：返回值

---

### showStage

```java
private void showStage(Stage stage, Parent root)
```

第 114 行

显示 Stage。

**参数**

- `stage` — 参数
- `root` — 参数

---

### applyDesktopFrost

```java
private void applyDesktopFrost(String windowTitle, boolean retry)
```

第 134 行

开启桌面磨砂，并把结果交给控制器选择对应的透明方案。

**参数**

- `windowTitle` — 窗口标题
- `retry` — 是否为延迟重试；重试只在成功时才重新应用样式

---

### _main

```java
public static void _main(String[] args) throws IOException
```

第 147 行

JavaFX 入口：启动插件系统并启动应用。

**参数**

- `args` — 参数

**异常**

- `IOException` — 异常

---

### fastLaunch

```java
private static void fastLaunch(String[] args)
```

第 170 行

快速启动：不创建 JavaFX 窗口，直接用上次的版本 JSON 启动游戏。
没有可用的已安装版本时回退到正常 UI。

**参数**

- `args` — 启动参数

**异常**

- `IOException` — 读取配置/版本文件失败

---

### createFastLaunchEvent

```java
private static LaunchEvent createFastLaunchEvent(ApplicationConfiguration configuration) throws IOException
```

第 197 行

构建快速启动事件；版本 JSON、客户端 jar 或主类缺失时返回 null。

**参数**

- `configuration` — 启动器配置

**返回**：启动事件，或 null

**异常**

- `IOException` — 读取版本 JSON 失败

---

### resolveJavaExecutable

```java
private static String resolveJavaExecutable(UserSettings settings)
```

第 232 行

优先使用配置里的 Java，其次使用启动器自身的 Java。

**参数**

- `settings` — 用户设置

**返回**：java 可执行文件路径

---

### javaExecutableName

```java
private static String javaExecutableName()
```

第 252 行

Java Executable Name。

**返回**：返回值

---

### loadConfiguration

```java
public static ApplicationConfigurationLoader loadConfiguration()
```

第 260 行

加载默认配置文件。

**返回**：返回值

---

### saveConfiguration

```java
private static void saveConfiguration()
```

第 269 行

保存当前配置；由 JVM 退出钩子调用，覆盖正常退出和快速启动退出。

---
