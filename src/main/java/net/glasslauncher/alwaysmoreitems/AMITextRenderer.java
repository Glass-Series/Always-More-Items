package net.glasslauncher.alwaysmoreitems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.CharacterUtils;
import net.modificationstation.stationapi.api.util.Formatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.*;

public class AMITextRenderer extends TextRenderer {
    public static final String OBFUSCATED = Formatting.FORMATTING_CODE_PREFIX + "k";
    public static final String BOLD = Formatting.FORMATTING_CODE_PREFIX + "l";
    public static final String STRIKETHROUGH = Formatting.FORMATTING_CODE_PREFIX + "m";
    public static final String UNDERLINE = Formatting.FORMATTING_CODE_PREFIX + "n";
    public static final String ITALICS = Formatting.FORMATTING_CODE_PREFIX + "o";
    public static final String RESET = Formatting.FORMATTING_CODE_PREFIX + "r";

    public static final String RANDOM_CHARS_PALLETTE = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";
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

    protected float renderChar(int posX, int posY, int character, boolean shadow) {
        int i = character % 16 * 8;
        int j = character / 16 * 8;
        int k = shadow ? 1 : 0;
        int l = charWidth(character);
        float f = (float)l - 0.01F;
        GL11.glBegin(5);
        GL11.glTexCoord2f((float)i / 128.0F, (float)j / 128.0F);
        GL11.glVertex3f(posX + (float)k, posY, 0.0F);
        GL11.glTexCoord2f((float)i / 128.0F, ((float)j + 7.99F) / 128.0F);
        GL11.glVertex3f(posX - (float)k, posY + 7.99F, 0.0F);
        GL11.glTexCoord2f(((float)i + f - 1.0F) / 128.0F, (float)j / 128.0F);
        GL11.glVertex3f(posX + f - 1.0F + (float)k, posY, 0.0F);
        GL11.glTexCoord2f(((float)i + f - 1.0F) / 128.0F, ((float)j + 7.99F) / 128.0F);
        GL11.glVertex3f(posX + f - 1.0F - (float)k, posY + 7.99F, 0.0F);
        GL11.glEnd();
        return (float)l;
    }

    public int charWidth(int character) {
        int var4 = CharacterUtils.VALID_CHARACTERS.indexOf(character);
        if (var4 >= 0) {
            return field_2462[var4 + 32];
        }
        return 0;
    }

    public void renderStringAtPos(String text, int posX, int posY, Color color, boolean shadow) {
        boolean randomStyle = false;
        boolean boldStyle = false;
        boolean italicStyle = false;
        boolean underlineStyle = false;
        boolean strikethroughStyle = false;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, field_2461);
        setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        for(int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int i1;
            int j1;
            if (c0 == 167 && i + 1 < text.length()) {
                i1 = VALID_COLOR_CHARS.indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i1 < 16) {
                    randomStyle = false;
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;
                    if (i1 < 0) {
                        i1 = 15;
                    }

                    if (shadow) {
                        i1 += 16;
                    }

                    j1 = COLOR_CODES[i1];
                    setColor((float)(j1 >> 16), (float)(j1 >> 8 & 255), (float)(j1 & 255), color.getAlpha());
                } else if (i1 == 16) {
                    randomStyle = true;
                } else if (i1 == 17) {
                    boldStyle = true;
                } else if (i1 == 18) {
                    strikethroughStyle = true;
                } else if (i1 == 19) {
                    underlineStyle = true;
                } else if (i1 == 20) {
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
                i1 = RANDOM_CHARS_PALLETTE.indexOf(c0);
                if (randomStyle && i1 != -1) {
                    j1 = charWidth(c0);

                    char c1;
                    do {
                        i1 = fontRandom.nextInt(RANDOM_CHARS_PALLETTE.length());
                        c1 = RANDOM_CHARS_PALLETTE.charAt(i1);
                    } while(j1 != charWidth(c1));

                    c0 = c1;
                }

                int charScale = 1;
                boolean flag = (c0 == 0 || i1 == -1) && shadow;
                if (flag) {
                    posX -= charScale;
                    posY -= charScale;
                }

                float f = validateAndRenderChar(posX, posY, c0, italicStyle);
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

                    validateAndRenderChar(posX, posY, c0, italicStyle);
                    posX -= charScale;
                    if (flag) {
                        posX += charScale;
                        posY += charScale;
                    }

                    ++f;
                }

                doDecorations(posX, posY, f, underlineStyle, strikethroughStyle);
                posX += (int) f;
            }
        }

    }

    protected void setColor(float red, float green, float blue, float alpha) {
        GL11.glColor4f(red / 255, green / 255, blue / 255, alpha / 255);
    }

    private float validateAndRenderChar(int posX, int posY, char charToRender, boolean shadow) {
        if (charToRender == ' ') {
            return 4.0F;
        } else {
            int charIndex = RANDOM_CHARS_PALLETTE.indexOf(charToRender);
            return renderChar(posX, posY, charIndex, shadow);
        }
    }

    protected void doDecorations(int posX, int posY, float charWidth, boolean strikethroughStyle, boolean underlineStyle) {
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


    public List<String> listFormattedStringToWidth(String p_listFormattedStringToWidth_1_, int p_listFormattedStringToWidth_2_) {
        return Arrays.asList(this.wrapFormattedStringToWidth(p_listFormattedStringToWidth_1_, p_listFormattedStringToWidth_2_).split("\n"));
    }

    String wrapFormattedStringToWidth(String p_wrapFormattedStringToWidth_1_, int p_wrapFormattedStringToWidth_2_) {
        int i = this.sizeStringToWidth(p_wrapFormattedStringToWidth_1_, p_wrapFormattedStringToWidth_2_);
        if (p_wrapFormattedStringToWidth_1_.length() <= i) {
            return p_wrapFormattedStringToWidth_1_;
        } else {
            String s = p_wrapFormattedStringToWidth_1_.substring(0, i);
            char c0 = p_wrapFormattedStringToWidth_1_.charAt(i);
            boolean flag = c0 == ' ' || c0 == '\n';
            String s1 = getFormatFromString(s) + p_wrapFormattedStringToWidth_1_.substring(i + (flag ? 1 : 0));
            return s + "\n" + this.wrapFormattedStringToWidth(s1, p_wrapFormattedStringToWidth_2_);
        }
    }

    private int sizeStringToWidth(String p_sizeStringToWidth_1_, int p_sizeStringToWidth_2_) {
        int i = p_sizeStringToWidth_1_.length();
        int j = 0;
        int k = 0;
        int l = -1;

        for(boolean flag = false; k < i; ++k) {
            char c0 = p_sizeStringToWidth_1_.charAt(k);
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
                        char c1 = p_sizeStringToWidth_1_.charAt(k);
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

            if (j > p_sizeStringToWidth_2_) {
                break;
            }
        }

        return k != i && l != -1 && l < k ? l : k;
    }

    public static String getFormatFromString(String p_getFormatFromString_0_) {
        StringBuilder s = new StringBuilder();
        int i = -1;
        int j = p_getFormatFromString_0_.length();

        while((i = p_getFormatFromString_0_.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = p_getFormatFromString_0_.charAt(i + 1);
                if (isFormatColor(c0)) {
                    s = new StringBuilder("§" + c0);
                } else if (isFormatSpecial(c0)) {
                    s.append("§").append(c0);
                }
            }
        }

        return s.toString();
    }

    private static boolean isFormatColor(char p_isFormatColor_0_) {
        return p_isFormatColor_0_ >= '0' && p_isFormatColor_0_ <= '9' || p_isFormatColor_0_ >= 'a' && p_isFormatColor_0_ <= 'f' || p_isFormatColor_0_ >= 'A' && p_isFormatColor_0_ <= 'F';
    }

    private static boolean isFormatSpecial(char p_isFormatSpecial_0_) {
        return p_isFormatSpecial_0_ >= 'k' && p_isFormatSpecial_0_ <= 'o' || p_isFormatSpecial_0_ >= 'K' && p_isFormatSpecial_0_ <= 'O' || p_isFormatSpecial_0_ == 'r' || p_isFormatSpecial_0_ == 'R';
    }
}
