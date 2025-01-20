package io.github.betterclient.jtml.internal.nodes;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.util.ElementParser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class HTMLElement extends HTMLNode<Element> {
    private List<Node> oldElements;

    protected HTMLElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
    }

    protected HTMLElement(Consumer<HTMLNode<?>> consumer, HTMLNode<?> parent, Element instance) {
        super(consumer, parent, instance);
    }

    /**
     * SAFE RELOAD IMPLEMENTATION
     * DO NOT CHANGE YOU WILL BREAK EVERYTHING.
     * I edited it ._.
     */
    @Override
    public void reload() {
        if (oldElements == null) {
            this.children.addAll(ElementParser.parse(this));
            this.oldElements = new ArrayList<>(this.instance.childNodes());
        } else {
            ArrayList<Node> nodes = new ArrayList<>(this.instance.childNodes());
            nodes.removeAll(oldElements);

            //Add new nodes
            for (Node node : nodes) {
                this.children.add(ElementParser.parseSingle(this, node));
                this.oldElements.add(node);
            }

            nodes = new ArrayList<>(oldElements);
            nodes.removeAll(this.instance.childNodes());

            //Remove old nodes
            for (Node node : nodes) {
                this.children.removeIf(htmlNode -> htmlNode.instance == node);
                this.oldElements.remove(node);
            }
        }

        document.reloadInlineCSS(document); //Reload css
        document.loadPositions(document); //Reload elements for every change to the dom
    }

    @Override
    public ElementDimensions getDimensions(ElementRenderingContext context) {
        int width = 0;
        int height = 0;

        if (!style.calculate("width").equals("auto") && parser.getSize("width") > 0) width = (int) parser.getSize("width");
        if (!style.calculate("height").equals("auto") && parser.getSize("height") > 0) height = (int) parser.getSize("height");

        if (this.width > 0) width = this.width;
        if (this.height > 0) height = this.height;

        ElementDimensions dimensions = super.getDimensions(context);
        if (width == 0) width = dimensions.width;
        if (height == 0) height = dimensions.height;

        return new ElementDimensions(width, height);
    }

    public abstract io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document);
}
