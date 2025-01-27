package io.github.betterclient.jtml.internal.css.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSSCompiler {
    public static Map<String, CompiledStyleSheet> compile(String src) {
        Map<String, CompiledStyleSheet> compiled = new HashMap<>();
        String currentSelector = null;
        CompiledStyleSheet currentCSS = null;

        List<String> lines = new ArrayList<>(List.of(src.split("\n")));
        lines.replaceAll(CSSCompiler::remove2SlashComments);
        lines.replaceAll(CSSCompiler::remove1SlashComments);
        lines.replaceAll(string -> string.replace("{", "{\n"));
        lines.replaceAll(string -> string.replace("}", "\n}"));
        lines.removeIf(string -> string.trim().isEmpty());
        lines.replaceAll(string -> string.replace("\r", ""));
        if (IS_IN_MULTILINE_COMMENT) {
            throw new IllegalStateException("Comment is not closed (end of file)");
        }

        for (String line : lines) {
            line = line.trim();

            if (line.endsWith("{")) {
                currentSelector = line.substring(0, line.lastIndexOf('{')).trim();
                currentCSS = new CompiledStyleSheet();
                continue;
            } else if (line.endsWith("}")) {
                if (currentCSS == null) throw new NullPointerException("Can't end non existent selector");

                compiled.put(currentSelector, currentCSS);
                continue;
            }

            String[] split = line.split(":");
            if (currentCSS != null && split.length == 2) {
                String value = split[1].trim();
                value = value.substring(0, value.length() - 1);
                currentCSS.getProperties().put(split[0].trim(), value);
            }
        }

        return compiled;
    }

    //-----LINE REMOVER-----
    //from Z--

    static boolean IS_IN_MULTILINE_COMMENT = false;

    public static String remove2SlashComments(String string) {
        StringBuilder out = new StringBuilder();
        boolean wasLastSlash = false;
        boolean isInString = false;
        boolean wasLastBackSlash = false;
        for (char c : string.toCharArray()) {
            if (c == '"')  {
                if (wasLastBackSlash && isInString) {
                    out.append(c);
                    continue;
                }

                isInString = !isInString;
                wasLastBackSlash = false;
                out.append(c);
            } else if (c == '\\') {
                wasLastBackSlash = !wasLastBackSlash;
                out.append(c);
            } else {
                if (c != '/') {
                    out.append(c);
                } else {
                    if (isInString || wasLastBackSlash) {
                        out.append(c);
                        continue;
                    }

                    if (wasLastSlash) {
                        out = new StringBuilder(out.substring(0, out.length() - 1));
                        break;
                    } else {
                        wasLastSlash = true;
                        out.append(c);
                    }
                }
            }
        }
        return out.toString();
    }

    public static String remove1SlashComments(String e) {
        StringBuilder out = new StringBuilder();

        boolean wasLastStar = false;
        boolean wasLastSlash = false;
        boolean inString = false;
        boolean wasLastBackSlash = false;
        for(char v : e.toCharArray()) {
            if(IS_IN_MULTILINE_COMMENT) {
                if(wasLastStar && v == '/') {
                    IS_IN_MULTILINE_COMMENT = false;
                    wasLastStar = false;
                    continue;
                }
            } else {
                if(!inString && wasLastSlash && v == '*') {
                    IS_IN_MULTILINE_COMMENT = true;
                    wasLastSlash = false;
                    out = new StringBuilder(out.substring(0, out.length() - 1));
                    continue;
                }

                if(!wasLastBackSlash && v == '"') {
                    inString = !inString;
                }
            }

            wasLastStar = v == '*';
            wasLastSlash = v == '/';
            wasLastBackSlash = v == '\\';

            if(!IS_IN_MULTILINE_COMMENT) out.append(v);
        }

        return out.toString();
    }
}