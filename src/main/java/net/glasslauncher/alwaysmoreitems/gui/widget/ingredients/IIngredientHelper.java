package net.glasslauncher.alwaysmoreitems.gui.widget.ingredients;

import net.glasslauncher.alwaysmoreitems.Focus;

import javax.annotation.*;
import java.util.*;

public interface IIngredientHelper<T> {
    Collection<T> expandSubtypes(Collection<T> contained);

    T getMatch(Iterable<T> contained, @Nonnull Focus toMatch);
}
