package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.nodes.HTMLElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Element;

public class HTMLBrElement extends HTMLElement {
    public HTMLBrElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
        this.display$moveDown = true;
    }

    @Override
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.htmlutil.api.elements.HTMLBrElement(document, this);
    }

    @Override
    public ElementDimensions getDimensions(ElementRenderingContext context) {
        return new ElementDimensions(0, 9);
    }
}