package io.github.betterclient.jtml.api.elements;

import org.jsoup.nodes.Element;

public class HTMLLabelElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLLabelElement> {
    public HTMLLabelElement(HTMLDocument document, HTMLElement<?> parent) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLLabelElement(
                parent.getInternal(),
                new Element("label")
        ));
    }

    public HTMLLabelElement(HTMLDocument document) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLLabelElement(
                document.getInternal(),
                new Element("label")
        ));
    }

    public HTMLLabelElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLLabelElement element) {
        super(document, () -> element);
    }
}
