# Parser

`org.lro.steven.aq.simpleminecraftlauncher.json.configuaration` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/json/configuaration/Parser.java#L37)

Parser类是用于解析参数json的接口.
  

所有适于解析的类都应该实现它。  

关于其parse返回值 Command

> **@author**: Lro.Steven.aq

> **@since**: 0.1

## 方法

### create

```java
static Parser create(String jsonRawContent, Configuration configuration, Features features)
```

第 46 行

创建版本 JSON 参数解析器。

**参数**

- `jsonRawContent` — 版本 JSON 文本
- `configuration` — 参数配置
- `features` — features 开关

**返回**：解析器

---

### setConfiguration

```java
void setConfiguration(Configuration configuration)
```

第 55 行

将配置设置为该参数.

**参数**

- `configuration` — 将要的设置

---

### getConfiguration

```java
Configuration getConfiguration()
```

第 62 行

用于获取其当前的配置.

**返回**：其现在所有的配置

---

### parse

```java
Command parse(Command command, Configuration configuration)
```

第 71 行

按照给定的配置来解析而忽略已经设置的配置来解析.
  

注意：不覆盖字段，只是不使用字段的configuration。

**参数**

- `configuration` — 配置

**返回**：配置结束的命令

---

### parse

```java
Command parse(Command command)
```

第 79 行

使用已有的配置解析，返回一个可执行的命令.
  

其内部委托于#parse(Command, Configuration)

**返回**：配置好的命令

---
