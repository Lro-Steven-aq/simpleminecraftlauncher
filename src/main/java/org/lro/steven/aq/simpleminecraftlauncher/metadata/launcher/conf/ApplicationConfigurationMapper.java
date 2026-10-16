package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf;

import com.google.gson.annotations.SerializedName;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;

public record ApplicationConfigurationMapper(

        @SerializedName("configuration_version")
        String configurationVersion,

        @SerializedName("settings")
        UserSettings userSettings

) {
}
