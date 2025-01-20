package io.github.betterclient.htmlutil.internal.render;

import io.github.betterclient.htmlutil.api.KeyboardKey;
import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.css.styles.Border;
import io.github.betterclient.htmlutil.internal.css.styles.TextDecoration;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.css.StyleParser;
import net.minecraft.client.MinecraftClient;

public class ElementRenderingContext {
    public int x, y;
    public final UIRenderingContext context;
    public HTMLNode<?> currentlyRendered;

    public ElementRenderingContext(UIRenderingContext context) {
        this.context = context;
    }

    public void drawBackground(HTMLNode<?> child) {
        ElementDimensions box = child.getDimensions(this);
        float radius = child.parser.getSize("border-radius");

        context.fillRound(child.getX(), child.getY(), child.getX() + box.width, child.getY() + box.height, child.parser.getBackgroundColor(), radius);

        Border border = Border.parseBorderElement(child.style);
        border.render(context, child.getX(), child.getY(), box.width, box.height);

        //this current function - drawBackground, is called before rendering an element
        //Which 100% guarantees child is going to be rendered right after this call
        //so just set it here and remove the call requirements
        //this will also make things simpler -betterclient
        //This note just exists, so you don't touch this file, this is hell please save me.
        this.currentlyRendered = child;
    }

    public void renderText(String text) {
        StyleParser parser = this.currentlyRendered.parser;
        this.context.renderText(text, x, y, parser.getColor(), parser.getSize("font-size"), TextDecoration.parse(this.currentlyRendered.style));
    }

    public int renderText(String text, int goDown) {
        StyleParser parser = this.currentlyRendered.parser;
        float size = parser.getSize("font-size");
        int color = parser.getColor();
        TextDecoration decoration = TextDecoration.parse(this.currentlyRendered.style);

        int y = this.y;
        int maxW = 0;
        for (String s : text.split("\n")) {
            maxW = Math.max(maxW, this.context.renderText(s, x + goDown, y + goDown, color, size, decoration) + parser.getWidthOffset());
            y += (int) size;
        }
        return maxW;
    }

    public int width(String text) {
        return (int) (context.width(text) * (this.currentlyRendered.parser.getSize("font-size") / 9f));
    }

    public ElementDimensions asDimensions(StyleParser parser, String text) {
        float fontSize = parser.getSize("font-size");

        return new ElementDimensions(
                Math.round(fontSize / 9f) * context.width(text) + parser.getWidthOffset(),
                Math.round(fontSize) + parser.getWidthOffset()
        );
    }

    public int screenWidth() {
        return context.scrWidth();
    }

    public int screenHeight() {
        return context.scrHeight();
    }

    public KeyboardKey getKey(int code) {
        try {
            return context.getKey(code);
        } catch (Exception e) {
            System.err.println("UIRenderingContext.getKey threw an exception, please return null for unsupported keys.");
            throw new IllegalStateException(e);
        }
    }

    public void fillVerticalLine(int width) {
        int ySize = (int) currentlyRendered.parser.getSize("font-size");
        context.fill(x + width, y, x + width + 2, y + ySize, -1);
    }

    public void fillVerticalLine(int x, int y) {
        int ySize = (int) currentlyRendered.parser.getSize("font-size");
        context.fill(this.x + x, this.y + y - ySize, this.x + x + 2, this.y + y, -1);
    }
}