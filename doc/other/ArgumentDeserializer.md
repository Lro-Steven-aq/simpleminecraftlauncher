# ArgumentDeserializer

`org.lro.steven.aq.simpleminecraftlauncher.deserializer` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/deserializer/ArgumentDeserializer.java#L42)

版本的 arguments 数组里每一项既可能是字符串，也可能是带 rules 的对象。
Gson 默认无法把这两种形式都映射到 Argument，这里做统一处理。

## 方法

### deserialize

```java
public Argument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
```

第 56 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `json` — 参数
- `typeOfT` — 参数
- `context` — 参数

**返回**：返回值

**异常**

- `JsonParseException` — 异常

---

### fromObject

```java
private Argument fromObject(JsonObject object, JsonDeserializationContext context)
```

第 75 行

内部方法：from Object。

**参数**

- `object` — 参数
- `context` — 参数

**返回**：返回值

---

### readRules

```java
private List<Rule> readRules(JsonObject object, JsonDeserializationContext context)
```

第 85 行

读取 Rules。

**参数**

- `object` — 参数
- `context` — 参数

**返回**：返回值

---

### readValue

```java
private List<String> readValue(JsonObject object, JsonDeserializationContext context)
```

第 102 行

读取 Value。

**参数**

- `object` — 参数
- `context` — 参数

**返回**：返回值

---
