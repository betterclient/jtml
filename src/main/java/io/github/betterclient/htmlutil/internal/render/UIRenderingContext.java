package io.github.betterclient.htmlutil.internal.render;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.betterclient.htmlutil.api.KeyboardKey;
import io.github.betterclient.htmlutil.internal.css.styles.TextDecoration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

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

    @SuppressWarnings("all")
    public void fillRound(float x, float y, float endX, float endY, int color, float radius) {
        Tessellator tessellator = Tessellator.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder builder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        float store = x;
        x = Math.min(x, endX);
        endX = Math.max(store, endX);
        store = y;
        y = Math.min(y, endY);
        endY = Math.max(store, endY);
        radius/=2;

        int n2;
        for (n2 = 0; n2 <= 90; n2 += 3) {
            builder.vertex(
                    (float) (x + radius + Math.sin(
                            n2 * Math.PI / 180
                    ) * (radius * -1)),
                    (float) (y + radius + Math.cos(
                            n2 * Math.PI / 180
                    ) * (radius * -1)), 0)

                    .color(color);
        }
        for (n2 = 90; n2 <= 180; n2 += 3) {
            builder.vertex(
                    (float) (x + radius + Math.sin(
                            n2 * Math.PI / 180
                    ) * (radius * -1)),
                    (float) (endY - radius + Math.cos(
                            n2 * Math.PI / 180
                    ) * (radius * -1)), 0)

                    .color(color);
        }
        for (n2 = 0; n2 <= 90; n2 += 3) {
            builder.vertex(
                    (float) (endX - radius + Math.sin(
                            n2 * Math.PI / 180
                    ) * radius),
                    (float) (endY - radius + Math.cos(
                            n2 * Math.PI / 180
                    ) * radius),
                    0)

                    .color(color);
        }
        for (n2 = 90; n2 <= 180; n2 += 3) {
            builder.vertex(
                    (float) (endX - radius + Math.sin(
                            n2 * Math.PI / 180
                    ) * radius),
                    (float) (y + radius + Math.cos(
                            n2 * Math.PI / 180
                    ) * radius), 0)

                    .color(color);
        }

        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.disableBlend();
    }

    public KeyboardKey getKey(int code) {
        return switch (code) {
            case GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT -> KeyboardKey.SHIFT;
            case GLFW.GLFW_KEY_BACKSPACE -> KeyboardKey.BACKSPACE;
            case GLFW.GLFW_KEY_ENTER -> KeyboardKey.ENTER;
            case GLFW.GLFW_KEY_LEFT_CONTROL -> KeyboardKey.CONTROL;
            case GLFW.GLFW_KEY_UP -> KeyboardKey.ARROW_UP;
            case GLFW.GLFW_KEY_DOWN -> KeyboardKey.ARROW_DOWN;
            case GLFW.GLFW_KEY_RIGHT -> KeyboardKey.ARROW_RIGHT;
            case GLFW.GLFW_KEY_LEFT -> KeyboardKey.ARROW_LEFT;
            default -> null; //Unsupported keys should be ignored, please.
        };
    }

    public int scrWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public int scrHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    public String getClipboard() {
        return MinecraftClient.getInstance().keyboard.getClipboard();
    }
}