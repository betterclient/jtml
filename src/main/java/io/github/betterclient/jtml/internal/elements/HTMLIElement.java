package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import org.jsoup.nodes.Element;

public class HTMLIElement extends HTMLElement {
    public HTMLIElement(HTMLElement parent, Element element) {
        super(parent, element);
        this.style.MAP.put("font-weight", "italic");
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLIElement(document, this);
    }
}