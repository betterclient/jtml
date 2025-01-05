package io.github.betterclient.htmlutil.api.elements;

import org.jsoup.nodes.Element;

public class HTMLButtonElement extends HTMLElement<io.github.betterclient.htmlutil.internal.elements.HTMLButtonElement> {
    public HTMLButtonElement(HTMLDocument document, HTMLElement<?> owner, String text) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLButtonElement(
                owner.internal,
                new Element("button")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLButtonElement(HTMLDocument document, String text) {
        super(document, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLButtonElement(
                document.internal,
                new Element("button")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLButtonElement(HTMLDocument document, io.github.betterclient.htmlutil.internal.elements.HTMLButtonElement element) {
        super(document, () -> element);
    }

    public void setText(String text) {
        this.internal.instance.text(text);
        this.internal.reload();
    }
}
