package net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients;

import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface IIngredientHelper<T> {
    Collection<T> expandSubtypes(Collection<T> contained);

    T getMatch(Iterable<T> contained, @Nonnull Focus toMatch);
}
