package io.github.betterclient.jtml.service;

public interface UtilityService {
    /**
     * Apply translation from html
     * @param string to translate
     * @return translated
     */
    String translate(String string);

    /**
     * Get a key name
     * Should return null for non-alphabetic keys
     * @apiNote Don't throw exceptions from this.
     * @return provided key's name (ex: "v")
     */
    String getKeyName(int keyCode, int scanCode);

    /**
     * @return The keyCode for the "space" key
     */
    int getSpaceKeyCode();

    /**
     * Is the control button down
     */
    boolean isControlDown();
}
