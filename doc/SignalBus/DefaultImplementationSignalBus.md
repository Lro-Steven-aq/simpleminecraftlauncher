# DefaultImplementationSignalBus

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.impl` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/signalbus/impl/DefaultImplementationSignalBus.java#L45)

这是信号总线的默认实现类.
  

此后如果需要其他实现请更改`SignalBus`的`static SignalBus get() {...`} 的指向.

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### 构造器

```java
private DefaultImplementationSignalBus()
```

第 52 行

拒绝外部实例化

---

### get

```java
public static SignalBus get()
```

第 65 行

返回信号总线单例。

**返回**：返回值

---

### connect

```java
public void connect(String signal, EventConsumer consumer)
```

第 79 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `signal` — 参数
- `consumer` — 参数

---

### connect

```java
public void connect(String signal, ChannelConsumer consumer)
```

第 90 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `signal` — 参数
- `consumer` — 参数

---

### emit

```java
public void emit(String signal, Event payload)
```

第 101 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `signal` — 参数
- `payload` — 参数

---

### emit

```java
public void emit(String signal, Channel payload)
```

第 120 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `signal` — 参数
- `payload` — 参数

---
