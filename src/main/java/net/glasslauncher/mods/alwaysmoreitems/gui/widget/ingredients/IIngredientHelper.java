package net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients;

import net.glasslauncher.mods.alwaysmoreitems.Focus;

import javax.annotation.*;
import java.util.*;

public interface IIngredientHelper<T> {
    Collection<T> expandSubtypes(Collection<T> contained);

    T getMatch(Iterable<T> contained, @Nonnull Focus toMatch);
}
