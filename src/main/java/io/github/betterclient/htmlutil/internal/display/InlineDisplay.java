package io.github.betterclient.htmlutil.internal.display;

import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Node;

public class InlineDisplay extends DisplayMode {
    static final DisplayMode INSTANCE = new InlineDisplay();

    public InlineDisplay() {
        super("inline");
    }

    @Override
    public void loadPositions(HTMLNode<?> node, ElementRenderingContext context) {
        node.y = 44;
        node.x = 44;

        int x = 0;
        int y = 0;
        for (HTMLNode<? extends Node> child : node.children) {
            child.x = x;
            child.y = y;

            x += child.getDimensions(context).width;
            if (child.display$moveDown) {
                y += child.getDimensions(context).height;
                x = 0;
            }
        }
    }
}