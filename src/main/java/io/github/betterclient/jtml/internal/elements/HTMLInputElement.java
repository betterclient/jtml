package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.nodes.FocusableElement;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

public class HTMLInputElement extends FocusableElement {
    public HTMLInputElement(HTMLElement parent, Element element) {
        super(parent, element, false);
        this.handler.allowedCharacterRegex = this.parse(element.attr("type"));
        this.style.MAP.put("background-color", "darkgray");
        this.style.MAP.put("border-radius", "4px");
    }

    private Pattern parse(String type) {
        return switch (type) {
            case "", "text", "password" -> Pattern.compile(".");
            case "number" -> Pattern.compile("\\d");

            default -> throw new UnsupportedOperationException("Input element with type: " + type);
        };
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLInputElement(document, this);
    }

    @Override
    public ElementDimensions getDefaultDimensions() {
        return new ElementDimensions(100, 13);
    }
}