package io.github.betterclient.htmlutil.internal.render;

import io.github.betterclient.htmlutil.internal.css.styles.TextDecoration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class UIRenderingContext {
    public static final UIRenderingContext DEFAULT_FONT = new UIRenderingContext(null);
    private final TextRenderer renderer;
    public final DrawContext context;

    public UIRenderingContext(DrawContext context) {
        this.context = context;
        this.renderer = MinecraftClient.getInstance().textRenderer;
    }

    public int renderText(String text, int x, int y, int color, float size, TextDecoration decoration) {
        MutableText t = Text.literal(text);
        if (decoration.isUnderline()) t.setStyle(Style.EMPTY.withUnderline(true));
        if (decoration.isStrikethrough()) t.setStyle(Style.EMPTY.withStrikethrough(true));

        this.context.getMatrices().push();
        this.context.getMatrices().translate(x, y, 1);
        this.context.getMatrices().scale( (size / 9f),  (size / 9f), 1f);
        this.context.getMatrices().translate(-x, -y, 1);

        this.context.drawText(renderer, t, x, y, color, true);

        this.context.getMatrices().pop();

        return (int) (this.renderer.getWidth(text) * (size / 9f));
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

    public int width(String text) {
        return this.renderer.getWidth(text);
    }

    public void fill(float x, float y, float endX, float endY, int color) {
        context.fill((int) x, (int) y, (int) endX, (int) endY, color);
    }
}