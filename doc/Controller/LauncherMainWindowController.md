# LauncherMainWindowController

`org.lro.steven.aq.simpleminecraftlauncher.controller.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/controller/impl/LauncherMainWindowController.java#L71)

启动器主窗口的控制器。

## 嵌套类型

- [ManifestCallback](LauncherMainWindowController.ManifestCallback.md) — 版本清单请求的回调，负责选择并安装版本。

## 方法

### 构造器

```java
public LauncherMainWindowController(Metadata metadata)
```

第 140 行

创建主窗口控制器。

**参数**

- `metadata` — 参数

---

### initialize

```java
public void initialize(URL location, ResourceBundle resources)
```

第 151 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `location` — 参数
- `resources` — 参数

---

### initializeStyle

```java
private void initializeStyle()
```

第 161 行

初始化 Style。

---

### applyOpacity

```java
private void applyOpacity()
```

第 181 行

透明度只作用于背景图和磨砂层，文字与控件保持不透明。

---

### applyFrostState

```java
public void applyFrostState(boolean frosted)
```

第 192 行

根据系统磨砂是否生效切换方案：
系统支持时隐藏内置模糊层、由系统模糊桌面；不支持时显示内置模糊背景副本。

**参数**

- `frosted` — 系统磨砂是否生效

---

### configuredAlpha

```java
private double configuredAlpha()
```

第 211 行

用户配置的背景图透明度；没有配置时按不透明处理。

**返回**：0 到 1 之间的透明度

---

### initializeVersionSelector

```java
private void initializeVersionSelector()
```

第 222 行

初始化 Version Selector。

---

### bindWindowControls

```java
private void bindWindowControls()
```

第 238 行

绑定 Window Controls。

---

### bindDrag

```java
private void bindDrag()
```

第 260 行

绑定 Drag。

---

### onMenuPressed

```java
private void onMenuPressed(MouseEvent event, double[] offsetX, double[] offsetY)
```

第 273 行

处理 Menu Pressed。

**参数**

- `event` — 参数
- `offsetX` — 参数
- `offsetY` — 参数

---

### onMenuDragged

```java
private void onMenuDragged(MouseEvent event, double[] offsetX, double[] offsetY)
```

第 290 行

处理 Menu Dragged。

**参数**

- `event` — 参数
- `offsetX` — 参数
- `offsetY` — 参数

---

### minimize

```java
private void minimize()
```

第 302 行

内部方法：minimize。

---

### launch

```java
private void launch(MouseEvent event)
```

第 312 行

内部方法：launch。

**参数**

- `event` — 参数

---

### startLaunch

```java
private void startLaunch() throws IOException
```

第 325 行

启动 Launch。

**异常**

- `IOException` — 异常

---

### missingClientJarVersion

```java
private String missingClientJarVersion(String jsonPath)
```

第 353 行

内部方法：missing Client Jar Version。

**参数**

- `jsonPath` — 参数

**返回**：返回值

---

### requireSettings

```java
private UserSettings requireSettings()
```

第 377 行

内部方法：require Settings。

**返回**：返回值

---

### resolveLaunchJava

```java
private String resolveLaunchJava()
```

第 389 行

解析 Launch Java。

**返回**：返回值

---

### executeLaunch

```java
private void executeLaunch(String jsonPath, String javaExecutable) throws IOException
```

第 410 行

执行 Launch。

**参数**

- `jsonPath` — 参数
- `javaExecutable` — 参数

**异常**

- `IOException` — 异常

---

### isValidManifest

```java
private static boolean isValidManifest(GameMetadataManifest manifest)
```

第 431 行

判断是否 Valid Manifest。

**参数**

- `manifest` — 参数

**返回**：返回值

---

### startGame

```java
private void startGame(LaunchEvent event)
```

第 439 行

启动 Game。

**参数**

- `event` — 参数

---

### runGame

```java
private void runGame(LaunchEvent event)
```

第 450 行

执行 Game。

**参数**

- `event` — 参数

---

### fail

```java
private void fail(String message)
```

第 459 行

内部方法：fail。

**参数**

- `message` — 参数

---

### installVersionAndLaunch

```java
private void installVersionAndLaunch()
```

第 467 行

安装 Version And Launch。

---

### downloadNewVersions

```java
private void downloadNewVersions()
```

第 474 行

下载 New Versions。

---

### showAbout

```java
private void showAbout()
```

第 482 行

显示 About。

---
