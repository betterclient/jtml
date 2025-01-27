package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import org.jsoup.nodes.Element;

import java.util.function.Consumer;

public class HTMLCanvasElement extends HTMLElement {
    public Consumer<ElementRenderingContext> renderEvent = (context) -> {};

    public HTMLCanvasElement(HTMLElement parent, Element element) {
        super(parent, element);
    }

    @Override
    public void reload() { } //Prevent children

    @Override
    public void render(ElementRenderingContext context) {
        renderEvent.accept(context);
    }

    @Override
    public ElementDimensions getDimensions(ElementRenderingContext context) {
        int width = 150;
        int height = 75;

        if (!style.calculate("width").equals("auto") && parser.getSize("width") > 0) width = (int) parser.getSize("width");
        if (!style.calculate("height").equals("auto") && parser.getSize("height") > 0) height = (int) parser.getSize("height");

        if (this.width > 0) width = this.width;
        if (this.height > 0) height = this.height;

        return new ElementDimensions(width, height);
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLCanvasElement(document, this);
    }
}