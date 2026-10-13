package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record Arguments(
        @SerializedName("default-user-jvm") List<Argument> defaultUserJavaVirtualMachine,
        List<Argument> game,
        @SerializedName("jvm") List<Argument> javaVirtualMachine
) {
}
