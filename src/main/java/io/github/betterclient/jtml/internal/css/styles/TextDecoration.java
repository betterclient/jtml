package io.github.betterclient.jtml.internal.css.styles;

import io.github.betterclient.jtml.internal.css.CSSStyle;
import lombok.*;

@Getter @Setter @NoArgsConstructor
public class TextDecoration {
    private boolean underline, strikethrough, bold, italic;

    public static TextDecoration parse(CSSStyle style) {
        TextDecoration decoration = new TextDecoration();

        for (String s : style.calculate("text-decoration").toLowerCase().split(" ")) {
            switch (s.trim()) {
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

        for (String s : style.calculate("font-weight").toLowerCase().split(" ")) {
            switch (s.trim()) {
                case "bold":
                    decoration.setBold(true);
                    break;
                case "italic":
                    decoration.setItalic(true);
                    break;

                case "none": break;
                default:
                    throw new UnsupportedOperationException("Unsupported font-weight: " + s);
            }
        }

        return decoration;
    }
}
