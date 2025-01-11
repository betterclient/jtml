package io.github.betterclient.htmlutil.internal;

import io.github.betterclient.htmlutil.internal.elements.*;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.nodes.HTMLTextNode;
import org.jsoup.nodes.Element;
import it.unimi.dsi.fastutil.Pair;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ElementParser {
    private static final Map<String, Function<Pair<HTMLElement, Element>, HTMLElement>> ELEMENT_MAP = new HashMap<>(
            Map.ofEntries(
                    Map.entry(
                            "span",
                            pair -> new HTMLSpanElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "br",
                            pair -> new HTMLBrElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "button",
                            pair -> new HTMLButtonElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "style",
                            pair -> null //Handle later
                    )
            )
    );

    private static final Map<String, Function<Pair<HTMLElement, Node>, HTMLNode<?>>> NODE_MAP = new HashMap<>(
            Map.ofEntries(
                    Map.entry(
                            "#text",
                            pair -> new HTMLTextNode((TextNode) pair.right(), pair.left())
                    )
            )
    );

    public static List<HTMLNode<?>> parse(HTMLElement element) {
        List<HTMLNode<?>> nodes = new ArrayList<>();

        for (Node child : element.instance.childNodes()) {
            if (child instanceof Element el) {
                HTMLElement htmlElement = ELEMENT_MAP.getOrDefault(
                        el.tagName(),
                        htmlElementElementPair -> {
                            throw new UnsupportedOperationException("Tag: " + el.tagName() + " not implemented");
                        }
                ).apply(Pair.of(element, el));
                if (htmlElement == null) continue; //style elements will skip here

                nodes.add(htmlElement);
            } else {
                nodes.add(NODE_MAP.getOrDefault(
                        child.nodeName(),
                        htmlElementNodePair -> {
                            throw new UnsupportedOperationException("Node: " + child.nodeName() + " not implemented");
                        }
                ).apply(Pair.of(element, child)));
            }
        }

        return nodes;
    }

    public static HTMLNode<?> parseSingle(HTMLElement parent, Node node) {
        if (node instanceof Element el) {
            return (ELEMENT_MAP.getOrDefault(
                    el.tagName(),
                    htmlElementElementPair -> {
                        throw new UnsupportedOperationException("Tag: " + el.tagName() + " not implemented");
                    }
            ).apply(Pair.of(parent, el)));
        } else {
            return (NODE_MAP.getOrDefault(
                    node.nodeName(),
                    htmlElementNodePair -> {
                        throw new UnsupportedOperationException("Node: " + node.nodeName() + " not implemented");
                    }
            ).apply(Pair.of(parent, node)));
        }
    }
}
