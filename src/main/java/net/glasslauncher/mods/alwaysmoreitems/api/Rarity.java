package net.glasslauncher.mods.alwaysmoreitems.api;

import net.glasslauncher.mods.alwaysmoreitems.gui.AMIRarityIcons;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;

import java.awt.*;
import java.util.*;

public enum Rarity {
    BAD("Bad", 'a', -1, new Color(AMITextRenderer.getColorFromCode('7')), HeaderCode.BAD),
    NONE("None", 'b', 0, new Color(AMITextRenderer.getColorFromCode('f')), HeaderCode.NONE),
    COMMON("Common", 'c', 1, new Color(AMITextRenderer.getColorFromCode('a')), HeaderCode.NONE),
    UNCOMMON("Uncommon", 'd', 2, new Color(AMITextRenderer.getColorFromCode('b')), HeaderCode.NORMAL),
    RARE("Rare", 'e', 3, new Color(AMITextRenderer.getColorFromCode('d')), HeaderCode.NORMAL),
    UNIQUE("Unique", 'f', 4, new Color(AMITextRenderer.getColorFromCode('e')), HeaderCode.FANCY),
    LEGENDARY("Legendary", 'g',  5, new Color(43, 194, 154), new Color(150, 39, 25), new Color(43, 194, 154).darker(), HeaderCode.FANCY),
    ARTEFACT("Artefact", 'h', 6, new Color(208, 75, 18), new Color(AMITextRenderer.getColorFromCode('6')), Color.GRAY, HeaderCode.ARTEFACT),
    ;

    public static final Map<Character, Rarity> AMI_RARITIES_BY_CODE = new HashMap<>() {{
        Arrays.stream(Rarity.values()).forEach(rarity -> put(rarity.injectionCode, rarity));
    }};

    public final String name;
    public final Integer value;
    public final Color textColor;
    public final Color backgroundColor;
    public final Color iconColor;
    public final char injectionCode;
    public final HeaderCode headerCode;
    public final String code;

    Rarity(String name, char injectionCode, Integer value, Color color, HeaderCode headerCode) {
        this.name = name;
        this.value = value;
        this.textColor = color;

        int intColor = color.darker().darker().getRGB();
        float red = (float)(intColor >> 16 & 255) / 255f;
        float green = (float)(intColor >> 8 & 255) / 255f;
        float blue = (float)(intColor & 255) / 255f;

        this.backgroundColor = new Color(red, green, blue, 192 / 255f);
        this.iconColor = color.darker();
        this.injectionCode = injectionCode;
        this.headerCode = headerCode;
        code = String.valueOf(HeaderCode.PREFIX_CHARACTER) + injectionCode;
    }

    Rarity(String name, char injectionCode, Integer value, Color textColor, Color backgroundColor, Color iconColor, HeaderCode headerCode) {
        this.name = name;
        this.value = value;
        this.textColor = textColor;

        int intColor = backgroundColor.getRGB();
        float red = (float)(intColor >> 16 & 255) / 255f;
        float green = (float)(intColor >> 8 & 255) / 255f;
        float blue = (float)(intColor & 255) / 255f;

        this.backgroundColor = new Color(red, green, blue, 192 / 255f);
        this.iconColor = iconColor;
        this.injectionCode = injectionCode;
        this.headerCode = headerCode;
        code = String.valueOf(HeaderCode.PREFIX_CHARACTER) +
                injectionCode;
    }

    @Override
    public String toString() {
        return code;
    }

    public enum HeaderCode {
        BAD("Bad", '1', -1, AMIRarityIcons.BAD, false),
        NONE("None", '2', 0, AMIRarityIcons.NONE, false),
        NORMAL("Normal", '3', 1, AMIRarityIcons.NORMAL, false),
        FANCY("Fancy", '4', 2, AMIRarityIcons.FANCY, true),
        ARTEFACT("Artefact", '5', 3, AMIRarityIcons.ARTEFACT, false),
        ;

        public static final char PREFIX_CHARACTER = '×';

        public final String name;
        public final char injectionChar;
        public final int value;
        public final boolean[][] icon;
        public final boolean edgesStretchAcross;

        HeaderCode(String name, char injectionChar, int value, boolean[][] icon, boolean edgesStretchAcross) {
            this.name = name;
            this.injectionChar = injectionChar;
            this.value = value;
            this.icon = icon;
            this.edgesStretchAcross = edgesStretchAcross;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
