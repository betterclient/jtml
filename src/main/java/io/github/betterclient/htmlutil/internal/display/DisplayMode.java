package io.github.betterclient.htmlutil.internal.display;

import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.css.CSSStyle;
import io.github.betterclient.htmlutil.internal.css.styles.Margin;
import io.github.betterclient.htmlutil.internal.css.styles.Padding;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import io.github.betterclient.htmlutil.internal.render.UIRenderingContext;

public abstract class DisplayMode {
    public final String cssName;

    public DisplayMode(String cssName) {
        this.cssName = cssName;
    }

    public static void applyMargins(HTMLNode<?> node) {
        ElementRenderingContext context = new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT);

        for (HTMLNode<?> child : node.children) {
            Margin margin = Margin.getMargin(child.parser);
            Padding padding = Padding.getPadding(child.parser);
            ElementDimensions dimensions = child.getDimensions(context);

            child.width = dimensions.width + (int) (margin.getRight() + margin.getLeft());
            child.height = dimensions.height + (int) (margin.getBottom() + margin.getTop());

            child.x += (int) padding.getLeft();
            child.y -= (int) padding.getTop();
            child.width += (int) padding.getRight();
            child.height += (int) padding.getBottom();
        }
    }

    public abstract void loadPositions(HTMLNode<?> element, ElementRenderingContext context);

    public static DisplayMode get(CSSStyle style) {
        return switch (style.calculate("display")) {
            case "inline" -> InlineDisplay.INSTANCE;
            case "flex" -> FlexDisplay.INSTANCE;
            case "grid" -> GridDisplay.INSTANCE;
            default -> throw new UnsupportedOperationException("Unsupported display mode: " + style.calculate("display"));
        };
    }
}