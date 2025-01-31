package io.github.betterclient.jtml.internal.render;

import io.github.betterclient.jtml.api.event.KeyboardKey;
import io.github.betterclient.jtml.internal.css.styles.BorderRadius;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.css.styles.Border;
import io.github.betterclient.jtml.internal.css.styles.TextDecoration;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.css.StyleParser;
import io.github.betterclient.jtml.service.RenderingService;

public class ElementRenderingContext {
    public int x, y;
    public final RenderingService context;
    public HTMLNode<?> currentlyRendered;

    public ElementRenderingContext(RenderingService context) {
        this.context = context;
    }

    public ElementDimensions drawBackground(ElementDimensions dimensions0, HTMLNode<?> child) {
        //this current function - drawBackground, is called before rendering an element
        //Which 100% guarantees child is going to be rendered right after this call
        //so just set it here and remove the call requirements
        //this will also make things simpler -betterclient
        //This note just exists, so you don't touch this file, this is hell please save me.
        this.currentlyRendered = child;

        if (child == child.document) {
            context.fill(0, 0, context.scrWidth(), context.scrHeight(), child.parser.getBackgroundColor());

            Border border = Border.parseBorderElement(child.style);
            border.render(this.currentlyRendered.document, context, 0, 0, context.scrWidth(), context.scrHeight());

            return new ElementDimensions(context.scrWidth(), context.scrHeight());
        }

        ElementDimensions box = child.getDimensions(this);
        BorderRadius radius = BorderRadius.getBorderRadius(child.parser);

        context.fillRound(child.getX(), child.getY(), child.getX() + box.width, child.getY() + box.height, child.parser.getBackgroundColor(), radius.getTopLeft(), radius.getTopRight(), radius.getBottomLeft(), radius.getBottomRight());

        assert child.parent0 != null; //Child won't be null since it's not the document
        context.startScissor(child.parent0.getX(), child.parent0.getY(), child.parent0.getX() + dimensions0.width, child.parent0.getY() + dimensions0.height + 20);

        Border border = Border.parseBorderElement(child.style);
        border.render(this.currentlyRendered.document, context, child.getX(), child.getY(), box.width, box.height);

        return box;
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
        TextDecoration parse = TextDecoration.parse(this.currentlyRendered.style);
        return (int) (context.width(text, parse) * (this.currentlyRendered.parser.getSize("font-size") / 9f));
    }

    public ElementDimensions asDimensions(StyleParser parser, String text) {
        float fontSize = parser.getSize("font-size");
        TextDecoration parse = TextDecoration.parse(parser.style());

        float width = ((fontSize / 9f) * context.width(text, parse) + parser.getWidthOffset());
        float height = fontSize + parser.getWidthOffset();

        return new ElementDimensions(
                Math.round(width),
                Math.round(height)
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
            System.err.println("RenderingService.getKey threw an exception, please return null for unsupported keys.");
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

    public void fill(int x, int y, int width, int height, int color) {
        context.fill(
                this.x + x,
                this.y + y,
                this.x + x + width,
                this.y + y + height,
                color
        );
    }
}