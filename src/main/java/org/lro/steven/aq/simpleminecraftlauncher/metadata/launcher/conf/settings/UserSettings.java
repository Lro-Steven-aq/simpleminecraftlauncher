package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings;

import com.google.gson.annotations.SerializedName;
import org.glavo.url.WebURL;

public record UserSettings(

        @SerializedName("background_image")
        WebURL preciousBackgroundImageUrl
) {
}
