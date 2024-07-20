package net.glasslauncher.alwaysmoreitems.util;

public class HoverChecker {

    private final int top;
    private final int bottom;
    private final int left;
    private final int right;

    public HoverChecker(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public boolean isOver(int mouseX, int mouseY) {
        return mouseY >= this.top && mouseY <= this.bottom && mouseX >= this.left && mouseX <= this.right;
    }

}
