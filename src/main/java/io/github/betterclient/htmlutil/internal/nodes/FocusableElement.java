package io.github.betterclient.htmlutil.internal.nodes;

import io.github.betterclient.htmlutil.api.KeyboardKey;
import io.github.betterclient.htmlutil.internal.ElementDimensions;
import io.github.betterclient.htmlutil.internal.css.styles.Border;
import io.github.betterclient.htmlutil.internal.elements.HTMLDocument;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import io.github.betterclient.htmlutil.internal.render.UIRenderingContext;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;
import java.util.Vector;

//TODO: split this into multiple classes
//TODO: shift and selection
//TODO: cutting and copying
public abstract class FocusableElement extends HTMLElement {
    public HTMLDocument document;
    public final boolean multiline;

    protected FocusableElement(HTMLNode<?> parent, Element instance, boolean multiline) {
        super(parent, instance);
        this.multiline = multiline;

        HTMLNode<?> node = parent;
        while(node.parent0 != null) {
            node = node.parent0;
        }
        if (node instanceof HTMLDocument doc) {
            this.document = doc;
        }

        this.mouseDown.add(event -> {
            this.document.focusedNode = this;
            this.reloadDisplay(event.context());
        });
        this.typedText = instance.text();
    }

    @Override
    public void reload() {
        this.reloadDisplay(new ElementRenderingContext(UIRenderingContext.DEFAULT_FONT));
        //super.reload();
        //Prevent children of input nodes (rendering :>)
    }

    protected boolean isFocused() {
        return document.focusedNode == this;
    }

    private void reloadDisplay(ElementRenderingContext context) {
        if (typedText == null) typedText = "";
        context.currentlyRendered = this;
        if (this.multiline) {
            reloadDisplayMultiline(context);
            return;
        }

        int length = typedText.length();
        int displayWidth = getDimensions(context).width - 4;

        int pointer = this.pointer;
        if (pointer == -1) {
            pointer = length;
        }

        int start = pointer;
        int end = pointer;

        while (start > 0 || end < length) {
            String substring = typedText.substring(start, end);
            if (context.width(substring) <= displayWidth) {
                displayText = substring;
                if (start > 0) start--;
                if (end < length) end++;
            } else {
                break;
            }
        }

        displayText = typedText.substring(start, end);
    }

    private void reloadDisplayMultiline(ElementRenderingContext context) {
        StringBuilder currentText = new StringBuilder();

        int curX = 0;
        int curY = 0;
        int maxW = getDimensions(context).width - 4;
        int maxH = getDimensions(context).height - 4;
        int size = (int) this.parser.getSize("font-size");
        for (String s : typedText.split("\n")) {
            for (char c : s.toCharArray()) {
                curX += context.width(Character.toString(c));
                if (curX >= maxW) {
                    currentText.append("\n");
                    curX = 0;
                    curY += size;
                }
                currentText.append(c);
            }

            currentText.append("\n");
            curX = 0;
            curY += size;

            while (curY > maxH) {
                int firstLineEnd = currentText.indexOf("\n");
                if (firstLineEnd != -1) {
                    currentText.delete(0, firstLineEnd + 1);
                    curY -= size;
                } else {
                    break;
                }
            }
        }

        displayText = currentText.toString();
    }

    @Override
    public void render(ElementRenderingContext context) {
        int width = context.renderText(displayText, 2);

        if (this.isFocused()) {
            Border border = new Border();
            border.setWidth("1px");
            border.setStyle("solid");
            border.setColor("white");
            border.render(context.context, this.getX(), this.getY(), this.getDimensions(context).width, this.getDimensions(context).height);
            if (multiline) {
                String pointing = displayText.substring(0, pointer == -1 ? displayText.length() : pointer);

                int y = (int) (pointing.split("\n").length * this.parser.getSize("font-size"));
                int x = context.width(pointing.lines().toList().getLast());
                context.fillVerticalLine(x, y);
            } else {
                if (pointer == -1 || pointer == typedText.length()) {
                    context.fillVerticalLine(width);
                } else {
                    context.fillVerticalLine(
                            //I have no idea how this works but it does
                            Math.min(context.width(typedText.substring(Math.min(typedText.indexOf(displayText), pointer), Math.max(pointer, typedText.indexOf(displayText)))), width - 4)
                    );
                }
            }
        } else {
            pointer = -1;
            pointerWidth = 0;
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

    public String typedText;
    protected String displayText = "";
    protected int pointerWidth = 0;
    protected int pointer = -1;

    protected List<KeyboardKey> MODIFIERS_PRESSED = new Vector<>();

    public void keyboardPress(ElementRenderingContext context, char character) {
        String type = character + "";
        if (type.toLowerCase().charAt(0) == 'v' && MODIFIERS_PRESSED.contains(KeyboardKey.CONTROL)) {
            try {
                //note: this method will throw a HeadlessException
                //unless you disable it from net.minecraft.client.main.Main.<clinit>
                Transferable far = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                if (far != null && far.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    type = (String) far.getTransferData(DataFlavor.stringFlavor);
                }
            } catch (Exception ignored) {
                type = context.context.getClipboard();
            }
        }

        int pointer = this.pointer < 0 ? typedText.length() : this.pointer;
        typedText = typedText.substring(0, pointer) + type + typedText.substring(pointer);

        if (this.pointer >= 0) this.pointer += type.length();

        reloadDisplay(context);
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

        if (handleKeys(context, key, controlPressed)) return;
        reloadDisplay(context);
    }

    private boolean handleKeys(ElementRenderingContext context, KeyboardKey key, boolean controlPressed) {
        switch (key) {
            case BACKSPACE -> {
                if (pointer == 0) return true;
                if (pointer == -1) pointer = typedText.length();

                String tr = typedText.substring(0, pointer);
                String after = typedText.substring(pointer);

                if (tr.trim().isEmpty()) tr = "";

                if (controlPressed) {
                    if (tr.endsWith(" ")) {
                        tr = tr.stripTrailing();
                        pointer--;
                    }

                    int lastSpaceIndex = tr.lastIndexOf(" ");
                    if (lastSpaceIndex == -1) {
                        pointer = 0;
                        tr = "";
                    } else {
                        pointer -= tr.substring(lastSpaceIndex).length() - 1;
                        tr = tr.substring(0, lastSpaceIndex + 1);
                    }
                    //hhhh hhhhhh hhhhhhhh hhhhhh
                } else {
                    tr = tr.substring(0, tr.length() - 1);
                    pointer--;
                }

                typedText = tr + after;
            }

            case ARROW_LEFT -> {
                if (controlPressed) {
                    if(pointer == -1) {
                        pointer = this.typedText.lastIndexOf(' ') + 1;
                    } else {
                        while (this.typedText.charAt(pointer - 1) == ' ') pointer--;

                        if (pointer > 0) pointer = this.typedText.substring(0, pointer).lastIndexOf(' ') + 1;
                    }
                } else {
                    if(pointer == -1) {
                        pointer = this.typedText.length() - 1;
                    } else {
                        if (pointer > 0) pointer--;
                    }
                }
            }

            case ARROW_RIGHT -> {
                if (controlPressed) {
                    if(pointer == -1) {
                        pointer = this.typedText.length();
                    } else {
                        if (pointer < this.typedText.length()) pointer += this.typedText.substring(pointer + 1).concat(/*concat a " " to guarantee space*/" ").indexOf(' ') + 1;
                        if (pointer > this.typedText.length()) pointer = this.typedText.length();
                    }
                } else {
                    if(pointer == -1) {
                        pointer = this.typedText.length();
                    } else {
                        if (pointer < this.typedText.length()) pointer++;
                    }
                }
            }

            case ENTER -> {
                if (multiline) {
                    keyboardPress(context, '\n');
                } else {
                    this.document.focusedNode = null;
                }
            }
        }
        return false;
    }
}
