package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.ElementParser;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class HTMLElement extends HTMLNode<@org.jetbrains.annotations.NotNull Element> {
    private List<Node> oldElements;

    protected HTMLElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
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

        HTMLNode<?> findDocument = this;
        while (findDocument.parent0 != null) {
            findDocument = findDocument.parent0;
        }

        if (findDocument instanceof io.github.betterclient.htmlutil.internal.elements.HTMLDocument document) {
            document.reloadInlineCSS(findDocument); //Reload css
            document.loadPositions(findDocument); //Reload elements for every change to the dom
        }
    }

    public abstract io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document);
}
