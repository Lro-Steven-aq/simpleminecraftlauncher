# PluginLoader

`org.lro.steven.aq.simpleminecraftlauncher.plugin.loader` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/loader/PluginLoader.java#L39)

插件加载接口。  

用来管理PluginClassLoader类实例

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### get

```java
static PluginLoader get()
```

第 45 行

获取默认实现.

**返回**：默认实现

---

### load

```java
Plugin load(String file, String clazz) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException
```

第 61 行

加载一个插件.

**参数**

- `file` — JAR 文件路径
- `clazz` — 类

**返回**：插件

**异常**

- `NoSuchMethodException` — 没有无参的构造函数
- `ClassNotFoundException` — 没有此类
- `InvocationTargetException` — 转换失败
- `InstantiationException` — 实例化失败
- `IllegalAccessException` — 无权限
- `MalformedURLException` — 异常URL

---

### unload

```java
void unload(String file) throws IOException
```

第 71 行

卸载一个插件

**参数**

- `file` — 插件JAR文件路径

**异常**

- `IOException` — io异常

---

### unloadAll

```java
void unloadAll() throws IOException
```

第 77 行

卸载所有插件

**异常**

- `IOException` — io异常

---
