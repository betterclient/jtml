package io.github.betterclient.htmlutil.internal.nodes;

import io.github.betterclient.htmlutil.api.event.MouseClickHandler;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import io.github.betterclient.htmlutil.internal.css.CSSStyle;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class HTMLNode<T extends Node> {
    public List<HTMLNode<? extends Node>> children = new ArrayList<>();
    public List<MouseClickHandler> mouseDown = new ArrayList<>();
    public List<MouseClickHandler> mouseUp = new ArrayList<>();

    public final T instance;
    public final Node parent;
    public final HTMLNode<? extends Node> parent0;

    public final CSSStyle style;

    protected HTMLNode(HTMLNode<? extends Node> parent, T instance) {
        this.instance = instance;

        if (parent != null) {
            this.parent0 = parent;
            this.parent = parent0.instance;
            this.style = new CSSStyle(this.parent0.style);
        } else {
            this.parent0 = null;
            this.parent = null;
            this.style = new CSSStyle(null);
        }
    }

    /**
     * APINOTE: The method calling this is NOT responsible for rendering the children.
     * <br>
     * APINOTE: The method calling this is responsible for background and positioning.
     * <br>
     * APINOTE: You should always call super.render() at the end of your custom implementation.
     * @param context rendering context
     */
    public void render(ElementRenderingContext context) {
        for (HTMLNode<? extends Node> child : this.children) {
            context.drawBackground(child);

            context.currentlyRendered = child;
            context.startLogging(child);
            child.render(context);
            context.endLogging(child);
        }
    }
}
