# PluginManager

`org.lro.steven.aq.simpleminecraftlauncher.plugin` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/PluginManager.java#L51)

插件管理类.
  

组合起`PluginLoader`和`SignalBus`

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### 构造器

```java
private PluginManager()
```

第 65 行

唯一示例化。

---

### get

```java
public static PluginManager get()
```

第 75 行

返回插件管理器单例。

**返回**：返回值

---

### start

```java
public void start(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
```

第 86 行

加载一个插件

**参数**

- `file` — JAR文件

---

### getManifest

```java
private Manifest getManifest(File file) throws IOException
```

第 107 行

获取 Manifest。

**参数**

- `file` — 参数

**返回**：返回值

**异常**

- `IOException` — 异常

---

### getPluginClass

```java
private String getPluginClass(File file) throws IOException
```

第 125 行

获取 Plugin Class。

**参数**

- `file` — 参数

**返回**：返回值

**异常**

- `IOException` — 异常

---

### startAll

```java
public void startAll() throws IOException
```

第 144 行

调用所有插件的`start(SignalBus signalBus)`方法.
  

而且最多执行一次。

**异常**

- `IOException` — io错误
- `ClassNotFoundException` — 查无此类
- `InvocationTargetException` — （无说明）
- `NoSuchMethodException` — 没有无参的构造函数
- `InstantiationException` — （无说明）
- `IllegalAccessException` — 无权限

---

### getSignalBus

```java
public SignalBus getSignalBus()
```

第 186 行

返回全局信号总线。

**返回**：返回值

---

### stopAll

```java
public void stopAll()
```

第 195 行

正序开始逆序卸载。

---

### stop

```java
public void stop(String filePath)
```

第 216 行

停止一个插件.

**参数**

- `filePath` — 需要停止的插件路径。

---
