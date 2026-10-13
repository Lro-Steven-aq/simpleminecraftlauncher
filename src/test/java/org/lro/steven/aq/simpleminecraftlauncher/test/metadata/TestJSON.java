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
