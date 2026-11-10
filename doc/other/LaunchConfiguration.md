# LaunchConfiguration

`org.lro.steven.aq.simpleminecraftlauncher.launch` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/launch/LaunchConfiguration.java#L47)

根据版本 JSON 和用户配置生成启动参数表（供 Parser 替换 ${...}）。

## 方法

### 构造器

```java
private LaunchConfiguration()
```

第 52 行

创建 `LaunchConfiguration` 实例。

---

### create

```java
public static Configuration create(ApplicationConfiguration configuration, GameMetadataManifest manifest, Path gameDirectory, Features features)
```

第 63 行

根据版本 JSON 和用户配置构建启动参数表。

**参数**

- `configuration` — 参数
- `manifest` — 参数
- `gameDirectory` — 参数
- `features` — 参数

**返回**：返回值

---

### username

```java
private static String username(UserSettings settings)
```

第 83 行

内部方法：username。

**参数**

- `settings` — 参数

**返回**：返回值

---

### clientJar

```java
private static Path clientJar(GameMetadataManifest manifest, Path versionDirectory)
```

第 95 行

内部方法：client Jar。

**参数**

- `manifest` — 参数
- `versionDirectory` — 参数

**返回**：返回值

---

### addAuth

```java
private static void addAuth(Configuration result, String username)
```

第 105 行

添加 Auth。

**参数**

- `result` — 参数
- `username` — 参数

---

### addPaths

```java
private static void addPaths(Configuration result, GameMetadataManifest manifest, Path gameDirectory, Path versionDirectory)
```

第 122 行

添加 Paths。

**参数**

- `result` — 参数
- `manifest` — 参数
- `gameDirectory` — 参数
- `versionDirectory` — 参数

---

### classpath

```java
public static String classpath(Path gameDirectory, GameMetadataManifest manifest, Path clientJar, Features features)
```

第 146 行

构建客户端与依赖库的 classpath。

**参数**

- `gameDirectory` — 参数
- `manifest` — 参数
- `clientJar` — 参数
- `features` — 参数

**返回**：返回值

---

### addClientJar

```java
private static void addClientJar(List<String> entries, Path clientJar)
```

第 161 行

添加 Client Jar。

**参数**

- `entries` — 参数
- `clientJar` — 参数

---

### addLibraries

```java
private static void addLibraries(List<String> entries, Path gameDirectory, GameMetadataManifest manifest, Features features)
```

第 174 行

添加 Libraries。

**参数**

- `entries` — 参数
- `gameDirectory` — 参数
- `manifest` — 参数
- `features` — 参数

---

### addLibrary

```java
private static void addLibrary(List<String> entries, Library library, Path libraries, Features features)
```

第 194 行

添加 Library。

**参数**

- `entries` — 参数
- `library` — 参数
- `libraries` — 参数
- `features` — 参数

---

### addLibraryJar

```java
private static void addLibraryJar(List<String> entries, Path jar)
```

第 212 行

添加 Library Jar。

**参数**

- `entries` — 参数
- `jar` — 参数

---

### assetIndex

```java
private static String assetIndex(GameMetadataManifest manifest)
```

第 225 行

内部方法：asset Index。

**参数**

- `manifest` — 参数

**返回**：返回值

---

### artifactPath

```java
public static String artifactPath(Library library)
```

第 235 行

依赖库 jar 的相对路径：优先使用 downloads.artifact.path，否则从 Maven 坐标推导。

---

### artifactPathFromDownloads

```java
private static String artifactPathFromDownloads(Library library)
```

第 245 行

内部方法：artifact Path From Downloads。

**参数**

- `library` — 参数

**返回**：返回值

---

### artifactPathFromCoordinates

```java
private static String artifactPathFromCoordinates(String mavenArtifactId)
```

第 259 行

内部方法：artifact Path From Coordinates。

**参数**

- `mavenArtifactId` — 参数

**返回**：返回值

---
