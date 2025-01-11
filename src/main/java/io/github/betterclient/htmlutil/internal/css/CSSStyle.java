package io.github.betterclient.htmlutil.internal.css;

import java.util.HashMap;
import java.util.Map;

public class CSSStyle {
    public Map<String, String> DEFAULTS_MAP = new HashMap<>(
            Map.ofEntries(
                    //-----TEXT-----
                    Map.entry(
                            "color",
                            "white"
                    ),
                    Map.entry(
                            "text-align",
                            "start"
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
                            "font-weight",
                            "normal"
                    ),

                    //-----BOX-----
                    Map.entry(
                            "width",
                            "auto"
                    ),
                    Map.entry(
                            "height",
                            "auto"
                    ),
                    Map.entry(
                            "margin",
                            "0"
                    ),
                    Map.entry(
                            "padding",
                            "0"
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
                    Map.entry(
                            "background-image",
                            "none"
                    ),
                    Map.entry(
                            "background-repeat",
                            "repeat"
                    ),

                    //-----OTHER-----
                    Map.entry(
                            "display",
                            "inline"
                    ),
                    Map.entry(
                            "position",
                            "static"
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
                            "text-align",
                            "default"
                    ),
                    Map.entry(
                            "text-decoration",
                            "default"
                    ),
                    Map.entry(
                            "font-size",
                            "default"
                    ),
                    Map.entry(
                            "font-weight",
                            "default"
                    ),

                    //-----BOX-----
                    Map.entry(
                            "width",
                            "default"
                    ),
                    Map.entry(
                            "height",
                            "default"
                    ),
                    Map.entry(
                            "margin",
                            "default"
                    ),
                    Map.entry(
                            "padding",
                            "default"
                    ),
                    Map.entry(
                            "border",
                            "default"
                    ),

                    //-----BACKGROUND-----
                    Map.entry(
                            "background-color",
                            "default"
                    ),
                    Map.entry(
                            "background-image",
                            "default"
                    ),
                    Map.entry(
                            "background-repeat",
                            "default"
                    ),

                    //-----OTHER-----
                    Map.entry(
                            "display",
                            "inline" //Reset display for every element.
                    ),
                    Map.entry(
                            "position",
                            "default"
                    ),
                    Map.entry(
                            "width-offset",
                            "default"
                    ),

                    //-----FLEX-----
                    Map.entry(
                            "justify-content",
                            "default"
                    ),
                    Map.entry(
                            "flex-direction",
                            "default"
                    ),
                    Map.entry(
                            "flex-wrap",
                            "default"
                    ),
                    Map.entry(
                            "align-items",
                            "default"
                    )
            )
    );

    public CSSStyle parent;

    public CSSStyle(CSSStyle parent) {
        this.parent = parent;
    }

    public String calculate(String name) {
        CSSStyle style = this;

        while(style != null && style.MAP.get(name).equals("default")) {
            style = style.parent;
        }
        String out = style == null ? this.DEFAULTS_MAP.get(name) : style.MAP.get(name);
        if (out.equals("auto")) out = "-1";

        return out;
    }
}
