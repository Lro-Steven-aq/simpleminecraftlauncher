# UIManager

`org.lro.steven.aq.simpleminecraftlauncher.utils` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/utils/UIManager.java#L42)

启动器的 UI 弹窗统一入口。
  

Controller 只负责业务状态，具体的 Alert/ChoiceDialog 和 popup 事件都放在这里。

## 方法

### 构造器

```java
private UIManager()
```

第 47 行

创建 `UIManager` 实例。

---

### showAbout

```java
public static void showAbout(Window owner)
```

第 54 行

显示“关于”对话框。

**参数**

- `owner` — 父窗口，可为 null

---

### chooseVersion

```java
public static Optional<String> chooseVersion(Window owner, List<String> versions, String initial, String title, String header, String content)
```

第 82 行

显示版本选择对话框。

**参数**

- `owner` — 父窗口，可为 null
- `versions` — 可选版本
- `initial` — 初始版本
- `title` — 标题
- `header` — 头部文本
- `content` — 内容文本

**返回**：用户选择的版本；取消时为空

---

### showJavaPrompt

```java
public static void showJavaPrompt(Runnable scan, Runnable download, Runnable close)
```

第 100 行

显示 Java 缺失时的确认弹窗。

**参数**

- `scan` — 扫描本机 Java
- `download` — 从网络下载 Java
- `close` — 取消/关闭

---
