# Environment

`org.lro.steven.aq.simpleminecraftlauncher.environment` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/environment/Environment.java#L47)

运行环境相关的辅助功能（目前是查找本机 Java）。

## 方法

### 构造器

```java
private Environment()
```

第 57 行

创建 `Environment` 实例。

---

### scanJavaRuntime

```java
public static void scanJavaRuntime(ApplicationConfiguration configuration)
```

第 63 行

扫描本机常见的 JDK/JRE 安装位置，选出版本最高的一个写入用户配置。

---

### userSettings

```java
private static UserSettings userSettings(ApplicationConfiguration configuration)
```

第 83 行

内部方法：user Settings。

**参数**

- `configuration` — 参数

**返回**：返回值

---

### collectCandidates

```java
private static Set<Path> collectCandidates()
```

第 91 行

内部方法：collect Candidates。

**返回**：返回值

---

### addCommonLocations

```java
private static void addCommonLocations(Set<Path> candidates)
```

第 103 行

添加 Common Locations。

**参数**

- `candidates` — 参数

---

### addPlatformLocations

```java
private static void addPlatformLocations(Set<Path> candidates)
```

第 117 行

添加 Platform Locations。

**参数**

- `candidates` — 参数

---

### addWindowsJavaHomes

```java
private static void addWindowsJavaHomes(Set<Path> candidates)
```

第 130 行

添加 Windows Java Homes。

**参数**

- `candidates` — 参数

---

### findBestJavaHome

```java
private static Path findBestJavaHome(Set<Path> candidates)
```

第 145 行

查找 Best Java Home。

**参数**

- `candidates` — 参数

**返回**：返回值

---

### saveJavaHome

```java
private static void saveJavaHome(ApplicationConfiguration configuration, UserSettings settings, Path javaHome)
```

第 167 行

保存 Java Home。

**参数**

- `configuration` — 参数
- `settings` — 参数
- `javaHome` — 参数

---

### addJavaHome

```java
private static void addJavaHome(Set<Path> candidates, String javaHome)
```

第 178 行

添加 Java Home。

**参数**

- `candidates` — 参数
- `javaHome` — 参数

---

### addChildren

```java
private static void addChildren(Set<Path> candidates, Path directory, boolean filterNames)
```

第 198 行

添加 Children。

**参数**

- `candidates` — 参数
- `directory` — 参数
- `filterNames` — 参数

---

### addMatchingJavaHomes

```java
private static void addMatchingJavaHomes(Set<Path> candidates, Stream<Path> stream, boolean filterNames)
```

第 215 行

添加 Matching Java Homes。

**参数**

- `candidates` — 参数
- `stream` — 参数
- `filterNames` — 参数

---

### addIfJavaHome

```java
private static void addIfJavaHome(Set<Path> candidates, Path path)
```

第 227 行

添加 If Java Home。

**参数**

- `candidates` — 参数
- `path` — 参数

---

### isJavaLike

```java
private static boolean isJavaLike(Path path)
```

第 238 行

判断是否 Java Like。

**参数**

- `path` — 参数

**返回**：返回值

---

### addMacJavaHomes

```java
private static void addMacJavaHomes(Set<Path> candidates, Path directory)
```

第 248 行

添加 Mac Java Homes。

**参数**

- `candidates` — 参数
- `directory` — 参数

---

### isJavaHome

```java
private static boolean isJavaHome(Path home)
```

第 267 行

判断是否 Java Home。

**参数**

- `home` — 参数

**返回**：返回值

---

### detectJavaVersion

```java
private static int detectJavaVersion(Path home)
```

第 278 行

从 release 文件读取 Java 主版本；读不到时退回目录名。

---

### readReleaseVersion

```java
private static int readReleaseVersion(Path home)
```

第 288 行

读取 Release Version。

**参数**

- `home` — 参数

**返回**：返回值

---

### directoryVersion

```java
private static int directoryVersion(Path home)
```

第 306 行

内部方法：directory Version。

**参数**

- `home` — 参数

**返回**：返回值

---

### parseMajor

```java
private static int parseMajor(String version)
```

第 323 行

解析 Major。

**参数**

- `version` — 参数

**返回**：返回值

---
