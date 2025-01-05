package io.github.betterclient.htmlutil.internal.render.display;

import io.github.betterclient.htmlutil.internal.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.elements.HTMLElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import io.github.betterclient.htmlutil.internal.render.UIRenderingContext;
import io.github.betterclient.htmlutil.internal.css.ColorParser;
import org.joml.Vector4i;
import org.jsoup.nodes.Node;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultDisplay {
    public static void render(HTMLDocument document, UIRenderingContext context) {
        ElementRenderingContext context0 = new ElementRenderingContext(context);
        context0.x = 44;
        context0.y = 44;

        //enable this when debugging why your new element isn't working "mike"
        //drawBoundingBoxes(context, document, new AtomicInteger(0));

        document.render(context0);
        context0.drawBackground(document);

        for (HTMLNode<?> child : document.children) {
            context0.currentlyRendered = child;
            context0.startLogging(child);
            child.render(context0);
            context0.endLogging(child);

            if (child instanceof HTMLElement) context0.x = 44;
        }
    }

    @SuppressWarnings("unused")
    private static void drawBoundingBoxes(UIRenderingContext context, HTMLNode<?> element, AtomicInteger count) {
        Vector4i box = ElementRenderingContext.ELEMENT_BOUNDING_BOXES_MAP.getOrDefault(
                element, new Vector4i(0, 0, 0, 0)
        );

        context.context.fill(box.x, box.y, box.x + box.z, box.y + box.w, ColorParser.NAMED_COLORS.entrySet().stream().toList().get(count.getAndIncrement()).getValue().getRGB());

        for (HTMLNode<? extends Node> child : element.children) {
            drawBoundingBoxes(context, child, count);
        }
    }
}