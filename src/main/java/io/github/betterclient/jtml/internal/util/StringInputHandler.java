package io.github.betterclient.jtml.internal.util;

import io.github.betterclient.jtml.internal.nodes.FocusableElement;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringInputHandler {
    public final FocusableElement owner;

    @Getter
    @Setter
    public String typedText;
    @Getter
    public String displayedText = "";
    public Pattern allowedCharacterRegex = Pattern.compile(".");

    protected int pointer = -1;
    protected int pointerWidth = 0;
    protected List<String> lastTypedTexts = new ArrayList<>();

    public StringInputHandler(FocusableElement owner, String typedText) {
        this.typedText = typedText;
        this.lastTypedTexts.add(typedText);
        this.owner = owner;
    }

    public void reloadDisplay(ElementRenderingContext context) {
        if (typedText == null) typedText = "";

        StringBuilder newTypedText = new StringBuilder();
        for (char c : typedText.toCharArray()) {
            if (this.allowedCharacterRegex.matcher(c + "").matches()) {
                newTypedText.append(c);
            }
        }
        typedText = newTypedText.toString();

        context.currentlyRendered = this.owner;

        int length = typedText.length();
        int displayWidth = this.owner.getDimensions(context).width - 4;

        int pointer = this.pointer;
        if (pointer == -1) {
            pointer = length;
        }
        pointer = Math.min(pointer, typedText.length());

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
        pointer = Math.min(pointer, typedText.length());

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
                //make the service get it since we can't
                type = context.context.getClipboard();
            }
        } else if (type.toLowerCase().charAt(0) == 'c' && controlPressed) {
            //copy selection
            if (pointerWidth == 0) return;

            pointer = Math.min(this.pointer, typedText.length());
            int start = pointer;
            int end = pointer + pointerWidth;

            if (pointerWidth < 0) {
                start = pointer + pointerWidth;
                end = pointer;
            }

            start = Math.max(0, start);
            end = Math.min(typedText.length(), end);

            context.context.setClipboard(
                    typedText.substring(start, end)
            );

            this.pointerWidth = 0;
            return;
        } else if (type.toLowerCase().charAt(0) == 'x' && controlPressed) {
            //cut selection
            if (pointerWidth == 0) return;
            this.lastTypedTexts.add(typedText);

            pointer = Math.min(this.pointer, typedText.length());
            int start = pointer;
            int end = pointer + pointerWidth;

            if (pointerWidth < 0) {
                start = pointer + pointerWidth;
                end = pointer;
            }

            start = Math.max(0, start);
            end = Math.min(typedText.length(), end);

            context.context.setClipboard(
                    typedText.substring(start, end)
            );

            typedText = typedText.substring(0, start) + typedText.substring(end);
            this.pointerWidth = 0;
            return;
        } else if (type.toLowerCase().charAt(0) == 'z' && controlPressed) {
            //undo
            if (lastTypedTexts.isEmpty()) return;

            typedText = lastTypedTexts.removeLast();

            return;
        } else if (type.toLowerCase().charAt(0) == 'a' && controlPressed) {
            //select all
            pointer = 0;
            pointerWidth = typedText.length();
            return;
        }
        this.lastTypedTexts.add(typedText);

        if (pointerWidth != 0 && !typedText.isEmpty()) {
            pointer = Math.min(this.pointer, typedText.length());
            int start = pointer;
            int end = pointer + pointerWidth;

            if (pointerWidth < 0) {
                start = pointer + pointerWidth;
                end = pointer;
            }

            start = Math.max(0, start);
            end = Math.min(typedText.length(), end);

            typedText = typedText.substring(0, start) + type + typedText.substring(end);
            pointerWidth = 0;
            return;
        }

        pointer = Math.min(this.pointer, typedText.length());
        int pointer = this.pointer < 0 ? typedText.length() : this.pointer;
        typedText = typedText.substring(0, pointer) + type + typedText.substring(pointer);

        if (this.pointer >= 0) this.pointer += type.length();
    }

    public void backspace(boolean controlPressed) {
        if (pointer == -1) pointer = typedText.length();
        this.lastTypedTexts.add(typedText);
        pointer = Math.min(typedText.length(), pointer);

        if (pointerWidth != 0 && !typedText.isEmpty()) {
            int start = pointer;
            int end = pointer + pointerWidth;

            if (pointerWidth < 0) {
                start = pointer + pointerWidth;
                end = pointer;
            }

            start = Math.max(0, start);
            end = Math.min(typedText.length(), end);

            typedText = typedText.substring(0, start) + typedText.substring(end);
            pointerWidth = 0;
            return;
        }

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
            if (tr.isEmpty()) return; //fuck you

            tr = tr.substring(0, tr.length() - 1);
            pointer--;
        }

        typedText = tr + after;

        pointerWidth = 0;
    }

    public void leftArrow(boolean controlPressed, boolean shiftPressed) {
        int startPointer = pointer == -1 ? typedText.length() : pointer;
        if (controlPressed) {
            if (pointer == -1) {
                pointer = this.typedText.lastIndexOf(' ') + 1;
            } else {
                while (pointer > 0 && this.typedText.charAt(pointer - 1) == ' ') pointer--;

                if (pointer > 0) pointer = this.typedText.substring(0, pointer).lastIndexOf(' ') + 1;
            }
        } else {
            if (pointer == -1) {
                pointer = this.typedText.length() - 1;
            } else {
                if (pointer > 0) pointer--;
            }
        }

        if (shiftPressed) {
            pointerWidth -= pointer - startPointer;
        } else {
            pointerWidth = 0;
        }
    }

    public void rightArrow(boolean controlPressed, boolean shiftPressed) {
        int startPointer = pointer == -1 ? typedText.length() : pointer;
        if (controlPressed) {
            if (pointer == -1) {
                pointer = this.typedText.length();
            } else {
                if (pointer < this.typedText.length())
                    pointer += this.typedText.substring(pointer + 1).concat(/*concat a " " to guarantee space*/" ").indexOf(' ') + 1;
                if (pointer > this.typedText.length()) pointer = this.typedText.length();
            }
        } else {
            if (pointer == -1) {
                pointer = this.typedText.length();
            } else {
                if (pointer < this.typedText.length()) pointer++;
            }
        }

        if (this.pointer == this.typedText.length()) this.pointer = -1;

        if (shiftPressed) {
            pointerWidth -= pointer - startPointer;
        } else {
            pointerWidth = 0;
        }
    }

    public void enter(ElementRenderingContext context) {
        this.owner.unFocus();
    }

    public void reset() {
        this.pointer = -1;
        this.pointerWidth = 0;
    }

    public void renderSelection(ElementRenderingContext context) {
        if (pointerWidth == 0) return;
        int pointer = this.pointer == -1 ? typedText.length() : this.pointer;
        int start, end;
        if (pointer == typedText.length()) {
            start = pointer - pointerWidth;
            end = pointer;
        } else {
            start = pointer;
            end = pointer + pointerWidth;
        }

        if (pointerWidth < 0) {
            start = pointer + pointerWidth;
            end = pointer;
        }
        start = Math.max(0, start);
        end = Math.min(typedText.length(), end);

        String starte = typedText.substring(0, start);

        int displayTextPosStart = context.width(starte);
        int displayTextPosEnd = context.width(typedText.substring(
                start,
                end
        )) - (start > displayedText.length() ? context.width(displayedText) : 0);

        context.fill(displayTextPosStart, 0, displayTextPosEnd, 10, -1);
    }
}