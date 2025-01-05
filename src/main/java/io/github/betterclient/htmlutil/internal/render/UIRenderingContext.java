package io.github.betterclient.htmlutil.internal.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class UIRenderingContext {
    private final TextRenderer renderer;
    public final DrawContext context;

    public UIRenderingContext(DrawContext context) {
        this.context = context;
        this.renderer = MinecraftClient.getInstance().textRenderer;
    }

    public int renderText(String text, int x, int y, int color) {
        this.context.drawText(renderer, text, x, y, color, true);
        return this.renderer.getWidth(text);
    }

    public int height() {
        return renderer.fontHeight;
    }

    public static boolean basicCollisionCheck(double mouseX, double mouseY, double x, double y, double endX, double endY) {
        double val = x;
        if(endX < x) {
            x = endX;
            endX = val;
        }

        val = y;
        if(endY < y) {
            y = endY;
            endY = val;
        }

        return mouseX >= x & mouseX <= endX & mouseY >= y & mouseY <= endY;
    }
}
