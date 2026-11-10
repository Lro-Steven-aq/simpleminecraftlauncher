# DesktopFrost

`org.lro.steven.aq.simpleminecraftlauncher.window` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/window/DesktopFrost.java#L41)

让 Windows 对窗口背后的桌面做磨砂。仅影响桌面，不影响启动器自己的界面内容。

## 方法

### 构造器

```java
private DesktopFrost()
```

第 56 行

创建 `DesktopFrost` 实例。

---

### apply

```java
public static boolean apply(String windowTitle)
```

第 64 行

对指定标题的窗口开启桌面磨砂。

**参数**

- `windowTitle` — 窗口标题，用于定位原生窗口句柄

**返回**：磨砂是否已生效；不支持时调用方应改用无磨砂的透明方案

---

### isWindows

```java
private static boolean isWindows()
```

第 121 行

判断是否 Windows。

**返回**：返回值

---
