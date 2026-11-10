# UserSettings

`org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/metadata/launcher/conf/settings/UserSettings.java#L30)

用户设置。

## 方法

### 构造器

```java
public UserSettings() {} /** * 返回背景图片 URL。 * @return 背景图片 URL；未设置时为 null */ public WebURL preciousBackgroundImageUrl()
```

第 56 行

创建一个空的用户设置。

---

### javaHome

```java
public String javaHome()
```

第 70 行

返回 JAVA_HOME。

**返回**：JAVA_HOME；未设置时为 null

---

### javaHome

```java
public void javaHome(String javaHome)
```

第 78 行

设置 JAVA_HOME。

**参数**

- `javaHome` — JAVA_HOME

---

### executableJavaCommand

```java
public String executableJavaCommand()
```

第 86 行

返回 Java 可执行文件路径。

**返回**：Java 可执行文件路径；未设置时为 null

---

### fastLaunch

```java
public boolean fastLaunch()
```

第 94 行

返回是否启用快速启动。

**返回**：是否快速启动

---

### fastLaunch

```java
public void fastLaunch(boolean enableFastLaunch)
```

第 102 行

返回是否启用快速启动。

**返回**：是否快速启动

---

### alpha

```java
public Double alpha()
```

第 110 行

返回窗口透明度。

**返回**：透明度（0 到 1）；未设置时为 null

---

### lastVersionJsonFile

```java
public String lastVersionJsonFile()
```

第 118 行

返回上次使用的版本 JSON 文件路径。

**返回**：版本 JSON 路径；未设置时为 null

---

### lastVersionJsonFile

```java
public void lastVersionJsonFile(String lastVersionJsonFile)
```

第 126 行

设置上次使用的版本 JSON 文件路径。

**参数**

- `lastVersionJsonFile` — 版本 JSON 路径

---

### gameDirectory

```java
public String gameDirectory()
```

第 134 行

返回游戏目录。

**返回**：游戏目录；未设置时为 null

---

### gameDirectory

```java
public void gameDirectory(String gameDirectory)
```

第 142 行

设置游戏目录。

**参数**

- `gameDirectory` — 游戏目录

---

### username

```java
public String username()
```

第 150 行

返回离线用户名。

**返回**：离线用户名；未设置时为 null

---

### username

```java
public void username(String username)
```

第 158 行

设置离线用户名。

**参数**

- `username` — 离线用户名

---
