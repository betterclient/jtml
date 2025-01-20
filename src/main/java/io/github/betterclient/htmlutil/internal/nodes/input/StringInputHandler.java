package io.github.betterclient.htmlutil.internal.nodes.input;

import io.github.betterclient.htmlutil.internal.nodes.FocusableElement;
import io.github.betterclient.htmlutil.internal.render.ElementRenderingContext;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

//TODO: shift and selection
//TODO: cutting and copying
public class StringInputHandler {
    public final FocusableElement owner;

    @Getter @Setter public String typedText;
    @Getter public String displayedText = "";

    protected int pointer = -1;
    protected int pointerWidth = 0;

    public StringInputHandler(FocusableElement owner, String typedText) {
        this.typedText = typedText;
        this.owner = owner;
    }

    public void reloadDisplay(ElementRenderingContext context) {
        if (typedText == null) typedText = "";
        context.currentlyRendered = this.owner;

        int length = typedText.length();
        int displayWidth = this.owner.getDimensions(context).width - 4;

        int pointer = this.pointer;
        if (pointer == -1) {
            pointer = length;
        }

        int start = pointer;
        int end = pointer;

        while (start > 0 || end < length) {
            String substring = typedText.substring(start, end);
            if (context.width(substring) <= displayWidth) {
                displayedText = substring;
                if (start > 0) start--;
                if (end < length) end++;
            } else {
                break;
            }
        }

        displayedText = typedText.substring(start, end);
    }

    public void renderVerticalLine(int width, ElementRenderingContext context) {
        if (pointer == -1 || pointer == typedText.length()) {
            context.fillVerticalLine(width);
        } else {
            context.fillVerticalLine(
                    Math.min(
                            context.width(typedText.substring(
                                    Math.min(
                                            typedText.indexOf(displayedText),
                                            pointer
                                    ),
                                    Math.max(
                                            pointer,
                                            typedText.indexOf(displayedText)
                                    ))
                            ),
                            width - 4
                    )
            );
        }
    }

    public void type(ElementRenderingContext context, char character, boolean controlPressed) {
        String type = character + "";
        if (type.toLowerCase().charAt(0) == 'v' && controlPressed) {
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
    }

    public void backspace(boolean controlPressed) {
        if (pointer == 0) return;
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

    public void leftArrow(boolean controlPressed) {
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

    public void rightArrow(boolean controlPressed) {
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

        if (this.pointer == this.typedText.length()) this.pointer = -1;
    }

    public void enter(ElementRenderingContext context) {
        this.owner.unFocus();
    }

    public void reset() {
        this.pointer = -1;
        this.pointerWidth = 0;
    }
}