package io.github.betterclient.jtml.internal.css.styles;

import io.github.betterclient.jtml.internal.css.StyleParser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class BorderRadius {
    private float topRight, bottomLeft, bottomRight, topLeft;

    public static BorderRadius getBorderRadius(StyleParser parser) {
        BorderRadius radius = new BorderRadius();
        String propertyInEgyptYuh = parser.style().calculate("border-radius");

        if (!propertyInEgyptYuh.equals("0")) {
            String[] values = propertyInEgyptYuh.split(" ");

            float v0 = parser.parse(values[0]);
            switch (values.length) {
                case 1:
                    radius.setBottomLeft(v0);
                    radius.setBottomRight(v0);
                    radius.setTopLeft(v0);
                    radius.setTopRight(v0);

                    break;
                case 4:
                    radius.setTopLeft(v0);
                    radius.setBottomRight(parser.parse(values[1]));
                    radius.setBottomLeft(parser.parse(values[2]));
                    radius.setTopRight(parser.parse(values[3]));
            }
        } else {
            radius.setTopLeft(parser.getSize("border-top-left-radius"));
            radius.setTopRight(parser.getSize("border-top-right-radius"));
            radius.setBottomLeft(parser.getSize("border-bottom-left-radius"));
            radius.setBottomRight(parser.getSize("border-bottom-right-radius"));
        }

        return radius;
    }
}