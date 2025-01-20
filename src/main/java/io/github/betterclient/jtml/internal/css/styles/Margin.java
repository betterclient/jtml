package io.github.betterclient.jtml.internal.css.styles;

import io.github.betterclient.jtml.internal.css.StyleParser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Margin {
    private float left, top, right, bottom;

    public static Margin getMargin(StyleParser parser) {
        Margin margin = new Margin();
        String propertyInEgyptYuh = parser.style().calculate("margin");

        if (!propertyInEgyptYuh.equals("0")) {
            String[] values = propertyInEgyptYuh.split(" ");

            float v0 = parser.parse(values[0]);
            switch (values.length) {
                case 1:
                    margin.setTop(v0);
                    margin.setLeft(v0);
                    margin.setRight(v0);
                    margin.setBottom(v0);
                    break;
                case 2:
                    float v1 = parser.parse(values[1]);
                    margin.setTop(v0);
                    margin.setLeft(v1);
                    margin.setRight(v1);
                    margin.setBottom(v0);
                    break;
                case 3:
                    float v_1 = parser.parse(values[1]);
                    margin.setTop(v0);
                    margin.setLeft(v_1);
                    margin.setRight(v_1);
                    margin.setBottom(parser.parse(values[2]));
                    break;
                case 4:
                    margin.setTop(v0);
                    margin.setRight(parser.parse(values[1]));
                    margin.setBottom(parser.parse(values[2]));
                    margin.setLeft(parser.parse(values[3]));
            }
        } else {
            margin.setTop(parser.getSize("margin-top"));
            margin.setRight(parser.getSize("margin-right"));
            margin.setLeft(parser.getSize("margin-left"));
            margin.setBottom(parser.getSize("margin-bottom"));
        }

        return margin;
    }
}