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
package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.io.IOUtils;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class ApplicationConfiguration {
        @SerializedName("configuration_version")
        String configurationVersion;

        @SerializedName("settings")
        UserSettings userSettings;



        public ApplicationConfiguration() {}

        public void save(String configurationFilePath) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
                        .create();
                try (OutputStream outputStream = new FileOutputStream(configurationFilePath)) {
                        String content = gson.toJson(this);
                        IOUtils.write(content, outputStream, Charset.defaultCharset());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }

        public UserSettings userSettings() {
                return userSettings;
        }
        public String configurationVersion() {
                return configurationVersion;
        }

}
