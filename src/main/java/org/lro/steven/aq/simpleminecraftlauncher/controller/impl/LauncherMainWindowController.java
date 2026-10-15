package org.lro.steven.aq.simpleminecraftlauncher.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.lro.steven.aq.simpleminecraftlauncher.controller.Controller;
import org.lro.steven.aq.simpleminecraftlauncher.internationalization.Internationalization;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.Metadata;

public class LauncherMainWindowController extends Controller {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button launchButton;

    @FXML
    private Pane root;

    private Metadata metadata;

    public LauncherMainWindowController() {}

    @FXML
    public void initialize() {
        root.setOpacity(0.4);
        // Internationalization
        backgroundImage.setImage(metadata.preciousBackgroundImage());
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
