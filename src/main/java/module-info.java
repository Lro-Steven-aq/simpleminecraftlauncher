module org.lro.steven.aq.simpleminecraftlauncher {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;


    opens org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.fxml;
    exports org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.graphics;
    exports org.lro.steven.aq.simpleminecraftlauncher;
}