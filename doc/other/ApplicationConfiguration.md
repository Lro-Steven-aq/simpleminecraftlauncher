# ApplicationConfiguration

`org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/metadata/launcher/conf/ApplicationConfiguration.java#L40)

启动器配置（版本号与用户设置）。

## 方法

### 构造器

```java
public ApplicationConfiguration() {} public void save(String configurationFilePath)
```

第 50 行

创建一个空的启动器配置。

---

### moveIntoPlace

```java
private static void moveIntoPlace(Path temporary, Path target) throws IOException
```

第 74 行

内部方法：move Into Place。

**参数**

- `temporary` — 参数
- `target` — 参数

**异常**

- `IOException` — 异常

---

### userSettings

```java
public UserSettings userSettings()
```

第 87 行

返回用户设置。

**返回**：返回值

---

### configurationVersion

```java
public String configurationVersion()
```

第 94 行

返回配置版本。

**返回**：返回值

---
