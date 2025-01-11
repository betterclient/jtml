package io.github.betterclient.htmlutil.internal.render;

import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.css.CSSStyle;
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
        StyleParser parser = new StyleParser(child.style);
        ElementDimensions box = child.getDimensions(this);

        context.context.fill(child.getX(), child.getY(), child.getX() + box.width, child.getY() + box.height, parser.getBackgroundColor());

        //this current function - drawBackground, is called before rendering an element
        //Which 100% guarantees child is going to be rendered right after this call
        //so just set it here and remove the call requirements
        //this will also make things simpler -betterclient
        //This note just exists, so you don't touch this file, this is hell please save me.
        this.currentlyRendered = child;
    }

    public void renderText(String text) {
        StyleParser parser = new StyleParser(this.currentlyRendered.style);
        //TODO: add sizing the text

        int width = this.context.renderText(text, x, y, parser.getColor()) + parser.getWidthOffset();
        this.x += width;
    }

    public ElementDimensions asDimensions(CSSStyle style, String text) {
        StyleParser parser = new StyleParser(style);
        String s = style.calculate("font-size");
        s = s.replace("px", "");
        float i = Float.parseFloat(s);

        return new ElementDimensions(
                (int) (i / 9) * context.width(text) + parser.getWidthOffset(),
                ((int) i)
        );
    }

    public int screenWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public int screenHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }
}