package io.github.betterclient.jtml.internal.util;

import io.github.betterclient.jtml.internal.elements.*;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.nodes.HTMLTextNode;
import org.jsoup.nodes.Element;
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
                            "div",
                            pair -> new HTMLDivElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "label",
                            pair -> new HTMLLabelElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "input",
                            pair -> new HTMLInputElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "textarea",
                            pair -> new HTMLTextAreaElement(pair.left(), pair.right())
                    ),

                    //-----HEADERS-----
                    Map.entry(
                            "h1",
                            pair -> new HTMLHeaderElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "h2",
                            pair -> new HTMLHeaderElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "h3",
                            pair -> new HTMLHeaderElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "h4",
                            pair -> new HTMLHeaderElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "h5",
                            pair -> new HTMLHeaderElement(pair.left(), pair.right())
                    ),
                    Map.entry(
                            "h6",
                            pair -> new HTMLHeaderElement(pair.left(), pair.right())
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
        List<String> notImplementedNodes = new ArrayList<>();

        for (Node child : element.instance.childNodes()) {
            if (child instanceof Element el) {
                HTMLElement htmlElement = ELEMENT_MAP.getOrDefault(
                        el.tagName(),
                        htmlElementElementPair -> {
                            notImplementedNodes.add("Tag: " + el.tagName());
                            return null;
                        }
                ).apply(Pair.of(element, el));
                if (htmlElement == null) continue; //style elements will skip here

                nodes.add(htmlElement);
            } else {
                HTMLNode<?> htmlNode = NODE_MAP.getOrDefault(
                        child.nodeName(),
                        htmlElementNodePair -> {
                            notImplementedNodes.add("Node: " + child.nodeName());
                            return null;
                        }
                ).apply(Pair.of(element, child));

                if (htmlNode == null) continue; //errors will skip here
                nodes.add(htmlNode);
            }
        }

        if (notImplementedNodes.isEmpty()) return nodes;

        String out = String.join("\n", notImplementedNodes).concat("\nNot implemented.");
        throw new UnsupportedOperationException(out);
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
