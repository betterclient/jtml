package io.github.betterclient.htmlutil.api.elements;

import org.jsoup.nodes.Element;

public class HTMLInputElement extends HTMLElement<io.github.betterclient.htmlutil.internal.elements.HTMLInputElement> {
    public HTMLInputElement(HTMLDocument document, HTMLElement<?> parent, String text) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLInputElement(
                parent.getInternal(),
                new Element("input")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLInputElement(HTMLDocument document, String text) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLInputElement(
                document.getInternal(),
                new Element("input")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLInputElement(HTMLDocument document, io.github.betterclient.htmlutil.internal.elements.HTMLInputElement element) {
        super(document, () -> element);
    }

    public void setText(String text) {
        this.internal.handler.setTypedText(text);
        this.internal.reload();
    }

    public String getText() {
        return this.internal.handler.getTypedText();
    }
}
