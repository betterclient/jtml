package io.github.betterclient.jtml.internal.display;

import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Node;

public class InlineDisplay extends DisplayMode {
    static final DisplayMode INSTANCE = new InlineDisplay();

    public InlineDisplay() {
        super("inline");
    }

    @Override
    public void loadPositions(HTMLNode<?> node, ElementRenderingContext context) {
        node.x = 44;
        node.y = 44;
        int x = 0;
        int y = 0;
        int maxY = 0;
        ElementDimensions parentDim = node.getDimensions(context);
        for (HTMLNode<? extends Node> child : node.children) {
            ElementDimensions dimensions = child.getDimensions(context);
            child.x = x;
            child.y = y;

            maxY = Math.max(maxY, dimensions.height);

            x += dimensions.width;
            if (child.display$moveDown && dimensions.height > 0) {
                y += maxY;
                maxY = 0;
                x = 0;
            }

            if (parentDim.width < x) {
                x = 0;
                y += maxY;
                maxY = 0;
            }
        }
    }
}