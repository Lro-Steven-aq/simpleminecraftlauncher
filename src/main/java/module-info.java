module org.lro.steven.aq.simpleminecraftlauncher {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.glavo.url;
    requires com.google.gson;
    requires org.jetbrains.annotations;

    // ui
    opens org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.fxml;
    exports org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.graphics;

    // for plugin-system.
    exports org.lro.steven.aq.simpleminecraftlauncher;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

    opens org.lro.steven.aq.simpleminecraftlauncher;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game to com.google.gson;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest;
}