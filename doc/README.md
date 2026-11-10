# 文档索引

按项目接口分组；每个类的文档以类名命名。

## Plugin

插件生命周期接口及其实现/管理器。

- [Plugin](Plugin/Plugin.md)
- [PluginException](Plugin/PluginException.md)
- [PluginManager](Plugin/PluginManager.md)

## PluginLoader

插件加载接口、默认实现与类加载器。

- [DefaultImplementationPluginLoader](PluginLoader/DefaultImplementationPluginLoader.md)
- [PluginClassLoader](PluginLoader/PluginClassLoader.md)
- [PluginLoader](PluginLoader/PluginLoader.md)

## SignalBus

信号总线接口与默认实现。

- [DefaultImplementationSignalBus](SignalBus/DefaultImplementationSignalBus.md)
- [SignalBus](SignalBus/SignalBus.md)

## Event

事件接口及日志、弹窗、下载、启动事件。

- [DownloadEvent](Event/DownloadEvent.md)
- [Event](Event/Event.md)
- [LaunchEvent](Event/LaunchEvent.md)
- [LogEvent](Event/LogEvent.md)
- [PopupEvent](Event/PopupEvent.md)

## Command

命令接口与默认实现。

- [Command](Command/Command.md)

## Configuration

参数配置接口与默认实现。

- [Configuration](Configuration/Configuration.md)
- [DefaultImplementationJsonConfiguration](Configuration/DefaultImplementationJsonConfiguration.md)

## Parser

版本 JSON 解析接口与默认实现。

- [DefaultImplementationJsonParser](Parser/DefaultImplementationJsonParser.md)
- [Parser](Parser/Parser.md)

## VersionManager

版本管理接口与默认实现。

- [DefaultImplementationVersionManager](VersionManager/DefaultImplementationVersionManager.md)
- [VersionManager](VersionManager/VersionManager.md)

## Downloader

下载器接口与 OkHttp 实现。

- [Downloader](Downloader/Downloader.md)

## Controller

窗口控制器基类与主窗口实现。

- [Controller](Controller/Controller.md)
- [LauncherMainWindowController.ManifestCallback](Controller/LauncherMainWindowController.ManifestCallback.md)
- [LauncherMainWindowController](Controller/LauncherMainWindowController.md)

## 未归入项目接口的类

启动器核心、数据模型与辅助类。

- [Action](other/Action.md)
- [Application](other/Application.md)
- [ApplicationConfiguration](other/ApplicationConfiguration.md)
- [ApplicationConfigurationLoader](other/ApplicationConfigurationLoader.md)
- [Argument](other/Argument.md)
- [ArgumentDeserializer](other/ArgumentDeserializer.md)
- [Arguments](other/Arguments.md)
- [Artifact](other/Artifact.md)
- [AssetIndex](other/AssetIndex.md)
- [Channel](other/Channel.md)
- [ChannelConsumer](other/ChannelConsumer.md)
- [Client](other/Client.md)
- [DesktopFrost](other/DesktopFrost.md)
- [Downloads](other/Downloads.md)
- [Environment](other/Environment.md)
- [EventConsumer](other/EventConsumer.md)
- [Exception](other/Exception.md)
- [Extract](other/Extract.md)
- [Features](other/Features.md)
- [File](other/File.md)
- [GameDirectory](other/GameDirectory.md)
- [GameMetadataManifest](other/GameMetadataManifest.md)
- [I18n](other/I18n.md)
- [Internationalization](other/Internationalization.md)
- [JavaVersion](other/JavaVersion.md)
- [Json](other/Json.md)
- [Latest](other/Latest.md)
- [LaunchConfiguration](other/LaunchConfiguration.md)
- [Launcher](other/Launcher.md)
- [LauncherExitCode](other/LauncherExitCode.md)
- [Library.Classifiers](other/Library.Classifiers.md)
- [Library.Downloads](other/Library.Downloads.md)
- [Library.Natives](other/Library.Natives.md)
- [Library](other/Library.md)
- [Logging.Client](other/Logging.Client.md)
- [Logging](other/Logging.md)
- [Metadata](other/Metadata.md)
- [OperatingSystem](other/OperatingSystem.md)
- [Rule.Os](other/Rule.Os.md)
- [Rule](other/Rule.md)
- [RuleEvaluator](other/RuleEvaluator.md)
- [Sendable](other/Sendable.md)
- [Server](other/Server.md)
- [Signals](other/Signals.md)
- [UIManager](other/UIManager.md)
- [UserSettings](other/UserSettings.md)
- [Version](other/Version.md)
- [VersionManifest](other/VersionManifest.md)
- [VersionRange](other/VersionRange.md)
- [VersionType](other/VersionType.md)
- [WebURLDeserializer](other/WebURLDeserializer.md)
