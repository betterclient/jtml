package io.github.betterclient.htmlutil.internal.css.styles;

import io.github.betterclient.htmlutil.internal.css.StyleParser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Padding {
    private float left, top, right, bottom;

    public static Padding getPadding(StyleParser parser) {
        Padding padding = new Padding();
        String propertyInEgyptYuh = parser.style().calculate("padding");

        if (!propertyInEgyptYuh.equals("0")) {
            String[] values = propertyInEgyptYuh.split(" ");

            float v0 = parser.parse(values[0]);
            switch (values.length) {
                case 1:
                    padding.setTop(v0);
                    padding.setLeft(v0);
                    padding.setRight(v0);
                    padding.setBottom(v0);
                    break;
                case 2:
                    float v1 = parser.parse(values[1]);
                    padding.setTop(v0);
                    padding.setLeft(v1);
                    padding.setRight(v1);
                    padding.setBottom(v0);
                    break;
                case 3:
                    float v_1 = parser.parse(values[1]);
                    padding.setTop(v0);
                    padding.setLeft(v_1);
                    padding.setRight(v_1);
                    padding.setBottom(parser.parse(values[2]));
                    break;
                case 4:
                    padding.setTop(v0);
                    padding.setRight(parser.parse(values[1]));
                    padding.setBottom(parser.parse(values[2]));
                    padding.setLeft(parser.parse(values[3]));
            }
        } else {
            padding.setTop(parser.getSize("padding-top"));
            padding.setRight(parser.getSize("padding-right"));
            padding.setLeft(parser.getSize("padding-left"));
            padding.setBottom(parser.getSize("padding-bottom"));
        }

        return padding;
    }
}