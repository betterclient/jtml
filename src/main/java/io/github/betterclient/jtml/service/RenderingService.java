package io.github.betterclient.jtml.service;

import io.github.betterclient.jtml.api.event.KeyboardKey;
import io.github.betterclient.jtml.internal.css.styles.TextDecoration;

import java.awt.*;

/**
 * Rendering service for jtml
 */
public interface RenderingService {
    /**
     * Render text
     * @param text text to render
     * @param x x position (left)
     * @param y y position (top)
     * @param color color (RGBA)
     * @param size 9 should be normal text size
     * @param decoration decoration elements of text
     * @return width of text
     */
    int renderText(String text, int x, int y, int color, float size, TextDecoration decoration);

    /**
     * Get the width of given text
     * @param text given text
     * @return width of text
     */
    @AlwaysWork
    int width(String text, TextDecoration decoration);

    /**
     * Fill a rectangle
     * @param x left
     * @param y top
     * @param endX right
     * @param endY bottom
     * @param color color(RGBA)
     */
    void fill(float x, float y, float endX, float endY, int color);

    /**
     * Fill a rounded rectangle
     * @param x left
     * @param y top
     * @param endX right
     * @param endY bottom
     * @param color color (RGBA)
     * @param radius radius of rect
     */
    void fillRound(float x, float y, float endX, float endY, int color, float radius);

    /**
     * Return the corresponding key
     * @param code code of key
     * @return key
     */
    @AlwaysWork
    KeyboardKey getKey(int code);

    /**
     * Get the screen width
     * @return screen width
     */
    @AlwaysWork
    int scrWidth();

    /**
     * Get the screen height
     * @return screen height
     */
    @AlwaysWork
    int scrHeight();

    /**
     * Get the string on the clipboard
     * This will only be used if a HeadlessException is thrown by {@link Toolkit#getDefaultToolkit()}
     * @apiNote Don't throw exceptions from this.
     * @return clipboard
     */
    @AlwaysWork
    String getClipboard();

    /**
     * Start scissoring rendered stuff
     * @param x left
     * @param y top
     * @param width width
     * @param height height
     */
    void startScissor(int x, int y, int width, int height);

    /**
     * End scissoring
     * MUST WORK WITH SCISSORING DISABLED TOO
     */
    void endScissor();

    /**
     * Put the given text in the clipboard
     * @param text text
     */
    @AlwaysWork
    void setClipboard(String text);
}
