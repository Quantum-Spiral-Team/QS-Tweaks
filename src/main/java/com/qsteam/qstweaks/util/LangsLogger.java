package com.qsteam.qstweaks.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.qsteam.qstweaks.config.QSDebugConfig;
import com.qsteam.qstweaks.Tags;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class LangsLogger {

    private static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME + "/LangsLogger");

    private static final String[] TARGET_LANGS = QSDebugConfig.GENERAL.languages;
    private static final Multimap<String, String> missingKeys = ArrayListMultimap.create();

    public static void markMissing(String key) {
        if (key == null || key.isEmpty()) return;
        missingKeys.put(detectModId(key), key);
    }

    private static String detectModId(String key) {
        if (Loader.instance() == null || !Loader.instance().hasReachedState(LoaderState.CONSTRUCTING)) {
            return "minecraft";
        }

        String[] parts = key.split("\\.");
        for (String part : parts) {
            try {
                if (Loader.isModLoaded(part)) return part;
                else if (part.equals("minecraft")) return "minecraft";
            } catch (Exception e) {
                return "unknown";
            }
        }
        return "unknown";
    }

    public static void dumpLog() {
        Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();

        if (missingKeys.isEmpty()) {
            LOGGER.info("Missing keys for {} lang not found.", TARGET_LANGS);
        } else {
            LOGGER.debug("Starting search for missing keys for {}", TARGET_LANGS);

            List<String> sortedModIds = new ArrayList<>(missingKeys.keySet());
            Collections.sort(sortedModIds);

            for (String modId : sortedModIds) {
                Collection<String> keys = missingKeys.get(modId);
                if (keys.isEmpty()) continue;

                String modName;
                switch (modId) {
                    case "minecraft":
                        modName = "Minecraft";
                        break;
                    case "unknown":
                        modName = "Unknown";
                        break;
                    default:
                        ModContainer container = modMap.get(modId);
                        modName = (container != null) ? container.getName() : "Unknown";
                }

                List<String> sortedKeys = new ArrayList<>(keys);
                Collections.sort(sortedKeys);

                LOGGER.info("{} {}: {}", modName, modId, sortedKeys);
            }

            LOGGER.debug("Finish search for missing keys for {}", TARGET_LANGS);
        }
    }

}
