package net.glasslauncher.mods.alwaysmoreitems;

import net.glasslauncher.mods.alwaysmoreitems.api.Rarity;
import net.minecraft.client.Minecraft;
import uk.co.benjiweber.expressions.tuple.TriTuple;

import java.util.*;
import java.util.stream.*;

public class AMITooltipSystem {

    /**
     * Draws a tooltip for you. Adds 9 and -15 to your mouseX and Y for you, so you don't need to offset it yourself.
     * Also handles rarities.
     * @param flipped If true, offsets the tooltip a little extra. Due to how things are handled, I can't really detect this automatically, sadly.
     */
    public static void drawTooltip(List<String> tooltip, int mouseX, int mouseY, boolean flipped) {
        if (tooltip != null) {
            tooltip.stream().mapToInt(AMITextRenderer.INSTANCE::getWidth).max().ifPresent(tooltipWidth -> {
                int tooltipX = mouseX + 9;
                int tooltipY = mouseY - 15;
                String firstTip = tooltip.get(0);
                boolean hasHeader = firstTip.startsWith(String.valueOf(Rarity.HeaderCode.PREFIX_CHARACTER));
                int borderOffset;
                if (hasHeader) {
                    Rarity rarity = Rarity.AMI_RARITIES_BY_CODE.get(firstTip.charAt(1));
                    firstTip = firstTip.substring(2);
                    tooltip.set(0, "");

                    borderOffset = drawHeader(firstTip, tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, rarity, flipped);
                } else {
                    borderOffset = 0;
                }
                int vertOffset = hasHeader ? 12 : 0;
                int horiOffset = flipped ? borderOffset * 2 : 0;
                AMIDrawContext.INSTANCE.fill(tooltipX - 3 - horiOffset, tooltipY - 3 + vertOffset, tooltipX + tooltipWidth + 3 + (borderOffset * 2) - horiOffset, tooltipY + (8 * tooltip.size()) + (3 * tooltip.size()), -1073741824);
                IntStream.range(0, tooltip.size()).forEach(currentTooltip -> Minecraft.INSTANCE.textRenderer.draw(tooltip.get(currentTooltip), tooltipX + 1 - horiOffset, tooltipY + (8 * currentTooltip) + (3 * currentTooltip) + 1, -1, true));
                IntStream.range(0, tooltip.size()).forEach(currentTooltip -> Minecraft.INSTANCE.textRenderer.draw(tooltip.get(currentTooltip), tooltipX - horiOffset, tooltipY + (8 * currentTooltip) + (3 * currentTooltip), -1, false));
            });
        }
    }

    private static int drawHeader(String name, int x1, int y1, int x2, Rarity rarity, boolean flipped) {
        boolean[][] icon = rarity.headerCode.icon;
        if (icon.length == 0) {
            AMIDrawContext.INSTANCE.fill(x1, y1, x2, y1 + 12, rarity.backgroundColor.getRGB());
            AMITextRenderer.INSTANCE.drawWithShadow(name, x1 + 3, y1 + 3, rarity.textColor.getRGB());
            return 0;
        }

        int templateWidth = icon[0].length;
        if (flipped) {
            x1 -= templateWidth * 2;
            x2 -= templateWidth * 2;
        }

        ArrayList<Integer> edgesToStretch = new ArrayList<>();

        AMIDrawContext.INSTANCE.fill(x1, y1, x2 + (templateWidth * 2), y1 + 12, rarity.backgroundColor.getRGB());
        for (int y = 0; y < icon.length; y++) {
            for (int x = 0; x < icon[y].length; x++) {
                if(icon[y][x]) {
                    AMIDrawContext.INSTANCE.fill(x1 + x, y1 + y, x1 + x + 1, y1 + y + 1, rarity.iconColor.getRGB());
                    if (rarity.headerCode.edgesStretchAcross && x == templateWidth - 1) {
                        edgesToStretch.add(y);
                    }
                }
                if(icon[y][templateWidth - 1 - x]) {
                    AMIDrawContext.INSTANCE.fill(x2 + templateWidth + x, y1 + y, x2 + templateWidth + x + 1, y1 + y + 1, rarity.iconColor.getRGB());
                }
            }
        }
        int finalX = x1;
        int finalX1 = x2;
        edgesToStretch.forEach(edge -> AMIDrawContext.INSTANCE.fill(finalX + templateWidth, y1 + edge, finalX1 + templateWidth, y1 + 1 + edge, rarity.iconColor.getRGB()));

        AMITextRenderer.INSTANCE.drawWithShadow(name, x1 + templateWidth + 3, y1 + 3, rarity.textColor.getRGB());
        return templateWidth;
    }

    /**
     * Not part of drawTooltip because there are cases where this behavior might not be desirable. Also keeps the code easier to maintain for myself.
     * @param mouseX Your mouse X position.
     * @param mouseY Your mouse Y position.
     * @param currentTooltip The tooltip you're trying to render.
     * @param width The width of your screen.
     * @param height The height of your screen.
     * @return A TriTuple containing the offsets for the tooltip itself, and also if it was flipped to the other side of the cursor. You will need to add mouseX, mouseY, and potentially backgroundWidth/Height changes for containers.
     */
    public static TriTuple<Integer, Integer, Boolean> getTooltipOffsets(int mouseX, int mouseY, List<String> currentTooltip, int width, int height) {
        int tooltipXOffset = 9;
        int tooltipYOffset = -15;
        boolean flipped = false;
        if (currentTooltip != null && !currentTooltip.isEmpty()) {
            // Take away the extra padding (3) and the header+1 (9ea + 3ea) (which is above the cursor) from the equation
            int tooltipYHitbox = (currentTooltip.size() * (AMITextRenderer.FONT_HEIGHT + 3)) - 12 - 18;
            // The extra single padding and line? Fuck knows, I'm done pretending I know what I'm doing here.
            if (mouseY > height + tooltipYOffset - tooltipYHitbox + 15) {
                // render above
                tooltipYOffset += height - (mouseY + tooltipYHitbox + 3);
            }
            else if (mouseY < -tooltipYOffset) {
                tooltipYOffset = 0;
            }
            int maxTipLength = AMITextRenderer.INSTANCE.getWidth(currentTooltip.stream().reduce((s, s2) -> s.replaceFirst(Rarity.HeaderCode.PREFIX_CHARACTER + ".", "").length() > s2.replaceFirst(Rarity.HeaderCode.PREFIX_CHARACTER + ".", "").length() ? s : s2).get());
            maxTipLength += 3;
            int iconWidth = 0;
            if (currentTooltip.get(0).startsWith(String.valueOf(Rarity.HeaderCode.PREFIX_CHARACTER))) {
                Rarity amiRarity = Rarity.AMI_RARITIES_BY_CODE.get(currentTooltip.get(0).charAt(1));
                iconWidth = amiRarity.headerCode.icon[0].length * 2;
            }
            if (mouseX + tooltipXOffset + maxTipLength + iconWidth > width) {
                tooltipXOffset = -tooltipXOffset - maxTipLength + 3;
                flipped = true;
            }
        }
        // To account for the inbult offset of drawTooltip, yes, technically wasteful, but easier to think about.
        tooltipXOffset -= 9;
        tooltipYOffset += 15;
        return TriTuple.of(tooltipXOffset, tooltipYOffset, flipped);
    }
}
