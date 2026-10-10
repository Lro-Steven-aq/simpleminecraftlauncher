package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public enum OperatingSystem {

    @SerializedName("osx")
    OSX,

    @SerializedName("linux")
    LINUX,

    @SerializedName("windows")
    WINDOWS;
    public static org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem getOs() {
        return switch (System.getProperty("os.name").toLowerCase(Locale.ENGLISH)) {
            case String string when string.contains("win") -> org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem.WINDOWS;
            case String string when string.contains("mac") -> org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem.OSX;
            case String string when string.contains("nix") || string.contains("nux") -> org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem.LINUX;
            case null, default -> null;
        };

    }
}
