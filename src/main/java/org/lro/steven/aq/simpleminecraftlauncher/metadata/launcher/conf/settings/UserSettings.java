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
        private WebURL preciousBackgroundImageUrl;

        @SerializedName("JAVA_HOME")
        private String javaHome;

        @SerializedName("JAVA_EXE")
        private String executableJavaCommand;

        @SerializedName("is_fast_launch")
        private boolean fastLaunch;

        @SerializedName("alpha")  // 透明度: 1为不透明，0为不可见.
        private Double alpha;

        @SerializedName("last_version_file")
        private String lastVersionJsonFile; // 如果用户没有更改，那么默认就是 .minecraft/versions/<SHA256_of_last_version>.json

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
        public String lastVersionJsonFile() {
                return lastVersionJsonFile;
        }
        public void lastVersionJsonFile(String lastVersionJsonFile) {
                this.lastVersionJsonFile = lastVersionJsonFile;
        }
        public void javaHome(String javaHome) {
                this.javaHome = javaHome;
        }
}
