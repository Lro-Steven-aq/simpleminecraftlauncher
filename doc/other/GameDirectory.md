# GameDirectory

`org.lro.steven.aq.simpleminecraftlauncher.launch` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/launch/GameDirectory.java#L36)

游戏目录/版本目录/依赖/资源/ natives 目录的解析。

## 方法

### 构造器

```java
private GameDirectory()
```

第 41 行

创建 `GameDirectory` 实例。

---

### resolve

```java
public static Path resolve(ApplicationConfiguration configuration)
```

第 47 行

用户配置里的 game_directory，缺省为 ~/.minecraft。

---

### versionDirectory

```java
public static Path versionDirectory(Path gameDirectory, String versionId)
```

第 67 行

返回指定版本的目录。

**参数**

- `gameDirectory` — 参数
- `versionId` — 参数

**返回**：返回值

---

### librariesDirectory

```java
public static Path librariesDirectory(Path gameDirectory)
```

第 76 行

返回依赖库目录。

**参数**

- `gameDirectory` — 参数

**返回**：返回值

---

### assetsDirectory

```java
public static Path assetsDirectory(Path gameDirectory)
```

第 85 行

返回资源目录。

**参数**

- `gameDirectory` — 参数

**返回**：返回值

---

### nativesDirectory

```java
public static Path nativesDirectory(Path versionDirectory)
```

第 93 行

找到版本目录下的 natives 目录。
优先使用 "natives-&lt;os&gt;-&lt;arch&gt;"，其次 "natives-&lt;os&gt;"、"natives"。

---

### osName

```java
private static String osName()
```

第 104 行

内部方法：os Name。

**返回**：返回值

---

### firstExisting

```java
private static Path firstExisting(Path versionDirectory, String os, String arch)
```

第 120 行

内部方法：first Existing。

**参数**

- `versionDirectory` — 参数
- `os` — 参数
- `arch` — 参数

**返回**：返回值

---

### normalizeArch

```java
public static String normalizeArch(String arch)
```

第 135 行

把 JVM 架构名规范化为 natives 目录使用的名字。

**参数**

- `arch` — 参数

**返回**：返回值

---
