# ManifestCallback

`org.lro.steven.aq.simpleminecraftlauncher.controller.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/controller/impl/LauncherMainWindowController.java#L487)

版本清单请求的回调，负责选择并安装版本。

上级类型：[LauncherMainWindowController](LauncherMainWindowController.md)

## 方法

### 构造器

```java
private ManifestCallback(boolean launchAfterInstall)
```

第 494 行

创建 `ManifestCallback` 实例。

**参数**

- `launchAfterInstall` — 参数

---

### onFailure

```java
public void onFailure(@NotNull Call call, @NotNull IOException e)
```

第 504 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `call` — 参数
- `e` — 参数

---

### onResponse

```java
public void onResponse(@NotNull Call call, @NotNull Response response)
```

第 514 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `call` — 参数
- `response` — 参数

---

### selectAndInstall

```java
} private void selectAndInstall(VersionManifest manifest, boolean launchAfterInstall)
```

第 530 行

内部方法：select And Install。

**参数**

- `manifest` — 参数
- `launchAfterInstall` — 参数

**返回**：返回值

---

### startInstall

```java
startInstall(version, launchAfterInstall)
```

第 543 行

启动 Install。

**参数**

- `version` — 参数
- `launchAfterInstall` — 参数

---

### startInstall

```java
} private void startInstall(String version, boolean launchAfterInstall)
```

第 550 行

启动 Install。

**参数**

- `version` — 参数
- `launchAfterInstall` — 参数

**返回**：返回值

---

### setDaemon

```java
installer.setDaemon(true)
```

第 559 行

设置 Daemon。

**参数**

- `true` — 参数

**返回**：返回值

---

### start

```java
installer.start()
```

第 564 行

内部方法：start。

**返回**：返回值

---

### installThenRelaunch

```java
} private void installThenRelaunch(String version, boolean launchAfterInstall)
```

第 571 行

安装 Then Relaunch。

**参数**

- `version` — 参数
- `launchAfterInstall` — 参数

**返回**：返回值

---

### install

```java
versionManager.install(version)
```

第 579 行

内部方法：install。

**参数**

- `version` — 参数

**返回**：返回值

---

### runLater

```java
Platform.runLater(() -> afterInstall(version, launchAfterInstall))
```

第 586 行

执行 Later。

**参数**

- `afterInstall(version` — 参数
- `launchAfterInstall)` — 参数

**返回**：返回值

---

### afterInstall

```java
} private void afterInstall(String version, boolean launchAfterInstall)
```

第 593 行

内部方法：after Install。

**参数**

- `version` — 参数
- `launchAfterInstall` — 参数

**返回**：返回值

---

### getItems

```java
versionSelector.getItems().setAll(versions)
```

第 601 行

获取 Items。

**返回**：返回值

---

### setValue

```java
versionSelector.setValue(version)
```

第 607 行

设置 Value。

**参数**

- `version` — 参数

**返回**：返回值

---

### setDisable

```java
launchButton.setDisable(false)
```

第 613 行

设置 Disable。

**参数**

- `false` — 参数

**返回**：返回值

---

### resolveJavaExecutable

```java
} private String resolveJavaExecutable(ApplicationConfiguration configuration, UserSettings settings)
```

第 623 行

解析 Java Executable。

**参数**

- `configuration` — 参数
- `settings` — 参数

**返回**：返回值

---

### javaExecutableFromEnvironment

```java
return javaExecutableFromEnvironment(configuration, settings)
```

第 638 行

Java Executable From Environment。

**参数**

- `configuration` — 参数
- `settings` — 参数

**返回**：返回值

---

### javaExecutableFromEnvironment

```java
} private String javaExecutableFromEnvironment(ApplicationConfiguration configuration, UserSettings settings)
```

第 645 行

Java Executable From Environment。

**参数**

- `configuration` — 参数
- `settings` — 参数

**返回**：返回值

---

### javaHome

```java
settings.javaHome(javaHome)
```

第 657 行

Java Home。

**参数**

- `javaHome` — 参数

**返回**：返回值

---

### showJavaPopup

```java
} private void showJavaPopup(ApplicationConfiguration configuration)
```

第 668 行

显示 Java Popup。

**参数**

- `configuration` — 参数

**返回**：返回值

---

### showJavaPrompt

```java
UIManager.showJavaPrompt( () -> { Environment.scanJavaRuntime(configuration); launch(null); }, () -> versionManager.downloadJavaRuntimeFromInternet(configuration), this::noChoice)
```

第 676 行

显示 Java Prompt。

**参数**

- `this::noChoice` — 参数

**返回**：返回值

---

### javaExecutableIn

```java
} private static String javaExecutableIn(String javaHome)
```

第 688 行

Java Executable In。

**参数**

- `javaHome` — 参数

**返回**：返回值

---

### of

```java
return Path.of(javaHome, "bin", executable).toString()
```

第 699 行

内部方法：of。

**参数**

- `javaHome` — 参数
- `"bin"` — 参数
- `executable` — 参数

**返回**：返回值

---

### selectedVersionJsonPath

```java
} private String selectedVersionJsonPath(UserSettings settings)
```

第 705 行

内部方法：selected Version Json Path。

**参数**

- `settings` — 参数

**返回**：返回值

---

### userSelectVersion

```java
} private String userSelectVersion(VersionManifest manifest)
```

第 727 行

内部方法：user Select Version。

**参数**

- `manifest` — 参数

**返回**：返回值

---

### chooseVersion

```java
return UIManager.chooseVersion( root.getScene() == null ? null : root.getScene().getWindow(), versions, initialVersion(manifest, versions), "Minecraft version", "Select the version to install", "Version:") .orElse(null)
```

第 745 行

内部方法：choose Version。

**参数**

- `root.getScene().getWindow()` — 参数
- `versions` — 参数
- `versions)` — 参数
- `version"` — 参数
- `install"` — 参数
- `"Version:"` — 参数

**返回**：返回值

---

### versionIds

```java
} private static List<String> versionIds(VersionManifest manifest)
```

第 758 行

内部方法：version Ids。

**参数**

- `manifest` — 参数

**返回**：返回值

---

### initialVersion

```java
} private static String initialVersion(VersionManifest manifest, List<String> versions)
```

第 777 行

内部方法：initial Version。

**参数**

- `manifest` — 参数
- `versions` — 参数

**返回**：返回值

---

### latest

```java
? manifest.latest().release() : versions.get(0)
```

第 785 行

内部方法：latest。

**返回**：返回值

---

### contains

```java
return versions.contains(initial) ? initial : versions.get(0)
```

第 792 行

内部方法：contains。

**参数**

- `initial` — 参数

**返回**：返回值

---

### noChoice

```java
} private void noChoice()
```

第 797 行

内部方法：no Choice。

**返回**：返回值

---

### emit

```java
SIGNAL_BUS.emit("log.error", LogEvent.of("No choice"))
```

第 806 行

内部方法：emit。

**参数**

- `"log.error"` — 参数
- `choice")` — 参数

**返回**：返回值

---
