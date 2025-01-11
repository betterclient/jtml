package io.github.betterclient.htmlutil.internal.display;

import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Node;

public class InlineDisplay extends DisplayMode {
    public InlineDisplay() {
        super("inline");
    }

    @Override
    public void loadPositions(HTMLNode<?> element, ElementRenderingContext context) {
        if(element.parent0 == null) {
            element.y = 44;
            element.x = 44;
            return;
        }
        element.y = 0;

        int x = 0;
        for (HTMLNode<? extends Node> child : element.parent0.children) {
            if (child == element) break;
            x += child.getDimensions(context).width;

            if (child.display$moveDown) {
                //Special case for "br" elements
                element.y += child.getDimensions(context).height;
                x = 0;
            }
        }

        element.x = x;
    }
}