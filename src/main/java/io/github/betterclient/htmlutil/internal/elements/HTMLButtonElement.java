package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import org.jsoup.nodes.Element;

public class HTMLButtonElement extends HTMLElement {
    public HTMLButtonElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
        this.style.MAP.put("background-color", "lightgray");
    }

    @Override
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.htmlutil.api.elements.HTMLButtonElement(document, this);
    }
}
