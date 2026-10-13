package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record Library(

        @SerializedName("name")
        String mavenArtifactId,

        Downloads downloads,
        List<Rule> rules,
        List<String> natives,

        @Nullable
        Extract extract
) {

        public record Downloads(
                @Nullable
                Artifact artifact,

                Map<String, Artifact> classifiers
        ) {
        }
}
