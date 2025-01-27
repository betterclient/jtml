package io.github.betterclient.jtml.api.elements;

import org.jsoup.nodes.Element;

public class HTMLAElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLAElement> {
    public HTMLAElement(HTMLDocument document, HTMLElement<?> parent, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLAElement(
                parent.getInternal(),
                new Element("a")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLAElement(HTMLDocument document, String text) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLAElement(
                document.getInternal(),
                new Element("a")
        ));

        this.internal.instance.text(text);
        this.internal.reload();
    }

    public HTMLAElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLAElement element) {
        super(document, () -> element);
    }

    public void setText(String text) {
        this.internal.instance.text(text);
        this.internal.reload();
    }

    public void setHref(String addr) {
        this.internal.instance.attr("href", addr);
        this.internal.reload();
    }

    public String getText() {
        return this.internal.instance.text();
    }
}
