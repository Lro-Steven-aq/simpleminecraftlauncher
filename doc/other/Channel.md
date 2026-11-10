# Channel

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/event/Channel.java#L9)

用于 [ 主程序 -> 插件 -> 主程序 ] 之间的通信。
  

Event仅单向发射，Event<b><i>不应该</i></b>用于双向。  

Event<b>应该</b>用完即<b>销毁</b>.

## 方法

### source

```java
String source()
```

第 14 行

获取发射的信号来源（字符串）

**返回**：信号Signal

---

### send

```java
void send(Sendable<?> content)
```

第 20 行

发送一个内容.

**参数**

- `content` — 发送的内容

---

### receive

```java
Sendable<?> receive()
```

第 26 行

接收一个内容.

**返回**：接收的内容

---
