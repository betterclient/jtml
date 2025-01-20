package io.github.betterclient.jtml.api.elements;

import org.jsoup.nodes.Element;

public class HTMLSpanElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLSpanElement> {
    public HTMLSpanElement(HTMLDocument document, HTMLElement<?> parent, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLSpanElement(
                parent.getInternal(),
                new Element("span")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLSpanElement(HTMLDocument document, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLSpanElement(
                document.getInternal(),
                new Element("span")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLSpanElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLSpanElement element) {
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
