module SimpleMinecraftLauncher {
    requires org.junit.jupiter.api;
    requires org.lro.steven.aq.simpleminecraftlauncher;
    requires javafx.graphics;

    opens org.lro.steven.aq.simpleminecraftlauncher.test.plugin to org.junit.platform.commons;
}