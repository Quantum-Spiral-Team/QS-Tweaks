package com.qsteam.qscore;

import net.minecraftforge.common.config.Config;

@SuppressWarnings("unused")
@Config(modid = Tags.MOD_ID, name = Tags.MOD_NAME)
public class QSConfig {

    @Config.Name("debug")
    @Config.Comment("Debug options")
    public static final DebugCategory general = new DebugCategory();

    public static class DebugCategory {
        @Config.Comment("Logging dimension IDs conflicts")
        public boolean dimConflictDetector = true;
    }

}
