package io.github.betterclient.htmlutil.internal.render;

import io.github.betterclient.htmlutil.api.DocumentScreenOptions;
import io.github.betterclient.htmlutil.api.event.MouseClickEvent;
import io.github.betterclient.htmlutil.internal.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.elements.HTMLElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.render.display.DefaultDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.joml.Vector4i;
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(options.renderBackground()) {
            super.render(context, mouseX, mouseY, delta);
        }

        //Only call rendering for children of document.
        //Others are responsible for rendering their children.
        DefaultDisplay.render(document, new UIRenderingContext(context));
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
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseClick(child, event)) return true;
        }

        Vector4i box = ElementRenderingContext.ELEMENT_BOUNDING_BOXES_MAP.get(element);
        if (box == null) return false;

        if (UIRenderingContext.basicCollisionCheck(event.mouseX(), event.mouseY(), box.x, box.y, box.x + box.z, box.y + box.w)) {
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
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseRelease(child, event)) return true;
        }

        Vector4i box = ElementRenderingContext.ELEMENT_BOUNDING_BOXES_MAP.get(element);
        if(box == null) return false;

        if (UIRenderingContext.basicCollisionCheck(event.mouseX(), event.mouseY(), box.x, box.y, box.x + box.z, box.y + box.w)) {
            element.mouseUp.forEach(mouseClickHandler -> mouseClickHandler.click(event));

            return element instanceof HTMLElement; //Normal nodes - shouldn't be included
            //I don't even now why I made them have mouse-up-down events :pray:
        }

        return false;
    }
}