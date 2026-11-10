# ApplicationConfigurationLoader

`org.lro.steven.aq.simpleminecraftlauncher` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/ApplicationConfigurationLoader.java#L40)

负责读取、校验并保存启动器配置。

## 方法

### registerTypeAdapter

```java
.registerTypeAdapter(WebURL.class, new WebURLDeserializer()) .create()
```

第 52 行

内部方法：register Type Adapter。

**参数**

- `WebURL.class` — 参数
- `WebURLDeserializer()` — 参数

**返回**：返回值

---

### 构造器

```java
public ApplicationConfigurationLoader()
```

第 59 行

创建一个配置加载器。

---

### load

```java
public void load(String content)
```

第 67 行

从 JSON 字符串或文件加载并校验配置。

**参数**

- `content` — 参数

---

### save

```java
public void save()
```

第 80 行

把当前配置写回默认配置文件。

---

### load

```java
public void load(File file)
```

第 88 行

从 JSON 字符串或文件加载并校验配置。

**参数**

- `file` — 参数

---

### readContent

```java
private String readContent(File file)
```

第 98 行

读取 Content。

**参数**

- `file` — 参数

**返回**：返回值

---

### initializeConfigurationFileIfAbsent

```java
public void initializeConfigurationFileIfAbsent(File file)
```

第 112 行

如果配置文件不存在，创建一个最小默认配置。

**参数**

- `file` — 参数

---

### check

```java
private void check(ApplicationConfiguration configurations)
```

第 133 行

内部方法：check。

**参数**

- `configurations` — 参数

---

### getMap

```java
public Map<String, String> getMap()
```

第 149 行

把当前配置转换成键值表。

**返回**：返回值

---

### getConfigurations

```java
public ApplicationConfiguration getConfigurations()
```

第 157 行

返回当前配置对象。

**返回**：返回值

---
