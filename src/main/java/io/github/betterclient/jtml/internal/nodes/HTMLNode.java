package io.github.betterclient.jtml.internal.nodes;

import io.github.betterclient.jtml.api.event.MouseClickEvent;
import io.github.betterclient.jtml.api.event.MouseClickHandler;
import io.github.betterclient.jtml.internal.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.css.StyleParser;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import io.github.betterclient.jtml.internal.css.CSSStyle;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class HTMLNode<T extends Node> {
    public List<HTMLNode<? extends Node>> children = new ArrayList<>();
    public List<MouseClickHandler> mouseDown = new ArrayList<>();
    public List<MouseClickHandler> mouseUp = new ArrayList<>();

    public boolean display$moveDown = false; //Used to indicate a "br" element, is in a field, so it's easy to use for other elements

    public int x = 0;
    public int y = 0;
    //These width & height properties should be set by the display compiler to forcefully set widths and heights
    public int width = 0;
    public int height = 0;

    public final T instance;
    public final Node parent;
    public final HTMLNode<? extends Node> parent0;

    public final CSSStyle style;
    public final StyleParser parser;
    public HTMLDocument document;

    protected HTMLNode(HTMLNode<? extends Node> parent, T instance) {
        this(null, parent, instance);
    }

    protected HTMLNode(Consumer<HTMLNode<?>> beforeEverything, HTMLNode<? extends Node> parent, T instance) {
        if (beforeEverything != null) beforeEverything.accept(this);
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

        HTMLNode<?> node = this;
        while(node.parent0 != null) {
            node = node.parent0;
        }
        this.document = (HTMLDocument) node;
        this.parser = new StyleParser(this.document, style);

        reload();
    }

    /**
     * APINOTE: The method calling this is responsible for rendering the children.
     * APINOTE: The method calling this is responsible for background and positioning.
     * @param context rendering context
     */
    public void render(ElementRenderingContext context) {}

    public ElementDimensions getDimensions(ElementRenderingContext context) {
        int width = this.width, height = this.height;

        for (HTMLNode<? extends Node> child : this.children) {
            ElementDimensions dimensions = child.getDimensions(context);

            if (width < (child.x + dimensions.width) && this.width <= 0) {
                width = child.x + dimensions.width;
            }

            if (height < (child.y + dimensions.height) && this.height <= 0) {
                height = child.y + dimensions.height;
            }
        }

        return new ElementDimensions(width, height);
    }

    public int getX() {
        int x = this.x;

        if (parent0 != null) x += parent0.getX();

        return x;
    }

    public void reload() {}

    public int getY() {
        int y = this.y;

        if (parent0 != null) y += parent0.getY();

        return y;
    }

    public void mouseDown(MouseClickEvent event) {
        this.mouseDown.forEach(mouseClickHandler -> mouseClickHandler.handle(event));
    }

    public void mouseUp(MouseClickEvent event) {
        this.mouseUp.forEach(mouseClickHandler -> mouseClickHandler.handle(event));
    }
}
