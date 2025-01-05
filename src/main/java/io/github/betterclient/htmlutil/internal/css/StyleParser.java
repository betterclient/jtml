package io.github.betterclient.htmlutil.internal.css;

public record StyleParser(CSSStyle style) {
    public int getColor() {
        String cValue = style.calculate("color");

        return ColorParser.calculateColor(cValue).getRGB();
    }

    public int getBackgroundColor() {
        String cValue = style.calculate("background-color");

        return ColorParser.calculateColor(cValue).getRGB();
    }
}
