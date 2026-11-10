# WebURLDeserializer

`org.lro.steven.aq.simpleminecraftlauncher.deserializer` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/deserializer/WebURLDeserializer.java#L43)

WebURL 与 JSON 字符串互转。
  

反序列化时解析 URL，序列化时写回 `toString()`，避免 Gson 反射 WebURL 内部字段。

## 方法

### deserialize

```java
public WebURL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
```

第 54 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `json` — 参数
- `typeOfT` — 参数
- `context` — 参数

**返回**：返回值

**异常**

- `JsonParseException` — 异常

---

### serialize

```java
public JsonElement serialize(WebURL src, Type typeOfSrc, JsonSerializationContext context)
```

第 69 行 · `@Override`

实现/覆写接口或父类方法。

**参数**

- `src` — 参数
- `typeOfSrc` — 参数
- `context` — 参数

**返回**：返回值

---
