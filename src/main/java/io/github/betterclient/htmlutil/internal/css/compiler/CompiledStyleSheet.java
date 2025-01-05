package io.github.betterclient.htmlutil.internal.css.compiler;

import java.util.HashMap;
import java.util.Map;

public class CompiledStyleSheet {
    private final String selector;
    private final Map<String, String> properties;

    public CompiledStyleSheet(String selector) {
        this.selector = selector;
        this.properties = new HashMap<>();
    }

    public String getSelector() {
        return selector;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
