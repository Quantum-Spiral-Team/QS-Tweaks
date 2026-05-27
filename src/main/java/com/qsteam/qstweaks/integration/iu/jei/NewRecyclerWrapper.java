package com.qsteam.qstweaks.integration.iu.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class NewRecyclerWrapper implements IRecipeWrapper {
    private final List<List<ItemStack>> inputs;
    private final ItemStack outputStack;

    public NewRecyclerWrapper(List<ItemStack> allItems, ItemStack outputStack) {
        this.inputs = Collections.singletonList(allItems);
        this.outputStack = outputStack;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
        ingredients.setOutputs(VanillaTypes.ITEM, Collections.singletonList(this.outputStack));
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

}
