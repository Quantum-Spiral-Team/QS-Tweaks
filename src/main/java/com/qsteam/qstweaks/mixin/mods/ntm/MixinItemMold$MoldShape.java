package com.qsteam.qstweaks.mixin.mods.ntm;

import com.hbm.items.machine.ItemMold;
import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemMold.MoldShape.class)
public class MixinItemMold$MoldShape {

    @Redirect(
            method = "getOutput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/registry/RegistryNamespaced;getNameForObject(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    public Object getOutput(RegistryNamespaced<ResourceLocation, Item> registryNamespaced, Object object) {
        Item item = ((ItemStack) object).getItem();
        ResourceLocation oreRegistryName = Item.REGISTRY.getNameForObject(item);
        if (QSModIntegrationConfig.NTM_CE.moldShapeTweak) {
            return oreRegistryName == null ? item.getRegistryName() : oreRegistryName;
        }
        return oreRegistryName;
    }

}
