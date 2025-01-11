package io.github.betterclient.htmlutil.internal.render;

import io.github.betterclient.htmlutil.api.DocumentScreenOptions;
import io.github.betterclient.htmlutil.api.event.MouseClickEvent;
import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.elements.HTMLElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jsoup.nodes.Node;

public class DocumentRenderer extends Screen {
    private final DocumentScreenOptions options;
    private final HTMLDocument document;
    public DocumentRenderer(HTMLDocument document, DocumentScreenOptions options) {
        super(Text.literal(""));
        this.options = options;
        this.document = document;
    }

    @Override
    protected void init() {
        this.document.loadPositions(document);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(options.renderBackground()) {
            super.render(context, mouseX, mouseY, delta);
        }

        ElementRenderingContext context1 = new ElementRenderingContext(new UIRenderingContext(context));
        recursiveRender(document, context1);
    }

    private void recursiveRender(HTMLNode<?> node, ElementRenderingContext context) {
        context.x = node.getX();
        context.y = node.getY();

        context.drawBackground(node);
        node.render(context);

        for (HTMLNode<? extends Node> child : node.children) {
            recursiveRender(child, context);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return options.closeOnEsc();
    }

    public void open() {
        MinecraftClient.getInstance().setScreen(this);
    }

    //-----MOUSEDOWN-----
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseClick(document, new MouseClickEvent(mouseX, mouseY, button));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean mouseClick(HTMLNode<? extends Node> element, MouseClickEvent event) {
        ElementRenderingContext context = new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT);
        ElementDimensions box = element.getDimensions(context);
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseClick(child, event)) return true;
        }

        if (UIRenderingContext.basicCollisionCheck(event.mouseX(), event.mouseY(), element.getX(), element.getY(), element.getX() + box.width, element.getY() + box.height)) {
            element.mouseDown.forEach(mouseClickHandler -> mouseClickHandler.click(event));

            return element instanceof HTMLElement; //Normal nodes - shouldn't be included
            //I don't even now why I made them have mouse-up-down events :pray:
        }

        return false;
    }

    //-----MOUSEUP-----
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseRelease(document, new MouseClickEvent(mouseX, mouseY, button));

        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean mouseRelease(HTMLNode<? extends Node> element, MouseClickEvent event) {
        ElementRenderingContext context = new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT);
        ElementDimensions box = element.getDimensions(context);
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseRelease(child, event)) return true;
        }

        if (UIRenderingContext.basicCollisionCheck(event.mouseX(), event.mouseY(), element.getX(), element.getY(), element.getX() + box.width, element.getY() + box.height)) {
            element.mouseUp.forEach(mouseClickHandler -> mouseClickHandler.click(event));

            return element instanceof HTMLElement; //Normal nodes - shouldn't be included
            //I don't even now why I made them have mouse-up-down events :pray:
        }

        return false;
    }
}