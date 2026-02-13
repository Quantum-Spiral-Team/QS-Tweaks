package com.qsteam.qstweaks.core;

import com.google.common.collect.ImmutableMap;
import com.qsteam.qstweaks.QSConfig;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.*;
import java.util.function.BooleanSupplier;

public class QSLoadingPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    public static final boolean IS_CLIENT = FMLLaunchHandler.side().isClient();

    private static final Map<String, BooleanSupplier> SERVER_MIXIN_CONFIGS = Collections.emptyMap();
    private static final Map<String, BooleanSupplier> CLIENT_MIXIN_CONFIGS = Collections.emptyMap();

    private static final Map<String, BooleanSupplier> COMMON_MIXIN_CONFIGS = ImmutableMap.of(
            "mixins/debug/mixins.qstweaks.dimensions.json", () -> QSConfig.general.dimConflictDetector,
            "mixins/debug/mixins.qstweaks.langs.json", () -> QSConfig.general.missingLangsLogger
    );

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return QSTransformer.class.getName();
    }

    @Override
    public List<String> getMixinConfigs() {
        List<String> configs = new ArrayList<>();
        if (IS_CLIENT) {
            configs.addAll(CLIENT_MIXIN_CONFIGS.keySet());
        } else {
            configs.addAll(SERVER_MIXIN_CONFIGS.keySet());
        }
        configs.addAll(COMMON_MIXIN_CONFIGS.keySet());
        return new ArrayList<>(configs);
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        BooleanSupplier sidedSupplier = IS_CLIENT ? CLIENT_MIXIN_CONFIGS.get(mixinConfig) : SERVER_MIXIN_CONFIGS.get(mixinConfig);
        BooleanSupplier commonSupplier = COMMON_MIXIN_CONFIGS.get(mixinConfig);
        return sidedSupplier != null ? sidedSupplier.getAsBoolean() : commonSupplier == null || commonSupplier.getAsBoolean();
    }

}
