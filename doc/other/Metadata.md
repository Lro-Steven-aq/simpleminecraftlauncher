# Metadata

`org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/metadata/launcher/Metadata.java#L36)

启动器运行期元数据。

## 方法

### 构造器

```java
private Metadata(OperatingSystem system, String systemVersion, Features features, Image preciousBackgroundImage, ApplicationConfiguration configurationMapper)
```

第 52 行

创建 `Metadata` 实例。

**参数**

- `system` — 参数
- `systemVersion` — 参数
- `features` — 参数
- `preciousBackgroundImage` — 参数
- `configurationMapper` — 参数

---

### from

```java
public static Metadata from(Features features, ApplicationConfiguration configurationMapper)
```

第 66 行

根据配置构建运行期元数据。

**参数**

- `features` — 参数
- `configurationMapper` — 参数

**返回**：返回值

---

### loadBackground

```java
private static Image loadBackground(WebURL url)
```

第 82 行

加载 Background。

**参数**

- `url` — 参数

**返回**：返回值

---

### system

```java
public OperatingSystem system()
```

第 102 行

返回当前操作系统。

**返回**：返回值

---

### systemVersion

```java
public String systemVersion()
```

第 110 行

返回系统版本。

**返回**：返回值

---

### features

```java
public Features features()
```

第 118 行

返回 features 开关。

**返回**：返回值

---

### preciousBackgroundImage

```java
public Image preciousBackgroundImage()
```

第 126 行

返回背景图片。

**返回**：返回值

---

### configurationMapper

```java
public ApplicationConfiguration configurationMapper()
```

第 134 行

返回配置对象。

**返回**：返回值

---

### save

```java
public void save()
```

第 141 行

把运行期配置（包括快速启动开关、Java 路径、游戏目录等）保存到默认配置文件。

---
