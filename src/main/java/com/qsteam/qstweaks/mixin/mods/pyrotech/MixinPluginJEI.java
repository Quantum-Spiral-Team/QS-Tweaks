package com.qsteam.qstweaks.mixin.mods.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic.Blocks;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockDryingRack.EnumType;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.PluginJEI;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category.JEIRecipeCategoryDryingRack;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category.JEIRecipeCategoryWorktable;
import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = PluginJEI.class, remap = false)
public abstract class MixinPluginJEI {

    @Redirect(
            method = "registerCategories",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/recipe/IRecipeCategoryRegistration;addRecipeCategories([Lmezz/jei/api/recipe/IRecipeCategory;)V"
            )
    )
    private void redirectRegisterCategories(IRecipeCategoryRegistration registry, IRecipeCategory<? extends IRecipeWrapper>[] categories) {
        IRecipeCategory<?>[] filtered = Arrays.stream(categories)
                .filter(this::qstweaks$shouldKeepCategory)
                .toArray(IRecipeCategory[]::new);

        registry.addRecipeCategories(filtered);
    }

    @Unique
    private boolean qstweaks$shouldKeepCategory(IRecipeCategory<? extends IRecipeWrapper> category) {
        return !(
                (QSModIntegrationConfig.PYROTECH.workbenchCategoryTweak && category instanceof JEIRecipeCategoryWorktable)
                || (QSModIntegrationConfig.PYROTECH.mergeDryingCategory && category instanceof JEIRecipeCategoryDryingRack)
        );
    }

    @Inject(
            method = "register",
            at = @At("TAIL")
    )
    private void qstweaks$register(IModRegistry registry, CallbackInfo cb) {
        if (QSModIntegrationConfig.PYROTECH.workbenchCategoryTweak) {
            registry.addRecipeCatalyst(new ItemStack(Blocks.WORKTABLE), VanillaRecipeCategoryUid.CRAFTING);
            registry.addRecipeCatalyst(new ItemStack(Blocks.WORKTABLE_STONE), VanillaRecipeCategoryUid.CRAFTING);
        }
        if (QSModIntegrationConfig.PYROTECH.mergeDryingCategory) {
            registry.addRecipeCatalyst(new ItemStack(Blocks.DRYING_RACK, 1, EnumType.NORMAL.getMeta()), "pyrotech.crude.drying");
        }
    }

}
