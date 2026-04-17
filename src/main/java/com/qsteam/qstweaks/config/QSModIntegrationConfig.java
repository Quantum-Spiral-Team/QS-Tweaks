package com.qsteam.qstweaks.config;

import com.codetaylor.mc.pyrotech.Reference;
import com.qsteam.qstweaks.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID, name = Tags.CFG_FOLDER + Tags.CFG_MOD_INTEGRATION)
public class QSModIntegrationConfig {

    @Config.Name(Reference.MOD_ID)
//    @Config.Comment("") TODO
    public static final PyrotechConfig PYROTECH = new PyrotechConfig();

    public static class PyrotechConfig {

        @Config.Comment("Enable Pyrotech Tweaks")
        public boolean enabled = true;

        @Config.Comment("Disable JEI Worktable category")
        public boolean workbenchCategoryTweak = true;
    }

}
