# SignalBus

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/signalbus/SignalBus.java#L39)

这是信号总线接口，用来实现总线.
  

Default implementation: DefaultImplementationSignalBus

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### get

```java
static SignalBus get()
```

第 45 行

获取默认实现的类实例.

**返回**：默认实现的类实例

---

### connect

```java
void connect(String signal, EventConsumer consumer)
```

第 54 行

链接一个信号

**参数**

- `signal` — 信号
- `consumer` — 触发信号时，所执行的函数

---

### connect

```java
void connect(String signal, ChannelConsumer consumer)
```

第 60 行

内部方法：connect。

**参数**

- `signal` — 参数
- `consumer` — 参数

---

### emit

```java
void emit(String signal, Event payload)
```

第 67 行

发送信号

**参数**

- `signal` — 信号
- `payload` — 事件对象；可携带附属信息.

---

### emit

```java
void emit(String signal, Channel payload)
```

第 73 行

内部方法：emit。

**参数**

- `signal` — 参数
- `payload` — 参数

---
