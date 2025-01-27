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
    private static final Map<String, Function<Pair<HTMLElement, Element>, HTMLElement>> ELEMENT_MAP = getElementMap();
    private static final Map<String, Function<Pair<HTMLElement, Node>, HTMLNode<?>>> NODE_MAP = getNodeMap();

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

    private static Map<String, Function<Pair<HTMLElement, Node>, HTMLNode<?>>> getNodeMap() {
        HashMap<String, Function<Pair<HTMLElement, Node>, HTMLNode<?>>> map = new HashMap<>();

        map.put("#text", pair -> new HTMLTextNode((TextNode) pair.right(), pair.left()));

        return map;
    }

    private static Map<String, Function<Pair<HTMLElement, Element>, HTMLElement>> getElementMap() {
        Map<String, Function<Pair<HTMLElement, Element>, HTMLElement>> map = new HashMap<>();

        //-----IGNORE-----
        map.put("style", pair -> null);

        //-----HEADER-----
        for (int i = 1; i < 7; i++) {
            map.put("h" + i, pair -> new HTMLHeaderElement(pair.left(), pair.right()));
        }

        //-----ELEMENTS-----
        map.put("span", pair -> new HTMLSpanElement(pair.left(), pair.right()));
        map.put("br", pair -> new HTMLBrElement(pair.left(), pair.right()));

        //-----INPUT-----
        map.put("button", pair -> new HTMLButtonElement(pair.left(), pair.right()));
        map.put("label", pair -> new HTMLLabelElement(pair.left(), pair.right()));
        map.put("input", pair -> new HTMLInputElement(pair.left(), pair.right()));
        map.put("textarea", pair -> new HTMLTextAreaElement(pair.left(), pair.right()));

        //-----BOX-----
        map.put("div", pair -> new HTMLDivElement(pair.left(), pair.right()));
        map.put("canvas", pair -> new HTMLCanvasElement(pair.left(), pair.right()));

        //-----TEXT-----
        map.put("a", pair -> new HTMLAElement(pair.left(), pair.right()));
        map.put("b", pair -> new HTMLBElement(pair.left(), pair.right()));
        map.put("i", pair -> new HTMLIElement(pair.left(), pair.right()));

        return map;
    }
}
