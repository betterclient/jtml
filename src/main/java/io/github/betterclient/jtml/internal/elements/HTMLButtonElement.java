package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import org.jsoup.nodes.Element;

public class HTMLButtonElement extends HTMLElement {
    public HTMLButtonElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
        this.style.MAP.put("background-color", "lightgray");
        this.style.MAP.put("border-radius", "4px");
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLButtonElement(document, this);
    }
}
