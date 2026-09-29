package org.lro.steven.aq.simpleminecraftlauncher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Application extends javafx.application.Application {
    private final Logger logger;

    public Application() {
        super();
        logger = LoggerFactory.getLogger(Application.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // size: 577 x 900
        Parent root = FXMLLoader.load(getClass().getResource("Application.fxml"));

        Font.loadFont(getClass().getResourceAsStream("LXGWWenKaiMono-Regular.ttf"), 48);

        primaryStage.setTitle("Simple Launcher");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        logger.info("Main Window shows...");
    }
}
