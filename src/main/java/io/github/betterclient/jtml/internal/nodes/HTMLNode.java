package io.github.betterclient.jtml.internal.nodes;

import io.github.betterclient.jtml.api.event.MouseClickEvent;
import io.github.betterclient.jtml.api.event.MouseClickHandler;
import io.github.betterclient.jtml.internal.css.styles.Padding;
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

    public boolean isScrollable = false; //Used to indicate whether to create a scroll bar or not
    public float scrollAmount = 0; //a number between 0 and 1 for scrolling

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
            this.style = new CSSStyle(this.parent0.style, this);
        } else {
            this.parent0 = null;
            this.parent = null;
            this.style = new CSSStyle(null, this);
        }

        if (this instanceof HTMLDocument doc) {
            this.document = doc;
        } else {
            HTMLNode<?> node = this;
            while(node.parent0 != null) {
                node = node.parent0;
            }
            assert node instanceof HTMLDocument;
            this.document = (HTMLDocument) node;
        }

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
        int width = 0, height = 0;

        for (HTMLNode<? extends Node> child : this.children) {
            ElementDimensions dimensions = child.getDimensions(context);

            if (width < (child.x + dimensions.width)) {
                width = child.x + dimensions.width;
            }

            if (height < (child.y + dimensions.height)) {
                height = child.y + dimensions.height;
            }
        }

        if (!style.calculate("width").equals("auto") && parser.getSize("width") > 0) width = (int) parser.getSize("width");
        if (!style.calculate("height").equals("auto") && parser.getSize("height") > 0) height = (int) parser.getSize("height");

        if (this.width > 0) width = this.width;
        if (this.height > 0) height = this.height;

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
