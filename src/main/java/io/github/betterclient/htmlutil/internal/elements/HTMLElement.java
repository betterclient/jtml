package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.ElementParser;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class HTMLElement extends HTMLNode<Element> {
    private List<Node> oldElements;

    protected HTMLElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
        reload();
    }

    /**
     * SAFE RELOAD IMPLEMENTATION
     * DO NOT CHANGE YOU WILL BREAK EVERYTHING.
     */
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
    }

    public abstract io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document);
}
