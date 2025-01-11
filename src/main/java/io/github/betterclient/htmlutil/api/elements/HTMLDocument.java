package io.github.betterclient.htmlutil.api.elements;

import io.github.betterclient.htmlutil.api.DocumentScreenOptions;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main HTMLDocument class, you should extend this class.
 */
public class HTMLDocument extends HTMLElement<io.github.betterclient.htmlutil.internal.elements.HTMLDocument> {
    Map<io.github.betterclient.htmlutil.internal.elements.HTMLElement, HTMLElement<?>> internalToMapped = new HashMap<>();

    /**
     * Create a new HTMLDocument
     * @param src source of the HTML file, either a resource location (resources folder) or a html source code
     */
    public HTMLDocument(String src) {
        super(null, () -> {
            io.github.betterclient.htmlutil.internal.elements.HTMLDocument internal;
            InputStream stream = HTMLDocument.class.getResourceAsStream(src);
            if (stream == null) {
                internal = new io.github.betterclient.htmlutil.internal.elements.HTMLDocument(src);
            } else {
                try {
                    internal = new io.github.betterclient.htmlutil.internal.elements.HTMLDocument(
                            new String(stream.readAllBytes())
                    );
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return internal;
        });

        cacheInternals(this);
    }

    private void cacheInternals(HTMLElement<?> element) {
        internalToMapped.put(element.internal, element);

        for (HTMLNode<? extends Node> child : element.internal.children) {
            if (child instanceof io.github.betterclient.htmlutil.internal.elements.HTMLElement el)
                this.cacheInternals(el.toAPI(this));
        }
    }

    /**
     * Create a new empty HTMLDocument
     */
    public HTMLDocument() {
        super(null, () -> new io.github.betterclient.htmlutil.internal.elements.HTMLDocument(""));
    }

    /**
     * Add a .css file or a source code to the document.
     * @param src .css file location or source code
     */
    public final void addStyleSheet(String src) {
        String out;
        InputStream stream = HTMLDocument.class.getResourceAsStream(src);
        if (stream == null) {
            out = src;
        } else {
            try {
                out = new String(stream.readAllBytes());
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.internal.addStyleSheet(out);
    }

    /**
     * Get an element by id.
     * @param id the id
     * @return the element with the id
     * @param <T> yea
     */
    @SuppressWarnings("unchecked")
    public final <T extends HTMLElement<?>> T getElementById(String id) {
        Element element = this.internal.instance.getElementById(id);
        for (io.github.betterclient.htmlutil.internal.elements.HTMLElement htmlElement : this.internalToMapped.keySet()) {
            if (htmlElement.instance == element) {
                HTMLElement<?> element1 = this.internalToMapped.get(htmlElement);
                return (T) element1;
            }
        }

        throw new NullPointerException("Element \"" + id + "\" not found in document.");
    }

    /**
     * Select elements using a CSS selector
     * @param cssSelector selector
     * @return elements that can be selected using the selector
     */
    public final List<HTMLElement<?>> select(String cssSelector) {
        List<HTMLElement<?>> elements = new ArrayList<>();
        Elements elements0 = this.internal.instance.select(cssSelector);
        for (io.github.betterclient.htmlutil.internal.elements.HTMLElement htmlElement : this.internalToMapped.keySet()) {
            if (elements0.contains(htmlElement.instance)) {
                HTMLElement<?> element1 = this.internalToMapped.get(htmlElement);
                elements.add(element1);
            }
        }

        return elements;
    }

    /**
     * Open this HTMLDocument as a screen.
     * This is the main function you'll use to open the document you've created.
     */
    public final void openAsScreen(DocumentScreenOptions options) {
        internal.openAsScreen(options);
    }
}
