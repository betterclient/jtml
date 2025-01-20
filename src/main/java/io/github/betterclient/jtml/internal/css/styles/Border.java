package io.github.betterclient.jtml.internal.css.styles;

import io.github.betterclient.jtml.internal.css.CSSStyle;
import io.github.betterclient.jtml.internal.css.StyleParser;
import io.github.betterclient.jtml.internal.elements.HTMLDocument;
import io.github.betterclient.jtml.service.RenderingService;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter @Setter
public class Border {
    private String style;
    private String width;
    private String color;

    public static Border parseBorderElement(CSSStyle style) {
        Border border = new Border();

        String propertyInEgypt = style.calculate("border").trim();
        if (propertyInEgypt.equals("none")) {
            //They didn't give me the property.
            border.setStyle("none");
            border.setColor("transparent");
            border.setWidth("0");
            return border;
        }

        String[] parts = propertyInEgypt.split("\\s+");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid border");
        }

        border.setWidth(parts[0]);
        border.setStyle(parts[1]);
        border.setColor(parts[2]);

        return border;
    }

    public void render(HTMLDocument document, RenderingService context, int x, int y, int width, int height) {
        if (this.getWidth().equals("0")) return;
        StyleParser parser = new StyleParser(document, null);

        switch (this.getStyle().toLowerCase()) {
            case "solid":
                renderSolidBorder(context,
                        x, y, width, height,
                        parser.parse(this.getWidth()),
                        ColorParser.calculateColor(this.getColor())
                );
                break;
            case "dashed":
                renderDashedBorder(context,
                        x, y, width, height,
                        parser.parse(this.getWidth()),
                        ColorParser.calculateColor(this.getColor())
                );
                break;
            case "dotted":
                renderDottedBorder(context,
                        x, y, width, height,
                        (int) parser.parse(this.getWidth()),
                        ColorParser.calculateColor(this.getColor()).getRGB()
                );
                break;
            default:
                throw new UnsupportedOperationException("Unsupported border mode \"" + style + "\"");
        }
    }

    private void renderDottedBorder(RenderingService context, int x, int y, int width, int height, int borderWidth, int color) {
        int gap = borderWidth * 2;

        renderDottedLine(context, x, y, x + width, y + borderWidth, borderWidth, gap, color);
        renderDottedLine(context, x, y + height - borderWidth, x + width, y + height, borderWidth, gap, color);
        renderDottedLineVertical(context, x, y + borderWidth, x + borderWidth, y + height - borderWidth, borderWidth, gap, color);
        renderDottedLineVertical(context, x + width - borderWidth, y + borderWidth, x + width, y + height - borderWidth, borderWidth, gap, color);
    }

    private void renderDashedBorder(RenderingService context, int x, int y, int width, int height, float borderWidth, Color color) {
        int dashLength = (int) (borderWidth * 3);
        int gapLength = (int) (borderWidth * 2);

        renderDashedLine(context, x, y, x + width, (int) (y + borderWidth), dashLength, gapLength, color.getRGB());
        renderDashedLine(context, x, (int) (y + height - borderWidth), x + width, y + height, dashLength, gapLength, color.getRGB());
        renderDashedLineVertical(context, x, (int) (y + borderWidth), (int) (x + borderWidth), (int) (y + height - borderWidth), dashLength, gapLength, color.getRGB());
        renderDashedLineVertical(context, (int) (x + width - borderWidth), (int) (y + borderWidth), x + width, (int) (y + height - borderWidth), dashLength, gapLength, color.getRGB());
    }

    private void renderSolidBorder(RenderingService context, int x, int y, int width, int height, float borderWidth, Color color) {
        context.fill(x, y, x + width, y + borderWidth, color.getRGB());
        context.fill(x, y + height - borderWidth, x + width, y + height, color.getRGB());
        context.fill(x, y + borderWidth, x + borderWidth, y + height - borderWidth, color.getRGB());
        context.fill(x + width - borderWidth, y + borderWidth, x + width, y + height - borderWidth, color.getRGB());
    }

    //-----------UTILITY-----------

    private void renderDottedLine(RenderingService context, int x, int y, int endX, int endY, int dot, int gap, int color) {
        while (x < endX) {
            int dotEndX = Math.min(x + dot, endX);
            context.fill(x, y, dotEndX, endY, color);
            x += dot + gap;
        }
    }

    private void renderDottedLineVertical(RenderingService context, int x, int y, int endX, int endY, int dot, int gap, int color) {
        while (y < endY) {
            int dotEndY = Math.min(y + dot, endY);
            context.fill(x, y, endX, dotEndY, color);
            y += dot + gap;
        }
    }

    private void renderDashedLineVertical(RenderingService context, int x, int y, int endX, int endY, int size, int gap, int color) {
        while (y < endY) {
            int dotEndY = Math.min(y + size, endY);
            context.fill(x, y, endX, dotEndY, color);
            y += size + gap;
        }
    }

    private void renderDashedLine(RenderingService context, int x, int y, int endX, int endY, int dash, int gap, int color) {
        while (y < endY) {
            int dashEndY = Math.min(y + dash, endY);
            context.fill(x, y, endX, dashEndY, color);
            y += dashEndY + gap;
        }
    }
}