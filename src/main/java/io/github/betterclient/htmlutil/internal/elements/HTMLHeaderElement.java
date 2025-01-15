package io.github.betterclient.htmlutil.internal.elements;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import org.jsoup.nodes.Element;

public class HTMLHeaderElement extends HTMLElement {
    public int count;

    public HTMLHeaderElement(HTMLNode<?> parent, Element instance) {
        super(parent, instance);
        this.setCount(Integer.parseInt(instance.tagName().replace("h", "")));
        this.display$moveDown = true;
    }

    public void setCount(int count) {
        this.count = count;
        instance.tagName("h" + count);
        float a = getSize(count);
        this.style.MAP.put("font-size", a + "px");
    }

    private float getSize(int count) {
        return switch (count) {
            case 1 -> 27;
            case 2 -> 18;
            case 3 -> 13.5f;
            case 4 -> 12;
            case 5 -> 10.5f;
            case 6 -> 9;
            default -> throw new UnsupportedOperationException("Unsupported header count: " + count);
        };
    }

    @Override
    public io.github.betterclient.htmlutil.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.htmlutil.api.elements.HTMLHeaderElement(document, this);
    }

    public void setText(String text) {
        this.instance.text(text);
    }
}