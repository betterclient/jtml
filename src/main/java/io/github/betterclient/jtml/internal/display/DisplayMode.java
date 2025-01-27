package io.github.betterclient.jtml.internal.display;

import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.css.CSSStyle;
import io.github.betterclient.jtml.internal.css.styles.Margin;
import io.github.betterclient.jtml.internal.css.styles.Padding;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Node;

public abstract class DisplayMode {
    public final String cssName;

    public DisplayMode(String cssName) {
        this.cssName = cssName;
    }

    public static void applyPadding(HTMLNode<?> node) {
        ElementRenderingContext context = new ElementRenderingContext(node.document.service.getFontService());

        //apply padding
        for (HTMLNode<?> child : node.children) {
            Padding padding = Padding.getPadding(child.parser);
            ElementDimensions dimensions = child.getDimensions(context);

            child.x += (int) padding.getLeft();
            child.y -= (int) padding.getTop();
            child.width = dimensions.width + (int) padding.getRight();
            child.height = dimensions.height + (int) padding.getBottom();
        }
    }

    public static void applyMargin(HTMLNode<?> node) {
        int currentY = 0;
        int increaseWidthBy = 0;
        //apply margin
        for (HTMLNode<?> child : node.children) {
            Margin margin = Margin.getMargin(child.parser);

            child.x += (int) (margin.getLeft());
            child.y += (int) (currentY + margin.getTop());

            increaseWidthBy += (int) margin.getRight();
            currentY += (int) (margin.getBottom());
        }

        if (node.parent0 != null && increaseWidthBy > 0) {
            boolean started = false;
            for (HTMLNode<? extends Node> child : node.parent0.children) {
                if (child == node) {
                    started = true;
                    continue;
                }

                if (started) {
                    child.x += increaseWidthBy;
                }
            }
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