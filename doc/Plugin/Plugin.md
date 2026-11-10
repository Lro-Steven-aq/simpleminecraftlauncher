# Plugin

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/Plugin.java#L35)

开发者的插件接口.
  

所有插件都应该实现它

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### start

```java
void start(SignalBus signalBus)
```

第 48 行

在开始时调用；应该链接信号.
<pre>`public void start(SignalBus signalBus) {
    signalBus.connect("launcher.init", (e) -> {
        IO.println(e.source());
    `);
}
}</pre>

**参数**

- `signalBus` — 应该运用的信号总线。

---

### stop

```java
default void stop()
```

第 61 行

默认不会在结束时做任何事.
  

如果需要，请覆写它：
<pre>`public void stop() {
    // do some cleanup.
    // for examples: fstream.close();
`
}</pre>

---
