package io.github.betterclient.jtml.api.elements;

import org.jsoup.nodes.Element;

public class HTMLTextAreaElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLTextAreaElement> {
    public HTMLTextAreaElement(HTMLDocument document, HTMLElement<?> parent, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLTextAreaElement(
                parent.getInternal(),
                new Element("textarea")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLTextAreaElement(HTMLDocument document, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLTextAreaElement(
                document.getInternal(),
                new Element("textarea")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLTextAreaElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLTextAreaElement element) {
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
