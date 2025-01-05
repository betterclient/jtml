package io.github.betterclient.htmlutil.api.event;

@FunctionalInterface
public interface MouseClickHandler {
    void click(MouseClickEvent event);
}
