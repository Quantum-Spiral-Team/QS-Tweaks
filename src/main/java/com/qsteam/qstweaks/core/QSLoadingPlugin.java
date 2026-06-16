package com.qsteam.qstweaks.core;

import com.google.common.collect.ImmutableMap;
import com.qsteam.qstweaks.config.QSDebugConfig;
import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import com.qsteam.qstweaks.core.transformer.modintegration.MoarTConPluginTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.*;
import java.util.function.BooleanSupplier;

@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE + 10)
public class QSLoadingPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    public static final Logger LOGGER = LogManager.getLogger(QSLoadingPlugin.class.getSimpleName());

    public static final boolean IS_CLIENT = FMLLaunchHandler.side().isClient();

    private static final Map<String, BooleanSupplier> SERVER_MIXIN_CONFIGS = Collections.emptyMap();
    private static final Map<String, BooleanSupplier> CLIENT_MIXIN_CONFIGS = Collections.emptyMap();

    private static final Map<String, BooleanSupplier> COMMON_MIXIN_CONFIGS = ImmutableMap.of(
            "mixins/debug/mixins.qstweaks.dimensions.json", () -> QSDebugConfig.GENERAL.dimConflictDetector
    );

    private static boolean isMoarTConCoreModPresent() {
        try {
            Class.forName("com.existingeevee.moretcon.mixinext.MoarTConCorePlugin", false, Launch.classLoader);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public String[] getASMTransformerClass() {
        List<String> transformerClassList = new ArrayList<>();
        if (isMoarTConCoreModPresent() && QSModIntegrationConfig.MOAR_TCON.enabled) transformerClassList.add(MoarTConPluginTransformer.class.getName());
        return transformerClassList.toArray(new String[0]);
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
