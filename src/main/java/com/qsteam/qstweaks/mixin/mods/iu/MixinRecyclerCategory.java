package com.qsteam.qstweaks.mixin.mods.iu;

import com.denfop.api.recipe.InventoryMultiRecipes;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.integration.jei.recycler.RecyclerCategory;
import com.denfop.integration.jei.recycler.RecyclerWrapper;
import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = RecyclerCategory.class, remap = false)
public abstract class MixinRecyclerCategory {

    @Final @Shadow private ContainerMultiMachine container1;

    @Inject(
            method = "setRecipe(Lmezz/jei/api/gui/IRecipeLayout;Lmezz/jei/api/recipe/IRecipeWrapper;Lmezz/jei/api/ingredients/IIngredients;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void injectionSetRecipe(IRecipeLayout layout, IRecipeWrapper recipes, IIngredients ingredients, CallbackInfo ci) {
        if (QSModIntegrationConfig.INDUSTRIAL_UPGRADE.recyclerCategoryTweak && !QSModIntegrationConfig.INDUSTRIAL_UPGRADE.removeRecyclerCategory) {
            IGuiItemStackGroup isg = layout.getItemStacks();

            List<SlotInvSlot> slots1 = this.container1.findClassSlots(InventoryMultiRecipes.class);
            List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

            if (!inputs.isEmpty() && !slots1.isEmpty()) {
                isg.init(0, true, slots1.get(0).getJeiX(), slots1.get(0).getJeiY());
                isg.set(0, inputs.get(0));
            }

            SlotInvSlot outputSlot = this.container1.findClassSlot(InventoryOutput.class);
            isg.init(1, false, outputSlot.getJeiX(), outputSlot.getJeiY());

            List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
            if (!outputs.isEmpty() && !outputs.get(0).isEmpty()) {
                isg.set(1, outputs.get(0).get(0));
            }

            ci.cancel();
        }
    }

}
