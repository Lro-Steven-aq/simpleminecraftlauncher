package org.lro.steven.aq.simpleminecraftlauncher.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.lro.steven.aq.simpleminecraftlauncher.controller.Controller;
import org.lro.steven.aq.simpleminecraftlauncher.internationalization.Internationalization;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.Metadata;

import java.net.URL;
import java.util.ResourceBundle;

public class LauncherMainWindowController extends Controller {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button launchButton;

    @FXML
    private Pane root;

    private Metadata metadata;

    public LauncherMainWindowController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOpacity(0.4);
        // Internationalization
        backgroundImage.setImage(metadata.preciousBackgroundImage());
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public void initializeData() {
        // change ui here.
    }
}
