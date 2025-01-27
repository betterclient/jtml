package io.github.betterclient.jtml.internal.util;

public class ElementDimensions {
    //These fields are practically final, they are only allowed to be changed for simplicity purposes
    //This class is always re-initiated every time
    public int width, height;

    public ElementDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
