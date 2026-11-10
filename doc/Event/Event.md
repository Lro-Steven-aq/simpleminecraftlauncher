# Event

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/event/Event.java#L36)

所有事件的接口.
  

在SignalBus里面传入的就是它或Channel。
  

Event仅单向发射，Event<b><i>不应该</i></b>用于双向。  

Event<b>应该</b>用完即<b>销毁</b>.

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### source

```java
String source()
```

第 42 行

获取发射的信号来源（字符串）

**返回**：信号Signal

---
