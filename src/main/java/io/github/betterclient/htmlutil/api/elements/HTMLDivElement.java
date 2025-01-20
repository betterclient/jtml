package io.github.betterclient.htmlutil.api.elements;

import org.jsoup.nodes.Element;

public class HTMLDivElement extends HTMLElement<io.github.betterclient.htmlutil.internal.elements.HTMLDivElement> {
    public HTMLDivElement(HTMLDocument document, HTMLElement<?> parent) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLDivElement(
                parent.getInternal(),
                new Element("div")
        ));
    }

    public HTMLDivElement(HTMLDocument document) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLDivElement(
                document.getInternal(),
                new Element("div")
        ));
    }

    public HTMLDivElement(HTMLDocument document, io.github.betterclient.htmlutil.internal.elements.HTMLDivElement element) {
        super(document, () -> element);
    }
}
