# DefaultImplementationPluginLoader

`org.lro.steven.aq.simpleminecraftlauncher.plugin.loader.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/loader/impl/DefaultImplementationPluginLoader.java#L45)

实现加载。

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### 构造器

```java
public DefaultImplementationPluginLoader()
```

第 51 行

创建插件加载器。

---

### load

```java
public Plugin load(String file, String clazz) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException
```

第 68 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `file` — 参数
- `clazz` — 参数

**返回**：返回值

**异常**

- `NoSuchMethodException` — 异常
- `ClassNotFoundException` — 异常
- `InvocationTargetException` — 异常
- `InstantiationException` — 异常
- `IllegalAccessException` — 异常
- `MalformedURLException` — 异常

---

### unload

```java
public void unload(String file) throws IOException
```

第 98 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `file` — 参数

**异常**

- `IOException` — 异常

---

### unloadAll

```java
public void unloadAll() throws IOException
```

第 110 行 · `@Override`

实现/覆写接口或父类方法。

**异常**

- `IOException` — 异常

---
