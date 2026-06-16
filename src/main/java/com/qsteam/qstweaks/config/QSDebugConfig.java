package com.qsteam.qstweaks.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.qsteam.qstweaks.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID, name = Tags.CFG_FOLDER + Tags.CFG_DEBUG)
public class QSDebugConfig {

    @Config.Name("debug")
    @Config.Comment("Debug options")
    public static final DebugCategory GENERAL = new DebugCategory();

    public static class DebugCategory {
        @Config.Comment("Logging dimension IDs conflicts")
        public boolean dimConflictDetector = false;
    }

    static {
        ConfigAnytime.register(QSDebugConfig.class);
    }

}
