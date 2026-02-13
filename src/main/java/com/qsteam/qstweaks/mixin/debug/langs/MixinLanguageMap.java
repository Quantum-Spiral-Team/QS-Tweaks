package com.qsteam.qstweaks.mixin.debug.langs;

import com.qsteam.qstweaks.util.LangsLogger;
import net.minecraft.util.text.translation.LanguageMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LanguageMap.class)
public abstract class MixinLanguageMap {

    @Shadow @Final private Map<String, String> languageList;

    @Inject(method = "translateKey", at = @At("HEAD"))
    private void onTranslateKey(String key, CallbackInfoReturnable<String> cir) {
        if (!this.languageList.containsKey(key)) {
            LangsLogger.markMissing(key);
        }
    }

}
