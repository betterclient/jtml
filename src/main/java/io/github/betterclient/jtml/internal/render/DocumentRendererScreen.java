package io.github.betterclient.jtml.internal.render;

import io.github.betterclient.jtml.api.DocumentScreenOptions;
import io.github.betterclient.jtml.api.event.MouseClickEvent;
import io.github.betterclient.jtml.internal.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.FocusableElement;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.nodes.HTMLTextNode;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.service.DocumentScreen;
import io.github.betterclient.jtml.service.RenderingService;
import org.jsoup.nodes.Node;

import java.util.List;
import java.util.Stack;

public class DocumentRendererScreen implements DocumentScreen {
    private final DocumentScreenOptions options;
    private final HTMLDocument document;

    public DocumentRendererScreen(HTMLDocument document, DocumentScreenOptions options) {
        this.options = options;
        this.document = document;
    }

    @Override
    public void render(RenderingService context, Runnable renderBackground, float delta, double mouseX, double mouseY) {
        if(options.renderBackground()) {
            renderBackground.run();
        }

        ElementRenderingContext context1 = new ElementRenderingContext(context);

        //I figured it out
        Stack<HTMLNode<?>> stack = new Stack<>();
        stack.push(document);
        while (!stack.isEmpty()) {
            HTMLNode<?> node = stack.pop();

            context1.x = node.getX();
            context1.y = node.getY();

            context1.drawBackground(node);
            node.render(context1);

            List<HTMLNode<? extends Node>> children = node.children;
            for (int i = children.size() - 1; i >= 0; i--) {
                stack.push(children.get(i));
            }
        }
    }

    @Override
    public void init() {
        recursiveReset(document);

        this.document.loadPositions(document);
        this.document.reloadPositions(document);
    }

    private void recursiveReset(HTMLNode<?> e) {
        for (HTMLNode<? extends Node> child : e.children) {
            recursiveReset(child);
        }

        e.x = 0;
        e.y = 0;
        e.width = -1;
        e.height = -1;

        if (e instanceof HTMLTextNode) e.reload();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return options.closeOnEsc();
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        ElementRenderingContext context = new ElementRenderingContext(this.document.service.getFontService());
        mouseClick(document, new MouseClickEvent(context, mouseX, mouseY, button));
    }

    private boolean mouseClick(HTMLNode<? extends Node> element, MouseClickEvent event) {
        ElementDimensions box = element.getDimensions(event.context());
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseClick(child, event)) return true;
        }

        if (basicCollisionCheck(event.mouseX(), event.mouseY(), element.getX(), element.getY(), element.getX() + box.width, element.getY() + box.height)) {
            element.mouseDown(event);
            if (element instanceof HTMLElement && !(element instanceof FocusableElement)) {
                document.focusedNode = null;
            }

            return element instanceof HTMLElement; //Normal nodes - shouldn't be included
            //I don't even now why I made them have mouse-up-down events :pray:
        }

        document.focusedNode = null;
        return false;
    }

    //-----MOUSEUP-----
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        ElementRenderingContext context = new ElementRenderingContext(this.document.service.getFontService());
        mouseRelease(document, new MouseClickEvent(context, mouseX, mouseY, button));
    }

    private boolean mouseRelease(HTMLNode<? extends Node> element, MouseClickEvent event) {
        ElementDimensions box = element.getDimensions(event.context());
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseRelease(child, event)) return true;
        }

        if (basicCollisionCheck(event.mouseX(), event.mouseY(), element.getX(), element.getY(), element.getX() + box.width, element.getY() + box.height)) {
            element.mouseUp(event);

            return element instanceof HTMLElement; //Normal nodes - shouldn't be included
            //I don't even now why I made them have mouse-up-down events :pray:
        }

        return false;
    }

    //----KEYBOARD----
    @Override
    public void charTyped(char chr) {
        if (document.focusedNode == null) return;

        if (document.focusedNode instanceof FocusableElement focusableElement) {
            focusableElement.keyboardPress(new ElementRenderingContext(this.document.service.getFontService()), chr);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode) {
        if (document.focusedNode == null) return;

        if (document.focusedNode instanceof FocusableElement focusableElement) {
            String s = this.document.service.getUtilityService().getKeyName(keyCode, scanCode);
            if (s == null && keyCode != this.document.service.getUtilityService().getSpaceKeyCode()) {
                focusableElement.keyboardDown(new ElementRenderingContext(this.document.service.getFontService()), keyCode);
            }

            if (s != null && s.equals("v") && this.document.service.getUtilityService().isControlDown()) {
                focusableElement.keyboardPress(new ElementRenderingContext(this.document.service.getFontService()), 'v');
            }
        }
    }

    @Override
    public void keyReleased(int keyCode, int scanCode) {
        if (document.focusedNode == null) return;

        if (document.focusedNode instanceof FocusableElement focusableElement) {
            if (this.document.service.getUtilityService().getKeyName(keyCode, scanCode) == null) {
                focusableElement.keyboardUp(new ElementRenderingContext(this.document.service.getFontService()), keyCode);
            }
        }
    }

    public static boolean basicCollisionCheck(double mouseX, double mouseY, double x, double y, double endX, double endY) {
        double val = x;
        if(endX < x) {
            x = endX;
            endX = val;
        }

        val = y;
        if(endY < y) {
            y = endY;
            endY = val;
        }

        return mouseX >= x & mouseX <= endX & mouseY >= y & mouseY <= endY;
    }
}