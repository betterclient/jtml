package io.github.betterclient.jtml.api.elements;

import org.jsoup.nodes.Element;

public class HTMLBElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLBElement> {
    public HTMLBElement(HTMLDocument document, HTMLElement<?> parent, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLBElement(
                parent.getInternal(),
                new Element("b")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLBElement(HTMLDocument document, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLBElement(
                document.getInternal(),
                new Element("b")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLBElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLBElement element) {
        super(document, () -> element);
    }

    public void setText(String text) {
        this.internal.instance.text(text);
        this.internal.reload();
    }

    public String getText() {
        return this.internal.instance.text();
    }
}
