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
 *
 * Plugin Exception:
 * Plugins that use the package org.lro.steven.aq.simpleminecraftlauncher.plugin.api
 * and its subpackages, together with the JAR files they call, are not required
 * to be licensed under the GPLv3 and may be distributed under any license
 * terms. See the LICENSE file ("SimpleMinecraftLauncher Plugin Exception")
 * for the full definition and conditions.
 */
package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;

/** 用户设置。 */
public class UserSettings {
        @JsonAdapter(WebURLDeserializer.class)
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

        @SerializedName("game_directory")
        private String gameDirectory; // .minecraft 目录；缺省为 ~/.minecraft

        @SerializedName("username")
        private String username; // 离线启动用户名

        /** 创建一个空的用户设置。 */
        public UserSettings() {}

        /**
         * 返回背景图片 URL。
         * @return 背景图片 URL；未设置时为 null
         */
        public WebURL preciousBackgroundImageUrl() {
                return preciousBackgroundImageUrl;
        }

        /**
         * 返回 JAVA_HOME。
         * @return JAVA_HOME；未设置时为 null
         */
        public String javaHome() {
                return javaHome;
        }

        /**
         * 设置 JAVA_HOME。
         * @param javaHome JAVA_HOME
         */
        public void javaHome(String javaHome) {
                this.javaHome = javaHome;
        }

        /**
         * 返回 Java 可执行文件路径。
         * @return Java 可执行文件路径；未设置时为 null
         */
        public String executableJavaCommand() {
                return executableJavaCommand;
        }

        /**
         * 返回是否启用快速启动。
         * @return 是否快速启动
         */
        public boolean fastLaunch() {
                return fastLaunch;
        }

        /**
         * 返回是否启用快速启动。
         * @return 是否快速启动
         */
        public void fastLaunch(boolean enableFastLaunch) {
                fastLaunch = enableFastLaunch;
        }

        /**
         * 返回窗口透明度。
         * @return 透明度（0 到 1）；未设置时为 null
         */
        public Double alpha() {
                return alpha;
        }

        /**
         * 返回上次使用的版本 JSON 文件路径。
         * @return 版本 JSON 路径；未设置时为 null
         */
        public String lastVersionJsonFile() {
                return lastVersionJsonFile;
        }

        /**
         * 设置上次使用的版本 JSON 文件路径。
         * @param lastVersionJsonFile 版本 JSON 路径
         */
        public void lastVersionJsonFile(String lastVersionJsonFile) {
                this.lastVersionJsonFile = lastVersionJsonFile;
        }

        /**
         * 返回游戏目录。
         * @return 游戏目录；未设置时为 null
         */
        public String gameDirectory() {
                return gameDirectory;
        }

        /**
         * 设置游戏目录。
         * @param gameDirectory 游戏目录
         */
        public void gameDirectory(String gameDirectory) {
                this.gameDirectory = gameDirectory;
        }

        /**
         * 返回离线用户名。
         * @return 离线用户名；未设置时为 null
         */
        public String username() {
                return username;
        }

        /**
         * 设置离线用户名。
         * @param username 离线用户名
         */
        public void username(String username) {
                this.username = username;
        }
}
