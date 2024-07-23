package net.glasslauncher.alwaysmoreitems.api;

import net.glasslauncher.alwaysmoreitems.AMIRarityIcons;
import net.glasslauncher.alwaysmoreitems.AMITextRenderer;
import net.minecraft.client.font.TextRenderer;
import net.modificationstation.stationapi.api.util.Formatting;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.*;

public enum AMIRarity {
    BAD("Bad", 'a', -1, new Color(AMITextRenderer.getColorFromCode('7')), HeaderCode.BAD),
    NONE("None", 'b', 0, new Color(AMITextRenderer.getColorFromCode('f')), HeaderCode.NONE),
    COMMON("Common", 'c', 1, new Color(AMITextRenderer.getColorFromCode('a')), HeaderCode.NONE),
    UNCOMMON("Uncommon", 'd', 2, new Color(AMITextRenderer.getColorFromCode('b')), HeaderCode.NORMAL),
    RARE("Rare", 'd', 3, new Color(AMITextRenderer.getColorFromCode('d')), HeaderCode.NORMAL),
    UNIQUE("Unique", 'e', 4, new Color(AMITextRenderer.getColorFromCode('e')), HeaderCode.FANCY),
    LEGENDARY("Legendary", 'f',  5, new Color(AMITextRenderer.getColorFromCode('5')), HeaderCode.FANCY),
    ARTEFACT("Artefact", 'g', 6, new Color(AMITextRenderer.getColorFromCode('6')), HeaderCode.ARTEFACT),
    ;

    public static final Map<Character, AMIRarity> AMI_RARITIES_BY_CODE = new HashMap<>() {{
        Arrays.stream(AMIRarity.values()).forEach(rarity -> put(rarity.injectionCode, rarity));
    }};

    public final String name;
    public final Integer value;
    public final Color textColor;
    public final Color backgroundColor;
    public final Color iconColor;
    public final char injectionCode;
    public final HeaderCode headerCode;
    public final String code;

    AMIRarity(String name, char injectionCode, Integer value, Color color, HeaderCode headerCode) {
        this.name = name;
        this.value = value;
        this.textColor = color;
        this.backgroundColor = color.darker().darker();
        this.iconColor = color.darker();
        this.injectionCode = injectionCode;
        this.headerCode = headerCode;
        code = String.valueOf(HeaderCode.PREFIX_CHARACTER) + injectionCode;
    }

    AMIRarity(String name, char injectionCode, Integer value, Color textColor, Color backgroundColor, Color iconColor, HeaderCode headerCode) {
        this.name = name;
        this.value = value;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
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
        BAD("Bad", '1', -1, AMIRarityIcons.BRACE_TEMPLATE),
        NONE("None", '2', 0, AMIRarityIcons.BRACE_TEMPLATE),
        NORMAL("Normal", '3', 1, AMIRarityIcons.BRACE_TEMPLATE),
        FANCY("Fancy", '4', 2, AMIRarityIcons.BRACE_TEMPLATE),
        ARTEFACT("Artefact", '5', 3, AMIRarityIcons.BRACE_TEMPLATE),
        ;

        public static final char PREFIX_CHARACTER = '×';

        public final String name;
        public final char injectionChar;
        public final int value;
        public final boolean[][] icon;

        HeaderCode(String name, char injectionChar, int value, boolean[][] icon) {
            this.name = name;
            this.injectionChar = injectionChar;
            this.value = value;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
