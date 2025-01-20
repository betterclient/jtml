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
        String pointing = displayedText.substring(0, this.pointer == -1 ? displayedText.length() : this.pointer);
        if (pointing.isEmpty()) return;

        int y = (int) (pointing.split("\n").length * this.owner.parser.getSize("font-size"));
        int x = context.width(pointing.lines().toList().getLast());
        context.fillVerticalLine(x, y);
    }

    @Override
    public void enter(ElementRenderingContext context) {
        this.type(context, '\n', false);
    }
}
