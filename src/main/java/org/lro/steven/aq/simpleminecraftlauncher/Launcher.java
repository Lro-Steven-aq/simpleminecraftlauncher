package org.lro.steven.aq.simpleminecraftlauncher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author lro.steven.aq
 * @since 0.1
 */
public class Launcher {
    static void main(String[] args) {
        // only depend on the java standard library.
        try {
            Application._main(args);
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
