package io.github.betterclient.htmlutil.internal.display;

import io.github.betterclient.htmlutil.internal.css.CSSStyle;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;

public abstract class DisplayMode {
    public final String cssName;

    public DisplayMode(String cssName) {
        this.cssName = cssName;
    }

    public abstract void loadPositions(HTMLNode<?> element, ElementRenderingContext context);

    public static DisplayMode create(CSSStyle style) {
        return switch (style.calculate("display")) {
            case "inline" -> new InlineDisplay();
            case "flex" -> new FlexDisplay();
            default -> throw new UnsupportedOperationException("Unsupported display mode: " + style.calculate("display"));
        };
    }
}