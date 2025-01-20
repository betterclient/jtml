package io.github.betterclient.jtml.api.elements;

import org.jsoup.nodes.Element;

/**
 * You should use the "count" variable to declare the header count
 * eg: count=5 is < h5>
*/
public class HTMLHeaderElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLHeaderElement> {
    public HTMLHeaderElement(HTMLDocument document, HTMLElement<?> parent, String text, int count) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLHeaderElement(parent.getInternal(), new Element("h" + count)));
        this.setCount(count);
        this.setText(text);
    }

    public HTMLHeaderElement(HTMLDocument document, String text, int count) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLHeaderElement(document.getInternal(), new Element("h" + count)));
        this.setCount(count);
        this.setText(text);
    }

    public HTMLHeaderElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLHeaderElement instance) {
        super(document, () -> instance);
    }

    public void setCount(int count) {
        this.internal.setCount(count);
        this.internal.reload();
    }

    public int getCount() {
        return this.internal.count;
    }

    public void setText(String text) {
        this.internal.setText(text);
        this.internal.reload();
    }
}
