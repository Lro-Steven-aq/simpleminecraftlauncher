package org.lro.steven.aq.simpleminecraftlauncher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Launcher {
    static void main(String[] args) {
        try {
            Application.main(args);
        } catch (Exception e) {
            System.exit(getExitCode(e));
        }
    }
    private static int getExitCode(Exception e) {
        return switch (e) {
            case IOException ignored -> 1;
            case ClassNotFoundException ignored -> 2;
            case InvocationTargetException ignored -> 3;
            case NoSuchMethodException ignored -> 4;
            case InstantiationException ignored -> 5;
            case IllegalAccessException ignored -> 6;
            case null, default -> -1;
        };
    }
}
