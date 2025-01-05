package io.github.betterclient.htmlutil.api;

import io.github.betterclient.htmlutil.api.elements.HTMLDocument;

/**
 * Options for {@link HTMLDocument#openAsScreen(DocumentScreenOptions)})}
 */
public record DocumentScreenOptions(boolean renderBackground, boolean closeOnEsc) { }
