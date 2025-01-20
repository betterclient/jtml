package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import org.jsoup.nodes.Element;

public class HTMLSpanElement extends HTMLElement {
    public HTMLSpanElement(HTMLElement parent, Element element) {
        super(parent, element);
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLSpanElement(document, this);
    }
}