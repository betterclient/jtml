package io.github.betterclient.jtml.api.elements;

import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Element;

public class HTMLCanvasElement extends HTMLElement<io.github.betterclient.jtml.internal.elements.HTMLCanvasElement> {
    private CanvasRenderingContext2D context = null;

    public HTMLCanvasElement(HTMLDocument document, HTMLElement<?> parent) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLCanvasElement(
                parent.getInternal(),
                new Element("canvas")
        ));
    }

    public HTMLCanvasElement(HTMLDocument document) {
        super(document, () -> new io.github.betterclient.jtml.internal.elements.HTMLCanvasElement(
                document.getInternal(),
                new Element("canvas")
        ));
    }

    public HTMLCanvasElement(HTMLDocument document, io.github.betterclient.jtml.internal.elements.HTMLCanvasElement element) {
        super(document, () -> element);
    }

    public int getWidth() {
        return this.internal.getDimensions(new ElementRenderingContext(this.document.internal.service.getFontService())).width;
    }

    public int getHeight() {
        return this.internal.getDimensions(new ElementRenderingContext(this.document.internal.service.getFontService())).height;
    }

    public CanvasRenderingContext2D getContext2D() {
        return context == null ? (context = new CanvasRenderingContext2D(this.internal)) : context;
    }
}
