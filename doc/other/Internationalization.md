# Internationalization

`org.lro.steven.aq.simpleminecraftlauncher.internationalization` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/internationalization/Internationalization.java#L40)

This class is used to process i18n

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### initialize

```java
private static void initialize()
```

第 53 行

内部方法：initialize。

---

### get

```java
public static String get(String key)
```

第 75 行

按当前语言获取国际化文本，找不到时回退到英语。

**参数**

- `key` — 参数

**返回**：返回值

---

### setLocale

```java
public void setLocale(Locale locale)
```

第 86 行

设置当前语言。

**参数**

- `locale` — 参数

---

### 构造器

```java
private Internationalization() {} private void addProperties(Locale locale, Properties properties)
```

第 92 行

创建 `Internationalization` 实例。

---

### load

```java
private Properties load(String propertiesFile) throws IOException
```

第 104 行

内部方法：load。

**参数**

- `propertiesFile` — 参数

**返回**：返回值

**异常**

- `IOException` — 异常

---

### parseLanguageTag

```java
private static String parseLanguageTag(String fileName)
```

第 114 行

解析 Language Tag。

**参数**

- `fileName` — 参数

**返回**：返回值

---

### buildPropertiesFromFile

```java
private static Properties buildPropertiesFromFile(File file)
```

第 124 行

构建 Properties From File。

**参数**

- `file` — 参数

**返回**：返回值

---
