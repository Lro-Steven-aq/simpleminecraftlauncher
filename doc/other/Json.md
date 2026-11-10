# Json

`org.lro.steven.aq.simpleminecraftlauncher.json` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/json/Json.java#L36)

项目内共用的 Gson 实例：统一注册 WebURL 与版本参数的自定义反序列化器。

## 方法

### registerTypeAdapter

```java
.registerTypeAdapter(WebURL.class, new WebURLDeserializer()) .registerTypeAdapter(Argument.class, new ArgumentDeserializer()) .create()
```

第 45 行

内部方法：register Type Adapter。

**参数**

- `WebURL.class` — 参数
- `WebURLDeserializer()` — 参数

**返回**：返回值

---

### 构造器

```java
private Json()
```

第 52 行

创建 `Json` 实例。

---
