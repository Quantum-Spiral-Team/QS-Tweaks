package com.qsteam.qstweaks.integration.moartcon;

import com.existingeevee.moretcon.mixinext.MoarTConCorePlugin;
import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import com.qsteam.qstweaks.core.QSLoadingPlugin;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class MoarTConLateMixinPlugin implements ILateMixinLoader {
    private static final String CLASS_NAME = MoarTConLateMixinPlugin.class.getSimpleName();

    @Override
    public List<String> getMixinConfigs() {
        if (Loader.isModLoaded("moretcon") && QSModIntegrationConfig.MOAR_TCON.enabled && QSModIntegrationConfig.MOAR_TCON.migrate2MixinBooter) {
            QSLoadingPlugin.LOGGER.info("[{}] Loading MoarTCon Mixin Late Config", CLASS_NAME);
            try {
                Field nonvanillaMixins = MoarTConCorePlugin.class.getDeclaredField("nonvanillaMixins");
                nonvanillaMixins.setAccessible(true);
                return new ArrayList<>(Collections.singleton((String) nonvanillaMixins.get(null)));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                QSLoadingPlugin.LOGGER.error("[{}]", CLASS_NAME, e);
            }
        }
        return Collections.emptyList();
    }

}
