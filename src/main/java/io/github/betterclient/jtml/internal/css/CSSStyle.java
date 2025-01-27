package io.github.betterclient.jtml.internal.css;

import io.github.betterclient.jtml.internal.nodes.HTMLNode;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CSSStyle {
    public Map<String, String> DEFAULTS_MAP = getDefaultsMap();
    //Default will inherit parent.
    public Map<String, String> MAP = getEmptyMap();

    public final CSSStyle parent;
    public final HTMLNode<?> owner;

    public String calculate(String name) {
        CSSStyle style = this;

        while(style != null && style.MAP.get(name).equals("default")) {
            style = style.parent;
        }
        String out = style == null ? this.DEFAULTS_MAP.get(name) : style.MAP.get(name);
        if (out == null) out = this.DEFAULTS_MAP.get(name);

        return out;
    }

    private static HashMap<String, String> getEmptyMap() {
        HashMap<String, String> map = new HashMap<>();

        //-----TEXT-----
        map.put("color", "default");
        map.put("text-decoration", "none");
        map.put("text-align", "none");
        map.put("font-size", "default");
        map.put("font-weight", "default");

        //-----BOX-----
        map.put("width", "-1");
        map.put("height", "-1");
        map.put("border", "none");
        map.put("border-radius", "0");

        //-----BACKGROUND-----
        map.put("background-color", "transparent");

        //-----OTHER-----
        map.put("display", "inline");
        map.put("width-offset", "default");

        //-----FLEX-----
        map.put("justify-content", "left");
        map.put("flex-direction", "row");
        map.put("align-items", "left");

        //-----GRID-----
        map.put("grid-template-columns", "none");
        map.put("grid-template-rows", "none");
        map.put("grid-column", "auto");
        map.put("grid-row", "auto");

        //-----MARGIN-----
        map.put("margin", "0");
        map.put("margin-top", "0");
        map.put("margin-bottom", "0");
        map.put("margin-left", "0");
        map.put("margin-right", "0");

        //-----PADDING-----
        map.put("padding", "0");
        map.put("padding-top", "0");
        map.put("padding-bottom", "0");
        map.put("padding-left", "0");
        map.put("padding-right", "0");

        return map;
    }

    private static HashMap<String, String> getDefaultsMap() {
        HashMap<String, String> map = new HashMap<>();

        //-----TEXT-----
        map.put("color", "white");
        map.put("text-decoration", "none");
        map.put("text-align", "none");
        map.put("font-size", "9px");
        map.put("font-weight", "none");

        //-----BOX-----
        map.put("width", "-1");
        map.put("height", "-1");
        map.put("border", "none");
        map.put("border-radius", "0");

        //-----BACKGROUND-----
        map.put("background-color", "transparent");

        //-----OTHER-----
        map.put("display", "inline");
        map.put("width-offset", "2px");

        //-----FLEX-----
        map.put("justify-content", "left");
        map.put("flex-direction", "row");
        map.put("align-items", "left");

        //-----GRID-----
        map.put("grid-template-columns", "none");
        map.put("grid-template-rows", "none");
        map.put("grid-column", "auto");
        map.put("grid-row", "auto");

        //-----MARGIN-----
        map.put("margin", "0");
        map.put("margin-top", "0");
        map.put("margin-bottom", "0");
        map.put("margin-left", "0");
        map.put("margin-right", "0");

        //-----PADDING-----
        map.put("padding", "0");
        map.put("padding-top", "0");
        map.put("padding-bottom", "0");
        map.put("padding-left", "0");
        map.put("padding-right", "0");

        return map;
    }
}
