package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.nodes.HTMLElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
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
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.htmlutil.api.elements.HTMLLabelElement(document, this);
    }
}
