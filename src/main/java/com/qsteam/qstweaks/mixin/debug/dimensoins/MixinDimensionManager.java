package com.qsteam.qstweaks.mixin.debug.dimensoins;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DimensionManager.class, remap = false)
public abstract class MixinDimensionManager {

    @Final
    private static Int2ObjectMap<Object> dimensions;

    @Unique
    private static Logger getLogger() {
        return LogManager.getLogger("DimConflictDetector");
    }

    @Inject(method = "registerDimension", at = @At("HEAD"))
    private static void onRegisterDimension(int id, DimensionType type, CallbackInfo ci) {
        Logger log = getLogger();
        if (dimensions == null) {
            log.info("dimensions == null");
        } else {
            ModContainer mod =  Loader.instance().activeModContainer();
            String modId = mod != null ? mod.getModId() : "null";
            log.info("Trying to register dimension \"{}\" with id {} from mod {}", type.name(), id, modId);
        }

        if (dimensions.containsKey(id)) {
            Object existingProvider = dimensions.get(id);

            log.error(
                    "================================================================\n" +
                    "!!! DIMENSION ID CONFLICT DETECTED !!!\n" +
                    "Attempted to register ID: {} with Dim Type: {}, with ID: {}\n" +
                    "Already registered by Dim Type: {}\n" +
                    "Stacktrace of the conflicting registration:",
                    id, type.getName(), type.getId(), existingProvider);

            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            for (int i = 2; i < Math.min(stackTrace.length, 20); i++) {
                log.error("at {}", stackTrace[i]);
            }
            log.error("================================================================");
        }
    }

}
