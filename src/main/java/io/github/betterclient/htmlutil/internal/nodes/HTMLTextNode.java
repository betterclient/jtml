package io.github.betterclient.htmlutil.internal.nodes;

import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.elements.HTMLElement;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import io.github.betterclient.htmlutil.internal.render.UIRenderingContext;
import net.minecraft.client.resource.language.I18n;
import org.jsoup.nodes.TextNode;

public class HTMLTextNode extends HTMLNode<TextNode> {
    public HTMLTextNode(TextNode instance, HTMLElement parent) {
        super(parent, instance);
    }

    @Override
    public void render(ElementRenderingContext context) {
        String text = stripFirst(instance.text());

        if (text.startsWith("Translate->")) text = I18n.translate(text.substring(11));

        context.renderText(text);

        if (this.parent0.style.calculate("text-align").equals("center")) {
            centerAllChildren();
        }
    }

    @Override
    public void reload() {
        if (parser.getSize("margin") > 0) {
            this.display$moveDown = true;
        } else if (parser.getSize("padding") > 0) {
            this.display$moveDown = true;
        } else if (this.parent0.parser.getSize("margin") > 0) {
            this.display$moveDown = true;
        } else if (this.parent0.parser.getSize("padding") > 0) {
            this.display$moveDown = true;
        }
    }

    private void centerAllChildren() {
        ElementRenderingContext context = new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT);
        ElementDimensions dimensions = parent0.getDimensions(context);

        int totalWidth = 0;
        for (HTMLNode<?> child : parent0.children) {
            totalWidth += child.getDimensions(context).width;
        }

        int currentX = (dimensions.width - totalWidth) / 2;
        for (HTMLNode<?> child : parent0.children) {
            ElementDimensions dimensions0 = child.getDimensions(context);

            child.x = currentX;
            child.y = (dimensions.height - dimensions0.height) / 2;

            currentX += dimensions0.width;
        }
    }

    @Override
    public ElementDimensions getDimensions(ElementRenderingContext context) {
        String text = stripFirst(instance.text());

        if (text.startsWith("Translate->")) text = I18n.translate(text.substring(11));
        if (text.isEmpty()) return new ElementDimensions(0, 0);

        return context.asDimensions(parser, text);
    }

    private String stripFirst(String text) {
        return text.charAt(0) == ' ' ? text.substring(1) : text;
    }
}