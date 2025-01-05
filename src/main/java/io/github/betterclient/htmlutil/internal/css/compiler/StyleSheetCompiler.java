package io.github.betterclient.htmlutil.internal.css.compiler;

import io.github.betterclient.htmlutil.internal.elements.HTMLDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * Compiler/Parser for .css files
 */
public class StyleSheetCompiler {
    public static Map<String, CompiledStyleSheet> compile(String src) {
        Map<String, CompiledStyleSheet> compilation = new HashMap<>();
        String currentSelector = null;
        CompiledStyleSheet currentRule = null;

        for (String line : src.split("\n")) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("/*") || line.startsWith("//")) {
                continue;
            }

            if (line.endsWith("{")) {
                currentSelector = line.substring(0, line.length() - 1).trim();
                currentRule = new CompiledStyleSheet(currentSelector);
            } else if (line.equals("}")) {
                if (currentRule != null) {
                    compilation.put(currentSelector, currentRule);
                }
                currentSelector = null;
                currentRule = null;
            } else if (currentRule != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String property = parts[0].trim();
                    String value = parts[1].trim().replace(";", "");
                    currentRule.getProperties().put(property, value);
                }
            }
        }

        return compilation;
    }
}
