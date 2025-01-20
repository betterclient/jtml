package io.github.betterclient.jtml.service;

public interface DocumentScreen {
    /**
     * Rendering function
     * @param context context that can render
     * @param renderBackground render a background
     * @param delta time since last call
     * @param mouseX mouseX
     * @param mouseY mouseY
     */
    void render(RenderingService context, Runnable renderBackground, float delta, double mouseX, double mouseY);

    /**
     * Call this when the screen first opens or when the screen width/height changes
     */
    void init();

    /**
     * Should the screen close when "esc" is clicked
     * @return whether it should close
     */
    boolean shouldCloseOnEsc();

    /**
     * MouseClick event
     * @param mouseX mouse position
     * @param mouseY mouse position
     * @param button button (0 == left, 1 == right, 2 == middle)
     */
    void mouseClicked(double mouseX, double mouseY, int button);

    /**
     * MouseRelease event
     * @param mouseX mouse position
     * @param mouseY mouse position
     * @param button button (0 == left, 1 == right, 2 == middle)
     */
    void mouseReleased(double mouseX, double mouseY, int button);

    /**
     * MouseScroll event
     * @param mouseX mouse position
     * @param mouseY mouse position
     * @param direction direction (-1=down, 1=up)
     */
    void mouseScroll(double mouseX, double mouseY, int direction);

    /**
     * Keyboard char type event
     * @param chr char typed
     */
    void charTyped(char chr);

    /**
     * Keyboard key press event (should include char type events)
     * @param keyCode keycode of press
     * @param scanCode scancode of press
     */
    void keyPressed(int keyCode, int scanCode);

    /**
     * Keyboard key release event (should include char type events)
     * @param keyCode keycode of release
     * @param scanCode scancode of release
     */
    void keyReleased(int keyCode, int scanCode);
}
