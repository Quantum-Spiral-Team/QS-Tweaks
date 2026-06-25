package com.qsteam.qstweaks.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import com.qsteam.qstweaks.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID, name = Tags.CFG_FOLDER + Tags.CFG_MOD_INTEGRATION)
public class QSModIntegrationConfig {

    @Config.Name("pyrotech")
//    @Config.Comment("") TODO
    public static final PyrotechConfig PYROTECH = new PyrotechConfig();

    public static class PyrotechConfig {

        @Config.Comment("Enable Pyrotech Tweaks")
        public boolean enabled = true;

        @Config.Comment("Disable JEI Worktable category")
        public boolean workbenchCategoryTweak = true;

//        @Config.Comment("") //TODO
        public boolean mergeDryingCategory = true;
    }

    @Config.Name("industrial_upgrade")
    public static final IUCategory INDUSTRIAL_UPGRADE = new IUCategory();

    public static class IUCategory {
        @Config.Comment("Enable IU Tweaks")
        public boolean enabled = true;

        @Config.Comment("Disable JEI Recycler category")
        public boolean recyclerCategoryTweak = true;

        @Config.Comment("Если да, категория полностью удаляется, если нет, то меняется на оптимальную версию")
        public boolean removeRecyclerCategory = false;
    }

    @Config.Name("moar_tcon")
    public static final MoarTConCategory MOAR_TCON = new MoarTConCategory();

    public static class MoarTConCategory {
        @Config.Comment("Enable MoarTCon Tweaks")
        public boolean enabled = true;

        @Config.Comment("Enable migrate mod to MixinBooter")
        public boolean migrate2MixinBooter = true;
    }

    @Config.Name("ntm-ce")
    public static final NTMCategory NTM_CE = new NTMCategory();

    public static class NTMCategory {
        @Config.Comment("Enable NTM:CE Tweaks")
        public boolean enabled = true;

        @Config.Comment("Fix NullPointer in ItemMold$MoldShape (https://github.com/Warfactory-Official/Hbm-s-Nuclear-Tech-CE/pull/1556)")
        public boolean moldShapeTweak = true;
    }

    static {
        ConfigAnytime.register(QSModIntegrationConfig.class);
    }

}
