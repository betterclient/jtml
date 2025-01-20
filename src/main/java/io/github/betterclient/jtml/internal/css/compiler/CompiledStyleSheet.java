package io.github.betterclient.jtml.internal.css.compiler;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CompiledStyleSheet {
    private final Map<String, String> properties;

    public CompiledStyleSheet() {
        this.properties = new HashMap<>();
    }
}
