package org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfigurationMapper;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationConfiguration implements Configuration {

    private ApplicationConfigurationMapper configurations;
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
            .create();
    private static final SignalBus signalBus = PluginManager.get().getSignalBus();

    public ApplicationConfiguration() {
        this.configurations = null;
    }

    @Override
    public void load(String content) {
        // parse it.
        configurations = GSON.fromJson(content, ApplicationConfigurationMapper.class);
        check(configurations);
    }

    @Override
    public void load(File file) {
        byte[] buffer = IOUtils.byteArray();
        try (InputStream inputStream = new FileInputStream(file)) {
            IOUtils.readFully(inputStream, buffer);
        } catch (FileNotFoundException e) {
            signalBus.emit("log.error",
                    LogEvent.of("Can't find launcher configuration file: %s; we will use default configurations".formatted(file)));
        } catch (IOException e) {
            signalBus.emit("log.error",
                    LogEvent.of("IO Exception, at reading file: %s; we will use default configurations".formatted(file)));
        }

        load(new String(buffer));

    }

    private void check(ApplicationConfigurationMapper configurations) {
        if (configurations == null) {
            throw new IllegalArgumentException("Null Configurations");
        }
        if (configurations.configurationVersion() == null) {
            signalBus.emit("log.error", LogEvent.of("Can't find the version of configurations"));
        }
        if (configurations.userSettings() == null) {
            signalBus.emit("log.warn", LogEvent.of("Can't find the user's settings."));
        }
    }

    @Override
    public Map<String, String> getMap() {
        return GSON.fromJson(GSON.toJson(configurations), Map.class);
    }

    public ApplicationConfigurationMapper getConfigurations() {
        return configurations;
    }
}
