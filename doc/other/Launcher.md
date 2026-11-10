# Launcher

`org.lro.steven.aq.simpleminecraftlauncher` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/Launcher.java#L34)

启动器进程入口。

> **@author**: lro.steven.aq

> **@since**: 0.1

## 方法

### main

```java
static void main(String[] args)
```

第 39 行

启动器入口：调用 Application#_main(String[])，失败时打印并返回退出码。

**参数**

- `args` — 参数

---

### getExitCode

```java
private static int getExitCode(Exception e)
```

第 54 行

获取 Exit Code。

**参数**

- `e` — 参数

**返回**：返回值

---
