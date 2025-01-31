package io.github.betterclient.jtml.internal.display;

import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;
import io.github.betterclient.jtml.internal.util.ElementDimensions;
import org.jsoup.nodes.Node;

public class GridDisplay extends DisplayMode {
    static final DisplayMode INSTANCE = new GridDisplay();

    public GridDisplay() {
        super("grid");
    }

    @Override
    public void loadPositions(HTMLNode<?> node, ElementRenderingContext context) {
        String gridTemplateColumns = node.style.calculate("grid-template-columns");
        String gridTemplateRows = node.style.calculate("grid-template-rows");
        float gridGap = node.parser.getSize("grid-gap");
        if (gridGap <= 0) gridGap = 0;

        ElementDimensions box = new ElementDimensions(
                node.width > 0 ? node.width : (context.screenWidth() - (44 * 2)),
                node.height > 0 ? node.height : (context.screenHeight() - (44 * 2))
        );

        int[] rowSizes = parseTemplate(gridTemplateRows, box.height, gridGap);
        int[] columnSizes = parseTemplate(gridTemplateColumns, box.width, gridGap);

        int indexC = 0;
        int currentX = 0;
        int indexR = 0;
        int currentY = 0;
        for (HTMLNode<? extends Node> child : node.children) {
            if (child.getDimensions(context).width == 0 && child.getDimensions(context).height == 0) {
                continue; //ignore empty nodes...
            }

            if (indexC >= columnSizes.length) {
                indexC = 0;
                currentX = 0;

                if (rowSizes[indexR] != box.height) currentY += (int) (rowSizes[indexR] + gridGap);
                else currentY += (int) (child.getDimensions(context).height + gridGap);

                indexR++;
            }

            if (indexR >= rowSizes.length) {
                indexR = 0;
            }

            child.x = currentX;
            child.y = currentY;

            child.width = columnSizes[indexC];
            if (rowSizes[indexR] != box.height) child.height = rowSizes[indexR];

            currentX += (int) (columnSizes[indexC] + gridGap);
            indexC++;
        }
    }

    private int[] parseTemplate(String gridTemplate, int max, float gridGap) {
        if (gridTemplate.equals("none")) return new int[] {max};

        String[] split = gridTemplate.split(" ");
        int remaining = max;
        int[] arr = new int[split.length];
        int index = 0;
        int totalFRS = 0;
        for (String s : split) {
            if (s.endsWith("fr")) {
                s = s.replace("fr", "");
                arr[index] = max * Integer.parseInt(s);
                totalFRS += Integer.parseInt(s);
            } else {
                s = s.replace("px", "");
                arr[index] = Integer.parseInt(s);
                remaining -= (int) (arr[index] + gridGap);
            }

            index++;
        }

        index = 0;
        for (int i : arr) {
            if (i >= max) {
                int wantedSize = i / max;
                arr[index] = (remaining / totalFRS) * wantedSize;
                remaining -= (int) gridGap;
            }

            index++;
        }

        return arr;
    }
}