package com.qsteam.qstweaks.mixin.mods.iu;

import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.recycler.RecyclerCategory;
import com.qsteam.qstweaks.config.QSModIntegrationConfig;
import com.qsteam.qstweaks.integration.iu.jei.NewRecyclerWrapper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(value = JEICompat.class, remap = false)
public abstract class MixinJEICompat {

    @Redirect(
            method = "registerCategories",
            at = @At(value = "INVOKE", target = "Lmezz/jei/api/recipe/IRecipeCategoryRegistration;addRecipeCategories([Lmezz/jei/api/recipe/IRecipeCategory;)V")
    )
    private void onAddRecipeCategories(IRecipeCategoryRegistration registry, IRecipeCategory<?>[] categories) {
        if (QSModIntegrationConfig.INDUSTRIAL_UPGRADE.recyclerCategoryTweak && QSModIntegrationConfig.INDUSTRIAL_UPGRADE.removeRecyclerCategory) {
            IRecipeCategory<?>[] filtered = Arrays.stream(categories)
                    .filter(c -> !(c instanceof RecyclerCategory))
                    .toArray(IRecipeCategory[]::new);

            if (filtered.length > 0) {
                registry.addRecipeCategories(filtered);
            }
        } else {
            registry.addRecipeCategories(categories);
        }
    }

    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lmezz/jei/api/IModRegistry;handleRecipes(Ljava/lang/Class;Lmezz/jei/api/recipe/IRecipeWrapperFactory;Ljava/lang/String;)V"))
    private <T> void onHandleRecipes(IModRegistry registry, Class<T> recipeClass, IRecipeWrapperFactory<T> factory, String categoryUid) {
        String recyclerUid = BlockSimpleMachine.recycler_iu.getName();

        if (QSModIntegrationConfig.INDUSTRIAL_UPGRADE.recyclerCategoryTweak && categoryUid.equals(recyclerUid)) {
            if (QSModIntegrationConfig.INDUSTRIAL_UPGRADE.removeRecyclerCategory) {
                return;
            }
            registry.handleRecipes(NewRecyclerWrapper.class, recipe -> recipe, categoryUid);
            return;
        }

        registry.handleRecipes(recipeClass, factory, categoryUid);
    }

    @Redirect(
            method = "register",
            at = @At(value = "INVOKE", target = "Lmezz/jei/api/IModRegistry;addRecipeCatalyst(Ljava/lang/Object;[Ljava/lang/String;)V")
    )
    private void onAddRecipeCatalyst(IModRegistry registry, Object stack, String[] ids) {
        if (QSModIntegrationConfig.INDUSTRIAL_UPGRADE.recyclerCategoryTweak && QSModIntegrationConfig.INDUSTRIAL_UPGRADE.removeRecyclerCategory) {
            String recyclerUid = new RecyclerCategory(registry.getJeiHelpers().getGuiHelper()).getUid();

            List<String> filteredIds = Arrays.stream(ids)
                    .filter(id -> !id.equals(recyclerUid))
                    .collect(Collectors.toList());

            if (!filteredIds.isEmpty()) {
                registry.addRecipeCatalyst(stack, filteredIds.toArray(new String[0]));
            }
        } else {
            registry.addRecipeCatalyst(stack, ids);
        }
    }

    @Redirect(
            method = "register(Lmezz/jei/api/IModRegistry;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/IModRegistry;addRecipes(Ljava/util/Collection;Ljava/lang/String;)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lcom/denfop/integration/jei/recycler/RecyclerHandler;getRecipes()Ljava/util/List;")
            ),
            remap = false
    )
    private void redirectAllAddRecipes(IModRegistry registry, Collection<?> recipes, String recipeCategoryUid) {
        String recyclerUid = BlockSimpleMachine.recycler_iu.getName();

        if (QSModIntegrationConfig.INDUSTRIAL_UPGRADE.recyclerCategoryTweak && recipeCategoryUid.equals(recyclerUid)) {
            if (QSModIntegrationConfig.INDUSTRIAL_UPGRADE.removeRecyclerCategory) {
                return;
            }

            registry.addRecipes(
                    Collections.singletonList(new NewRecyclerWrapper(
                            new ArrayList<>(registry.getIngredientRegistry().getAllIngredients(VanillaTypes.ITEM)),
                            IUItem.scrap)
                    ),
                    recyclerUid
            );
        } else {
            registry.addRecipes(recipes, recipeCategoryUid);
        }
    }

}
