package com.qsteam.qstweaks.core;

import com.codetaylor.mc.pyrotech.Reference;
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
        "mixins/mod/mixins.qstweaks.pyrotech.json", () -> Loader.isModLoaded(Reference.MOD_ID) && QSModIntegrationConfig.PYROTECH.enabled
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
