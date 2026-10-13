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
package org.lro.steven.aq.simpleminecraftlauncher.test.metadata;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.apache.commons.io.IOUtils;
import org.glavo.url.WebURL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.VersionManifest;

import java.lang.reflect.Type;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestJSON {

    @Test
    public void test() throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
                .create();
        VersionManifest manifest = gson.fromJson(
                Files.readString(
                        Paths.get("src/test/resources/test-data-version_manifest.json")
                ),
                VersionManifest.class
        );

        manifest.versions().forEach(IO::println);

    }

    public static class WebURLDeserializer implements JsonDeserializer<WebURL> {

        @Override
        public WebURL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return WebURL.parse(json.getAsString());
        }
    }

}
