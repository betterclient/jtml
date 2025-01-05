package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Element;

public class HTMLBrElement extends HTMLElement {
    public HTMLBrElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
    }

    @Override
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.htmlutil.api.elements.HTMLBrElement(document, this);
    }

    @Override
    public void render(ElementRenderingContext context) {
        context.y += 9;

        super.render(context);
    }
}
