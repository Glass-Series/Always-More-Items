package net.glasslauncher.mods.alwaysmoreitems.registry.multiblock;

import net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock.BlockPatternEntry;
import net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock.MultiBlockRecipe;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockRecipeRegistry {
    public static MultiBlockRecipeRegistry INSTANCE = new MultiBlockRecipeRegistry();
    private final List<MultiBlockRecipe> recipes;

    public MultiBlockRecipeRegistry() {
        recipes = new ArrayList<>();
    }

    public List<MultiBlockRecipe> getRecipes() {
        return recipes;
    }

    public void addMultiblockRecipe(Identifier name, List<Object> description, List<String[]> layers, List<BlockPatternEntry> blockPatterns) {
        recipes.add(new MultiBlockRecipe(name, description, layers, blockPatterns));
    }
}
