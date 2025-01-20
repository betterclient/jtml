package io.github.betterclient.jtml.api;

import io.github.betterclient.jtml.internal.css.CSSStyle;

public class ElementStyle {
    private final CSSStyle mirror;

    public ElementStyle(CSSStyle mirror) {
        this.mirror = mirror;
    }

    public void put(String key, String value) {
        mirror.MAP.put(key, value);
    }

    public String get(String key) {
        return mirror.calculate(key);
    }
}
