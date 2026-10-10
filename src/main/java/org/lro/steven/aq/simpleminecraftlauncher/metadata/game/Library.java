package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Library(

        @SerializedName("name")
        String mavenArtifactId,

        LDownloads downloads,
        List<Rule> rules,
        List<String> natives,

        @Nullable
        Extract extract
) {
}
