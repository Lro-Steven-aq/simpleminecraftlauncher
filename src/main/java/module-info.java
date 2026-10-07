module org.lro.steven.aq.simpleminecraftlauncher {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires static lombok;
    requires org.glavo.url;

    // ui
    opens org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.fxml;
    exports org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.graphics;

    exports org.lro.steven.aq.simpleminecraftlauncher;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

}