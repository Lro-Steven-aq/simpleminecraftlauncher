# Features

`org.lro.steven.aq.simpleminecraftlauncher.json.configuaration` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/json/configuaration/Features.java#L29)

版本 JSON 中 features 规则使用的开关集合。

## 方法

### create

```java
public static Features create(Map<String, Boolean> config)
```

第 38 行

创建 features 开关集合。

**参数**

- `config` — 参数

**返回**：返回值

---

### 构造器

```java
private Features(Map<String, Boolean> config)
```

第 47 行

创建 `Features` 实例。

**参数**

- `config` — 参数

---

### getOrDefault

```java
public Boolean getOrDefault(String key, Boolean defaultValue)
```

第 57 行

获取指定 feature 的值，缺省时返回默认值。

**参数**

- `key` — 参数
- `defaultValue` — 参数

**返回**：返回值

---
