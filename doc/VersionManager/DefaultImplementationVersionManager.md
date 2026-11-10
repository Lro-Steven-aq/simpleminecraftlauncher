# DefaultImplementationVersionManager

`org.lro.steven.aq.simpleminecraftlauncher.version.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/version/impl/DefaultImplementationVersionManager.java#L74)

VersionManager 的默认实现：从 Mojang 版本清单下载并安装版本。

## 方法

### 构造器

```java
public DefaultImplementationVersionManager(String url, ApplicationConfiguration configuration)
```

第 90 行

创建版本管理器。

**参数**

- `url` — 参数
- `configuration` — 参数

---

### getVersions

```java
public List<String> getVersions()
```

第 100 行 · `@Override`

实现/覆写接口或父类方法。

**返回**：返回值

---

### listAvailableVersions

```java
public List<String> listAvailableVersions()
```

第 117 行 · `@Override`

实现/覆写接口或父类方法。

**返回**：返回值

---

### getVersionManifest

```java
public VersionManifest getVersionManifest()
```

第 139 行 · `@Override`

实现/覆写接口或父类方法。

**返回**：返回值

---

### install

```java
public void install(String version)
```

第 167 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `version` — 参数

---

### installFiles

```java
private void installFiles(Version selected, Path gameDirectory, Path versionDirectory, String version, Path jsonFile)
```

第 189 行

安装 Files。

**参数**

- `selected` — 参数
- `gameDirectory` — 参数
- `versionDirectory` — 参数
- `version` — 参数
- `jsonFile` — 参数

---

### logInstalled

```java
private static void logInstalled(String version)
```

第 204 行

内部方法：log Installed。

**参数**

- `version` — 参数

---

### canInstall

```java
private boolean canInstall(String version)
```

第 213 行

判断是否 Install。

**参数**

- `version` — 参数

**返回**：返回值

---

### findVersion

```java
private Version findVersion(String version)
```

第 229 行

查找 Version。

**参数**

- `version` — 参数

**返回**：返回值

---

### downloadVersionJson

```java
private void downloadVersionJson(Version selected, Path versionDirectory, Path jsonFile) throws IOException
```

第 253 行

下载 Version Json。

**参数**

- `selected` — 参数
- `versionDirectory` — 参数
- `jsonFile` — 参数

**异常**

- `IOException` — 异常

---

### installVersionFiles

```java
private void installVersionFiles(Path gameDirectory, Path versionDirectory, String version, Path jsonFile) throws IOException
```

第 267 行

安装 Version Files。

**参数**

- `gameDirectory` — 参数
- `versionDirectory` — 参数
- `version` — 参数
- `jsonFile` — 参数

**异常**

- `IOException` — 异常

---

### saveInstalledVersion

```java
private void saveInstalledVersion(Path jsonFile)
```

第 279 行

保存 Installed Version。

**参数**

- `jsonFile` — 参数

---

### downloadClientJar

```java
private void downloadClientJar(Path versionDirectory, String version, GameMetadataManifest metadata) throws IOException
```

第 291 行

下载 Client Jar。

**参数**

- `versionDirectory` — 参数
- `version` — 参数
- `metadata` — 参数

**异常**

- `IOException` — 异常

---

### downloadLibraries

```java
private void downloadLibraries(Path gameDirectory, GameMetadataManifest metadata)
```

第 305 行

下载 Libraries。

**参数**

- `gameDirectory` — 参数
- `metadata` — 参数

---

### downloadLibrary

```java
private boolean downloadLibrary(Library library, Path libraries, Features features)
```

第 329 行

下载 Library。

**参数**

- `library` — 参数
- `libraries` — 参数
- `features` — 参数

**返回**：返回值

---

### downloadLibraryJar

```java
private boolean downloadLibraryJar(String url, Path target, String path)
```

第 351 行

下载 Library Jar。

**参数**

- `url` — 参数
- `target` — 参数
- `path` — 参数

**返回**：返回值

---

### artifactUrl

```java
private String artifactUrl(Library library)
```

第 366 行

内部方法：artifact Url。

**参数**

- `library` — 参数

**返回**：返回值

---

### downloadAssetIndex

```java
private void downloadAssetIndex(Path gameDirectory, GameMetadataManifest metadata)
```

第 379 行

下载 Asset Index。

**参数**

- `gameDirectory` — 参数
- `metadata` — 参数

---

### downloadAssetObjects

```java
private void downloadAssetObjects(Path gameDirectory, GameMetadataManifest metadata)
```

第 399 行

下载 Asset Objects。

**参数**

- `gameDirectory` — 参数
- `metadata` — 参数

---

### downloadObjects

```java
private void downloadObjects(Path objectsDirectory, JsonObject objects)
```

第 425 行

下载 Objects。

**参数**

- `objectsDirectory` — 参数
- `objects` — 参数

---

### downloadAssetObject

```java
private void downloadAssetObject(Path objectsDirectory, String hash, AtomicInteger completed, AtomicInteger failed, int total)
```

第 457 行

下载 Asset Object。

**参数**

- `objectsDirectory` — 参数
- `hash` — 参数
- `completed` — 参数
- `failed` — 参数
- `total` — 参数

---

### enqueue

```java
public void enqueue(Callback callback)
```

第 479 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `callback` — 参数

---

### parseVersionManifest

```java
public VersionManifest parseVersionManifest(@NotNull Call call, @NotNull Response response)
```

第 491 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `call` — 参数
- `response` — 参数

**返回**：返回值

---

### downloadJavaRuntimeFromInternet

```java
public void downloadJavaRuntimeFromInternet(ApplicationConfiguration configuration)
```

第 510 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `configuration` — 参数

---

### downloadJavaRuntime

```java
private void downloadJavaRuntime(ApplicationConfiguration configuration)
```

第 520 行

下载 Java Runtime。

**参数**

- `configuration` — 参数

---

### resolveJavaRuntime

```java
private Path resolveJavaRuntime() throws Exception
```

第 540 行

解析 Java Runtime。

**返回**：返回值

**异常**

- `Exception` — 异常

---

### downloadAndExtractRuntime

```java
private Path downloadAndExtractRuntime(Path targetDirectory, Path runtimeDirectory) throws Exception
```

第 558 行

下载 And Extract Runtime。

**参数**

- `targetDirectory` — 参数
- `runtimeDirectory` — 参数

**返回**：返回值

**异常**

- `Exception` — 异常

---

### javaRuntimeUrl

```java
private static String javaRuntimeUrl()
```

第 574 行

Java Runtime Url。

**返回**：返回值

---

### download

```java
private void download(String url, Path target) throws IOException
```

第 596 行

内部方法：download。

**参数**

- `url` — 参数
- `target` — 参数

**异常**

- `IOException` — 异常

---

### untar

```java
private static void untar(Path archive, Path target) throws IOException, InterruptedException
```

第 615 行

内部方法：untar。

**参数**

- `archive` — 参数
- `target` — 参数

**异常**

- `IOException` — 异常
- `InterruptedException` — 异常

---

### unzip

```java
private static void unzip(Path archive, Path target) throws IOException
```

第 632 行

内部方法：unzip。

**参数**

- `archive` — 参数
- `target` — 参数

**异常**

- `IOException` — 异常

---

### writeZipEntry

```java
private static void writeZipEntry(ZipInputStream zip, ZipEntry entry, Path target) throws IOException
```

第 649 行

写入 Zip Entry。

**参数**

- `zip` — 参数
- `entry` — 参数
- `target` — 参数

**异常**

- `IOException` — 异常

---

### findJavaHome

```java
private static Path findJavaHome(Path root)
```

第 669 行

查找 Java Home。

**参数**

- `root` — 参数

**返回**：返回值

---

### isJavaExecutable

```java
private static boolean isJavaExecutable(Path path)
```

第 688 行

判断是否 Java Executable。

**参数**

- `path` — 参数

**返回**：返回值

---
