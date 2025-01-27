package io.github.betterclient.jtml.internal.render;

import io.github.betterclient.jtml.api.util.DocumentScreenOptions;
import io.github.betterclient.jtml.api.event.MouseClickEvent;
import io.github.betterclient.jtml.internal.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.nodes.FocusableElement;
import io.github.betterclient.jtml.internal.nodes.HTMLElement;
import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.nodes.HTMLTextNode;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.service.DocumentScreen;
import io.github.betterclient.jtml.service.RenderingService;
import io.github.betterclient.jtml.service.UtilityService;
import org.jsoup.nodes.Node;

import java.util.List;
import java.util.Stack;

public class DocumentRendererScreen implements DocumentScreen {
    public final DocumentScreenOptions options;
    public final HTMLDocument document;

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
            ElementDimensions box = node.getDimensions(context1);

            context1.drawBackground(node);
            context.startScissor(node.getX(), node.getY(), box.width, box.height);
            node.render(context1);
            context.endScissor();

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

    @Override
    public void mouseScroll(double mouseX, double mouseY, int direction) {

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
            UtilityService service = this.document.service.getUtilityService();

            String s = service.getKeyName(keyCode, scanCode);
            if (s == null && keyCode != service.getSpaceKeyCode()) {
                focusableElement.keyboardDown(new ElementRenderingContext(this.document.service.getFontService()), keyCode);
            }

            if (s != null && (s.equals("v") || s.equals("z") || s.equals("x") || s.equals("c") || s.equals("a")) && service.isControlDown()) {
                focusableElement.keyboardPress(new ElementRenderingContext(this.document.service.getFontService()), s.charAt(0));
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