# RuleEvaluator

`org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version` · `class` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/metadata/game/version/RuleEvaluator.java#L38)

版本 JSON 里 rules 的判定逻辑。
  

参数和依赖库都用它来判断当前机器是否适用。

## 方法

### 构造器

```java
private RuleEvaluator()
```

第 43 行

创建 `RuleEvaluator` 实例。

---

### isAllowed

```java
public static boolean isAllowed(List<Rule> rules, Features features)
```

第 50 行

按照规则列表判断是否允许。
后面的规则覆盖前面的规则，默认不允许。

---

### matches

```java
private static boolean matches(Rule rule, Features features)
```

第 72 行

内部方法：matches。

**参数**

- `rule` — 参数
- `features` — 参数

**返回**：返回值

---

### matchesOs

```java
private static boolean matchesOs(Rule.Os os)
```

第 81 行

内部方法：matches Os。

**参数**

- `os` — 参数

**返回**：返回值

---

### matchesFeatures

```java
private static boolean matchesFeatures(Map<String, Boolean> required, Features features)
```

第 97 行

内部方法：matches Features。

**参数**

- `required` — 参数
- `features` — 参数

**返回**：返回值

---

### checkVersionRange

```java
private static boolean checkVersionRange(VersionRange versionRange)
```

第 115 行

判断当前系统版本是否在给定范围内（仅 Windows 使用版本范围）。

---

### isGreaterThan

```java
private static boolean isGreaterThan(String[] a, String[] b)
```

第 135 行

判断是否 Greater Than。

**参数**

- `a` — 参数
- `b` — 参数

**返回**：返回值

---

### stringToInteger

```java
private static int stringToInteger(String string)
```

第 155 行

内部方法：string To Integer。

**参数**

- `string` — 参数

**返回**：返回值

---
