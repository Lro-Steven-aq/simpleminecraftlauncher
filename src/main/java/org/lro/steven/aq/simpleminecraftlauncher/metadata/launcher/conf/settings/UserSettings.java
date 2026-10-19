/*
 * Copyright (C) ${year} Lro-Steven.aq
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings;

import com.google.gson.annotations.SerializedName;
import org.glavo.url.WebURL;

public class UserSettings {
        @SerializedName("background_image")
        WebURL preciousBackgroundImageUrl;

        @SerializedName("JAVA_HOME")
        String javaHome;

        @SerializedName("JAVA_EXE")
        String executableJavaCommand;

        @SerializedName("is_fast_launch")
        boolean fastLaunch;

        @SerializedName("alpha")  // 透明度: 1为不透明，0为不可见.
        Double alpha;

        public UserSettings() {}

        public WebURL preciousBackgroundImageUrl() {
                return preciousBackgroundImageUrl;
        }
        public String javaHome() {
                return javaHome;
        }
        public String executableJavaCommand() {
                return executableJavaCommand;
        }
        public boolean fastLaunch() {
                return fastLaunch;
        }
        public Double alpha() {
                return alpha;
        }
}
