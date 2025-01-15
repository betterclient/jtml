package io.github.betterclient.htmlutil.internal.css.styles;

import io.github.betterclient.htmlutil.internal.css.CSSStyle;
import lombok.*;

@Getter @Setter @NoArgsConstructor
public class TextDecoration {
    private boolean underline, strikethrough;

    public static TextDecoration parse(CSSStyle style) {
        TextDecoration decoration = new TextDecoration();
        for (String s : style.calculate("text-decoration").toLowerCase().split(" ")) {
            switch (s) {
                case "strikethrough":
                    decoration.setStrikethrough(true);
                    break;
                case "underline":
                    decoration.setUnderline(true);
                    break;

                case "none": break;
                default:
                    throw new UnsupportedOperationException("Unsupported text-decoration: " + s);
            }
        }

        return decoration;
    }
}
