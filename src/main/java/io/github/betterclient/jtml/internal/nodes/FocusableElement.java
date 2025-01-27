package io.github.betterclient.jtml.internal.nodes;

import io.github.betterclient.jtml.api.event.KeyboardKey;
import io.github.betterclient.jtml.internal.elements.HTMLInputElement;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import io.github.betterclient.jtml.internal.css.styles.Border;
import io.github.betterclient.jtml.internal.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.util.MultilineStringInputHandler;
import io.github.betterclient.jtml.internal.util.StringInputHandler;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Vector;

public abstract class FocusableElement extends HTMLElement {
    public HTMLDocument document;
    public final StringInputHandler handler;

    protected FocusableElement(HTMLNode<?> parent, Element instance, boolean multiline) {
        super(parent, instance);

        HTMLNode<?> node = parent;
        while(node.parent0 != null) {
            node = node.parent0;
        }
        if (node instanceof HTMLDocument doc) {
            this.document = doc;
        }

        this.handler = multiline ?
                new MultilineStringInputHandler(this, instance.text()) :
                new StringInputHandler(this, instance.text());

        this.handler.reloadDisplay(new ElementRenderingContext(this.document.service.getFontService())); //Do it yourself because reload() will not

        this.mouseDown.add(event -> {
            this.document.focusedNode = this;
            this.handler.reloadDisplay(event.context());
        });
    }

    @Override
    public void reload() {
        if (this.handler != null) this.handler.reloadDisplay(new ElementRenderingContext(this.document.service.getFontService()));
        //super.reload();
        //Prevent children of input nodes (rendering :>)
    }

    protected boolean isFocused() {
        return document.focusedNode == this;
    }

    @Override
    @SuppressWarnings("SuspiciousRegexArgument") //waah waah cry louder
    public void render(ElementRenderingContext context) {
        String t = this.handler.getDisplayedText();
        if (this instanceof HTMLInputElement a && a.instance.attr("type").equals("password")) {
            t = t.replaceAll(".", "*");
        }

        int width = context.renderText(t, 2);

        if (this.isFocused()) {
            Border border = new Border();
            border.setWidth("1px");
            border.setStyle("solid");
            border.setColor("white");
            border.render(this.document, context.context, this.getX(), this.getY(), this.getDimensions(context).width, this.getDimensions(context).height);
            this.handler.renderVerticalLine(width, context);
            this.handler.renderSelection(context);
        } else {
            this.handler.reset();
        }
    }

    public abstract ElementDimensions getDefaultDimensions();

    @Override
    public ElementDimensions getDimensions(ElementRenderingContext context) {
        int width = getDefaultDimensions().width;
        int height = getDefaultDimensions().height;

        if (!style.calculate("width").equals("auto") && parser.getSize("width") > 0) width = (int) parser.getSize("width");
        if (!style.calculate("height").equals("auto") && parser.getSize("height") > 0) height = (int) parser.getSize("height");

        if (this.width > 0) width = this.width;
        if (this.height > 0) height = this.height;

        return new ElementDimensions(width, height);
    }

    List<KeyboardKey> MODIFIERS_PRESSED = new Vector<>();

    public void keyboardPress(ElementRenderingContext context, char character) {
        this.handler.type(context, character, MODIFIERS_PRESSED.contains(KeyboardKey.CONTROL));
        this.handler.reloadDisplay(context);
    }

    public void keyboardDown(ElementRenderingContext context, int id) {
        KeyboardKey key = context.getKey(id);
        if (key == null) return;

        MODIFIERS_PRESSED.add(key);
    }

    public void keyboardUp(ElementRenderingContext context, int id) {
        KeyboardKey key = context.getKey(id);
        if (key == null) return;

        MODIFIERS_PRESSED.remove(key);

        boolean controlPressed = MODIFIERS_PRESSED.contains(KeyboardKey.CONTROL);
        boolean shiftPressed = MODIFIERS_PRESSED.contains(KeyboardKey.SHIFT);
        handleKeys(context, key, controlPressed, shiftPressed);
    }

    private void handleKeys(ElementRenderingContext context, KeyboardKey key, boolean controlPressed, boolean shiftPressed) {
        switch (key) {
            case BACKSPACE -> this.handler.backspace(controlPressed);
            case ARROW_LEFT -> this.handler.leftArrow(controlPressed, shiftPressed);
            case ARROW_RIGHT -> this.handler.rightArrow(controlPressed, shiftPressed);
            case ENTER -> this.handler.enter(context);
        }

        this.handler.reloadDisplay(context);
    }

    public void unFocus() {
        this.document.focusedNode = null;
    }
}
