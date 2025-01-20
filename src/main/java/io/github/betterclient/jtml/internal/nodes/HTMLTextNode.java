package io.github.betterclient.jtml.internal.nodes;

import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import org.jsoup.nodes.TextNode;

public class HTMLTextNode extends HTMLNode<TextNode> {
    public boolean reload = false;

    public HTMLTextNode(TextNode instance, HTMLElement parent) {
        super(parent, instance);
    }

    @Override
    public void render(ElementRenderingContext context) {
        if (reload && this.parent0.style.calculate("text-align").equals("center")) {
            reload = false;
            centerAllChildren();
            context.x = this.getX();
            context.y = this.getY();
        }

        String text = stripFirst(instance.text());

        if (text.startsWith("Translate->")) text = this.document.service.getUtilityService().translate(text.substring(11));

        context.renderText(text);
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
        reload = true;
    }

    private void centerAllChildren() {
        ElementRenderingContext context = new ElementRenderingContext(this.document.service.getFontService());
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

        if (text.startsWith("Translate->")) text = this.document.service.getUtilityService().translate(text.substring(11));
        if (text.isEmpty()) return new ElementDimensions(0, 0);

        return context.asDimensions(parser, text);
    }

    private String stripFirst(String text) {
        return text.charAt(0) == ' ' ? text.substring(1) : text;
    }
}