# Configuration

`org.lro.steven.aq.simpleminecraftlauncher.json.configuaration` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/json/configuaration/Configuration.java#L40)

对于json解析的配置.
  

其实就是一一对应.
比如：${classpath}  

其本质就是一个 Map，这是在内部维护的。

> **@author**: Lro.Steven.aq

> **@since**: 0.1

## 方法

### create

```java
static Configuration create()
```

第 45 行

创建参数配置表。

**返回**：配置表

---

### load

```java
default void load(String string) {} /** * 文件加载配置. * @param file 配置字符串 */ default void load(File file) {} /** * 获取Configuration内部的表. * <br> * 这是它的本质。<br> * @return 其内部维护的表。 */ Map<String, String> getMap()
```

第 53 行

字符串加载配置.

**参数**

- `string` — 配置字符串

---

### add

```java
default void add(String key, String value)
```

第 74 行

向内部的Map存储一对K, V.

**参数**

- `key` — 键
- `value` — 值

---

### get

```java
default String get(String key)
```

第 83 行

从内部的Map依据一个K，获取一个 V.

**参数**

- `key` — 键

**返回**：值

---

### get

```java
default String get(String key, String defaultValue)
```

第 92 行

从内部的Map依据一个K，获取一个 V.

**参数**

- `key` — 键

**返回**：值

---
