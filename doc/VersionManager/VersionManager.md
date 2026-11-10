# VersionManager

`org.lro.steven.aq.simpleminecraftlauncher.version` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/version/VersionManager.java#L37)

版本清单与版本安装/Java 运行时下载的接口。

## 方法

### create

```java
static VersionManager create(ApplicationConfiguration configuration)
```

第 44 行

创建版本管理器。

**参数**

- `configuration` — 启动器配置

**返回**：版本管理器

---

### getVersions

```java
List<String> getVersions()
```

第 53 行

Mojang 版本清单里的全部版本 id（远程）。

---

### listAvailableVersions

```java
List<String> listAvailableVersions()
```

第 58 行

本地已经安装（存在版本 JSON）的版本 id。

---

### getVersionManifest

```java
VersionManifest getVersionManifest()
```

第 63 行

获取版本清单；失败返回 null。

---

### install

```java
void install(String version)
```

第 68 行

下载指定版本的 JSON、客户端 jar、依赖库和资源索引，并写入 last_version_file。

---

### enqueue

```java
void enqueue(Callback callback)
```

第 73 行

异步请求版本清单。

---

### parseVersionManifest

```java
VersionManifest parseVersionManifest(@NotNull Call call, @NotNull Response response)
```

第 78 行

把响应体解析成版本清单。

---

### downloadJavaRuntimeFromInternet

```java
void downloadJavaRuntimeFromInternet(ApplicationConfiguration configuration)
```

第 83 行

从网络下载一个 Java 运行时（Adoptium），解压后写入 JAVA_HOME。

---
