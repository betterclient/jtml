package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Element;

public class HTMLBrElement extends HTMLElement {
    public HTMLBrElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
        this.display$moveDown = true;
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLBrElement(document, this);
    }

    @Override
    public ElementDimensions getDimensions(ElementRenderingContext context) {
        return new ElementDimensions(0, 9);
    }
}