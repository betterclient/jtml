package io.github.betterclient.htmlutil.api.elements;

import org.jsoup.nodes.Element;

public class HTMLBrElement extends HTMLElement<io.github.betterclient.htmlutil.internal.elements.HTMLBrElement> {
    public HTMLBrElement(HTMLDocument document, HTMLElement<?> parent) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLBrElement(parent.getInternal(), new Element("br")));
    }

    public HTMLBrElement(HTMLDocument document) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLBrElement(document.getInternal(), new Element("br")));
    }

    public HTMLBrElement(HTMLDocument document, io.github.betterclient.htmlutil.internal.elements.HTMLBrElement element) {
        super(document, () -> element);
    }
}
