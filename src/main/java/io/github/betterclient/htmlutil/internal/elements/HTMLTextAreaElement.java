package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.nodes.FocusableElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLElement;
import org.jsoup.nodes.Element;

public class HTMLTextAreaElement extends FocusableElement {
    public HTMLTextAreaElement(HTMLElement parent, Element element) {
        super(parent, element, true);
        this.style.MAP.put("background-color", "darkgray");
        this.style.MAP.put("border-radius", "4px");
    }

    @Override
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.htmlutil.api.elements.HTMLTextAreaElement(document, this);
    }

    @Override
    public ElementDimensions getDefaultDimensions() {
        return new ElementDimensions(100, 33);
    }
}