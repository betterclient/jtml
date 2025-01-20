package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.nodes.HTMLElement;
import org.jsoup.nodes.Element;

public class HTMLSpanElement extends HTMLElement {
    public HTMLSpanElement(HTMLElement parent, Element element) {
        super(parent, element);
    }

    @Override
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.htmlutil.api.elements.HTMLSpanElement(document, this);
    }
}