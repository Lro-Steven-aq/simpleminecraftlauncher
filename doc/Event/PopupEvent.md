# PopupEvent

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/event/impl/PopupEvent.java#L29)

弹窗事件，携带标题、文本和按钮回调。

## 方法

### of

```java
public static Event of(String title, String head, String msg, Runnable callableOnYes, Runnable callableOnNo, Runnable callableOnClose)
```

第 40 行

创建一条弹窗事件。

**参数**

- `title` — 参数
- `head` — 参数
- `msg` — 参数
- `callableOnYes` — 参数
- `callableOnNo` — 参数
- `callableOnClose` — 参数

**返回**：返回值

---

### 构造器

```java
private PopupEvent(String title, String head, String msg, Runnable callableOnYes, Runnable callableOnNo, Runnable callableOnClose)
```

第 65 行

创建 `PopupEvent` 实例。

**参数**

- `title` — 参数
- `head` — 参数
- `msg` — 参数
- `callableOnYes` — 参数
- `callableOnNo` — 参数
- `callableOnClose` — 参数

---

### source

```java
public String source()
```

第 84 行 · `@Override`

实现/覆写接口或父类方法。

**返回**：返回值

---

### getTitle

```java
public String getTitle()
```

第 92 行

获取 Title。

**返回**：返回值

---

### getMessage

```java
public String getMessage()
```

第 100 行

返回弹窗正文。

**返回**：返回值

---

### getHead

```java
public String getHead()
```

第 108 行

返回弹窗头部文本。

**返回**：返回值

---

### getCallableOnYes

```java
public Runnable getCallableOnYes()
```

第 116 行

返回“是”按钮的回调。

**返回**：返回值

---

### getCallableOnNo

```java
public Runnable getCallableOnNo()
```

第 124 行

返回“否”按钮的回调。

**返回**：返回值

---

### getCallableOnClose

```java
public Runnable getCallableOnClose()
```

第 132 行

返回关闭按钮的回调。

**返回**：返回值

---
