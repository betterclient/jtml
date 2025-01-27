package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.nodes.HTMLTextNode;
import org.jsoup.nodes.Element;

public class HTMLDivElement extends HTMLElement {
    public HTMLDivElement(HTMLElement parent, Element element) {
        super(parent, element);
    }

    @Override
    public void reload() {
        for (HTMLNode<?> child : children) {
            if(child instanceof HTMLTextNode) child.reload();
            if(child instanceof HTMLTextNode & child.display$moveDown) this.display$moveDown = true;
        }
        super.reload();
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLDivElement(document, this);
    }
}