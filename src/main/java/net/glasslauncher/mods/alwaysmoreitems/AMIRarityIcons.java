package net.glasslauncher.mods.alwaysmoreitems;

public class AMIRarityIcons {

    public static final boolean[][] NONE = new boolean[][] {
            new boolean[] {}, // So I don't have to do extra checks. Optimisation/lazy code.
    };
    public static final boolean[][] BAD = new boolean[][] {
            new boolean[] { false, true, false, false, false, false, false, false, false, false, true, false },
            new boolean[] { true, false, false, false, false, true, true, false, false, false, false, true },
            new boolean[] { false, true, false, true, true, true, true, true, true, false, true, false },
            new boolean[] { true, false, false, true, false, false, true, false, true, false, false, true },
            new boolean[] { false, false, true, true, true, true, true, true, true, true, false, false },
            new boolean[] { false, true, false, true, false, false, true, false, false, true, true, false },
            new boolean[] { false, true, false, false, true, false, false, true, false, false, true, false },
            new boolean[] { false, true, true, true, true, true, true, true, true, true, true, false },
            new boolean[] { true, false, true, false, false, false, false, true, false, false, false, true },
            new boolean[] { true, false, false, true, false, false, false, false, true, false, false, true },
            new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true },
            new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true },
    };

    public static final boolean[][] NORMAL = new boolean[][] {
            new boolean[] { true, false, false },
            new boolean[] { true, false, false },
            new boolean[] { false, true, false },
            new boolean[] { false, true, false },
            new boolean[] { false, false, true },
            new boolean[] { false, false, true },
            new boolean[] { false, false, true },
            new boolean[] { false, true, false },
            new boolean[] { false, true, false },
            new boolean[] { true, false, false },
            new boolean[] { true, false, false },
    };
    public static final boolean[][] ARTEFACT = new boolean[][] {
            new boolean[] { true, true, false, false, false, false, false, true },
            new boolean[] { true, true, true, false, false, false, false, true },
            new boolean[] { false, true, true, false, false, false, true, true },
            new boolean[] { false, false, false, false, false, false, true, false },
            new boolean[] { false, false, false, false, true, true, true, false },
            new boolean[] { true, true, true, true, true, true, true, false },
            new boolean[] { true, true, true, true, true, true, true, false },
            new boolean[] { false, false, false, false, true, true, true, false },
            new boolean[] { false, false, false, false, false, false, true, false },
            new boolean[] { false, true, true, false, false, false, true, true },
            new boolean[] { true, true, true, false, false, false, false, true },
            new boolean[] { true, true, false, false, false, false, false, true },
    };
    public static final boolean[][] FANCY = new boolean[][] {
            new boolean[] { true, true, false, false, false, false, false, false, false, false, true, true },
            new boolean[] { true, true, true, false, false, false, false, false, false, true, true, true },
            new boolean[] { true, true, true, true, false, false, false, false, true, true, true, false },
            new boolean[] { true, true, true, true, true, false, false, true, true, true, false, false },
            new boolean[] { true, true, false, true, false, true, true, false, true, false, false, false },
            new boolean[] { true, true, false, false, true, false, true, true, false, false, false, false },
            new boolean[] { true, true, false, false, true, true, false, true, false, false, false, false },
            new boolean[] { true, true, false, true, false, true, true, false, true, false, false, false },
            new boolean[] { true, true, true, true, true, false, false, true, true, true, false, false },
            new boolean[] { true, true, true, true, false, false, false, false, true, true, true, false },
            new boolean[] { true, true, true, false, false, false, false, false, false, true, true, true },
            new boolean[] { true, true, false, false, false, false, false, false, false, false, true, true },
    };

}
