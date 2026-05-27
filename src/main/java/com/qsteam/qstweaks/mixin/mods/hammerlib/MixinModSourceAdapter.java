package com.qsteam.qstweaks.mixin.mods.hammerlib;

import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import com.zeitheron.hammercore.utils.java.io.win32.ModSourceAdapter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

// Mixin taken from modpack IsolatedCrystal3
// https://github.com/friendlyhj/IsolatedCrystal3/blob/master/.minecraft/scripts/mixin/hammercore.zs
@Mixin(value = ModSourceAdapter.class, remap = false)
public abstract class MixinModSourceAdapter {

    @Shadow @Mutable @Final public static List<ModSourceAdapter.IllegalSite> ILLEGAL_SITES;

    @Inject(
            method = "<clinit>",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onInit(CallbackInfo ci) {
        if (QSModIntegrationConfig.HAMMER_LIB.fixCheckIllegalSites) {
            ILLEGAL_SITES = Collections.emptyList();
            ci.cancel();
        }
    }
}
