# Command

`org.lro.steven.aq.simpleminecraftlauncher.json.configuaration` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/json/configuaration/Command.java#L36)

一个可执行的命令，它是Parser的解析结果。
  

本质是封装了Process/ProcessBuilder

> **@author**: Lro.Steven.aq

> **@since**: 0.1

## 方法

### execute

```java
void execute() throws IOException, InterruptedException
```

第 43 行

创建一个子进程运行命令.

**异常**

- `IOException` — 可能是找不到文件？
- `InterruptedException` — 中断错误。

---

### getAsString

```java
String getAsString()
```

第 49 行

获取为其实际执行的命令行字符串.

**返回**：拼接为命令行的字符串

---

### getCommand

```java
List<String> getCommand()
```

第 55 行

返回命令参数列表的副本。

**返回**：命令参数列表

---

### add

```java
void add(String...commands)
```

第 61 行

追加一条命令参数。

**参数**

- `commands` — 参数

---
