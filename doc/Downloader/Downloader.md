# Downloader

`org.lro.steven.aq.simpleminecraftlauncher.plugin.api.downloader` · `interface` · [源码](../../src/main/java/org/lro/steven/aq/simpleminecraftlauncher/plugin/api/downloader/Downloader.java#L34)

下载器接口，由下载器插件实现并在启动时注册。
  

启动器只依赖这个接口，不直接进行网络下载。

## 方法

### download

```java
void download(String url, Path target) throws IOException
```

第 44 行

下载 `url` 到 `target`。
  

目标文件已存在且非空时应跳过；实现应支持基于 `.part` 的断点续传。

**参数**

- `url` — 下载地址
- `target` — 目标文件

**异常**

- `IOException` — 下载失败

---

### readString

```java
String readString(String url) throws IOException
```

第 52 行

读取远程文本内容。

**参数**

- `url` — 地址

**返回**：文本内容

**异常**

- `IOException` — 读取失败

---
