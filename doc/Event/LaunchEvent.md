# LaunchEvent

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/event/impl/LaunchEvent.java#L38)

启动事件：只承载启动所需的原始参数，由启动插件负责构建命令并执行。
  

启动器发出 `launch.fast`/`launch.game` 后不再读取事件内容；
启动成功与否、panic 和弹窗都由启动插件负责。

## 方法

### 构造器

```java
private LaunchEvent(String version, String versionJson, Configuration launchConfiguration, Features features, String javaExecutable)
```

第 54 行

创建 `LaunchEvent` 实例。

**参数**

- `version` — 参数
- `versionJson` — 参数
- `launchConfiguration` — 参数
- `features` — 参数
- `javaExecutable` — 参数

---

### of

```java
public static LaunchEvent of(String version, String versionJson, Configuration launchConfiguration, Features features, String javaExecutable)
```

第 72 行

创建启动事件。

**参数**

- `version` — 要启动的版本 id
- `versionJson` — 版本 JSON 原文
- `launchConfiguration` — 启动参数配置（classpath、目录、认证等）
- `features` — features 开关
- `javaExecutable` — Java 可执行文件

**返回**：启动事件

---

### source

```java
public String source()
```

第 82 行 · `@Override`

实现/覆写接口或父类方法。

**返回**：返回值

---

### version

```java
public String version()
```

第 90 行

返回要启动的版本 id。

**返回**：版本 id

---

### versionJson

```java
public String versionJson()
```

第 98 行

返回版本 JSON 原文。

**返回**：版本 JSON

---

### launchConfiguration

```java
public Configuration launchConfiguration()
```

第 106 行

返回启动参数配置。

**返回**：启动参数配置

---

### features

```java
public Features features()
```

第 114 行

返回 features 开关。

**返回**：features

---

### javaExecutable

```java
public String javaExecutable()
```

第 122 行

返回 Java 可执行文件。

**返回**：Java 可执行文件路径

---
