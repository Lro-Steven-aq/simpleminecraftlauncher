package org.lro.steven.aq.simpleminecraftlauncher.internationalization;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is used to process i18n
 * @author lro.steven.aq
 * @since 0.1
 */
public final class Internationalization {

    private static String INTERNATIONALIZATION_FILE_ROOT = ".simpleminecraftlauncher/i18n";
    private Map<Locale, Properties> properties;    // K -> V
    private final static Internationalization internationalization;

    static {
        internationalization = new Internationalization();
        initialize();
    }
    private static void initialize() {
        try {
            internationalization.addProperties(
                    Locale.ENGLISH,
                    internationalization.load("%s/language.properties".formatted(INTERNATIONALIZATION_FILE_ROOT))
            );
            Files.list(new File("%s/".formatted(INTERNATIONALIZATION_FILE_ROOT)).toPath())
                    .filter((f) -> f.toFile().getName().startsWith("language_"))
                    .forEach((f) -> {
                            internationalization.addProperties(Locale.forLanguageTag(parseLanguageTag(f.toFile().getName())), buildPropertiesFromFile(f.toFile()));
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return internationalization.properties.getOrDefault(
                Locale.getDefault(),
                internationalization.properties.get(Locale.ENGLISH)  // 退化为英语
        ).getProperty(key, key);
    }
    private Internationalization() {}


    private void addProperties(Locale locale, Properties properties) {
        this.properties.put(locale, properties);
    }
    private Properties load(String propertiesFile) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile));
        return properties;
    }
    private static String parseLanguageTag(String fileName) {
        Pattern pattern = Pattern.compile("language_(.+)");
        return pattern.matcher(fileName).group(1);
    }
    private static Properties buildPropertiesFromFile(File file) {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream(file)) {
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            PluginManager.get().getSignalBus().emit("log.error", LogEvent.of("Error to read/load the language file"));
        }
        return properties;
    }
}
