package io.github.betterclient.jtml.internal.elements;

import io.github.betterclient.jtml.api.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.render.DocumentRendererScreen;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class HTMLAElement extends HTMLElement {
    public HTMLAElement(HTMLElement parent, Element element) {
        super(parent, element);
        if (!this.instance.attr("href").isEmpty()) {
            this.style.MAP.put("text-decoration", "underline");
        }

        this.mouseUp.add(event -> {
            String href = this.instance.attr("href");
            if (!href.isEmpty()) {
                if (this.document.api.base.isEmpty()) {
                    try {
                        InputStream inputStream = new URI(href).toURL().openConnection().getInputStream();
                        href = new String(inputStream.readAllBytes());
                        inputStream.close();
                    } catch (IOException | URISyntaxException ignored) {}
                } else {
                    href = this.document.api.base.substring(0, this.document.api.base.lastIndexOf('/') + 1) + href;
                }

                new HTMLDocument(href, this.document.service).openAsScreen(
                        this.document.service.getDocument() instanceof DocumentRendererScreen doc ? doc.options : null
                );
            }
        });
    }

    @Override
    public io.github.betterclient.jtml.api.elements.HTMLElement<?> toAPI(HTMLDocument document) {
        return new io.github.betterclient.jtml.api.elements.HTMLAElement(document, this);
    }
}