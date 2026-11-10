# PluginClassLoader

`org.lro.steven.aq.simpleminecraftlauncher.plugin.loader` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/loader/PluginClassLoader.java#L35)

插件类加载器：核心类向父类请求，私有类子类优先。

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### 构造器

```java
public PluginClassLoader(URL[] urls, ClassLoader parent)
```

第 49 行

创建插件类加载器。

**参数**

- `urls` — 参数
- `parent` — 参数

---

### loadClass

```java
public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
```

第 62 行 · `@Override`

加载类，若其前缀在`P`里面，则委托于父类；否然子类加载.

**参数**

- `name
The` — <a href="#binary-name">binary name</a> of the class

**返回**：找到的类

**异常**

- `ClassNotFoundException` — 类未找到

---
