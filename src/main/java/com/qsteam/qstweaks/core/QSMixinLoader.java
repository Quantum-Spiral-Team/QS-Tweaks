package com.qsteam.qstweaks.core;

import com.google.common.collect.ImmutableMap;
import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class QSMixinLoader implements ILateMixinLoader {

    private static final Map<String, BooleanSupplier> MIXIN_CONFIGS = ImmutableMap.of(
            "mixins/mod/mixins.qstweaks.pyrotech.json", () -> Loader.isModLoaded("pyrotech") && QSModIntegrationConfig.PYROTECH.enabled,
            "mixins/mod/mixins.qstweaks.iu.json", () -> Loader.isModLoaded("industrialupgrade") && QSModIntegrationConfig.INDUSTRIAL_UPGRADE.enabled
    );

    @Override
    public List<String> getMixinConfigs() {
        return new ArrayList<>(MIXIN_CONFIGS.keySet());
    }

    @Override
    public boolean shouldMixinConfigQueue(String config) {
        return MIXIN_CONFIGS.get(config).getAsBoolean();
    }

}
