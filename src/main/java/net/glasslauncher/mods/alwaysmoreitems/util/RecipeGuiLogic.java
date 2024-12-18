package net.glasslauncher.mods.alwaysmoreitems.util;

import com.google.common.collect.ImmutableList;
import net.glasslauncher.mods.alwaysmoreitems.api.RecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.minecraft.client.Minecraft;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RecipeGuiLogic {
    private static class State {
        /* The focus of this GUI */
        @Nonnull
        public final Focus focus;
        public int recipeCategoryIndex;
        public int pageIndex;

        public State(@Nonnull Focus focus, int recipeCategoryIndex, int pageIndex) {
            this.focus = focus;
            this.recipeCategoryIndex = recipeCategoryIndex;
            this.pageIndex = pageIndex;
        }
    }

    /* The current state of this GUI */
    @Nullable
    private State state = null;

    /* The previous states of this GUI */
    @Nonnull
    private final Stack<State> history = new Stack<>();

    /* List of Recipe Categories that involve the focus */
    @Nonnull
    private List<RecipeCategory> recipeCategories = ImmutableList.of();

    /* List of recipes for the currently selected recipeClass */
    @Nonnull
    private List<Object> recipes = Collections.emptyList();

    private int recipesPerPage = 0;

    public boolean setFocus(@Nonnull Focus focus) {
        return setFocus(focus, true);
    }

    public boolean back() {
        if (history.empty()) {
            return false;
        }
        State state = history.pop();
        if (setFocus(state.focus, false)) {
            this.state = state;
            return true;
        }
        return false;
    }

    private boolean setFocus(@Nonnull Focus focus, boolean saveHistory) {
        if (this.state != null && this.state.focus.equalsFocus(focus)) {
            return true;
        }

        List<RecipeCategory> types = focus.getCategories();
        if (types.isEmpty()) {
            return false;
        }

        this.recipeCategories = types;
        int recipeCategoryIndex = 0;

        ScreenHandler container = Minecraft.INSTANCE.player.container;
        if (container != null) {
            for (int i = 0; i < recipeCategories.size(); i++) {
                RecipeCategory recipeCategory = recipeCategories.get(i);
                if (AlwaysMoreItems.getRecipeRegistry().getRecipeTransferHandler(container, recipeCategory) != null) {
                    recipeCategoryIndex = i;
                    break;
                }
            }
        }

        if (this.state != null && saveHistory) {
            history.push(this.state);
        }
        this.state = new State(focus, recipeCategoryIndex, 0);

        updateRecipes();

        return true;
    }

    public boolean setCategoryFocus() {
        RecipeCategory recipeCategory = getRecipeCategory();
        if (recipeCategory == null) {
            return false;
        }

        if (this.state != null) {
            history.push(this.state);
        }

        this.recipeCategories = AlwaysMoreItems.getRecipeRegistry().getRecipeCategories();

        int recipeCategoryIndex = this.recipeCategories.indexOf(recipeCategory);

        this.state = new State(new Focus(), recipeCategoryIndex, 0);

        updateRecipes();

        return true;
    }

    public boolean setCategoryFocus(List<String> recipeCategoryUids) {
        List<RecipeCategory> recipeCategories = AlwaysMoreItems.getRecipeRegistry().getRecipeCategories(recipeCategoryUids);
        if (recipeCategories.isEmpty()) {
            return false;
        }

        if (this.state != null) {
            history.push(this.state);
        }

        this.recipeCategories = recipeCategories;

        this.state = new State(new Focus(), 0, 0);

        updateRecipes();

        return true;
    }

    public Focus getFocus() {
        if (state == null) {
            return null;
        }
        return state.focus;
    }

    public void setRecipesPerPage(int recipesPerPage) {
        if (state == null) {
            return;
        }
        if (this.recipesPerPage != recipesPerPage) {
            int recipeIndex = state.pageIndex * this.recipesPerPage;
            state.pageIndex = recipeIndex / recipesPerPage;

            this.recipesPerPage = recipesPerPage;
            updateRecipes();
        }
    }

    private void updateRecipes() {
        if (state == null) {
            return;
        }
        RecipeCategory recipeCategory = getRecipeCategory();
        recipes = state.focus.getRecipes(recipeCategory);
    }

    public RecipeCategory getRecipeCategory() {
        if (state == null || recipeCategories.isEmpty()) {
            return null;
        }
        return recipeCategories.get(state.recipeCategoryIndex);
    }

    @Nonnull
    public List<RecipeLayout> getRecipeWidgets(int posX, int posY, int spacingY) {
        if (state == null) {
            return Collections.emptyList();
        }

        List<RecipeLayout> recipeWidgets = new ArrayList<>();

        RecipeCategory recipeCategory = getRecipeCategory();
        if (recipeCategory == null) {
            return recipeWidgets;
        }

        RecipeRegistry recipeRegistry = AlwaysMoreItems.getRecipeRegistry();

        int recipeWidgetIndex = 0;
        for (int recipeIndex = state.pageIndex * recipesPerPage; recipeIndex < recipes.size() && recipeWidgets.size() < recipesPerPage; recipeIndex++) {
            Object recipe = recipes.get(recipeIndex);
            RecipeHandler recipeHandler = recipeRegistry.getRecipeHandler(recipe.getClass());
            if (recipeHandler == null) {
                AlwaysMoreItems.LOGGER.error("Couldn't find recipe handler for recipe: {}", recipe);
                continue;
            }

            @SuppressWarnings("unchecked")
            RecipeWrapper recipeWrapper = recipeHandler.getRecipeWrapper(recipe);

            RecipeLayout recipeWidget = new RecipeLayout(recipeWidgetIndex++, posX, posY, recipeCategory, recipeWrapper, state.focus);
            recipeWidgets.add(recipeWidget);

            posY += spacingY;
        }

        return recipeWidgets;
    }

    public void nextRecipeCategory() {
        if (state == null) {
            return;
        }
        int recipesTypesCount = recipeCategories.size();
        state.recipeCategoryIndex = (state.recipeCategoryIndex + 1) % recipesTypesCount;
        state.pageIndex = 0;
        updateRecipes();
    }

    public boolean hasMultiplePages() {
        return recipes.size() > recipesPerPage;
    }

    public void previousRecipeCategory() {
        if (state == null) {
            return;
        }
        int recipesTypesCount = recipeCategories.size();
        state.recipeCategoryIndex = (recipesTypesCount + state.recipeCategoryIndex - 1) % recipesTypesCount;
        state.pageIndex = 0;
        updateRecipes();
    }

    public void nextPage() {
        if (state == null) {
            return;
        }
        int pageCount = pageCount(recipesPerPage);
        state.pageIndex = (state.pageIndex + 1) % pageCount;
        updateRecipes();
    }

    public void previousPage() {
        if (state == null) {
            return;
        }
        int pageCount = pageCount(recipesPerPage);
        state.pageIndex = (pageCount + state.pageIndex - 1) % pageCount;
        updateRecipes();
    }

    private int pageCount(int recipesPerPage) {
        if (recipes.size() <= 1) {
            return 1;
        }

        return MathUtil.divideCeil(recipes.size(), recipesPerPage);
    }

    @Nonnull
    public String getPageString() {
        if (state == null) {
            return "1/1";
        }
        return (state.pageIndex + 1) + "/" + pageCount(recipesPerPage);
    }

    public boolean hasMultipleCategories() {
        return recipeCategories.size() > 1;
    }
}
