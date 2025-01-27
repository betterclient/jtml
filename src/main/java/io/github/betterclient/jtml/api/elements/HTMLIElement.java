package io.github.betterclient.jtml.api.elements;

import org.jsoup.nodes.Element;

public class HTMLIElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLIElement> {
    public HTMLIElement(HTMLDocument document, HTMLElement<?> parent, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLIElement(
                parent.getInternal(),
                new Element("i")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLIElement(HTMLDocument document, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLIElement(
                document.getInternal(),
                new Element("i")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLIElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLIElement element) {
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
