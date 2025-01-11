package io.github.betterclient.htmlutil.internal.css.compiler;

import java.util.HashMap;
import java.util.Map;

public class CompiledStyleSheet {
    private final Map<String, String> properties;

    public CompiledStyleSheet() {
        this.properties = new HashMap<>();
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
