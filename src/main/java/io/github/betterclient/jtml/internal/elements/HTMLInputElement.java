package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.nodes.FocusableElement;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import org.jsoup.nodes.Element;

public class HTMLInputElement extends FocusableElement {
    public HTMLInputElement(HTMLElement parent, Element element) {
        super(parent, element, false);
        this.style.MAP.put("background-color", "darkgray");
        this.style.MAP.put("border-radius", "4px");
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLInputElement(document, this);
    }

    @Override
    public ElementDimensions getDefaultDimensions() {
        return new ElementDimensions(100, 13);
    }
}