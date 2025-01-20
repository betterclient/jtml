package io.github.betterclient.jtml.service;

/**
 * JTML Service
 */
public interface JTMLService {
    /**
     * Will be used by the renderer at events to use the FontRenderer
     * This service is only going to be used for functions marked with {@link AlwaysWork}
     * @return a rendering service incapable of rendering
     */
    RenderingService getFontService();

    UtilityService getUtilityService();

    /**
     * Open a {@link DocumentScreen}
     */
    void openScreen(DocumentScreen document);
}