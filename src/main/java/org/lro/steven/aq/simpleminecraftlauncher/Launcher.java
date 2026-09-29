package org.lro.steven.aq.simpleminecraftlauncher;

import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
    private final static Logger logger = LoggerFactory.getLogger(Launcher.class);
    public static void main(String[] args) {
        logger.info("Start...");
        Application.launch(
                org.lro.steven.aq.simpleminecraftlauncher.Application.class,
                args
        );
    }
}
