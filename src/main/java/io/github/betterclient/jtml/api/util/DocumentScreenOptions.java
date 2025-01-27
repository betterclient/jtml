package io.github.betterclient.jtml.api.util;

import io.github.betterclient.jtml.api.elements.HTMLDocument;

/**
 * Options for {@link HTMLDocument#openAsScreen(DocumentScreenOptions)})}
 */
public record DocumentScreenOptions(boolean renderBackground, boolean closeOnEsc) { }
