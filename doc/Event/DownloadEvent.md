# DownloadEvent

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/event/impl/DownloadEvent.java#L37)

下载事件：既表示“下载文件”，也表示“读取文本”。
  

启动器发出 `download.file`/`download.read` 信号，下载器插件处理
后把结果写回事件。

## 方法

### 构造器

```java
private DownloadEvent(String url, Path target)
```

第 50 行

创建 `DownloadEvent` 实例。

**参数**

- `url` — 参数
- `target` — 参数

---

### of

```java
public static DownloadEvent of(String url, Path target)
```

第 61 行

创建文件下载事件。

**参数**

- `url` — 下载地址
- `target` — 目标文件

**返回**：下载事件

---

### of

```java
public static DownloadEvent of(String url)
```

第 70 行

创建文本读取事件。

**参数**

- `url` — 地址

**返回**：下载事件

---

### source

```java
public String source()
```

第 79 行 · `@Override`

实现/覆写接口或父类方法。

**返回**：返回值

---

### url

```java
public String url()
```

第 87 行

返回下载地址。

**返回**：下载地址

---

### target

```java
public Path target()
```

第 95 行

返回目标文件。

**返回**：目标文件；文本读取事件为 null

---

### content

```java
public String content()
```

第 103 行

返回读取到的文本。

**返回**：文本；尚未读取时为 null

---

### content

```java
public void content(String content)
```

第 111 行

设置读取到的文本。

**参数**

- `content` — 文本

---

### error

```java
public IOException error()
```

第 119 行

返回下载/读取错误。

**返回**：错误；成功时为 null

---

### fail

```java
public void fail(IOException error)
```

第 127 行

记录下载/读取失败。

**参数**

- `error` — 错误

---

### handled

```java
public boolean handled()
```

第 135 行

返回事件是否已被插件处理。

**返回**：是否已处理

---

### handled

```java
public void handled(boolean handled)
```

第 143 行

标记事件已被处理。

**参数**

- `handled` — 是否已处理

---

### throwIfFailed

```java
public void throwIfFailed() throws IOException
```

第 151 行

如果下载/读取失败，抛出对应异常。

**异常**

- `IOException` — 下载/读取失败

---
