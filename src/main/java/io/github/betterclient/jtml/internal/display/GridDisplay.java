package io.github.betterclient.jtml.internal.display;

import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;

//TODO: rewrite this
public class GridDisplay extends DisplayMode {
    static final DisplayMode INSTANCE = new GridDisplay();

    public GridDisplay() {
        super("grid");
    }

    @Override
    public void loadPositions(HTMLNode<?> node, ElementRenderingContext context) {
        String gridTemplateColumns = node.style.calculate("grid-template-columns");
        String gridTemplateRows = node.style.calculate("grid-template-rows");
        String gridColumn = node.style.calculate("grid-column");
        String gridRow = node.style.calculate("grid-row");

        int[] columnSizes = template(gridTemplateColumns, context.screenWidth());
        int[] rowSizes = template(gridTemplateRows, context.screenHeight());
        int[] columnRange = placeGrid(gridColumn, columnSizes.length);
        int[] rowRange = placeGrid(gridRow, rowSizes.length);

        int x = move(columnSizes, columnRange);
        int y = move(rowSizes, rowRange);

        node.x = x;
        node.y = y;
    }

    private int[] template(String template, int totalSize) {
        if (template == null || template.isEmpty()) {
            throw new UnsupportedOperationException("Invalid grid template");
        }

        String[] parts = template.split("\\s+");
        int[] sizes = new int[parts.length];
        int remainingSize = totalSize;
        int autoCount = 0;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("auto")) {
                sizes[i] = -1;
                autoCount++;
                continue;
            } else if (parts[i].endsWith("px")) {
                sizes[i] = Integer.parseInt(parts[i].replace("px", ""));
            } else if (parts[i].endsWith("%")) {
                sizes[i] = (int) (totalSize * Double.parseDouble(parts[i].replace("%", "")) / 100);
            } else continue;

            remainingSize -= sizes[i];
        }

        if (autoCount > 0 && remainingSize > 0) {
            int autoSize = remainingSize / autoCount;
            for (int i = 0; i < sizes.length; i++) {
                if (sizes[i] == -1) {
                    sizes[i] = autoSize;
                }
            }
        }

        return sizes;
    }

    private int[] placeGrid(String placement, int maxIndex) {
        if (placement == null || placement.isEmpty()) {
            return new int[] { 0, 1 };
        }
        String[] parts = placement.split("/");
        int start = parsePlacementPart(parts[0].trim(), maxIndex);
        int end = parts.length > 1 ? parsePlacementPart(parts[1].trim(), maxIndex) : start + 1;
        return new int[] { start, end };
    }

    private int parsePlacementPart(String part, int maxIndex) {
        if (part.equals("auto")) {
            return maxIndex;
        }

        try {
            int value = Integer.parseInt(part);
            return Math.max(0, Math.min(value - 1, maxIndex));
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("Invalid grid placement value: " + part);
        }
    }

    private int move(int[] sizes, int[] range) {
        int position = 0;
        for (int i = 0; i < range[0]; i++) {
            position += sizes[i];
        }
        return position;
    }
}