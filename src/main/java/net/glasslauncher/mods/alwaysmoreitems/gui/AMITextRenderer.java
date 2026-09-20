package net.glasslauncher.mods.alwaysmoreitems.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.CharacterUtils;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AMITextRenderer extends TextRenderer {
    public static final String OBFUSCATED = Formatting.FORMATTING_CODE_PREFIX + "k";
    public static final String BOLD = Formatting.FORMATTING_CODE_PREFIX + "l";
    public static final String STRIKETHROUGH = Formatting.FORMATTING_CODE_PREFIX + "m";
    public static final String UNDERLINE = Formatting.FORMATTING_CODE_PREFIX + "n";
    public static final String ITALICS = Formatting.FORMATTING_CODE_PREFIX + "o";
    public static final String RESET = Formatting.FORMATTING_CODE_PREFIX + "r";

    public static final int FONT_HEIGHT = 9;
    public static final Random fontRandom = new Random();

    public static final String VALID_COLOR_CHARS = "0123456789abcdefklmnor";
    public static final int[] COLOR_CODES = new int[32];

    static {
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            COLOR_CODES[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public static final AMITextRenderer INSTANCE = new AMITextRenderer(Minecraft.INSTANCE.options, "/font/default.png", Minecraft.INSTANCE.textureManager);

    public AMITextRenderer(GameOptions gameOptions, String texturePath, TextureManager textureManager) {
        super(gameOptions, texturePath, textureManager);
    }

    public static int getColorFromCode(char code) {
        return COLOR_CODES[(VALID_COLOR_CHARS.indexOf(code))];
    }

    protected int renderChar(int posX, int posY, char character, boolean shadow) {
        int charIndex = CharacterUtils.VALID_CHARACTERS.indexOf(character) + 32;
        int charU = charIndex % 16 * 8;
        int charV = charIndex / 16 * 8;
        int shadowOffset = shadow ? 1 : 0;
        int width = charWidth(character);
        float preventBleedWidth = (float)width - 0.01F;
        GL11.glBegin(5);
        GL11.glTexCoord2f((float)charU / 128.0F, (float)charV / 128.0F);
        GL11.glVertex3f(posX + (float)shadowOffset, posY, 0.0F);
        GL11.glTexCoord2f((float)charU / 128.0F, ((float)charV + 7.99F) / 128.0F);
        GL11.glVertex3f(posX - (float)shadowOffset, posY + 7.99F, 0.0F);
        GL11.glTexCoord2f(((float)charU + preventBleedWidth - 1.0F) / 128.0F, (float)charV / 128.0F);
        GL11.glVertex3f(posX + preventBleedWidth - 1.0F + (float)shadowOffset, posY, 0.0F);
        GL11.glTexCoord2f(((float)charU + preventBleedWidth - 1.0F) / 128.0F, ((float)charV + 7.99F) / 128.0F);
        GL11.glVertex3f(posX + preventBleedWidth - 1.0F - (float)shadowOffset, posY + 7.99F, 0.0F);
        GL11.glEnd();
        return width;
    }

    public int charWidth(char character) {
        int var4 = CharacterUtils.VALID_CHARACTERS.indexOf(character);
        if (var4 >= 0) {
            return characterWidths[var4 + 32];
        }
        return 0;
    }

    /**
     * Use {@link #draw(String, int, int, int, boolean)} instead.
     */
    @ApiStatus.Internal
    public void renderStringAtPos(String text, int posX, int posY, Color color, boolean shadow) {
        boolean randomStyle = false;
        boolean boldStyle = false;
        boolean italicStyle = false;
        boolean underlineStyle = false;
        boolean strikethroughStyle = false;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, boundTexture);
        setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        for(int i = 0; i < text.length(); ++i) {
            char currentChar = text.charAt(i);
            int characterIndex;
            int colorInt;
            if (currentChar == '§' && i + 1 < text.length()) {
                characterIndex = VALID_COLOR_CHARS.indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (characterIndex < 16) {
                    randomStyle = false;
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;
                    if (characterIndex < 0) {
                        characterIndex = 15;
                    }

                    if (shadow) {
                        characterIndex += 16;
                    }

                    colorInt = COLOR_CODES[characterIndex];
                    setColor((float)(colorInt >> 16), (float)(colorInt >> 8 & 255), (float)(colorInt & 255), color.getAlpha());
                } else if (characterIndex == 16) {
                    randomStyle = true;
                } else if (characterIndex == 17) {
                    boldStyle = true;
                } else if (characterIndex == 18) {
                    strikethroughStyle = true;
                } else if (characterIndex == 19) {
                    underlineStyle = true;
                } else if (characterIndex == 20) {
                    italicStyle = true;
                } else {
                    randomStyle = false;
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;
                    setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                }

                ++i;
            } else {
                characterIndex = CharacterUtils.VALID_CHARACTERS.indexOf(currentChar);
                if (randomStyle && characterIndex != -1) {
                    colorInt = charWidth(currentChar);

                    char randomChar;
                    do {
                        characterIndex = fontRandom.nextInt(CharacterUtils.VALID_CHARACTERS.length());
                        randomChar = CharacterUtils.VALID_CHARACTERS.charAt(characterIndex);
                    } while(colorInt != charWidth(randomChar));

                    currentChar = randomChar;
                }

                int charScale = 1;
                boolean flag = (currentChar == 0 || characterIndex == -1) && shadow;
                if (flag) {
                    posX -= charScale;
                    posY -= charScale;
                }

                int charWidth = renderCharRespectSpaces(posX, posY, currentChar, italicStyle);
                if (flag) {
                    posX += charScale;
                    posY += charScale;
                }

                if (boldStyle) {
                    posX += charScale;
                    if (flag) {
                        posX -= charScale;
                        posY -= charScale;
                    }

                    renderCharRespectSpaces(posX, posY, currentChar, italicStyle);
                    posX -= charScale;
                    if (flag) {
                        posX += charScale;
                        posY += charScale;
                    }

                    ++charWidth;
                }

                doDecorations(posX, posY, charWidth, strikethroughStyle, underlineStyle);
                posX += charWidth;
            }
        }

    }

    protected void setColor(float red, float green, float blue, float alpha) {
        GL11.glColor4f(red / 255, green / 255, blue / 255, alpha / 255);
    }

    private int renderCharRespectSpaces(int posX, int posY, char charToRender, boolean shadow) {
        if (charToRender == ' ') {
            return 4;
        } else {
            return renderChar(posX, posY, charToRender, shadow);
        }
    }

    protected void doDecorations(int posX, int posY, int charWidth, boolean strikethroughStyle, boolean underlineStyle) {
        Tessellator tessellator1 = null;
        if (strikethroughStyle) {
            tessellator1 = Tessellator.INSTANCE;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator1.startQuads();
            tessellator1.vertex(posX, posY + (float)(FONT_HEIGHT / 2), 0.0);
            tessellator1.vertex(posX + charWidth, posY + (float)(FONT_HEIGHT / 2), 0.0);
            tessellator1.vertex(posX + charWidth, posY + (float)(FONT_HEIGHT / 2) - 1.0F, 0.0);
            tessellator1.vertex(posX, posY + (float)(FONT_HEIGHT / 2) - 1.0F, 0.0);
            tessellator1.draw();
            if(!underlineStyle) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
        }

        if (underlineStyle) {
            if(!strikethroughStyle) {
                tessellator1 = Tessellator.INSTANCE;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            tessellator1.startQuads();
            int underlineOffset = -1;
            tessellator1.vertex(posX + (float)underlineOffset, posY + (float)FONT_HEIGHT, 0.0);
            tessellator1.vertex(posX + charWidth, posY + (float)FONT_HEIGHT, 0.0);
            tessellator1.vertex(posX + charWidth, posY + (float)FONT_HEIGHT - 1.0F, 0.0);
            tessellator1.vertex(posX + (float)underlineOffset, posY + (float)FONT_HEIGHT - 1.0F, 0.0);
            tessellator1.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }


    public List<String> listFormattedStringToWidth(String text, int width) {
        return Arrays.asList(this.wrapFormattedStringToWidth(text, width).split("\n"));
    }

    String wrapFormattedStringToWidth(String text, int width) {
        int i = this.cropStringToWidth(text, width);
        if (text.length() <= i) {
            return text;
        } else {
            String s = text.substring(0, i);
            char c0 = text.charAt(i);
            boolean flag = c0 == ' ' || c0 == '\n';
            String s1 = getFormatFromString(s) + text.substring(i + (flag ? 1 : 0));
            return s + "\n" + this.wrapFormattedStringToWidth(s1, width);
        }
    }

    private int cropStringToWidth(String text, int width) {
        int i = text.length();
        int j = 0;
        int k = 0;
        int l = -1;

        for(boolean flag = false; k < i; ++k) {
            char c0 = text.charAt(k);
            switch (c0) {
                case '\n':
                    --k;
                    break;
                case ' ':
                    l = k;
                default:
                    j += this.charWidth(c0);
                    if (flag) {
                        ++j;
                    }
                    break;
                case '§':
                    if (k < i - 1) {
                        ++k;
                        char c1 = text.charAt(k);
                        if (c1 != 'l' && c1 != 'L') {
                            if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
                                flag = false;
                            }
                        } else {
                            flag = true;
                        }
                    }
            }

            if (c0 == '\n') {
                ++k;
                l = k;
                break;
            }

            if (j > width) {
                break;
            }
        }

        return k != i && l != -1 && l < k ? l : k;
    }

    public static String getFormatFromString(String text) {
        StringBuilder s = new StringBuilder();
        int i = -1;
        int j = text.length();

        while((i = text.indexOf('§', i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = text.charAt(i + 1);
                if (isFormatColor(c0)) {
                    s = new StringBuilder("§" + c0);
                } else if (isFormatSpecial(c0)) {
                    s.append("§").append(c0);
                }
            }
        }

        return s.toString();
    }

    private static boolean isFormatColor(char chr) {
        return chr >= '0' && chr <= '9' || chr >= 'a' && chr <= 'f' || chr >= 'A' && chr <= 'F';
    }

    private static boolean isFormatSpecial(char chr) {
        return chr >= 'k' && chr <= 'o' || chr >= 'K' && chr <= 'O' || chr == 'r' || chr == 'R';
    }
}
