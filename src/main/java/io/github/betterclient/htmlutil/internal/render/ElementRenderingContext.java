package io.github.betterclient.htmlutil.internal.render;

import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.css.StyleParser;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import org.joml.Vector4i;
import org.jsoup.nodes.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ElementRenderingContext {
    public static Map<HTMLNode<?>, Vector4i> ELEMENT_BOUNDING_BOXES_MAP = new HashMap<>();
    public int x, y;
    private final UIRenderingContext context;
    public HTMLNode<?> currentlyRendered;

    public ElementRenderingContext(UIRenderingContext context) {
        this.context = context;
    }

    public void drawBackground(HTMLNode<? extends Node> child) {
        StyleParser parser = new StyleParser(child.style);
        Vector4i box = ELEMENT_BOUNDING_BOXES_MAP.get(child);
        if (box == null) {
            if (child.parent == null) {
                box = new Vector4i(0, 0, context.context.getScaledWindowWidth(), context.context.getScaledWindowHeight());
            } else {
                return;
            }
        }

        context.context.fill(box.x, box.y, box.x + box.z, box.y + box.w, parser.getBackgroundColor());
    }

    public void renderText(String text) {
        StyleParser parser = new StyleParser(this.currentlyRendered.style);
        //TODO: add sizing the text

        int width = this.context.renderText(text, x, y, parser.getColor()) + 2;
        this.x += width;
        //TODO: make this offset customizable (the +2)

        logHeight(currentlyRendered, width, this.context.height());
    }

    private void logHeight(HTMLNode<?> element, int x, int y) {
        if (element.parent0 != null) {
            logHeight(element.parent0, x, y);
        }

        if (HEIGHT_LOGGER.get(element) != null) {
            Pair<AtomicInteger, Integer> pairH = HEIGHT_LOGGER.get(element);
            Pair<AtomicInteger, Integer> pairW = WIDTH_LOGGER.get(element);

            if (pairH.second() != this.y) {
                pairH.second(this.y);
                pairH.first().getAndAdd(y);
            }

            if (pairW.second() != this.x) {
                pairW.second(this.x);
                pairW.first().getAndAdd(x);
            }
        }
    }

    //Width and height logger should store a pair of
    //AtomicInteger - will be incremented for y/x increase
    //Integer - will control whether the atomic is increased, it stores the last x/y value
    Map<HTMLNode<?>, Pair<AtomicInteger, Integer>> HEIGHT_LOGGER = new HashMap<>();
    Map<HTMLNode<?>, Pair<AtomicInteger, Integer>> WIDTH_LOGGER = new HashMap<>();

    public void startLogging(HTMLNode<?> element) {
        HEIGHT_LOGGER.put(element, new ObjectObjectMutablePair<>(new AtomicInteger(0), 0));
        WIDTH_LOGGER.put(element, new ObjectObjectMutablePair<>(new AtomicInteger(0), 0));
    }

    public void endLogging(HTMLNode<?> element) {
        if (!WIDTH_LOGGER.containsKey(element)) return;

        int width = WIDTH_LOGGER.remove(element).first().get();
        int height = HEIGHT_LOGGER.remove(element).first().get();

        ELEMENT_BOUNDING_BOXES_MAP.put(element, new Vector4i(x - width, y, width, height));
    }
}