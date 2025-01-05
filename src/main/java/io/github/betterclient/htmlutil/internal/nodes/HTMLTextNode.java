package io.github.betterclient.htmlutil.internal.nodes;

import io.github.betterclient.htmlutil.internal.elements.HTMLElement;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import net.minecraft.client.resource.language.I18n;
import org.jsoup.nodes.TextNode;

public class HTMLTextNode extends HTMLNode<TextNode> {
    public HTMLTextNode(TextNode instance, HTMLElement parent) {
        super(parent, instance);
        //parent.kill();
    }

    @Override
    public void render(ElementRenderingContext context) {
        String text = stripFirst(instance.text());

        if (text.startsWith("Translate->")) text = I18n.translate(text.substring(11));

        context.renderText(text);

        super.render(context);
    }

    private String stripFirst(String text) {
        return text.charAt(0) == ' ' ? text.substring(1) : text;
    }
}