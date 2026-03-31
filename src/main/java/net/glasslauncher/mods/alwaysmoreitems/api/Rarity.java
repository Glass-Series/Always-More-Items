package net.glasslauncher.mods.alwaysmoreitems.api;

import net.glasslauncher.mods.alwaysmoreitems.gui.AMIRarityIcons;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Rarity {
    public static final Map<Character, Rarity> AMI_RARITIES_BY_CODE = new HashMap<>();

    public static final Rarity VANILLA = new Rarity("Vanilla", 'z', 0, new Color(255, 255, 255), new Color(0, 0, 0, 192), new Color(0, 0, 0, 192), HeaderCode.NONE);
    public static final Rarity BAD = new Rarity("Bad", 'a', -1, new Color(AMITextRenderer.getColorFromCode('7')), HeaderCode.BAD);
    public static final Rarity NONE = new Rarity("None", 'b', 0, new Color(AMITextRenderer.getColorFromCode('f')), HeaderCode.NONE);
    public static final Rarity COMMON = new Rarity("Common", 'c', 1, new Color(AMITextRenderer.getColorFromCode('a')), HeaderCode.NONE);
    public static final Rarity UNCOMMON = new Rarity("Uncommon", 'd', 2, new Color(AMITextRenderer.getColorFromCode('b')), HeaderCode.NORMAL);
    public static final Rarity RARE = new Rarity("Rare", 'e', 3, new Color(AMITextRenderer.getColorFromCode('d')), HeaderCode.NORMAL);
    public static final Rarity UNIQUE = new Rarity("Unique", 'f', 4, new Color(AMITextRenderer.getColorFromCode('e')), HeaderCode.FANCY);
    public static final Rarity LEGENDARY = new Rarity("Legendary", 'g',  5, new Color(43, 194, 154), new Color(150, 39, 25), new Color(43, 194, 154).darker(), HeaderCode.FANCY);
    public static final Rarity ARTEFACT = new Rarity("Artefact", 'h', 6, new Color(208, 75, 18), new Color(AMITextRenderer.getColorFromCode('6')), Color.GRAY, HeaderCode.ARTEFACT);

    public final String name;
    public final Integer value;
    public final Color textColor;
    public final Color backgroundColor;
    public final Color iconColor;
    public final char injectionCode;
    public final HeaderCode headerCode;
    public final String code;

    public Rarity(String name, char injectionCode, Integer value, Color color, HeaderCode headerCode) {
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
        AMI_RARITIES_BY_CODE.put(injectionCode, this);
    }

    public Rarity(String name, char injectionCode, Integer value, Color textColor, Color backgroundColor, Color iconColor, HeaderCode headerCode) {
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
        code = String.valueOf(HeaderCode.PREFIX_CHARACTER) + injectionCode;
        AMI_RARITIES_BY_CODE.put(injectionCode, this);
    }

    @Override
    public String toString() {
        return code;
    }

    public static class HeaderCode {
        public static HeaderCode BAD = new HeaderCode("Bad", '1', -1, AMIRarityIcons.BAD, false);
        public static HeaderCode NONE = new HeaderCode("None", '2', 0, AMIRarityIcons.NONE, false);
        public static HeaderCode NORMAL = new HeaderCode("Normal", '3', 1, AMIRarityIcons.NORMAL, false);
        public static HeaderCode FANCY = new HeaderCode("Fancy", '4', 2, AMIRarityIcons.FANCY, true);
        public static HeaderCode ARTEFACT = new HeaderCode("Artefact", '5', 3, AMIRarityIcons.ARTEFACT, false);

        public static final char PREFIX_CHARACTER = '×';

        public final String name;
        public final char injectionChar;
        public final int value;
        public final boolean[][] icon;
        public final boolean edgesStretchAcross;

        public HeaderCode(String name, char injectionChar, int value, boolean[][] icon, boolean edgesStretchAcross) {
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
