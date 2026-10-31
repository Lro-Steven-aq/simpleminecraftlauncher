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
package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** 启动器配置（版本号与用户设置）。 */
public class ApplicationConfiguration {
        @SerializedName("configuration_version")
        private String configurationVersion;

        @SerializedName("settings")
        private UserSettings userSettings;



        /** 创建一个空的启动器配置。 */
        public ApplicationConfiguration() {}

        public void save(String configurationFilePath) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
                        .create();
                String content = gson.toJson(this);
                Path target = Path.of(configurationFilePath);
                Path temporary = target.resolveSibling(target.getFileName() + ".tmp");
                try {
                        Files.createDirectories(target.toAbsolutePath().getParent());
                        Files.writeString(temporary, content);
                        moveIntoPlace(temporary, target);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }

        private static void moveIntoPlace(Path temporary, Path target) throws IOException {
                try {
                        Files.move(temporary, target,
                                StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException e) {
                        Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
                }
        }

        /**
         * 返回用户设置。
         * @return 返回值
         */
        public UserSettings userSettings() {
                return userSettings;
        }
        /**
         * 返回配置版本。
         * @return 返回值
         */
        public String configurationVersion() {
                return configurationVersion;
        }

}
