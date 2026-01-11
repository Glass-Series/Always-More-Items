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

    /**
     * Creates a multiblock recipe.
     * @param identifier Used to create a translation key. The actual block name is specified the identifier's path.
     * @param description A text box which can be read by holding shift. Each line is added as a string to the list.
     * @param layers Provides the shape of the multiblock. Each layer is made of a string array where each string is a linear slice. Each character represents a different block. Spaces are for air blocks.
     * @param blockPatterns Used to convert characters from the layers into blocks. Each block pattern entry handles a single character.
     */
    public void addMultiblockRecipe(Identifier identifier, List<Object> description, String[][] layers, List<BlockPatternEntry> blockPatterns) {
        recipes.add(new MultiBlockRecipe(identifier, description, layers, blockPatterns));
    }

    public void addMultiblockRecipe(MultiBlockRecipe recipe){
        recipes.add(recipe);
    }
}
