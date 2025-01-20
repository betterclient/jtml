package io.github.betterclient.htmlutil.internal.render;

import io.github.betterclient.htmlutil.api.DocumentScreenOptions;
import io.github.betterclient.htmlutil.api.event.MouseClickEvent;
import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.nodes.FocusableElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLElement;
import io.github.betterclient.htmlutil.internal.nodes.HTMLNode;
import io.github.betterclient.htmlutil.internal.nodes.HTMLTextNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jsoup.nodes.Node;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Stack;

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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(options.renderBackground()) {
            super.render(context, mouseX, mouseY, delta);
        }

        ElementRenderingContext context1 = new ElementRenderingContext(new UIRenderingContext(context));

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
    public boolean shouldCloseOnEsc() {
        return options.closeOnEsc();
    }

    public void open() {
        MinecraftClient.getInstance().setScreen(this);
    }

    //-----MOUSEDOWN-----
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ElementRenderingContext context = new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT);
        mouseClick(document, new MouseClickEvent(context, mouseX, mouseY, button));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean mouseClick(HTMLNode<? extends Node> element, MouseClickEvent event) {
        ElementDimensions box = element.getDimensions(event.context());
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseClick(child, event)) return true;
        }

        if (UIRenderingContext.basicCollisionCheck(event.mouseX(), event.mouseY(), element.getX(), element.getY(), element.getX() + box.width, element.getY() + box.height)) {
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
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        ElementRenderingContext context = new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT);
        mouseRelease(document, new MouseClickEvent(context, mouseX, mouseY, button));

        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean mouseRelease(HTMLNode<? extends Node> element, MouseClickEvent event) {
        ElementDimensions box = element.getDimensions(event.context());
        for (HTMLNode<? extends Node> child : element.children) {
            if(this.mouseRelease(child, event)) return true;
        }

        if (UIRenderingContext.basicCollisionCheck(event.mouseX(), event.mouseY(), element.getX(), element.getY(), element.getX() + box.width, element.getY() + box.height)) {
            element.mouseUp(event);

            return element instanceof HTMLElement; //Normal nodes - shouldn't be included
            //I don't even now why I made them have mouse-up-down events :pray:
        }

        return false;
    }

    //----KEYBOARD----
    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (document.focusedNode == null) return super.charTyped(chr, modifiers);

        if (document.focusedNode instanceof FocusableElement focusableElement) {
            focusableElement.keyboardPress(new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT), chr);
        }

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (document.focusedNode == null) return super.keyPressed(keyCode, scanCode, modifiers);

        if (document.focusedNode instanceof FocusableElement focusableElement) {
            String s = GLFW.glfwGetKeyName(keyCode, scanCode);
            if (s == null && keyCode != GLFW.GLFW_KEY_SPACE) {
                focusableElement.keyboardDown(new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT), keyCode);
            }

            if (s != null && s.equals("v") && Screen.hasControlDown()) {
                focusableElement.keyboardPress(new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT), 'v');
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (document.focusedNode == null) return super.keyReleased(keyCode, scanCode, modifiers);

        if (document.focusedNode instanceof FocusableElement focusableElement) {
            if (GLFW.glfwGetKeyName(keyCode, scanCode) == null) {
                focusableElement.keyboardUp(new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT), keyCode);
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}