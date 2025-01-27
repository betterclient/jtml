package io.github.betterclient.jtml.api.elements;

import io.github.betterclient.jtml.internal.css.styles.Border;
import io.github.betterclient.jtml.internal.css.styles.ColorParser;
import io.github.betterclient.jtml.internal.css.styles.TextDecoration;
import io.github.betterclient.jtml.internal.elements.HTMLCanvasElement;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CanvasRenderingContext2D {
    private final HTMLCanvasElement canvas;
    private int color = -1;
    private float textSize = 9f;
    private String lastColorString = "white";
    private final List<Consumer<ElementRenderingContext>> consumers = new ArrayList<>();

    CanvasRenderingContext2D(HTMLCanvasElement canvas) {
        this.canvas = canvas;
        this.canvas.renderEvent = elementRenderingContext -> {
            for (Consumer<ElementRenderingContext> consumer : consumers) {
                consumer.accept(elementRenderingContext);
            }
        };

        this.clear();
    }

    /**
     * Set the color for future renderings by the canvas.
     * @param color color (CSS)
     */
    public void setColor(String color) {
        this.lastColorString = color;
        this.color = ColorParser.calculateColor(color).getRGB();
    }

    /**
     * Set the font size for future renderings by the canvas.
     * @param size size (CSS)
     */
    public void setFontSize(String size) {
        this.textSize = this.canvas.parser.parse(size);
    }

    /**
     * Fill the given rectangle
     * @param x left
     * @param y top
     * @param width width
     * @param height height
     */
    public void fillRectangle(int x, int y, int width, int height) {
        int color = this.color;
        this.consumers.add(
                context -> context.fill(
                        x,
                        y,
                        width,
                        height,
                        color
                )
        );
    }

    /**
     * Outline the given rectangle
     * @param x left
     * @param y top
     * @param width width
     * @param height height
     */
    public void outlineRectangle(int x, int y, int width, int height) {
        //make the border renderer render it for us
        String color = this.lastColorString;
        this.consumers.add(context -> {
            Border border = new Border();
            border.setWidth("2px");
            border.setStyle("solid");
            border.setColor(color);

            border.render(
                    this.canvas.document,
                    context.context,
                    canvas.getX() + x, canvas.getY() + y,
                    width, height
            );
        });
    }

    /**
     * Render given text
     * @param text text
     * @param x left
     * @param y top
     */
    public void renderText(String text, int x, int y) {
        int color = this.color;
        float textSize = this.textSize;

        this.consumers.add(context ->
            context.context.renderText(
                    text,
                    canvas.getX() + x,
                    canvas.getY() + y,
                    color,
                    textSize,
                    new TextDecoration()
            )
        );
    }

    /**
     * Clear the canvas
     */
    public void clear() {
        this.consumers.clear();
    }
}
