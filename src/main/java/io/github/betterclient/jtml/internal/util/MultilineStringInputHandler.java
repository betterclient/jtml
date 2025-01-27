package io.github.betterclient.jtml.internal.util;

import io.github.betterclient.jtml.internal.nodes.FocusableElement;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;

public class MultilineStringInputHandler extends StringInputHandler {
    public MultilineStringInputHandler(FocusableElement owner, String typedText) {
        super(owner, typedText);
    }

    @Override
    public void reloadDisplay(ElementRenderingContext context) {
        if (typedText == null) typedText = "";
        context.currentlyRendered = this.owner;

        StringBuilder currentText = new StringBuilder();

        int curX = 0;
        int maxW = this.owner.getDimensions(context).width - 4;

        for (String s : typedText.split("\n")) {
            for (char c : s.toCharArray()) {
                curX += context.width(Character.toString(c));
                if (curX >= maxW) {
                    currentText.append("\n");
                    curX = 0;
                }
                currentText.append(c);
            }

            currentText.append("\n");
            curX = 0;
        }

        displayedText = currentText.toString();
    }
    
    @Override
    public void renderVerticalLine(int width, ElementRenderingContext context) {
        pointer = Math.min(pointer, typedText.length());

        int index = this.pointer == -1 ? displayedText.length() : this.pointer;
        String pointing = displayedText.substring(0, Math.min(index, displayedText.length()));
        if (pointing.isEmpty()) return;

        int y = (int) (pointing.split("\n").length * this.owner.parser.getSize("font-size"));
        int x = context.width(pointing.lines().toList().getLast());
        context.fillVerticalLine(x, y);
    }

    @Override
    public void enter(ElementRenderingContext context) {
        this.lastTypedTexts.add(typedText);
        this.type(context, '\n', false);
    }

    @Override
    public void renderSelection(ElementRenderingContext context) {
        if (pointerWidth == 0) return;
        int pointer = this.pointer == -1 ? displayedText.length() : this.pointer;

        int start = pointer - Math.min(pointerWidth, 0);
        int end = pointer + Math.max(pointerWidth, 0);
        end = Math.min(end, displayedText.length());

        int temp = start;
        if (end < start) {
            start = end;
            end = temp;
        }

        float size = owner.parser.getSize("font-size");
        String[] split = displayedText.substring(0, start).split("\n");
        int index = split.length;
        int acIndex = 0;
        for (String s : displayedText.substring(start, end).split("\n")) {
            int yStart = (int) (index * size - size);
            int yEnd = (int) size;
            String concatStart = acIndex == 0 ? split[split.length - 1] : "";

            context.fill(context.width(concatStart), yStart, context.width(concatStart + s), yEnd, -1);

            index++;
            acIndex++;
        }
    }
}