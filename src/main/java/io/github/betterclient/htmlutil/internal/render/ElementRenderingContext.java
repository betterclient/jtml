package io.github.betterclient.htmlutil.internal.render;

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

        context.fill(child.getX(), child.getY(), child.getX() + box.width, child.getY() + box.height, child.parser.getBackgroundColor());

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
        int width = this.context.renderText(text, x, y, parser.getColor(), parser.getSize("font-size"), TextDecoration.parse(this.currentlyRendered.style)) + parser.getWidthOffset();
        this.x += width;
    }

    public ElementDimensions asDimensions(StyleParser parser, String text) {
        float fontSize = parser.getSize("font-size");

        return new ElementDimensions(
                Math.round(fontSize / 9f) * context.width(text) + parser.getWidthOffset(),
                Math.round(fontSize) + parser.getWidthOffset()
        );
    }

    public int screenWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public int screenHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }
}