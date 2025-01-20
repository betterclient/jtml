package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import org.jsoup.nodes.Element;

public class HTMLLabelElement extends HTMLElement {
    public HTMLLabelElement(HTMLElement parent, Element element) {
        super(parent, element);
    }

    @Override
    public void reload() {
        for (HTMLNode<?> child : children) {
            if (child instanceof HTMLBrElement & child.display$moveDown) {
                this.display$moveDown = true;
                break;
            }
        }
        super.reload();
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLLabelElement(document, this);
    }
}
