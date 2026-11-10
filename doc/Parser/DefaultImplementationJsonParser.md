# DefaultImplementationJsonParser

`org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/json/configuaration/impl/DefaultImplementationJsonParser.java#L51)

实际解析类的默认实现.

<p>解析顺序：jvm 参数 → mainClass → game 参数；所有 ${...} 占位符都从
Configuration 里取值。</p>

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### 构造器

```java
public DefaultImplementationJsonParser(String jsonRawContent, Configuration configuration, Features features)
```

第 65 行

创建版本 JSON 解析器。

**参数**

- `jsonRawContent` — 参数
- `configuration` — 参数
- `features` — 参数

---

### setConfiguration

```java
public void setConfiguration(Configuration configuration)
```

第 76 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `configuration` — 参数

---

### getConfiguration

```java
public Configuration getConfiguration()
```

第 85 行 · `@Override`

实现/覆写接口或父类方法。

**返回**：返回值

---

### parse

```java
public Command parse(Command command, Configuration configuration)
```

第 96 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `command` — 参数
- `configuration` — 参数

**返回**：返回值

---

### effectiveConfiguration

```java
private Configuration effectiveConfiguration(Configuration configuration)
```

第 113 行

内部方法：effective Configuration。

**参数**

- `configuration` — 参数

**返回**：返回值

---

### addJvmArguments

```java
private void addJvmArguments(Command command, Arguments arguments, Configuration configuration)
```

第 126 行

添加 Jvm Arguments。

**参数**

- `command` — 参数
- `arguments` — 参数
- `configuration` — 参数

---

### addGameArguments

```java
private void addGameArguments(Command command, Arguments arguments, Configuration configuration)
```

第 140 行

添加 Game Arguments。

**参数**

- `command` — 参数
- `arguments` — 参数
- `configuration` — 参数

---

### addMainClass

```java
private void addMainClass(Command command, GameMetadataManifest manifest)
```

第 151 行

添加 Main Class。

**参数**

- `command` — 参数
- `manifest` — 参数

---

### addArgumentValues

```java
private void addArgumentValues(Command command, List<Argument> arguments, Configuration configuration)
```

第 163 行

添加 Argument Values。

**参数**

- `command` — 参数
- `arguments` — 参数
- `configuration` — 参数

---

### isApplicable

```java
private boolean isApplicable(Argument argument)
```

第 179 行

判断是否 Applicable。

**参数**

- `argument` — 参数

**返回**：返回值

---

### addValues

```java
private void addValues(Command command, List<String> values, Configuration configuration)
```

第 192 行

添加 Values。

**参数**

- `command` — 参数
- `values` — 参数
- `configuration` — 参数

---

### parseReference

```java
private String parseReference(String string, Configuration configuration)
```

第 201 行

把字符串里的 ${key} 替换成配置里的值。

---

### resolveKey

```java
private String resolveKey(String key, String fallback, Configuration configuration)
```

第 222 行

解析 Key。

**参数**

- `key` — 参数
- `fallback` — 参数
- `configuration` — 参数

**返回**：返回值

---

### parse

```java
public Command parse(Command command)
```

第 237 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `command` — 参数

**返回**：返回值

---
