package io.github.betterclient.htmlutil.internal.css;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CSSStyle {
    public Map<String, String> DEFAULTS_MAP = new HashMap<>(
            Map.ofEntries(
                    //-----TEXT-----
                    Map.entry(
                            "color",
                            "white"
                    ),
                    Map.entry(
                            "text-decoration",
                            "none"
                    ),
                    Map.entry(
                            "font-size",
                            "9px"
                    ),
                    Map.entry(
                            "text-align",
                            "none"
                    ),

                    //-----BOX-----
                    Map.entry(
                            "width",
                            "-1"
                    ),
                    Map.entry(
                            "height",
                            "-1"
                    ),
                    Map.entry(
                            "border",
                            "none"
                    ),

                    //-----BACKGROUND-----
                    Map.entry(
                            "background-color",
                            "transparent"
                    ),

                    //-----OTHER-----
                    Map.entry(
                            "display",
                            "inline"
                    ),
                    Map.entry(
                            "width-offset",
                            "2px"
                    ),

                    //-----FLEX-----
                    Map.entry(
                            "justify-content",
                            "left"
                    ),
                    Map.entry(
                            "flex-direction",
                            "row"
                    ),
                    Map.entry(
                            "align-items",
                            "left"
                    ),

                    //-----GRID-----
                    Map.entry(
                            "grid-template-columns",
                            "none"
                    ),
                    Map.entry(
                            "grid-template-rows",
                            "none"
                    ),
                    Map.entry(
                            "grid-column",
                            "auto"
                    ),
                    Map.entry(
                            "grid-row",
                            "auto"
                    ),


                    //-----MARGIN-----
                    Map.entry(
                            "margin",
                            "0"
                    ),
                    Map.entry(
                            "margin-top",
                            "0"
                    ),
                    Map.entry(
                            "margin-bottom",
                            "0"
                    ),
                    Map.entry(
                            "margin-left",
                            "0"
                    ),
                    Map.entry(
                            "margin-right",
                            "0"
                    ),

                    //-----PADDING-----
                    Map.entry(
                            "padding",
                            "0"
                    ),
                    Map.entry(
                            "padding-top",
                            "0"
                    ),
                    Map.entry(
                            "padding-bottom",
                            "0"
                    ),
                    Map.entry(
                            "padding-left",
                            "0"
                    ),
                    Map.entry(
                            "padding-right",
                            "0"
                    )
            )
    );

    //Default will inherit parent.
    public Map<String, String> MAP = new HashMap<>(
            Map.ofEntries(
                    //-----TEXT-----
                    Map.entry(
                            "color",
                            "default"
                    ),
                    Map.entry(
                            "text-decoration",
                            "none"
                    ),
                    Map.entry(
                            "font-size",
                            "default"
                    ),
                    Map.entry(
                            "text-align",
                            "none"
                    ),

                    //-----BOX-----
                    Map.entry(
                            "width",
                            "-1"
                    ),
                    Map.entry(
                            "height",
                            "-1"
                    ),
                    Map.entry(
                            "border",
                            "none"
                    ),

                    //-----BACKGROUND-----
                    Map.entry(
                            "background-color",
                            "transparent"
                    ),

                    //-----OTHER-----
                    Map.entry(
                            "display",
                            "inline"
                    ),
                    Map.entry(
                            "width-offset",
                            "default"
                    ),

                    //-----FLEX-----
                    Map.entry(
                            "justify-content",
                            "left"
                    ),
                    Map.entry(
                            "flex-direction",
                            "row"
                    ),
                    Map.entry(
                            "align-items",
                            "left"
                    ),

                    //-----GRID-----
                    Map.entry(
                            "grid-template-columns",
                            "none"
                    ),
                    Map.entry(
                            "grid-template-rows",
                            "none"
                    ),
                    Map.entry(
                            "grid-column",
                            "auto"
                    ),
                    Map.entry(
                            "grid-row",
                            "auto"
                    ),

                    //-----MARGIN-----
                    Map.entry(
                            "margin",
                            "0"
                    ),
                    Map.entry(
                            "margin-top",
                            "0"
                    ),
                    Map.entry(
                            "margin-bottom",
                            "0"
                    ),
                    Map.entry(
                            "margin-left",
                            "0"
                    ),
                    Map.entry(
                            "margin-right",
                            "0"
                    ),

                    //-----PADDING-----
                    Map.entry(
                            "padding",
                            "0"
                    ),
                    Map.entry(
                            "padding-top",
                            "0"
                    ),
                    Map.entry(
                            "padding-bottom",
                            "0"
                    ),
                    Map.entry(
                            "padding-left",
                            "0"
                    ),
                    Map.entry(
                            "padding-right",
                            "0"
                    )
            )
    );

    public final CSSStyle parent;

    public String calculate(String name) {
        CSSStyle style = this;

        while(style != null && style.MAP.get(name).equals("default")) {
            style = style.parent;
        }
        String out = style == null ? this.DEFAULTS_MAP.get(name) : style.MAP.get(name);
        if (out == null) out = this.DEFAULTS_MAP.get(name);

        return out;
    }
}
