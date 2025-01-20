package io.github.betterclient.jtml.internal.css;

import io.github.betterclient.jtml.internal.css.styles.ColorParser;
import io.github.betterclient.jtml.internal.elements.HTMLDocument;
import io.github.betterclient.jtml.internal.render.ElementRenderingContext;

public record StyleParser(HTMLDocument document, CSSStyle style) {
    public int getColor() {
        String cValue = style.calculate("color");

        return ColorParser.calculateColor(cValue).getRGB();
    }

    public int getBackgroundColor() {
        String cValue = style.calculate("background-color");

        return ColorParser.calculateColor(cValue).getRGB();
    }

    public int getWidthOffset() {
        String cValue = style.calculate("width-offset");

        return Integer.parseInt(cValue.substring(0, cValue.length() - 2));
    }

    public float getSize(String s) {
        String out = style.calculate(s);
        if (out.contains("em")) {
            out = out.replace("em", "");
            float i = Float.parseFloat(out);
            return new StyleParser(document, style.parent).getSize(s) * i;
        }
        if (out.contains("vh")) {
            ElementRenderingContext context = new ElementRenderingContext(document.service.getFontService());
            out = out.replace("vh", "");
            float i = Float.parseFloat(out);

            if (s.equals("width")) {
                return context.screenWidth() * (i / 100);
            }

            if (s.equals("height")) {
                return context.screenHeight() * (i / 100);
            }
        }
        out = out.replace("px", "");
        return Float.parseFloat(out);
    }

    public float parse(String out) {
        out = out.replace("px", "");
        return Float.parseFloat(out);
    }
}
