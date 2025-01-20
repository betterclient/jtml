package io.github.betterclient.jtml.internal.css.styles;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorParser {
    public static final Map<String, Color> NAMED_COLORS = new HashMap<>();

    static {
        NAMED_COLORS.put("black", Color.BLACK);
        NAMED_COLORS.put("white", Color.WHITE);
        NAMED_COLORS.put("red", Color.RED);
        NAMED_COLORS.put("green", Color.GREEN);
        NAMED_COLORS.put("blue", Color.BLUE);
        NAMED_COLORS.put("yellow", new Color(255, 255, 0));
        NAMED_COLORS.put("cyan", Color.CYAN);
        NAMED_COLORS.put("magenta", Color.MAGENTA);
        NAMED_COLORS.put("gray", Color.GRAY);
        NAMED_COLORS.put("lightgray", Color.LIGHT_GRAY);
        NAMED_COLORS.put("darkgray", Color.DARK_GRAY);
        NAMED_COLORS.put("orange", new Color(255, 165, 0));
        NAMED_COLORS.put("pink", new Color(255, 192, 203));
        NAMED_COLORS.put("purple", new Color(128, 0, 128));
        NAMED_COLORS.put("brown", new Color(165, 42, 42));
        NAMED_COLORS.put("lime", new Color(0, 255, 0));
        NAMED_COLORS.put("olive", new Color(128, 128, 0));
        NAMED_COLORS.put("teal", new Color(0, 128, 128));
        NAMED_COLORS.put("navy", new Color(0, 0, 128));
        NAMED_COLORS.put("maroon", new Color(128, 0, 0));
        NAMED_COLORS.put("aqua", new Color(0, 255, 255));
        NAMED_COLORS.put("fuchsia", new Color(255, 0, 255));
        NAMED_COLORS.put("transparent", new Color(0, 0, 0, 0));
    }

    public static Color calculateColor(String color) {
        if (NAMED_COLORS.containsKey(color.toLowerCase())) {
            return NAMED_COLORS.get(color.toLowerCase());
        }

        if (color.startsWith("#")) {
            return parseHexColor(color);
        }

        if (color.startsWith("rgb")) {
            return parseRgbColor(color);
        }

        throw new IllegalArgumentException("Unsupported color format: " + color);
    }

    private static Color parseHexColor(String hex) {
        if (hex.length() == 4) { // #RGB
            int r = Integer.parseInt(String.valueOf(hex.charAt(1)) + hex.charAt(1), 16);
            int g = Integer.parseInt(String.valueOf(hex.charAt(2)) + hex.charAt(2), 16);
            int b = Integer.parseInt(String.valueOf(hex.charAt(3)) + hex.charAt(3), 16);
            return new Color(r, g, b);
        } else if (hex.length() == 7) { // #RRGGB
            int r = Integer.parseInt(hex.substring(1, 3), 16);
            int g = Integer.parseInt(hex.substring(3, 5), 16);
            int b = Integer.parseInt(hex.substring(5, 7), 16);
            return new Color(r, g, b);
        } else {
            throw new IllegalArgumentException("Invalid hex color format: " + hex);
        }
    }

    private static Color parseRgbColor(String rgb) {
        Pattern pattern = Pattern.compile(
                "rgba?\\s*\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})(?:\\s*,\\s*(\\d*\\.?\\d+))?\\s*\\)"
        );
        Matcher matcher = pattern.matcher(rgb);

        if (matcher.matches()) {
            int r = Integer.parseInt(matcher.group(1));
            int g = Integer.parseInt(matcher.group(2));
            int b = Integer.parseInt(matcher.group(3));
            float a = matcher.group(4) != null ? Float.parseFloat(matcher.group(4)) : 1.0f;

            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255 || a < 0.0f || a > 1.0f) {
                throw new IllegalArgumentException("Invalid rgb(a) color values: " + rgb);
            }

            return new Color(r, g, b, Math.round(a * 255));
        } else {
            throw new IllegalArgumentException("Invalid rgb(a) color format: " + rgb);
        }
    }
}